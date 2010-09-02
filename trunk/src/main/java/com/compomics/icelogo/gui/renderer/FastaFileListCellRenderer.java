package com.compomics.icelogo.gui.renderer;

import com.compomics.icelogo.gui.model.FastaFileComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 18, 2009
 * Time: 7:08:06 PM
 * <p/>
 * This class
 */
public class FastaFileListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(list == null){
            list = new JList();
        }
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            if (value instanceof File) {
                File lFile = (File) value;
                value = lFile.getName();
                c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            } else if (value.equals(FastaFileComboBoxModel.AddString) || value.equals(FastaFileComboBoxModel.DownloadString)) {
                c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(c.getFont().deriveFont(Font.ITALIC));
            }
        }
        return c;
    }
}
