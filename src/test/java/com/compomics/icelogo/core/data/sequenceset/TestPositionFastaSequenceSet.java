package com.compomics.icelogo.core.data.sequenceset;
/**
 * Created by IntelliJ IDEA.
 *
 * User: Kenny
 * Date: 29-aug-2008
 * Time: 10:20:59
 *
 *
 * The 'TestPositionFastaSequenceSet ' class was created for
 */

import junit.TestCaseLM;
import junit.framework.Assert;

public class TestPositionFastaSequenceSet extends TestCaseLM {
    PositionFastaSequenceSet iPositionFastaSequenceSet;

    /*
    fasta_db_1.fasta:

    >prot1
    MARGFKQRLI
    >prot2
    MAGARSTTAA
    >prot3
    MAQNNFAFKF
    >prot4
    MAATTPINID
    >prot5
    AFKPVDFSET
    */

    public TestPositionFastaSequenceSet() {
        super("Testscenario for class PositionFastaSequenceSet'.");
    }

    public void testInclude() {
        iPositionFastaSequenceSet =
                new PositionFastaSequenceSet(getFullFilePath("fasta_db_1.fasta"), "Test fasta db 1", new char[]{'F'}, 1);
        Assert.assertEquals("AFKVVDFSET", iPositionFastaSequenceSet.nextSequence());
        Assert.assertNull(iPositionFastaSequenceSet.nextSequence());
    }


    public void testInclude2() {
        iPositionFastaSequenceSet =
                new PositionFastaSequenceSet(getFullFilePath("fasta_db_1.fasta"), "Test fasta db 1", new char[]{'M'}, 0);
        int lCount = 0;
        while (iPositionFastaSequenceSet.nextSequence() != null) {
            lCount++;
        }
        Assert.assertEquals(4, lCount);
    }

    public void testInclude3() {
        iPositionFastaSequenceSet =
                new PositionFastaSequenceSet(getFullFilePath("fasta_db_1.fasta"), "Test fasta db 1", new char[]{'F', 'A'}, 9);
        int lCount = 0;
        while (iPositionFastaSequenceSet.nextSequence() != null) {
            lCount++;
        }
        Assert.assertEquals(2, lCount);
    }


}