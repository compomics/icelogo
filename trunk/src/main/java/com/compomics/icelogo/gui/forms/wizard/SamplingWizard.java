package com.compomics.icelogo.gui.forms.wizard;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.enumeration.ExperimentTypeEnum;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.enumeration.SamplingTypeEnum;
import com.compomics.icelogo.core.interfaces.ISequenceSet;
import com.compomics.icelogo.gui.component.Messenger;
import com.compomics.icelogo.gui.forms.IceLogo;
import com.compomics.icelogo.gui.interfaces.GraphableAcceptor;
import com.compomics.icelogo.gui.interfaces.WizardStep;
import com.compomics.icelogo.gui.runner.SamplingRunner;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 17, 2009
 * Time: 2:01:45 PM
 * <p/>
 * This class
 */
public class SamplingWizard extends Observable {
// ------------------------------ FIELDS ------------------------------

    private static SamplingWizard instance = null;

    private JCheckBox chkExpert;

    private JPanel jpanContent;
    private JButton btnBack;
    private JButton btnForward;
    private JPanel jpanWizardStep;
    private JPanel jpanButton;
    private JLabel lblTitle;
    private JPanel jpanTitle;

    private GraphableAcceptor iAcceptor;


    private int iWizardIndex;
    private WizardStep[] iWizardSteps;
    private WizardStep iActiveWizardStep;

    /**
     * This boolean reflects the expert option mode in this panel.
     */
    private boolean iExpert = false;

    // Wizzard panels.
    private ReferenceStep iReferenceStep;
    private ExperimentStep iExperimentStep;
    private OverViewStep iOverViewStep;
    private ActionListener iForwardAction;
    private ActionListener iBackAction;
    private ActionListener iExpertAction;

// -------------------------- STATIC METHODS --------------------------

    /**
     * Return the singleton instance of the sampling wizard.
     *
     * @param aIceLogo
     * @return
     */
    public static JPanel getInstance(final IceLogo aIceLogo) {

        if (instance == null) {
            instance = new SamplingWizard(aIceLogo);
        }
        //Before we add this observer we will delete all the others
        MainInformationFeeder.getInstance().deleteObservers();

        return instance.getContent();
    }

