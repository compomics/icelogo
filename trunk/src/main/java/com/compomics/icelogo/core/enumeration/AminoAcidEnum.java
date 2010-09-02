package com.compomics.icelogo.core.enumeration;

/**
 * Enum type for working with AminoAcids.
 */
public enum AminoAcidEnum {
    GLY('G'), ALA('A'), SER('S'), PRO('P'),
    VAL('V'), THR('T'), CYS('C'), LEU('L'),
    ILE('I'), ASN('N'), ASP('D'), GLN('Q'),
    LYS('K'), GLU('E'), MET('M'), HIS('H'),
    PHE('F'), ARG('R'), TYR('Y'), TRP('W'), OTHER('X');

// ------------------------------ FIELDS ------------------------------

    private char iOneLetterCode;
    private AminoAcidEnum[] iAminoAcidEnumByPhysicoChemicalProperties = new AminoAcidEnum[20];

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Enum construction.
     *
     * @param aOneLetterCode Single letter representation for the Amino Acid.
     */
    AminoAcidEnum(char aOneLetterCode) {
        iOneLetterCode = aOneLetterCode;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Get the one-letter code for the AminoAcid instance. (For instance Ala returns 'A').
     *
     * @return char one-letter code for the AminoAcid.
     */
    public char getOneLetterCode() {
        return iOneLetterCode;
    }

// -------------------------- OTHER METHODS --------------------------

    public AminoAcidEnum[] valuesByPhysicoChemicalProperties() {
        if (iAminoAcidEnumByPhysicoChemicalProperties != null) {
            iAminoAcidEnumByPhysicoChemicalProperties = new AminoAcidEnum[]{
                    GLY, ALA, VAL, //small
                    SER, THR,
                    CYS, MET,
                    LEU, ILE,
                    LYS, ARG,
                    GLU, ASP,
                    GLN, ASN,
                    PHE, TYR, TRP,
                    PRO, HIS, OTHER
            };
        }

        return iAminoAcidEnumByPhysicoChemicalProperties;
    }

    public String toString() {
        return String.valueOf(this.getOneLetterCode());
    }
}
