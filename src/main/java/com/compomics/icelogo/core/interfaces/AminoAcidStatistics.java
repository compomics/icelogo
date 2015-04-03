package com.compomics.icelogo.core.interfaces;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 7-okt-2008 Time: 19:17:25 The 'AminoAcidMatrix ' class was created for
 */
public interface AminoAcidStatistics {
    /**
     * Returns a Statistics object for the given AminoAcid. The statistics are generated over the different columns of
     * the Matrix.
     *
     * @param aAminoAcid AminoAcid for which the Statistics must be returned.
     * @return The statistics object for aAminoAcid (Object from org.apache.commons.math)
     */
    public StatisticalSummary getStatistics(AminoAcidEnum aAminoAcid);

    /**
     * Returns the number of Columns in the Matrix.
     *
     * @return The number of Columns in the Matrix.
     */
    public int getDimension();

    /**
     * @return double with the bit for that position
     */
    public double getBit();

    /**
     * @return double with the calculated bit for that position
     */
    public double getCallBit();

    /**
     * This method will create a random (based on this AminoAcidStatistics) array of AminoAcidEnum  with a given length
     *
     * @param aLength The length of the array
     * @return AminoAcidEnum[] Array with random AminoAcids
     */
    public AminoAcidEnum[] getRandomPeptide(int aLength);
}
