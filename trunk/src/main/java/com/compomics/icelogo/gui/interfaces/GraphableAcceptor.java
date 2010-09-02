package com.compomics.icelogo.gui.interfaces;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 2-sep-2008 Time: 11:15:45 The 'DataModelAcceptor ' class was created for
 */
public interface GraphableAcceptor {
// -------------------------- OTHER METHODS --------------------------

    public void addComponent(JComponent comp, String aTitle);

    public void addGraphable(Graphable aGraphable);

    public void removeAll();

    public void removeAllSavables();
}
