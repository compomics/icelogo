/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.icelogo.gui.interfaces;

import javax.swing.JPanel;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author Davy
 */
public interface Savable {

    /**
     * This method gives a SVG document if available
     *
     * @return SVGDocument
     */
    SVGDocument getSVG();

    /**
     * Returns the text, if the Savable is Text type. Else returns null.
     *
     * @return
     */
    String getText();

    /**
     * Getter for the title
     *
     * @return String with the title
     */
    String getTitle();

    /**
     * Gives a boolean that indicates if the saveble is a chart
     *
     * @return boolean
     */
    boolean isChart();

    /**
     * Gives a boolean that indicates if the saveble is an svg
     *
     * @return boolean
     */
    boolean isSvg();

    /**
     * Gives a boolean that indicates if the saveble is text.
     *
     * @return
     */
    boolean isText();

    public String getDescription();

    public JPanel getContentPanel();
}
