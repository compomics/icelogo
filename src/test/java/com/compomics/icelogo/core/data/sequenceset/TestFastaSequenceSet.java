package com.compomics.icelogo.core.data.sequenceset;
/**
 * Created by IntelliJ IDEA.
 *
 * User: Kenny
 * Date: 27-aug-2008
 * Time: 13:24:56
 *
 *
 * The 'TestFastaSequenceSet ' class was created for
 */

import junit.TestCaseLM;
import junit.framework.Assert;

public class TestFastaSequenceSet extends TestCaseLM {
    FastaSequenceSet iFastaSequenceSet;

    public TestFastaSequenceSet() {
        super("Testscenario for class FastaSequenceSet'.");
        iFastaSequenceSet = new FastaSequenceSet(getFullFilePath("fasta_db_1.fasta"), "Test fasta db 1");
    }

    public void testNextSequence() {
        Assert.assertEquals("MARGFKQRLI", iFastaSequenceSet.nextSequence());
        Assert.assertEquals("MAGARSTTAA", iFastaSequenceSet.nextSequence());
        Assert.assertEquals(5, iFastaSequenceSet.getNumberOfSequences());
        iFastaSequenceSet.reset();
        Assert.assertEquals("MARGFKQRLI", iFastaSequenceSet.nextSequence());
    }
}