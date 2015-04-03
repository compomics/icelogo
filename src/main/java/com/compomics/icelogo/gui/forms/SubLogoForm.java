package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.RegulatedEntity;
import com.compomics.icelogo.core.data.RegulatedPosition;
import com.compomics.icelogo.core.data.sequenceset.RawSequenceSet;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.SamplingDirectionEnum;
import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;
import com.compomics.icelogo.core.factory.AminoAcidStatisticsFactory;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.core.io.MatrixDataModelSaver;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import com.compomics.icelogo.gui.graph.IceLogoComponent;
import com.compomics.icelogo.gui.interfaces.Savable;
import com.compomics.icelogo.gui.interfaces.GraphableAcceptor;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Niklaas Colaert Date: 10-okt-2008 Time: 19:02:43 To change this template use File |
 * Settings | File Templates.
 */
public class SubLogoForm implements Observer, Savable, GraphableAcceptor {
    private JList list1;
    private JCheckBox useWholeSetAsCheckBox;
    private JCheckBox useSwissprotMeansCheckBox;
    private JComboBox comboBoxSwissProt;
    private JTextArea txtPosSelection;
    private JPanel jpanMain;
    private JPanel jTabPanelSubLogo;
    private JTabbedPane tabPane;
    private JPanel saveParameterPane;
    private JButton saveButton;
    private JCheckBox chbSignificant;
    private JCheckBox chbAll;
    private JSlider positionSlider;
    private JComboBox cmbAminoAcids;
    private JPanel selecteePeptidesPanel;

    private Vector<SwissProtComposition> iSwissProtComp;
    private String[] iPositiveSet;
    private RegulatedPosition[] iRegulatedPositions;
    private MatrixDataModel iDataModel;
    private double iZscore;
    private ScoringTypeEnum iScoringType;
    private int iNumberOfElements;
    private Vector iSubLogoElements = new Vector();
    private int iStartPosition;
    private IceLogoComponent logoPanel;
    private MainInformationFeeder iInformationFeeder;
    private Vector<Savable> iSavableElements = new Vector<Savable>();


