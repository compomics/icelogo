package com.compomics.icelogo.gui.forms.wizard;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.sequenceset.FastaSequenceSet;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.enumeration.SamplingDirectionEnum;
import com.compomics.icelogo.core.enumeration.SamplingTypeEnum;
import com.compomics.icelogo.core.interfaces.ISequenceSet;
import com.compomics.icelogo.gui.forms.FastaDownloaderForm;
import com.compomics.icelogo.gui.model.FastaFileComboBoxModel;
import com.compomics.icelogo.gui.renderer.FastaFileListCellRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Jun 17, 2009 Time: 3:51:43 PM
 * <p/>
 * This class
 */
public class ReferenceStep extends AbstractSamplingWizardStep implements Observer {
    // GUI components.
    private JPanel jpanContent;
    private JPanel jpanReferenceOptions;
    private JPanel jpanReferenceType;

    private JRadioButton rdbRandom;
    private JRadioButton rdbTerminal;
    private JRadioButton rdbNterm;
    private JRadioButton rdbCterm;
    private JRadioButton rdbRegional;

    private JSpinner spinIterationSize;
    private JSpinner spinStartPosition;
    private JSpinner spinLength;

    private JLabel lblIterationSize;
    private JLabel lblStartPosition;
    private JLabel lblLength;

    private JComboBox cmbFastaFile;

    private JLabel lblIllustration;

    private ButtonGroup rdbSamplingGroup;
    private ActionListener actionSampling;


    /**
     * Construct the SamplingStep panel.
     *
     * @param aParent Parent wizard
     */
    public ReferenceStep(final SamplingWizard aParent) {
        super(aParent);
        // Set the parent wizard.
        $$$setupUI$$$();
        MainInformationFeeder.getInstance().addObserver(this);

        // Construct the panel.
        construct();

        iParent.addObserver(this);

    }

    public JPanel getContentPane() {
        return jpanContent;
    }

    public void backClicked() {
        iParent.setSamplingType(null);
    }

    public void nextClicked() {
        // All but an appropriate fasta file for the reference set have default params.
        Object o = cmbFastaFile.getSelectedItem();
        if (o instanceof File) {
            File lFile = (File) o;
            if (lFile.exists()) {
                FastaSequenceSet lSequenceSet;
                lSequenceSet = new FastaSequenceSet(lFile.getAbsolutePath(), lFile.getName());

                if (lSequenceSet.test(lFile)) {
                    MainInformationFeeder.getInstance().setActiveReferenceSet(lSequenceSet);
                    setFeasableToProceed(true);
                } else {
                    // Failed to test the FastaSequenceSet!!
                    setFeasableToProceed(false);
                    setNotFeasableReason("Failed to test '" + cmbFastaFile.getSelectedItem().toString() + "' for fasta content!!");
                }
            } else {
                setFeasableToProceed(false);
                setNotFeasableReason("'" + cmbFastaFile.getSelectedItem().toString() + "' does not exist!!");
            }
        } else {
            setFeasableToProceed(false);
            setNotFeasableReason("No fasta file selected!! (Add a fasta file by the drop down menu.)");
        }
    }

    public void construct() {
        // First create the core components.
        $$$setupUI$$$();

        // Second add the action listeners to the buttons.
        setActionListeners();

        MainInformationFeeder.getInstance().addObserver(ReferenceStep.this);

        // Set models and renders for the comboboxmodels.
        constructFastaCombobox();

        // Activate default sampling radiobutton.
        doSamplingTypeButton();

        // Set the values to the spinners.
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        spinIterationSize.setValue(lFeeder.getIterationSize());
        spinLength.setValue(lFeeder.getHorizontalSamplingLength());
        spinStartPosition.setValue(lFeeder.getAnchorStartPosition() + 1);

        // Set the values to the direction rdbs.
        SamplingDirectionEnum lDirection = lFeeder.getReferenceDirection();
        if (lDirection == SamplingDirectionEnum.NtermToCterm) {
            rdbNterm.setSelected(true);
        } else if (lDirection == SamplingDirectionEnum.CtermToNterm) {
            rdbCterm.setSelected(true);
        }

        // Update the UI.
        jpanReferenceOptions.updateUI();
    }

    /**
     * Activates the appropriate SamplingType radiobutton according to the SamplingType set on the parent.
     */
    private void doSamplingTypeButton() {
        SamplingTypeEnum lSamplingTypeEnum = iParent.getSamplingType();
        JRadioButton rdb = null;

        switch (lSamplingTypeEnum) {

            case RANDOM:
                rdb = rdbRandom;
                break;
            case REGIONAL:
                rdb = rdbRegional;
                break;
            case TERMINAL:
                rdb = rdbTerminal;
                break;
        }

        if (rdb != null) {
            rdb.setSelected(true);
            rdb.doClick();
        }
    }

