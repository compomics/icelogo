package com.compomics.icelogo.gui.renderer;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.stat.StatisticsConversion;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 12-sep-2008 Time: 15:24:47 The 'HeatMapCellRenderer ' class was created
 * for
 */
public class DefaultHeatMapCellRenderer implements Observer {
// ------------------------------ FIELDS ------------------------------

    protected Color currentColor;
    private double iColorUnitSize;

    protected double iNegativeColorStart;
    protected double iNegativeColorEnd;


    protected double iPositiveColorStart;
    protected double iPositiveColorEnd;

// --------------------------- CONSTRUCTORS ---------------------------

    public DefaultHeatMapCellRenderer() {
        // Transform the Lower And Upper boundaries into powered values in order to
        // have a continous distribution between 0.05 and 0.005.

        buildColorBoundaries();

        // Calculate the color unit size based on the upper and lower z-score boundaries.
        iColorUnitSize = calculateColorUnitSize(200);

    }

    private void buildColorBoundaries() {
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        double lHeatMapStartValue = lFeeder.getHeatMapStartValue();
        double lHeatMapEndValue = lFeeder.getHeatMapEndValue();

        // If negative start value is 0.05, the inverse cumulative probability of 0.025 will be - 1.96.
        iNegativeColorStart = StatisticsConversion.inverseCumulativeProbability(lHeatMapStartValue / 2);

        // If negative end value is 0.005, the inverse cumulative probability of 0.0025 will be - 2.67.
        iNegativeColorEnd = StatisticsConversion.inverseCumulativeProbability(lHeatMapEndValue / 2);

        // If positive start value is 0.95 (1-0.05), the inverse cumulative probability of 0.975 will be 1.96.
        iPositiveColorStart = StatisticsConversion.inverseCumulativeProbability(1 - (lHeatMapStartValue / 2));

        // If positive end value is 0.095 (1-0.005), the inverse cumulative probability of 0.9975 will be 2.67.
        iPositiveColorEnd = StatisticsConversion.inverseCumulativeProbability(1 - (lHeatMapEndValue / 2));
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public double getNegativeColorStart() {
        return iNegativeColorStart;
    }

    public double getNegativeColorEnd() {
        return iNegativeColorEnd;
    }

    public double getPositiveColorStart() {
        return iPositiveColorStart;
    }

    public double getPositiveColorEnd() {
        return iPositiveColorEnd;
    }

    // -------------------------- OTHER METHODS --------------------------

    public String getColorAsHexadecimal(double aZScore) {
        Color lColor = getColor(aZScore);
        StringBuffer sb = new StringBuffer();
        int r = lColor.getRed();
        int g = lColor.getGreen();
        int b = lColor.getBlue();

        sb.append("#");

        sb.append(Integer.toHexString(r));
        if (r < 16) {
            sb.append("0");
        }

        sb.append(Integer.toHexString(g));
        if (g < 16) {
            sb.append("0");
        }

        sb.append(Integer.toHexString(b));
        if (b < 16) {
            sb.append("0");
        }

        return sb.toString();
    }

    /**
     * Returns whether the given factor is worth the color! As such, only a factor higher then the preset SDThreshold
     * will be colored. (Small deviations stay white!)
     *
     * @param aFactor The Factor to check.
     * @return a boolean indicating whether the cell must be colored.
     */
    public boolean isColored(double aFactor) {
        return iNegativeColorStart < aFactor;
    }

    /**
     * Calculate a unit for the coloring relative to the instance lower and upper boundaries.
     *
     * @param iRange
     * @return double colorintensty
     */
    private double calculateColorUnitSize(final int iRange) {
        // Get the delta for the positive values (deltas is equal for both negative and positive colors)
        double delta = iPositiveColorEnd - iPositiveColorStart;
        // Calculate a unitsize over a range of intensity points.
        return iRange / delta;
    }

    public Color getColor(double aZScore) {
        // The default dark color.
        int r = 30;
        int g = 30;
        int b = 30;

        if (aZScore < 0) {
            // Negative : Get red derivate color.
            // If below the lower limit, neutralize the zscore.
            if (aZScore > iNegativeColorStart) {
                aZScore = 0;
            } else if (aZScore < iNegativeColorEnd) {
                // Else if above the end color, set to the max color.
                aZScore = Math.abs(iNegativeColorEnd) - Math.abs(iNegativeColorStart);
            } else {
                // Else is in between.
                // case: aZscore = -3, iNegativeColorStart -1.96, iNegativeColorEnd -4
                // aZscore = -3 - (1.96) = -1.04 -> take absolute value!
                // THEN aZscore = 3 - 1.96 = 1.04 == positive color factor!

                aZScore = Math.abs(aZScore) - Math.abs(iNegativeColorStart);
            }
            r = r + ((int) (iColorUnitSize * aZScore));


        } else {
            // Positive : Get green derivate color.

            // for the Positive color to turn more greenish.

            if (aZScore < iPositiveColorStart) {
                // If below the lower limit, neutralize the zscore.
                aZScore = 0;
            } else if (aZScore > iPositiveColorEnd) {
                //Else if above the end color, set to the max color.
                aZScore = iPositiveColorEnd - iPositiveColorStart;
            } else {
                // Else is in between.
                aZScore = aZScore - iPositiveColorStart;
            }
            g = g + ((int) (iColorUnitSize * aZScore));
        }

        return new Color(r, g, b);
    }


// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Observer ---------------------

    public void update(final Observable o, final Object arg) {
        // 
        buildColorBoundaries();

        // Calculate the color unit size based on the upper and lower z-score boundaries.
        iColorUnitSize = calculateColorUnitSize(200);

    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        DefaultHeatMapCellRenderer lHeatMapCellRenderer = new DefaultHeatMapCellRenderer();
        double[] sds = new double[]{1.1, 1.96, 2.8, 3.48};

        JPanel jpanPositive = new JPanel();
        jpanPositive.setLayout(new FlowLayout());
        JPanel jpanNegative = new JPanel();
        jpanNegative.setLayout(new FlowLayout());

        for (int i = 0; i < sds.length; i++) {
            double lSd = sds[i];
            Color aPositiveColor = lHeatMapCellRenderer.getColor(lSd);
            Color aNegativeColor = lHeatMapCellRenderer.getColor(lSd);

            JLabel pos = new JLabel("@@@@ " + lSd + " @@@@@@");
            pos.setForeground(aPositiveColor);
            jpanPositive.add(pos);

            JLabel neg = new JLabel("@@@@ " + lSd + " @@@@@@");
            neg.setForeground(aNegativeColor);
            jpanNegative.add(neg);
        }


        JPanel jpanMain = new JPanel();
        jpanMain.setLayout(new BorderLayout());
        jpanMain.add(jpanPositive, BorderLayout.NORTH);
        jpanMain.add(jpanNegative, BorderLayout.SOUTH);

        frame.getContentPane().add(jpanMain);
        frame.setPreferredSize(new Dimension(1000, 150));
        frame.setLocation(new Point(300, 300));
        frame.pack();
        frame.setVisible(true);
    }
}