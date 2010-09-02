package com.compomics.icelogo.core.factory;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;

/**
 * This Factory returns types of the AminoAcid enum.
 */
public class AminoAcidFactory {
// -------------------------- STATIC METHODS --------------------------

    /**
     * Returns a AminoAcid enum instance for the given
     *
     * @param aSingleLetter capital letter corresponding to single letter amino acid code.
     * @return AminoAcid enum
     */
    public static AminoAcidEnum getAminoAcid(char aSingleLetter) {
        switch (aSingleLetter) {
            case 'G':
                return AminoAcidEnum.GLY;

            case 'A':
                return AminoAcidEnum.ALA;

            case 'S':
                return AminoAcidEnum.SER;

            case 'P':
                return AminoAcidEnum.PRO;

            case 'V':
                return AminoAcidEnum.VAL;

            case 'T':
                return AminoAcidEnum.THR;

            case 'C':
                return AminoAcidEnum.CYS;

            case 'L':
                return AminoAcidEnum.LEU;

            case 'I':
                return AminoAcidEnum.ILE;

            case 'N':
                return AminoAcidEnum.ASN;

            case 'D':
                return AminoAcidEnum.ASP;

            case 'Q':
                return AminoAcidEnum.GLN;

            case 'K':
                return AminoAcidEnum.LYS;

            case 'E':
                return AminoAcidEnum.GLU;

            case 'M':
                return AminoAcidEnum.MET;

            case 'H':
                return AminoAcidEnum.HIS;

            case 'F':
                return AminoAcidEnum.PHE;

            case 'R':
                return AminoAcidEnum.ARG;

            case 'Y':
                return AminoAcidEnum.TYR;

            case 'W':
                return AminoAcidEnum.TRP;

            default:
                return AminoAcidEnum.OTHER;
        }
    }

    /**
     * Returns an Array of AminoAcidEnum instances for the given String of aminoacid chars.
     *
     * @param aAminoAcids as a String "KR" will return two AminoAcidEnums for Lysine and Arginine.
     * @return
     */
    public static AminoAcidEnum[] getAminoAcidArray(String aAminoAcids) {
        AminoAcidEnum[] lResult = new AminoAcidEnum[aAminoAcids.length()];
        for (int i = 0; i < lResult.length; i++) {
            lResult[i] = getAminoAcid(aAminoAcids.charAt(i));
        }
        return lResult;
    }
}
