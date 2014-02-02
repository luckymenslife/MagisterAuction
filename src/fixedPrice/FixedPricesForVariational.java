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
public class FixedPricesForVariational 
{
    private List<InputData> inputDataSellers;
    private List<InputData> inputDataBuyers;
    private Double[] outputSellers;// массив объемов полученного объема продаж продавцов
    private Double[] outputBuyers;// --//-- покупателей
    private double costs;
    private double maxNum;

    public FixedPricesForVariational(List<InputData> inputDataSellers, List<InputData> inputDataBuyers) {
        this.inputDataSellers = new ArrayList<InputData>();
        this.inputDataBuyers = new ArrayList<InputData>();
        for (int i = 0; i<inputDataSellers.size();i++)
        {
            this.inputDataSellers.add(copyInputDataList(inputDataSellers.get(i)));
        }
        for (int i = 0; i<inputDataBuyers.size();i++)
        {
            this.inputDataBuyers.add(copyInputDataList(inputDataBuyers.get(i)));
        }
        outputSellers = new Double[inputDataSellers.size()];
        outputBuyers = new Double[inputDataBuyers.size()];
    }
    
    private InputData copyInputDataList(InputData input)
    {
        InputData ret = new InputData(input.getNumber(), input.getPrice(), input.getId());
        return ret;
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
                return new BuyersPriceInterval(price,price, lastSum-lastNum, inputDataBuyers.get(i).getCurrentSum()-inputDataBuyers.get(i).getNumber(),i,inputDataBuyers.get(i).getNumber());
            else
                if (price < inputDataBuyers.get(i).getPrice())
                    return new BuyersPriceInterval(lastPrice,inputDataBuyers.get(i).getPrice(), lastSum-lastNum, inputDataBuyers.get(i).getCurrentSum()-inputDataBuyers.get(i).getNumber(), i, inputDataBuyers.get(i).getNumber());
            lastPrice = inputDataBuyers.get(i).getPrice();
            lastSum = inputDataBuyers.get(i).getCurrentSum();
            lastNum = inputDataBuyers.get(i).getNumber();
        }
        return null;
    }
    
    public void auctionModel(double constBuyers)
    {
        List<ProfitItem> ret = new ArrayList<ProfitItem>();
        double p = getCosts();
        double step = (double) (0.001*p);
        //double step = 0.5;
        double profit = 0;
        prepareBuyersList(constBuyers);
        //prepareSellersList();
        
            double piece = 1;
            Collections.sort(inputDataSellers);
            inputDataSellers = recalcSums(inputDataSellers,0);
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
            double lastB = 0;
            int kB = 0;
            //while (sum<getBuyVolume())
            while ((!isHalt)&&(k < inputDataSellers.size()))
            {
                curSum = inputDataSellers.get(k).getCurrentSum();
                curPrice = inputDataSellers.get(k).getPrice();
                BuyersPriceInterval bpi = searchPriceInterval(curPrice);
                
                if (bpi.getMinPrice()!=bpi.getMaxPrice())
                {
                    if ((bpi.getMinPriceSum() < curSum)&&(bpi.getMinPriceSum() > lastSum))
                    {
                        isHalt = true;
                        lastA = inputDataSellers.get(k).getNumber() - (curSum - bpi.getMinPriceSum());
                        lastB = bpi.getMaxPriceNum();
                        kB = bpi.getId();
                    }
                    else
                        if ((curSum > bpi.getMaxPriceSum())&&(curSum < bpi.getMinPriceSum()))
                        {
                            isAlmostHalt = true;
                            lastA = inputDataSellers.get(k).getNumber();
                            lastB = bpi.getMaxPriceNum() - (bpi.getMinPriceSum() - curSum);
                            kB = bpi.getId();
                        }
                        else
                            if (curSum>bpi.getMinPriceSum())
                            {
                                isHalt = true;
                                if (isAlmostHalt)
                                {
                                    k--;
                                    lastA = inputDataSellers.get(k).getNumber();
                                }
                            }
                }
                else
                {
                    if (curSum >= bpi.getMinPriceSum())
                    {
                        isHalt = true;
                        if (isAlmostHalt)     //????????????
                        {
                            k--;
                            lastA = inputDataSellers.get(k).getNumber();
                        }
                        else
                        {
                            lastA = inputDataSellers.get(k).getNumber() - (curSum - bpi.getMinPriceSum());
                            lastB = bpi.getMaxPriceNum();
                            kB = bpi.getId();
                        }
                    }
                    else
                        if ((curSum >= bpi.getMaxPriceSum())&&(curSum < bpi.getMinPriceSum()))
                        {
                            isHalt = true;
                            lastA = inputDataSellers.get(k).getNumber();
                            lastB = bpi.getMaxPriceNum() - (bpi.getMinPriceSum() - curSum);
                            kB = bpi.getId();
                        }
                }
                k++;
                lastPrice = curPrice;
                lastSum = curSum;
            }
            k--;// то самое K
            
            //Ищем элементы с одинаковыми ценами
            Double[] helperArr = new Double[outputSellers.length];
            for (int i = 0; i<helperArr.length; i++)
            {
                if (i<k) helperArr[i]=1.0;
                else
                    if (i==k)
                        helperArr[i]=lastA/inputDataSellers.get(k).getNumber();
                    else
                        helperArr[i]=0.0;
            }
            Double price = inputDataSellers.get(k).getPrice();
            sum = lastA;
            Double allSum = inputDataSellers.get(k).getNumber();
            for(int i = 0; i<inputDataSellers.size(); i++)
            {
                if ((inputDataSellers.get(i).getPrice() == price)&&(i!=k))
                {
                    if (i>k) allSum+=inputDataSellers.get(i).getNumber();
                    else
                    {
                        sum+=inputDataSellers.get(i).getNumber();
                        allSum+=inputDataSellers.get(i).getNumber();
                    }
                }
            }
            
            if (allSum>inputDataSellers.get(k).getNumber())
            {
                Double valP = sum/allSum;
                for(int i = 0; i<inputDataSellers.size(); i++)
                {
                    if (inputDataSellers.get(i).getPrice() == price)
                    {
                        helperArr[i]=valP;
                    }
                }
            }
            
            //k - индекс, после которого надо обнулять элементы списка продавцов
            //kB - индекс, ДО которого надо обнулять элементы списка
            
            for(int i = 0; i<inputDataSellers.size(); i++)
            {
                outputSellers[inputDataSellers.get(i).getId()] = inputDataSellers.get(i).getNumber()*helperArr[i];
            }
            
            for(int i = 0; i<inputDataBuyers.size(); i++)
            {
                if (i > kB)
                    outputBuyers[inputDataBuyers.get(i).getId()] = inputDataBuyers.get(i).getNumber();
                else
                    if (i == kB)
                        outputBuyers[inputDataBuyers.get(i).getId()] = lastB;
                    else
                        outputBuyers[inputDataBuyers.get(i).getId()] = 0.0;
            }
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

    public Double[] getOutputBuyers() {
        return outputBuyers;
    }

    public Double[] getOutputSellers() {
        return outputSellers;
    }
    
    
    
}
