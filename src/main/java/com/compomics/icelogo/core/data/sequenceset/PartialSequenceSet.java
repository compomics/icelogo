package com.compomics.icelogo.core.data.sequenceset;

import com.compomics.icelogo.core.interfaces.ISequenceSet;
import org.apache.commons.math.random.RandomDataImpl;

import javax.swing.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class extends the FastaSequenceSet as it randomly represents only a part of the fasta database. <br>For example,
 * if Swissprot has about 6000 yeast entries, this class allows you to randomly only use an absolute number 'n' out of
 * these. If n equals '100', then '100' entries are chosen randomly out of the complete fasta file.
 */
public class PartialSequenceSet implements ISequenceSet {
// ------------------------------ FIELDS ------------------------------

    /**
     * Instance RandomNumberGenerator.
     */
    protected RandomDataImpl iRandom = new RandomDataImpl();
    /**
     * The parent SequenceSet.
     */
    protected ISequenceSet iSequenceSet;
    /**
     * The number of sequences that are used from the parent fasta file.
     */
    protected int iNumberOfSequences = 100;
    /**
     * A random set of integers created while initializing the instance. <br>The integers vary between 0 and the number
     * of sequences in the parent's number of sequences. <br>Upon iterating the parent's fasta file entries, only these
     * indexes are returned by the PartialFastaSequenceSet instance.
     */
    protected Set<Integer> iIndexSet = new TreeSet<Integer>();
    /**
     * A class index for itaration of the parent's sequences.
     */
    protected int iIterationIndex = 0; // Iteration is 0-based!

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Creates a new PartialFastaSequenceSet. This class extends the FastaSequenceSet, this extension randomly
     * represents only a part of the parent's fasta database. <br>For example, if Swissprot has about 6000 yeast
     * entries, this class allows you to randomly only use an absolute number 'n' out of these. If n equals '100', then
     * '100' entries are chosen randomly out of the complete fasta file.
     *
     * @param aSequenceSet The SequenceSet parent wherefrom this partial SequenceSet was derived.
     *                     <b>aSequenceSet.reset() is called upon initiation!</b>
     * @param aSubsetSize  The absolute number of sequences that should be used (randomnly) from the fasta database.
     */
    public PartialSequenceSet(final ISequenceSet aSequenceSet, final int aSubsetSize) {
        iSequenceSet = aSequenceSet;
        iSequenceSet.reset();
        iNumberOfSequences = aSubsetSize;
        buildRandomIndexSet();
    }

    /**
     * Private method builds the indexes for the partial sequenceset. <br>The class variable 'iIndexSet' stores random
     * ints between '0' and the number of sequences in the parent fasta file. The 'nextSequence()' method then makes use
     * of this index by only returning parent sequences if the class variable 'iIterationIndex' is also in this
     * 'iIndexSet'.
     */
    protected void buildRandomIndexSet() {
        iIndexSet = new TreeSet<Integer>();
        int lNumberOfParentSequences = getParentNumberOfSequences();

        if (lNumberOfParentSequences < iNumberOfSequences) {
            JOptionPane.showMessageDialog(new JFrame(), new String[]{"The database you selected has " + lNumberOfParentSequences + " entries, your experimentel set has " + iNumberOfSequences + " entries!\nIt's impossible to sample from this database.\niceLogo will close."}, "Warning!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        for (int i = 0; i < iNumberOfSequences; i++) {
            while (!iIndexSet.add(iRandom.nextSecureInt(0, lNumberOfParentSequences - 1))) {
                // While continues as long a unique (0-BASED!) integers are stored in the IndexSet.
            }
        }
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * {@inheritDoc}
     */
    public int getNumberOfSequences() {
        return iNumberOfSequences;
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ISequenceSet ---------------------


    public PartialSequenceSet derivePartialSequenceSet(final int aSubSetSize) {
        return new PartialSequenceSet(this, aSubSetSize);
    }

    /**
     * {@inheritDoc}
     */
    public String getID() {
        return iSequenceSet.getID() + ":" + getNumberOfSequences() + " samples (" + Math.round(new Float(getSubsetPercentageOfFastaSequenceSet() * 100)) + "% of database).";    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public String nextSequence() {
        String lResult = null;
        do {
            // Get the next Sequence ..
            lResult = iSequenceSet.nextSequence();
            // While the IndexSet does not contain the increasing IterationIndex ..
        } while (!iIndexSet.contains(iIterationIndex++) && lResult != null);

        return lResult;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Returns the percentage of the partial subset within the complete set.
     *
     * @return double percentage
     */
    public double getSubsetPercentageOfFastaSequenceSet() {
        return (double) getNumberOfSequences() / (double) getParentNumberOfSequences();
    }

    /**
     * Set the the absolute number of sequences that should be used (randomnly) from the fasta file.
     * <br><br><b>Note,</b> this call also resets the SequenceSet!
     *
     * @param aNumberOfSequences The number of sequences.
     */
    public void setNumberOfSequences(final int aNumberOfSequences) {
        assert (aNumberOfSequences < getParentNumberOfSequences()); // Partial must be smaller then complete!

        iNumberOfSequences = aNumberOfSequences;
        reset();
    }

    /**
     * Returns the number of sequences of the parent fasta file.
     *
     * @return
     */
    public int getParentNumberOfSequences() {
        return iSequenceSet.getNumberOfSequences();
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        iSequenceSet.reset();
        iIterationIndex = 0;
        buildRandomIndexSet();
    }
}
