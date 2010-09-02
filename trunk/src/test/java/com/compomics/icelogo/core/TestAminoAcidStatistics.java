package com.compomics.icelogo.core;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 25-aug-2008
 * Time: 11:30:40
 * To change this template use File | Settings | File Templates.
 */

import com.compomics.icelogo.core.data.AminoAcidCounter;
import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.factory.AminoAcidFactory;
import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.compomics.util.protein.Protein;
import junit.TestCaseLM;
import junit.framework.Assert;
import org.apache.commons.math.random.RandomDataImpl;

import java.io.IOException;
import java.util.Vector;

public class TestAminoAcidStatistics extends TestCaseLM {
    private Vector<AminoAcidCounter> iAminoAcidCounters;

    // Static initialization.


    public TestAminoAcidStatistics() {
        super("Test Case for AminoAcidStatistics");

        int lMatrixColumnSize = 10;
        // The startsite for indexing proteins. (1-based)
        RandomDataImpl random = new RandomDataImpl();

        iAminoAcidCounters = new Vector<AminoAcidCounter>(lMatrixColumnSize);

        for (int i = 0; i < lMatrixColumnSize; i++) {
            iAminoAcidCounters.add(new AminoAcidCounter());
        }

        DBLoader loader = new FASTADBLoader();
        try {
            loader.load(getFullFilePath("fasta_db_1.fasta"));

            Protein lProtein = null;
            long lNumberOfEntries = loader.countNumberOfEntries();
            int lAminoAcidIndex;

            for (int i = 0; i < lNumberOfEntries; i++) {
                lProtein = loader.nextProtein();

                lAminoAcidIndex = -1;
                for (AminoAcidCounter lAminoAcidCounter : iAminoAcidCounters) {
                    lAminoAcidIndex++;
                    char lAminoAcidChar = lProtein.getSequence().getSequence().charAt(lAminoAcidIndex);
                    lAminoAcidCounter.count(lAminoAcidChar);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public MatrixAminoAcidStatistics[] getMatrixAminoAcidStatisticsAsArray() {
        return getMatrixAminoAcidStatisticsAsArray("TEST");
    }

    public MatrixAminoAcidStatistics[] getMatrixAminoAcidStatisticsAsArray(String aName) {
        MatrixAminoAcidStatistics[] lAminoAcidStatisticsAsArray =
                new MatrixAminoAcidStatistics[iAminoAcidCounters.size()];
        for (int i = 0; i < iAminoAcidCounters.size(); i++) {
            Vector<AminoAcidCounter> v = new Vector<AminoAcidCounter>();
            v.add(iAminoAcidCounters.get(i));
            lAminoAcidStatisticsAsArray[i] = new MatrixAminoAcidStatistics(v, aName + i);
        }
        return lAminoAcidStatisticsAsArray;
    }


    public MatrixAminoAcidStatistics getMatrixAminoAcidStatistics() {
        return new MatrixAminoAcidStatistics(iAminoAcidCounters, "TEST");
    }

    public void testAminoAcidMatrix() throws Exception {
        MatrixAminoAcidStatistics lAminoAcidStatistics = getMatrixAminoAcidStatistics();
        Assert.assertEquals(50.0, lAminoAcidStatistics.getTotalMatrixCount());
        Assert.assertEquals(10.0, lAminoAcidStatistics.getAminoAcidCount(AminoAcidFactory.getAminoAcid('A')));
    }

    public void testGetStatistics() {
        MatrixAminoAcidStatistics lAminoAcidStatistics = getMatrixAminoAcidStatistics();
        Assert.assertEquals(0.7400000000000002, lAminoAcidStatistics.getStatistics(AminoAcidFactory.getAminoAcid('A')).getPercentile(90));
        Assert.assertEquals(0.2, lAminoAcidStatistics.getStatistics(AminoAcidFactory.getAminoAcid('A')).getPercentile(50));
        Assert.assertEquals(0.23094010767585035, lAminoAcidStatistics.getStatistics(AminoAcidFactory.getAminoAcid('A')).getStandardDeviation());
        Assert.assertEquals(0.2, lAminoAcidStatistics.getStatistics(AminoAcidFactory.getAminoAcid('A')).getMean());
    }
}