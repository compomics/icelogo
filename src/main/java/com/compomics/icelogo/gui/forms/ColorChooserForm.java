package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.ColorEnum;
import com.compomics.icelogo.core.enumeration.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 17-nov-2008
 * Time: 16:11:55
 * To change this template use File | Settings | File Templates.
 */
public class ColorChooserForm {
    private JPanel contentPane;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JComboBox comboBox9;
    private JComboBox comboBox10;
    private JComboBox comboBox11;
    private JComboBox comboBox12;
    private JComboBox comboBox13;
    private JComboBox comboBox14;
    private JComboBox comboBox15;
    private JComboBox comboBox16;
    private JComboBox comboBox17;
    private JComboBox comboBox18;
    private JComboBox comboBox19;
    private JComboBox comboBox20;
    private JComboBox comboBox21;

    public MainInformationFeeder iInformationFeeder;
    public ColorScheme iColorScheme = new ColorScheme();

    public ColorChooserForm() {
        $$$setupUI$$$();
        iInformationFeeder = MainInformationFeeder.getInstance();
        iColorScheme = iInformationFeeder.getColorScheme();
        comboBox2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox15.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox16.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox17.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox18.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox19.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox20.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
        comboBox21.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorScheme();
            }
        });
    }

    public void setColorScheme() {

        iColorScheme = new ColorScheme();
        iInformationFeeder.setColorScheme(iColorScheme);
    }

    private void createUIComponents() {

        //todo: change this to color comboboxes

        comboBox2 = new JComboBox();


        comboBox2.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox3 = new JComboBox();
        comboBox3.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox4 = new JComboBox();
        comboBox4.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox5 = new JComboBox();
        comboBox5.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox6 = new JComboBox();
        comboBox6.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox7 = new JComboBox();
        comboBox7.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox8 = new JComboBox();
        comboBox8.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox9 = new JComboBox();
        comboBox9.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox10 = new JComboBox();
        comboBox10.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox11 = new JComboBox();
        comboBox11.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox12 = new JComboBox();
        comboBox12.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox13 = new JComboBox();
        comboBox13.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox14 = new JComboBox();
        comboBox14.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox15 = new JComboBox();
        comboBox15.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox16 = new JComboBox();
        comboBox16.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox17 = new JComboBox();
        comboBox17.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox18 = new JComboBox();
        comboBox18.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox19 = new JComboBox();
        comboBox19.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox20 = new JComboBox();
        comboBox20.setModel(new DefaultComboBoxModel(ColorEnum.values()));
        comboBox21 = new JComboBox();
        comboBox21.setModel(new DefaultComboBoxModel(ColorEnum.values()));

        comboBox2.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.ALA));
        comboBox3.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.CYS));
        comboBox4.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.ASP));
        comboBox5.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.GLU));
        comboBox6.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.PHE));
        comboBox7.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.GLY));
        comboBox8.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.HIS));
        comboBox9.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.ILE));
        comboBox10.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.LYS));
        comboBox11.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.LEU));
        comboBox12.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.MET));
        comboBox13.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.ASN));
        comboBox14.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.PRO));
        comboBox15.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.GLN));
        comboBox16.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.ARG));
        comboBox17.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.SER));
        comboBox18.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.THR));
        comboBox19.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.VAL));
        comboBox20.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.TRP));
        comboBox21.setSelectedItem(iColorScheme.getAminoAcidColor(AminoAcidEnum.TYR));
    }

    public JPanel getContentPane() {
        return contentPane;
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel1.add(contentPane, gbc);
        contentPane.setBorder(BorderFactory.createTitledBorder("Color"));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setText("A");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        contentPane.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(0);
        label2.setText("C");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        contentPane.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(0);
        label3.setText("D");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        contentPane.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(0);
        label4.setText("E");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        contentPane.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(0);
        label5.setText("F");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        contentPane.add(label5, gbc);
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(0);
        label6.setText("G");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        contentPane.add(label6, gbc);
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(0);
        label7.setText("H");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        contentPane.add(label7, gbc);
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(0);
        label8.setText("I");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        contentPane.add(label8, gbc);
        final JLabel label9 = new JLabel();
        label9.setHorizontalAlignment(0);
        label9.setText("K");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        contentPane.add(label9, gbc);
        final JLabel label10 = new JLabel();
        label10.setHorizontalAlignment(0);
        label10.setText("L");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weighty = 1.0;
        contentPane.add(label10, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox2, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox3, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox4, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox5, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox6, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox7, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox8, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox9, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox10, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox11, gbc);
        final JLabel label11 = new JLabel();
        label11.setHorizontalAlignment(0);
        label11.setText("M");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        contentPane.add(label11, gbc);
        final JLabel label12 = new JLabel();
        label12.setHorizontalAlignment(0);
        label12.setText("N");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        contentPane.add(label12, gbc);
        final JLabel label13 = new JLabel();
        label13.setHorizontalAlignment(0);
        label13.setText("P");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        contentPane.add(label13, gbc);
        final JLabel label14 = new JLabel();
        label14.setHorizontalAlignment(0);
        label14.setText("Q");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        contentPane.add(label14, gbc);
        final JLabel label15 = new JLabel();
        label15.setHorizontalAlignment(0);
        label15.setText("R");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        contentPane.add(label15, gbc);
        final JLabel label16 = new JLabel();
        label16.setHorizontalAlignment(0);
        label16.setText("S");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        contentPane.add(label16, gbc);
        final JLabel label17 = new JLabel();
        label17.setHorizontalAlignment(0);
        label17.setText("T");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        contentPane.add(label17, gbc);
        final JLabel label18 = new JLabel();
        label18.setHorizontalAlignment(0);
        label18.setText("V");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        contentPane.add(label18, gbc);
        final JLabel label19 = new JLabel();
        label19.setHorizontalAlignment(0);
        label19.setText("W");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        contentPane.add(label19, gbc);
        final JLabel label20 = new JLabel();
        label20.setHorizontalAlignment(0);
        label20.setText("Y");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.weighty = 1.0;
        contentPane.add(label20, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox12, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox13, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox14, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox15, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox16, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox17, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox18, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox19, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox20, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(comboBox21, gbc);
    }
}