    public SubLogoForm(Vector<SwissProtComposition> aSwissProtComposition, String[] aPosSet, MatrixDataModel aDataModel) {
        this.iInformationFeeder = MainInformationFeeder.getInstance();
        iInformationFeeder.addObserver(this);

        this.iSwissProtComp = aSwissProtComposition;
        this.iPositiveSet = aPosSet;
        this.iDataModel = aDataModel;
        this.iZscore = iInformationFeeder.getZscore();
        this.iScoringType = iInformationFeeder.getScoringType();
        this.iNumberOfElements = iDataModel.getNumberOfPositions();
        this.iStartPosition = iInformationFeeder.getStartPosition();


        $$$setupUI$$$();
        this.setRegulatedPosition();
        this.useWholeSetAsCheckBox.setSelected(true);
        positionSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                createPositionLogo();
            }
        });

        cmbAminoAcids.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    createPositionLogo();
                }
            }
        });
        chbAll.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    createPositionLogo();
                }
            }
        });

        chbSignificant.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    new listSelectionHandler();
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFrame saveFrame = new JFrame("Save");
                //create JFrame parameters
                GraphableSaverForm lSave = new GraphableSaverForm(iSavableElements);
                saveFrame.setContentPane(lSave.getContentPane());
                saveFrame.setSize(600, 300);
                saveFrame.setLocation(100, 100);
                saveFrame.setVisible(true);
            }
        });
    }

    public void setRegulatedPosition() {
        iSubLogoElements.removeAllElements();
        iRegulatedPositions = iDataModel.getRegulatedPositions(iZscore);
        for (int r = 0; r < iRegulatedPositions.length; r++) {
            RegulatedEntity[] posReg = iRegulatedPositions[r].getPositiveRegulatedEntity(iScoringType);
            for (int e = 0; e < posReg.length; e++) {
                char aa = posReg[e].getAminoAcid();
                Vector posSelection = new Vector();
                for (int s = 0; s < iPositiveSet.length; s++) {
                    if (iPositiveSet[s].charAt(r) == aa) {
                        posSelection.add(iPositiveSet[s]);
                    }
                }
                String[] lPosSelection = new String[posSelection.size()];
                posSelection.toArray(lPosSelection);
                iSubLogoElements.add(new SubLogoElement(String.valueOf(aa) + " at position " + (r + iStartPosition) + " (found " + posSelection.size() + ")", lPosSelection));
            }
        }
        list1.setListData(iSubLogoElements);
        list1.updateUI();
    }

    public JPanel getMainPanel() {
        return jpanMain;
    }


    public void setSliderBounds() {
        int min = iStartPosition;
        int max = iStartPosition + iNumberOfElements;
        // int init = positionSlider.getValue() + iStartPosition;
        positionSlider.setMinimum(min);
        positionSlider.setMaximum(max);
        //positionSlider.setValue(init);
        positionSlider.setMajorTickSpacing(5);
        positionSlider.setMinorTickSpacing(1);
        positionSlider.setPaintTicks(true);
        positionSlider.setPaintLabels(true);
        positionSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    }

    private void createUIComponents() {
        list1 = new JList(iSubLogoElements);

        ListSelectionModel listSelectionModel = list1.getSelectionModel();
        listSelectionModel.addListSelectionListener(new listSelectionHandler());

        comboBoxSwissProt = new JComboBox(iSwissProtComp);

        cmbAminoAcids = new JComboBox();
        cmbAminoAcids.setModel(new DefaultComboBoxModel(AminoAcidEnum.values()));

        int min = iStartPosition;
        int max = iStartPosition + iNumberOfElements - 1;
        int init = (max + 1) / 2;
        positionSlider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        positionSlider.setMajorTickSpacing(5);
        positionSlider.setMinorTickSpacing(1);
        positionSlider.setPaintTicks(true);
        positionSlider.setPaintLabels(true);
        positionSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    }


    public SubLogoForm getSubLogoPanel() {
        return this;
    }


    public void createPositionLogo() {
        chbAll.setSelected(true);
        iInformationFeeder.setSubLogoHeight(jTabPanelSubLogo.getHeight());
        iInformationFeeder.setSubLogoWidth(jTabPanelSubLogo.getWidth());
        int position = positionSlider.getValue() - iStartPosition;
        AminoAcidEnum lAminoAcid = (AminoAcidEnum) cmbAminoAcids.getSelectedItem();

        Vector posSelection = new Vector();
        for (int s = 0; s < iPositiveSet.length; s++) {
            if (iPositiveSet[s].charAt(position) == lAminoAcid.getOneLetterCode()) {
                posSelection.add(iPositiveSet[s]);
            }
        }
        String[] lPosSelection = new String[posSelection.size()];
        posSelection.toArray(lPosSelection);
        SubLogoElement element = new SubLogoElement(String.valueOf(lAminoAcid.getOneLetterCode()) + " at position " + (position + iStartPosition) + " (found " + posSelection.size() + ")", lPosSelection);
        txtPosSelection.setText("");
        txtPosSelection.append(element + "\n");
        if (element.getPos().length == 0) {
            tabPane.removeAll();
            return;
        }

        //Get the sequences and create sequence set
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < element.getPos().length; i++) {
            lRawPositiveSequenceSet.add(element.getPos()[i]);
            txtPosSelection.append(element.getPos()[i] + "\n");
        }
        AminoAcidStatistics[] lExperimentalStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, element.getPos()[0].length(), SamplingDirectionEnum.NtermToCterm);

        AminoAcidStatistics[] lReferenceStatistics;
        if (useSwissprotMeansCheckBox.isSelected()) {
            SwissProtComposition swComp = (SwissProtComposition) comboBoxSwissProt.getSelectedItem();
            AminoAcidStatistics lNegative =
                    AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(swComp, element.getPos().length);
            lReferenceStatistics = new AminoAcidStatistics[element.getPos()[0].length()];
            for (int i = 0; i < element.getPos()[0].length(); i++) {
                lReferenceStatistics[i] = lNegative;
            }
        } else {
            //Get the sequences and create sequence set
            RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
            for (int i = 0; i < iPositiveSet.length; i++) {
                lRawNegativeSequenceSet.add(iPositiveSet[i]);
            }
            lReferenceStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, iPositiveSet[0].length(), element.getPos().length);
        }

        // TODO, create a usefull name for the reference set.
        String lReferenceID = "Static reference set";

        //create the datamodel
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lReferenceStatistics, lExperimentalStatistics, lReferenceID);

        //create the logo
        logoPanel = new IceLogoComponent(dataModel, true);
        //now addComponent the logo to the panel

        //first remove everything
        removeAll();
        removeAllSavables();
        //add logo
        addComponent(logoPanel, "iceLogo");
        addSavable(logoPanel);

        // Add the saver for the datamodel.
        MatrixDataModelSaver lMatrixDataModelSaver = new MatrixDataModelSaver(dataModel);
        addSavable(lMatrixDataModelSaver);

        //add the barchart
        BarChartForm lSliding = new BarChartForm(dataModel);
        addComponent(lSliding.$$$getRootComponent$$$(), "Bar chart");
        addSavable(lSliding);
        //add selected peptides panel
        addComponent(selecteePeptidesPanel, "Selected peptides");
    }

    public void update(Observable o, Object arg) {
        if (iZscore != iInformationFeeder.getZscore()) {
            iZscore = iInformationFeeder.getZscore();
            this.iScoringType = iInformationFeeder.getScoringType();
            this.iStartPosition = iInformationFeeder.getStartPosition();
            this.setRegulatedPosition();
        } else {
            this.iScoringType = iInformationFeeder.getScoringType();
            if (iStartPosition != iInformationFeeder.getStartPosition()) {
                this.iStartPosition = iInformationFeeder.getStartPosition();
                setSlider();
            }
        }
    }

    public void setSlider() {

        int min = iStartPosition;
        int max = iStartPosition + iNumberOfElements - 1;
        int init = (max + 1) / 2;
        positionSlider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        positionSlider.setMajorTickSpacing(5);
        positionSlider.setMinorTickSpacing(1);
        positionSlider.setPaintTicks(true);
        positionSlider.setPaintLabels(true);
        positionSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        positionSlider.updateUI();
    }

    public boolean isSvg() {
        return true;
    }

    public boolean isChart() {
        return false;
    }

    public JPanel getContentPanel() {
        return null;
    }

    public SVGDocument getSVG() {
        return logoPanel.getSVG();
    }

    public String getTitle() {
        return "subLogo";
    }

    public String getDescription() {
        return "subLogo ";
    }

    /**
     * Gives a boolean that indicates if the saveble is text.
     *
     * @return
     */
    public boolean isText() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void addComponent(final JComponent comp, final String aTitle) {
        tabPane.add(aTitle, comp);
    }

    public void addSavable(Savable aSavable) {
        iSavableElements.add(aSavable);
    }

    public void removeAllSavables() {
        iSavableElements.removeAllElements();
    }

    public void removeAll() {
        tabPane.removeAll();
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
        jpanMain = new JPanel();
        jpanMain.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanMain.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel2.add(panel3, BorderLayout.SOUTH);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        panel2.add(panel4, BorderLayout.NORTH);
        panel4.setBorder(BorderFactory.createTitledBorder("Negative set"));
        useWholeSetAsCheckBox = new JCheckBox();
        useWholeSetAsCheckBox.setText("Use whole set as negative set");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(useWholeSetAsCheckBox, gbc);
        useSwissprotMeansCheckBox = new JCheckBox();
        useSwissprotMeansCheckBox.setText("Use Swiss-Prot means");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(useSwissprotMeansCheckBox, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel4.add(comboBoxSwissProt, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        panel2.add(panel5, BorderLayout.CENTER);
        panel5.setBorder(BorderFactory.createTitledBorder("Positive set"));
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel5.add(scrollPane1, gbc);
        scrollPane1.setViewportView(list1);
        chbSignificant = new JCheckBox();
        chbSignificant.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(chbSignificant, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(positionSlider, gbc);
        chbAll = new JCheckBox();
        chbAll.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(chbAll, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(cmbAminoAcids, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Amino acid: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel5.add(label1, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel6, gbc);
        saveParameterPane = new JPanel();
        saveParameterPane.setLayout(new GridBagLayout());
        panel6.add(saveParameterPane, BorderLayout.CENTER);
        saveButton = new JButton();
        saveButton.setText(" Save subLogo");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        saveParameterPane.add(saveButton, gbc);
        jTabPanelSubLogo = new JPanel();
        jTabPanelSubLogo.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 10.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanMain.add(jTabPanelSubLogo, gbc);
        tabPane = new JTabbedPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 5);
        jTabPanelSubLogo.add(tabPane, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        tabPane.addTab("no logo", panel7);
        final JLabel label2 = new JLabel();
        label2.setText("no logo");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(label2, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel7.add(spacer2, gbc);
        selecteePeptidesPanel = new JPanel();
        selecteePeptidesPanel.setLayout(new GridBagLayout());
        tabPane.addTab("Selected peptides", selecteePeptidesPanel);
        selecteePeptidesPanel.setBorder(BorderFactory.createTitledBorder("Selected peptides"));
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        selecteePeptidesPanel.add(scrollPane2, gbc);
        txtPosSelection = new JTextArea();
        txtPosSelection.setFont(new Font("Courier New", txtPosSelection.getFont().getStyle(), txtPosSelection.getFont().getSize()));
        scrollPane2.setViewportView(txtPosSelection);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(useWholeSetAsCheckBox);
        buttonGroup.add(useSwissprotMeansCheckBox);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(chbSignificant);
        buttonGroup.add(chbAll);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanMain;
    }

    class listSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            chbSignificant.setSelected(true);
            iInformationFeeder.setSubLogoHeight(jTabPanelSubLogo.getHeight());
            iInformationFeeder.setSubLogoWidth(jTabPanelSubLogo.getWidth());
            SubLogoElement element = (SubLogoElement) list1.getSelectedValue();
            txtPosSelection.setText("");
            txtPosSelection.append(element + "\n");

            //Get the sequences and create sequence set
            RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
            for (int i = 0; i < element.getPos().length; i++) {
                lRawPositiveSequenceSet.add(element.getPos()[i]);
                txtPosSelection.append(element.getPos()[i] + "\n");
            }
            AminoAcidStatistics[] lPositiveStatistics =
                    AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, element.getPos()[0].length(), SamplingDirectionEnum.NtermToCterm);

            AminoAcidStatistics[] lNegativeStatistics;
            if (useSwissprotMeansCheckBox.isSelected()) {
                SwissProtComposition swComp = (SwissProtComposition) comboBoxSwissProt.getSelectedItem();
                AminoAcidStatistics lNegative =
                        AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(swComp, element.getPos().length);
                lNegativeStatistics = new AminoAcidStatistics[element.getPos()[0].length()];
                for (int i = 0; i < element.getPos()[0].length(); i++) {
                    lNegativeStatistics[i] = lNegative;
                }
            } else {
                //Get the sequences and create sequence set
                RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
                for (int i = 0; i < iPositiveSet.length; i++) {
                    lRawNegativeSequenceSet.add(iPositiveSet[i]);
                }
                lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, iPositiveSet[0].length(), element.getPos().length);

            }

            // TODO, create a usefull name for the reference set.
            String lReferenceID = "Static reference set";

            //create the datamodel

            OneSampleMatrixDataModel dataModel =
                    new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);

            //create the logo
            logoPanel = new IceLogoComponent(dataModel, true);
            //now addComponent the logo to the panel

            //first remove everything
            removeAll();
            removeAllSavables();

            // Add the saver for the datamodel.
            MatrixDataModelSaver lMatrixDataModelSaver = new MatrixDataModelSaver(dataModel);
            addSavable(lMatrixDataModelSaver);

            //add logo
            addComponent(logoPanel, "iceLogo");
            addSavable(logoPanel);

            //add the barchart
            BarChartForm lSliding = new BarChartForm(dataModel);
            addComponent(lSliding.$$$getRootComponent$$$(), "Bar chart");
            addSavable(lSliding);

            //add selected peptides panel
            addComponent(selecteePeptidesPanel, "Selected peptides");

        }
    }

    /**
     * Created by IntelliJ IDEA.
     * User: Niklaas Colaert
     * Date: 7-okt-2008
     * Time: 13:13:12
     * <p/>
     * This is an object that holds the positive set and a title for this positive set. SubLogoElemnts are made and used
     * in a SubLogoPanel
     */
    public class SubLogoElement {
        public String iTitle;
        public String[] iPos;

        public SubLogoElement(String iTitle, String[] iPos) {
            this.iTitle = iTitle;
            this.iPos = iPos;
        }


        public String getITitle() {
            return iTitle;
        }

        public String[] getPos() {
            return iPos;
        }

        public String toString() {
            return iTitle;
        }
    }
}

