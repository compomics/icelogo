package com.compomics.icelogo.core.strategy;

import com.compomics.icelogo.core.interfaces.ISamplingStrategy;
import org.apache.commons.math.random.RandomDataImpl;

/**
 * This class samples a character from a String preceeded by another specified character.
 */
public class PrefixSamplingStrategy implements ISamplingStrategy {
// ------------------------------ FIELDS ------------------------------

    /**
     * Instance RandomNumberGenerator.
     */
    private RandomDataImpl iRandomGenerator = new RandomDataImpl();

    /**
     * This char must preceed the actual sampled char.
     */
    private char iPrefix;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructs a PrefixSamplingStrategy. <br>This instance will sample a character from a String preceeded by another
     * specified character.
     */
    public PrefixSamplingStrategy() {
        iRandomGenerator = new RandomDataImpl();
    }

    /**
     * Set the prefix for the random sampling.
     *
     * @param aPrefix
     */
    public void setPrefix(final char aPrefix) {
        iPrefix = aPrefix;
    }

    // ------------------------ CANONICAL METHODS ------------------------

    public String toString() {
        return "Random prefix '" + iPrefix + "' sampler";
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ISamplingStrategy ---------------------

    /**
     * {@inheritDoc}
     */
    public char sample(final String aSequence) {
        boolean hasCorrectPrefix = true;

        int lIndex;
        char lResult = 0;

        while (hasCorrectPrefix) {
            lIndex = iRandomGenerator.nextSecureInt(1, aSequence.length() - 1);
            // As the prefix can only be found starting from postion '0',
            // only sample the actual character starting from position '1'.
            if (iPrefix == aSequence.charAt(lIndex - 1)) { // 0-based!
                hasCorrectPrefix = false;
                lResult = aSequence.charAt(lIndex);
            }
        }

        return lResult;
    }
}
