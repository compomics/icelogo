package com.compomics.icelogo.gui.model;

import com.compomics.icelogo.core.data.MainInformationFeeder;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Jun 18, 2009 Time: 4:37:50 PM
 * <p/>
 * This class
 */
public class FastaFileComboBoxModel extends DefaultComboBoxModel {
    public static String AddString = "Add a new Fasta file...";
    public static String DownloadString = "Download a new Fasta file from Uniprot...";

    /**
     * Constructs a DefaultComboBoxModel object initialized with an array of Files.
     */
    public FastaFileComboBoxModel(JComboBox aParent) {
        super();
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        ArrayList<File> lFastaFiles = lFeeder.getFastaFiles();
        if (lFastaFiles != null && lFastaFiles.size() > 0) {
            setFastaFiles(lFastaFiles.get(0), lFastaFiles);
        }
    }


    @Override
    public int getSize() {
        return super.getSize() + 2;
    }

    @Override
    public Object getElementAt(final int index) {
        if (index == super.getSize()) {
            // If the index equals the number of objects of the parent,
            // the model must show the "Add..." String.
            return AddString;
        } else if (index == super.getSize() + 1) {
            // If the index equals the number of objects plus 1 of the parent,
            // the model must show the "Download..." String.
            return DownloadString;
        } else {
            return super.getElementAt(index);
        }
    }

    @Override
    public Object getSelectedItem() {
        Object o = super.getSelectedItem();
        // If the add string has been requested, then select a new file.
        return o;
    }


    private void setFastaFiles(final File aFile, final ArrayList<File> aFastaFiles) {
        //Set the fasta files inside the combobox.
        removeAllElements();
        for (int i = 0; i < aFastaFiles.size(); i++) {
            File lFastaFile = aFastaFiles.get(i);
            addElement(lFastaFile);
        }
        setSelectedItem(aFile);
    }

    /**
     * This method tries to match the parameter file to its contents, and if a match is found it will be used as the
     * selected item for the combobox.
     *
     * @param aFile The required fasta file.
     */
    public void setSelectedFastaFile(File aFile) {
        for (int i = 0; i < getSize(); i++) {
            Object o = getElementAt(i);
            if (o instanceof File) {
                File lFile = (File) o;
                if (lFile.equals(aFile)) {
                    this.setSelectedItem(lFile);
                    break;
                }
            }
        }
    }
}
