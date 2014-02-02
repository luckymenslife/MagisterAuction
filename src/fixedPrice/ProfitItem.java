/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fixedPrice;

/**
 *
 * @author iLGiZ
 */
public class ProfitItem implements Comparable<ProfitItem>
{
    private double profit;
    private double price;

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

    public ProfitItem(double profit, double price) {
        this.profit = profit;
        this.price = price;
    }

    @Override
    public int compareTo(ProfitItem o) {
        if (getPrice()<o.getPrice()) return -1;
        else if (getPrice()==o.getPrice()) return 0;
        else return 1;
    }
    
}
