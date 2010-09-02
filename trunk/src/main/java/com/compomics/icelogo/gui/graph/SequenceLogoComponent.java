package com.compomics.icelogo.gui.graph;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.RegulatedEntity;
import com.compomics.icelogo.core.data.RegulatedPosition;
import com.compomics.icelogo.core.enumeration.ColorScheme;
import com.compomics.icelogo.core.enumeration.ExperimentTypeEnum;
import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.gui.interfaces.Graphable;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 13-nov-2008
 * Time: 15:41:22
 * This class can create a "weblogo". The weblogo can be constructed using a negative set correction.
 * More information can be found on the website:  http://weblogo.berkeley.edu/ .
 */
public class SequenceLogoComponent extends JSVGCanvas implements Observer, Graphable {
    public int iLogoWidth;
    public int iLogoHeigth;
    public int iLogoElements;
    public int iStartPosition;
    public double iLogoYaxisHeigth;
    public double iElementWidth;

    public ColorScheme iScheme;
    public SVGDocument doc;
    public ScoringTypeEnum iScoringType;
    public MatrixDataModel iDataModel;
    public double iStandardDeviation;
    public double iPvalue;
    public RegulatedPosition[] iRegulatedPositions;
    public MainInformationFeeder iInformationFeeder;
    public boolean iFill = false;
    public boolean iNegativeSetCorrection = true;

