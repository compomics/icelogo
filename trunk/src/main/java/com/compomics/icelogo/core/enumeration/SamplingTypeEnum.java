package com.compomics.icelogo.core.enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 17, 2009
 * Time: 2:57:29 PM
 * <p/>
 * This class
 */
public enum SamplingTypeEnum {

    HORIZONTAL("Horizontal"), RANDOM("Random"), TERMINAL("Vertical"), REGIONAL("Regional");

    private String iName = null;

    private SamplingTypeEnum(final String aName) {
        iName = aName;
    }

    /**
     * Returns a description for this sampling type.
     *
     * @return
     */
    public String getDescription() {
        if (this == HORIZONTAL) {
            return "Sample the average occurence of an amino acid along a protein stretch";
        } else if (this == TERMINAL) {
            return "Sample the average occurence of an amino acid at fixed postions";
        } else if (this == RANDOM) {
            return "Sample the average occurence of an amino acid at random along a protein";
        } else if (this == REGIONAL) {
            return "Sample the average occurence of an amino acid around a given region";
        } else {
            return "No description.";
        }
    }

    /**
     * Returns the name for the sampling type.
     *
     * @return
     */
    public String getName() {
        return iName;
    }
}
