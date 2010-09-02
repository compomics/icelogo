package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.enumeration.IceLogoEnum;
import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;
import com.compomics.icelogo.core.aaindex.AAIndexMatrix;
import com.compomics.icelogo.core.aaindex.AAIndexSubstitutionMatrix;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 26-feb-2009
 * Time: 8:49:10
 * To change this template use File | Settings | File Templates.
 */
public class IceLogoOptionForm implements Observer {
    private JPanel jpanContent;
    private JPanel parameterPane;
    private JLabel lblxAxis;
    private JLabel lblStartPosition;
    private JRadioButton differencePercentageRadioButton;
    private JRadioButton foldChangeRadioButton;
    private JLabel lblChooseScoreing;
    private JSpinner spinnerStart;
    private JSpinner spinnerAxis;
    private JRadioButton standardDeviationRadioButton;
    private JPanel SequenceLogoParameters;
    private JCheckBox fillCheckBox;
    private JCheckBox useNegetaveSetForCheckBox;
    private JPanel conservationPanel;
    private JList listMatrix;
    private JCheckBox chbNormalization;
    private Vector<AAIndexMatrix> iSubstitutionMatrices;

    private MainInformationFeeder iInfoFeeder;

    public IceLogoOptionForm() {
        iInfoFeeder = MainInformationFeeder.getInstance();

        $$$setupUI$$$();
        iInfoFeeder.addObserver(this);
        chbNormalization.setSelected(iInfoFeeder.isConservationLineNormalized());
        if (iInfoFeeder.getIceLogoType() == IceLogoEnum.STATIC) {
            spinnerStart.setEnabled(true);
        } else {
            spinnerStart.setEnabled(false);
        }
        spinnerStart.setValue(iInfoFeeder.getStartPosition());
        spinnerAxis.setValue(iInfoFeeder.getYaxisValue());
        if (iInfoFeeder.getScoringType() == ScoringTypeEnum.FOLDCHANGE) {
            foldChangeRadioButton.setSelected(true);
        } else if (iInfoFeeder.getScoringType() == ScoringTypeEnum.STANDARD_DEVIATION) {
            standardDeviationRadioButton.setSelected(true);
        } else if (iInfoFeeder.getScoringType() == ScoringTypeEnum.PERCENTAGE) {
            differencePercentageRadioButton.setSelected(true);
        }
        if (iInfoFeeder.isFillWeblogo()) {
            fillCheckBox.setSelected(true);
        }
        if (iInfoFeeder.isWeblogoNegativeCorrection()) {
            useNegetaveSetForCheckBox.setSelected(true);
        }
        fillCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iInfoFeeder.setFillWeblogo(fillCheckBox.isSelected());
            }
        });
        useNegetaveSetForCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iInfoFeeder.setWeblogoNegativeCorrection(useNegetaveSetForCheckBox.isSelected());
            }
        });
        foldChangeRadioButton.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (foldChangeRadioButton.isSelected()) {
                    iInfoFeeder.setScoringType(ScoringTypeEnum.FOLDCHANGE);
                }
            }
        });
        differencePercentageRadioButton.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (differencePercentageRadioButton.isSelected()) {
                    iInfoFeeder.setScoringType(ScoringTypeEnum.PERCENTAGE);
                }
            }
        });
        standardDeviationRadioButton.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (standardDeviationRadioButton.isSelected()) {
                    iInfoFeeder.setScoringType(ScoringTypeEnum.STANDARD_DEVIATION);
                }
            }
        });
        spinnerStart.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iInfoFeeder.setStartPosition((Integer) spinnerStart.getValue());
            }
        });
        spinnerAxis.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iInfoFeeder.setYaxisValue((Integer) spinnerAxis.getValue());
            }
        });

        chbNormalization.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setConservationLineNormalized(chbNormalization.isSelected());
            }
        });
    }

    public JPanel getContentPane() {
        return this.jpanContent;
    }

    private void createUIComponents() {
        iSubstitutionMatrices = iInfoFeeder.getSubstitutionMatrixes();
        listMatrix = new JList(iSubstitutionMatrices);
        ListSelectionModel listSelectionModel = listMatrix.getSelectionModel();
        listSelectionModel.addListSelectionListener(new listSelectionHandler());

    }

    public void update(Observable o, Object arg) {
        if (Double.valueOf(spinnerAxis.getValue().toString()) != iInfoFeeder.getYaxisValue()) {
            spinnerAxis.setValue(iInfoFeeder.getYaxisValue());
        }
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
        parameterPane = new JPanel();
        parameterPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 5, 0, 5);
        jpanContent.add(parameterPane, gbc);
        parameterPane.setBorder(BorderFactory.createTitledBorder("iceLogo parameters"));
        lblxAxis = new JLabel();
        lblxAxis.setText("Set y-axis dimension:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        parameterPane.add(lblxAxis, gbc);
        lblStartPosition = new JLabel();
        lblStartPosition.setText("Set start position:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        parameterPane.add(lblStartPosition, gbc);
        differencePercentageRadioButton = new JRadioButton();
        differencePercentageRadioButton.setText("% difference");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        parameterPane.add(differencePercentageRadioButton, gbc);
        foldChangeRadioButton = new JRadioButton();
        foldChangeRadioButton.setText("Fold change");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        parameterPane.add(foldChangeRadioButton, gbc);
        lblChooseScoreing = new JLabel();
        lblChooseScoreing.setText("Choose scoring system:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        parameterPane.add(lblChooseScoreing, gbc);
        spinnerStart = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parameterPane.add(spinnerStart, gbc);
        spinnerAxis = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parameterPane.add(spinnerAxis, gbc);
        standardDeviationRadioButton = new JRadioButton();
        standardDeviationRadioButton.setText("Standard deviation");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        parameterPane.add(standardDeviationRadioButton, gbc);
        SequenceLogoParameters = new JPanel();
        SequenceLogoParameters.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(SequenceLogoParameters, gbc);
        SequenceLogoParameters.setBorder(BorderFactory.createTitledBorder("Sequence logo parameters"));
        fillCheckBox = new JCheckBox();
        fillCheckBox.setText("Fill?");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        SequenceLogoParameters.add(fillCheckBox, gbc);
        useNegetaveSetForCheckBox = new JCheckBox();
        useNegetaveSetForCheckBox.setText("Use negetave set for correction");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        SequenceLogoParameters.add(useNegetaveSetForCheckBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SequenceLogoParameters.add(spacer1, gbc);
        conservationPanel = new JPanel();
        conservationPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(conservationPanel, gbc);
        conservationPanel.setBorder(BorderFactory.createTitledBorder("Conservation line parameters"));
        final JLabel label1 = new JLabel();
        label1.setText("Sustitution matrix");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        conservationPanel.add(label1, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        conservationPanel.add(scrollPane1, gbc);
        scrollPane1.setViewportView(listMatrix);
        final JLabel label2 = new JLabel();
        label2.setText("Normalized conservation line");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        conservationPanel.add(label2, gbc);
        chbNormalization = new JCheckBox();
        chbNormalization.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        conservationPanel.add(chbNormalization, gbc);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(differencePercentageRadioButton);
        buttonGroup.add(foldChangeRadioButton);
        buttonGroup.add(standardDeviationRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }


    private class listSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            AAIndexSubstitutionMatrix lMatrix = (AAIndexSubstitutionMatrix) listMatrix.getSelectedValue();
            iInfoFeeder.setSubstitutionMatrix(lMatrix);
        }
    }

}
