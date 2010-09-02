package com.compomics.icelogo.core.data.sequenceset;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 27-aug-2008
 * Time: 13:05:52
 * To change this template use File | Settings | File Templates.
 */

import junit.TestCaseLM;
import junit.framework.Assert;

public class TestRawSequenceSet extends TestCaseLM {
    RawSequenceSet iRawSequenceSet;

    public TestRawSequenceSet() {
        super("Testscenario for class 'RawSequenceSet'.");
        iRawSequenceSet = new RawSequenceSet("Test RawSequenceSet");
        iRawSequenceSet.add("KENNY");
        iRawSequenceSet.add("HELSENS");
        iRawSequenceSet.add("GHENT");
    }

    public void testNextSequence() {
        Assert.assertEquals("KENNY", iRawSequenceSet.nextSequence());
        Assert.assertEquals("HELSENS", iRawSequenceSet.nextSequence());
        Assert.assertEquals(3, iRawSequenceSet.getNumberOfSequences());
        iRawSequenceSet.reset();
        Assert.assertEquals("KENNY", iRawSequenceSet.nextSequence());
    }
}