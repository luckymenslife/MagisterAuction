/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fixedPrice;
/**
 *
 * @author iLGiZ
 */
public class BuyersPriceInterval {
    
    private double minPrice;
    private double maxPrice;
    private double minPriceSum;
    private double maxPriceSum;
    private int id;
    private double maxPriceNum;

    public BuyersPriceInterval(double minPrice, double maxPrice, double minPriceSum, double maxPriceSum, int id, double maxPriceNum) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minPriceSum = minPriceSum;
        this.maxPriceSum = maxPriceSum;
        this.id = id;
        this.maxPriceNum = maxPriceNum;
    }

    public BuyersPriceInterval(double minPrice, double maxPrice, double minPriceSum, double maxPriceSum) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minPriceSum = minPriceSum;
        this.maxPriceSum = maxPriceSum;
        this.id = 0;
        this.maxPriceNum = 0;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setMaxPriceNum(double maxPriceNum) {
        this.maxPriceNum = maxPriceNum;
    }

    public int getId() {
        return id;
    }

    public double getMaxPriceNum() {
        return maxPriceNum;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public double getMaxPriceSum() {
        return maxPriceSum;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMinPriceSum() {
        return minPriceSum;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMaxPriceSum(double maxPriceNum) {
        this.maxPriceSum = maxPriceNum;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMinPriceSum(double minPriceNum) {
        this.minPriceSum = minPriceNum;
    }
}
