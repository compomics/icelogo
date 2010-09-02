package com.compomics.icelogo.core.data.sequenceset;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 29-aug-2008 Time: 10:14:10 The 'PositionFastaSequenceSet ' class was
 * created for
 */
public class PositionFastaSequenceSet extends ConditionalFastaSequenceSet {
// ------------------------------ FIELDS ------------------------------

    private char[] iSequenceElements;
    private int iPosition;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructs a new FastaSequenceSet from the given filename.
     *
     * @param aFileName   The complete fasta filename (incl path!).
     * @param aID         The identifier String.
     * @param aAminoAcids The aminoacid that must be matched
     * @param aPosition   The position that must be inspected (<b>0-based!</b>)
     */
    public PositionFastaSequenceSet(final String aFileName, final String aID, final char[] aAminoAcids, final int aPosition) {
        super(aFileName, aID);
        iSequenceElements = aAminoAcids;
        iPosition = aPosition;
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ISequenceSet ---------------------


    public PartialSequenceSet derivePartialSequenceSet(final int aSubSetSize) {
        // For the partial sequenceset, the number of parent sequences is a must to construct the random index.
        // Therefor, upon deriving the FastaSequenceSet - do a first iteration to observe the number of sequences passing the condition and set that as the parent size.
        initiateNumberOfSequencesToInclude();
        return super.derivePartialSequenceSet(aSubSetSize);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getID() {
        StringBuffer sb = new StringBuffer();
        sb.append("Parent:").append(super.getID()).append("\tAll sequences have ");

        for (char lSequenceElement : iSequenceElements) {
            sb.append(" '").append(lSequenceElement).append("' ");
        }

        sb.append(" at position '").append(iPosition + 1).append("'.");

        return sb.toString();
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * {@inheritDoc} Returns true the sequence if its 'iPosition' equals one of the preset 'iSequenceElements'.
     */
    protected boolean include(final String aSequence) {
        boolean boolInclude = false;
        if (aSequence != null) {
            for (char lAminoAcid : iSequenceElements) {
                if (lAminoAcid == aSequence.charAt(iPosition)) {
                    boolInclude = true;
                    break;
                }
            }
        }
        return boolInclude;
    }
}
