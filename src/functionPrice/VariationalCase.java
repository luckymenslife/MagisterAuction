/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functionPrice;

import fixedPrice.FixedPricesForVariational;
import fixedPrice.InputData;
import fixedPrice.ProfitItem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iLGiZ
 */
public class VariationalCase 
{
    private List<InputData> inputDataSellers;
    private List<InputData> inputDataBuyers;
    private Double costs;
    private double maxNum;

    public VariationalCase(List<InputData> inputDataSellers, List<InputData> inputDataBuyers, Double costs, double maxNum) {
        this.inputDataSellers = inputDataSellers;
        this.inputDataBuyers = inputDataBuyers;
        this.costs = costs;
        this.maxNum = maxNum;
    }
    
    private Double priceFunction(Double[] sellers, Double[] buyers, int k, Double initVal, Float disp)
    {
        Double sum = 0.0;
        for (int i = 0; i<sellers.length; i++)
        {
            sum+=((sellers[i]+k)*(i+k)%sellers.length);
        }
        for (int i = 0; i<buyers.length; i++)
        {
            sum+=((buyers[i]+k)*(i+k)%buyers.length);
        }
        sum = sum%disp+initVal;
        return sum;
    }
    
    private Double selfCost(Double raw_c, Double a, Double a_c, Double b, Double b_c, Double n)
    {
        return (raw_c + a*a_c + b*b_c)/n;
    }
    
    private Double selValZ(Double x, Double a, Double b)
    {
        return a*x+b;
    }
    
    private Double priceFunction(Double val, Double initVal, int k)
    {
        Double ret = initVal-k%5;
        ret-=(Math.sqrt(val))/4;
        return ret;
    } 
   
    private Double priceFunction_new(Double val, Double initVal, int k)
    {
        Double ret = initVal+(k%4)*2;
        ret+=(val/600);
        return ret;
    } 
    
    private void printArray(Double[]arr)
    {
        for(int i=0;i<arr.length;i++)
        {
            System.out.print(arr[i]+"  ");
        }
        System.out.println();
    }
    
    private void recalcPrices(List<InputData>inputDataSellers, List<InputData> inputDataBuyers, Double[] sellers, Double[] buyers, Double initVal, Float disp, Double p, Double maxNum)
    {
        InputData tempData;
        for(int i = 0; i<inputDataSellers.size(); i++)
        {
            tempData = inputDataSellers.remove(i);
            tempData.setPrice(priceFunction(sellers, buyers, i, initVal, disp));
            tempData.setId(i+1);
            inputDataSellers.add(i, tempData);
        }
        tempData= new InputData(maxNum, p, (int)0);
        inputDataSellers.add(tempData);
        for(int i = 0; i<inputDataBuyers.size(); i++)
        {
            tempData = inputDataBuyers.remove(i);
            tempData.setPrice(priceFunction(sellers, buyers, i, initVal, disp));
            tempData.setId(i);
            inputDataBuyers.add(i, tempData);
        }
    }
    
    private void recalcPrices(List<InputData>inputDataSellers, List<InputData> inputDataBuyers, Double[] sellers, Double[] buyers, Double initValSellers, Double initValBuyers, Double p, Double maxNum)
    {
        InputData tempData;
        for(int i = 0; i<inputDataSellers.size(); i++)
        {
            tempData = inputDataSellers.remove(i);
            tempData.setPrice(priceFunction_new(sellers[i+1], initValSellers,i+1));
            tempData.setId(i+1);
            inputDataSellers.add(i, tempData);
        }
        tempData= new InputData(maxNum, p, (int)0);
        inputDataSellers.add(tempData);
        for(int i = 0; i<inputDataBuyers.size(); i++)
        {
            tempData = inputDataBuyers.remove(i);
            tempData.setPrice(priceFunction(buyers[i], initValBuyers, i+1));
            tempData.setId(i);
            inputDataBuyers.add(i, tempData);
        }
    }
    
