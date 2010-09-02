package com.compomics.icelogo.core.enumeration;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 7-sep-2008 Time: 17:45:08 The 'SequenceSetPanelEnum ' class was created
 * for
 */
public enum ExperimentTypeEnum {
    EXPERIMENT, EXPERIMENT_TWO;

    /**
     * Empty constructor.
     */
    ExperimentTypeEnum() {
        // Do nothing.
    }

    public String getName() {
        String s = "";
        if (this == EXPERIMENT) {
            s = "Experiment one";
        } else if (this == EXPERIMENT_TWO) {
            s = "Experiment two";
        }
        return s;
    }

}