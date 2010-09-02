package com.compomics.icelogo.core;
/**
 * Created by IntelliJ IDEA.
 *
 * User: Kenny
 * Date: 18-sep-2008
 * Time: 10:30:47
 *
 *
 * The 'TestOneToOneMatrixDataModel ' class was created for
 */

import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.factory.AminoAcidFactory;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import junit.TestCaseLM;
import junit.framework.Assert;
import org.jfree.data.statistics.StatisticalCategoryDataset;

public class TestPositionalOneSampleMatrixDataModel extends TestCaseLM {
    OneSampleMatrixDataModel iOneToOneMatrixDataModel;

    public TestPositionalOneSampleMatrixDataModel() {
        super("Testscenario for class OneSampleMatrixDataModel'.");

        TestAminoAcidStatistics lTestAminoAcidMatrix = new TestAminoAcidStatistics();
        MatrixAminoAcidStatistics lAminoAcidStatistics = lTestAminoAcidMatrix.getMatrixAminoAcidStatistics();
        MatrixAminoAcidStatistics[] lAminoAcidStatisticsAsArray =
                lTestAminoAcidMatrix.getMatrixAminoAcidStatisticsAsArray();


        String lReferenceID = "Test reference set";

        iOneToOneMatrixDataModel =
                new OneSampleMatrixDataModel(new MatrixAminoAcidStatistics[]{lAminoAcidStatistics, lAminoAcidStatistics}, lAminoAcidStatisticsAsArray, lReferenceID);
    }

    public void testGetReferenceAndPositionStatisticalCategoryDataset() {
        StatisticalCategoryDataset lStatisticalCategoryDataset =
                iOneToOneMatrixDataModel.getReferenceAndPositionStatisticalCategoryDataset(0);

        // Values from the positional set, position 1.
        Assert.assertEquals(lStatisticalCategoryDataset.getMeanValue("Position", 'M'), 0.8);
        Assert.assertEquals(lStatisticalCategoryDataset.getMeanValue("Position", 'A'), 0.2);

        // Values from the refrence set, position 1.
        Assert.assertEquals(lStatisticalCategoryDataset.getMeanValue("TEST", 'G'), 0.039999999999999994);
        Assert.assertEquals(lStatisticalCategoryDataset.getStdDevValue("TEST", 'G'), 0.08432740427115679);

        lStatisticalCategoryDataset = iOneToOneMatrixDataModel.getReferenceAndPositionStatisticalCategoryDataset(1);

        // Values from the positional set, position 1.
        Assert.assertEquals(lStatisticalCategoryDataset.getMeanValue("Position", 'M'), 0.0);
        Assert.assertEquals(lStatisticalCategoryDataset.getMeanValue("Position", 'A'), 0.8);

        // Values from the refrence set, position 1.
        Assert.assertEquals(lStatisticalCategoryDataset.getMeanValue("TEST", 'G'), 0.039999999999999994);
        Assert.assertEquals(lStatisticalCategoryDataset.getStdDevValue("TEST", 'G'), 0.08432740427115679);

    }


    public void testGetReferenceValue() {
        double lReferenceGly_0 = iOneToOneMatrixDataModel.getReferenceValue(AminoAcidFactory.getAminoAcid('G'), 0);
        double lReferenceGly_1 = iOneToOneMatrixDataModel.getReferenceValue(AminoAcidFactory.getAminoAcid('G'), 1);

        Assert.assertEquals(0.039999999999999994, lReferenceGly_0);
        Assert.assertEquals(0.039999999999999994, lReferenceGly_1);

    }

    public void testGetReferenceSD() {
        double lReferenceGly_0 = iOneToOneMatrixDataModel.getReferenceSD(AminoAcidFactory.getAminoAcid('G'), 0);
        double lReferenceGly_1 = iOneToOneMatrixDataModel.getReferenceSD(AminoAcidFactory.getAminoAcid('G'), 1);

        Assert.assertEquals(0.08432740427115679, lReferenceGly_0);
        Assert.assertEquals(0.08432740427115679, lReferenceGly_1);
    }
}