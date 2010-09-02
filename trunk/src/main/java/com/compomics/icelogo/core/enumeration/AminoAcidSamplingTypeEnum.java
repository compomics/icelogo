package com.compomics.icelogo.core.enumeration;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 29-aug-2008 Time: 15:54:29 The 'SamplingType ' class was created for
 */
public enum AminoAcidSamplingTypeEnum {
    HORIZONTAL("Horizontal sampling"), VERTICAL("Vertical sampling"), RANDOM("Random sampling");

    public String NAME;

    AminoAcidSamplingTypeEnum(String aTitle) {
        NAME = aTitle;
    }

}
