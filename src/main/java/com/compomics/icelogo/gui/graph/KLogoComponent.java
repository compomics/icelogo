package com.compomics.icelogo.gui.graph;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.RegulatedEntity;
import com.compomics.icelogo.core.data.RegulatedPosition;
import com.compomics.icelogo.core.enumeration.ColorScheme;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.gui.interfaces.Savable;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Sep 27, 2009 Time: 1:46:16 PM
 * <p/>
 * This class
 */
public class KLogoComponent extends JSVGCanvas implements Observer, Savable {
    public int iLogoWidth;
    public int iLogoHeight;
    public int iLogoElements;
    public int iStartPosition;
    public double iLogoYaxisHeight;
    public double iElementWidth;

    public ColorScheme iColorScheme;
    public SVGDocument doc;
    public ScoringTypeEnum iScoringType;
    public MatrixDataModel iDataModel;
    public double iZscore;
    public double iPvalue;
    public RegulatedPosition[] iRegulatedPositions;
    public MainInformationFeeder iMainInformationFeeder;
    private boolean isSubLogo;

    public KLogoComponent(MatrixDataModel aDataModel, boolean isSubLogo) {

        this.iLogoElements = aDataModel.getNumberOfPositions();
        this.isSubLogo = isSubLogo;
        this.iMainInformationFeeder = MainInformationFeeder.getInstance();
        iMainInformationFeeder.addObserver(this);
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
        this.makeSVG();
    }

    public KLogoComponent(final MatrixDataModel[] aClusteredMatrixDatamodels) {
    }


    public SVGDocument getSVG() {
        return doc;
    }

    public void getInfo() {
        this.iStartPosition = iMainInformationFeeder.getStartPosition();
        this.iZscore = iMainInformationFeeder.getZscore();
        this.iLogoYaxisHeight = iMainInformationFeeder.getYaxisValue();
        this.iScoringType = iMainInformationFeeder.getScoringType();
        this.iPvalue = iMainInformationFeeder.getPvalue();
    }


