package com.compomics.icelogo.core.interfaces;

/**
 * This interface defines the behaviour of sampling a character from a given String.
 */
public interface ISamplingStrategy {
// -------------------------- OTHER METHODS --------------------------

    /**
     * Returns a 'char' from the given String aSequence
     *
     * @param aSequence String to be sampled
     * @return char from the given String
     */
    public char sample(String aSequence);
}
