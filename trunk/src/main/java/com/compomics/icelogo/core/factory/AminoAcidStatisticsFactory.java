package com.compomics.icelogo.core.factory;

import com.compomics.icelogo.core.data.AminoAcidCounter;
import com.compomics.icelogo.core.data.FixedAminoAcidStatistics;
import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.data.sequenceset.PartialSequenceSet;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.SamplingDirectionEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.interfaces.ISamplingStrategy;
import com.compomics.icelogo.core.interfaces.ISequenceSet;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;

import java.util.HashMap;
import java.util.Vector;

/**
 * This class is a Factory creating AminoAcid matrices from SequenceSet implementing instances.
 */
public class AminoAcidStatisticsFactory {
// ------------------------------ FIELDS ------------------------------


// -------------------------- STATIC METHODS --------------------------

    /**
     * Returns a Experimental AminoAcidMatrix instance for the given SequenceSet. <br>The first AminoAcidCounter of the
     * Matrix refers to the <b>offset</b> position of each Sequence. <br> <br> As a reference set, the mean and SD are
     * created '<b>horizontal</b>' along the sequence from position 1 up to position 30 from a single sequenceset. <br>
     * <table border=0 cellpadding=0 cellspacing=0 width=447 style='border-collapse:
     * collapse;table-layout:fixed;width:335pt'> <col width=127 style='mso-width-source:userset;mso-width-alt:4644;width:95pt'>
     * <col width=64 span=5 style='width:48pt'> <tr height=20 style='height:15.0pt'> <td height=20 class=xl1530331
     * width=127 style='height:15.0pt;width:95pt'>AminoAcidCounter</td> <td class=xl6330331 width=64
     * style='width:48pt'>1</td> <td class=xl6330331 width=64 style='width:48pt'>2</td> <td class=xl6330331 width=64
     * style='width:48pt'>3</td> <td class=xl6330331 width=64 style='width:48pt'>..</td> <td class=xl6330331 width=64
     * style='width:48pt'>30</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20 class=xl1530331
     * style='height:15.0pt'>SequenceSet</td> <td class=xl6330331>A</td> <td class=xl6330331>A</td> <td
     * class=xl6330331>A</td> <td class=xl6330331>..</td> <td class=xl6330331>A</td> </tr> <tr height=20
     * style='height:15.0pt'> <td height=20 class=xl1530331 style='height:15.0pt'>Sequence Position</td> <td
     * class=xl6330331>1</td> <td class=xl6330331>2</td> <td class=xl6330331>3</td> <td class=xl6330331>..</td> <td
     * class=xl6330331>30</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20 class=xl1530331
     * style='height:15.0pt'>Ala %</td> <td class=xl6330331>5.5</td> <td class=xl6330331>5.7</td> <td
     * class=xl6330331>5.4</td> <td class=xl6330331>..</td> <td class=xl6330331>5.5</td> </tr> <![if
     * supportMisalignedColumns]> <tr height=0 style='display:none'> <td width=127 style='width:95pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> </tr> <![endif]> </table>
     *
     * @param aSequenceSet SequenceSet implementing instance.
     * @param aLength      number of AminoAcidCounters. This is equals the number of positions that are counted for
     *                     each protein.
     * @param aOffset      Integer start site in each protein. This value is <b>0-based</b>, so if '10' is passed as a
     *                     parameter, the first AminoAcidcounter is generated at the 11th Position of each Protein.
     * @return AminoAcidMatrix instance for the given parameters.
     */
    public static MatrixAminoAcidStatistics createHorizontalPositionAminoAcidMatrix(ISequenceSet aSequenceSet, int aLength, int aOffset) {
        /**
         * Declare & initialize the AminoAcidCounters
         */
        Vector<AminoAcidCounter> lAminoAcidCounters = new Vector<AminoAcidCounter>(aLength);
        for (int i = 0; i < aLength; i++) {
            lAminoAcidCounters.add(new AminoAcidCounter());
        }

        /**
         * Fill the AminoAcidCounters.
         */
        for (int i = 0; i < aSequenceSet.getNumberOfSequences(); i++) {
            String lSequence = aSequenceSet.nextSequence();
            if (lSequence != null) { // Conditional SequenceSets are unpredictable to return a null at the end!
                if (lSequence.length() >= lAminoAcidCounters.size()) {
                    int lAminoAcidCounterIndex = aOffset; // Offset is 0-based!
                    for (AminoAcidCounter lAminoAcidCounter : lAminoAcidCounters) {
                        if (lAminoAcidCounterIndex < lSequence.length()) {
                            char lAminoAcidChar = lSequence.charAt(lAminoAcidCounterIndex);
                            lAminoAcidCounter.count(lAminoAcidChar);
                            lAminoAcidCounterIndex++;
                        } else {
                            // No more residues for this sequence!
                            // @todo - introduce the option (Strategy?) to ignore this sequence entry if it cannot feed all AminoAcidCounters.
                            break;
                        }
                    }
                }
            }
        }

        return new MatrixAminoAcidStatistics(lAminoAcidCounters, aSequenceSet.getID());
    }