    /**
     * The Constructor
     *
     * @param aDataModel MatrixDataModel to base the weblogo on.
     */
    public SequenceLogoComponent(MatrixDataModel aDataModel) {

        this.iLogoElements = aDataModel.getNumberOfPositions();
        this.iInformationFeeder = MainInformationFeeder.getInstance();
        iInformationFeeder.addObserver(this);
        this.iDataModel = aDataModel;
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == 1) {
                    zoomIn();
                    repaint();
                }
                if (me.getButton() == 3) {
                    zoomOut();
                    repaint();
                }
            }
        });
        getInfo();
        if (iFill) {
            this.iLogoYaxisHeigth = 1.0;
        } else {
            this.iLogoYaxisHeigth = 4.0;
        }
        this.makeSVG();
    }

    /**
     * This methods gives an SVG document.
     *
     * @return An SVG document
     */
    public SVGDocument getSVG() {
        return doc;
    }

    /**
     * This method get all the necessary information from the MainInformationFeeder Singleton.
     */
    public void getInfo() {
        this.iStartPosition = iInformationFeeder.getStartPosition();
        this.iStandardDeviation = iInformationFeeder.getZscore();
        this.iScoringType = ScoringTypeEnum.FREQUENCY;
        this.iPvalue = iInformationFeeder.getPvalue();
        this.iFill = iInformationFeeder.isFillWeblogo();
        this.iNegativeSetCorrection = iInformationFeeder.isWeblogoNegativeCorrection();
    }


    /**
     * This method "paints" the logo in a SVG document.
     */
    public void makeSVG() {

        this.getInfo();
        this.iRegulatedPositions = iDataModel.getAllPositions();
        this.iScheme = iInformationFeeder.getColorScheme();
        this.iLogoHeigth = iInformationFeeder.getGraphableHeight();
        this.iLogoWidth = iInformationFeeder.getGraphableWidth();
        //calculate the width of every element
        iElementWidth = (iLogoWidth - 100) / iLogoElements;


        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        // get the root element (the svg element)
        Element svgRoot = doc.getDocumentElement();

        // set the width and height attribute on the svg root element
        svgRoot.setAttributeNS(null, "width", String.valueOf(iLogoWidth - 50));
        svgRoot.setAttributeNS(null, "height", String.valueOf(iLogoHeigth));

        //paint axis
        Element yAxis = doc.createElementNS(svgNS, "rect");
        yAxis.setAttributeNS(null, "x", "49");
        yAxis.setAttributeNS(null, "y", "50");
        yAxis.setAttributeNS(null, "width", "1");
        yAxis.setAttributeNS(null, "height", String.valueOf(iLogoHeigth - 80));
        yAxis.setAttributeNS(null, "style", "fill:black");

        Element xAxis1 = doc.createElementNS(svgNS, "rect");
        xAxis1.setAttributeNS(null, "x", "49");
        xAxis1.setAttributeNS(null, "y", String.valueOf(iLogoHeigth - 50));
        xAxis1.setAttributeNS(null, "width", String.valueOf(iElementWidth * iLogoElements));
        xAxis1.setAttributeNS(null, "height", "1");
        xAxis1.setAttributeNS(null, "style", "fill:black");

        Element xAxis2 = doc.createElementNS(svgNS, "rect");
        xAxis2.setAttributeNS(null, "x", "49");
        xAxis2.setAttributeNS(null, "y", String.valueOf(iLogoHeigth - 30));
        xAxis2.setAttributeNS(null, "width", String.valueOf(iElementWidth * iLogoElements));
        xAxis2.setAttributeNS(null, "height", "1");
        xAxis2.setAttributeNS(null, "style", "fill:black");

        //arrows
        Element top = doc.createElementNS(svgNS, "path");
        top.setAttributeNS(null, "d", "M  44.5,54 L 49.5,50 L 54.5,54");
        top.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        //axis markers

        //paint axis markers
        Element markerLine1 = doc.createElementNS(svgNS, "path");
        markerLine1.setAttributeNS(null, "d", "M  49,70 L 44,70 L 44,70");
        markerLine1.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        Element markerLine2 = doc.createElementNS(svgNS, "path");
        markerLine2.setAttributeNS(null, "d", "M  49," + String.valueOf(70 + (iLogoHeigth - 120) / 2) + " L 44," + String.valueOf(70 + (iLogoHeigth - 120) / 2) + "");
        markerLine2.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");

        Element marker1 = doc.createElementNS(svgNS, "text");
        marker1.setAttributeNS(null, "x", "20");
        marker1.setAttributeNS(null, "y", "70");
        marker1.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
        marker1.setAttributeNS(null, "text-anchor", "middle");
        Text markerText1 = doc.createTextNode(String.valueOf(iLogoYaxisHeigth));
        marker1.appendChild(markerText1);
        svgRoot.appendChild(marker1);


        Element marker3 = doc.createElementNS(svgNS, "text");
        marker3.setAttributeNS(null, "x", "20");
        marker3.setAttributeNS(null, "y", String.valueOf(70 + (iLogoHeigth - 70 - 50) / 2));
        marker3.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
        marker3.setAttributeNS(null, "text-anchor", "middle");
        Text markerText3 = doc.createTextNode(String.valueOf(iLogoYaxisHeigth / 2));
        marker3.appendChild(markerText3);
        svgRoot.appendChild(marker3);

        //paint numbers
        int startNumber = iStartPosition;
        int elementCount = 0;
        for (int s = startNumber; s < startNumber + iLogoElements; s++) {
            //a small position change for numbers that are made from one number, and for the negative numbers

            Element number = doc.createElementNS(svgNS, "text");
            number.setAttributeNS(null, "x", String.valueOf(50 + elementCount * iElementWidth + iElementWidth / 2));
            number.setAttributeNS(null, "y", String.valueOf(iLogoHeigth - 35));
            number.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            number.setAttributeNS(null, "text-anchor", "middle");
            Text numberText = doc.createTextNode(String.valueOf(s));
            number.appendChild(numberText);
            svgRoot.appendChild(number);


            elementCount = elementCount + 1;
            Element line = doc.createElementNS(svgNS, "path");
            line.setAttributeNS(null, "d", "M  " + String.valueOf(49 + elementCount * iElementWidth) + "," + String.valueOf(iLogoHeigth - 30) + " L " + String.valueOf(49 + elementCount * iElementWidth) + "," + String.valueOf(iLogoHeigth - 50));
            line.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
            svgRoot.appendChild(line);
        }


        svgRoot.appendChild(yAxis);
        svgRoot.appendChild(xAxis1);
        svgRoot.appendChild(xAxis2);
        svgRoot.appendChild(top);
        svgRoot.appendChild(markerLine1);
        svgRoot.appendChild(markerLine2);


        if (ScoringTypeEnum.FREQUENCY == iScoringType && !iFill) {
            Element titleAxis2 = doc.createElementNS(svgNS, "text");
            titleAxis2.setAttributeNS(null, "x", String.valueOf((iLogoHeigth / -2) + (iLogoHeigth / -4)));
            titleAxis2.setAttributeNS(null, "y", "35");
            titleAxis2.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis2.setAttributeNS(null, "text-anchor", "middle");
            Text data2 = doc.createTextNode("Bits");
            titleAxis2.appendChild(data2);
            svgRoot.appendChild(titleAxis2);
        } else {
            Element titleAxis2 = doc.createElementNS(svgNS, "text");
            titleAxis2.setAttributeNS(null, "x", String.valueOf((iLogoHeigth / -2) + (iLogoHeigth / -4)));
            titleAxis2.setAttributeNS(null, "y", "35");
            titleAxis2.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis2.setAttributeNS(null, "text-anchor", "middle");
            Text data2 = doc.createTextNode("%");
            titleAxis2.appendChild(data2);
            svgRoot.appendChild(titleAxis2);
        }

        //paint positive sequence elements
        for (int p = 0; p < iRegulatedPositions.length; p++) {
            AminoAcidStatistics lExperimentalMatrix = iDataModel.getExperimentalAminoAcidStatistics(p, ExperimentTypeEnum.EXPERIMENT);
            AminoAcidStatistics lReferenceMatrix = iDataModel.getReferenceAminoAcidStatistics(p);
            double maxHeigth = 0.0;
            if (iNegativeSetCorrection) {
                maxHeigth = (4.321 - lExperimentalMatrix.getCallBit()) - (4.321 - lReferenceMatrix.getCallBit());
                if (maxHeigth < 0.0) {
                    maxHeigth = 0.0;
                }
            } else {
                maxHeigth = lExperimentalMatrix.getBit();
            }
            RegulatedPosition position = iRegulatedPositions[p];
            RegulatedEntity[] posEntities = position.getPositveRegulatedEntity(iScoringType);
            double elementStartY;
            if (iFill) {
                elementStartY = 70.0;
                maxHeigth = 1.0;
            } else {
                elementStartY = iLogoHeigth - 50.0 - (int) ((int) (iLogoHeigth - 120) * (maxHeigth / iLogoYaxisHeigth));
            }
            double elementStartX = 50.0 + p * iElementWidth;

            for (int e = posEntities.length - 1; e >= 0; e--) {
                RegulatedEntity entity = posEntities[e];
                iScheme.setColor(entity);

                //first check if the elements height is positive
                if (entity.getChange(iScoringType) * maxHeigth >= 0.0) {
                    //calculate heigth of picture
                    double changeElement = entity.getChange(iScoringType) * maxHeigth;
                    double pictureHeigth = ((iLogoHeigth - 120) * (changeElement / iLogoYaxisHeigth));
                    double fontHeigth = (pictureHeigth / 0.71582);
                    double letterWidth = fontHeigth * 0.92041;
                    double calcFactor = iElementWidth / letterWidth;
                    if (calcFactor > 0.0 && pictureHeigth > 1.0) {
                        elementStartY = elementStartY + pictureHeigth;
                        Element aa = doc.createElementNS(svgNS, "text");
                        if (entity.getInfinite()) {
                            aa.setAttributeNS(null, "x", String.valueOf(((elementStartX + iElementWidth / 2) / calcFactor)));
                            aa.setAttributeNS(null, "y", String.valueOf(elementStartY));
                            aa.setAttributeNS(null, "transform", "scale(" + calcFactor + ",1.0)");
                            aa.setAttributeNS(null, "style", "font-size:" + String.valueOf(fontHeigth) + "px;fill:pink;background-color:black;font-family:Arial");
                            aa.setAttributeNS(null, "text-anchor", "middle");
                        } else {
                            aa.setAttributeNS(null, "x", String.valueOf(((elementStartX + iElementWidth / 2) / calcFactor)));
                            aa.setAttributeNS(null, "y", String.valueOf(elementStartY));
                            aa.setAttributeNS(null, "transform", "scale(" + calcFactor + ",1.0)");
                            aa.setAttributeNS(null, "style", "font-size:" + String.valueOf(fontHeigth) + "px;;fill:" + entity.getColorName() + ";font-family:Arial");
                            aa.setAttributeNS(null, "text-anchor", "middle");
                        }
                        Text data = doc.createTextNode(String.valueOf(entity.getAminoAcid()));
                        aa.appendChild(data);
                        svgRoot.appendChild(aa);
                    }
                }
            }
        }
        this.setSVGDocument(doc);
    }

    /**
     * This method makes the letters in the logo larger. This is done by changing the Y axis height
     */
    public void zoomIn() {
        if (!iFill) {
            iLogoYaxisHeigth = Math.round(iLogoYaxisHeigth * 0.5 * 100) / 100;
            if (iLogoYaxisHeigth == 0.0) {
                iLogoYaxisHeigth = 1.0;
            }
            this.makeSVG();
        }
    }

    /**
     * This method makes the letters in the logo smaller. This is done by changing the Y axis height
     */
    public void zoomOut() {
        if (!iFill) {
            iLogoYaxisHeigth = Math.round(iLogoYaxisHeigth * 1.5 * 100) / 100;
            if (iLogoYaxisHeigth == 1.0) {
                iLogoYaxisHeigth = 2.0;
            }
            this.makeSVG();
        }
    }

    /**
     * An update is performed when something is changed in the observed object (MainInformationFeeder)
     *
     * @param o   The observed object
     * @param arg An argument
     */
    public void update(Observable o, Object arg) {

        if(arg != null && (arg.equals(ObservableEnum.NOTIFY_SEQUENCE_LOGO) || arg.equals(ObservableEnum.NOTIFY_COLOR_SCHEME)  || arg.equals(ObservableEnum.NOTIFY_GRAPHABLE_FRAME_SIZE)  || arg.equals(ObservableEnum.NOTIFY_START_POSITION))){
            if (iInformationFeeder.isFillWeblogo()) {
                iLogoYaxisHeigth = 1.0;
            }
            this.makeSVG();
        }

    }


    public boolean isSvg() {
        return true;
    }

    public boolean isChart() {
        return false;
    }

    public JPanel getContentPanel() {
        return null;
    }

    public String getTitle() {
        return "sequenceLogo";
    }

    public String getDescription() {
        return "Sequence logo for positive set";
    }
}
