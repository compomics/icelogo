package com.compomics.icelogo.core.enumeration;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 2-sep-2008 Time: 17:56:32 The 'SequenceSetEnum ' class was created for
 */
public enum SequenceSetTypeEnum {
    FASTA("Fasta"), FASTA_EXPERIMENTAL("Conditional Fasta"), RAW("Manual");

// ------------------------------ FIELDS ------------------------------

    String iName;

// --------------------------- CONSTRUCTORS ---------------------------

    SequenceSetTypeEnum(String aName) {
        iName = aName;
    }

// ------------------------ CANONICAL METHODS ------------------------

    public String toString() {
        return iName;
    }
}
