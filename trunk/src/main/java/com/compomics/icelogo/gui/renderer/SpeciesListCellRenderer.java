package com.compomics.icelogo.gui.renderer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 24-Jun-2009
 * Time: 13:10:58
 */
public class SpeciesListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        c.setFont(c.getFont().deriveFont(Font.ITALIC));
        return c;
    }
}