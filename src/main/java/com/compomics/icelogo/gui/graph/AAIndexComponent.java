package com.compomics.icelogo.gui.graph;

import com.compomics.icelogo.core.aaindex.AAIndexParameterMatrix;
import com.compomics.icelogo.core.aaindex.AAIndexParameterResult;
import com.compomics.icelogo.core.data.AminoAcidCounter;
import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.ExperimentTypeEnum;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.gui.interfaces.Savable;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niklaas
 * Date: 26-feb-2009
 * Time: 14:26:00
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class creates a SVG document. It paint a graph with the values (mean, reference mean, confidence interval) for a specific Aa parameter matrix
 */
public class AAIndexComponent extends JSVGCanvas implements Observer, Savable {
    /*
    * The width of the panel where the SVG document will be created
    */
    public int iLogoWidth;
    /*
    * The height of the panel where the SVG document will be created
    */
    public int iLogoHeigth;
    /**
     * The number of elements
     */
    public int iLogoElements;
    /**
     * The start position
     */
    public int iStartPosition;
    /**
     * The width of on element (This is the (logo width - 100)/ logo elements)
     */
    public double iElementWidth;
    /**
     * The SVG document
     */
    public SVGDocument doc;
    /**
     * The matrix data model
     */
    public MatrixDataModel iDataModel;
    /**
     * The standard deviation used for the creation of this graph
     */
    public double iStandardDeviation;
    /**
     * The information feeder
     */
    public MainInformationFeeder iInformationFeeder;
    /**
     * The Aa parameter matrix for this graph
     */
    private AAIndexParameterMatrix iAaParameterMatrix;
    /**
     * A vector with the calculated results for the aa parameter matrix
     */
    private Vector<AAIndexParameterResult> iAaParameterResults = new Vector<AAIndexParameterResult>();
    /**
     * The maximum value that can be displayed on the graph
     */
    private double iMax = -99999999999.0;
    /**
     * The minimum value that can be displayed on the graph
     */
    private double iMin = 99999999999.0;
    /**
     * A boolean that indicates if a sliding window must be used
     */
    private boolean iUseSlidingWindow;
    /**
     * Int with the size of the sliding window
     */
    private int iSlidingWindowSize;
    /**
     * indicates if this panel is updating
     */
    private boolean iUpdating;
    /**
     * boolean that indicates that we work with two sets
     */
    private boolean lUseTwoSets = false;

    /**
     * The Constructor
     *
     * @param aDataModel MatrixDataModel.
     */
    public AAIndexComponent(MatrixDataModel aDataModel) {

        //set the number of elements
        this.iLogoElements = aDataModel.getNumberOfPositions();
        //get an instance of the information feeder
        this.iInformationFeeder = MainInformationFeeder.getInstance();
        //add this as an observer to the information feeder
        iInformationFeeder.addObserver(this);
        //set the datamodel
        this.iDataModel = aDataModel;
        //create the svg document
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
        this.iAaParameterMatrix = iInformationFeeder.getSelectedAaParameterMatrix();
        this.iSlidingWindowSize = iInformationFeeder.getSlidingWindowSize();
        this.iUseSlidingWindow = iInformationFeeder.isSlidingWindowInAaParameter();
        this.iLogoHeigth = iInformationFeeder.getGraphableHeight();
        this.iLogoWidth = iInformationFeeder.getGraphableWidth();
    }

