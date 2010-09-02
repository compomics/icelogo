package com.compomics.icelogo.core.dbComposition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 02-Jun-2009
 * Time: 09:10:49
 */
public class SpeciesListReader {

    /**
     * A vector that holds all SwissProtCompositions
     */
    public Vector<SwissProtComposition> iComp = new Vector<SwissProtComposition>();


    /**
     * Constructor
     *
     * @throws IOException
     */
    public SpeciesListReader() throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream("db/speciesList.txt");
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(reader);
        String strLine;
        iComp = new Vector();
        while ((strLine = in.readLine()) != null) {
            iComp.add(new SwissProtComposition(strLine.substring(strLine.indexOf("= ") + 2), strLine.substring(0, strLine.indexOf(" = "))));
        }
    }

    /**
     * Getter for the vector with swissprot compositions
     *
     * @return Vector with swissprot compositions
     */
    public Vector<SwissProtComposition> getComp() {
        return iComp;
    }
}
