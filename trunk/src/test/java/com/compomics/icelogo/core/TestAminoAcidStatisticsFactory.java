package com.compomics.icelogo.core;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 25-aug-2008
 * Time: 21:47:09
 * To change this template use File | Settings | File Templates.
 */

import com.compomics.icelogo.core.data.AminoAcidCounter;
import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.data.sequenceset.RawSequenceSet;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.SamplingDirectionEnum;
import com.compomics.icelogo.core.enumeration.SamplingStrategy;
import com.compomics.icelogo.core.factory.AminoAcidStatisticsFactory;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestAminoAcidStatisticsFactory extends TestCase {
    private RawSequenceSet iRawSequenceSet;

    public TestAminoAcidStatisticsFactory() {
        iRawSequenceSet = new RawSequenceSet("TestAAFactory_raw_example");
        iRawSequenceSet.add("AAAAAAAAAA");
        iRawSequenceSet.add("ADADADADAD");
        iRawSequenceSet.add("EEEEEEEEEE");
        iRawSequenceSet.add("ADADADADAD");
        iRawSequenceSet.add("AAAAAAAAAA");
    }

    public void testCreateHorizontalPositionAminoAcidMatrix() throws Exception {
        iRawSequenceSet.reset();
        MatrixAminoAcidStatistics lMatrix =
                AminoAcidStatisticsFactory.createHorizontalPositionAminoAcidMatrix(iRawSequenceSet, 10, 0);
        Assert.assertEquals(0.8, lMatrix.getAminoAcidCounter(0).getPercentage(AminoAcidEnum.ALA));
        Assert.assertEquals(0.4, lMatrix.getAminoAcidCounter(9).getPercentage(AminoAcidEnum.ALA));
        Assert.assertEquals(0.2, lMatrix.getAminoAcidCounter(1).getPercentage(AminoAcidEnum.GLU));
        Assert.assertEquals(0.0, lMatrix.getAminoAcidCounter(1).getPercentage(AminoAcidEnum.TYR));
    }

    public void testCreatePositionalOffsetAminoAcidMatrix() throws Exception {
        iRawSequenceSet.reset();
        MatrixAminoAcidStatistics lMatrix =
                AminoAcidStatisticsFactory.createHorizontalPositionAminoAcidMatrix(iRawSequenceSet, 10, 1);
        Assert.assertEquals(10, lMatrix.getDimension());
        Assert.assertEquals(0.4, lMatrix.getAminoAcidCounter(0).getPercentage(AminoAcidEnum.ALA));
        Assert.assertEquals(0.0, lMatrix.getAminoAcidCounter(9).getPercentage(AminoAcidEnum.ALA));
    }

    public void testCreateRandomSampleAminoAcidMatrix() throws Exception {
        boolean loop = true;
        boolean random1 = false;
        boolean random2 = false;

        int lLoopCount = 0;
        while (loop) {
            iRawSequenceSet.reset();
            MatrixAminoAcidStatistics lMatrix =
                    AminoAcidStatisticsFactory.createRandomSampleAminoAcidMatrix(iRawSequenceSet, 5, 30, SamplingStrategy.ALL.getInstance());

            if (lMatrix.getAminoAcidCounter(0).getPercentage(AminoAcidEnum.ALA) == 0.4) {
                random1 = true;
            }

            if (lMatrix.getAminoAcidCounter(0).getPercentage(AminoAcidEnum.ALA) == 0.8) {
                random2 = true;
            }

            if (random1 && random2) {
                loop = false;
            }

            if (++lLoopCount > 20) {
                fail("Randomisation is failing.." + lLoopCount);
            }
        }
    }

    public void testCreateVerticalPositionAminoAcidMatrix() {
        iRawSequenceSet.reset();
        // '20' fold iteration: Sample '2' proteins from the '5' raw sequences and determine count the AminoAcid occurence at position '0'.
        // Ala percentage should be 1 or 0.5 for each iteration!
        AminoAcidStatistics lMatrix =
                AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(iRawSequenceSet, 20, 0, 1, 2, SamplingDirectionEnum.NtermToCterm)[0];
        int lNumberOfAminoAcidCounters = lMatrix.getDimension();
        for (int i = 0; i < lNumberOfAminoAcidCounters; i++) {
            AminoAcidCounter lAminoAcidCounter = ((MatrixAminoAcidStatistics) lMatrix).getAminoAcidCounter(i);
            if (lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA) != 0.5 && lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA) != 1) {
                fail(new StringBuilder().append("'").append(lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA)).append("' should be impossible!!").toString());
            }
        }
    }

    public void testCreateVerticalPositionAminoAcidMatrix2() {
        iRawSequenceSet.reset();

        // '20' fold iteration: Sample '2' proteins from the '5' raw sequences and determine count the AminoAcid occurence at position '0'.
        // Ala percentage should be 1 or 0.5 for each iteration at position '0' or '2'!

        AminoAcidStatistics[] lMatrix =
                AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(iRawSequenceSet, 20, 0, 3, 2, SamplingDirectionEnum.NtermToCterm);

        // Ala percentage should be 1 or 0.5 for each iteration at position '0'!
        int lNumberOfAminoAcidCounters = lMatrix[0].getDimension();
        for (int i = 0; i < lNumberOfAminoAcidCounters; i++) {
            AminoAcidCounter lAminoAcidCounter = ((MatrixAminoAcidStatistics) lMatrix[0]).getAminoAcidCounter(i);
            if (lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA) != 0.5 && lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA) != 1) {
                fail(new StringBuilder().append("'").append(lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA)).append("' should be impossible!!").toString());
            }
        }

        Assert.assertEquals(3, lMatrix.length);
        Assert.assertEquals(20, lNumberOfAminoAcidCounters);

        // Ala percentage should be 1 or 0.5 for each iteration at position '2'!
        lNumberOfAminoAcidCounters = lMatrix[2].getDimension();
        for (int i = 0; i < lNumberOfAminoAcidCounters; i++) {
            AminoAcidCounter lAminoAcidCounter = ((MatrixAminoAcidStatistics) lMatrix[0]).getAminoAcidCounter(i);
            if (lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA) != 0.5 && lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA) != 1) {
                fail(new StringBuilder().append("'").append(lAminoAcidCounter.getPercentage(AminoAcidEnum.ALA)).append("' should be impossible!!").toString());
            }
        }
    }
}