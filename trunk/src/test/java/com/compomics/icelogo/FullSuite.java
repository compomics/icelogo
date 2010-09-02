package com.compomics.icelogo;

import com.compomics.icelogo.core.*;
import com.compomics.icelogo.core.adapter.TestMatrixDataModelAdapter;
import com.compomics.icelogo.core.data.sequenceset.*;
import com.compomics.icelogo.core.strategy.TestPrefixSamplingStrategy;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 28-aug-2007
 * Time: 14:08:45
 */

/**
 * Class description: ------------------ This class was developed to group all tests for peptizer.
 */
public class FullSuite extends TestCase {
    public FullSuite() {
        this("Suite of tests for frisco project.");
    }

    public FullSuite(String aName) {
        super(aName);


    }

    public static Test suite() {
        // Create a new TestSuite
        TestSuite ts = new TestSuite();

        // Add the TestCases
        ts.addTest(new TestSuite(TestAminoAcidCounter.class));
        ts.addTest(new TestSuite(TestAminoAcidStatistics.class));
        ts.addTest(new TestSuite(TestAminoAcidStatisticsFactory.class));

        ts.addTest(new TestSuite(TestFastaSequenceSet.class));

        ts.addTest(new TestSuite(TestPositionalOneSampleMatrixDataModel.class));

        ts.addTest(new TestSuite(TestOneSampleMatrixDataModel.class));
        ts.addTest(new TestSuite(TestMatrixDataModelAdapter.class));

        ts.addTest(new TestSuite(TestPartialSequenceSet.class));
        ts.addTest(new TestSuite(TestRegionalSequenceSet.class));
        ts.addTest(new TestSuite(TestPrefixSamplingStrategy.class));
        ts.addTest(new TestSuite(TestPositionFastaSequenceSet.class));

        ts.addTest(new TestSuite(TestRandomData.class));
        ts.addTest(new TestSuite(TestRawSequenceSet.class));
        ts.addTest(new TestSuite(TestNormalDistribution.class));


        return ts;
    }
}