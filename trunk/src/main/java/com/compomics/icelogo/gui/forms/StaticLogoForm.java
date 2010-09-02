package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.sequenceset.RawSequenceSet;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;
import com.compomics.icelogo.core.enumeration.ExperimentTypeEnum;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.enumeration.SamplingDirectionEnum;
import com.compomics.icelogo.core.factory.AminoAcidStatisticsFactory;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import com.compomics.icelogo.gui.component.Messenger;
import com.compomics.icelogo.gui.graph.*;
import com.compomics.icelogo.gui.interfaces.GraphableAcceptor;
import com.compomics.icelogo.gui.model.SpeciesComboBoxModel;
import com.compomics.icelogo.gui.renderer.SpeciesListCellRenderer;
import com.compomics.util.sun.SwingWorker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Niklaas Colaert Date: 30-sep-2008 Time: 10:46:11 To change this template use File |
 * Settings | File Templates.
 */
public class StaticLogoForm extends JPanel implements Observer {

    //gui stuff
    public JTextArea txtNeg;
    public JTextArea txtPos;
    public JCheckBox useSwissprotMeansCheckBox;
    public JComboBox cmbSwissprot;
    public JButton makeLogoButton;
    public JButton use2NegativeSetsButton;
    public JPanel jpanContent;
    public JComboBox swissMean1;
    public JLabel negOneLabel;
    public JLabel negTwoLabel;
    public JLabel splitLabel;
    public JButton useOneNegativeSetButton;
    public JSlider negativeSplit;
    public JTextArea txtNeg2;
    public JTextArea txtNeg1;
    public JPanel oneNegativePanel;
    public JPanel twoNegativePanel;
    public JComboBox swissMean2;
    public JTabbedPane negativeTab;
    public JCheckBox useSwissprotMeanCheckBox2;
    public JCheckBox useSwissprotMeanCheckBox1;
    private JPanel positivePanel;

    /**
     * Vector with all the different species
     */
    public Vector<SwissProtComposition> iSwissProtMeans = new Vector<SwissProtComposition>();
    /**
     * boolean that indicated if the negative sets has two parts
     */
    public boolean use2NegativeSets;
    /**
     * A peptide that will be used as an example in the labels
     */
    public String iPeptideExample = "Give Positive Set";
    /**
     * The accepter of the created panels (The parent)
     */
    private GraphableAcceptor iAcceptor = null;
    /**
     * An instance of the MainInformationFeeder singleton
     */
    private MainInformationFeeder iInfoFeeder;
    /**
     * The instance of this class
     */
    private static StaticLogoForm instance;

