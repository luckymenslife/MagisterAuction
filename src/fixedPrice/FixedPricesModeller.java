/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fixedPrice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author iLGiZ
 */
public class FixedPricesModeller 
{
    private List<InputData> inputDataSellers;
    private List<InputData> inputDataBuyers;
    private double costs;
    private double maxNum;

    public FixedPricesModeller(List<InputData> inputDataSellers, List<InputData> inputDataBuyers, double costs, double maxNum) {
        this.inputDataSellers = inputDataSellers;
        this.inputDataBuyers = inputDataBuyers;
        this.costs = costs;
        this.maxNum = maxNum;
    }
    
    private List<InputData> recalcSums(List<InputData> inputList, double constBuyers)
    {
        List<InputData> ret = new ArrayList<InputData>();
        double lastSum = constBuyers;
        for(int i = 0; i < inputList.size(); i++)
        {
            lastSum = lastSum + inputList.get(i).getNumber();
            ret.add(new InputData(inputList.get(i).getNumber(),inputList.get(i).getPrice(), inputList.get(i).getId(), lastSum));
        }
        return ret;
    }
    
    private void prepareBuyersList(double constBuyers)
    {
        Collections.sort(inputDataBuyers);
        Collections.reverse(inputDataBuyers);
        inputDataBuyers = recalcSums(inputDataBuyers, constBuyers);
        Collections.reverse(inputDataBuyers);
    }
    
    private double searchSellersPrice(List<InputData> inputList,double price, double num)
    {
        double ret = 1;
        for(int i = 0; i<inputList.size();i++)
        {
            if (price == inputList.get(i).getPrice())
            {
                InputData id = inputList.remove(i);
                //id.setNumber(id.getNumber()+num);
                //id.setId(1);
                ret = num/(id.getNumber()+num);
                inputList.add(i, new InputData(id.getNumber()+num, price, (int)1));
                return ret;
            }
        }
        return ret;
    }
    
    private void prepareSellersList()
    {
        Collections.sort(inputDataSellers);
        double lastPrice=0;
        for(int i=0; i<inputDataSellers.size();i++)
        {
            if (lastPrice == inputDataSellers.get(i).getPrice())
            {
                InputData id = inputDataSellers.remove(i);
                id.setNumber(id.getNumber()+inputDataSellers.get(i-1).getNumber());
                inputDataSellers.remove(i-1);
                inputDataSellers.add(i-1, id);
                i--;
            }
            else
                lastPrice=inputDataSellers.get(i).getPrice();
        }
    }
    
    private BuyersPriceInterval searchPriceInterval(double price)
    {
        double lastPrice = 0;
        double lastNum = 0;
        double lastSum = inputDataBuyers.get(0).getCurrentSum();
        for (int i = 0; i < inputDataBuyers.size(); i++)
        {
            if (price == inputDataBuyers.get(i).getPrice())
                return new BuyersPriceInterval(price,price, lastSum-lastNum, inputDataBuyers.get(i).getCurrentSum()-inputDataBuyers.get(i).getNumber());
            else
                if (price < inputDataBuyers.get(i).getPrice())
                    return new BuyersPriceInterval(lastPrice,inputDataBuyers.get(i).getPrice(), lastSum-lastNum, inputDataBuyers.get(i).getCurrentSum()-inputDataBuyers.get(i).getNumber());
            lastPrice = inputDataBuyers.get(i).getPrice();
            lastSum = inputDataBuyers.get(i).getCurrentSum();
            lastNum = inputDataBuyers.get(i).getNumber();
        }
        return null;
    }
    
    public List<ProfitItem> auctionModel(double constBuyers)
    {
        List<ProfitItem> ret = new ArrayList<ProfitItem>();
        double p = getCosts();
        double step = (double) (0.001*p);
        //double step = 0.5;
        double profit = 0;
        prepareBuyersList(constBuyers);
        prepareSellersList();
        while (profit >=0)
        {
            double piece = 1;
            List<InputData> auctionList = new ArrayList<InputData>();
            auctionList.addAll(getInputDataSellers());
            piece = searchSellersPrice(auctionList, p, getMaxNum());
            
            if (piece == 1)
            {
                auctionList.add(new InputData(getMaxNum(), p, new Integer(1)));
                Collections.sort(auctionList);
            }
            auctionList = recalcSums(auctionList,0);
            double sum = 0;
            Integer ourIndex = null;
            int k=0;
            boolean isHalt = false;
            boolean isAlmostHalt = false;
            double lastSum = 0;
            double lastPrice = 0;
            double curSum = 0;
            double curPrice = 0;
            boolean isAllA = false;
            double totalPrice=0;
            double lastA = 0;
            //while (sum<getBuyVolume())
            while ((!isHalt)&&(k < auctionList.size()))
            {
                if (auctionList.get(k).getId()==1) ourIndex = k;
                curSum = auctionList.get(k).getCurrentSum();
                curPrice = auctionList.get(k).getPrice();
                BuyersPriceInterval bpi = searchPriceInterval(curPrice);
                
                if (bpi.getMinPrice()!=bpi.getMaxPrice())
                {
                    if ((bpi.getMinPriceSum() < curSum)&&(bpi.getMinPriceSum() > lastSum))
                    {
                        isHalt = true;
                        lastA = auctionList.get(k).getNumber() - (curSum - bpi.getMinPriceSum());
                    }
                    else
                        if ((curSum > bpi.getMaxPriceSum())&&(curSum < bpi.getMinPriceSum()))
                        {
                            isAlmostHalt = true;
                            lastA = auctionList.get(k).getNumber();
                        }
                        else
                            if (curSum>bpi.getMinPriceSum())
                            {
                                isHalt = true;
                                if (isAlmostHalt)
                                {
                                    k--;
                                    lastA = auctionList.get(k).getNumber();
                                }
                            }
                }
                else
                {
                    if (curSum >= bpi.getMinPriceSum())
                    {
                        isHalt = true;
                        if (isAlmostHalt)
                        {
                            k--;
                            lastA = auctionList.get(k).getNumber();
                        }
                        else
                        {
                            lastA = auctionList.get(k).getNumber() - (curSum - bpi.getMinPriceSum());
                        }
                    }
                    else
                        if ((curSum >= bpi.getMaxPriceSum())&&(curSum < bpi.getMinPriceSum()))
                        {
                            isHalt = true;
                            lastA = auctionList.get(k).getNumber();
                        }
                }
                k++;
                lastPrice = curPrice;
                lastSum = curSum;
            }
            k--;// то самое K
            if (ourIndex==null)
                profit = -1;
            else 
                if (ourIndex < k)
                    profit = (p-getCosts())*getMaxNum();
                else 
                    if (ourIndex==k)
                        profit = (p-getCosts())*lastA*piece;
                    else
                        profit = -1;
            
            if (profit >=0) ret.add(new ProfitItem(profit, p));
            else ret.add(new ProfitItem(0, p));
            p+=step;
        }
        return ret;
    }

    public double getCosts() {
        return costs;
    }

    public List<InputData> getInputDataBuyers() {
        return inputDataBuyers;
    }

    public List<InputData> getInputDataSellers() {
        return inputDataSellers;
    }

    public double getMaxNum() {
        return maxNum;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    public void setInputDataBuyers(List<InputData> inputDataBuyers) {
        this.inputDataBuyers = inputDataBuyers;
    }

    public void setInputDataSellers(List<InputData> inputDataSellers) {
        this.inputDataSellers = inputDataSellers;
    }

    public void setMaxNum(double maxNum) {
        this.maxNum = maxNum;
    }
    
}
