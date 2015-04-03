package com.compomics.icelogo.core.data;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.stat.StatisticalSummaryImpl;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 8-okt-2008 Time: 10:22:55 The 'SingleColumnAminoAcidMatrix ' class was
 * created for
 */
public class FixedAminoAcidStatistics implements AminoAcidStatistics {
    private HashMap<AminoAcidEnum, StatisticalSummary> iStatistics;

    /**
     * Constructs a AminoAcidMatrix with a single column.
     *
     * @param aContent The content to create the Statistics.
     * @param aN       The number of entries used to build the percentages.
     */
    public FixedAminoAcidStatistics(HashMap<AminoAcidEnum, Double> aContent, final int aN) {

        iStatistics = new HashMap<AminoAcidEnum, StatisticalSummary>();
        for (final AminoAcidEnum lAminoAcidEnum : aContent.keySet()) {
            //calculate theoretical standard deviation
            double lStandardDeviation = Math.sqrt((aContent.get(lAminoAcidEnum)) / aN);
            iStatistics.put(lAminoAcidEnum, new StatisticalSummaryImpl(aContent.get(lAminoAcidEnum), aN, lStandardDeviation));
        }
    }

    /**
     * {@inheritDoc}
     */
    public StatisticalSummary getStatistics(final AminoAcidEnum aAminoAcid) {
        return iStatistics.get(aAminoAcid);
    }

    /**
     * {@inheritDoc}
     */
    public int getDimension() {
        return 1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public double getBit() {

        double maxBits = 4.321928;
        double calBits = 0.0;
        for (AminoAcidEnum lAA : AminoAcidEnum.values()) {
            StatisticalSummary stats = this.getStatistics(lAA);
            double lFrequency = stats.getMean();
            calBits = calBits + (lFrequency * log2(lFrequency));
        }
        calBits *= -1.0;
        return maxBits - calBits;
    }

    /**
     * {@inheritDoc}
     */
    public double getCallBit() {
        double calBits = 0.0;
        for (AminoAcidEnum lAA : AminoAcidEnum.values()) {
            StatisticalSummary stats = this.getStatistics(lAA);
            double lFrequency = stats.getMean();
            if (lFrequency != 0.0) {
                calBits = calBits + (lFrequency * log2(lFrequency));
            }
        }
        calBits *= -1.0;
        return calBits;
    }

    /**
     * {@inheritDoc}
     */
    public AminoAcidEnum[] getRandomPeptide(int aLength) {
        AminoAcidEnum[] lAas = new AminoAcidEnum[aLength];

        for (int i = 0; i < lAas.length; i++) {

            double lPercSum = 0.0;
            double lRandom = Math.random();

            for (AminoAcidEnum aa : AminoAcidEnum.values()) {

                lPercSum = lPercSum + this.getStatistics(aa).getMean();
                if (lRandom <= lPercSum) {
                    lAas[i] = aa;
                    break;
                }

            }
            if (lAas[i] == null) {
                lAas[i] = AminoAcidEnum.OTHER;
            }
        }
        return lAas;
    }

    public double log2(double x) {

        return Math.log(x) / Math.log(2.0);

    }

    public String toString() {
        return "Fixed Reference set";
    }

}
