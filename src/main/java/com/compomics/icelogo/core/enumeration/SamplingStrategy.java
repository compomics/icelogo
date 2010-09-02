package com.compomics.icelogo.core.enumeration;

import com.compomics.icelogo.core.interfaces.ISamplingStrategy;
import com.compomics.icelogo.core.strategy.PrefixSamplingStrategy;
import com.compomics.icelogo.core.strategy.UnrestrictedSamplingStrategy;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 19, 2009
 * Time: 2:38:15 PM
 * <p/>
 * This class
 */
public enum SamplingStrategy {
    ALL, PREFIX;

    @Override
    public String toString() {
        String lResult = "na";
        if (this == PREFIX) {
            lResult = "Random sample with prefix";
        } else if (this == ALL) {
            lResult = "Random sample";
        }
        return lResult;
    }

    /**
     * Returns an instance of this enumtype.
     *
     * @return
     */
    public ISamplingStrategy getInstance() {
        ISamplingStrategy lStrategy = null;
        if (this == PREFIX) {
            lStrategy = new PrefixSamplingStrategy();
        } else if (this == ALL) {
            lStrategy = new UnrestrictedSamplingStrategy();
        }
        return lStrategy;
    }
}
