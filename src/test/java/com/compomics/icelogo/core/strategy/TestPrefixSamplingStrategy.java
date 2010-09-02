package com.compomics.icelogo.core.strategy;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 27-aug-2008
 * Time: 11:07:19
 * To change this template use File | Settings | File Templates.
 */

import junit.TestCaseLM;
import junit.framework.Assert;

public class TestPrefixSamplingStrategy extends TestCaseLM {
    PrefixSamplingStrategy iPrefixSamplingStrategy;

    public TestPrefixSamplingStrategy() {
        super("TestCase for PrefixSamplingStrategy");
        iPrefixSamplingStrategy = new PrefixSamplingStrategy();
        iPrefixSamplingStrategy.setPrefix('M');
    }

    public void testSample() throws Exception {
        String s = "AAAAMDAAAA";
        char c = iPrefixSamplingStrategy.sample(s);
        Assert.assertEquals(c, 'D');

        s = "MDAAAA";
        c = iPrefixSamplingStrategy.sample(s);
        Assert.assertEquals(c, 'D');

        s = "AAAAMD";
        c = iPrefixSamplingStrategy.sample(s);
        Assert.assertEquals(c, 'D');
    }
}