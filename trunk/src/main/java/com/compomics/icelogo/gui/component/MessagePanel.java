package com.compomics.icelogo.gui.component;

import com.compomics.icelogo.gui.interfaces.MessageAcceptor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 27-okt-2008 Time: 17:48:03 The 'MessagePanel ' class was created for
 */
public class MessagePanel extends JPanel implements MessageAcceptor {
    private JLabel lblText;


    public MessagePanel() {
        super();

        lblText = new JLabel();
        lblText.setFont(new Font("Verdana", lblText.getFont().getStyle(), 10));
        lblText.setText("Status");
        lblText.setForeground(Color.GRAY);

        this.add(lblText);

    }

    public void message(final Object aObject) {
        lblText.setText(aObject.toString());
        repaint();
    }
}