    /**
     * Returns a Randomized AminoAcidMatrix instance for the given SequenceSet <br>For each sequence, a random AminoAcid
     * is sampled using the <i>SamplingStrategy</i> currently set at the Factory.
     *
     * @param aSequenceSet   implementing instance.
     * @param aSamplingSize  number of AminoAcidCounters. This is equals the number of positions that are counted for
     *                       each protein.
     * @param aIterationSize
     * @return AminoAcidMatrix instance for the given parameters.
     */
    public static MatrixAminoAcidStatistics createRandomSampleAminoAcidMatrix(ISequenceSet aSequenceSet, int aSamplingSize, int aIterationSize, ISamplingStrategy aSamplingStrategy) {
        /**
         * Declare & initialize the AminoAcidCounters
         */
        Vector<AminoAcidCounter> lAminoAcidCounters = new Vector<AminoAcidCounter>(aIterationSize);

        for (int i = 0; i < aIterationSize; i++) {
            lAminoAcidCounters.add(new AminoAcidCounter());
        }

        /**
         * Fill the AminoAcidCounters.
         * Derive a new sequenceSet for each amino acid counter.
         */
        for (AminoAcidCounter lAminoAcidCounter : lAminoAcidCounters) {
            // Sample n (where n equals the number of AminoAcidCounters - samplesize) times from each protein sequence.
            ISequenceSet lSequenceSet = aSequenceSet.derivePartialSequenceSet(aSamplingSize);
            long lNumberOfEntries = lSequenceSet.getNumberOfSequences();
            for (int i = 0; i < lNumberOfEntries; i++) {
                String lSequence = lSequenceSet.nextSequence();
                char lAminoAcidChar = aSamplingStrategy.sample(lSequence);
                lAminoAcidCounter.count(lAminoAcidChar);
            }
        }

        return new MatrixAminoAcidStatistics(lAminoAcidCounters, aSequenceSet.getID());
    }

