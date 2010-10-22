package com.compomics.icelogo.gui.forms.wizard;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.sequenceset.RawSequenceSet;
import com.compomics.icelogo.core.enumeration.ExperimentTypeEnum;
import com.compomics.icelogo.core.enumeration.SamplingTypeEnum;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 18, 2009
 * Time: 11:36:53 AM
 * <p/>
 * This class
 */
public class ExperimentStep extends AbstractSamplingWizardStep {
// ------------------------------ FIELDS ------------------------------

    // GUI components.
    private JTextPane txtSequencesTwo;
    private JTextPane txtSequence;

    private JPanel jpanSequenceTwo;

    private JPanel jpanSequence;
    private JPanel jpanContent;
    private JCheckBox chkSecondSet;

    private JSpinner spinAnchor;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Create a new panel to setup the position set.
     *
     * @param aParent
     */
    public ExperimentStep(final SamplingWizard aParent) {
        super(aParent);
        $$$setupUI$$$();

        // Construct the panel.
        construct();
    }

    /**
     * This method will construct things that are not done in the normal panel constructor
     */
    public void construct() {


        // Display the sequence anchor depending on the sampling type.

        if (this.getPositionDocument(ExperimentTypeEnum.EXPERIMENT) != null) {
            txtSequence.setDocument(this.getPositionDocument(ExperimentTypeEnum.EXPERIMENT));
        }
        if (this.getPositionDocument(ExperimentTypeEnum.EXPERIMENT_TWO) != null) {
            txtSequencesTwo.setDocument(this.getPositionDocument(ExperimentTypeEnum.EXPERIMENT_TWO));
        }

        setListeners();
        // Set selected to true, then invoke a click to false.
        boolean boolTwoSet = MainInformationFeeder.getInstance().isTwoExperiment();
        chkSecondSet.setSelected(!boolTwoSet);
        chkSecondSet.doClick();


        // Make the editors paint the anchor in color.
        if (isRegionalSampling()) {
            MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();

            spinAnchor.setValue(lFeeder.getExperimentAnchorValue() + 1);
            spinAnchor.setVisible(true);
            spinAnchor.setForeground(Color.red);

            updateColorAnchor();
        } else {
            spinAnchor.setVisible(false);
        }


        jpanContent.updateUI();
    }

