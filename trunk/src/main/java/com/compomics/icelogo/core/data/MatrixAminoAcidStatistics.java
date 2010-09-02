package com.compomics.icelogo.core.data;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.stat.DescriptiveStatisticsImpl;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 21-aug-2008 Time: 10:22:46 To change this template use File | Settings |
 * File Templates.
 */
public class MatrixAminoAcidStatistics implements AminoAcidStatistics {
// ------------------------------ FIELDS ------------------------------

    private final static String iSeparator = ",";

    private final Vector<AminoAcidCounter> iAminoAcidCounters;
    /**
     * The identifier for this AminoAcidMatrix.
     */
    private final String iMatrixIdentifier;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructs an AminoAcid matrix instance from a series of CharCounter instances.
     *
     * @param aAminoAcidCounters CharCounter instances, these can for instance differentiate by position.
     * @param aIdentifier        The String identifying the AminoAcidMatrix.
     */
    public MatrixAminoAcidStatistics(final Vector<AminoAcidCounter> aAminoAcidCounters, String aIdentifier) {
        iAminoAcidCounters = aAminoAcidCounters;
        iMatrixIdentifier = aIdentifier;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Getter for property 'matrixIdentifier'.
     *
     * @return Value for property 'matrixIdentifier'.
     */
    private String getMatrixIdentifier() {
        return iMatrixIdentifier;
    }

// ------------------------ CANONICAL METHODS ------------------------

    public String toString() {
        return iMatrixIdentifier;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Returns the AminoAcidMatrix (AminoAcids x CharCounters) as a String.
     *
     * @return String
     */
    public String get2DMatrixAsString() {
        StringBuffer sb = new StringBuffer();
        // First dimension iteration over the aminoacids. (LINES)

        sb.append("AA").append(iSeparator);
        for (int i = 0; i < iAminoAcidCounters.size(); i++) {
            sb.append(i).append(iSeparator);
        }
        sb.append('\n');


        for (AminoAcidEnum lAminoAcid : AminoAcidEnum.values()) {
            sb.append(lAminoAcid.getOneLetterCode());
            sb.append(iSeparator);

            // Second dimension iteration over the counters. (COLUMNS)
            for (int j = 0; j < iAminoAcidCounters.size(); j++) {
                AminoAcidCounter lCharCounter = iAminoAcidCounters.elementAt(j);
                sb.append(lCharCounter.getPercentage(lAminoAcid));
                sb.append(iSeparator);
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Returns the aminoAcidCounter at the given index.
     *
     * @param aIndex int (0-based!)
     * @return AminoAcidCounter for the given index.
     */
    public AminoAcidCounter getAminoAcidCounter(int aIndex) {
        return iAminoAcidCounters.get(aIndex);
    }

    /**
     * Returns a String with all Amino Acids Standard Deviations for this Matrix.
     *
     * @return String
     */
    public String getAminoAcidStatisticsAsString() {
        StringBuffer sb = new StringBuffer();


        sb.append("AA").append(iSeparator);
        sb.append("SD").append(iSeparator);
        sb.append("MEAN").append(iSeparator);
        sb.append("PERC_5").append(iSeparator);
        sb.append("PERC_95").append(iSeparator).append("\n");


        for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
            DescriptiveStatistics stat = getStatistics(lAminoAcidEnum);
            sb.append(lAminoAcidEnum.getOneLetterCode());
            sb.append(iSeparator);
            sb.append((lAminoAcidEnum));
            sb.append(iSeparator);
            sb.append(stat.getMean());
            sb.append(iSeparator);
            sb.append(stat.getPercentile(5));
            sb.append(iSeparator);
            sb.append(stat.getPercentile(95));

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Private method to genereate a descriptive statistics element for a given AminoAcid.
     *
     * @param aAminoAcid
     * @return
     */
    public DescriptiveStatistics getStatistics(final AminoAcidEnum aAminoAcid) {
        DescriptiveStatisticsImpl stat = new DescriptiveStatisticsImpl();
        for (int i = 0; i < iAminoAcidCounters.size(); i++) {
            stat.addValue(iAminoAcidCounters.elementAt(i).getPercentage(aAminoAcid));
        }
        stat.setSequenceSetSize((int) iAminoAcidCounters.elementAt(0).getTotalCount());
        return stat;
    }

    /**
     * Returns a String representation to be read by the R scan() function for this aminoacidmatrix.
     *
     * @return Id, AminoAcid, Position and Percentage on each line.
     */
    public String getMatrixToRAsString() {
        StringBuffer sb = new StringBuffer();
        // First dimension iteration over the aminoacids. (LINES)
        for (AminoAcidEnum lAminoAcid : AminoAcidEnum.values()) {
            int lIndex = -1;
            for (AminoAcidCounter lAminoAcidCounter : iAminoAcidCounters) {
                lIndex++;

                // ID
                sb.append(getMatrixIdentifier());
                sb.append(iSeparator);

                // AminoAcid
                sb.append(lAminoAcid.getOneLetterCode());
                sb.append(iSeparator);

                // Position
                AminoAcidCounter lCharCounter = iAminoAcidCounters.elementAt(lIndex);
                sb.append(lIndex);
                sb.append(iSeparator);

                // Percentage
                sb.append(lCharCounter.getPercentage(lAminoAcid));

                // Newline
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Returns the number of AminoAcidCouters.
     *
     * @return int value with the number of amino acid counters.
     */
    public int getDimension() {
        return iAminoAcidCounters.size();
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
            if (lFrequency != 0.0) {
                calBits = calBits + (lFrequency * log2(lFrequency));
            }
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
    public AminoAcidEnum[] getRandomPeptide(int aLenght) {
        AminoAcidEnum[] lAas = new AminoAcidEnum[aLenght];

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

    /**
     * Returns the total sum of the matrix.
     *
     * @return
     */
    public double getTotalMatrixCount() {
        double result = 0;

        for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
            result = result + getAminoAcidCount(lAminoAcidEnum);
        }

        return result;
    }

    /**
     * Returns the total count for the given AminoAcidEnum.
     *
     * @param aAminoAcid
     * @return
     */
    public double getAminoAcidCount(AminoAcidEnum aAminoAcid) {
        double result = 0;

        for (AminoAcidCounter lAminoAcidCounter : iAminoAcidCounters) {
            result = result + lAminoAcidCounter.getCount(aAminoAcid);
        }

        return result;
    }
}