    /**
     * Returns a Experimental AminoAcidMatrix instance for the given SequenceSet. <br>The first AminoAcidCounter of the
     * Matrix refers to the AminoAcid percentage at 'aVerticalPosition' in the first derived PartialSequenceSet. <br>As
     * a reference set, the mean and SD are created '<b>vertical</b>' for a fixed position by each of the sequence
     * entries from SequenceSet 1 up to SequenceSet 30. <br> <br> <table border=0 cellpadding=0 cellspacing=0 width=447
     * style='border-collapse: collapse;table-layout:fixed;width:335pt'> <col width=127
     * style='mso-width-source:userset;mso-width-alt:4644;width:95pt'> <col width=64 span=5 style='width:48pt'> <tr
     * height=20 style='height:15.0pt'> <td height=20 class=xl1530331 width=127 style='height:15.0pt;width:95pt'>AminoAcidCounter</td>
     * <td class=xl6530331 width=64 style='width:48pt'>1</td> <td class=xl6530331 width=64 style='width:48pt'>2</td> <td
     * class=xl6530331 width=64 style='width:48pt'>3</td> <td class=xl6530331 width=64 style='width:48pt'>..</td> <td
     * class=xl6530331 width=64 style='width:48pt'>30</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20
     * class=xl1530331 style='height:15.0pt'>SequenceSet</td> <td class=xl6530331>A</td> <td class=xl6530331>B</td> <td
     * class=xl6530331>C</td> <td class=xl6530331>..</td> <td class=xl6530331>AD</td> </tr> <tr height=20
     * style='height:15.0pt'> <td height=20 class=xl1530331 style='height:15.0pt'>Sequence Position</td> <td
     * class=xl6530331>2</td> <td class=xl6530331>2</td> <td class=xl6530331>2</td> <td class=xl6530331>..</td> <td
     * class=xl6530331>2</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20 class=xl1530331
     * style='height:15.0pt'>Ala %</td> <td class=xl6530331>5.5</td> <td class=xl6530331>5.7</td> <td
     * class=xl6530331>5.4</td> <td class=xl6530331>..</td> <td class=xl6530331>5.5</td> </tr> <![if
     * supportMisalignedColumns]> <tr height=0 style='display:none'> <td width=127 style='width:95pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> </tr> <![endif]> </table>
     *
     * @param aSequenceSet   The SequenceSet to derive partial sets for the different sampling iterations.
     * @param aIterationSize The number of sampling iterations.
     * @param aOffset        The start position. (0-based!)
     * @param aLength        The number of AminoAcidMatrix instances starting from position 'aOffset'.
     * @param aDirection     The Integer type for the direction as defined on this class. The direction can be from the N-term to the C-term or opposite.
     * @return An array of AminoAcidMatrix instances starting from the given 'offset' for 'aLength' positions.
     */
    public static AminoAcidStatistics[] createVerticalPositionAminoAcidMatrix(ISequenceSet aSequenceSet, int aIterationSize, int aOffset, final int aLength, final SamplingDirectionEnum aDirection) {
        int[] lPositions = new int[aLength];
        for (int i = 0; i < aLength; i++) {
            int lPosition = (int) aOffset + i;
            lPositions[i] = lPosition;
        }

        Vector[] vecMatrices = new Vector[lPositions.length];

        for (int i = 0; i < vecMatrices.length; i++) {
            /**
             * Declare & initialize the AminoAcidCounters
             */
            Vector<AminoAcidCounter> lAminoAcidCounterVector = new Vector<AminoAcidCounter>(aIterationSize);
            for (int j = 0; j < aIterationSize; j++) {
                lAminoAcidCounterVector.add(new AminoAcidCounter());
            }

            vecMatrices[i] = lAminoAcidCounterVector;
        }

        /**
         * Fill the AminoAcidCounters.
         */
        for (int samplingCounter = 0; samplingCounter < vecMatrices[0].size(); samplingCounter++) {
            // Reset the SequenceSet for each sampling round.
            aSequenceSet.reset();

            // Iterate over all sequences in the PartialSequenceSet.
            for (int j = 0; j < aSequenceSet.getNumberOfSequences(); j++) {
                String lSequence = aSequenceSet.nextSequence();
                if (lSequence != null) { // Conditional SequenceSets are unpredictable to return a null at the end!
                    // If the required vertical position is availlable in the current sequence,
                    // addComponent it to the current AminoAcidCounter, whereas 'current' reflects the current PartialSequenceSet.

                    for (int i = 0; i < lPositions.length; i++) {

                        // If we move from the C-term to the N-term, adapt the position value in relation to the Protein's C-terminus.
                        int lVerticalPosition = lPositions[i];

                        if (aDirection == SamplingDirectionEnum.CtermToNterm) {
                            lVerticalPosition = lSequence.length() - (lPositions.length - lVerticalPosition);
                        }

                        if (lVerticalPosition < lSequence.length() & lVerticalPosition >= 0) {
                            char lAminoAcidChar = lSequence.charAt(lVerticalPosition);

                            Object lAminoAcidCounter = vecMatrices[i].get(samplingCounter);
                            ((AminoAcidCounter) lAminoAcidCounter).count(lAminoAcidChar);

                        } else {
                            assert false : "Never get to this point!";
                            //System.err.println("Aminoacidposition " + lVerticalPosition + "was not used as expected..");
                        }
                    }
                }
            }
        }

        MatrixAminoAcidStatistics[] result = new MatrixAminoAcidStatistics[lPositions.length];
        for (int i = 0; i < vecMatrices.length; i++) {
            Vector<AminoAcidCounter> lVector = vecMatrices[i];
            result[i] = new MatrixAminoAcidStatistics(lVector, aSequenceSet.getID() + " Vertical position " + lPositions[i]);
        }

        return result;
    }
    