    /**
     * Returns the content panel of the wizzard.
     *
     * @return
     */
    public JPanel getContent() {
        return jpanContent;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public SamplingWizard(final GraphableAcceptor aAcceptor) {
        iAcceptor = aAcceptor;
        // Create UI.
        $$$setupUI$$$();

        // Define the steps in the wizard.
        iReferenceStep = new ReferenceStep(this);
        iExperimentStep = new ExperimentStep(this);
        iOverViewStep = new OverViewStep(this);

        iWizardSteps = new WizardStep[]{iReferenceStep, iExperimentStep, iOverViewStep};

        setActionListeners();
        // Invoke the first press forward.
        btnForward.doClick();
    }

    /**
     * Private method to customize gui components.
     */
    private void createUIComponents() {
        ImageIcon lIcon;
        URL lURL;

        lURL = ClassLoader.getSystemResource("icons/back.png");
        lIcon = new ImageIcon(lURL);
        btnBack = new JButton(lIcon);
        btnBack.setToolTipText("Go one step back");


        lURL = ClassLoader.getSystemResource("icons/forward.png");
        lIcon = new ImageIcon(lURL);
        btnForward = new JButton(lIcon);
        btnForward.setToolTipText("Go one step forward");
    }

    /**
     * Set the action listeners for this form.
     */
    private void setActionListeners() {

        //action listener for a resize event
        jpanContent.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                MainInformationFeeder.getInstance().setGraphableHeight(jpanContent.getHeight());
                MainInformationFeeder.getInstance().setGraphableWidth(jpanContent.getWidth());
            }
        });
        //action listener for the next button
        iForwardAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //perform the next method on the current wizardpanel
                if (iActiveWizardStep == null) {
                    iWizardIndex = 0;
                    iActiveWizardStep = iWizardSteps[iWizardIndex];
                    iActiveWizardStep.construct();
                    //visualize the panel
                    jpanWizardStep.removeAll();
                    jpanWizardStep.setLayout(new BoxLayout(jpanWizardStep, BoxLayout.X_AXIS));
                    jpanWizardStep.add(iActiveWizardStep.getContentPane());
                    //
                    btnForward.setIcon(new ImageIcon(getClass().getResource("/icons/forward.png")));
                    btnForward.setText("");
                    btnForward.setToolTipText("Next");
                } else {
                    iActiveWizardStep.nextClicked();
                    //check if it's ok to proceed
                    if (iActiveWizardStep.isFeasableToProceed() && iWizardIndex != iWizardSteps.length - 1) {
                        //set the next index
                        iWizardIndex = iWizardIndex + 1;
                        //get the next panel
                        iActiveWizardStep = iWizardSteps[iWizardIndex];
                        iActiveWizardStep.construct();
                        if (iActiveWizardStep == iOverViewStep) {
                            // The reference sequence updating must be started!!
                            iOverViewStep.startUpdatingReference();
                        }
                        //visualize the panel
                        jpanWizardStep.removeAll();
                        jpanWizardStep.setLayout(new BoxLayout(jpanWizardStep, BoxLayout.X_AXIS));
                        jpanWizardStep.add(iActiveWizardStep.getContentPane());
                    } else {
                        if (iWizardIndex != iWizardSteps.length - 1) {
                            //it's not ok to proceed
                            Messenger.getInstance().sendMessage(iActiveWizardStep.getNotFeasableReason());
                        } else {
                            // The Start button was pressed and all was evaluated fine!
                            // Lets start the processing.
                            setToBeginState();
                            wizardFinished();
                        }
                    }
                }
                setTitle();
                jpanWizardStep.updateUI();
                setForwardIcon();
                setBackwardIcon();
            }


        };


        //action listener for the previous button
        iBackAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setBackwardIcon();
                setForwardIcon();
                if (iActiveWizardStep == null) {
                    iWizardIndex = 0;
                    iActiveWizardStep = iWizardSteps[iWizardIndex];
                    iActiveWizardStep.construct();
                    //visualize the panel
                    jpanWizardStep.removeAll();
                    jpanWizardStep.setLayout(new BoxLayout(jpanWizardStep, BoxLayout.X_AXIS));
                    jpanWizardStep.add(iActiveWizardStep.getContentPane());
                } else {
                    //perform the previous method on the current wizardpanel
                    iActiveWizardStep.backClicked();
                    //check if it's ok to proceed
                    if (iActiveWizardStep.isFeasableToProceed() && iWizardIndex != 0) {
                        //set the previous index
                        iWizardIndex = iWizardIndex - 1;
                        //get the previous panel
                        iActiveWizardStep = iWizardSteps[iWizardIndex];
                        iActiveWizardStep.construct();
                        //visualize the panel
                        jpanWizardStep.removeAll();
                        jpanWizardStep.setLayout(new BoxLayout(jpanWizardStep, BoxLayout.X_AXIS));
                        jpanWizardStep.add(iActiveWizardStep.getContentPane());
                    } else {
                        //it's not ok to proceed
                        if (iWizardIndex != 0) {
                            Messenger.getInstance().sendMessage(new Throwable(iActiveWizardStep.getNotFeasableReason()));
                        }
                    }
                    if (iWizardIndex == iWizardSteps.length - 1) {
                        btnForward.setIcon(new ImageIcon(getClass().getResource("/icons/finish.png")));
                        btnForward.setText("Start");
                        btnForward.setToolTipText("Start");
                    } else {
                        btnForward.setIcon(new ImageIcon(getClass().getResource("/icons/forward.png")));
                        btnForward.setText("");
                        btnForward.setToolTipText("Next");
                    }
                }
                setTitle();
                setForwardIcon();
                setBackwardIcon();
                jpanWizardStep.updateUI();
            }
        };


        /**
         * Set the boolExpert value to the gui input.
         */
        iExpertAction = new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            public void actionPerformed(final ActionEvent e) {
                setExpert(chkExpert.isSelected());
                notifyObservers(ObservableEnum.NOTIFY_EXPERT_MODE);
            }
        };

        jpanContent.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
                lFeeder.setGraphableHeight(jpanContent.getHeight());
                lFeeder.setGraphableWidth(jpanContent.getWidth());
            }
        });


        btnForward.addActionListener(iForwardAction);
        btnBack.addActionListener(iBackAction);
        chkExpert.addActionListener(iExpertAction);
    }

    /**
     * Sets the icon for the forward button.
     */
    private void setForwardIcon() {
        if (iWizardIndex == iWizardSteps.length - 1) {
            btnForward.setIcon(new ImageIcon(getClass().getResource("/icons/finish.png")));
            btnForward.setText("Start");
            btnForward.setToolTipText("Start");
        } else {
            btnForward.setIcon(new ImageIcon(getClass().getResource("/icons/forward.png")));
            btnForward.setText("");
            btnForward.setToolTipText("Next");
        }
    }

    /**
     * Sets the icon for the backward button.
     */
    private void setBackwardIcon() {
        btnBack.setIcon(new ImageIcon(getClass().getResource("/icons/back.png")));
        if (iWizardIndex == 0) {
            btnBack.setEnabled(false);
        } else {
            btnBack.setEnabled(true);
        }
    }

    /**
     * Set the Wizard to the start panel.
     */
    private void setToBeginState() {
        iActiveWizardStep = null;
        btnForward.doClick();
    }

    /**
     * This method is called when the wizard is finished.
     * It should start the sampling task in a separate thread.
     */
    private void wizardFinished() {
        SamplingTypeEnum lSamplingType = this.getSamplingType();

        SamplingRunner runner = new SamplingRunner(lSamplingType, this, iAcceptor);
        Messenger.getInstance().setProgressIndeterminate(true);

        runner.start();
    }


    /**
     * Set the title of the wizzard panel.
     */
    public void setTitle() {
        String aTitle = "Sampling Step " + (iWizardIndex + 1) + "/" + iWizardSteps.length + " " + iActiveWizardStep.getTitle();
        lblTitle.setText(aTitle);
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to indicate
     * that this object has no longer changed.
     * <p/>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>arg</code> argument.
     *
     * @param arg any object.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#hasChanged()
     * @see java.util.Observer#update(java.util.Observable, Object)
     */
    @Override
    public void notifyObservers(final Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Return the Sampling type.
     * Null if the sampling type was not set.
     *
     * @return
     */
    public SamplingTypeEnum getSamplingType() {
        return MainInformationFeeder.getInstance().getSamplingType();
    }

    /**
     * Set the sampling Type to be used.
     *
     * @param aSamplingType
     */
    public void setSamplingType(final SamplingTypeEnum aSamplingType) {
        MainInformationFeeder.getInstance().setSamplingType(aSamplingType);
    }

    /**
     * Returns the expert mode status.
     *
     * @return
     */
    public boolean isExpert() {
        return iExpert;
    }

    /**
     * Sets the expert mode status.
     *
     * @param aExpert
     */
    public void setExpert(final boolean aExpert) {
        iExpert = aExpert;
    }

// -------------------------- OTHER METHODS --------------------------

    public boolean hasTwoExperiments() {
        return MainInformationFeeder.getInstance().isTwoExperiment();
    }


    public void setTwoExperiments(final boolean isTwoExperiment) {
        MainInformationFeeder.getInstance().setTwoExperiment(isTwoExperiment);
    }


    /**
     * Returns the JTextPane from the PositionStep panel.
     *
     * @param aType
     * @return
     */
    public Document getPositionDocument(ExperimentTypeEnum aType) {
        return iExperimentStep.getPositionDocument(aType);
    }

    /**
     * This method calculates and returns the sample size
     * based on the position input from the PositionStep.
     *
     * @param aAveraged - Whether or not the average of two samples should be taken when two positions are given.
     */
    public int getPositionSampleSize(boolean aAveraged) {
        // Local var for the sampling size.
        int lResult = -1;
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        // Always get the SequenceSet from the first positins.
        ISequenceSet lPositionSequenceSet = lFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        int pos = lPositionSequenceSet.getNumberOfSequences();

        if (hasTwoExperiments()) {
            ISequenceSet lPositionSequenceSetTwo = lFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
            int pos2 = lPositionSequenceSetTwo.getNumberOfSequences();
            // Multiple sets.
            if (aAveraged) {
                // Average of two samples.
                // pick average between 1 and 2.
                lResult = Math.round((pos + pos2) / 2);
            } else {
                // If pos less then pos2, then take pos else take pos 2 :)
                // DEFAULT = LESSER VALUE.
                lResult = pos < pos2 ? pos : pos2;
            }
        } else {
            lResult = pos;
        }

        return lResult;
    }

    public void setForwordButtonEnabled(boolean aEnabledStatus) {
        btnForward.setEnabled(aEnabledStatus);
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        jpanButton = new JPanel();
        jpanButton.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        jpanContent.add(jpanButton, gbc);
        btnBack.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        jpanButton.add(btnBack, gbc);
        btnForward.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        jpanButton.add(btnForward, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        jpanButton.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanButton.add(spacer2, gbc);
        chkExpert = new JCheckBox();
        chkExpert.setFont(new Font(chkExpert.getFont().getName(), Font.PLAIN, 9));
        chkExpert.setForeground(new Color(-10066330));
        chkExpert.setText("Expert options");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        jpanButton.add(chkExpert, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        jpanButton.add(spacer3, gbc);
        jpanWizardStep = new JPanel();
        jpanWizardStep.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        jpanContent.add(jpanWizardStep, gbc);
        jpanTitle = new JPanel();
        jpanTitle.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanTitle, gbc);
        lblTitle = new JLabel();
        lblTitle.setFont(new Font(lblTitle.getFont().getName(), Font.BOLD, 14));
        lblTitle.setForeground(new Color(-10066330));
        lblTitle.setText("Title");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 15, 0, 0);
        jpanTitle.add(lblTitle, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanTitle.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanTitle.add(spacer5, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
