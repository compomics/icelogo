package com.compomics.icelogo.core;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 27-aug-2008
 * Time: 11:33:23
 * To change this template use File | Settings | File Templates.
 */

import com.compomics.icelogo.core.data.AminoAcidCounter;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestAminoAcidCounter extends TestCase {
    AminoAcidCounter iAminoAcidCounter;

    public void testCount() throws Exception {
        AminoAcidCounter lCounter = new AminoAcidCounter();
        lCounter.count('A');
        lCounter.count(AminoAcidEnum.ALA);
        lCounter.count(AminoAcidEnum.ALA);
        lCounter.count(AminoAcidEnum.ALA);
        lCounter.count(AminoAcidEnum.GLU);

        double lPercentage = lCounter.getPercentage(AminoAcidEnum.ALA);
        Assert.assertEquals(0.8, lPercentage);
    }
}