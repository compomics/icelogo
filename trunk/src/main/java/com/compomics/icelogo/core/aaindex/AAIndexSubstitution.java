package com.compomics.icelogo.core.aaindex;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 14, 2009
 * Time: 12:51:47 PM
 * <p/>
 * This class
 */
public class AAIndexSubstitution {
    /**
     * The value for substitution in the matrix.
     */
    private double iSubstitutionValue;

    private AminoAcidEnum iTarget;

    public AAIndexSubstitution(final double aSubstitutionValue, final AminoAcidEnum aTarget) {
        iSubstitutionValue = aSubstitutionValue;
        iTarget = aTarget;
    }

    /**
     * Returns whether the SubstitutionValue is higher then the given trheshold.
     *
     * @param aThreshold boundary for substitution.
     * @return aThreshold = 3, iSubstitutionValue = 5 --> returns true.
     */
    public boolean compareTo(double aThreshold) {
        return aThreshold < iSubstitutionValue;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getSubstiturionValue(){
        return iSubstitutionValue;
    }

    /**
     * Returns the targetted AminoAcidEnum.
     *
     * @return
     */
    public AminoAcidEnum getTarget() {
        return iTarget;
    }
}
