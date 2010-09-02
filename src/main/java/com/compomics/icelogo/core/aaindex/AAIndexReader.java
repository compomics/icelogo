package com.compomics.icelogo.core.aaindex;

import com.compomics.icelogo.core.enumeration.AAIndexEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 26-feb-2009
 * Time: 13:07:13
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class reads the aaindex1 file. It will created a Vector with AaParameterMatrices
 */
public class AAIndexReader {

    /**
     * A Vector with all the AaParameterMatrices that could be read from the aaindex1 file
     */
    private Vector<AAIndexMatrix> iAaMatrix_1;

    /**
     * A Vector with all the AaParameterMatrices that could be read from the aaindex1 file
     */
    private Vector<AAIndexMatrix> iAaMatrix_2;

    /**
     * Constructor
     * This will read the aaindex1 file and store the different AaParameterMatrices in the iAaParameterMatrix Vector.
     */
    public AAIndexReader() {

    }

    /**
     * Getter for the AaParameterMatrices that could be read
     *
     * @return Vector<AaParameterMatrix> Vector with the AaParameterMatrices
     */
    public Vector<AAIndexMatrix> getAaParameterMatrixes(AAIndexEnum aType) {
        // Switch over the aaindex Types.
        switch (aType) {
            case AAINDEX1:
                if (iAaMatrix_1 == null) {
                    String lParameterFileContent = getAaParameterFile("aaindex1");

                    //create the vector with the AaParameterMatrixes
                    iAaMatrix_1 = parseAaMatrix(aType, lParameterFileContent);
                }
                return iAaMatrix_1;

            case AAINDEX2:
                if (iAaMatrix_2 == null) {
                    String lParameterFileContent = getAaParameterFile("aaindex2");

                    //create the vector with the AaParameterMatrixes
                    iAaMatrix_2 = parseAaMatrix(aType, lParameterFileContent);
                }
                return iAaMatrix_2;

            default:
                return null;
        }
    }

    /**
     * Returns a Vector with AaMatrix instances for both aaindex files.
     *
     * @param aType                 - The type of aamatrix that is comming.
     * @param aParameterFileContent The content of the aaindex file in case.
     * @return Vector with AaMatrix objects as in the aaindex file.
     */
    private Vector<AAIndexMatrix> parseAaMatrix(final AAIndexEnum aType, final String aParameterFileContent) {
        Vector<AAIndexMatrix> lResult = new Vector<AAIndexMatrix>();

        String lMatrixPrefix = null;
        if (aType == AAIndexEnum.AAINDEX1) {
            lMatrixPrefix = "I";
        } else if (aType == AAIndexEnum.AAINDEX2) {
            lMatrixPrefix = "M";
        }

        //Parse the parameter file
        int lParamStart = 0;
        int lParamStop = aParameterFileContent.indexOf("//");
        while (lParamStop > -1) {

            String lParam = aParameterFileContent.substring(lParamStart, lParamStop);
            StringTokenizer lTok = new StringTokenizer(lParam, "\n");
            String iAaindexId = null;
            String iTitle = null;
            String iDescription = null;
            String iPubMedId = null;
            String iLitId = null;
            String iAuthors = null;
            String iJournal = null;
            String iMatrix = null;
            boolean parsingDescription = false;
            boolean parsingMatrix = false;
            boolean parsingTitle = false;

            while (lTok.hasMoreElements()) {


                String lLine = lTok.nextToken();
                //find the aaindex db id
                if (lLine.startsWith("H ")) {
                    //parse this line
                    iAaindexId = lLine.substring(lLine.indexOf("H ") + 2);
                } else if (lLine.startsWith("D ")) {
                    //parse this line
                    iTitle = lLine.substring(lLine.indexOf("D ") + 2);
                    parsingTitle = true;
                } else if (lLine.startsWith("R")) {
                    //parse this line
                    parsingTitle = false;
                    if (lLine.indexOf("PMID:") > 0) {
                        iPubMedId = lLine.substring(lLine.indexOf("PMID:") + 5);
                        if (lLine.indexOf("LIT:") > 0) {
                            iLitId = lLine.substring(lLine.indexOf("LIT:") + 4, lLine.indexOf("PMID:") - 1);
                        }
                    } else {
                        if (lLine.indexOf("LIT:") > 0) {
                            iLitId = lLine.substring(lLine.indexOf("LIT:") + 4);
                        }
                    }
                } else if (lLine.startsWith("A ")) {
                    //parse this line
                    iAuthors = lLine.substring(lLine.indexOf("A ") + 2);
                } else if (lLine.startsWith("T ")) {
                    //the title of the manuscript can be more than one line
                    //parse this line
                    iDescription = lLine.substring(lLine.indexOf("T ") + 2);
                    parsingDescription = true;
                } else if (lLine.startsWith("J ")) {
                    //parse this line
                    iJournal = lLine.substring(lLine.indexOf("J ") + 2);
                    parsingDescription = false;
                } else {
                    // The MatrixPrefix is "I" in aaindex1 while "M" in aaindex2.
                    if (lLine.startsWith(lMatrixPrefix)) {
                        //parse this line
                        iMatrix = lLine.substring(lLine.indexOf(lMatrixPrefix) + 2);
                        parsingMatrix = true;
                    } else if (parsingTitle) {
                        iTitle = iTitle + lLine.substring(1);
                    } else if (parsingDescription) {
                        iDescription = iDescription + lLine.substring(1);
                    } else if (parsingMatrix) {
                        iMatrix = iMatrix + "!" + lLine;
                    }
                }

            }
            //add the new AAIndexParameterMatrix to the vector
            switch (aType) {
                case AAINDEX1:
                    lResult.add(new AAIndexParameterMatrix(iAaindexId, iTitle, iDescription, iPubMedId, iLitId, iAuthors, iJournal, iMatrix));
                    break;

                case AAINDEX2:
                    lResult.add(new AAIndexSubstitutionMatrix(iAaindexId, iTitle, iDescription, iPubMedId, iLitId, iAuthors, iJournal, iMatrix));
                    break;
            }

            lParamStart = lParamStop + 4;
            lParamStop = aParameterFileContent.indexOf("\n//", lParamStop + 4);
        }

        return lResult;
    }


    /**
     * This method reads the aaindex1 file and gives this file as a string.
     *
     * @return String from the aaindex1 file
     */
    private String getAaParameterFile(String aFileName) {
        String lContent = null;
        try {
            // First of all, try it via the classloader for this file.
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(aFileName);
            if (is == null) {
                // Apparently not found, try again with the System (bootstrap) classloader.
                is = ClassLoader.getSystemResourceAsStream(aFileName);
                if (is == null) {
                    lContent = "No help file (" + aFileName + ") could be found in the classpath!";
                }
            }

            // See if we have an input stream.
            if (is != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                lContent = sb.toString();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //return the content of the aaindex1 file
        return lContent;
    }


    public static void main(String[] args) {
        AAIndexReader reader = new AAIndexReader();
        reader.getAaParameterMatrixes(AAIndexEnum.AAINDEX2);
        System.exit(0);
    }
}
