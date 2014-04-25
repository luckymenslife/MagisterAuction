/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magisterauction;

import functionPrice.FunctionMaker;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 *
 * @author iLGiZ
 */
public class SurfaceChart extends AbstractAnalysis {
    private FunctionMaker fm;
    private Double maxA;
    private Double maxB;
    public void start(){
        try
        {
            AnalysisLauncher.open(new SurfaceChart(fm, maxA, maxB));
        }
        catch(Exception ex)
        {
            System.err.println("There is problem while generating chart! \n"+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public SurfaceChart(FunctionMaker fm, Double maxA, Double maxB) {
        this.fm = fm;
        this.maxA = maxA;
        this.maxB = maxB;
    }

    public void setFm(FunctionMaker fm) {
        this.fm = fm;
    }

    public void setMaxA(Double maxA) {
        this.maxA = maxA;
    }

    public void setMaxB(Double maxB) {
        this.maxB = maxB;
    }
    
    @Override
    public void init() {
        // Define a function to plot
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                return fm.getResult(x, y);
            }
        };
        // Define range and precision for the function to plot
        Range rangeA = new Range(1, maxA);
        Range rangeB = new Range(0, maxB);
        int stepsA = maxA.intValue();
        int stepsB = maxB.intValue();

        // Create the object to represent the function over the given range.
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(rangeA, stepsA, rangeB, stepsB), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(10, 10, 10, 0.95f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        // Create a chart
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        chart.getScene().getGraph().add(surface);
    }
}