    /**
     * Returns a Experimental AminoAcidMatrix instance for the given SequenceSet. <br>The first AminoAcidCounter of the
     * Matrix refers to the AminoAcid percentage at 'aVerticalPosition' in the first derived PartialSequenceSet. <br>As
     * a reference set, the mean and SD are created '<b>vertical</b>' for a fixed position by each of the sequence
     * entries from SequenceSet 1 up to SequenceSet 30. <br> <br> <table border=0 cellpadding=0 cellspacing=0 width=447
     * style='border-collapse: collapse;table-layout:fixed;width:335pt'> <col width=127
     * style='mso-width-source:userset;mso-width-alt:4644;width:95pt'> <col width=64 span=5 style='width:48pt'> <tr
     * height=20 style='height:15.0pt'> <td height=20 class=xl1530331 width=127 style='height:15.0pt;width:95pt'>AminoAcidCounter</td>
     * <td class=xl6530331 width=64 style='width:48pt'>1</td> <td class=xl6530331 width=64 style='width:48pt'>2</td> <td
     * class=xl6530331 width=64 style='width:48pt'>3</td> <td class=xl6530331 width=64 style='width:48pt'>..</td> <td
     * class=xl6530331 width=64 style='width:48pt'>30</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20
     * class=xl1530331 style='height:15.0pt'>SequenceSet</td> <td class=xl6530331>A</td> <td class=xl6530331>B</td> <td
     * class=xl6530331>C</td> <td class=xl6530331>..</td> <td class=xl6530331>AD</td> </tr> <tr height=20
     * style='height:15.0pt'> <td height=20 class=xl1530331 style='height:15.0pt'>Sequence Position</td> <td
     * class=xl6530331>2</td> <td class=xl6530331>2</td> <td class=xl6530331>2</td> <td class=xl6530331>..</td> <td
     * class=xl6530331>2</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20 class=xl1530331
     * style='height:15.0pt'>Ala %</td> <td class=xl6530331>5.5</td> <td class=xl6530331>5.7</td> <td
     * class=xl6530331>5.4</td> <td class=xl6530331>..</td> <td class=xl6530331>5.5</td> </tr> <![if
     * supportMisalignedColumns]> <tr height=0 style='display:none'> <td width=127 style='width:95pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> </tr> <![endif]> </table>
     *
     * @param aParentSequenceSet The SequenceSet to derive partial sets for the different sampling iterations.
     * @param aIterationSize     The number of sampling iterations.
     * @param aOffset            The start position. (0-based!)
     * @param aLength            The number of AminoAcidMatrix instances starting from position 'aOffset'.
     * @param aSubSetSize        The subsetsize to create each partial set.
     * @param aDirection         The Integer type for the direction as defined on this class. The direction can be from the N-term to the C-term or opposite.
     * @return AminoAcidMatrices instances for the given parameters.
     */
    public static AminoAcidStatistics[] createVerticalPositionAminoAcidMatrix(ISequenceSet aParentSequenceSet, int aIterationSize, final int aOffset, final int aLength, int aSubSetSize, SamplingDirectionEnum aDirection) {
        int[] lPositions = new int[aLength];
        for (int i = 0; i < aLength; i++) {
            int lPosition = (int) aOffset + i;
            lPositions[i] = lPosition;
        }

        Vector[] vecMatrices = new Vector[lPositions.length];

        for (int i = 0; i < vecMatrices.length; i++) {
            /**
             * Declare & initialize the AminoAcidCounters
             */
            Vector<AminoAcidCounter> lAminoAcidCounters = new Vector<AminoAcidCounter>(aIterationSize);
            for (int j = 0; j < aIterationSize; j++) {
                lAminoAcidCounters.add(new AminoAcidCounter());
            }

            vecMatrices[i] = lAminoAcidCounters;
        }

        /**
         * Fill the AminoAcidCounters.
         */
        for (int samplingCounter = 0; samplingCounter < vecMatrices[0].size(); samplingCounter++) {
            // Derive a new PartialSequenceSet for each sampling round.
            PartialSequenceSet lPartialSequenceSet = aParentSequenceSet.derivePartialSequenceSet(aSubSetSize);

            // Iterate over all sequences in the PartialSequenceSet.
            for (int j = 0; j < lPartialSequenceSet.getNumberOfSequences(); j++) {

                String lSequence = lPartialSequenceSet.nextSequence();

                if (lSequence != null) {

                    // Conditional SequenceSets are unpredictable to return a null at the end!
                    // If the required vertical position is availlable in the current sequence,
                    // addComponent it to the current AminoAcidCounter, whereas 'current' reflects the current PartialSequenceSet.
                    for (int i = 0; i < lPositions.length; i++) {

                        // If we move from the C-term to the N-term, adapt the position value in relation to the Protein's C-terminus.
                        int lVerticalPosition = lPositions[i];

                        if (aDirection == SamplingDirectionEnum.CtermToNterm) {
                            lVerticalPosition = lSequence.length() - lVerticalPosition - 1;
                        }

                        if (lVerticalPosition < lSequence.length() & lVerticalPosition >= 0) {
                            char lAminoAcidChar = lSequence.charAt(lVerticalPosition);

                            Object lAminoAcidCounter = vecMatrices[i].get(samplingCounter);
                            ((AminoAcidCounter) lAminoAcidCounter).count(lAminoAcidChar);
                        }
                    }
                }
            }
        }

        MatrixAminoAcidStatistics[] result = new MatrixAminoAcidStatistics[lPositions.length];
        for (int i = 0; i < vecMatrices.length; i++) {
            Vector<AminoAcidCounter> lVector = vecMatrices[i];
            result[i] =
                    new MatrixAminoAcidStatistics(lVector, aParentSequenceSet.getID() + " Vertical position " + lPositions[i]);
        }

        return result;
    }