    private InputData copyInputDataList(InputData input)
    {
        InputData ret = new InputData(input.getNumber(), input.getPrice(), input.getId());
        return ret;
    }
    
    private Double findMaxDiff(Double[] oneArr, Double[] otherArr)
    {
        Double ret=0.0;
        for(int i = 0; i<oneArr.length; i++)
        {
            if (Math.abs(oneArr[i]-otherArr[i])>ret)
                ret = Math.abs(oneArr[i]-otherArr[i]);
        }
        return ret;
    }
    
    private double calculateCosts(double initCost, double num)
    {
        return (initCost-num/500);
    }
    
    public List<ProfitItem> auctionModel(boolean isFunc)
    {
        Double[] sellersVals = new Double[inputDataSellers.size()+1];
        Double[] buyersVals = new Double[inputDataBuyers.size()];
        for(int i = 0; i < sellersVals.length; i++)
            sellersVals[i]=0.0;
        for(int i = 0; i < buyersVals.length; i++)
            buyersVals[i]=0.0;
        Double initValSellers = 57.0;
        Double initValBuyers = 68.0;
        float disp = 10;
        double p = this.costs;
        double step = (double) (0.001*p);
        double maxPriceVal = initValSellers+40*step;
        List<ProfitItem> ret = new ArrayList<ProfitItem>();
        Double profit = 0.0;
        while ((p<maxPriceVal)||(profit > 1))
        {
            for(int i = 0; i < sellersVals.length; i++)
                sellersVals[i]=0.0;
            for(int i = 0; i < buyersVals.length; i++)
                buyersVals[i]=0.0;
            List<InputData> sellersData = new ArrayList<InputData>();
            List<InputData> buyersData = new ArrayList<InputData>();
            for (int i = 0; i<inputDataSellers.size();i++)
            {
                sellersData.add(copyInputDataList(inputDataSellers.get(i)));
            }
            for (int i = 0; i<inputDataBuyers.size();i++)
            {
                buyersData.add(copyInputDataList(inputDataBuyers.get(i)));
            }
            recalcPrices(sellersData, buyersData, sellersVals, buyersVals, initValSellers, initValBuyers, p, this.maxNum);
            
            double maxDiff = 0;
            Double[] lastVals;
            int k = 0;
            Double c = 0.5;
            Double alpha;            
            do
            {
                lastVals = sellersVals.clone();
                FixedPricesForVariational fpv = new FixedPricesForVariational(sellersData, buyersData);
                fpv.auctionModel(0);
                alpha = c/(k+1);
                for(int i=0; i<sellersVals.length; i++)
                {
                    sellersVals[i]=(1-alpha)*sellersVals[i]+alpha*fpv.getOutputSellers()[i];
                }
                for(int i=0; i<buyersVals.length; i++)
                {
                    buyersVals[i]=(1-alpha)*buyersVals[i]+alpha*fpv.getOutputBuyers()[i];
                }
                
                
                sellersData = new ArrayList<InputData>();
                buyersData = new ArrayList<InputData>();
                for (int i = 0; i<inputDataSellers.size();i++)
                {
                    sellersData.add(copyInputDataList(inputDataSellers.get(i)));
                }
                for (int i = 0; i<inputDataBuyers.size();i++)
                {
                    buyersData.add(copyInputDataList(inputDataBuyers.get(i)));
                }
                recalcPrices(sellersData, buyersData, sellersVals, buyersVals, initValSellers, initValBuyers, p, this.maxNum);
                
                
                k++;
                //System.out.println(findMaxDiff(lastVals, sellersVals));
                //printArray(sellersVals);
                //printArray(lastVals);
            }
            while(findMaxDiff(lastVals, sellersVals)>0.001);
            System.out.print("Price: "+p+"      ");
            printArray(sellersVals);
            double d=0;
            if (isFunc)
                d=calculateCosts(this.costs, sellersVals[0]);
            else
                d=this.costs;
            profit = (p-d)*sellersVals[0];
            ret.add(new ProfitItem(profit, p));
            p+=step;
        }
        
        return ret;
    }
}
