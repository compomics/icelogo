package com.compomics.icelogo.gui.graph;

import com.compomics.icelogo.core.adapter.OneSampleHeatmapAdapter;
import com.compomics.icelogo.core.adapter.TwoSampleHeatmapAdapter;
import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.ColorScheme;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import com.compomics.icelogo.core.model.TwoSampleMatrixDataModel;
import com.compomics.icelogo.core.stat.StatisticsConversion;
import com.compomics.icelogo.gui.interfaces.Savable;
import com.compomics.icelogo.gui.interfaces.HeatMapDataSupplier;
import com.compomics.icelogo.gui.renderer.DefaultHeatMapCellRenderer;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
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
public class HeatMapComponent extends JSVGCanvas implements Observer, Savable {
// ------------------------------ FIELDS ------------------------------

    public static int ONE_SAMPLE = 1;
    public static int TWO_SAMPLE = 2;


    public int iHeatMapWidth;
    public int iHeatMapHeight;
    public int iNumberOfPositions;
    public int iStartPosition;
    private int iElementHeight;
    public double iElementWidth;

    public ColorScheme iScheme;
    public SVGDocument doc;
    public ScoringTypeEnum iScoringType;
    public MatrixDataModel iMatrixDataModel;
    public double iStandardDeviation;
    public double iPvalue;
    public MainInformationFeeder iMainInformationFeeder;
    public boolean iNegativeSetCorrection = true;
    private HeatMapDataSupplier iHeatMapDataSupplier;
    private DefaultHeatMapCellRenderer iRenderer;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * The Constructor
     *
     * @param aDataModel MatrixDataModel to base the weblogo on.
     */
    public HeatMapComponent(MatrixDataModel aDataModel) {
        this.iNumberOfPositions = aDataModel.getNumberOfPositions();
        this.iMainInformationFeeder = MainInformationFeeder.getInstance();
        this.iMatrixDataModel = aDataModel;

        initHeatMapDataSupplier();
        getInfo();

        iMainInformationFeeder.addObserver(iRenderer);
        iMainInformationFeeder.addObserver(this);

        this.makeSVG();
    }

    private void initHeatMapDataSupplier() {
        // If the incoming MatrixDataModel is from the one sample type, create a OneSample - Adapter
        if (getMatrixDataModelType() == this.ONE_SAMPLE) {
            iHeatMapDataSupplier = new OneSampleHeatmapAdapter((OneSampleMatrixDataModel) iMatrixDataModel);
            // Else create a TwoSample - Adapter
        } else if (getMatrixDataModelType() == this.TWO_SAMPLE) {
            iHeatMapDataSupplier = new TwoSampleHeatmapAdapter((TwoSampleMatrixDataModel) iMatrixDataModel);
        }

        iRenderer = new DefaultHeatMapCellRenderer();
    }

    /**
     * Returns the Datamodel type of the HeatMapTable.
     *
     * @return Static Integer types HeatMapTable.ONE_SAMPLE and HeatMapTable.TWO_SAMPLE on the HeatMapTable.
     */
    public int getMatrixDataModelType() {
        if (iMatrixDataModel instanceof OneSampleMatrixDataModel) {
            return ONE_SAMPLE;
        } else if (iMatrixDataModel instanceof TwoSampleMatrixDataModel) {
            return TWO_SAMPLE;
        } else {
            assert false;
            return -1;
        }
    }

    /**
     * This method get all the necessary information from the MainInformationFeeder Singleton.
     */
    public void getInfo() {
        this.iStartPosition = iMainInformationFeeder.getStartPosition();
        this.iStandardDeviation = iMainInformationFeeder.getZscore();
        this.iScoringType = ScoringTypeEnum.FREQUENCY;
        this.iPvalue = iMainInformationFeeder.getPvalue();
        this.iNegativeSetCorrection = iMainInformationFeeder.isWeblogoNegativeCorrection();
    }

