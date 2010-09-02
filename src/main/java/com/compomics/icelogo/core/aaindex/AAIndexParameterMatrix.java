package com.compomics.icelogo.core.aaindex;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 26-feb-2009
 * Time: 13:12:21
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class holds the different parameters and values of an AaParameterMatrix
 */
public class AAIndexParameterMatrix extends AAIndexMatrix {


    /**
     * The constructor
     *
     * @param aAaindexId   a AaIndexId
     * @param aTitle       The title
     * @param aDescription The description
     * @param aPubMedId    The pubmedid
     * @param aLitId       The Litid
     * @param aAuthors     The authors
     * @param aJournal     The journal
     * @param aMatrix      The matrix
     */
    public AAIndexParameterMatrix(String aAaindexId, String aTitle, String aDescription, String aPubMedId, String aLitId, String aAuthors, String aJournal, String aMatrix) {
        super(aAaindexId, aTitle, aDescription, aPubMedId, aLitId, aAuthors, aJournal, aMatrix);
        this.parseMatrix();
    }

    /**
     * This method parses the iMatrix string and stores the values found in that String in  a HashMap (iAaMap)
     */
    public void parseMatrix() {
        //split the matrix line in 3 (use the ! as splitter)
        String[] lLines = iMatrix.split("!");
        String[] lAa = lLines[0].split("     ");
        String[] lValues1 = lLines[1].split(" ");
        Vector<Double> lV1 = new Vector<Double>();
        for (int i = 0; i < lValues1.length; i++) {
            if (lValues1[i].trim().length() > 0) {
                if (lValues1[i].trim().equalsIgnoreCase("NA")) {
                    lV1.add(Double.NaN);
                } else {
                    lV1.add(Double.valueOf(lValues1[i].trim()));
                }
            }
        }

        String[] lValues2 = lLines[2].split(" ");
        Vector<Double> lV2 = new Vector<Double>();
        for (int i = 0; i < lValues2.length; i++) {
            if (lValues2[i].trim().length() > 0) {
                if (lValues2[i].trim().equalsIgnoreCase("NA")) {
                    lV2.add(Double.NaN);
                } else {
                    lV2.add(Double.valueOf(lValues2[i].trim()));
                }
            }
        }
        for (int i = 0; i < lAa.length; i++) {
            String lPart = lAa[i];
            lPart = lPart.trim();
            String lAa1String = lPart.substring(0, lPart.indexOf("/"));
            String lAa2String = lPart.substring(lPart.indexOf("/") + 1);

            for (AminoAcidEnum aa : AminoAcidEnum.values()) {
                if (lAa1String.equalsIgnoreCase(String.valueOf(aa.getOneLetterCode()))) {
                    iAaMap.put(aa, lV1.get(i));
                }
                if (lAa2String.equalsIgnoreCase(String.valueOf(aa.getOneLetterCode()))) {
                    iAaMap.put(aa, lV2.get(i));
                }
            }
        }
        //The value for X is the mean of all the others
        double lSum = 0.0;
        for (AminoAcidEnum aa : AminoAcidEnum.values()) {
            if (aa.getOneLetterCode() != 'X') {
                lSum = lSum + (Double) this.getValueForAminoAcid(aa);
            }
        }
        iAaMap.put(AminoAcidEnum.OTHER, lSum / 20.0);

        iMatrix = "";
    }

    /**
     * Getter for the value for a specific aminoacid
     *
     * @param lAa The aminoacid where the value is asked for
     * @return double The value of the specific aminoacid found in the matrix of the AaParameterMatrix
     */
    public Object getValueForAminoAcid(AminoAcidEnum lAa) {
        return (Double) iAaMap.get(lAa);
    }

    /**
     * To String method
     *
     * @return String with the title
     */
    public String toString() {
        return iTitle;
    }

    /**
     * Getter for the iAaMap parameter
     *
     * @return HashMap with the values for every aminoacid
     */
    public HashMap getAaMap() {
        return iAaMap;
    }
}
