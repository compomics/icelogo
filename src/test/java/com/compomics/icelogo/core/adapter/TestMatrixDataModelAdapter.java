package com.compomics.icelogo.core.adapter;
/**
 * Created by IntelliJ IDEA.
 *
 * User: Kenny
 * Date: 18-sep-2008
 * Time: 10:27:12
 *
 *
 * The 'TestMatrixDataModelAdapter ' class was created for
 */

import com.compomics.icelogo.core.TestAminoAcidStatistics;
import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import junit.TestCaseLM;
import junit.framework.Assert;

public class TestMatrixDataModelAdapter extends TestCaseLM {
    HeatmapAdapter iHeatmapAdapter;

    public TestMatrixDataModelAdapter() {
        super("Testscenario for class HeatmapAdapter'.");

        TestAminoAcidStatistics lTestAminoAcidMatrix = new TestAminoAcidStatistics();


        MatrixAminoAcidStatistics lAminoAcidStatistics = lTestAminoAcidMatrix.getMatrixAminoAcidStatistics();
        MatrixAminoAcidStatistics[] lAminoAcidStatisticsAsArray =
                lTestAminoAcidMatrix.getMatrixAminoAcidStatisticsAsArray();

        String lReferenceID = "Test reference set";
        OneSampleMatrixDataModel iOneToOneMatrixDataModel =
                new OneSampleMatrixDataModel(new AminoAcidStatistics[]{lAminoAcidStatistics}, lAminoAcidStatisticsAsArray, lReferenceID);

        iHeatmapAdapter = new OneSampleHeatmapAdapter(iOneToOneMatrixDataModel);
    }

    public void testGetMaxValue() {
        Object o = iHeatmapAdapter.getMaxValue();
        Assert.assertEquals(0.9977867370710403, o);
    }

    public void testGetMinValue() {
        Object o = iHeatmapAdapter.getMinValue();
        Assert.assertEquals(0.1226390584033864, o);
    }

    public void testGetColumnCount() {
        Object o = iHeatmapAdapter.getColumnCount();
        Assert.assertEquals(10, o);
    }

    public void testGetRowCount() {
        Object o = iHeatmapAdapter.getRowCount();
        // current number of entries in the AminoAcid enumertion.
        Assert.assertEquals(21, o);
    }

    public void testGetValueAt() {
        Object o = null;

        // For debugging purposes..
        AminoAcidEnum[] lAminoAcidEnums = AminoAcidEnum.OTHER.valuesByPhysicoChemicalProperties();

        // Phe (row 15) at pos '1'
        o = iHeatmapAdapter.getValueAt(15, 0);
        Assert.assertEquals(0.1226390584033864, o);

        // Phe (row 15) at pos '2'
        o = iHeatmapAdapter.getValueAt(15, 1);
        Assert.assertEquals(0.7807109869595001, o);

        // Met (row 6) at pos '1'
        o = iHeatmapAdapter.getValueAt(6, 0);
        Assert.assertEquals(0.9977867370710402, o);

        // Ala (row 1) at pos '2'
        o = iHeatmapAdapter.getValueAt(1, 1);
        Assert.assertEquals(0.9953126157702827, o);
    }
}