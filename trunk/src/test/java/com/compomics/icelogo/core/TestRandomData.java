package com.compomics.icelogo.core;

import junit.TestCaseLM;
import junit.framework.Assert;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 27-aug-2008 Time: 12:01:24 To change this template use File | Settings |
 * File Templates.
 */
public class TestRandomData extends TestCaseLM {
    public TestRandomData() {
        super("TestCase for random number generation.");
    }

    public void testRandomInt() {
        RandomDataImpl lRandom = new RandomDataImpl();
        DescriptiveStatistics lStatistics = new DescriptiveStatistics();
        for (int i = 0; i < 30; i++) {
            lStatistics.addValue(lRandom.nextInt(1, 3));
        }
        Assert.assertEquals(1.0, lStatistics.getMin());
        Assert.assertEquals(3.0, lStatistics.getMax());
    }
}
