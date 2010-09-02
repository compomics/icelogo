package com.compomics.icelogo.core.interfaces;

import com.compomics.icelogo.core.data.sequenceset.PartialSequenceSet;

/**
 * This interface defines the behaviour of a SequenceSet
 */
public interface ISequenceSet {
// -------------------------- OTHER METHODS --------------------------

    PartialSequenceSet derivePartialSequenceSet(int aSubSetSize);

    /**
     * Returns an identifier for this set
     *
     * @return String identifying this set
     */
    public String getID();

    /**
     * Returns the number of sequences availlable in this set.
     *
     * @return int number of sequences
     */
    public int getNumberOfSequences();

    /**
     * Returns the next sequence in this set
     *
     * @return String as the next sequence
     */
    public String nextSequence();

    /**
     * Resets the SequenceSet to its initial start point. <br>For example, a SequenceSet with 10 sequences should always
     * return the first sequence in answer to the nextSequence() method if the reset() method was called.
     */
    public void reset();

}
