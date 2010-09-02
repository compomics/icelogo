package com.compomics.icelogo.core.strategy;

import com.compomics.icelogo.core.interfaces.ISamplingStrategy;
import org.apache.commons.math.random.RandomDataImpl;

/**
 * This class randomnly samples a character from a String.
 */
public class UnrestrictedSamplingStrategy implements ISamplingStrategy {
// ------------------------------ FIELDS ------------------------------

    /**
     * Instance RandomNumberGenerator.
     */
    private RandomDataImpl iRandomGenerator = new RandomDataImpl();

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructs a UnrestrictedSamplingStrategy. <br>This instance will sample a character from a String at random.
     */
    public UnrestrictedSamplingStrategy() {
        iRandomGenerator = new RandomDataImpl();
    }

// ------------------------ CANONICAL METHODS ------------------------

    public String toString() {
        return "Random sampler";
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ISamplingStrategy ---------------------

    /**
     * {@inheritDoc}
     */
    public char sample(final String aSequence) {
        int lIndex = iRandomGenerator.nextSecureInt(0, aSequence.length() - 1); // 0-based.
        return aSequence.charAt(lIndex);
    }
}