package com.compomics.icelogo.gui.component;

import com.compomics.icelogo.gui.interfaces.MessageAcceptor;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 27-okt-2008 Time: 17:42:15 The 'Messenger ' class was created for
 */
public class Messenger {

    private static Messenger iSingleton = new Messenger();
    private Vector<MessageAcceptor> iListeners = new Vector<MessageAcceptor>();
    private JProgressBar progress = new JProgressBar();

    private Messenger() {
        // Disable the progressbar by default.
        setProgressIndeterminate(false);
    }

    public static Messenger getInstance() {
        if (iSingleton == null) {
            iSingleton = new Messenger();
        }

        return iSingleton;
    }

    public void addMessageListener(MessageAcceptor aListener) {
        iListeners.add(aListener);
    }

    public void sendMessage(Object aMessage) {
        for (int i = 0; i < iListeners.size(); i++) {
            MessageAcceptor lMessageListener = iListeners.elementAt(i);
            lMessageListener.message(aMessage);
        }
    }

    /**
     * Set the status of the Messenger progressbar.
     *
     * @param aStatus
     */
    public void setProgressIndeterminate(boolean aStatus) {
        progress.setIndeterminate(aStatus);
        progress.setVisible(aStatus);
    }

    public void setProgressStringPainted(boolean aStatus){
        progress.setStringPainted(aStatus);
    }

    /**
     * Set the String to be displayed in the messenger progressbar.
     *
     * @param aString
     */
    public void setProgressMessage(String aString) {
        progress.setString(aString);
    }

    /**
     * Get the progressbar used by the messenger.
     *
     * @return
     */
    public JProgressBar getProgressBar() {
        return progress;
    }
}
