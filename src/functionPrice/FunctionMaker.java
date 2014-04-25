/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package functionPrice;

import java.util.List;

/**
 *
 * @author iLGiZ
 */
public class FunctionMaker 
{
    private List<FunctionData> ret;

    
    public void print()
    {
        for(int i = 0; i < ret.size(); i++)
            ret.get(i).print();
    }
    
    public FunctionMaker(List<FunctionData> ret) {
        this.ret = ret;
    }

    public List<FunctionData> getRet() {
        return ret;
    }

    public void setRet(List<FunctionData> ret) {
        this.ret = ret;
    }
    
    public Double getResult(Double x, Double y)
    {
        //Пусть a - это x, а b - это y. 
        //При построении массива гарантируется, что он упорядочен по b, a.
        //Опираясь на это можно найти решение более оптимальным способом.
        
        if (    (x > this.ret.get(this.ret.size()-1).getX())||
                (y > this.ret.get(this.ret.size()-1).getY())||
                (y < this.ret.get(0).getY())||(x < this.ret.get(0).getY()))
            return 0.0;
        else
        {
            int i=0, j=0;
            Double lastX = -1.0, lastY = -1.0, newX = -1.0, newY = -1.0;
            
            while((newX<x)||(newY<y))
            {
                if ((x>newX)&&(x<=this.ret.get(i).getX()))
                    newX = this.ret.get(i).getX();
                else if (x > newX)
                    lastX = this.ret.get(i).getX();
                
                if ((y>newY)&&(y<=this.ret.get(i).getY()))
                    newY = this.ret.get(i).getY();
                else if (y > newY)
                    lastY = this.ret.get(i).getY();
                i++;
            }
            //lastX = x1, newX = x2, lastY = y1, newY = y2
            //if (lastX < 0) lastX = newX;
            //if (lastY < 0) lastY = newY;
            //System.out.println(Double.compare(x, newX)+"     "+Double.compare(y, newY));
            if ((Double.compare(x, newX)==0)&&(Double.compare(y, newY)==0))
                return this.f(x, y);
            else
                if (Double.compare(x, newX)==0)
                    return ((newY-y)*this.f(newX, lastY)/(2*(newY-lastY))+
                            (y-lastY)*this.f(newX, newY)/(2*(newY-lastY)));
                if (Double.compare(y, newY)==0)
                    return ((newX-x)*this.f(lastX, newY)/(2*(newX-lastX))+
                            (x-lastX)*this.f(newX, newY)/(2*(newX-lastX)));
                else
                {
                    Double d1, d2, d3, d4, s;
                    d1 = Math.sqrt((x-lastX)*(x-lastX)+(y-lastY)*(y-lastY));
                    d2 = Math.sqrt((newX-x)*(newX-x)+(y-lastY)*(y-lastY));
                    d3 = Math.sqrt((x-lastX)*(x-lastX)+(newY-y)*(newY-y));
                    d4 = Math.sqrt((newX-x)*(newX-x)+(newY-y)*(newY-y));
                    return ((d2+d3+d4)*this.f(lastX, lastY)+
                            (d1+d3+d4)*this.f(newX, lastY)+
                            (d1+d2+d4)*this.f(lastX, newY)+
                            (d1+d2+d3)*this.f(newX, newY))/(4*(d1+d2+d3+d4));
                }
        }
    }
    
    private Double f(Double x, Double y)
    {
        for(int i = 0; i<this.ret.size(); i++)
            if ((Double.compare(y, this.ret.get(i).getY())==0)&&(Double.compare(x, this.ret.get(i).getX())==0))
                return this.ret.get(i).getZ();
        return 0.0;
    }
}
