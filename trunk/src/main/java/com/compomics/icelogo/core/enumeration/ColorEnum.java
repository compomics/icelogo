package com.compomics.icelogo.core.enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 9-okt-2008
 * Time: 11:36:16
 * To change this template use File | Settings | File Templates.
 */
public enum ColorEnum {
    BLACK("black"), GREEN("green"), RED("red"), BLUE("blue"), PURPLE("purple");


    // ------------------------------ FIELDS ------------------------------

    private String iDisplayName;


    // --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Enum construction.
     *
     * @param aDisplayName String representation for the color.
     */
    ColorEnum(String aDisplayName) {
        iDisplayName = aDisplayName;
    }

    public String toString() {
        return iDisplayName;
    }

}
