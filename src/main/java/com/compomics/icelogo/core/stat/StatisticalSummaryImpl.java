package com.compomics.icelogo.core.stat;

import org.apache.commons.math.stat.descriptive.StatisticalSummary;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 8-okt-2008 Time: 10:39:12 The 'StatisticalSummaryImpl ' class was created
 * for
 */
public class StatisticalSummaryImpl implements StatisticalSummary {
    private double iMean;
    private double iVariance;
    private double iStandardDeviation;
    private double iMax;
    private double iMin;
    private long iN;
    private double iSum;

    public StatisticalSummaryImpl(final double aMean, final double aVariance, final double aStandardDeviation, final double aMax, final double aMin, final long aN, final double aSum) {
        iMean = aMean;
        iVariance = aVariance;
        iStandardDeviation = aStandardDeviation;
        iMax = aMax;
        iMin = aMin;
        iN = aN;
        iSum = aSum;
    }

    public StatisticalSummaryImpl(final double aMean) {
        iMean = aMean;
        iVariance = 0;
        iStandardDeviation = 0;
        iMax = -1;
        iMin = -1;
        iN = -1;
        iSum = -1;
    }

    public StatisticalSummaryImpl(final double aMean, final long aN) {
        iMean = aMean;
        iN = aN;
        iVariance = -1;
        iStandardDeviation = -1;
        iMax = -1;
        iMin = -1;
        iSum = -1;
    }

    public StatisticalSummaryImpl(final double aMean, final long aN, double aStandardDeviation) {
        iMean = aMean;
        iN = aN;
        iVariance = -1;
        iStandardDeviation = aStandardDeviation;
        iMax = -1;
        iMin = -1;
        iSum = -1;
    }


    public double getMean() {
        return iMean;
    }

    public double getVariance() {
        return iVariance;
    }

    public double getStandardDeviation() {
        return iStandardDeviation;
    }

    public double getMax() {
        return iMax;
    }

    public double getMin() {
        return iMin;
    }

    public long getN() {
        return iN;
    }

    public double getSum() {
        return iSum;
    }
}
