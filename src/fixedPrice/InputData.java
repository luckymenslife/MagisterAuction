/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fixedPrice;

/**
 *
 * @author iLGiZ
 */
public class InputData implements Comparable<InputData>
{
    private double number;
    private double price;
    private int id;
    private double currentSum;

    public void setId(int id) {
        this.id = id;
    }

    public InputData(double number, double price, int id, double currentSum) {
        this.number = number;
        this.price = price;
        this.id = id;
        this.currentSum = currentSum;
    }

    public InputData(double number, double price, double currentSum) {
        this.number = number;
        this.price = price;
        this.currentSum = currentSum;
    }
    
    public InputData(double number, double price, int id) {
        this.number = number;
        this.price = price;
        this.id = id;
    }

    public InputData(double number, double price) {
        this.number = number;
        this.price = price;
        this.id=0;
    }

    public double getCurrentSum() {
        return currentSum;
    }

    public void setCurrentSum(double currentSum) {
        this.currentSum = currentSum;
    }
    
    public int getId() {
        return id;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public int compareTo(InputData o) {
        if (getPrice()<o.getPrice()) return -1;
        else if (getPrice()==o.getPrice()) return 0;
        else return 1;
    }
    
}
