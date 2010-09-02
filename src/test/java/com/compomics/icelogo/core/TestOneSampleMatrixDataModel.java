package com.compomics.icelogo.core;
/**
 * Created by IntelliJ IDEA.
 *
 * User: Kenny
 * Date: 18-sep-2008
 * Time: 10:29:42
 *
 *
 * The 'TestMatrixDataModel ' class was created for
 */

import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.factory.AminoAcidFactory;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import junit.TestCaseLM;
import junit.framework.Assert;

public class TestOneSampleMatrixDataModel extends TestCaseLM {
    private OneSampleMatrixDataModel iMatrixDataModel;
    private OneSampleMatrixDataModel iModelA;
    private OneSampleMatrixDataModel iModelB;

    public TestOneSampleMatrixDataModel() {
        super("Testscenario for class OneSampleMatrixDataModel'.");

        TestAminoAcidStatistics lTestAminoAcidMatrix = new TestAminoAcidStatistics();
        MatrixAminoAcidStatistics lAminoAcidStatistics = lTestAminoAcidMatrix.getMatrixAminoAcidStatistics();
        MatrixAminoAcidStatistics[] lAminoAcidStatisticsAsArray =
                lTestAminoAcidMatrix.getMatrixAminoAcidStatisticsAsArray();


        String lReferenceID_A = "Static reference set A";
        String lReferenceID_B = "Static reference set B";

        iModelA =
                new OneSampleMatrixDataModel(new MatrixAminoAcidStatistics[]{lAminoAcidStatistics}, lAminoAcidStatisticsAsArray, lReferenceID_A);

        iModelB =
                new OneSampleMatrixDataModel(new MatrixAminoAcidStatistics[]{lAminoAcidStatistics, lAminoAcidStatistics}, lAminoAcidStatisticsAsArray, lReferenceID_B);


    }

    public void testGetNumberOfPositions() {
        Assert.assertEquals(10, iModelA.getNumberOfPositions());
        Assert.assertEquals(10, iModelB.getNumberOfPositions());
    }

    public void testGetPositionValue() {
        Assert.assertEquals(0.8, iModelA.getPositionValue(AminoAcidFactory.getAminoAcid('M'), 0));
        Assert.assertEquals(0.2, iModelA.getPositionValue(AminoAcidFactory.getAminoAcid('A'), 0));
        Assert.assertEquals(0.0, iModelA.getPositionValue(AminoAcidFactory.getAminoAcid('M'), 9));
        Assert.assertEquals(0.2, iModelA.getPositionValue(AminoAcidFactory.getAminoAcid('F'), 9));

        Assert.assertEquals(0.8, iModelB.getPositionValue(AminoAcidFactory.getAminoAcid('M'), 0));
        Assert.assertEquals(0.2, iModelB.getPositionValue(AminoAcidFactory.getAminoAcid('A'), 0));
        Assert.assertEquals(0.0, iModelB.getPositionValue(AminoAcidFactory.getAminoAcid('M'), 9));
        Assert.assertEquals(0.2, iModelB.getPositionValue(AminoAcidFactory.getAminoAcid('F'), 9));

    }
}