package com.compomics.icelogo.core.data.sequenceset;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 26-aug-2008
 * Time: 10:02:51
 * To change this template use File | Settings | File Templates.
 */

import junit.TestCaseLM;
import junit.framework.Assert;

public class TestPartialSequenceSet extends TestCaseLM {
    PartialSequenceSet iPartialFastaSequenceSet;
    private int iSubsetSize;

    public TestPartialSequenceSet() {
        super("TestCase for PartialSequenceSet");
        iSubsetSize = 3;

        FastaSequenceSet lFastaSequenceSet =
                new FastaSequenceSet(getFullFilePath("fasta_db_1.fasta"), "fasta_db_1.fasta");
        iPartialFastaSequenceSet =
                lFastaSequenceSet.derivePartialSequenceSet(3);
    }

    public void testPartialFastaSequenceSet() throws Exception {
        assertEquals(5, iPartialFastaSequenceSet.getParentNumberOfSequences());
        assertEquals(3, iPartialFastaSequenceSet.getNumberOfSequences());
        assertEquals(0.6, iPartialFastaSequenceSet.getSubsetPercentageOfFastaSequenceSet());
    }

    public void testNextSequence() throws Exception {
        iPartialFastaSequenceSet.reset();
        int lCounter = 0;
        while (iPartialFastaSequenceSet.nextSequence() != null) {
            lCounter++;
        }
        Assert.assertEquals(iSubsetSize, lCounter);
    }

    public void testPartialPositionSequenceSet() {
        iPartialFastaSequenceSet =
                new PositionFastaSequenceSet(getFullFilePath("fasta_db_1.fasta"), "Test fasta db 1", new char[]{'F', 'A'}, 9).derivePartialSequenceSet(1);

        String s = iPartialFastaSequenceSet.nextSequence();
        if ((!s.equals("MAQNNFAFKF")) && (!s.equals("MAGARSTTAA"))) {
            fail();
        }
        Assert.assertNull(iPartialFastaSequenceSet.nextSequence());
    }

}