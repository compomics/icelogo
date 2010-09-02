package com.compomics.icelogo.core.data;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.factory.AminoAcidFactory;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 21-aug-2008 Time: 10:23:00 To change this template use File | Settings |
 * File Templates.
 */
public class AminoAcidCounter {
// ------------------------------ FIELDS ------------------------------

    /**
     * Private counter for each added unit.
     */
    private double iCounter = 0;

    /**
     * This HashMap stores a running counter for each character.
     */
    private HashMap<AminoAcidEnum, Double> iUnitCounter = new HashMap<AminoAcidEnum, Double>();

// --------------------------- CONSTRUCTORS ---------------------------

    public AminoAcidCounter() {
    } // Empty constructor.

// -------------------------- OTHER METHODS --------------------------

    /**
     * Increase the counter for character c.
     *
     * @param c character.
     */
    public void count(Character c) {
        count(AminoAcidFactory.getAminoAcid(c));
    }

    /**
     * Increase the counter for character c.
     *
     * @param ae AminoAcidEnum.
     */
    public void count(AminoAcidEnum ae) {
        if (iUnitCounter.get(ae) == null) {
            iUnitCounter.put(ae, 1.0);
        } else {
            iUnitCounter.put(ae, iUnitCounter.get(ae) + 1);
        }
        iCounter++;
    }

    /**
     * Returns the count for the given AminoAcidEnum.
     *
     * @param ae
     * @return
     */
    public double getCount(AminoAcidEnum ae) {
        Object o = iUnitCounter.get(ae);
        if (o == null) {
            return 0;
        } else {
            return (Double) o;
        }
    }

    /**
     * Returns the percentage of AminoAcidEnum ae divided by the total character count.
     *
     * @param ae AminoAcidEnum.
     * @return double as percentage (value is between 0 and 1).
     */
    public double getPercentage(AminoAcidEnum ae) {
        if (iUnitCounter.get(ae) != null) {
            double lCharacterCount = iUnitCounter.get(ae);
            return lCharacterCount / iCounter;
        } else {
            return 0;
        }
    }

    /**
     * Returns the total sum of all character counts.
     *
     * @return
     */
    public double getTotalCount() {
        return iCounter;
    }
}
