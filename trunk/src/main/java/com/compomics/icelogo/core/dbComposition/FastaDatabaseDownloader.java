package com.compomics.icelogo.core.dbComposition;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 22-Jun-2009
 * Time: 13:45:39
 */
public class FastaDatabaseDownloader {
    private String iUniprotRelease;
    private String iSprotRelease;
    private String iTremblRelease;
    /**
     * The species name to filter for
     */
    private String iTaxonomyFilter;
    /**
     * The folder location where the fasta db must be saved
     */
    private String iFolderLocation;
    /**
     * The filename of the created database
     */
    private String iFileName;
    /**
     * The progress bar
     */
    private JProgressBar iProgressBar;
    /**
     * The number of saved proteins
     */
    private int iProteinUsedCounter = 0;


    public FastaDatabaseDownloader(String aFolderLocation, String aTaxonomyFilter, JProgressBar aProgressBar) {
        //set the folder location and taxonomy filter
        this.iFolderLocation = aFolderLocation;
        this.iTaxonomyFilter = aTaxonomyFilter;
        this.iProgressBar = aProgressBar;

    }

    public String getDatabaseFilename() {
        return iFileName;
    }

    public int getNumberOfSavedProteins() {
        return iProteinUsedCounter;
    }

    public boolean start() {
        try {

            //get the release notes
            URL url = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/reldate.txt");
            URLConnection urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            Reader r = new InputStreamReader(is);
            StringBuilder inputRelNotes = new StringBuilder();


            int i;
            while ((i = r.read()) != -1) {
                inputRelNotes.append((char) i);
            }
            String lRelNotes = inputRelNotes.toString();
            this.parseReleaseNotes(lRelNotes);
            System.out.println("Parsed the release notes");

            //create a writer
            if (iTaxonomyFilter != null) {
                iFileName = "sprot_" + iSprotRelease + "_" + iTaxonomyFilter + ".fasta";
            } else {
                iFileName = "sprot_" + iSprotRelease + ".fasta";
            }

            //create the writer
            BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(iFolderLocation, iFileName)));
            //create the correct url connection
            URL urlDbDownload = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot.fasta.gz");

            URLConnection urlcDbDownload = urlDbDownload.openConnection();
            InputStream isDbDownload = urlcDbDownload.getInputStream();
            GZIPInputStream lZippedInputstream = new GZIPInputStream(isDbDownload);

            System.out.println("Downloading and unzipping the database");

            int lProteinCounter = 0;
            int entry;
            String lHeader = "";
            String lSequence = "";
            boolean lParsingHeader = false;
            boolean lParsingSequence = false;
            while ((entry = lZippedInputstream.read()) != -1) {
                if ((char) entry == '\n') {
                    if (lParsingHeader) {
                        lParsingHeader = false;
                        lParsingSequence = true;
                    }
                } else if ((char) entry == '>') {
                    if (lParsingSequence) {
                        lParsingHeader = true;
                        lParsingSequence = false;
                        if (this.writeProtein(lDbOutputWriter, lHeader, lSequence)) {
                            iProteinUsedCounter++;
                        }
                        lHeader = "";
                        lSequence = "";
                        lProteinCounter++;
                        if (lProteinCounter % 100 == 0) {
                            if (iProgressBar != null) {
                                if (iTaxonomyFilter != null) {
                                    iProgressBar.setString("Selected and written '" + iProteinUsedCounter + "' " + iTaxonomyFilter + " proteins of the totale " + lProteinCounter + " checked Swiss-Prot proteins!");
                                } else {
                                    iProgressBar.setString("Written " + lProteinCounter + " Swiss-Prot proteins!");
                                }
                            }
                        }
                        if (lProteinCounter % 500 == 0) {
                            System.out.print(".");
                            lDbOutputWriter.flush();
                        }
                        if (lProteinCounter % 40000 == 0) {
                            System.out.print("\n");
                            lDbOutputWriter.flush();
                        }
                    }
                    lParsingSequence = false;
                    lParsingHeader = true;
                    lHeader = lHeader + (char) entry;
                } else if (lParsingHeader) {
                    lHeader = lHeader + (char) entry;
                } else if (lParsingSequence) {
                    lSequence = lSequence + (char) entry;
                }
            }
            //flush and close the db
            if(lHeader.length() != 0 && lSequence.length() != 0){
                this.writeProtein(lDbOutputWriter, lHeader, lSequence);
            }
            lDbOutputWriter.flush();
            lDbOutputWriter.close();
            System.out.println("\nDone downloading and unzipping");


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * This method will delete the directory and all subdirectories and files from this directory
     *
     * @param dir File to delete sub elements in
     * @return boolean true if no errors occured
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    /**
     * This method will parse the uniprot release notes
     *
     * @param lRelNotes
     */
    public void parseReleaseNotes(String lRelNotes) {
        iUniprotRelease = lRelNotes.substring(lRelNotes.indexOf("Knowledgebase Release ") + 22, lRelNotes.indexOf(" consists "));
        iSprotRelease = lRelNotes.substring(lRelNotes.indexOf("Swiss-Prot Release ") + 19, lRelNotes.indexOf(" of", lRelNotes.indexOf("Swiss-Prot Release ")));
        iTremblRelease = lRelNotes.substring(lRelNotes.indexOf("TrEMBL Release ") + 15, lRelNotes.indexOf(" of", lRelNotes.indexOf("TrEMBL Release ")));
    }


    /**
     * This method will write the protein if the taxonomy is correct
     *
     * @param lOutputWriter The writer
     * @param lHeader       The fasta protein header
     * @param lSequence     The sequence
     * @throws IOException
     */
    public boolean writeProtein(BufferedWriter lOutputWriter, String lHeader, String lSequence) throws IOException {

        boolean lUse = true;
        if (iTaxonomyFilter != null) {
            //System.out.println(lHeader);
            String lParsedTaxonomy;
            if (lHeader.indexOf(" GN=") > 0) {
                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" GN="));
            } else {
                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" PE="));
            }
            if (lParsedTaxonomy.equalsIgnoreCase(iTaxonomyFilter)) {
                lUse = true;
            } else {
                lUse = false;
            }
        }
        if (lUse) {
            lOutputWriter.write(lHeader + "\n" + lSequence + "\n");
        }
        return lUse;

    }
}
