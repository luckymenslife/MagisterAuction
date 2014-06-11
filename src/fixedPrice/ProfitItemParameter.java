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
    private double num;
    private double zVal;
    private double auctionPrice;
    private double auctionVol;

    public double getAuctionPrice() {
        return auctionPrice;
    }

    public void setSelfCost(double selfCost) {
        this.selfCost = selfCost;
    }

    public void setAuctionVol(double auctionVol) {
        this.auctionVol = auctionVol;
    }

    public double getAuctionVol() {
        return auctionVol;
    }

    public void setzVal(double zVal) {
        this.zVal = zVal;
    }

    public double getzVal() {
        return zVal;
    }

    public void setaVal(double aVal) {
        this.aVal = aVal;
    }

    public void setbVal(double bVal) {
        this.bVal = bVal;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public double getSelfCost() {
        return selfCost;
    }

    public double getNum() {
        return num;
    }

    public double getaVal() {
        return aVal;
    }

    public double getbVal() {
        return bVal;
    }

    public ProfitItemParameter(double profit, double price, double selfCost, double aVal, double bVal, double num, double zVal, double auctionVol, double auctionPrice) {
        this.profit = profit;
        this.price = price;
        this.selfCost = selfCost;
        this.aVal = aVal;
        this.bVal = bVal;
        this.num = num;
        this.zVal = zVal;
        this.auctionVol = auctionVol;
        this.auctionPrice = auctionPrice;
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
    
    public void print()
    {
        System.out.println(this.price+"   "+this.profit+"      "+this.selfCost+", "+", "+this.num+", "+this.zVal+", "+this.aVal+", "+this.bVal);
    }

    @Override
    public int compareTo(ProfitItemParameter o) {
        if (getPrice()<o.getPrice()) return -1;
        else if (getPrice()==o.getPrice()) return 0;
        else return 1;
    }
    
}
