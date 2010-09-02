package com.compomics.icelogo.core.dbComposition;

import com.compomics.icelogo.core.data.MainInformationFeeder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 02-Jun-2009
 * Time: 09:16:37
 */
public class CompositionExtractor {

    /**
     * The compositions to extract
     */
    private ArrayList<SwissProtComposition> iCompositionsToWrite;
    /**
     * The main information feeder
     */
    private MainInformationFeeder iFeeder;

    /**
     * Constructor
     *
     * @param aCompositionsToWrite Compositions to complete
     * @throws IOException
     */
    public CompositionExtractor(ArrayList<SwissProtComposition> aCompositionsToWrite, MainInformationFeeder aFeeder) throws IOException {
        //set the compositions to extract
        this.iCompositionsToWrite = (ArrayList<SwissProtComposition>) aCompositionsToWrite.clone();
        this.iFeeder = aFeeder;

        //read the composition file
        InputStream is = ClassLoader.getSystemResourceAsStream("db/compositions.txt");
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(reader);
        String strLine;
        String aComposition = "";
        boolean aInAComposition = false;
        SwissProtComposition aCurrentComposition = null;
        while ((strLine = in.readLine()) != null) {
            if (aInAComposition) {
                aComposition = aComposition + strLine + "\n";
                if (strLine.startsWith("total")) {
                    aInAComposition = false;
                    aCurrentComposition.setComposition(aComposition);
                    aComposition = "";
                }
            }
            for (int i = 0; i < iCompositionsToWrite.size(); i++) {
                if (strLine.equalsIgnoreCase("//" + iCompositionsToWrite.get(i).getSpecieLink())) {
                    aCurrentComposition = iCompositionsToWrite.get(i);
                    aInAComposition = true;
                }
            }
        }
    }

    /**
     * This method will add every Composition to the feeder
     */
    public void addToMainInformationFeeder() {
        //add every composition to the main information feeder
        for (int i = 0; i < iCompositionsToWrite.size(); i++) {
            iFeeder.addSwissProtComposition(iCompositionsToWrite.get(i));
        }
    }
}