    public void constructFastaCombobox() {
        FastaFileComboBoxModel lFastaComboBoxModel = new FastaFileComboBoxModel(cmbFastaFile);
        cmbFastaFile.setModel(lFastaComboBoxModel);
        ISequenceSet lActiveSequenceSet = MainInformationFeeder.getInstance().getActiveReferenceSet();
        // Verify the active set is derived from a Fasta file,
        if (lActiveSequenceSet instanceof FastaSequenceSet) {
            // And then set the file into the combobox.
            lFastaComboBoxModel.setSelectedFastaFile(((FastaSequenceSet) lActiveSequenceSet).getFastaFile());
        }
        cmbFastaFile.setRenderer(new FastaFileListCellRenderer());
        cmbFastaFile.updateUI();
    }

    /**
     * Returns a title for the step.
     */
    public String getTitle() {
        return "Define the reference set";  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void setActionListeners() {

        // Set actionlisteners for the sampling radiobuttons.
        actionSampling = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                setAllOptionsVisible(false);
                // RANDOM actions.
                if (rdbRandom.isSelected()) {
                    iParent.setSamplingType(SamplingTypeEnum.RANDOM);
                    if (iParent.isExpert()) {
                        lblIterationSize.setVisible(true);
                        spinIterationSize.setVisible(true);
                    }
                }
                // TERMINAL actions.
                else if (rdbTerminal.isSelected()) {
                    iParent.setSamplingType(SamplingTypeEnum.TERMINAL);
                    spinStartPosition.setVisible(true);
                    lblStartPosition.setVisible(true);
                    if (iParent.isExpert()) {
                        lblIterationSize.setVisible(true);
                        spinIterationSize.setVisible(true);
                        rdbCterm.setVisible(true);
                        rdbNterm.setVisible(true);
                    }
                }
                // REGIONAL actions.
                else if (rdbRegional.isSelected()) {
                    iParent.setSamplingType(SamplingTypeEnum.REGIONAL);
                    if (iParent.isExpert()) {
                        lblIterationSize.setVisible(true);
                        spinIterationSize.setVisible(true);
                    }
                } else {
                    // ELSE.
                    iParent.setSamplingType(null);
                }
                // Always update the illustration label.
                updateIllustration();
            }
        };

        rdbTerminal.addActionListener(actionSampling);
        rdbRandom.addActionListener(actionSampling);
        rdbRegional.addActionListener(actionSampling);

