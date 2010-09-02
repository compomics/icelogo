package com.compomics.icelogo.core.dbComposition;

import com.compomics.icelogo.core.data.AminoAcidCounter;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.dbtoolkit.io.UnknownDBFormatException;
import com.compomics.dbtoolkit.io.implementations.AutoDBLoader;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.compomics.util.protein.Protein;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Niklaas Colaert Date: 4-aug-2008 Time: 10:08:16 To change this template use File |
 * Settings | File Templates.
 */

/**
 * This class will read a ncbi fasta database. It will count all the aminoacids for every specie. Then it will created
 * properties files with the generall occurrence of every amino acid in every specie. A species.properties file will
 * save all the differens species with a link to the properties file of the specie. Ex. 1 = Homo sapiens 2 = Mus
 * musculus ...
 */
public class NcbiCompositionCalculator {
// ------------------------------ FIELDS ------------------------------

    /**
     * Vector with the different species
     */
    public Vector species = new Vector();
    /**
     * Vector with the counters for the different species
     */
    public Vector counters = new Vector();
    /**
     * The location to save the properties files to
     */
    public String iPropertiesLocation;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructor
     *
     * @param aLocation           The location of the Swissprot fasta file
     * @param aPropertiesLocation The location to save the different properties files to
     */
    public NcbiCompositionCalculator(String aLocation, String aPropertiesLocation) {
        iPropertiesLocation = aPropertiesLocation;
        File input = new File(aLocation);

        // Default built-in DBLoader types.
        Properties pr = null;
        if (pr == null || pr.size() == 0) {
            pr = new Properties();
            pr.put("1", "com.compomics.dbtoolkit.io.implementations.SwissProtDBLoader");
            pr.put("2", "com.compomics.dbtoolkit.io.implementations.FASTADBLoader");
        }
        String[] classNames = new String[pr.size()];
        Iterator it = pr.values().iterator();
        int counter = 0;
        while (it.hasNext()) {
            classNames[counter] = (String) it.next();
            counter++;
        }

        AutoDBLoader adb = new AutoDBLoader(classNames);
        DBLoader loader = null;
        try {
            loader = adb.getLoaderForFile(input.getAbsolutePath());
        } catch (IOException ioe) {
        } catch (UnknownDBFormatException udfe) {
        }
        if (loader == null) {
            JOptionPane.showMessageDialog(new JFrame(), new String[]{"No connection could be made to " + aLocation + " !"}, "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            // now read the db and see if we can find anything.
            try {
                Protein prot;
                String raw = null;
                while ((raw = loader.nextRawEntry()) != null) {
                    String specie = null;
                    try {
                        specie = raw.substring(raw.lastIndexOf("[") + 1, raw.lastIndexOf("]"));
                    } catch (Exception e) {
                        System.out.println("Could not find the species in the next fasta entry (normally the species must be between \"[\" and \"]\" )");
                        System.out.println(raw);
                        specie = "unknown";
                    }
                    boolean newSpecies = true;
                    AminoAcidCounter counterForSpecies = new AminoAcidCounter();
                    for (int i = 0; i < species.size(); i++) {
                        String matchSpecie = (String) species.get(i);
                        if (matchSpecie.equalsIgnoreCase(specie)) {
                            newSpecies = false;
                            counterForSpecies = (AminoAcidCounter) counters.get(i);
                        }
                    }
                    if (newSpecies) {
                        species.add(specie);
                        counters.add(counterForSpecies);
                    }
                    prot = new Protein(raw);
                    String sequence = prot.getSequence().getSequence();
                    //search all peptides
                    for (int l = 0; l < sequence.length(); l++) {
                        counterForSpecies.count(sequence.charAt(l));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        //write properties files for every specie
        try {
            PrintWriter speciesPrint = new PrintWriter(new FileWriter(iPropertiesLocation + "species.properties"));
            for (int in = 0; in < species.size(); in++) {
                String specieToStore = (String) species.get(in);
                speciesPrint.println(specieToStore + " = " + in);
                PrintWriter out = new PrintWriter(new FileWriter(iPropertiesLocation + in + ".properties"));
                AminoAcidCounter counterSpecie = (AminoAcidCounter) counters.get(in);

                for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
                    double perc = counterSpecie.getCount(lAminoAcidEnum) / counterSpecie.getTotalCount();
                    out.println(lAminoAcidEnum.getOneLetterCode() + " = " + perc);
                }

                //out.println("totale = " + counterSpecie.getTotaleCount());

                out.flush();
                out.close();
            }
            speciesPrint.flush();
            speciesPrint.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        new NcbiCompositionCalculator("C:\\Documents and Settings\\niklaas\\Desktop\\Chlorobium.fasta", "E:/temp/");
    }
}