    public void makeSVG() {

        this.getInfo();
        iRegulatedPositions = iDataModel.getRegulatedPositions(iZscore);

        this.iColorScheme = iMainInformationFeeder.getColorScheme();
        if (isSubLogo) {
            this.iLogoHeight = iMainInformationFeeder.getSubLogoHeight();
            this.iLogoWidth = iMainInformationFeeder.getSubLogoWidth();
        } else {
            this.iLogoHeight = iMainInformationFeeder.getGraphableHeight();
            this.iLogoWidth = iMainInformationFeeder.getGraphableWidth();
        }
        //calculate the width of every element
        iElementWidth = (iLogoWidth - 100) / iLogoElements;


        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        // get the root element (the svg element)
        Element svgRoot = doc.getDocumentElement();

        // set the width and height attribute on the svg root element
        svgRoot.setAttributeNS(null, "width", String.valueOf(iLogoWidth - 50));
        svgRoot.setAttributeNS(null, "height", String.valueOf(iLogoHeight));

        //paint axis
        Element yAxis = doc.createElementNS(svgNS, "rect");
        yAxis.setAttributeNS(null, "x", "49");
        yAxis.setAttributeNS(null, "y", "50");
        yAxis.setAttributeNS(null, "width", "1");
        yAxis.setAttributeNS(null, "height", String.valueOf(iLogoHeight - 100));
        yAxis.setAttributeNS(null, "style", "fill:black");

        Element xAxis1 = doc.createElementNS(svgNS, "rect");
        xAxis1.setAttributeNS(null, "x", "49");
        xAxis1.setAttributeNS(null, "y", String.valueOf(iLogoHeight / 2 - 9));
        xAxis1.setAttributeNS(null, "width", String.valueOf(iElementWidth * iLogoElements));
        xAxis1.setAttributeNS(null, "height", "1");
        xAxis1.setAttributeNS(null, "style", "fill:black");

        Element xAxis2 = doc.createElementNS(svgNS, "rect");
        xAxis2.setAttributeNS(null, "x", "49");
        xAxis2.setAttributeNS(null, "y", String.valueOf(iLogoHeight / 2 + 10));
        xAxis2.setAttributeNS(null, "width", String.valueOf(iElementWidth * iLogoElements));
        xAxis2.setAttributeNS(null, "height", "1");
        xAxis2.setAttributeNS(null, "style", "fill:black");
        //arrows
        Element top = doc.createElementNS(svgNS, "path");
        top.setAttributeNS(null, "d", "M  44.5,54 L 49.5,50 L 54.5,54");
        top.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        Element bottom = doc.createElementNS(svgNS, "path");
        bottom.setAttributeNS(null, "d", "M 44.5," + String.valueOf(iLogoHeight - 54) + " L 49.5," + String.valueOf(iLogoHeight - 50) + " L 54.5," + String.valueOf(iLogoHeight - 54));
        bottom.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        //axis markers

        //paint axis markers
        Element markerLine1 = doc.createElementNS(svgNS, "path");
        markerLine1.setAttributeNS(null, "d", "M  49,70 L 44,70 L 44,70");
        markerLine1.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        Element markerLine2 = doc.createElementNS(svgNS, "path");
        markerLine2.setAttributeNS(null, "d", "M  49," + String.valueOf(60 + (iLogoHeight / 2 - 10 - 50) / 2) + " L 44," + String.valueOf(60 + (iLogoHeight / 2 - 10 - 50) / 2) + "");
        markerLine2.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        Element markerLine3 = doc.createElementNS(svgNS, "path");
        markerLine3.setAttributeNS(null, "d", "M  49," + String.valueOf(iLogoHeight - 70) + " L 44," + String.valueOf(iLogoHeight - 70) + "");
        markerLine3.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        Element markerLine4 = doc.createElementNS(svgNS, "path");
        markerLine4.setAttributeNS(null, "d", "M  49," + String.valueOf(iLogoHeight - 60 - (iLogoHeight / 2 - 10 - 50) / 2) + " L 44," + String.valueOf(iLogoHeight - 60 - (iLogoHeight / 2 - 10 - 50) / 2) + "");
        markerLine4.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");

        Element marker1 = doc.createElementNS(svgNS, "text");
        marker1.setAttributeNS(null, "x", "20");
        marker1.setAttributeNS(null, "y", "70");
        marker1.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
        marker1.setAttributeNS(null, "text-anchor", "middle");
        Text markerText1 = doc.createTextNode(String.valueOf(iLogoYaxisHeight));
        marker1.appendChild(markerText1);
        svgRoot.appendChild(marker1);

        Element marker2 = doc.createElementNS(svgNS, "text");
        marker2.setAttributeNS(null, "x", "20");
        marker2.setAttributeNS(null, "y", String.valueOf(iLogoHeight - 70));
        marker2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
        marker2.setAttributeNS(null, "text-anchor", "middle");
        Text markerText2 = doc.createTextNode(String.valueOf(iLogoYaxisHeight * -1));
        marker2.appendChild(markerText2);
        svgRoot.appendChild(marker2);

        Element marker3 = doc.createElementNS(svgNS, "text");
        marker3.setAttributeNS(null, "x", "20");
        marker3.setAttributeNS(null, "y", String.valueOf(60 + (iLogoHeight / 2 - 10 - 50) / 2));
        marker3.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
        marker3.setAttributeNS(null, "text-anchor", "middle");
        Text markerText3 = doc.createTextNode(String.valueOf(iLogoYaxisHeight / 2));
        marker3.appendChild(markerText3);
        svgRoot.appendChild(marker3);

        Element marker4 = doc.createElementNS(svgNS, "text");
        marker4.setAttributeNS(null, "x", "20");
        marker4.setAttributeNS(null, "y", String.valueOf(iLogoHeight - 60 - (iLogoHeight / 2 - 10 - 50) / 2));
        marker4.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
        marker4.setAttributeNS(null, "text-anchor", "middle");
        Text markerText4 = doc.createTextNode(String.valueOf((iLogoYaxisHeight / 2) * -1));
        marker4.appendChild(markerText4);
        svgRoot.appendChild(marker4);

        //paint numbers
        int startNumber = iStartPosition;
        int elementCount = 0;
        for (int s = startNumber; s < startNumber + iLogoElements; s++) {
            //a small position change for numbers that are made from one number, and for the negative numbers

            Element number = doc.createElementNS(svgNS, "text");
            number.setAttributeNS(null, "x", String.valueOf(50 + elementCount * iElementWidth + iElementWidth / 2));
            number.setAttributeNS(null, "y", String.valueOf(iLogoHeight / 2 + 6));
            number.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            number.setAttributeNS(null, "text-anchor", "middle");
            Text numberText = doc.createTextNode(String.valueOf(s));
            number.appendChild(numberText);
            svgRoot.appendChild(number);


            elementCount = elementCount + 1;
            Element line = doc.createElementNS(svgNS, "path");
            line.setAttributeNS(null, "d", "M  " + String.valueOf(49 + elementCount * iElementWidth) + "," + String.valueOf(iLogoHeight / 2 - 9) + " L " + String.valueOf(49 + elementCount * iElementWidth) + "," + String.valueOf(iLogoHeight / 2 + 10));
            line.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
            svgRoot.appendChild(line);
        }


        svgRoot.appendChild(yAxis);
        svgRoot.appendChild(xAxis1);
        svgRoot.appendChild(xAxis2);
        svgRoot.appendChild(top);
        svgRoot.appendChild(bottom);
        svgRoot.appendChild(markerLine1);
        svgRoot.appendChild(markerLine2);
        svgRoot.appendChild(markerLine3);
        svgRoot.appendChild(markerLine4);


        if (ScoringTypeEnum.FOLDCHANGE == iScoringType) {
            Element titleAxis1 = doc.createElementNS(svgNS, "text");
            titleAxis1.setAttributeNS(null, "x", String.valueOf(iLogoHeight / -2));
            titleAxis1.setAttributeNS(null, "y", "15");
            titleAxis1.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis1.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis1.setAttributeNS(null, "text-anchor", "middle");
            Text data1 = doc.createTextNode("P value = " + iPvalue);
            titleAxis1.appendChild(data1);
            svgRoot.appendChild(titleAxis1);

            Element titleAxis2 = doc.createElementNS(svgNS, "text");
            titleAxis2.setAttributeNS(null, "x", String.valueOf(iLogoHeight / -2));
            titleAxis2.setAttributeNS(null, "y", "35");
            titleAxis2.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis2.setAttributeNS(null, "text-anchor", "middle");
            Text data2 = doc.createTextNode("Fold change");
            titleAxis2.appendChild(data2);
            svgRoot.appendChild(titleAxis2);
        } else if (ScoringTypeEnum.PERCENTAGE == iScoringType) {
            Element titleAxis1 = doc.createElementNS(svgNS, "text");
            titleAxis1.setAttributeNS(null, "x", String.valueOf(iLogoHeight / -2));
            titleAxis1.setAttributeNS(null, "y", "15");
            titleAxis1.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis1.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis1.setAttributeNS(null, "text-anchor", "middle");
            Text data1 = doc.createTextNode("P value = " + iPvalue);
            titleAxis1.appendChild(data1);
            svgRoot.appendChild(titleAxis1);

            Element titleAxis2 = doc.createElementNS(svgNS, "text");
            titleAxis2.setAttributeNS(null, "x", String.valueOf(iLogoHeight / -2));
            titleAxis2.setAttributeNS(null, "y", "35");
            titleAxis2.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis2.setAttributeNS(null, "text-anchor", "middle");
            Text data2 = doc.createTextNode("% difference");
            titleAxis2.appendChild(data2);
            svgRoot.appendChild(titleAxis2);
        } else if (ScoringTypeEnum.STANDARD_DEVIATION == iScoringType) {
            Element titleAxis1 = doc.createElementNS(svgNS, "text");
            titleAxis1.setAttributeNS(null, "x", String.valueOf(iLogoHeight / -2));
            titleAxis1.setAttributeNS(null, "y", "15");
            titleAxis1.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis1.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis1.setAttributeNS(null, "text-anchor", "middle");
            Text data1 = doc.createTextNode("P value = " + iPvalue);
            titleAxis1.appendChild(data1);
            svgRoot.appendChild(titleAxis1);

            Element titleAxis2 = doc.createElementNS(svgNS, "text");
            titleAxis2.setAttributeNS(null, "x", String.valueOf(iLogoHeight / -2));
            titleAxis2.setAttributeNS(null, "y", "35");
            titleAxis2.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
            titleAxis2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis2.setAttributeNS(null, "text-anchor", "middle");
            Text data2 = doc.createTextNode("Standard deviation");
            titleAxis2.appendChild(data2);
            svgRoot.appendChild(titleAxis2);
        }

        //paint positive sequence elements
        for (int p = 0; p < iRegulatedPositions.length; p++) {
            RegulatedPosition position = iRegulatedPositions[p];
            RegulatedEntity[] posEntities = position.getPositiveRegulatedEntity(iScoringType);
            double elementStartY = iLogoHeight / 2 - 10.0;
            double elementStartX = 50.0 + p * iElementWidth;

            for (int e = 0; e < posEntities.length; e++) {
                RegulatedEntity entity = posEntities[e];
                //if an elements is not in the positve set the fold change will be infinite. But it is set to -100.101010
                //But the height and the color must be changed to cleary show this to the user
                if (entity.getInfinite() && iScoringType == ScoringTypeEnum.FOLDCHANGE) {
                    if (e == 0) {
                        entity.setChange(iScoringType, iLogoYaxisHeight / 2);
                    } else {
                        entity.setChange(iScoringType, posEntities[e - 1].getChange(iScoringType) * 1.1);
                    }
                }
                //first check if the elements is at least 5% of the max
                if (entity.getChange(iScoringType) >= iLogoYaxisHeight / 40.0) {
                    //calculate height of picture
                    double changeElement = entity.getChange(iScoringType);
                    double pictureHeight = (int) ((int) (iLogoHeight / 2 - 90) * (changeElement / iLogoYaxisHeight));
                    int fontHeight = (int) (pictureHeight / 0.71582);
                    //System.out.println(fontHeight);
                    double letterWidth = fontHeight * 0.92041;
                    double calcFactor = iElementWidth / letterWidth;
                    if (calcFactor > 0.0 && pictureHeight > 5) {
                        Element aa = doc.createElementNS(svgNS, "text");
                        if (entity.getInfinite()) {
                            aa.setAttributeNS(null, "x", String.valueOf(((elementStartX + iElementWidth / 2) / calcFactor)));
                            aa.setAttributeNS(null, "y", String.valueOf(elementStartY - 3));
                            aa.setAttributeNS(null, "transform", "scale(" + calcFactor + ",1.0)");
                            aa.setAttributeNS(null, "style", "font-size:" + String.valueOf(fontHeight - 6) + "px;fill:pink;background-color:black;font-family:Arial");
                            aa.setAttributeNS(null, "text-anchor", "middle");
                        } else {
                            aa.setAttributeNS(null, "x", String.valueOf(((elementStartX + iElementWidth / 2) / calcFactor)));
                            aa.setAttributeNS(null, "y", String.valueOf(elementStartY - 3));
                            aa.setAttributeNS(null, "transform", "scale(" + calcFactor + ",1.0)");
                            aa.setAttributeNS(null, "style", "font-size:" + String.valueOf(fontHeight - 6) + "px;;fill:" + entity.getColorName() + ";font-family:Arial");
                            aa.setAttributeNS(null, "text-anchor", "middle");
                        }

                        Text data = doc.createTextNode(String.valueOf(entity.getAminoAcid()));
                        aa.appendChild(data);
                        svgRoot.appendChild(aa);

                        elementStartY = elementStartY - pictureHeight;

                    }
                }
            }
        }

        //paint negative sequence elements
        for (int p = 0; p < iRegulatedPositions.length; p++) {
            RegulatedPosition position = iRegulatedPositions[p];
            RegulatedEntity[] negEntities = position.getNegativeRegulatedEntity(iScoringType);
            double elementStartY = iLogoHeight / 2 + 10.0;
            double elementStartX = 50.0 + p * iElementWidth;

            for (int e = 0; e < negEntities.length; e++) {
                RegulatedEntity entity = negEntities[e];
                //if an elements is not in the positve set the fold change will be infinite. But it is set to -100.101010
                //But the height and the color must be changed to cleary show this to the user
                if (entity.getInfinite() && iScoringType == ScoringTypeEnum.FOLDCHANGE) {
                    if (e == 0) {
                        entity.setChange(iScoringType, -iLogoYaxisHeight / negEntities.length);
                    } else {
                        entity.setChange(iScoringType, negEntities[e - 1].getChange(iScoringType));
                    }
                }
                //first check if the elements is at least 5% of the max
                if (entity.getChange(iScoringType) <= iLogoYaxisHeight / -40.0) {
                    //calculate height of picture
                    double changeElement = entity.getChange(iScoringType);
                    double pictureHeight = (int) ((int) (iLogoHeight / 2 - 90) * (changeElement / iLogoYaxisHeight));
                    pictureHeight = pictureHeight * -1;
                    int fontHeight = (int) (pictureHeight / 0.71582);
                    //System.out.println(fontHeight);
                    double letterWidth = fontHeight * 0.92041;
                    double calcFactor = (double) iElementWidth / letterWidth;
                    //The letter must have a minimum height
                    if (calcFactor > 0.0 && pictureHeight > 5) {
                        elementStartY = elementStartY + pictureHeight;
                        Element aa = doc.createElementNS(svgNS, "text");
                        if (entity.getInfinite()) {
                            aa.setAttributeNS(null, "x", String.valueOf(((elementStartX + iElementWidth / 2) / calcFactor)));
                            aa.setAttributeNS(null, "y", String.valueOf(elementStartY));
                            aa.setAttributeNS(null, "transform", "scale(" + calcFactor + ",1.0)");
                            aa.setAttributeNS(null, "style", "font-size:" + String.valueOf(fontHeight - 6) + "px;fill:pink;background-color:black;font-family:Arial");
                            aa.setAttributeNS(null, "text-anchor", "middle");
                        } else {
                            aa.setAttributeNS(null, "x", String.valueOf(((elementStartX + iElementWidth / 2) / calcFactor)));
                            aa.setAttributeNS(null, "y", String.valueOf(elementStartY));
                            aa.setAttributeNS(null, "transform", "scale(" + calcFactor + ",1.0)");
                            Color color = iColorScheme.getAminoAcidColor(entity);
                            aa.setAttributeNS(null, "style", "font-size:" + String.valueOf(fontHeight - 6) + "px;;fill:" + String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue())  + ";font-family:Arial");
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


    public void zoomIn() {
        iLogoYaxisHeight = Math.round(iLogoYaxisHeight * 0.5 * 100) / 100;
        if (iLogoYaxisHeight == 0.0) {
            iLogoYaxisHeight = 1.0;
        }
        iMainInformationFeeder.setYaxisValue((int) iLogoYaxisHeight);
        this.makeSVG();
    }

    public void zoomOut() {
        iLogoYaxisHeight = Math.round(iLogoYaxisHeight * 1.5 * 100) / 100;
        if (iLogoYaxisHeight == 1.0) {
            iLogoYaxisHeight = 2.0;
        }
        iMainInformationFeeder.setYaxisValue((int) iLogoYaxisHeight);
        this.makeSVG();
    }

    public void update(Observable o, Object arg) {
        if (arg != null && (arg.equals(ObservableEnum.NOTIFY_STATISTICAL) || arg.equals(ObservableEnum.NOTIFY_Y_AXIS) || arg.equals(ObservableEnum.NOTIFY_SCORING_TYPE) || arg.equals(ObservableEnum.NOTIFY_COLOR_SCHEME) || arg.equals(ObservableEnum.NOTIFY_GRAPHABLE_FRAME_SIZE) || arg.equals(ObservableEnum.NOTIFY_START_POSITION))) {
            if (arg.equals(ObservableEnum.NOTIFY_Y_AXIS) && iLogoYaxisHeight != iMainInformationFeeder.getYaxisValue()) {
                this.makeSVG();
            } else if (!arg.equals(ObservableEnum.NOTIFY_Y_AXIS)) {
                this.makeSVG();
            }

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
        return "icelogo";
    }

    public String getDescription() {
        return "icelogo of positive and reference set";
    }

    /**
     * Gives a boolean that indicates if the saveble is text.
     *
     * @return
     */
    public boolean isText() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