    private void updateColorAnchor() {
        constructColorAnchor(txtSequence);
        constructColorAnchor(txtSequencesTwo);
    }


// ------------------------ INTERFACE METHODS ------------------------


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
        MainInformationFeeder.getInstance().clearExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        MainInformationFeeder.getInstance().clearExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
        txtSequence.setText("");
        txtSequencesTwo.setText("");
        spinAnchor.setValue(1);
        if (chkSecondSet.isSelected()) {
            chkSecondSet.doClick();
        }
        setFeasableToProceed(true);
    }

    /**
     * This method will be performed when the next button is clicked
     */
    public void nextClicked() {
        setFeasableToProceed(true);
        if (txtSequence.getText().equalsIgnoreCase("")) {
            setFeasableToProceed(false);
            setNotFeasableReason("Your experimental set is not defined");
        }
        if (txtSequencesTwo.getText().equalsIgnoreCase("") && chkSecondSet.isSelected()) {
            setFeasableToProceed(false);
            setNotFeasableReason("Your experimental set is not defined");
        }
    }

    /**
     * Returns a title for the step.
     */
    public String getTitle() {
        String lTitle = "Define the experimental set";
        if (isRegionalSampling()) {
            lTitle = lTitle + " and set the regional anchor";
        }
        return lTitle;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Creates a colored column at aIndex on the given JTextPane.
     *
     * @param aJTextPane The JTextPane to color.
     */
    public static void constructColorAnchor(JTextPane aJTextPane) {
        // First apply to the first sequenceset.

        String txt = aJTextPane.getText();
        // Variable line breaks in different OS should be unified into \n.
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
            txt = txt.replaceAll("\r\n", "\n");
        } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
            txt = txt.replaceAll("\r", "\n");
        }
        StringTokenizer lStringTokenizer = new StringTokenizer(txt, "\n");

        // Variable for the entry length.
        int lEntryLength = 0;
        // Variable for the number of lines.
        int lNumberOfEntries = lStringTokenizer.countTokens();

        // Get the first token and save its length.
        if (lStringTokenizer.hasMoreTokens()) {
            String lFirstToken = lStringTokenizer.nextToken();
            lEntryLength = lFirstToken.length();
        }

        //remove the previous highlights
        Highlighter highlighter = aJTextPane.getHighlighter();

        // Remove any existing highlights for last word

        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            highlighter.removeHighlight(h);
        }


        int lIndex;
        lIndex = MainInformationFeeder.getInstance().getExperimentAnchorValue();
        // Set text in the anchors to the style,
        for (int i = 0; i < lNumberOfEntries; i++) {
            int lOffset = (i * (lEntryLength + 1)) + (lIndex); // +1 counts for line chars.
            try {
                aJTextPane.getHighlighter().addHighlight(lOffset, lOffset + 1, DefaultHighlighter.DefaultPainter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void setListeners() {
        // Add a checkbox for the second position set.
        chkSecondSet.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            public void actionPerformed(final ActionEvent e) {
                if (chkSecondSet.isSelected()) {
                    setSequenceTwoVisible(true);
                } else {
                    setSequenceTwoVisible(false);
                }
            }
        });

        ChangeListener anchorListener = new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                if (isRegionalSampling()) {
                    MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
                    lFeeder.setExperimentAnchorValue((Integer) spinAnchor.getValue() - 1); // In program 0-values!!
                    // Always update the color.
                    updateColorAnchor();
                }
            }
        };
        spinAnchor.addChangeListener(anchorListener);

        // Add listeners to the textpanes.

        DocumentListener txtListener = new DocumentListener() {
            private Document doc;

            public void insertUpdate(DocumentEvent e) {
                doc = e.getDocument();
                verifyInput();
            }

            public void removeUpdate(DocumentEvent e) {
                doc = e.getDocument();
                verifyInput();
            }

            public void changedUpdate(DocumentEvent e) {
                doc = e.getDocument();
                verifyInput();
            }

            private void verifyInput() {
                try {
                    // First localize the sequenceSet.
                    RawSequenceSet lSequenceSet = null;
                    if (doc.equals(txtSequence.getDocument())) {
                        lSequenceSet = MainInformationFeeder.getInstance().getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
                    } else if (doc.equals(txtSequencesTwo.getDocument())) {
                        lSequenceSet = MainInformationFeeder.getInstance().getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
                    }

                    lSequenceSet.clearContent();

                    String content = doc.getText(0, doc.getLength());
                    int lFirstEntryLength = 0;
                    String lFirstEntrySequence = "";
                    int lines = 0;
                    for (StringTokenizer token = new StringTokenizer(content, "\n"); token.hasMoreTokens();) {
                        // Get the next token.
                        String s = token.nextToken();
                        lines++;
                        // First entry, set the entrylength!
                        if (lines == 1) {
                            lFirstEntryLength = s.length();
                            lFirstEntrySequence = s;
                            // Add to the sequenceset.
                            lSequenceSet.add(s);
                        }
                        // The last token can be an empty String, a new line.
                        if (!token.hasMoreTokens()) {
                            if (s.length() == 0) {
                                // If last token is empty, break te loop as we are done!
                                break;
                            } else if (s.length() > lFirstEntryLength) {
                                JOptionPane.showMessageDialog(jpanContent, "Sequence Length for all entries must be equal!! (here: '" + lFirstEntrySequence + "' is not as long as '" + s + "')");
                            } else if (s.length() == lFirstEntryLength) {
                                lSequenceSet.add(s);
                            } else {
                                // The last line can be in busy in modifiation. Do nothing.
                            }
                        } else {
                            // We are busy with the inner lines.
                            // If the token length is different then the first - call an option dialog and remove all content.
                            if (lFirstEntryLength != s.length()) {
                                JOptionPane.showMessageDialog(jpanContent, "Sequence Length for all entries must be equal!! (here: '" + lFirstEntrySequence + "' is not as long as '" + s + "')");
                            } else {
                                // Add to the sequenceSet.
                                lSequenceSet.add(s);
                            }
                        }
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        };

        txtSequence.getDocument().addDocumentListener(txtListener);
        txtSequencesTwo.getDocument().addDocumentListener(txtListener);


        //set the correct style for the JTextPanes
        StyledDocument doc = txtSequence.getStyledDocument();
        StyledDocument doc2 = txtSequencesTwo.getStyledDocument();

        MutableAttributeSet attrsBlack = txtSequence.getInputAttributes();
        MutableAttributeSet attrsBlack2 = txtSequencesTwo.getInputAttributes();
        Font lFontToSet = new Font("Courier New", txtSequencesTwo.getFont().getStyle(), 12);
        StyleConstants.setFontFamily(attrsBlack, lFontToSet.getFamily());
        StyleConstants.setFontFamily(attrsBlack2, lFontToSet.getFamily());
        StyleConstants.setFontSize(attrsBlack, lFontToSet.getSize());
        StyleConstants.setFontSize(attrsBlack2, lFontToSet.getSize());
        // Set the font color
        StyleConstants.setForeground(attrsBlack, Color.BLACK);
        StyleConstants.setForeground(attrsBlack2, Color.BLACK);

        doc.setCharacterAttributes(0, 1000, attrsBlack, true);
        doc2.setCharacterAttributes(0, 1000, attrsBlack2, true);


    }

    /**
     * Set the visibility status of sequence set two.
     *
     * @param b
     */
    private void setSequenceTwoVisible(final boolean b) {
        // Turn the GUI components (in)visible in response to the checkbox.
        jpanSequenceTwo.setVisible(b);
        // Trace the action for the parent.
        iParent.setTwoExperiments(b);
    }

    /**
     * Returns whether or not the position anchor must be shown.
     *
     * @return
     */
    private boolean isRegionalSampling() {
        SamplingTypeEnum lSamplingType = iParent.getSamplingType();
        if (lSamplingType == SamplingTypeEnum.REGIONAL) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the active Position JTextPane by the PositionTypeEnum.
     *
     * @param aType PositionTypeEnum
     * @return JTextPane
     */
    public Document getPositionDocument(ExperimentTypeEnum aType) {
        Document lResult = null;
        switch (aType) {
            case EXPERIMENT:
                lResult = txtSequence.getStyledDocument();
                break;
            case EXPERIMENT_TWO:
                lResult = txtSequencesTwo.getStyledDocument();
                break;
        }
        return lResult;
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        jpanSequenceTwo = new JPanel();
        jpanSequenceTwo.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanSequenceTwo, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        jpanSequenceTwo.add(scrollPane1, gbc);
        txtSequencesTwo = new JTextPane();
        txtSequencesTwo.setFont(new Font("Ravie", txtSequencesTwo.getFont().getStyle(), 12));
        txtSequencesTwo.setMargin(new Insets(10, 15, 10, 15));
        txtSequencesTwo.setText("");
        scrollPane1.setViewportView(txtSequencesTwo);
        jpanSequence = new JPanel();
        jpanSequence.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanSequence, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        jpanSequence.add(scrollPane2, gbc);
        txtSequence = new JTextPane();
        txtSequence.setFont(new Font("Courier New", txtSequence.getFont().getStyle(), 12));
        txtSequence.setMargin(new Insets(10, 15, 10, 15));
        txtSequence.setText("");
        scrollPane2.setViewportView(txtSequence);
        chkSecondSet = new JCheckBox();
        chkSecondSet.setHorizontalTextPosition(2);
        chkSecondSet.setText("Second set?");
        chkSecondSet.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 0);
        jpanContent.add(chkSecondSet, gbc);
        spinAnchor = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.ipadx = 30;
        jpanContent.add(spinAnchor, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
