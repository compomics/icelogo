package com.compomics.icelogo.core.aaindex;

import com.compomics.icelogo.core.data.MainInformationFeeder;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 26-feb-2009
 * Time: 14:28:45
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class stores the result of a calculation for one position where an AaParameterMatrix is used
 */
public class AAIndexParameterResult {


    /**
     * The mean in the positive set
     */
    private double iCalulatedMean;
    /**
     * The mean in the second positive set
     */
    private double iCalulatedMeanSetTwo;
    /**
     * the mean in the negative/reference set
     */
    private double iNegativeSetMean;
    /**
     * the standard deviation calculated on the negative set
     */
    private double iStandardDeviation;
    /**
     * the position
     */
    private int iPosition;
    /**
     * An instance of the MainInformationFeeder
     */
    private MainInformationFeeder iInfoFeeder;

    private boolean iUseSecondMean = false;

    /**
     * Constructor
     *
     * @param aCalulatedMean     The mean of the positive set
     * @param aNegativeSetMean   The mean of the reference set
     * @param aStandardDeviation The standard deviation calculated for the reference set
     * @param aPosition          The position
     */
    public AAIndexParameterResult(double aCalulatedMean, double aNegativeSetMean, double aStandardDeviation, int aPosition) {
        iInfoFeeder = MainInformationFeeder.getInstance();
        this.iCalulatedMean = aCalulatedMean;
        this.iNegativeSetMean = aNegativeSetMean;
        this.iStandardDeviation = aStandardDeviation;
        this.iPosition = aPosition;
    }

    /**
     * Getter for the calculated mean
     *
     * @return double with the calculated mean for the positive set
     */
    public double getCalulatedMean() {
        return iCalulatedMean;
    }

    /**
     * Getter for the calculated mean of the second set
     *
     * @return double with the calculated mean for the second positive set
     */
    public double getCalulatedMeanSetTwo() {
        return iCalulatedMeanSetTwo;
    }


    /**
     * Setter for the calculated mean of the second set
     * @param aSecondMean The second mean to set
     */
    public void setCalulatedMeanSetTwo(double aSecondMean) {
        iCalulatedMeanSetTwo = aSecondMean;
        iUseSecondMean = true;
    }


    /**
     * Getter for the reference mean
     *
     * @return double with the mean for the reference/negative set
     */
    public double getNegativeSetMean() {
        return iNegativeSetMean;
    }

    /**
     * Getter for the standard deviation
     *
     * @return double with the standard deviation calculated for the reference/negative set
     */
    public double getStandardDeviation() {
        return iStandardDeviation;
    }

    /**
     * Getter for the position
     *
     * @return int with the position
     */
    public int getPosition() {
        return iPosition;
    }

    /**
     * Getter for the calculated upper confidence limit. It will
     * use the Z score from the MainInformationFeeder.
     *
     * @return double with the value of the Upper confidence limit
     */
    public double getUpperConfidenceLimit() {
        double lZscore = iInfoFeeder.getZscore();
        return (iNegativeSetMean + (lZscore * iStandardDeviation));
    }

    /**
     * Getter for the calculated lower confidence limit. It will
     * use the Z score from the MainInformationFeeder.
     *
     * @return double with the value of the lower confidence limit
     */
    public double getLowerConfidenceLimit() {
        double lZscore = iInfoFeeder.getZscore();
        return iNegativeSetMean - (lZscore * iStandardDeviation);
    }
}