    /**
     * This method "paints" the logo in a SVG document.
     */
    public void makeSVG() {
        this.getInfo();
        this.iScheme = iMainInformationFeeder.getColorScheme();
        this.iHeatMapHeight = iMainInformationFeeder.getGraphableHeight();
        this.iHeatMapWidth = iMainInformationFeeder.getGraphableWidth();

        //calculate the width of every element
        iElementHeight = (iHeatMapHeight - 100) / AminoAcidEnum.values().length;
        iElementWidth = 30;

        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        // get the root element (the svg element)
        Element svgRoot = doc.getDocumentElement();

        int lWidth = 30 + (new Double(iNumberOfPositions * iElementWidth)).intValue() + 50;
        double lDoubleWidth = new Double(lWidth + iElementWidth).intValue();

        // set the width and height attribute on the svg root element
        svgRoot.setAttributeNS(null, "width", String.valueOf(lDoubleWidth + 100));
        svgRoot.setAttributeNS(null, "height", String.valueOf(iHeatMapHeight));

        //paint axis
        Element lInnerYaxis = doc.createElementNS(svgNS, "rect");
        lInnerYaxis.setAttributeNS(null, "x", "49");
        lInnerYaxis.setAttributeNS(null, "y", "30");
        lInnerYaxis.setAttributeNS(null, "width", "1");
        lInnerYaxis.setAttributeNS(null, "height", String.valueOf(iElementHeight * (iHeatMapDataSupplier.getRowCount()) + 22));
        lInnerYaxis.setAttributeNS(null, "style", "fill:black");

        Element lOuterYaxis = doc.createElementNS(svgNS, "rect");
        lOuterYaxis.setAttributeNS(null, "x", "20");
        lOuterYaxis.setAttributeNS(null, "y", "50");
        lOuterYaxis.setAttributeNS(null, "width", "1");
        lOuterYaxis.setAttributeNS(null, "height", String.valueOf((iElementHeight * iHeatMapDataSupplier.getRowCount()) + 2));
        lOuterYaxis.setAttributeNS(null, "style", "fill:black");

        Element lInnerXaxis = doc.createElementNS(svgNS, "rect");
        lInnerXaxis.setAttributeNS(null, "x", "20"); // The x- coordinate is the top of the first aminoacid!
        lInnerXaxis.setAttributeNS(null, "y", "50");
        lInnerXaxis.setAttributeNS(null, "width", String.valueOf(iElementWidth * iNumberOfPositions + 31));
        lInnerXaxis.setAttributeNS(null, "height", "1");
        lInnerXaxis.setAttributeNS(null, "style", "fill:black");

        Element lOuterXaxis = doc.createElementNS(svgNS, "rect");
        lOuterXaxis.setAttributeNS(null, "x", "49");
        lOuterXaxis.setAttributeNS(null, "y", "30");
        lOuterXaxis.setAttributeNS(null, "width", String.valueOf((iElementWidth * iNumberOfPositions) + 2));
        lOuterXaxis.setAttributeNS(null, "height", "1");
        lOuterXaxis.setAttributeNS(null, "style", "fill:black");


        svgRoot.appendChild(lInnerYaxis);
        svgRoot.appendChild(lOuterYaxis);
        svgRoot.appendChild(lInnerXaxis);
        svgRoot.appendChild(lOuterXaxis);


        //paint positions. (along the x-axis)

        int startNumber = iStartPosition;
        int lColumnCounter = 0;
        for (int s = startNumber; s < startNumber + iNumberOfPositions; s++) {
            //a small position change for numbers that are made from one number, and for the negative numbers
            Element number = doc.createElementNS(svgNS, "text");
            number.setAttributeNS(null, "x", String.valueOf(50 + lColumnCounter * iElementWidth + iElementWidth / 2));
            number.setAttributeNS(null, "y", String.valueOf(47));
            number.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            number.setAttributeNS(null, "text-anchor", "middle");
            Text numberText = doc.createTextNode(String.valueOf(s));
            number.appendChild(numberText);
            svgRoot.appendChild(number);

            lColumnCounter = lColumnCounter + 1;
            Element line = doc.createElementNS(svgNS, "path");
            line.setAttributeNS(null, "d",
                    "M  " + String.valueOf(2 + 49 + lColumnCounter * iElementWidth) + "," + String.valueOf(30) +
                            " L " + String.valueOf(2 + 49 + lColumnCounter * iElementWidth) + "," + String.valueOf(50));
            line.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
            svgRoot.appendChild(line);
        }

        // Paint amino acids. (along the y-axis)
        int lRowCounter = 0;
        AminoAcidEnum[] aa = iHeatMapDataSupplier.getRows();
        for (AminoAcidEnum anAa : aa) {
            Element number = doc.createElementNS(svgNS, "text");
            number.setAttributeNS(null, "x", String.valueOf(35));
            number.setAttributeNS(null, "y", String.valueOf(57 + lRowCounter * iElementHeight + iElementHeight / 2));
            number.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            number.setAttributeNS(null, "text-anchor", "middle");
            Text numberText = doc.createTextNode(String.valueOf(anAa.getOneLetterCode()));
            number.appendChild(numberText);
            svgRoot.appendChild(number);

            lRowCounter = lRowCounter + 1;
            Element line = doc.createElementNS(svgNS, "path");
            line.setAttributeNS(null, "d",
                    "M  " + String.valueOf(49 + lRowCounter * iElementHeight) + "," + String.valueOf(20) +
                            " L " + String.valueOf(49 + lRowCounter * iElementHeight) + "," + String.valueOf(50));

            line.setAttributeNS(null, "d",
                    " M " + String.valueOf(20) + "," + String.valueOf(2 + 49 + lRowCounter * iElementHeight) +
                            " L " + String.valueOf(49) + "," + String.valueOf(2 + 49 + lRowCounter * iElementHeight));

            line.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
            svgRoot.appendChild(line);
        }

        // Create the legend.
        // ------------------


        // Make the gradient half the height of the heatmap.
        int lNumberOfLines = iHeatMapHeight / 2;
        // Constant X coordinates.
        int lLegendXStart = 30 + (new Double(iNumberOfPositions * iElementWidth)).intValue() + 50;
        int lLegendXEnd = new Double(lLegendXStart + iElementWidth).intValue();


        // If the pvalue is 0.05 (=startvalue)
        // we want the innerticks at 0.025 and 0.975
        // and the outerticks at 0.0025 and 0.9975

        double lStartValue = MainInformationFeeder.getInstance().getHeatMapStartValue();
        double lEndValue = MainInformationFeeder.getInstance().getHeatMapEndValue();

        double lInnerNegativeTickQuantile = lStartValue / 2;
        lInnerNegativeTickQuantile = Math.round(lInnerNegativeTickQuantile * 100000.0) / 100000.0;
        double lInnerPositiveTickQuantile = 1 - (lStartValue / 2);
        lInnerPositiveTickQuantile = Math.round(lInnerPositiveTickQuantile * 100000.0) / 100000.0;

        double lOuterNegativeTickQuantile = lEndValue / 2;
        lOuterNegativeTickQuantile = Math.round(lOuterNegativeTickQuantile * 100000.0) / 100000.0;
        double lOuterPositiveTickQuantile = 1 - (lEndValue / 2);
        lOuterPositiveTickQuantile = Math.round(lOuterPositiveTickQuantile * 100000.0) / 100000.0;

        // Calcuate a factor unit increase for each line in the gradient.
        double lFactorUnit = ((iRenderer.getPositiveColorEnd() + 1) * 2) / lNumberOfLines;

        // Variables to run along the Y-axis.
        int lYRunner = iHeatMapHeight / 4;
        double lFactorRunner = iRenderer.getNegativeColorEnd() - 1; // Start with the negative upper boundary.

        boolean lInnerPositiveTickSet = false;
        double lInnerPositiveTickZValue = StatisticsConversion.inverseCumulativeProbability(lInnerPositiveTickQuantile);

        boolean lInnerNegativeTickSet = false;
        double lInnerNegativeTickZValue = StatisticsConversion.inverseCumulativeProbability(lInnerNegativeTickQuantile);

        boolean lOuterPositiveTickSet = false;
        double lOuterPositiveTickZValue = StatisticsConversion.inverseCumulativeProbability(lOuterPositiveTickQuantile);

        boolean lOuterNegativeTickSet = false;
        double lOuterNegativeTickZValue = StatisticsConversion.inverseCumulativeProbability(lOuterNegativeTickQuantile);

        // Distance for the ticks away from the gradient.
        int lTickIndent = 10;


        for (int lLineIndex = 0; lLineIndex < lNumberOfLines; lLineIndex++) {
            lYRunner++;
            lFactorRunner = lFactorRunner + lFactorUnit;

            Element line = doc.createElementNS(svgNS, "path");
            line.setAttributeNS(null, "d",
                    "M  " + String.valueOf(lLegendXStart) + "," + lYRunner +
                            " L " + String.valueOf(lLegendXEnd) + "," + lYRunner);

            String lColor = iRenderer.getColorAsHexadecimal(lFactorRunner);

            line.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:" + lColor + ";stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
            svgRoot.appendChild(line);

            if (lFactorRunner > lOuterNegativeTickZValue && !lOuterNegativeTickSet) {

                lOuterNegativeTickSet = true;

                Element number = doc.createElementNS(svgNS, "text");
                number.setAttributeNS(null, "x", String.valueOf(lLegendXEnd + lTickIndent));
                number.setAttributeNS(null, "y", String.valueOf(lYRunner));
                number.setAttributeNS(null, "style", "font-size:12px;font-style:oblique;fill:black;font-family:Tahoma");
                number.setAttributeNS(null, "text-anchor", "start");

                Text numberText = doc.createTextNode(String.valueOf(lOuterNegativeTickQuantile) + " --");
                number.appendChild(numberText);
                svgRoot.appendChild(number);

            } else if (lFactorRunner > lInnerNegativeTickZValue && !lInnerNegativeTickSet) {

                lInnerNegativeTickSet = true;

                Element number = doc.createElementNS(svgNS, "text");
                number.setAttributeNS(null, "x", String.valueOf(lLegendXEnd + lTickIndent));
                number.setAttributeNS(null, "y", String.valueOf(lYRunner));
                number.setAttributeNS(null, "style", "font-size:12px;font-style:oblique;fill:black;font-family:Tahoma");
                number.setAttributeNS(null, "text-anchor", "start");

                Text numberText = doc.createTextNode(String.valueOf(lInnerNegativeTickQuantile) + " --");
                number.appendChild(numberText);
                svgRoot.appendChild(number);

            } else if (lFactorRunner > lInnerPositiveTickZValue && !lInnerPositiveTickSet) {

                lInnerPositiveTickSet = true;

                Element number = doc.createElementNS(svgNS, "text");
                number.setAttributeNS(null, "x", String.valueOf(lLegendXEnd + lTickIndent));
                number.setAttributeNS(null, "y", String.valueOf(lYRunner));
                number.setAttributeNS(null, "style", "font-size:12px;font-style:oblique;fill:black;font-family:Tahoma");
                number.setAttributeNS(null, "text-anchor", "start");

                Text numberText = doc.createTextNode(String.valueOf(lInnerPositiveTickQuantile) + " ++");
                number.appendChild(numberText);
                svgRoot.appendChild(number);

            } else if (lFactorRunner > lOuterPositiveTickZValue && !lOuterPositiveTickSet) {

                lOuterPositiveTickSet = true;

                Element number = doc.createElementNS(svgNS, "text");
                number.setAttributeNS(null, "x", String.valueOf(lLegendXEnd + lTickIndent));
                number.setAttributeNS(null, "y", String.valueOf(lYRunner));
                number.setAttributeNS(null, "style", "font-size:12px;font-style:oblique;fill:black;font-family:Tahoma");
                number.setAttributeNS(null, "text-anchor", "start");

                Text numberText = doc.createTextNode(String.valueOf(lOuterPositiveTickQuantile) + " ++");
                number.appendChild(numberText);
                svgRoot.appendChild(number);

            }

        }

        /*/ Draw a title for the legend.
        Element number = doc.createElementNS(svgNS, "text");
        number.setAttributeNS(null, "x", String.valueOf(lLegendXEnd - (iElementWidth / 2)));
        number.setAttributeNS(null, "y", String.valueOf((iHeatMapHeight / 4) - 10));
        number.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
        number.setAttributeNS(null, "text-anchor", "middle");

        Text numberText = doc.createTextNode("Quantile");
        number.appendChild(numberText);
        svgRoot.appendChild(number);
        */

        // Paint the values
        String lXOffset;
        String lYOffset;


        // First dimension loops over the amino acids - along the y-axis
        for (lRowCounter = 0; lRowCounter < aa.length; lRowCounter++) {
            lYOffset = String.valueOf(53 + (lRowCounter) * iElementHeight);

            // Second dimension loops over the positions - along the x-axis
            for (lColumnCounter = 0; lColumnCounter < iNumberOfPositions; lColumnCounter++) {
                lXOffset = String.valueOf(52 + lColumnCounter * iElementWidth);

                Element lHeatMapCell = doc.createElementNS(svgNS, "rect");
                lHeatMapCell.setAttributeNS(null, "x", lXOffset); // The x- coordinate is the top of the first aminoacid!
                lHeatMapCell.setAttributeNS(null, "y", lYOffset);
                lHeatMapCell.setAttributeNS(null, "width", String.valueOf(iElementWidth - 2));
                lHeatMapCell.setAttributeNS(null, "height", String.valueOf(iElementHeight - 2));

                // Get the value from the HeatMapSupplier.
                double lValue = iHeatMapDataSupplier.getValueAt(lRowCounter, lColumnCounter);
                lHeatMapCell.setAttributeNS(null, "fill", iRenderer.getColorAsHexadecimal(StatisticsConversion.inverseCumulativeProbability(lValue)));
                svgRoot.appendChild(lHeatMapCell);
            }
        }

        this.setSVGDocument(doc);
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Observer ---------------------


    /**
     * An update is performed when something is changed in the observed object (MainInformationFeeder)
     *
     * @param o   The observed object
     * @param arg An argument
     */
    public void update(Observable o, Object arg) {
        if (arg != null && (arg.equals(ObservableEnum.NOTIFY_STATISTICAL) || arg.equals(ObservableEnum.NOTIFY_GRAPHABLE_FRAME_SIZE) || arg.equals(ObservableEnum.NOTIFY_START_POSITION))) {
            this.makeSVG();
        }
    }

// --------------------- Interface Savable ---------------------


    public boolean isSvg() {
        return true;
    }

    public boolean isChart() {
        return false;
    }

    public JPanel getContentPanel() {
        return null;
    }

    /**
     * This methods gives an SVG document.
     *
     * @return An SVG document
     */
    public SVGDocument getSVG() {
        return doc;
    }

    public String getTitle() {
        return "sequenceHeatMap";
    }

    public String getDescription() {
        return "Sequence logo for positive set";
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