package com.compomics.icelogo.gui.forms.wizard;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.sequenceset.FastaSequenceSet;
import com.compomics.icelogo.core.data.sequenceset.RegionalFastaSequenceSet;
import com.compomics.icelogo.core.enumeration.*;
import com.compomics.util.protein.Protein;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Jun 23, 2009 Time: 12:20:05 PM
 * <p/>
 * This class
 */
public class OverViewStep extends AbstractSamplingWizardStep implements Observer {
// ------------------------------ FIELDS ------------------------------

    /**
     * The timer for updating the reference panel.
     */
    private Timer iTimer = null;

    // GUI components.
    private JPanel jpanContent;

    private JCheckBox chkAverage;

    private JTextPane txtReference;
    private JTextPane txtPosition;
    private JTextPane txtPositionTwo;

    private JPanel jpanExperimentSequence;
    private JPanel jpanExperimentSequenceTwo;
    private JPanel jpanPosition;

    private JSpinner spinSampleSize;
    private JSpinner spinIterationSize;

    private JLabel lblSampleSize;
    private JLabel lblItartionSize;
    private JPanel jpanOptions;

    private Color[] iReferenceColors;
    /**
     * Upon splitting the sequence into a fasta entry, create a new line every 'lFastaLineLength' chars.
     */
    private int iFastaLineLength = 75;
    private FastaSequenceSet iFastaSequenceSet;
    private RegionalFastaSequenceSet iRegionalFastaSequenceSet;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Create a new panel to setup the position set.
     *
     * @param aParent
     */
    public OverViewStep(final SamplingWizard aParent) {
        super(aParent);

        // Observe for actions on the parent frame.
        iParent.addObserver(this);

        // Construct the panel.
        $$$setupUI$$$();
        construct();
    }

