package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.dbComposition.FastaDatabaseDownloader;
import com.compomics.icelogo.core.dbComposition.SpeciesListReader;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;
import com.compomics.util.sun.SwingWorker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: niklaas Date: 22-Jun-2009 Time: 13:20:57
 */
public class FastaDownloaderForm extends JFrame {
    private JPanel jpanContent;
    private JList allSpecies;
    private JTextField txtFieldSearch;
    private JLabel lblSelectedSpecies;
    private JButton saveButton;
    private JButton exitButton;
    private JProgressBar progressBar1;

    /**
     * All the species
     */
    private Vector<SwissProtComposition> iAllSpecies;
    /**
     * Vector with the possible searched compositions
     */
    private Vector<SwissProtComposition> iSpeciesList = new Vector<SwissProtComposition>();
    private SwissProtComposition iSelectedSpecies;

    public FastaDownloaderForm() {

        //get all the species
        $$$setupUI$$$();
        try {
            //get the possible compositions
            SpeciesListReader lListReader = new SpeciesListReader();
            iAllSpecies = lListReader.getComp();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, new String[]{"Error in reading the species file!"}, "Warning!", JOptionPane.WARNING_MESSAGE);
            this.dispose();
        }


        txtFieldSearch.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyPressed(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyReleased(KeyEvent e) {
                if (txtFieldSearch.getText().length() > 3) {
                    iSpeciesList.removeAllElements();
                    for (int i = 0; i < iAllSpecies.size(); i++) {
                        if (iAllSpecies.get(i).getSpecieName().toLowerCase().indexOf(txtFieldSearch.getText().toLowerCase()) >= 0) {
                            iSpeciesList.add(iAllSpecies.get(i));
                        }
                    }
                    allSpecies.setSelectedIndex(0);
                    allSpecies.updateUI();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //check if a species is selected
                if (iSelectedSpecies != null) {
                    //open a file saver
                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fc.showSaveDialog(new JFrame());
                    final String lFolderLocation = fc.getSelectedFile().getAbsolutePath();

                    final MainInformationFeeder iFeeder = MainInformationFeeder.getInstance();
                    final Vector lFilename = new Vector();
                    SwingWorker saver = new SwingWorker() {
                        public Boolean construct() {
                            JOptionPane.showMessageDialog(new JFrame(), new String[]{"Downloading a species specific database is dependant on the speed of your internet connection.\nThis may take 10 or more minutes!"}, "Warning!", JOptionPane.WARNING_MESSAGE);
                            progressBar1.setVisible(true);
                            progressBar1.setIndeterminate(true);
                            progressBar1.setStringPainted(true);
                            progressBar1.setString("");
                            saveButton.setEnabled(false);

                            FastaDatabaseDownloader lDownloaderFasta = new FastaDatabaseDownloader(lFolderLocation, iSelectedSpecies.getSpecieName(), progressBar1);
                            boolean allOk = lDownloaderFasta.start();
                            lFilename.add(lDownloaderFasta.getDatabaseFilename());
                            lFilename.add(lDownloaderFasta.getNumberOfSavedProteins());
                            return allOk;
                        }

                        public void finished() {
                            progressBar1.setIndeterminate(false);
                            progressBar1.setVisible(false);
                            saveButton.setVisible(false);
                            exitButton.setVisible(true);
                            iFeeder.addFastaFile(new File(lFolderLocation, (String) lFilename.get(0)));
                            JOptionPane.showMessageDialog(new JFrame(), new String[]{"Downloaded " + lFilename.get(1) + " proteins and saved to the " + lFilename.get(0) + " database !"}, "Info!", JOptionPane.INFORMATION_MESSAGE);
                        }

                    };
                    saver.start();
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), new String[]{"No species was selected!"}, "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        exitButton.setVisible(false);
        progressBar1.setVisible(false);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //set JFrame parameters
        this.setTitle("Download a species specific .fasta database");
        this.setContentPane(jpanContent);
        this.setSize(700, 500);
        this.setLocation(100, 100);
        this.setVisible(true);

    }

    private void createUIComponents() {
        allSpecies = new JList(iSpeciesList);
        allSpecies.setMinimumSize(new Dimension(150, 300));
        allSpecies.setMaximumSize(new Dimension(150, 300));
        ListSelectionModel listSelectionModel = allSpecies.getSelectionModel();
        listSelectionModel.addListSelectionListener(new listSelectionHandler());

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
        final JLabel label1 = new JLabel();
        label1.setText("Seach species");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(label1, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.gridheight = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(scrollPane1, gbc);
        allSpecies.setFont(new Font(allSpecies.getFont().getName(), Font.ITALIC, allSpecies.getFont().getSize()));
        scrollPane1.setViewportView(allSpecies);
        txtFieldSearch = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(txtFieldSearch, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setText("Selected species");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 40;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(label2, gbc);
        lblSelectedSpecies = new JLabel();
        lblSelectedSpecies.setFont(new Font(lblSelectedSpecies.getFont().getName(), Font.ITALIC, lblSelectedSpecies.getFont().getSize()));
        lblSelectedSpecies.setText("no species selected");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(lblSelectedSpecies, gbc);
        exitButton = new JButton();
        exitButton.setText("exit");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(exitButton, gbc);
        saveButton = new JButton();
        saveButton.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(saveButton, gbc);
        progressBar1 = new JProgressBar();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(progressBar1, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }

    private class listSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            iSelectedSpecies = (SwissProtComposition) allSpecies.getSelectedValue();
            lblSelectedSpecies.setText(iSelectedSpecies.getSpecieName());
        }
    }
}
