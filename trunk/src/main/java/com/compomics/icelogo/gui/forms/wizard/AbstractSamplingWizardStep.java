package com.compomics.icelogo.gui.forms.wizard;

import com.compomics.icelogo.gui.interfaces.WizardStep;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 19, 2009
 * Time: 3:33:06 PM
 * <p/>
 * This class
 */
public abstract class AbstractSamplingWizardStep implements WizardStep {
// ------------------------------ FIELDS ------------------------------

    /**
     * Boolean that indicates if we can go to the next panel
     */
    protected boolean iFeasableToProceed = true;
    /**
     * The reason why we cannot go to the next panel
     */
    protected String iNotFeasableReason = null;
    /**
     * The parent SamplingWizzard.
     */
    protected SamplingWizard iParent = null;

// --------------------------- CONSTRUCTORS ---------------------------

    public AbstractSamplingWizardStep(final SamplingWizard aParent) {
        iParent = aParent;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    ;

    public String getNotFeasableReason() {
        return iNotFeasableReason;
    }

    ;

    public void setNotFeasableReason(final String aNotFeasableReason) {
        iNotFeasableReason = aNotFeasableReason;
    }

    /**
     * Returns a message why the panel was not ready to proceed.
     *
     * @return
     */
    public boolean isFeasableToProceed() {
        return iFeasableToProceed;
    }

    public void setFeasableToProceed(final boolean aFeasableToProceed) {
        iFeasableToProceed = aFeasableToProceed;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface WizardStep ---------------------

    public abstract JPanel getContentPane();

    public abstract void backClicked();

    public abstract void nextClicked();

    public abstract void construct();

    public abstract String getTitle();
}