    /**
     * This method will construct things that are not done in the normal panel constructor
     */
    public void construct() {
        $$$setupUI$$$();

        // Add listeners to the components.
        setListeners();

        // Set the iteration Size.
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        spinIterationSize.setValue(lFeeder.getIterationSize());

        // Set the sampling size.
        updateSampleSize();

        // Copy the textpanes documents from the positionstep.
        txtPosition.setDocument(iParent.getPositionDocument(ExperimentTypeEnum.EXPERIMENT));
        txtPositionTwo.setDocument(iParent.getPositionDocument(ExperimentTypeEnum.EXPERIMENT_TWO));

        txtPosition.getHighlighter().removeAllHighlights();
        txtPositionTwo.getHighlighter().removeAllHighlights();

        if (iParent.getSamplingType() == SamplingTypeEnum.REGIONAL) {
            ExperimentStep.constructColorAnchor(txtPosition);
            ExperimentStep.constructColorAnchor(txtPositionTwo);
        }

        // Activate the second position panel or not.
        evaluateTwoPosition();

        // Create reference colors.
        createReferenceColors();

        // Sets the visibility of the options by the expert and position count.
        setOptionsVisible();

        iFastaSequenceSet = (FastaSequenceSet) lFeeder.getActiveReferenceSet();


        if (lFeeder.getSamplingType().equals(SamplingTypeEnum.REGIONAL)) {

            // fetch the anchored amino acids.
            int lAnchorIndex = lFeeder.getExperimentAnchorValue();
            AminoAcidEnum[] lAminoAcids = lFeeder.getExperimentalAminoAcids(lAnchorIndex);
            int lNumberOfExperimentalPositions = lFeeder.getNumberOfExperimentalPositions();
            int lPrefix = lAnchorIndex; // If anchor is set to 5 (0-based), then there are 5 preceding amino acids.
            int lSuffix = lNumberOfExperimentalPositions - 1 - lAnchorIndex; // If there are 12 positions, there are 6(=12-5-1) following amino acids.

            iRegionalFastaSequenceSet = iFastaSequenceSet.deriveRegionalSequenceSet(lAminoAcids, lPrefix, lSuffix);
        }

        /**
         * Create a timer for updating the reference panel.
         */
        iTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                updateReferenceText();
            }
        });

        jpanContent.updateUI();
    }

    private void evaluateTwoPosition() {
        TitledBorder lTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        lTitledBorder.setTitleColor(Color.DARK_GRAY);

        lTitledBorder.setTitle("Experiment One");
        jpanExperimentSequence.setBorder(lTitledBorder);

        if (!iParent.hasTwoExperiments()) {
            // Show gui for 1 position!
            jpanExperimentSequence.setBorder(BorderFactory.createEmptyBorder());
            jpanExperimentSequenceTwo.setBorder(BorderFactory.createEmptyBorder());
            jpanExperimentSequenceTwo.setVisible(false);
        } else {
            // Show gui for 2 positions!

            lTitledBorder.setTitle("Experiment Two");
            jpanExperimentSequenceTwo.setBorder(lTitledBorder);
            jpanExperimentSequenceTwo.setVisible(true);
        }


    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    /**
     * Finish the timer and perform main finalization.
     */
    protected void finalize() throws Throwable {
        iTimer.stop();
        super.finalize();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Observer ---------------------

    /**
     * This method is called whenever the observed object is changed. An application calls an <tt>Observable</tt>
     * object's <code>notifyObservers</code> method to have all the object's observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code> method.
     */
    public void update(final Observable o, final Object arg) {
        if (arg != null) {
            // If a expert mode notification comes in, trigger the actionSampling ActionListener.
            if (arg.equals(ObservableEnum.NOTIFY_EXPERT_MODE)) {
                setOptionsVisible();
            }
        }
    }

// --------------------- Interface WizardStep ---------------------

    /**
     * This method gives the  JPanel with all the content of this wizard panel
     *
     * @return JPanel
     */
    public JPanel getContentPane() {
        return jpanContent;
    }

    /**
     * This method will be performed when the back button is clicked
     */
    public void backClicked() {
        // Neutralize that actions that have been undertaken.
        iTimer.stop();
        setFeasableToProceed(true);
    }

    /**
     * This method will be performed when the next button is clicked
     */
    public void nextClicked() {
        iTimer.stop();
        // OK, this is it. Last checks before the sampling can start!!
        MainInformationFeeder.getInstance().setPositionSampleSize(iParent.getPositionSampleSize(chkAverage.isSelected()));
        setFeasableToProceed(true);
    }

    /**
     * Returns a title for the step.
     */
    public String getTitle() {
        String lTitle = "Summary";

        return lTitle;
    }

// -------------------------- OTHER METHODS --------------------------

    private void createReferenceColors() {
        // Get the number of positions.
        int lPositionCount = MainInformationFeeder.getInstance().getNumberOfExperimentalPositions();
        // Create an Color array for every position.
        if (lPositionCount == 0) {
            iReferenceColors = new Color[1];
        } else {
            iReferenceColors = new Color[lPositionCount];
        }

        // Construct a random number generator.
        Random rand = new Random();

        // The first 'default color' is constant GREEN.
        iReferenceColors[0] = new Color(20, 150, 20);
        // The other colors are random.
        for (int i = 1; i < iReferenceColors.length; i++) {
            // Random colors by random rgb intensities between 100-250
            int r = 100 + rand.nextInt(150);
            int g = 100 + rand.nextInt(150);
            int b = 100 + rand.nextInt(150);
            iReferenceColors[i] = new Color(r, g, b);
        }
    }

    private void createUIComponents() {
        // empty.
    }

    /**
     * Add listeners.
     */
    private void setListeners() {
        chkAverage.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                updateSampleSize();
            }
        });
    }

    /**
     * Set the sample size.
     */
    private void updateSampleSize() {
        spinSampleSize.setValue(iParent.getPositionSampleSize(isAveragedSampleSize()));
        spinSampleSize.updateUI();
    }

    /**
     * Returns whether or not the sampling size should be averaged.
     *
     * @return
     */
    public boolean isAveragedSampleSize() {
        return chkAverage.isSelected();
    }


    /**
     * Set the visibility of the options.
     */
    private void setOptionsVisible() {
        if (iParent.isExpert()) {
            // Expert mode, set all visible!
            jpanOptions.setVisible(true);
            // If two positions, also enable the 'average' chkbox.
            if (iParent.hasTwoExperiments()) {
                chkAverage.setVisible(true);
            } else {
                chkAverage.setVisible(false);
            }
        } else {
            jpanOptions.setVisible(false);
        }
    }

    public void startUpdatingReference() {
        // Start the timer for updating the reference text..
        iTimer.start();
    }

    /**
     * Update the reference text panel by the selected fasta file and sampling type options.
     */
    private void updateReferenceText() {
        // First get the FastaSequence.
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();

        Protein lProtein;
        String lSequence;

        if (lFeeder.getSamplingType().equals(SamplingTypeEnum.REGIONAL)) {
            // The regional fasta sequenceset will a regional peptide.
            // After this has been returned, we fetch the active
            lSequence = iRegionalFastaSequenceSet.nextSequence();
            lProtein = iRegionalFastaSequenceSet.getParentProtein();
        } else {
            // The other fasta sequence sets simply retrieve a Protein and fetch the respecive protein sequence.
            lProtein = iFastaSequenceSet.nextProtein();
            lSequence = lProtein.getSequence().getSequence();
        }

        String lHeader;
        lHeader = ">" + lProtein.getHeader().getAccession() + " - " + lProtein.getHeader().getDescription() + "\n";

        // Split the sequence in parts of 75 aa.
        String lFullSequence = lProtein.getSequence().getSequence();
        ArrayList<String> lSequenceList = cleaveSequence(lFullSequence, iFastaLineLength);

        StringBuffer sb = new StringBuffer();

        sb.append(lHeader + "\n");
        for (int j = 0; j < lSequenceList.size(); j++) {
            String s = lSequenceList.get(j);
            sb.append(s + "\n");
        }

        // Set the content type.
        //txtReference.setContentType("text");
        txtReference.setText(sb.toString());
        // Update the font for the whole document.
        setSamplingColors(lSequence, lHeader);
        txtReference.updateUI();

        System.out.println("Updated reference sequence.");
    }

    /**
     * This method chops the given sequence into lines of length aLineLength
     *
     * @param aSequence
     * @param aLineLength
     * @return
     */
    private ArrayList<String> cleaveSequence(final String aSequence, final int aLineLength) {
        // container object.
        ArrayList<String> lSequenceList = new ArrayList<String>();

        int i = 1;
        // While there are more lines..
        while ((i * aLineLength) < aSequence.length()) {
            String s = aSequence.substring((i - 1) * aLineLength, i * aLineLength);
            // Add them to the list.
            lSequenceList.add(s);
            i++;
        }

        // Add the trailing sequence line.
        lSequenceList.add(aSequence.substring((i - 1) * aLineLength, aSequence.length()));
        return lSequenceList;
    }

    /**
     * Set the samplingColors to the Reference textpane for the given sequence.
     *
     * @param aSequence
     */
    private void setSamplingColors(String aSequence, String aHeader) {
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        SamplingTypeEnum lSamplingType = iParent.getSamplingType();

        ArrayList<AnnotationIndex> indices = new ArrayList<AnnotationIndex>();
        Color base = iReferenceColors[0];

        int lOffset = -1;
        int lLength = -1;
        int lLineBreakChars = -1;

        switch (lSamplingType) {
            case HORIZONTAL:
                // Get the horizontal offset.
                lOffset = lFeeder.getAnchorStartPosition();
                // Get the horizontal length.
                lLength = lFeeder.getHorizontalSamplingLength();
                // Verify the sequence length.
                if (lOffset + lLength < aSequence.length()) {
                    // Create a colorindex.
                    indices.add(new AnnotationIndex(lOffset, lLength, base));
                }
                break;

            case RANDOM:
                // Get a random index over the sequence.

                for (int i = 0; i < 10; i++) {
                    lOffset = (int) Math.floor(Math.random() * aSequence.length());
                    indices.add(new AnnotationIndex(lOffset, 1, base));
                }

                break;

            case REGIONAL:
                // Locate the regional peptide in the active protein.
                int lRegionalIndex = iRegionalFastaSequenceSet.getActiveSequence().indexOf(aSequence); // Center around the anchor.

                lLineBreakChars = (int) Math.floor(lRegionalIndex / iFastaLineLength);
                lOffset = lRegionalIndex + lLineBreakChars;

                int lRegionalLength = lFeeder.getNumberOfExperimentalPositions();
                // lazy cache an the experimental set.

                if (iReferenceColors.length != lRegionalLength) {
                    throw new IllegalArgumentException("The number of reference colors (" + iReferenceColors.length +
                            ") does not match the number of positions from the MainInformationFeeder(" + lRegionalLength + ")!!");
                }

                // Set a different color to each 'vertical' amino acid.
                for (int i = 0; i < iReferenceColors.length; i++) {
                    Color lReferenceColor = iReferenceColors[i];
                    int lRunningOffset = lOffset + i;
                    indices.add(new AnnotationIndex(lRunningOffset, 1, lReferenceColor));
                }

                break;

            case TERMINAL:
                lOffset = lFeeder.getAnchorStartPosition();
                int lVerticalLength = lFeeder.getNumberOfExperimentalPositions();
                if (lFeeder.getReferenceDirection() == SamplingDirectionEnum.CtermToNterm) {
                    // If the direction is from c to n term, then inverse the offset value!!
                    // PLUS, correct for line-break chars.
                    lLineBreakChars = (int) Math.floor(aSequence.length() / iFastaLineLength);
                    lOffset = aSequence.length() + lLineBreakChars - (lOffset + lVerticalLength);
                }

                if (iReferenceColors.length != lVerticalLength) {
                    throw new IllegalArgumentException("The number of reference colors (" + iReferenceColors.length +
                            ") does not match the number of positions from the MainInformationFeeder(" + lVerticalLength + ")!!");
                }

                // Set a different color to each 'vertical' amino acid.
                for (int i = 0; i < iReferenceColors.length; i++) {
                    Color lReferenceColor = iReferenceColors[i];
                    int lRunningOffset = lOffset + i;
                    indices.add(new AnnotationIndex(lRunningOffset, 1, lReferenceColor));
                }

                break;
        }

        // First set all color to neutral.
        setReferenceAnnotationNeutral();

        int lHeaderLength = aHeader.length() + 1; // Header characters count also - add this length to the offset based on the sequence!!

        // Ok, then render all the indices.
        for (int i = 0; i < indices.size(); i++) {
            AnnotationIndex lAnnotationIndex = indices.get(i);
            lAnnotationIndex.index = lAnnotationIndex.index + lHeaderLength;
            // Add the to the document by this method.
            addReferenceAnnotation(lAnnotationIndex);
        }
    }

    /**
     * This method sets the reference text panel to neutral gray.
     */
    private void setReferenceAnnotationNeutral() {
        // Second remove all annotation.

        MutableAttributeSet attrs = txtReference.getInputAttributes();

        StyleConstants.setFontSize(attrs, 12);
        StyleConstants.setBold(attrs, false);
        StyleConstants.setForeground(attrs, Color.BLACK);

        // Retrieve the pane's document object
        StyledDocument doc = txtReference.getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        doc.setCharacterAttributes(0, txtReference.getText().length(), attrs, false);
    }

    /**
     * Add a ColorIndex to the Reference JTextPane.
     *
     * @param aIndex
     */
    public void addReferenceAnnotation(AnnotationIndex aIndex) {
        // Get the document.
        // Localize the stylename.
        String lColorStyleName = aIndex.getName();
        String lBoldStyleName = "bold";

        MutableAttributeSet attrs = txtReference.getInputAttributes();

        // Set the font family, size, and style, based on properties of
        // the Font object. Note that JTextPane supports a number of
        // character attributes beyond those supported by the Font class.
        // For example, underline, strike-through, super- and sub-script.
        StyleConstants.setFontSize(attrs, 14);
        StyleConstants.setBold(attrs, true);
        StyleConstants.setForeground(attrs, aIndex.color);

        // Retrieve the pane's document object
        StyledDocument doc = txtReference.getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        doc.setCharacterAttributes(aIndex.index, aIndex.end, attrs, false);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
     * code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanContent.add(spacer1, gbc);
        jpanPosition = new JPanel();
        jpanPosition.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanPosition, gbc);
        jpanPosition.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Compare the sampled amino acid distributions to:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP));
        jpanExperimentSequence = new JPanel();
        jpanExperimentSequence.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanPosition.add(jpanExperimentSequence, gbc);
        jpanExperimentSequence.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Position", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanExperimentSequence.add(scrollPane1, gbc);
        txtPosition = new JTextPane();
        txtPosition.setEditable(false);
        txtPosition.setMargin(new Insets(10, 10, 10, 10));
        txtPosition.setText("");
        scrollPane1.setViewportView(txtPosition);
        jpanExperimentSequenceTwo = new JPanel();
        jpanExperimentSequenceTwo.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanPosition.add(jpanExperimentSequenceTwo, gbc);
        jpanExperimentSequenceTwo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Position two", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanExperimentSequenceTwo.add(scrollPane2, gbc);
        txtPositionTwo = new JTextPane();
        txtPositionTwo.setEditable(false);
        txtPositionTwo.setMargin(new Insets(10, 10, 10, 10));
        txtPositionTwo.setText("");
        scrollPane2.setViewportView(txtPositionTwo);
        final JScrollPane scrollPane3 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(scrollPane3, gbc);
        scrollPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Simulation of sampling from 'fasta'file", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP));
        txtReference = new JTextPane();
        txtReference.setDoubleBuffered(true);
        txtReference.setEditable(false);
        txtReference.setFont(new Font("Consolas", txtReference.getFont().getStyle(), 12));
        txtReference.setMargin(new Insets(10, 15, 15, 10));
        txtReference.setText("");
        scrollPane3.setViewportView(txtReference);
        jpanOptions = new JPanel();
        jpanOptions.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 6;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanOptions, gbc);
        lblSampleSize = new JLabel();
        lblSampleSize.setText("Fasta samples for mean:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanOptions.add(lblSampleSize, gbc);
        spinSampleSize = new JSpinner();
        spinSampleSize.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanOptions.add(spinSampleSize, gbc);
        chkAverage = new JCheckBox();
        chkAverage.setText("Average of position one and two?");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanOptions.add(chkAverage, gbc);
        lblItartionSize = new JLabel();
        lblItartionSize.setText("Iterations:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanOptions.add(lblItartionSize, gbc);
        spinIterationSize = new JSpinner();
        spinIterationSize.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanOptions.add(spinIterationSize, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanOptions.add(spacer2, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }

    // -------------------------- INNER CLASSES --------------------------

    /**
     * Inner class for indexing colors and indices for annotating a document.
     */
    private class AnnotationIndex {
        int index = -1;
        int end = -1;
        Color color = null;

        AnnotationIndex(final int aIndex, final int aEnd, final Color aColor) {
            index = aIndex;
            end = aEnd;
            color = aColor;
        }

        // Return a integer name for the color.

        public String getName() {
            return "" + color.getRed() + color.getGreen() + color.getBlue();
        }
    }
}
