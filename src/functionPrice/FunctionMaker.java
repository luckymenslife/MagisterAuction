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
        //Пусть a - это x, а b - это y. При построении массива гарантируется, что он упорядочен по b, a.
        //Опираясь на это можно найти решение более оптимальным способом.
        
        if ((x > this.ret.get(this.ret.size()-1).getX())||(y > this.ret.get(this.ret.size()-1).getY())||(y < this.ret.get(0).getY())||(x < this.ret.get(0).getY()))
            return 0.0;
        else
        {
            while
        }
        return null;
    }
    
}
