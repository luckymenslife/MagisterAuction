/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fixedPrice;

/**
 *
 * @author iLGiZ
 */
public class ProfitItemParameter implements Comparable<ProfitItemParameter>
{
    private double profit;
    private double price;
    private double selfCost;
    private double aVal;
    private double bVal;

    public double getSelfCost() {
        return selfCost;
    }

    public double getaVal() {
        return aVal;
    }

    public double getbVal() {
        return bVal;
    }

    public ProfitItemParameter(double profit, double price, double selfCost, double aVal, double bVal) {
        this.profit = profit;
        this.price = price;
        this.selfCost = selfCost;
        this.aVal = aVal;
        this.bVal = bVal;
    }

    public double getPrice() {
        return price;
    }

    public double getProfit() {
        return profit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public ProfitItemParameter(double profit, double price) {
        this.profit = profit;
        this.price = price;
    }

    @Override
    public int compareTo(ProfitItemParameter o) {
        if (getPrice()<o.getPrice()) return -1;
        else if (getPrice()==o.getPrice()) return 0;
        else return 1;
    }
    
}
