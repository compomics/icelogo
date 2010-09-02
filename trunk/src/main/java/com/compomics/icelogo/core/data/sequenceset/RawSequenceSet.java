package com.compomics.icelogo.core.data.sequenceset;

import com.compomics.icelogo.core.interfaces.ISequenceSet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents a raw set of sequences as a SequenceSet.
 */
public class RawSequenceSet implements ISequenceSet {
// ------------------------------ FIELDS ------------------------------

    /**
     * This List owns all the sequences of this SequenceSet.
     */
    private ArrayList<String> iRawSequences = new ArrayList<String>();
    /**
     * A class index for itaration of the parent's sequences.
     */
    private int iterationIndex = 0;
    /**
     * The identifier for this SequenceSet.
     */
    private String iID = null;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructs a RawSequenceSet, ready to store sequences.
     *
     * @param aID The identifier String.
     */
    public RawSequenceSet(final String aID) {
        iID = aID;
    }

    /**
     * Constructs a RawSequenceSet from a collection of sequences. Still, more sequences can be added later.
     *
     * @param aCollection The Collection with sequences to be stored in this RawSequenceSet.
     * @param aID         The identifier String.
     */
    public RawSequenceSet(final Collection<String> aCollection, final String aID) {
        iID = aID;
        iRawSequences.addAll(aCollection);
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * {@inheritDoc}
     */
    public String getID() {
        return iID;  //To change body of implemented methods use File | Settings | File Templates.
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ISequenceSet ---------------------


    public PartialSequenceSet derivePartialSequenceSet(final int aSubSetSize) {
        return new PartialSequenceSet(this, aSubSetSize);
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfSequences() {
        return iRawSequences.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public String nextSequence() {
        String lResult = null;
        if (iterationIndex < iRawSequences.size()) {
            lResult = iRawSequences.get(iterationIndex);
            iterationIndex++;
        }

        return lResult;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        iterationIndex = 0;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Add a Sequence into the RawSequenceSet.
     *
     * @param aEntry The String to be added.
     */
    public void add(String aEntry) {
        iRawSequences.add(aEntry);
    }

    /**
     * Removes all sequences from the set.
     */
    public void clearContent() {
        iRawSequences.clear();
    }
}
