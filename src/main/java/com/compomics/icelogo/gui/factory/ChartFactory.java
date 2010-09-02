package com.compomics.icelogo.gui.factory;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.core.model.TwoSampleMatrixDataModel;
import com.compomics.icelogo.gui.graph.HeatMapComponent;
import com.compomics.icelogo.gui.graph.IceLogoComponent;
import com.compomics.icelogo.gui.graph.SequenceLogoComponent;
import com.compomics.icelogo.gui.renderer.StatisticalBarRendererImpl;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.StatisticalCategoryDataset;

import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 1-sep-2008 Time: 17:47:25 The 'ChartFactory ' class was created for
 */
public class ChartFactory {
    /**
     * This value holds the last used value for the ZScore in the barchart. (Sync and update purposes)
     */
    private static double iChartZScore;

    public final static Color iReferenceBarColor = Color.black;
    public final static Color iPositionBarColor = new Color(190, 0, 0);
    public final static Color iPositionTwoBarColor = new Color(0, 0, 190);


// ------------------------------ FIELDS ------------------------------


// -------------------------- STATIC METHODS --------------------------

    /**
     * Returns a Barchart for the given matrixdatamodel.
     *
     * @param aMatrixDataModel
     * @param aIndex
     * @return JFreeChart
     */
    public static JFreeChart createBarchartPanel(MatrixDataModel aMatrixDataModel, int aIndex) {
        StatisticalCategoryDataset lDataset =
                aMatrixDataModel.getReferenceAndPositionStatisticalCategoryDataset(aIndex);

        // create the chart...
        JFreeChart lBarChart = org.jfree.chart.ChartFactory.createBarChart(
                "",       // chart title
                "",               // domain axis label
                "",                  // range axis label
                lDataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        // customizeBarchart (Set axis ticks, error bars, color gradient)

        lBarChart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) lBarChart.getPlot();
        Color lGray = new Color(215, 220, 220);
        plot.setDomainGridlinesVisible(true);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(lGray);
        plot.setRangeGridlinePaint(lGray);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        // disable bar outlines...
        //BarRenderer renderer = (BarRenderer) plot.getRenderer();
        StatisticalBarRendererImpl renderer = new StatisticalBarRendererImpl();
        renderer.setDrawBarOutline(false);
        updateChartZscore();

        renderer.setStandardDeviationFactor(iChartZScore);

        // set up gradient paints for series 1...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.darkGray,
                0.0f, 5.0f, iReferenceBarColor);
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesToolTipGenerator(0, new StandardCategoryToolTipGenerator());


        // set up gradient paints for series 2...
        GradientPaint gp1 = new GradientPaint(0.0f, 50.0f, iPositionBarColor,
                0.0f, 2.0f, new Color(100, 0, 0));
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesToolTipGenerator(1, new StandardCategoryToolTipGenerator());

        if (aMatrixDataModel instanceof TwoSampleMatrixDataModel) {
            // set up gradient paints for series 2...
            GradientPaint gp2 = new GradientPaint(0.0f, 50.0f, iPositionTwoBarColor,
                    0.0f, 2.0f, new Color(0, 0, 100));
            renderer.setSeriesPaint(2, gp2);
            renderer.setSeriesToolTipGenerator(2, new StandardCategoryToolTipGenerator());
        }

        // customise the renderer...
        renderer.setErrorIndicatorPaint(new Color(60, 145, 210));
        renderer.setMaximumBarWidth(0.10);
        renderer.setSeriesVisible(0, true);
        renderer.setItemMargin(0.0);
        plot.setRenderer(0, renderer);

        return lBarChart;
    }

    private static void updateChartZscore() {
        iChartZScore = MainInformationFeeder.getInstance().getZscore();
    }

    public static double getChartZScore() {
        return iChartZScore;
    }

    /**
     * Returns a HeatMapTable for the given MatrixDataModel.
     *
     * @param aMatrixDataModel The MatrixDataModel for which a HeatMap is created.
     * @return HeatMapTable component
     */
    public static HeatMapComponent createHeatMapPanel(final MatrixDataModel aMatrixDataModel) {
        return new HeatMapComponent(aMatrixDataModel);
    }

    /**
     * Returns a SequenceLogoComponent for the given MatrixDataModel
     *
     * @param aMatrixDataModel The MatrixDataModel for which a logo is created.
     * @return SequenceLogoComponent
     */
    public static IceLogoComponent createLogoPanel(final MatrixDataModel aMatrixDataModel) {
        return new IceLogoComponent(aMatrixDataModel, false);
    }

    /**
     * Returns a WeblogoComponent for the given WeblogoComponent
     *
     * @param aMatrixDataModel The MatrixDataModel for which a weblogo is created.
     * @return WeblogoComponent
     */
    public static SequenceLogoComponent createWeblogoPanel(final MatrixDataModel aMatrixDataModel) {
        return new SequenceLogoComponent(aMatrixDataModel);
    }
}