        /**
         * Adds an actionlistener to the fasta combobox to update the label aside.
         */
        cmbFastaFile.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                // Listen for changes that need an update!
                String s = ";";
                Object lSelectedItem = cmbFastaFile.getSelectedItem();
                if (lSelectedItem.equals(FastaFileComboBoxModel.AddString)) {
                    MainInformationFeeder.getInstance().addObserver(ReferenceStep.this);
                    fileSelection();
                } else if (lSelectedItem.equals(FastaFileComboBoxModel.DownloadString)) {
                    MainInformationFeeder.getInstance().addObserver(ReferenceStep.this);
                    new FastaDownloaderForm();
                }
            }
        });


        /**
         * Add an action when the reference samplin direction changes.
         */
        ActionListener lRdbDirectionListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
                if (e.getSource() == rdbNterm) {
                    lFeeder.setReferenceDirection(SamplingDirectionEnum.NtermToCterm);
                } else if (e.getSource() == rdbCterm) {
                    lFeeder.setReferenceDirection(SamplingDirectionEnum.CtermToNterm);
                }
            }
        };
        rdbCterm.addActionListener(lRdbDirectionListener);
        rdbNterm.addActionListener(lRdbDirectionListener);

        /**
         * Sync changes from the spinners to the information feeder.
         *
         */
        ChangeListener spinListener = new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
                // Start position spinner.
                if (e.getSource() == spinStartPosition) {
                    lFeeder.setAnchorStartPosition(new Integer(spinStartPosition.getValue().toString()) - 1); // Make the GUI value 0-based.
                }
                // Iteration size spinner.
                else if (e.getSource() == spinIterationSize) {
                    lFeeder.setIterationSize(new Integer(spinIterationSize.getValue().toString()));
                }
                // Horizontal sampling length spinner.
                else if (e.getSource() == spinLength) {
                    lFeeder.setHorizontalSamplingLength(new Integer(spinLength.getValue().toString()));
                }
            }
        };
        spinStartPosition.addChangeListener(spinListener);
        spinLength.addChangeListener(spinListener);
        spinIterationSize.addChangeListener(spinListener);


    }

    /**
     * Set the illustration according to the selected radiobutton.
     */
    private void updateIllustration() {
        SamplingTypeEnum lSamplingType = iParent.getSamplingType();
        ImageIcon lIcon = null;
        if (lSamplingType == SamplingTypeEnum.REGIONAL) {
            lIcon = new ImageIcon(ClassLoader.getSystemResource("icons/regional.png"));
        } else if (lSamplingType == SamplingTypeEnum.RANDOM) {
            lIcon = new ImageIcon(ClassLoader.getSystemResource("icons/random.png"));
        } else if (lSamplingType == SamplingTypeEnum.TERMINAL) {
            lIcon = new ImageIcon(ClassLoader.getSystemResource("icons/terminal.png"));
        }
        /**else if (lSamplingType == SamplingTypeEnum.HORIZONTAL) {
         lIcon = new ImageIcon(ClassLoader.getSystemResource("icons/horizontal.png"));
         }    */

        lblIllustration.setIcon(lIcon);
    }

    /**
     * This method turns all GUI option fields as Visible (true or false).
     *
     * @param b
     */
    private void setAllOptionsVisible(boolean b) {
        lblLength.setVisible(b);
        spinLength.setVisible(b);

        lblIterationSize.setVisible(b);
        spinIterationSize.setVisible(b);

        spinStartPosition.setVisible(b);
        lblStartPosition.setVisible(b);
        rdbCterm.setVisible(b);
        rdbNterm.setVisible(b);
    }


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
                actionSampling.actionPerformed(new ActionEvent(this, 1, "expert_call"));
            }
            if (arg.equals(ObservableEnum.NOTIFY_FASTA_COMBOBOX)) {
                constructFastaCombobox();
                jpanContent.updateUI();
            }
        }
    }

    private void fileSelection() {

        // Previous selected path.
        String lPath = "";

        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        ArrayList<File> lFiles = lFeeder.getFastaFiles();
        if (lFiles != null) {
            File lFile = lFiles.get(0);
            if (lFile != null) {
                lPath = lFile.getPath();
            } else {
                lPath = System.getProperty("user.home");
            }
        }

        // The file filter to use.
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                boolean result = false;
                if (f.isDirectory() || f.getName().endsWith(".fas") || f.getName().endsWith(".fasta")) {
                    result = true;
                }
                return result;
            }

            public String getDescription() {
                return "Fasta dat file (.fas,.fasta)";
            }
        };
        JFileChooser jfc = new JFileChooser(lPath);
        jfc.setDialogTitle("Select fasta file");
        jfc.setDialogType(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(filter);
        int returnVal = jfc.showOpenDialog(getContentPane());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File lFile = jfc.getSelectedFile();
            // Check for existing file.
            if (lFile.exists() && lFile.isFile()) {
                lFeeder.addFastaFile(lFile);
            }
        }
    }


    private void createUIComponents() {
        spinIterationSize = new JSpinner();
        SpinnerNumberModel lNumberModel = new SpinnerNumberModel(30, 30, 100000, 1);
        spinIterationSize.setModel(lNumberModel);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
     * code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        jpanReferenceOptions = new JPanel();
        jpanReferenceOptions.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanReferenceOptions, gbc);
        jpanReferenceOptions.setBorder(BorderFactory.createTitledBorder("Options"));
        lblLength = new JLabel();
        lblLength.setText("Length");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(lblLength, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Fasta file");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(label1, gbc);
        cmbFastaFile = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 30;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(cmbFastaFile, gbc);
        spinLength = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 30;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(spinLength, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.gridheight = 17;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanReferenceOptions.add(spacer1, gbc);
        lblIterationSize = new JLabel();
        lblIterationSize.setText("Iteration size");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(lblIterationSize, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 30;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(spinIterationSize, gbc);
        lblStartPosition = new JLabel();
        lblStartPosition.setText("Terminal index");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridheight = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(lblStartPosition, gbc);
        spinStartPosition = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 30;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(spinStartPosition, gbc);
        rdbCterm = new JRadioButton();
        rdbCterm.setText("C-terminus");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 16;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(rdbCterm, gbc);
        rdbNterm = new JRadioButton();
        rdbNterm.setSelected(true);
        rdbNterm.setText("N-terminus");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        jpanReferenceOptions.add(rdbNterm, gbc);
        jpanReferenceType = new JPanel();
        jpanReferenceType.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 10, 0);
        jpanContent.add(jpanReferenceType, gbc);
        rdbRandom = new JRadioButton();
        rdbRandom.setText("Random");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 10, 0);
        jpanReferenceType.add(rdbRandom, gbc);
        lblIllustration = new JLabel();
        lblIllustration.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 12;
        gbc.insets = new Insets(0, 10, 10, 0);
        jpanReferenceType.add(lblIllustration, gbc);
        rdbRegional = new JRadioButton();
        rdbRegional.setText("Regional");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 10, 0);
        jpanReferenceType.add(rdbRegional, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridheight = 12;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanReferenceType.add(spacer2, gbc);
        rdbTerminal = new JRadioButton();
        rdbTerminal.setText("Terminal");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 10, 0);
        jpanReferenceType.add(rdbTerminal, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanReferenceType.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 11;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanReferenceType.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanReferenceType.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanContent.add(spacer6, gbc);
        rdbSamplingGroup = new ButtonGroup();
        rdbSamplingGroup.add(rdbTerminal);
        rdbSamplingGroup.add(rdbTerminal);
        rdbSamplingGroup.add(rdbRandom);
        rdbSamplingGroup.add(rdbRegional);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rdbNterm);
        buttonGroup.add(rdbNterm);
        buttonGroup.add(rdbCterm);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
