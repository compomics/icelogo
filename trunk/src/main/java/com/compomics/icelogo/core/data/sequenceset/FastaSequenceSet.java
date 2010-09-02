package com.compomics.icelogo.core.data.sequenceset;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.interfaces.ISequenceSet;
import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.compomics.util.protein.Protein;

import java.io.File;
import java.io.IOException;

/**
 * This class represents a Fasta file as a SequenceSet.
 */
public class FastaSequenceSet implements ISequenceSet {
// ------------------------------ FIELDS ------------------------------

    /**
     * The complete fasta filename (incl path!).
     */
    private String iFileName;
    /**
     * The DBLoader processing the fasta file.
     */
    private DBLoader iLoader;
    /**
     * The identifier for this SequenceSet.
     */
    private String iID;
    /**
     * The number of sequences in the fasta file (lazy cached).
     */
    private int iNumberOfSequences = -1;
    private Protein iActiveProtein;
    private File iFile = null;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructs a new FastaSequenceSet from the given filename.
     *
     * @param aFileName The complete fasta filename (incl path!).
     * @param aID       The identifier String.
     */
    public FastaSequenceSet(final String aFileName, final String aID) {
        iFileName = aFileName;
        iID = aID;
        initialize();
    }

    /**
     * Constructs a new FastaSequenceSet  from the given file handle.
     *
     * @param aFile The fasta file.
     * @param aID   The identifier String.
     */
    public FastaSequenceSet(final File aFile, final String aID) {
        iFile = aFile;
        iFileName = aFile.getAbsolutePath();
        iID = aID;
        initialize();
    }

    /**
     * Private initialization method constructs the DBLoader from the fasta file.
     */
    private void initialize() {
        try {
            iLoader = new FASTADBLoader();
            iLoader.load(iFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * {@inheritDoc}
     */
    public String getID() {
        return iID;
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfSequences() {
        // Lazy cache.
        if (iNumberOfSequences == -1) {
            try {
                iNumberOfSequences = ((Long) (iLoader.countNumberOfEntries())).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return iNumberOfSequences;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (iLoader != null) {
            iLoader.close();
        }
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ISequenceSet ---------------------


    public PartialSequenceSet derivePartialSequenceSet(int aSubSetSize) {
        return new PartialSequenceSet(this, aSubSetSize);
    }

    /**
     * Returns a RegionalFastaSequenceSet based on this FastaSequenceSet.
     *
     * @param aAminoAcidEnums The regional anchor Amino Acids
     * @param aPrefix         The number of amino acids to precede the given AA's.
     * @param aSuffix         The number of amino acids to follow the given AA's.
     * @return The RegionalFastaSequenceSet
     */
    public RegionalFastaSequenceSet deriveRegionalSequenceSet(AminoAcidEnum[] aAminoAcidEnums, int aPrefix, int aSuffix) {
        return new RegionalFastaSequenceSet(aAminoAcidEnums, this, aPrefix, aSuffix);
    }

    /**
     * {@inheritDoc}
     */
    public String nextSequence() {
        String lResult = null;

        Protein lProtein = nextProtein();
        if (lProtein != null) {
            lResult = lProtein.getSequence().getSequence();
        }

        return lResult;
    }

    /**
     * Returns the next Protein in-line with the FastaSequenceSet iteration.
     *
     * @return
     */
    public Protein nextProtein() {
        iActiveProtein = null;
        try {
            iActiveProtein = iLoader.nextProtein();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iActiveProtein;

    }

    /**
     * {@inheritDoc} Calls the nextSequence method, directly followed by a reset.
     *
     * @return A testSequence.
     */
    public String getTestSequence() {
        String s = nextSequence();
        reset();
        return s;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        if (iLoader != null) {
            try {
                iLoader.reset();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * Run a fasta sequenceset.
     *
     * @param args
     */
    public static void main(String[] args) {
        String lFileName = args[0];
        String lId = args[1];

        FastaSequenceSet lFastaSequenceSet = new FastaSequenceSet(lFileName, lId);
        for (int i = 0; i < 3; i++) {
            System.out.println("Run " + i);
            PartialSequenceSet lSet = lFastaSequenceSet.derivePartialSequenceSet(400);
            String lSequence = null;
            int lCounter = 0;

            while (lCounter < 300) {

                lSequence = lSet.nextSequence();
                lSequence.toUpperCase();

                if (lSequence.indexOf('Y') == -1 | lSequence.length() < 100) {
                    continue;
                }

                boolean run = true;
                int lTyrIndex = -1;
                for (int j = 0; j < 3; j++) {
                    // Give it three trials to get a tyr peptide..
                    int lOffset = Math.round(new Float(lSequence.length() * Math.random()));
                    lTyrIndex = lSequence.indexOf('Y', lOffset);
                    if (lTyrIndex != -1 & (lTyrIndex + 10 < lSequence.length()) & lTyrIndex > 10) {
                        // Keep the index!
                        System.out.println(lSequence.substring(lTyrIndex - 10, lTyrIndex + 10));
                        lCounter++;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Constructs a test dbloader and calls the 'canReadFile' method.
     *
     * @return
     */
    public static boolean test(File aFile) {
        DBLoader loader = new FASTADBLoader();
        return loader.canReadFile(aFile);
    }

    /**
     * Returns the latest active Protein.
     *
     * @return
     */
    public Protein getActiveProtein() {
        return iActiveProtein;
    }

    /**
     * Get the file handle for the original fasta file.
     *
     * @return File instance to the fasta file.
     */
    public File getFastaFile() {
        if (iFile != null) {
            return iFile;
        } else {
            return new File(iFileName);
        }
    }

    public void setFile(final File aFile) {
        iFile = aFile;
    }
}
