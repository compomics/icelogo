package com.compomics.icelogo.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 13, 2009
 * Time: 6:26:12 PM
 * <p/>
 * This class
 */
public class TestNormalDistribution extends TestCase {

    public void testNormalDistribution() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(0);
        dist.setStandardDeviation(1);
        try {
            Assert.assertEquals(0.050502583474103635, dist.cumulativeProbability(-1.64));
            Assert.assertEquals(0.024997895148220428, dist.cumulativeProbability(-1.96));
            Assert.assertEquals(0.9494974165258964, dist.cumulativeProbability(1.64));
            Assert.assertEquals(0.9750021048517796, dist.cumulativeProbability(1.96));
            Assert.assertEquals(1.6448536283610324, dist.inverseCumulativeProbability(0.95), 0.0000001);
            //Assert.assertEquals(-1.6448536283610324, dist.inverseCumulativeProbability(0.05));
            //Assert.assertEquals(-1.6448536283610324, dist.inverseCumulativeProbability(0.05));
            Assert.assertEquals(0.9500042097035593, dist.cumulativeProbability(-1.96, 1.96));
        } catch (MathException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
