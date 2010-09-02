package com.compomics.icelogo.core.stat;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 8-okt-2008 Time: 15:01:44 The 'DescriptiveStatisticsImpl ' class was
 * created for
 */
public class DescriptiveStatisticsImpl extends DescriptiveStatistics {
    private int iSequenceSetSize = -1;

    public DescriptiveStatisticsImpl() {
        super();
    }

    public int getSequenceSetSize() {
        return iSequenceSetSize;
    }

    public void setSequenceSetSize(final int aSequenceSetSize) {
        iSequenceSetSize = aSequenceSetSize;
    }

    public long getN() {
        return iSequenceSetSize;
    }
}

