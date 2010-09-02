package com.compomics.icelogo.core.enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Dec 9, 2008
 * Time: 10:36:04 AM
 * To change this template use File | Settings | File Templates.
 */
public enum SamplingDirectionEnum {
    NtermToCterm("N -> C"), CtermToNterm("C -> N");

    private String iName;

    /**
     * Enumeration constructor.
     */
    SamplingDirectionEnum(String aName) {
        iName = aName;
    }

    @Override
    public String toString() {
        return iName;
    }
}
