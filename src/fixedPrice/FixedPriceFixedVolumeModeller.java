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
public class FixedPriceFixedVolumeModeller 
{
    private List<InputData> inputData;
    private double buyVolume;
    private double maxNum;
    private double costs;

    public FixedPriceFixedVolumeModeller(List<InputData> inputData, double costs, double maxNum, double buyVolume) {
        this.inputData = inputData;
        this.buyVolume = buyVolume;
        this.maxNum = maxNum;
        this.costs = costs;
    }
    
    public List<ProfitItem> auctionModel()
    {
        List<ProfitItem> ret = new ArrayList<ProfitItem>();
        double p = getCosts();
        double step = (double) (0.001*p);
        double profit = 0;
        while (profit >=0)
        {
            List<InputData> auctionList = new ArrayList<InputData>();
            auctionList.addAll(getInputData());
            auctionList.add(new InputData(getMaxNum(), p, 1));
            Collections.sort(auctionList);
            double sum = 0;
            Integer ourIndex = null;
            int k=0;
            while (sum<getBuyVolume())
            {
                if (auctionList.get(k).getId()==1) ourIndex = k;
                sum+=auctionList.get(k).getNumber();
                k++;
            }
            k--;// то самое K
            if (ourIndex==null)
                profit = -1;
            else if (ourIndex<k)
            {
                profit = (p-getCosts())*auctionList.get(ourIndex).getNumber();
            }
            else
            {
                sum-=auctionList.get(k).getNumber();
                profit = (p-getCosts())*(getBuyVolume()-sum);
            }
            
            if (profit >=0) ret.add(new ProfitItem(profit, p));
            else ret.add(new ProfitItem(0, p));
            p+=step;
        }
        return ret;
    }
    
    public double getBuyVolume() {
        return buyVolume;
    }

    public double getCosts() {
        return costs;
    }

    public List<InputData> getInputData() {
        return inputData;
    }

    public double getMaxNum() {
        return maxNum;
    }

    public void setBuyVolume(double buyVolume) {
        this.buyVolume = buyVolume;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    public void setInputData(List<InputData> inputData) {
        this.inputData = inputData;
    }

    public void setMaxNum(double maxNum) {
        this.maxNum = maxNum;
    }
}
