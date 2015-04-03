package com.compomics.icelogo.core.enumeration;

/**
 * Enum type for working with AminoAcids.
 */
public enum AminoAcidEnum {
    GLY('G',5), ALA('A',0), SER('S',15), PRO('P',12),
    VAL('V',17), THR('T',16), CYS('C',1), LEU('L',9),
    ILE('I',7), ASN('N',11), ASP('D',2), GLN('Q',13),
    LYS('K',8), GLU('E',3), MET('M',10), HIS('H',6),
    PHE('F',4), ARG('R',14), TYR('Y',20), TRP('W',18), OTHER('X',19);

// ------------------------------ FIELDS ------------------------------

    private char iOneLetterCode;
    private AminoAcidEnum[] iAminoAcidEnumByPhysicoChemicalProperties = new AminoAcidEnum[20];
    private final int iNumber;


// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Enum construction.
     *
     * @param aOneLetterCode Single letter representation for the Amino Acid.
     */
    AminoAcidEnum(char aOneLetterCode,int numericRepresentation) {
        iOneLetterCode = aOneLetterCode;
        iNumber = numericRepresentation;
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
                    GLY, ALA, SER, //small
                    PRO, VAL,
                    THR, CYS,
                    LEU, ILE,
                    ASN, ASP,
                    GLN, LYS,
                    GLU, MET, HIS,
                    PHE, ARG, TYR, TRP, OTHER
            };
        }

        return iAminoAcidEnumByPhysicoChemicalProperties;
    }

    public int getAANumber(){
        return this.iNumber;
    }

    public String toString() {
        return String.valueOf(this.getOneLetterCode());
    }
}
