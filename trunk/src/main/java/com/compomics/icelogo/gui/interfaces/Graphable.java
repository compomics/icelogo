package com.compomics.icelogo.gui.interfaces;

import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 2-mrt-2009
 * Time: 12:53:28
 * To change this template use File | Settings | File Templates.
 */
public interface Graphable {


    /**
     * Gives a boolean that indicates if the saveble is an svg
     *
     * @return boolean
     */
    public boolean isSvg();


    /**
     * Gives a boolean that indicates if the saveble is a chart
     *
     * @return boolean
     */
    public boolean isChart();

    /**
     * This method gives a panel with the savable information
     *
     * @return JPanel
     */
    public JPanel getContentPanel();

    /**
     * This method gives a SVG document if available
     *
     * @return SVGDocument
     */
    public SVGDocument getSVG();

    /**
     * Getter for the title
     *
     * @return String with the title
     */
    public String getTitle();

    /**
     * Getter for the description
     *
     * @return String with the description
     */
    public String getDescription();


}