    /**
     * This method calculates the AaParameterResult for the different positions.
     * The results are stored in the iAaParameterResults vector.
     */
    public void calculate() {
        //set the maximum and minimum
        iMax = -99999999999.0;
        iMin = 99999999999.0;
        //clear the results vector
        iAaParameterResults.removeAllElements();

        //calculate the AAIndexParameterResult for every position
        for (int p = 0; p < iDataModel.getNumberOfPositions(); p++) {
            //get the Positive AminoAcidStatistics
            AminoAcidStatistics lExperimentalMatrix = iDataModel.getExperimentalAminoAcidStatistics(p, ExperimentTypeEnum.EXPERIMENT);
            AminoAcidStatistics lTwoExperimentalMatrix = iDataModel.getExperimentalAminoAcidStatistics(p, ExperimentTypeEnum.EXPERIMENT_TWO);
            if (lTwoExperimentalMatrix != null) {
                lUseTwoSets = true;
            }
            //get the Reference AminoAcidStatistics
            AminoAcidStatistics lReferenceMatrix = iDataModel.getReferenceAminoAcidStatistics(p);

            //Get the number of aminoacids used for that position
            int lPosSetSize = (int) lExperimentalMatrix.getStatistics(AminoAcidEnum.ALA).getN();
            int lTwoPosSetSize = 0;
            if (lUseTwoSets) {
                lTwoPosSetSize = (int) lTwoExperimentalMatrix.getStatistics(AminoAcidEnum.ALA).getN();
            }
            int lRefSetSize = (int) lReferenceMatrix.getStatistics(AminoAcidEnum.ALA).getN();
            //Always use the smallest set to do you statistics on
            int lN = 0;
            if (lPosSetSize > lRefSetSize) {
                //the reference set is the smallest set
                lN = lRefSetSize;
            } else {
                //the positive set is the smallest set
                lN = lPosSetSize;
            }
            if (lUseTwoSets && lN > lTwoPosSetSize) {
                lN = lTwoPosSetSize;
            }

            //calculate a mean for the reference set and the positive set
            double lPositionMean = 0.0;
            double lTwoPositionMean = 0.0;
            double lReferenceMean = 0.0;
            for (AminoAcidEnum aa : AminoAcidEnum.values()) {
                //get the StatisticalSummary for this aa from the positive set
                StatisticalSummary lPosStat = lExperimentalMatrix.getStatistics(aa);
                //add the value for this aa to the mean
                lPositionMean = lPositionMean + lPosStat.getMean() * (Double) iAaParameterMatrix.getValueForAminoAcid(aa);
                if (lUseTwoSets) {
                    //get the StatisticalSummary for this aa from the positive set
                    StatisticalSummary lTwoPosStat = lTwoExperimentalMatrix.getStatistics(aa);
                    //add the value for this aa to the mean
                    lTwoPositionMean = lTwoPositionMean + lTwoPosStat.getMean() * (Double) iAaParameterMatrix.getValueForAminoAcid(aa);
                }
            }

            //we should calculate the mean on different samples from the dataset
            //if it's a FixedAminoAcidStatistics there is only one dimension
            //so we have the create 100 random peptides with the lenght the number of aminoacids in the set
            //this DescriptiveStatistics will store the different means
            DescriptiveStatistics lReferenceMeans = new DescriptiveStatistics();
            if (lReferenceMatrix.getDimension() == 1) {
                for (int i = 0; i < 100; i++) {
                    AminoAcidEnum[] lAas = lReferenceMatrix.getRandomPeptide(lN);
                    double lSum = 0.0;
                    for (int j = 0; j < lAas.length; j++) {
                        lSum = lSum + (Double) iAaParameterMatrix.getValueForAminoAcid(lAas[j]);
                    }
                    //add the mean to the collection of means
                    lReferenceMeans.addValue(lSum / (double) lAas.length);
                }
            } else {
                MatrixAminoAcidStatistics lReferenceMatrixStatistics = (MatrixAminoAcidStatistics) lReferenceMatrix;
                for (int i = 0; i < lReferenceMatrixStatistics.getDimension(); i++) {
                    AminoAcidCounter lCounter = lReferenceMatrixStatistics.getAminoAcidCounter(i);
                    double lSum = 0.0;
                    for (AminoAcidEnum aa : AminoAcidEnum.values()) {
                        for (int j = 0; j < lCounter.getCount(aa); j++) {
                            lSum = lSum + (Double) iAaParameterMatrix.getValueForAminoAcid(aa);
                        }
                    }
                    //add the mean to the collection of means
                    lReferenceMeans.addValue(lSum / lCounter.getTotalCount());
                }
            }

            //set the values from the DescriptiveStatistics
            lReferenceMean = lReferenceMeans.getMean();
            double lStandardDeviation = lReferenceMeans.getStandardDeviation();

            //Check if the max and min are still correct
            if (lPositionMean > iMax) {
                iMax = lPositionMean;
            }
            if (lUseTwoSets && lTwoPositionMean > iMax) {
                iMax = lTwoPositionMean;
            }
            if (lReferenceMean > iMax) {
                iMax = lReferenceMean;
            }
            if (lPositionMean < iMin) {
                iMin = lPositionMean;
            }
            if (lUseTwoSets && lTwoPositionMean < iMin) {
                iMin = lTwoPositionMean;
            }
            if (lReferenceMean < iMin) {
                iMin = lReferenceMean;
            }
            //create the AAIndexParameterResult
            AAIndexParameterResult lResult = new AAIndexParameterResult(lPositionMean, lReferenceMean, lStandardDeviation, p);
            if (lUseTwoSets) {
                lResult.setCalulatedMeanSetTwo(lTwoPositionMean);
            }
            //Check if the max and min are still correct
            if (lResult.getUpperConfidenceLimit() > iMax) {
                iMax = lResult.getUpperConfidenceLimit();
            }
            if (lResult.getLowerConfidenceLimit() < iMin) {
                iMin = lResult.getLowerConfidenceLimit();
            }

            iAaParameterResults.add(lResult);
        }

        //Make the max and min just a little bit bigger (10%)
        double lDiff = iMax - iMin;
        iMax = Math.round((iMax + lDiff / 10.0) * 100.0) / 100.0;
        iMin = Math.round((iMin - lDiff / 10.0) * 100.0) / 100.0;
        //check if a sliding window is wanted
        if (iUseSlidingWindow) {
            useSlidingWindow();
        }
    }


