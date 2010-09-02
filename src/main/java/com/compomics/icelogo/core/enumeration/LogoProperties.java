package com.compomics.icelogo.core.enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 18, 2009
 * Time: 4:04:16 PM
 * <p/>
 * This class
 */
public enum LogoProperties {
    FASTAFILE_1("FASTAFILE_1"),
    FASTAFILE_2("FASTAFILE_2"),
    FASTAFILE_3("FASTAFILE_3"),
    PVALUE("PVALUE"),
    HEATMAP_MAGNITUDE("HEATMAP_ORDERS_OF_MAGNITUDE"),
    STARTPOSITION("STARTPOSITION"),
    ITERATIONSIZE("ITERATIONSIZE"),
    STATIC_SPECIES1("STATIC_SPECIES1"),
    STATIC_SPECIES2("STATIC_SPECIES2"),
    STATIC_SPECIES3("STATIC_SPECIES3"),
    STATIC_SPECIES4("STATIC_SPECIES4"),
    STATIC_SPECIES5("STATIC_SPECIES5"),
    STATIC_SPECIES6("STATIC_SPECIES6");

    private String iName;

    LogoProperties(final String aName) {
        iName = aName;
    }

    @Override
    public String toString() {
        return iName;
    }
}
