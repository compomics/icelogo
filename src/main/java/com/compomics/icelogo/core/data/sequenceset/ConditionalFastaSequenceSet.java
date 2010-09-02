package com.compomics.icelogo.core.data.sequenceset;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 29-aug-2008 Time: 9:59:38 The 'ConditionalAminoAcidSequenceSet ' class
 * was created for
 */
public abstract class ConditionalFastaSequenceSet extends FastaSequenceSet {
// ------------------------------ FIELDS ------------------------------

    protected int iEstimatedNumberOfSequences = 1;
    private int iTotalSequenceCounter = 0;
    private int iNumberOfIncludedSequences = -1;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructs a new FastaSequenceSet from the given filename.
     *
     * @param aFileName The complete fasta filename (incl path!).
     * @param aID       The identifier String.
     */
    public ConditionalFastaSequenceSet(final String aFileName, final String aID) {
        super(aFileName, aID);
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ISequenceSet ---------------------


    public int getNumberOfSequences() {
        if (iNumberOfIncludedSequences == -1) {
            // If the number of sequences was not calculated for this conditional sequenceset, build it constructively.
            // Always assume there will be one condition more fullfiled - as long as the parent has more sequences!
            if (iTotalSequenceCounter < super.getNumberOfSequences()) {
                return iEstimatedNumberOfSequences;
            } else {
                return iEstimatedNumberOfSequences - 1;
            }
        } else {
            return iNumberOfIncludedSequences;
        }
    }

    /**
     * {@inheritDoc} The given accomplishes for a specific condition.
     */
    public String nextSequence() {
        String lSequence = null;
        do {
            lSequence = super.nextSequence();
            iTotalSequenceCounter++;
        } while (lSequence != null & !include(lSequence));

        if (lSequence != null) {
            // Upon the last parent sequence, this counter will not increase anymore!
            iEstimatedNumberOfSequences++;
        }

        return lSequence;
    }

// -------------------------- OTHER METHODS --------------------------

    protected void initiateNumberOfSequencesToInclude() {
        reset();

        iNumberOfIncludedSequences = 0;

        String lSequence = null;
        while ((lSequence = super.nextSequence()) != null) {
            if (include(lSequence)) {
                iNumberOfIncludedSequences++;
            }
        }
    }

    /**
     * This method must implement the condition which the protein sequence must pass for.
     *
     * @param aSequence The sequence that must be inspected whether to be included or not.
     * @return boolean - The status of this sequence whether passing or failing to meet the required condition.
     */
    protected abstract boolean include(String aSequence);
}
