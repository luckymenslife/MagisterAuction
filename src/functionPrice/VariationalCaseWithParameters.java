/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functionPrice;

import fixedPrice.FixedPricesForVariational;
import fixedPrice.InputData;
import fixedPrice.ProfitItemParameter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iLGiZ
 */
public class VariationalCaseWithParameters 
{
    private List<InputData> inputDataSellers;
    private List<InputData> inputDataBuyers;
    private Double rawCost;
    private double maxA;
    private double maxB;
    private double minValZ;
    private double fine;

    public void setInputDataSellers(List<InputData> inputDataSellers) {
        this.inputDataSellers = inputDataSellers;
    }

    public void setInputDataBuyers(List<InputData> inputDataBuyers) {
        this.inputDataBuyers = inputDataBuyers;
    }

    public void setRawCost(Double rawCost) {
        this.rawCost = rawCost;
    }

    public void setMaxA(double maxA) {
        this.maxA = maxA;
    }

    public void setMaxB(double maxB) {
        this.maxB = maxB;
    }

    public void setMinValZ(double minValZ) {
        this.minValZ = minValZ;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public List<InputData> getInputDataSellers() {
        return inputDataSellers;
    }

    public List<InputData> getInputDataBuyers() {
        return inputDataBuyers;
    }

    public Double getRawCost() {
        return rawCost;
    }

    public double getMaxA() {
        return maxA;
    }

    public double getMaxB() {
        return maxB;
    }

    public double getMinValZ() {
        return minValZ;
    }

    public double getFine() {
        return fine;
    }

    public VariationalCaseWithParameters(List<InputData> inputDataSellers, List<InputData> inputDataBuyers, Double rawCost, double maxA, double maxB, double minValZ, double fine) {
        this.inputDataSellers = inputDataSellers;
        this.inputDataBuyers = inputDataBuyers;
        this.rawCost = rawCost;
        this.maxA = maxA;
        this.maxB = maxB;
        this.minValZ = minValZ;
        this.fine = fine;
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
        Double ret = initVal+2*(k%4);
        ret+=(val/500);
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
    
    public List<ProfitItemParameter> auctionModel()
    {
        //Взятые с потолка коэффициенты
        Double aCoef = 2000.0;
        Double bCoef = 200.0;
        //Double aStep = 0.01*this.maxA;
        Double aStep = 1.0;
        Double bStep = 0.1*this.maxB;
        Double aVal = 1.0;
        Double bVal = 0.0;
        Double zVal = this.minValZ;
        List<ProfitItemParameter> ret = new ArrayList<ProfitItemParameter>();
        
        //Здесь надо вставить цикл по a и b
        while (Double.compare(bVal, this.maxB)<1)
        {
            //System.out.println("B:   "+bVal);
            
            aVal = 1.0;
            while (Double.compare(aVal,this.maxA)<1)
            {
                Double masterProfit = 0.0;
                Double masterP = 0.0;
                Double masterSelfCost = 0.0;
                Double masterSellerVal = 0.0;
                Double masterZVal = 0.0;
                
                //System.out.println("A:   "+aVal);
                zVal = aVal*this.minValZ+bVal*aVal;
                Double selfCost = this.rawCost + (aVal*aCoef+bVal*bCoef)/zVal;
                Double[] sellersVals = new Double[inputDataSellers.size()+1];
                Double[] buyersVals = new Double[inputDataBuyers.size()];
                for(int i = 0; i < sellersVals.length; i++)
                    sellersVals[i]=0.0;
                for(int i = 0; i < buyersVals.length; i++)
                    buyersVals[i]=0.0;
                Double initValSellers = 57.0;
                Double initValBuyers = 62.0;
                double p = selfCost;
                //double step = (double) (0.001*p);
                double step = (double) (0.01*p);
                double maxPriceVal = selfCost+50*step;
                boolean isFirst = true;
                while (p<maxPriceVal)
                {
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
                    recalcPrices(sellersData, buyersData, sellersVals, buyersVals, initValSellers, initValBuyers, p, zVal);

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
                        recalcPrices(sellersData, buyersData, sellersVals, buyersVals, initValSellers, initValBuyers, p, zVal);


                        k++;
                        //System.out.println(findMaxDiff(lastVals, sellersVals));
                        //printArray(sellersVals);
                        //printArray(lastVals);
                    }
                    while(findMaxDiff(lastVals, sellersVals)>0.007);
                    //System.out.print("Price: "+p+"      ");
                    //printArray(sellersVals);
                    double d=selfCost;
                    Double profit = (p-d)*sellersVals[0]-this.fine*(zVal-sellersVals[0]);
                    //ProfitItemParameter pip = new ProfitItemParameter(profit, p, selfCost, aVal, bVal, sellersVals[0], zVal);
                    //ret.add(pip);
                    //pip.print();
                    p+=step;
                    if (isFirst)
                    {
                        masterProfit = profit;
                        masterP = p;
                        masterSelfCost = selfCost;
                        masterSellerVal = sellersVals[0];
                        masterZVal = zVal;
                        isFirst = false;
                    }
                    else
                        if (profit > masterProfit)
                        {
                            masterProfit = profit;
                            masterP = p;
                            masterSelfCost = selfCost;
                            masterSellerVal = sellersVals[0];
                            masterZVal = zVal;
                        }
                    
                }
                ProfitItemParameter pip = new ProfitItemParameter(masterProfit, masterP, masterSelfCost, aVal, bVal, masterSellerVal, masterZVal);
                ret.add(pip);
                aVal+=aStep;
            }
            bVal+=bStep;
        }
        return ret;
    }
}