    /**
     * Constructur
     *
     * @param aAcceptor An acceptor for the panels that will be created
     */
    public StaticLogoForm(GraphableAcceptor aAcceptor) {
        super();
        //get an instance of the MainInformationFeeder
        iInfoFeeder = MainInformationFeeder.getInstance();
        //give a message to the Messenger
        Messenger.getInstance().sendMessage("Static iceLogo started");
        //set the acceptor
        iAcceptor = aAcceptor;
        //load the different swissprot compositions
        this.loadSwissprotCompositions();
        //create the gui
        $$$setupUI$$$();
        //set the labels
        setLabels();
        //add the jpanContent to this panel
        this.setLayout(new BorderLayout());
        add(jpanContent, BorderLayout.CENTER);
        //add action listeners
        makeLogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeLogo();
            }
        });
        txtPos.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyPressed(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyReleased(KeyEvent e) {
                String[] posSet = txtPos.getText().split("\n");
                if (posSet[0].length() > 0) {
                    iPeptideExample = posSet[0];
                    setSliderBounds();
                    setLabels();
                }
            }
        });
        negativeSplit.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setLabels();
            }
        });
        jpanContent.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                iInfoFeeder.setGraphableHeight(jpanContent.getHeight());
                iInfoFeeder.setGraphableWidth(jpanContent.getWidth());
            }
        });
        useSwissprotMeansCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (useSwissprotMeansCheckBox.isSelected()) {
                    txtNeg.setEnabled(false);
                } else {
                    txtNeg.setEnabled(true);
                }
            }
        });
        useSwissprotMeanCheckBox1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (useSwissprotMeanCheckBox1.isSelected()) {
                    txtNeg1.setEnabled(false);
                } else {
                    txtNeg1.setEnabled(true);
                }
            }
        });
        useSwissprotMeanCheckBox2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (useSwissprotMeanCheckBox2.isSelected()) {
                    txtNeg2.setEnabled(false);
                } else {
                    txtNeg2.setEnabled(true);
                }
            }
        });
        cmbSwissprot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cmbSwissprot.getSelectedItem().toString().equalsIgnoreCase(SpeciesComboBoxModel.AddString)) {
                    AddSpeciesForm aAdder = new AddSpeciesForm();
                }
            }
        });
        swissMean1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (swissMean1.getSelectedItem().toString().equalsIgnoreCase(SpeciesComboBoxModel.AddString)) {
                    AddSpeciesForm aAdder = new AddSpeciesForm();
                }
            }
        });
        swissMean2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (swissMean2.getSelectedItem().toString().equalsIgnoreCase(SpeciesComboBoxModel.AddString)) {
                    AddSpeciesForm aAdder = new AddSpeciesForm();
                }
            }
        });
    }


    /**
     * Get an instance of this class
     *
     * @param aAcceptor A tab acceptor
     * @return LogoNovice
     */
    public static StaticLogoForm getInstance(GraphableAcceptor aAcceptor) {
        if (instance == null) {
            instance = new StaticLogoForm(aAcceptor);
        }

        //Before we add this observer we will delete all the others
        MainInformationFeeder.getInstance().deleteObservers();
        MainInformationFeeder.getInstance().addObserver(instance);

        return instance;
    }


    private void createUIComponents() {

        cmbSwissprot = new JComboBox(iSwissProtMeans);
        swissMean1 = new JComboBox(iSwissProtMeans);
        swissMean2 = new JComboBox(iSwissProtMeans);
        constructSpeciesCombobox();

        //make things for two negative panel
        int min = 1;
        int max = iPeptideExample.length() - 1;
        int init = (max + 1) / 2;
        negativeSplit = new JSlider(JSlider.HORIZONTAL, min, max, init);
        negativeSplit.setMajorTickSpacing(5);
        negativeSplit.setMinorTickSpacing(1);
        negativeSplit.setPaintTicks(true);
        negativeSplit.setPaintLabels(true);
        negativeSplit.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    }

    /**
     * This method will set the labels in the "2 negative set" panel
     */
    public void setLabels() {
        splitLabel.setText("Split sequences on position: " + negativeSplit.getValue());
        String partOne = iPeptideExample.substring(0, negativeSplit.getValue());
        String partTwo = iPeptideExample.substring(negativeSplit.getValue());
        negOneLabel.setText("Example: " + partOne + " (length: " + partOne.length() + ")");
        negTwoLabel.setText("Example: " + partTwo + " (length: " + partTwo.length() + ")");
    }

    /**
     * This method will set the sliderbounds in the "2 negative set" panel
     */
    public void setSliderBounds() {
        int min = 1;
        int max = iPeptideExample.length() - 1;
        int init = (max + 1) / 2;
        negativeSplit.setMinimum(min);
        negativeSplit.setMaximum(max);
        negativeSplit.setValue(init);
        negativeSplit.setMajorTickSpacing(5);
        negativeSplit.setMinorTickSpacing(1);
        negativeSplit.setPaintTicks(true);
        negativeSplit.setPaintLabels(true);
        negativeSplit.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    }

    /**
     * Add elements to the MainInformationFeeder
     */
    public void fillTheInformationFeeder() {
        iInfoFeeder.setGraphableHeight(jpanContent.getHeight());
        iInfoFeeder.setGraphableWidth(jpanContent.getWidth());
    }


    /**
     * This method will make the different panels
     */
    private void makeLogo() {
        this.fillTheInformationFeeder();

        final Messenger iMessenger = Messenger.getInstance();
        final StaticLogoForm lParent = this;
        SwingWorker iceLogoCreator = new SwingWorker() {
            Boolean error = false;

            public Boolean construct() {
                iMessenger.setProgressIndeterminate(true);
                iMessenger.setProgressStringPainted(false);

                //check positive set
                if (!testPositveSet()) {
                    JOptionPane.showMessageDialog(new JFrame(), new String[]{"Not all peptide in the positve set have the same length"}, "Problem with positive set", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //Check if we need to use two negative sets
                if (negativeTab.getSelectedIndex() == 1) {
                    makeLogoForTwoNegativeSets();
                    return false;
                }
                //check negative set
                if (txtNeg.getText().equalsIgnoreCase("") && !useSwissprotMeansCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(new JFrame(), new String[]{"No negative set is available,\ngive a negative set or select a swissprot mean!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //Get the sequences and create sequence set
                String positiveSequences = txtPos.getText();
                String[] positiveSet = positiveSequences.split("\n");
                //check positive set length
                if (positiveSet.length == 1) {
                    JOptionPane.showMessageDialog(new JFrame(), new String[]{"No positive set is available!"}, "Problem with positive set", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //Create a raw sequence set for the positive sequences
                RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
                for (int i = 0; i < positiveSet.length; i++) {
                    lRawPositiveSequenceSet.add(positiveSet[i]);
                    iInfoFeeder.addSequence(ExperimentTypeEnum.EXPERIMENT, positiveSet[i]);
                }
                AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, positiveSet[0].length(), SamplingDirectionEnum.NtermToCterm);

                //create the negative statistics
                AminoAcidStatistics[] lNegativeStatistics;
                if (useSwissprotMeansCheckBox.isSelected()) {
                    if (cmbSwissprot.getSelectedItem().toString().equalsIgnoreCase(SpeciesComboBoxModel.AddString)) {
                        JOptionPane.showMessageDialog(new JFrame(), new String[]{"No correct species is selected, \nselect a correct species!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    SwissProtComposition lSelectedSpComp = (SwissProtComposition) cmbSwissprot.getSelectedItem();
                    if (lSelectedSpComp == null) {
                        JOptionPane.showMessageDialog(new JFrame(), new String[]{"You selected swissprot mean as a negative set,\nbut you didn't select a specie!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    AminoAcidStatistics lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lSelectedSpComp, positiveSet.length);
                    lNegativeStatistics = new AminoAcidStatistics[positiveSet[0].length()];
                    for (int i = 0; i < positiveSet[0].length(); i++) {
                        lNegativeStatistics[i] = lNegative;
                    }
                } else {
                    if (!testNegativeSet(txtNeg)) {
                        JOptionPane.showMessageDialog(new JFrame(), new String[]{"Not all peptide in the negative set have the same length"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    //Get the sequences and create sequence set
                    String negativeSequences = txtNeg.getText();
                    String[] negativeSet = negativeSequences.split("\n");
                    RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
                    for (int i = 0; i < negativeSet.length; i++) {
                        lRawNegativeSequenceSet.add(negativeSet[i]);
                    }
                    // always use the smallest set to do the statistics
                    if (positiveSet.length < negativeSet.length) {
                        lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, positiveSet[0].length(), positiveSet.length);
                    } else {
                        lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, positiveSet[0].length(), negativeSet.length);
                    }
                }

                // TODO, create a usefull name for the reference set.
                String lReferenceID = "Static reference set";

                //create the datamodel
                OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);

                iAcceptor.removeAll();
                iAcceptor.removeAllSavables();
                iAcceptor.addComponent(lParent, "Home");

                // Add the logo
                if (iInfoFeeder.isUseIceLogo()) {
                    IceLogoComponent logo = new IceLogoComponent(dataModel, false);
                    iAcceptor.addComponent(logo, "iceLogo");
                    iAcceptor.addGraphable(logo);
                }

                //Add the barchart
                if (iInfoFeeder.isUseBarchart()) {
                    BarChartForm lSliding = new BarChartForm(dataModel);
                    iAcceptor.addComponent(lSliding.$$$getRootComponent$$$(), "Bar chart");
                    iAcceptor.addGraphable(lSliding);
                }

                // Add the HeatMap
                if (iInfoFeeder.isUseHeatmap()) {
                    HeatMapComponent lHeatmap = new HeatMapComponent(dataModel);
                    iAcceptor.addComponent(lHeatmap, "Heat map");
                    iAcceptor.addGraphable(lHeatmap);
                }

                // Add sublogo panel
                if (iInfoFeeder.isUseSubLogo()) {
                    SubLogoForm subLogo = new SubLogoForm(iSwissProtMeans, positiveSet, dataModel);
                    iAcceptor.addComponent(subLogo.getMainPanel(), "subLogo");
                    iAcceptor.addGraphable(subLogo);
                }

                // Add the sequence logo
                if (iInfoFeeder.isUseSequenceLogo()) {
                    SequenceLogoComponent lSequenceLogo = new SequenceLogoComponent(dataModel);
                    iAcceptor.addComponent(lSequenceLogo, "Sequence logo");
                    iAcceptor.addGraphable(lSequenceLogo);
                }

                // Add the AaParamterComponent
                if (iInfoFeeder.isUseAaParameterGraph()) {
                    AAIndexComponent lAaParam = new AAIndexComponent(dataModel);
                    iAcceptor.addComponent(lAaParam, "Aa Parameter");
                    iAcceptor.addGraphable(lAaParam);
                }

                // Add the conservation logo
                if (iInfoFeeder.isUseConservationLogo()) {
                    ConservationComponent lCons = new ConservationComponent(dataModel);
                    iAcceptor.addComponent(lCons, "Conservation line");
                    iAcceptor.addGraphable(lCons);
                }

                lParent.update(getGraphics());
                return error;
            }

            public void finished() {
                iMessenger.setProgressIndeterminate(false);
            }

        };
        iceLogoCreator.start();

    }

    /**
     * This method will make the different panels when two negative sets are used
     */
    private void makeLogoForTwoNegativeSets() {
        fillTheInformationFeeder();
        final Messenger iMessenger = Messenger.getInstance();
        final StaticLogoForm lParent = this;
        SwingWorker iceLogoCreator = new SwingWorker() {
            Boolean error = false;

            public Boolean construct() {
                //check negative set
                if (txtNeg1.getText().equalsIgnoreCase("") && !useSwissprotMeanCheckBox1.isSelected()) {
                    JOptionPane.showMessageDialog(lParent, new String[]{"No first negative set is available,\ngive a negatvie set or select a swissprot mean!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //check negative set
                if (txtNeg2.getText().equalsIgnoreCase("") && !useSwissprotMeanCheckBox2.isSelected()) {
                    JOptionPane.showMessageDialog(lParent, new String[]{"No second negative set is available,\ngive a negatvie set or select a swissprot mean!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //Get the sequences and create sequence set
                String positiveSequences = txtPos.getText();
                String[] positiveSet = positiveSequences.split("\n");
                //create a raw sequence set for the positive sequences
                RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
                for (int i = 0; i < positiveSet.length; i++) {
                    lRawPositiveSequenceSet.add(positiveSet[i]);
                    iInfoFeeder.addSequence(ExperimentTypeEnum.EXPERIMENT, positiveSet[i]);
                }
                AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, positiveSet[0].length(), SamplingDirectionEnum.NtermToCterm);

                AminoAcidStatistics[] lNegativeStatistics1;
                AminoAcidStatistics[] lNegativeStatistics2;

                if (useSwissprotMeanCheckBox1.isSelected()) {
                    if (swissMean1.getSelectedItem().toString().equalsIgnoreCase(SpeciesComboBoxModel.AddString)) {
                        JOptionPane.showMessageDialog(new JFrame(), new String[]{"No correct species is selected for the first negative set, \nselect a correct species!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    SwissProtComposition lSelectedSpComp1 = (SwissProtComposition) swissMean1.getSelectedItem();
                    if (lSelectedSpComp1 == null) {
                        JOptionPane.showMessageDialog(lParent, new String[]{"You selected swissprot mean as a negative set,\nbut you didn't select a specie!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    AminoAcidStatistics lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lSelectedSpComp1, positiveSet.length);
                    lNegativeStatistics1 = new AminoAcidStatistics[negativeSplit.getValue()];
                    for (int i = 0; i < negativeSplit.getValue(); i++) {
                        lNegativeStatistics1[i] = lNegative;
                    }
                } else {
                    if (!testNegativeSet(txtNeg1)) {
                        JOptionPane.showMessageDialog(lParent, new String[]{"Not all peptide in the first negative set have the same length"}, "Problem with first negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    //Get the sequences and create sequence set
                    String negativeSequences = txtNeg1.getText();
                    String[] negativeSet = negativeSequences.split("\n");
                    RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
                    for (int i = 0; i < negativeSet.length; i++) {
                        lRawNegativeSequenceSet.add(negativeSet[i]);
                    }
                    lNegativeStatistics1 = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, positiveSet[0].length(), positiveSet.length);

                }

                if (useSwissprotMeanCheckBox2.isSelected()) {
                    if (swissMean2.getSelectedItem().toString().equalsIgnoreCase(SpeciesComboBoxModel.AddString)) {
                        JOptionPane.showMessageDialog(new JFrame(), new String[]{"No correct species is selected in for the second negative set, \nselect a correct species!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    SwissProtComposition lSelectedSpComp2 = (SwissProtComposition) swissMean2.getSelectedItem();
                    if (lSelectedSpComp2 == null) {
                        JOptionPane.showMessageDialog(lParent, new String[]{"You selected swissprot mean as a negative set,\nbut you didn't select a specie!"}, "Problem with negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    AminoAcidStatistics lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lSelectedSpComp2, positiveSet.length);
                    lNegativeStatistics2 = new AminoAcidStatistics[positiveSet[0].length() - negativeSplit.getValue()];
                    for (int p = 0; p < positiveSet[0].length() - negativeSplit.getValue(); p++) {
                        lNegativeStatistics2[p] = lNegative;
                    }

                } else {
                    if (!testNegativeSet(txtNeg2)) {
                        JOptionPane.showMessageDialog(lParent, new String[]{"Not all peptide in the first negative set have the same length"}, "Problem with first negative set", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    //Get the sequences and create sequence set
                    String negativeSequences = txtNeg2.getText();
                    String[] negativeSet = negativeSequences.split("\n");
                    RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
                    for (int i = 0; i < negativeSet.length; i++) {
                        lRawNegativeSequenceSet.add(negativeSet[i]);
                    }
                    lNegativeStatistics2 = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, positiveSet[0].length(), positiveSet.length);
                }


                AminoAcidStatistics[] lNegativeStatistics = new AminoAcidStatistics[lNegativeStatistics1.length + lNegativeStatistics2.length];
                for (int i = 0; i < lNegativeStatistics1.length; i++) {
                    lNegativeStatistics[i] = lNegativeStatistics1[i];
                }

                for (int i = 0; i < lNegativeStatistics2.length; i++) {
                    lNegativeStatistics[i + lNegativeStatistics1.length] = lNegativeStatistics2[i];
                }
                // TODO, create a usefull name for the reference set.
                String lReferenceID = "Static reference set";
                OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);


                iAcceptor.removeAll();
                iAcceptor.removeAllSavables();
                iAcceptor.addComponent(lParent, "Home");


                // Add the logo
                if (iInfoFeeder.isUseIceLogo()) {
                    IceLogoComponent logo = new IceLogoComponent(dataModel, false);
                    iAcceptor.addComponent(logo, "iceLogo");
                    iAcceptor.addGraphable(logo);
                }

                //Add the barchart
                if (iInfoFeeder.isUseBarchart()) {
                    BarChartForm lSliding = new BarChartForm(dataModel);
                    iAcceptor.addComponent(lSliding.$$$getRootComponent$$$(), "Bar chart");
                    iAcceptor.addGraphable(lSliding);
                }

                // Add the HeatMap
                if (iInfoFeeder.isUseHeatmap()) {
                    HeatMapComponent lHeatmap = new HeatMapComponent(dataModel);
                    iAcceptor.addComponent(lHeatmap, "Heat map");
                    iAcceptor.addGraphable(lHeatmap);
                }

                // Add sublogo panel
                if (iInfoFeeder.isUseSubLogo()) {
                    SubLogoForm subLogo = new SubLogoForm(iSwissProtMeans, positiveSet, dataModel);
                    iAcceptor.addComponent(subLogo.getMainPanel(), "subLogo");
                    iAcceptor.addGraphable(subLogo);
                }

                // Add the sequence logo
                if (iInfoFeeder.isUseSequenceLogo()) {
                    SequenceLogoComponent lSequenceLogo = new SequenceLogoComponent(dataModel);
                    iAcceptor.addComponent(lSequenceLogo, "Sequence logo");
                    iAcceptor.addGraphable(lSequenceLogo);
                }

                // Add the AaParamterComponent
                if (iInfoFeeder.isUseAaParameterGraph()) {
                    AAIndexComponent lAaParam = new AAIndexComponent(dataModel);
                    iAcceptor.addComponent(lAaParam, "Aa Parameter");
                    iAcceptor.addGraphable(lAaParam);
                }

                // Add the conservation logo
                if (iInfoFeeder.isUseConservationLogo()) {
                    ConservationComponent lCons = new ConservationComponent(dataModel);
                    iAcceptor.addComponent(lCons, "Conservation line");
                    iAcceptor.addGraphable(lCons);
                }

                lParent.update(getGraphics());
                return error;
            }

            public void finished() {
                iMessenger.setProgressIndeterminate(false);
            }

        };
        iceLogoCreator.start();

    }

    /**
     * This method will test the positive set
     *
     * @return boolean True if everything is ok
     */
    private boolean testPositveSet() {
        boolean positiveSetOk = true;
        String positive = txtPos.getText();
        String[] posSet = positive.split("\n");
        int length = posSet[0].length();
        for (int i = 0; i < posSet.length; i++) {
            if (length != posSet[i].length()) {
                positiveSetOk = false;
            }
        }
        iPeptideExample = posSet[0];
        if (posSet[0].equalsIgnoreCase("") && posSet.length == 1) {
            iPeptideExample = "Give positive set";
        }
        return positiveSetOk;
    }

    /**
     * This method will test the negative set
     *
     * @param areaToTest JTextArea to find to negative set
     * @return boolean True if everything is ok
     */
    private boolean testNegativeSet(JTextArea areaToTest) {
        boolean setOk = true;
        String sequenceSet = areaToTest.getText();
        String[] set = sequenceSet.split("\n");
        int length = set[0].length();
        for (int i = 0; i < set.length; i++) {
            if (length != set[i].length()) {
                setOk = false;
            }
        }
        return setOk;
    }

    private void constructSpeciesCombobox() {
        cmbSwissprot.setModel(new SpeciesComboBoxModel());
        cmbSwissprot.setRenderer(new SpeciesListCellRenderer());

        swissMean1.setModel(new SpeciesComboBoxModel());
        swissMean1.setRenderer(new SpeciesListCellRenderer());

        swissMean2.setModel(new SpeciesComboBoxModel());
        swissMean2.setRenderer(new SpeciesListCellRenderer());
    }

    /**
     * This method will load the different Swissprot compositions. A link to the different properties files with the
     * compositions can be found in the species.properties file.
     */
    public void loadSwissprotCompositions() {
        iSwissProtMeans = iInfoFeeder.getSwissProtCompositions();
    }

    /**
     * This method is called whenever the observed object is changed. An application calls an <tt>Observable</tt>
     * object's <code>notifyObservers</code> method to have all the object's observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code> method.
     */
    public void update(final Observable o, final Object arg) {
        // If a combobox notification comes in, rebuild the combobox.
        if (arg != null && arg.equals(ObservableEnum.NOTIFY_SPECIES_COMBOBOX)) {
            constructSpeciesCombobox();
        }
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
        positivePanel = new JPanel();
        positivePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(positivePanel, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Positive set");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        positivePanel.add(label1, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        positivePanel.add(scrollPane1, gbc);
        txtPos = new JTextArea();
        txtPos.setFont(new Font("Courier New", txtPos.getFont().getStyle(), txtPos.getFont().getSize()));
        scrollPane1.setViewportView(txtPos);
        negativeTab = new JTabbedPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        positivePanel.add(negativeTab, gbc);
        oneNegativePanel = new JPanel();
        oneNegativePanel.setLayout(new GridBagLayout());
        negativeTab.addTab("One negative set", oneNegativePanel);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        oneNegativePanel.add(scrollPane2, gbc);
        txtNeg = new JTextArea();
        txtNeg.setFont(new Font("Courier New", txtNeg.getFont().getStyle(), txtNeg.getFont().getSize()));
        scrollPane2.setViewportView(txtNeg);
        useSwissprotMeansCheckBox = new JCheckBox();
        useSwissprotMeansCheckBox.setSelected(true);
        useSwissprotMeansCheckBox.setText("Use Swiss-Prot means");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        oneNegativePanel.add(useSwissprotMeansCheckBox, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(0);
        label2.setText("Negative set");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 8;
        gbc.insets = new Insets(5, 5, 5, 5);
        oneNegativePanel.add(label2, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        oneNegativePanel.add(cmbSwissprot, gbc);
        twoNegativePanel = new JPanel();
        twoNegativePanel.setLayout(new GridBagLayout());
        negativeTab.addTab("Two negative sets", twoNegativePanel);
        final JScrollPane scrollPane3 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoNegativePanel.add(scrollPane3, gbc);
        txtNeg1 = new JTextArea();
        txtNeg1.setColumns(0);
        txtNeg1.setFont(new Font("Courier New", txtNeg1.getFont().getStyle(), txtNeg1.getFont().getSize()));
        txtNeg1.setRows(0);
        txtNeg1.setText("");
        scrollPane3.setViewportView(txtNeg1);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoNegativePanel.add(swissMean1, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoNegativePanel.add(swissMean2, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(0);
        label3.setText("Negative set");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoNegativePanel.add(label3, gbc);
        splitLabel = new JLabel();
        splitLabel.setHorizontalAlignment(0);
        splitLabel.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        twoNegativePanel.add(splitLabel, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        twoNegativePanel.add(negativeSplit, gbc);
        negOneLabel = new JLabel();
        negOneLabel.setFont(new Font(negOneLabel.getFont().getName(), negOneLabel.getFont().getStyle(), 10));
        negOneLabel.setHorizontalAlignment(0);
        negOneLabel.setText("label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        twoNegativePanel.add(negOneLabel, gbc);
        negTwoLabel = new JLabel();
        negTwoLabel.setFont(new Font(negTwoLabel.getFont().getName(), negTwoLabel.getFont().getStyle(), 10));
        negTwoLabel.setHorizontalAlignment(0);
        negTwoLabel.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        twoNegativePanel.add(negTwoLabel, gbc);
        final JScrollPane scrollPane4 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoNegativePanel.add(scrollPane4, gbc);
        txtNeg2 = new JTextArea();
        txtNeg2.setColumns(0);
        txtNeg2.setFont(new Font("Courier New", txtNeg2.getFont().getStyle(), txtNeg2.getFont().getSize()));
        txtNeg2.setRows(0);
        scrollPane4.setViewportView(txtNeg2);
        useSwissprotMeanCheckBox1 = new JCheckBox();
        useSwissprotMeanCheckBox1.setText("Use Swiss-Prot mean");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoNegativePanel.add(useSwissprotMeanCheckBox1, gbc);
        useSwissprotMeanCheckBox2 = new JCheckBox();
        useSwissprotMeanCheckBox2.setText("Use Swiss-Prot mean");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        twoNegativePanel.add(useSwissprotMeanCheckBox2, gbc);
        makeLogoButton = new JButton();
        makeLogoButton.setText("Make logo");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(makeLogoButton, gbc);
        negOneLabel.setLabelFor(scrollPane3);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
