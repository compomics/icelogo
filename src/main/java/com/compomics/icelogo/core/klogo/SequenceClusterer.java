package com.compomics.icelogo.core.klogo;

import com.compomics.icelogo.core.data.sequenceset.RawSequenceSet;
import com.compomics.icelogo.core.interfaces.ISequenceSet;

import java.util.Random;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Sep 27, 2009 Time: 2:10:44 PM
 * <p/>
 * This class
 */
public class SequenceClusterer {
    /**
     * The SequenceSet to be clustered.
     */
    private ISequenceSet iSequenceSet;

    /**
     * The number of clusters to return.
     */
    private int iClusterCount = 3;


    public SequenceClusterer(ISequenceSet aSequenceSet) {
        iSequenceSet = aSequenceSet;

    }

    /**
     * This method returns n clustered SequenceSets.
     *
     * @return
     */
    public ISequenceSet[] getSequenceClusters() {
        Random lRandom = new Random();
        RawSequenceSet[] lRawSequenceSets = new RawSequenceSet[iClusterCount];

        String lSequence = null;
        while ((lSequence = iSequenceSet.nextSequence()) != null) {
            RawSequenceSet lRawSequenceSet;
            double r = lRandom.nextDouble();
            if (r < 0.333) {
                lRawSequenceSet = lRawSequenceSets[0];
            } else if (r < 0.666) {
                lRawSequenceSet = lRawSequenceSets[1];
            } else {
                lRawSequenceSet = lRawSequenceSets[2];
            }

            if (lRawSequenceSet == null) {
                lRawSequenceSet = new RawSequenceSet("dummy" + r);
            }

            lRawSequenceSet.add(lSequence);
        }

        return lRawSequenceSets;
    }

    public int getClusterCount() {
        return iClusterCount;
    }

    public void setClusterCount(final int aClusterCount) {
        iClusterCount = aClusterCount;
    }
}
