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
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.factory.AminoAcidFactory;

import java.util.ArrayList;

public class TestRegionalSequenceSet extends TestCaseLM {
    RegionalFastaSequenceSet iRegionalFastaSequenceSet;
    private AminoAcidEnum[] iTestAminoAcids;

    public TestRegionalSequenceSet() {
        super("TestCase for PartialSequenceSet");




        FastaSequenceSet lFastaSequenceSet =
                new FastaSequenceSet(getFullFilePath("fasta_db_1.fasta"), "fasta_db_1.fasta");


        // Create a AA container.
        ArrayList<AminoAcidEnum> lAminoAcidEnumArrayList = new ArrayList<AminoAcidEnum>();
        String lSequence = null;

        iTestAminoAcids = new AminoAcidEnum[]{AminoAcidEnum.ALA, AminoAcidEnum.PRO};
        iRegionalFastaSequenceSet =
                lFastaSequenceSet.deriveRegionalSequenceSet(iTestAminoAcids, 1,1);
    }

    public void testRegionalFastaSequenceSet() throws Exception {
        assertEquals(5, iRegionalFastaSequenceSet.getParentNumberOfSequences());
        assertEquals(2, iRegionalFastaSequenceSet.getNumberOfSequences());
        assertEquals(0.4, iRegionalFastaSequenceSet.getSubsetPercentageOfFastaSequenceSet());
    }

    public void testNextSequence() throws Exception {
        iRegionalFastaSequenceSet.reset();
        // Iterate over the sequences.
        int lCounter = 0;
        while (iRegionalFastaSequenceSet.nextSequence() != null) {
            lCounter++;
        }

        Assert.assertEquals(iTestAminoAcids.length, lCounter);
    }


    public void testNextSequence2() throws Exception {
        iRegionalFastaSequenceSet.reset();


        String lSequence = iRegionalFastaSequenceSet.nextSequence();
        // The first sequence should have a centered Ala
        Assert.assertEquals(lSequence.length(), 3);
        Assert.assertEquals('A', lSequence.charAt(1));


        lSequence = iRegionalFastaSequenceSet.nextSequence();
        // The second sequence should have a centered Ala
        Assert.assertEquals(lSequence.length(), 3);
        Assert.assertEquals('P', lSequence.charAt(1));
        Assert.assertEquals("IPT", lSequence);

        Assert.assertNull(iRegionalFastaSequenceSet.nextSequence());

    }



}