package com.compomics.icelogo.gui.model;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 24-Jun-2009
 * Time: 13:06:20
 */
public class SpeciesComboBoxModel extends DefaultComboBoxModel {

    public static String AddString = "Add a new species...";

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of Files.
     */
    public SpeciesComboBoxModel() {
        super();
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        if (lFeeder.getSwissProtCompositions() != null) {
            setSpecies(lFeeder.getSwissProtCompositions());
        }
    }



    @Override
    public int getSize() {
        return super.getSize() + 1;
    }

    @Override
    public Object getElementAt(final int index) {
        if (index == super.getSize()) {
            // If the index equals the number of objects of the parent,
            // the model must show the "Add..." String.
            return AddString;
        } else {
            return super.getElementAt(index);
        }
    }

    @Override
    public Object getSelectedItem() {
        Object o = super.getSelectedItem();

        return o;
    }

    /**
     * Set the SwissProtCompositions inside the combobox.
     *
     * @param aCompositions
     */
    public void setSpecies(Vector<SwissProtComposition> aCompositions) {
        super.removeAllElements();
        for (int i = 0; i < aCompositions.size(); i++) {
            super.addElement(aCompositions.get(i));
        }
    }

}