    /**
     * This method will do the calculations if a sliding window is needed.
     * It will get the AaParameterResults from the iAaParameterResults Vector.
     * It will store the results also in the iAaParameterResults Vector.
     */
    public void useSlidingWindow() {
        //create a temporary AAIndexParameterResult Vector
        Vector<AAIndexParameterResult> lTemp = new Vector<AAIndexParameterResult>();

        //find the position where the window must start
        //if the sliding window size is 3 this position must be 2, for 4 it's 2
        int lSlidingWindowStart = 0;
        if (iSlidingWindowSize % 2 == 0) {
            lSlidingWindowStart = iSlidingWindowSize / 2;
        } else {
            lSlidingWindowStart = (iSlidingWindowSize + 1) / 2;
        }

        //here will the calculation be done for every position
        for (int i = 0; i < iAaParameterResults.size(); i++) {
            //create temporary calculated and reference means and a standard deviation
            double lTempCalMean = 0.0;
            double lTempRefMean = 0.0;
            double lTempSD = 0.0;
            double lTwoTempCalMean = 0.0;
            double lTwoTempRefMean = 0.0;
            double lTwoTempSD = 0.0;
            //the number of combined AaParameterResults
            int lNumberOfSummedResults = 0;

            for (int j = 1; j <= iSlidingWindowSize; j++) {
                //the result position that will be used
                int lResultPosition = i - (lSlidingWindowStart - j);
                if (lResultPosition >= 0 && lResultPosition <= iAaParameterResults.size() - 1) {
                    //we can find that position
                    //add the values from the AAIndexParameterResult to the temporary values
                    AAIndexParameterResult lResult = iAaParameterResults.get(lResultPosition);
                    lTempCalMean = lTempCalMean + lResult.getCalulatedMean();
                    lTempRefMean = lTempRefMean + lResult.getNegativeSetMean();
                    lTempSD = lTempSD + lResult.getStandardDeviation();
                    if (lUseTwoSets) {
                        lTwoTempCalMean = lTwoTempCalMean + lResult.getCalulatedMeanSetTwo();
                        lTwoTempRefMean = lTwoTempRefMean + lResult.getNegativeSetMean();
                        lTwoTempSD = lTwoTempSD + lResult.getStandardDeviation();
                    }
                    lNumberOfSummedResults = lNumberOfSummedResults + 1;
                }
            }
            //add this calculated sliding window position to the temporary Vector
            AAIndexParameterResult lResult = new AAIndexParameterResult(lTempCalMean / (double) lNumberOfSummedResults, lTempRefMean / (double) lNumberOfSummedResults, lTempSD / (double) lNumberOfSummedResults, i);
            if (lUseTwoSets) {
                lResult.setCalulatedMeanSetTwo(lTwoTempCalMean / (double) lNumberOfSummedResults);
            }
            lTemp.add(lResult);
        }
        //Save the results in the iAaParameterResults Vector
        iAaParameterResults = lTemp;
    }


