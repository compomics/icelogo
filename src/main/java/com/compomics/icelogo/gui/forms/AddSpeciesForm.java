package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.dbComposition.CompositionExtractor;
import com.compomics.icelogo.core.dbComposition.SpeciesListReader;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 02-Jun-2009
 * Time: 10:10:15
 */
public class AddSpeciesForm extends JFrame {
    private JPanel jpanContent;
    private JTextField textField1;
    private JList allSpecies;
    private JList selectedSpecies;
    private JButton button2;
    private JButton button3;
    private JButton addButton;

    /**
     * Vector with the possible compositions
     */
    private Vector<SwissProtComposition> iAllSpecies;
    /**
     * Vector with the possible searched compositions
     */
    private Vector<SwissProtComposition> iSpeciesList = new Vector<SwissProtComposition>();
    /**
     * Vector with the selected compositions
     */
    private Vector<SwissProtComposition> iSelectedSpeciesList = new Vector<SwissProtComposition>();

    private MainInformationFeeder iFeeder = MainInformationFeeder.getInstance();

    public AddSpeciesForm() {
        //set the title
        super("Add a species");


        try {
            //get the possible compositions
            SpeciesListReader lListReader = new SpeciesListReader();
            iAllSpecies = lListReader.getComp();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, new String[]{"Error in reading the species file!"}, "Warning!", JOptionPane.WARNING_MESSAGE);
            this.dispose();
        }
        $$$setupUI$$$();


        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addComposition();
            }
        });
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteComposition();
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    store();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(new JFrame(), new String[]{"Error in writting the species files!"}, "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        textField1.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyPressed(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyReleased(KeyEvent e) {
                if (textField1.getText().length() > 3) {
                    iSpeciesList.removeAllElements();
                    for (int i = 0; i < iAllSpecies.size(); i++) {
                        if (iAllSpecies.get(i).getSpecieName().toLowerCase().indexOf(textField1.getText().toLowerCase()) >= 0) {
                            iSpeciesList.add(iAllSpecies.get(i));
                        }
                    }
                    allSpecies.setSelectedIndex(0);
                    allSpecies.updateUI();
                }
            }
        });

        //set JFrame parameters
        this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("icons/adder.png")).getImage());
        this.setContentPane(jpanContent);
        this.setSize(700, 500);
        this.setLocation(100, 100);
        this.setVisible(true);

    }

    private void createUIComponents() {
        allSpecies = new JList(iSpeciesList);
        allSpecies.setMinimumSize(new Dimension(150, 300));
        allSpecies.setMaximumSize(new Dimension(150, 300));
        selectedSpecies = new JList(iSelectedSpeciesList);
        selectedSpecies.setMinimumSize(new Dimension(150, 300));
        selectedSpecies.setMaximumSize(new Dimension(150, 300));
    }

    /**
     * This method will add one or more selected compositions in the selected compostions list and vector
     */
    public void addComposition() {
        if (iSpeciesList.size() == 0) {
            JOptionPane.showMessageDialog(this, new String[]{"The available species list is empty"}, "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (allSpecies.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(this, new String[]{"Nothing is selected"}, "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object[] selectedObjects = allSpecies.getSelectedValues();

        for (int j = 0; j < selectedObjects.length; j++) {
            SwissProtComposition selection = (SwissProtComposition) selectedObjects[j];

            Boolean found = false;
            int items = iSelectedSpeciesList.size();
            for (int i = 0; i < items; i++) {
                if (selection == iSelectedSpeciesList.get(i)) {
                    found = true;
                }
            }
            if (found) {
                JOptionPane.showMessageDialog(this, new String[]{selection.toString(), " is already a selected"}, "Warning!", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                iSelectedSpeciesList.add(selection);
                selectedSpecies.setSelectedIndex(0);
                selectedSpecies.updateUI();
                iSpeciesList.remove(selection);
                allSpecies.setSelectedIndex(0);
                allSpecies.updateUI();
            }
        }
    }

    /**
     * This method will delete one or more selected compositions
     */
    public void deleteComposition() {
        if (iSelectedSpeciesList.size() == 0) {
            JOptionPane.showMessageDialog(this, new String[]{"The selected groupusers list is empty"}, "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedSpecies.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(this, new String[]{"Nothing is selected"}, "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object[] selectedObjects = selectedSpecies.getSelectedValues();
        for (int j = 0; j < selectedObjects.length; j++) {
            SwissProtComposition selection = (SwissProtComposition) selectedObjects[j];
            Boolean found = false;
            int items = iSpeciesList.size();
            for (int i = 0; i < items; i++) {
                if (selection == iSpeciesList.get(i)) {
                    found = true;
                }
            }
            if (found) {
                JOptionPane.showMessageDialog(this, new String[]{selection.toString(), " is already not selected"}, "Warning!", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                iSpeciesList.add(selection);
                allSpecies.setSelectedIndex(0);
                allSpecies.updateUI();
                iSelectedSpeciesList.remove(selection);
                selectedSpecies.setSelectedIndex(0);
                selectedSpecies.updateUI();
            }
        }
    }


    public void store() throws IOException {
        ArrayList<SwissProtComposition> lToAdd = new ArrayList<SwissProtComposition>();
        for (int i = 0; i < iSelectedSpeciesList.size(); i++) {
            lToAdd.add(iSelectedSpeciesList.get(i));
        }
        CompositionExtractor lEx = new CompositionExtractor(lToAdd, MainInformationFeeder.getInstance());
        lEx.addToMainInformationFeeder();
        JOptionPane.showMessageDialog(this, "Adding species done.", "Info!", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
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
        textField1 = new JTextField();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(textField1, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(scrollPane1, gbc);
        scrollPane1.setViewportView(allSpecies);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(scrollPane2, gbc);
        scrollPane2.setViewportView(selectedSpecies);
        button3 = new JButton();
        button3.setText("<<<");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(button3, gbc);
        button2 = new JButton();
        button2.setText(">>>");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(button2, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanContent.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanContent.add(spacer2, gbc);
        addButton = new JButton();
        addButton.setText("Add");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(addButton, gbc);
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.ITALIC, label1.getFont().getSize()));
        label1.setText("Search species: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 25, 5, 5);
        jpanContent.add(label1, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