    /**
     * Returns a Experimental AminoAcidMatrix instance for the given SequenceSet. <br>The first AminoAcidCounter of the
     * Matrix refers to the AminoAcid percentage at 'aVerticalPosition' in the first derived PartialSequenceSet. <br>As
     * a reference set, the mean and SD are created '<b>vertical</b>' for a fixed position by each of the sequence
     * entries from SequenceSet 1 up to SequenceSet 30. <br> <br> <table border=0 cellpadding=0 cellspacing=0 width=447
     * style='border-collapse: collapse;table-layout:fixed;width:335pt'> <col width=127
     * style='mso-width-source:userset;mso-width-alt:4644;width:95pt'> <col width=64 span=5 style='width:48pt'> <tr
     * height=20 style='height:15.0pt'> <td height=20 class=xl1530331 width=127 style='height:15.0pt;width:95pt'>AminoAcidCounter</td>
     * <td class=xl6530331 width=64 style='width:48pt'>1</td> <td class=xl6530331 width=64 style='width:48pt'>2</td> <td
     * class=xl6530331 width=64 style='width:48pt'>3</td> <td class=xl6530331 width=64 style='width:48pt'>..</td> <td
     * class=xl6530331 width=64 style='width:48pt'>30</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20
     * class=xl1530331 style='height:15.0pt'>SequenceSet</td> <td class=xl6530331>A</td> <td class=xl6530331>B</td> <td
     * class=xl6530331>C</td> <td class=xl6530331>..</td> <td class=xl6530331>AD</td> </tr> <tr height=20
     * style='height:15.0pt'> <td height=20 class=xl1530331 style='height:15.0pt'>Sequence Position</td> <td
     * class=xl6530331>2</td> <td class=xl6530331>2</td> <td class=xl6530331>2</td> <td class=xl6530331>..</td> <td
     * class=xl6530331>2</td> </tr> <tr height=20 style='height:15.0pt'> <td height=20 class=xl1530331
     * style='height:15.0pt'>Ala %</td> <td class=xl6530331>5.5</td> <td class=xl6530331>5.7</td> <td
     * class=xl6530331>5.4</td> <td class=xl6530331>..</td> <td class=xl6530331>5.5</td> </tr> <![if
     * supportMisalignedColumns]> <tr height=0 style='display:none'> <td width=127 style='width:95pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64 style='width:48pt'></td> <td width=64
     * style='width:48pt'></td> <td width=64 style='width:48pt'></td> </tr> <![endif]> </table>
     *
     * @param aSequenceSet  The SequenceSet to derive partial sets for the different sampling iterations.
     * @param aSamplingSize The number of sampling iterations.
     * @param aOffset       The start position. (0-based!)
     * @param aLength       The number of AminoAcidMatrix instances starting from position 'aOffset'.
     * @return An array of AminoAcidMatrix instances starting from the given 'offset' for 'aLength' positions.
     */
    public static AminoAcidStatistics[] createFixedStatisticsVerticalPositionAminoAcidMatrix(ISequenceSet aSequenceSet, int aSamplingSize, int aOffset, final int aLength, int aSetSize) {
        int[] lPositions = new int[aLength];
        for (int i = 0; i < aLength; i++) {
            int lPosition = (int) aOffset + i;
            lPositions[i] = lPosition;
        }

        Vector[] vecMatrices = new Vector[lPositions.length];

        for (int i = 0; i < vecMatrices.length; i++) {
            /**
             * Declare & initialize the AminoAcidCounters
             */
            Vector<AminoAcidCounter> lAminoAcidCounters = new Vector<AminoAcidCounter>(aSamplingSize);
            for (int j = 0; j < aSamplingSize; j++) {
                lAminoAcidCounters.add(new AminoAcidCounter());
            }

            vecMatrices[i] = lAminoAcidCounters;
        }

        /**
         * Fill the AminoAcidCounters.
         */
        for (int samplingCounter = 0; samplingCounter < vecMatrices[0].size(); samplingCounter++) {
            // Reset the SequenceSet for each sampling round.
            aSequenceSet.reset();

            // Iterate over all sequences in the PartialSequenceSet.
            for (int j = 0; j < aSequenceSet.getNumberOfSequences(); j++) {
                String lSequence = aSequenceSet.nextSequence();
                if (lSequence != null) { // Conditional SequenceSets are unpredictable to return a null at the end!
                    // If the required vertical position is availlable in the current sequence,
                    // addComponent it to the current AminoAcidCounter, whereas 'current' reflects the current PartialSequenceSet.

                    for (int i = 0; i < lPositions.length; i++) {
                        int aVerticalPosition = lPositions[i];
                        if (aVerticalPosition < lSequence.length()) {
                            char lAminoAcidChar = lSequence.charAt(aVerticalPosition);

                            Object lAminoAcidCounter = vecMatrices[i].get(samplingCounter);
                            ((AminoAcidCounter) lAminoAcidCounter).count(lAminoAcidChar);
                        }
                    }
                }
            }
        }

        FixedAminoAcidStatistics[] result = new FixedAminoAcidStatistics[lPositions.length];
        for (int i = 0; i < vecMatrices.length; i++) {
            Vector<AminoAcidCounter> lVector = vecMatrices[i];
            HashMap map = new HashMap();
            //calculate the mean from the different counters (normale there will only be one)
            for (AminoAcidEnum aa : AminoAcidEnum.values()) {
                double mean = 0.0;
                for (int v = 0; v < lVector.size(); v++) {
                    AminoAcidCounter lCounter = lVector.get(v);
                    mean = mean + (lCounter.getCount(aa) / lCounter.getTotalCount());
                }
                mean = mean / lVector.size();
                map.put(aa, mean);
            }

            result[i] = new FixedAminoAcidStatistics(map, aSetSize);
        }
        return result;
    }

    /**
     * @param aSpeciesComposition The species composition
     * @param aN          The number of entries used to construct this FixedAminoAcidMatrix.
     * @return be.proteomics.logo.model.interfaces.AminoAcidMatrix The requested AminoAcidMatrix.
     */
    public static AminoAcidStatistics createFixedAminoAcidMatrix(SwissProtComposition aSpeciesComposition, int aN) {
        FixedAminoAcidStatistics lAminoAcidMatrix = null;

        String lComposition = aSpeciesComposition.getComposition();
        String[] lComps = lComposition.split("\n");
        HashMap<AminoAcidEnum, Double> lContent = new HashMap<AminoAcidEnum, Double>();
        for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
            for(int i = 0; i<lComps.length; i ++){
                if(lComps[i].startsWith(String.valueOf(lAminoAcidEnum.getOneLetterCode()))){
                    Double lValue = Double.parseDouble(lComps[i].substring(lComps[i].indexOf("= ") + 2));
                    lContent.put(lAminoAcidEnum, lValue);
                }
            }
        }

        lAminoAcidMatrix = new FixedAminoAcidStatistics(lContent, aN);

        return lAminoAcidMatrix;
    }


}