    /**
     * This method "paints" the logo in a SVG document.
     */
    public void makeSVG() {

        if (!iUpdating) {
            iUpdating = true;
            //get the info from the MainInformationFeeder
            this.getInfo();
            //calculate the values for the different positions
            this.calculate();
            //calculate the width of every element
            iElementWidth = (iLogoWidth - 100) / iLogoElements;

            //create the SVG document
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
            Text markerText1 = doc.createTextNode(String.valueOf(iMax));
            marker1.appendChild(markerText1);
            svgRoot.appendChild(marker1);

            Element marker3 = doc.createElementNS(svgNS, "text");
            marker3.setAttributeNS(null, "x", "20");
            marker3.setAttributeNS(null, "y", String.valueOf(70 + (iLogoHeigth - 70 - 50) / 2));
            marker3.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            marker3.setAttributeNS(null, "text-anchor", "middle");
            Text markerText3 = doc.createTextNode(String.valueOf(Math.round((iMax - (iMax - iMin) / 2) * 100.0) / 100.0));
            marker3.appendChild(markerText3);
            svgRoot.appendChild(marker3);

            //paint numbers
            int startNumber = iStartPosition;
            int elementCount = 0;
            for (int s = startNumber; s < startNumber + iLogoElements; s++) {
                //paint the number
                Element number = doc.createElementNS(svgNS, "text");
                number.setAttributeNS(null, "x", String.valueOf(50 + elementCount * iElementWidth + iElementWidth / 2));
                number.setAttributeNS(null, "y", String.valueOf(iLogoHeigth - 35));
                number.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
                number.setAttributeNS(null, "text-anchor", "middle");
                Text numberText = doc.createTextNode(String.valueOf(s));
                number.appendChild(numberText);
                svgRoot.appendChild(number);

                //paint a line the enclose the number that is painted
                elementCount = elementCount + 1;
                Element line = doc.createElementNS(svgNS, "path");
                line.setAttributeNS(null, "d", "M  " + String.valueOf(49 + elementCount * iElementWidth) + "," + String.valueOf(iLogoHeigth - 30) + " L " + String.valueOf(49 + elementCount * iElementWidth) + "," + String.valueOf(iLogoHeigth - 50));
                line.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
                svgRoot.appendChild(line);
            }

            //add the created SVG object to the SVG root
            svgRoot.appendChild(yAxis);
            svgRoot.appendChild(xAxis1);
            svgRoot.appendChild(xAxis2);
            svgRoot.appendChild(top);
            svgRoot.appendChild(markerLine1);
            svgRoot.appendChild(markerLine2);

            //paint the sliding window size label if it is used
            if (iUseSlidingWindow) {
                Element titleAxis2 = doc.createElementNS(svgNS, "text");
                titleAxis2.setAttributeNS(null, "x", String.valueOf((iLogoHeigth / -2) + (iLogoHeigth / -4)));
                titleAxis2.setAttributeNS(null, "y", "35");
                titleAxis2.setAttributeNS(null, "transform", "matrix(0,-1,1,0,0,0)");
                titleAxis2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
                titleAxis2.setAttributeNS(null, "text-anchor", "middle");
                Text data2 = doc.createTextNode("Sliding window size: " + iSlidingWindowSize);
                titleAxis2.appendChild(data2);
                svgRoot.appendChild(titleAxis2);
            }

            //paint the title
            Element titleAxis2 = doc.createElementNS(svgNS, "text");
            titleAxis2.setAttributeNS(null, "x", String.valueOf(iLogoWidth / 2));
            titleAxis2.setAttributeNS(null, "y", "25");
            titleAxis2.setAttributeNS(null, "style", "font-size:14px;fill:black;font-family:Arial");
            titleAxis2.setAttributeNS(null, "text-anchor", "middle");
            Text data2 = doc.createTextNode(iAaParameterMatrix.getTitle());
            titleAxis2.appendChild(data2);
            svgRoot.appendChild(titleAxis2);

            //create a double with the maximum vertical height
            double lMaxVerticalHeight = (double) iLogoHeigth - 100.0;
            //create a double with the difference between the max and min
            double lMaxDiff = iMax - iMin;
            //create string where the different point of the lines will be saved in
            String lConfidenceIntervalString = "";
            String lPositionString = "";
            String lReferenceString = "";

            //add the upper confidence limits to the lConfidenceIntervalString
            for (int p = 0; p < iAaParameterResults.size(); p++) {
                //calculate the x start position
                double elementStartX = 50.0 + p * iElementWidth + iElementWidth / 2;
                //the difference between the negative set mean and the minimun
                double lDiff = iAaParameterResults.get(p).getUpperConfidenceLimit() - iMin;
                //the percentage of the lDiff of the whole lMaxDiff
                double lDiffPerc = lDiff / lMaxDiff;
                //calculate the y start position
                double elementStartY = (double) iLogoHeigth - 50.0 - (lMaxVerticalHeight * lDiffPerc);
                if (p == 0) {
                    //if it's the first point it must start with an M
                    lConfidenceIntervalString = "M  " + String.valueOf(elementStartX) + "," + elementStartY;
                } else {
                    lConfidenceIntervalString = lConfidenceIntervalString + " L " + String.valueOf(elementStartX) + "," + elementStartY;
                }
            }

            //add the lower confidence limits to the lConfidenceIntervalString
            for (int p = 0; p < iAaParameterResults.size(); p++) {
                //calculate the x start position
                double elementStartX = 50.0 + (iAaParameterResults.size() - 1 - p) * iElementWidth + iElementWidth / 2;
                //the difference between the negative set mean and the minimun
                double lDiff = iAaParameterResults.get(iAaParameterResults.size() - 1 - p).getLowerConfidenceLimit() - iMin;
                //the percentage of the lDiff of the whole lMaxDiff
                double lDiffPerc = lDiff / lMaxDiff;
                //calculate the y start position
                double elementStartY = (double) iLogoHeigth - 50.0 - (lMaxVerticalHeight * lDiffPerc);
                lConfidenceIntervalString = lConfidenceIntervalString + " L " + String.valueOf(elementStartX) + "," + elementStartY;
            }
            //close the line with a "z"
            lConfidenceIntervalString = lConfidenceIntervalString + " z ";

            //add this line to the root
            Element lConfidence = doc.createElementNS(svgNS, "path");
            lConfidence.setAttributeNS(null, "d", lConfidenceIntervalString);
            lConfidence.setAttributeNS(null, "style", "fill:#ed00b2;fill-rule:evenodd;stroke:#000000;stroke-width:0.9;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;fill-opacity:1;opacity:0.27;stroke-miterlimit:4;stroke-dasharray:none");
            svgRoot.appendChild(lConfidence);

            //add the position means to the string
            for (int p = 0; p < iAaParameterResults.size(); p++) {
                //calculate the x start position
                double elementStartX = 50.0 + p * iElementWidth + iElementWidth / 2;
                //the difference between the negative set mean and the minimun
                double lDiff = iAaParameterResults.get(p).getCalulatedMean() - iMin;
                //the percentage of the lDiff of the whole lMaxDiff
                double lDiffPerc = lDiff / lMaxDiff;
                //calculate the y start position
                double elementStartY = (double) iLogoHeigth - 50.0 - (lMaxVerticalHeight * lDiffPerc);
                if (p == 0) {
                    lPositionString = "M  " + String.valueOf(elementStartX) + "," + elementStartY;
                } else {
                    lPositionString = lPositionString + " L " + String.valueOf(elementStartX) + "," + elementStartY;
                }
            }

            //add this line to the root
            Element lPositionMeans = doc.createElementNS(svgNS, "path");
            lPositionMeans.setAttributeNS(null, "d", lPositionString);
            lPositionMeans.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#00ff00;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;stroke-miterlimit:4;stroke-dasharray:none");
            svgRoot.appendChild(lPositionMeans);


            if (lUseTwoSets) {
                //add the position means to the string
                for (int p = 0; p < iAaParameterResults.size(); p++) {
                    //calculate the x start position
                    double elementStartX = 50.0 + p * iElementWidth + iElementWidth / 2;
                    //the difference between the negative set mean and the minimun
                    double lDiff = iAaParameterResults.get(p).getCalulatedMeanSetTwo() - iMin;
                    //the percentage of the lDiff of the whole lMaxDiff
                    double lDiffPerc = lDiff / lMaxDiff;
                    //calculate the y start position
                    double elementStartY = (double) iLogoHeigth - 50.0 - (lMaxVerticalHeight * lDiffPerc);
                    if (p == 0) {
                        lPositionString = "M  " + String.valueOf(elementStartX) + "," + elementStartY;
                    } else {
                        lPositionString = lPositionString + " L " + String.valueOf(elementStartX) + "," + elementStartY;
                    }
                }

                //add this line to the root
                Element lTwoPositionMeans = doc.createElementNS(svgNS, "path");
                lTwoPositionMeans.setAttributeNS(null, "d", lPositionString);
                lTwoPositionMeans.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;stroke-miterlimit:4;stroke-dasharray:none");
                svgRoot.appendChild(lTwoPositionMeans);
            }

            //add the reference means to the string
            for (int p = 0; p < iAaParameterResults.size(); p++) {
                //calculate the x start position
                double elementStartX = 50.0 + p * iElementWidth + iElementWidth / 2;
                //the difference between the negative set mean and the minimun
                double lDiff = iAaParameterResults.get(p).getNegativeSetMean() - iMin;
                //the percentage of the lDiff of the whole lMaxDiff
                double lDiffPerc = lDiff / lMaxDiff;
                //calculate the y start position
                double elementStartY = (double) iLogoHeigth - 50.0 - (lMaxVerticalHeight * lDiffPerc);
                if (p == 0) {
                    lReferenceString = "M  " + String.valueOf(elementStartX) + "," + elementStartY;
                } else {
                    lReferenceString = lReferenceString + " L " + String.valueOf(elementStartX) + "," + elementStartY;
                }
            }
            //add this line to the root
            Element lReferenceMeans = doc.createElementNS(svgNS, "path");
            lReferenceMeans.setAttributeNS(null, "d", lReferenceString);
            lReferenceMeans.setAttributeNS(null, "style", "fill:none;fill-rule:evenodd;stroke:#ff0000;stroke-width:3;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;stroke-miterlimit:4;stroke-dasharray:none");
            svgRoot.appendChild(lReferenceMeans);

            //set the document
            this.setSVGDocument(doc);
            iUpdating = false;
        }
    }

    /**
     * An update is performed when something is changed in the observed object (MainInformationFeeder)
     *
     * @param o   The observed object
     * @param arg An argument
     */
    public void update(Observable o, Object arg) {
        //only if one of the following things is changed create a new graph
        if (arg != null && (arg.equals(ObservableEnum.NOTIFY_AA_PARAMETER) || arg.equals(ObservableEnum.NOTIFY_STATISTICAL) || arg.equals(ObservableEnum.NOTIFY_GRAPHABLE_FRAME_SIZE) || arg.equals(ObservableEnum.NOTIFY_START_POSITION))) {
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
        return "aaParameter";
    }

    public String getDescription() {
        return "Graph with the aa parameter";
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
