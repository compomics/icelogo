package com.compomics.icelogo.core.aaindex;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.factory.AminoAcidFactory;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 14, 2009
 * Time: 11:57:53 AM
 * <p/>
 * This class
 */
public class AAIndexSubstitutionMatrix extends AAIndexMatrix {

    public AAIndexSubstitutionMatrix(String aAaindexId, String aTitle, String aDescription, String aPubMedId, String aLitId, String aAuthors, String aJournal, String aMatrix) {
        super(aAaindexId, aTitle, aDescription, aPubMedId, aLitId, aAuthors, aJournal, aMatrix);
        this.parseMatrix();
    }

    public void parseMatrix() {
        // Example Matrix input:
        /*rows = ARNDCQEGHILKMFPSTWYV, cols = ARNDCQEGHILKMFPSTWYV!
        3.!
        -3.      6.!
        0.     -1.      4.!
        0.     -3.      2.      5.!
        -3.     -4.     -5.     -7.      9.!
        -1.      1.      0.      1.     -7.      6.!
        0.     -3.      1.      3.     -7.      2.      5.!
        1.     -4.      0.      0.     -5.     -3.     -1.      5.!
        -3.      1.      2.      0.     -4.      3.     -1.     -4.      7.!
        -1.     -2.     -2.     -3.     -3.     -3.     -3.     -4.     -4.      6.!
        -3.     -4.     -4.     -5.     -7.     -2.     -4.     -5.     -3.      1.      5.!
        -2.      2.      1.     -1.     -7.      0.     -1.     -3.     -2.     -2.     -4.      5.!
        -2.     -1.     -3.     -4.     -6.     -1.     -4.     -4.     -4.      1.      3.      0.      8.!
        -4.     -4.     -4.     -7.     -6.     -6.     -6.     -5.     -2.      0.      0.     -6.     -1.      8.!
        1.     -1.     -2.     -2.     -3.      0.     -1.     -2.     -1.     -3.     -3.     -2.     -3.     -5.      6.!
        1.     -1.      1.      0.     -1.     -2.     -1.      1.     -2.     -2.     -4.     -1.     -2.     -3.      1.      3.!
        1.     -2.      0.     -1.     -3.     -2.     -2.     -1.     -3.      0.     -3.     -1.     -1.     -4.     -1.      2.      4.!
        -7.      1.     -5.     -8.     -8.     -6.     -8.     -8.     -5.     -7.     -5.     -5.     -7.     -1.     -7.     -2.     -6.     12.!
        -4.     -6.     -2.     -5.     -1.     -5.     -4.     -6.     -1.     -2.     -3.     -6.     -4.      4.     -6.     -3.     -3.     -1.      8.!
        0.     -3.     -3.     -3.     -2.     -3.     -3.     -2.     -3.      3.      1.     -4.      1.     -3.     -2.     -2.      0.     -8.     -3.      5.

        */
        // 1. Create the Keyset for the hashmap with aminoacids.

        AminoAcidEnum[] lAminoAcids = AminoAcidEnum.values();
        for (int i = 0; i < lAminoAcids.length; i++) {
            AminoAcidEnum lAminoAcid = lAminoAcids[i];
            iAaMap.put(lAminoAcid, new Vector<AAIndexSubstitution>());
        }

        // 2. Create a StringTokenizer to iterate over the rows.
        StringTokenizer st = new StringTokenizer(iMatrix, "!");
        String line = null;
        // Container for the row and columns
        AminoAcidEnum[] lRows = null;
        AminoAcidEnum[] lColumns = null;
        int lMatrixRowCounter = -1;

        while (st.hasMoreTokens()) {
            line = st.nextToken();
            // First option, the header line contains an equal to '=' character.
            if (line.indexOf("=") >= 0) {
                // rows
                int lBeginIndex = line.indexOf('=') + 2;
                int lEndIndex = line.indexOf(',');

                lRows = AminoAcidFactory.getAminoAcidArray(line.substring(lBeginIndex, lEndIndex));

                line = line.substring(lEndIndex); // shorten the line.

                // columns
                lBeginIndex = line.indexOf('=') + 2;
                // Get AminoAcid array for the columns.
                lColumns = AminoAcidFactory.getAminoAcidArray(line.substring(lBeginIndex));
            } else {
                // Second option, the matrix rows (0-based).
                lMatrixRowCounter++;
                AminoAcidEnum lRowAminoAcid = lRows[lMatrixRowCounter];

                // 3. For each combination, create two Substitution instances. Thereby we will be able to retrieve all values for 1 Amino Acid.
                // ex: A <-> R == -3
                // Add into HashMap key 'Ala' a new Substitution object 'R' with value -3.
                // Add into HashMap key 'Arg' a new Substitution object 'A' with value -3.
                int lMatrixColumnCounter = -1;
                String[] lRowElements = line.split(" ");
                for (int i = 0; i < lRowElements.length; i++) { // First token is always empty.

                    if (lRowElements[i].indexOf('.') != -1) { // Only elements with a decimal point are to be considered.

                        // While the variable 'MatrixRowCounter' links to the row, the variable 'MatrixColumnCounter' links to the column.
                        lMatrixColumnCounter++;
                        AminoAcidEnum lColumnAminoAcid = lColumns[lMatrixColumnCounter];

                        if (lRowAminoAcid != lColumnAminoAcid) {
                            // The corresponding Matrix value for both aminoacids.
                            double lValue = Double.parseDouble(lRowElements[i].trim()); // Trim to remove leading or trailing spaces.

                            // Now create and store two Substitution instances.
                            ((Vector<AAIndexSubstitution>) iAaMap.get(lColumnAminoAcid)).add(new AAIndexSubstitution(lValue, lRowAminoAcid));
                            ((Vector<AAIndexSubstitution>) iAaMap.get(lRowAminoAcid)).add(new AAIndexSubstitution(lValue, lColumnAminoAcid));
                        } else {
                            // The corresponding Matrix value for both aminoacids.
                            double lValue = Double.parseDouble(lRowElements[i].trim());
                            ((Vector<AAIndexSubstitution>) iAaMap.get(lColumnAminoAcid)).add(new AAIndexSubstitution(lValue, lRowAminoAcid));
                        }
                    }

                }

            }
        }


    }

    /**
     * Returns a Vector with AaSubstitution instances for the given AminoAcidEnum.
     *
     * @param aAminoAcidEnum
     * @return AaSubstitution instances in a Vector.
     */
    public Object getValueForAminoAcid(final AminoAcidEnum aAminoAcidEnum) {
        return iAaMap.get(aAminoAcidEnum);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String toString() {
        return super.iTitle;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public HashMap getAaMap() {
        return super.iAaMap;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
