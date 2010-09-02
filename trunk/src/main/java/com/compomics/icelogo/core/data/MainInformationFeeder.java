/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 6-nov-2008
 * Time: 13:44:34
 */
package com.compomics.icelogo.core.data;

import com.compomics.icelogo.core.aaindex.AAIndexMatrix;
import com.compomics.icelogo.core.aaindex.AAIndexParameterMatrix;
import com.compomics.icelogo.core.aaindex.AAIndexReader;
import com.compomics.icelogo.core.aaindex.AAIndexSubstitutionMatrix;
import com.compomics.icelogo.core.data.sequenceset.RawSequenceSet;
import com.compomics.icelogo.core.dbComposition.CompositionExtractor;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;
import com.compomics.icelogo.core.enumeration.*;
import com.compomics.icelogo.core.factory.AminoAcidFactory;
import com.compomics.icelogo.core.interfaces.ISequenceSet;
import com.compomics.icelogo.core.klogo.SequenceClusterer;
import com.compomics.icelogo.core.stat.StatisticsConversion;
import com.compomics.icelogo.gui.component.Messenger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Properties;
import java.util.Vector;


/**
 * This singleton holds all the necessary information for the logos and panels. Whenever a value is changed the
 * "Observers" will be notified.
 */
public class MainInformationFeeder extends Observable {
// ------------------------------ FIELDS ------------------------------

    /**
     * The singleton instance
     */
    private static MainInformationFeeder ourInstance = new MainInformationFeeder();
    /**
     * The color for every amino acid is stored in this color scheme
     */
    private ColorScheme iColorScheme = new ColorScheme();
    /**
     * The height of the logo
     */
    private int iGraphableHeight = 400;
    /**
     * The width of the logo
     */
    private int iGraphableWidth = 300;
    /**
     * The P value
     */
    private double iPvalue = 0.05;
    /**
     * The Z score
     */
    private double iZscore = 1.96;
    /**
     * The start position
     */
    private int iStartPosition = 1;
    /**
     * The value reflects the maximum height of the Y axis in a logo
     */
    private int iYaxisValue = 10;
    /**
     * The scoring type
     */
    private ScoringTypeEnum iScoringType = ScoringTypeEnum.PERCENTAGE;
    /**
     * If the weblogo must be filled this boolean is true
     */
    private boolean iFillWeblogo = false;
    /**
     * If the weblogo must be constructed with a negative set correction this boolean is set to true
     */
    private boolean iWeblogoNegativeCorrection = false;
    /**
     * Vector with the different AaParameterMatrices from the aaindex1 file read by the AaParameterReader
     */
    private Vector<AAIndexMatrix> iAaParameterMatrixes = null;
    /**
     * The selected AaParameterMatrix
     */
    private AAIndexMatrix iSelectedAaParameterMatrix = null;
    /**
     * boolean that indicates if a sliding window is wanted in the AaParameterComponent
     */
    private boolean iSlidingWindowInAaParameter = false;
    /**
     * int with the size of the sliding window
     */
    private int iSlidingWindowSize = 3;
    /**
     * The height of the logo
     */
    private int iSubLogoHeight = 400;
    /**
     * The width of the logo
     */
    private int iSubLogoWidht = 300;
    /**
     * boolean that indicates if we have to create the icoLogo
     */
    private boolean iUseIceLogo;
    /**
     * boolean that indicates if we have to create the barchart
     */
    private boolean iUseBarchart;
    /**
     * boolean that indicates if we have to create the heatmap
     */
    private boolean iUseHeatmap = true;
    /**
     * boolean that indicates if we have to create the sequenceLogo
     */
    private boolean iUseSequenceLogo;
    /**
     * boolean that indicates if we have to create the subLogo
     */
    private boolean iUseSubLogo;

    /**
     * boolean that indicates if we have to create the aa parameter matrix graph
     */
    private boolean iUseAaParameterGraph;

        /**
     * boolean that indicates if we have to create the conservation logo
     */
    private boolean iUseConservationLogo;
    /**
     * Boolean to indicate whether to use the KLogo graph.
     */
    private boolean iUseKlogo = true;

    /**
     * The icelogo type
     */
    private IceLogoEnum iIceLogoEnum;

    /**
     * This list keeps a link to all fasta files that have been used.
     */
    private ArrayList<File> iFastaFiles = null;

    /**
     * The variable for the anchor direction. Default from n to c terminus.
     */
    private SamplingDirectionEnum iReferenceDirection = SamplingDirectionEnum.NtermToCterm;

    /**
     * The number of itarations for the sampling to calculate the statistics.
     */
    private int iIterationSize = 50;

    /**
     * The number of amonoacids for the horizontal sampling stretch.
     */
    private int iHorizontalSamplingLength = 5;



    /**
     * The used substitution matrix
     */
    private AAIndexSubstitutionMatrix iSubstitutionMatrix;


    private ArrayList<SwissProtComposition> iSpCompositions = new ArrayList<SwissProtComposition>();

    /**
     * Integer 'anchor' index for the position set.
     */
    private int iExperimentAnchorValue = 0;

    /**
     * The first position SequenceSet.
     */
    private RawSequenceSet iExperimentSequenceSet = new RawSequenceSet(ExperimentTypeEnum.EXPERIMENT.getName());

    /**
     * The second position SequenceSet.
     */
    private RawSequenceSet iExperimentSequenceSetTwo = new RawSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO.getName());

    /**
     * This variable tracks the active fasta reference set.
     */
    private ISequenceSet iActiveReferenceSet;

    private int iNotifiesRunning = 0;


    private boolean iConservationLineNormalized = true;

    private boolean iUpdate = true;


    /**
     * The type of Sampling that is performed on the fasta database.
     */
    private SamplingTypeEnum iSamplingType = SamplingTypeEnum.RANDOM;

    /**
     * Vector with the different AaParameterMatrices from the aaindex2 file read by the AaParameterReader
     */
    private Vector<AAIndexMatrix> iSubstitutionMatrices = null;

    /**
     * This variable holds the sampling size. Default set at 30 for the central limit theorem.
     */
    private int iPositionSampleSize = 30;
    private boolean boolTwoExperiment = false;
    private int iHeatMapMagnitude = 2;


// -------------------------- STATIC METHODS --------------------------

    public static MainInformationFeeder getInstance() {
        return ourInstance;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    private MainInformationFeeder() {
        loadProperties();
    }

    /**
     * Load the properties from icelogo.properties.
     */
    public void loadProperties() {
        // Read properties file.
        Properties properties = new Properties();

        try {
            // Attempt to load the properties.
            properties.load(ClassLoader.getSystemResourceAsStream("icelogo.properties"));

            // File 1.
            String lValue;
            lValue = String.valueOf(properties.get(LogoProperties.FASTAFILE_1.toString()));

            File lFile = null;
            if (lValue != null) {
                lFile = new File(lValue);
                if (lFile.exists()) {
                    addFastaFile(lFile);
                }
            }

            // File 2.
            lValue = String.valueOf(properties.get(LogoProperties.FASTAFILE_2.toString()));
            if (lValue != null) {
                lFile = new File(lValue);
                if (lFile.exists()) {
                    addFastaFile(lFile);
                }
            }

            // File 3.
            lValue = String.valueOf(properties.get(LogoProperties.FASTAFILE_3.toString()));
            if (lValue != null) {
                lFile = new File(lValue);
                if (lFile.exists()) {
                    addFastaFile(lFile);
                }
            }

            // Parse pvalue.
            lValue = String.valueOf(properties.get(LogoProperties.PVALUE.toString()));
            if (!lValue.equals("null")) {
                iPvalue = Double.parseDouble(lValue);
            }

            // Parse orders of magnitude in the heatmap.
            lValue = String.valueOf(properties.get(LogoProperties.HEATMAP_MAGNITUDE.toString()));
            if (!lValue.equals("null")) {
                iHeatMapMagnitude = Integer.parseInt(lValue);
            }

            // Parse iterationsize.
            lValue = String.valueOf(properties.get(LogoProperties.ITERATIONSIZE.toString()));
            if (!lValue.equals("null")) {
                iIterationSize = Integer.parseInt(lValue);
            }

            // Parse startposition.
            lValue = String.valueOf(properties.get(LogoProperties.STARTPOSITION.toString()));
            if (!lValue.equals("null")) {
                iStartPosition = Integer.parseInt(lValue);
            }

            // Find the static species
            // first clear the vector
            iSpCompositions.clear();
            //STATIC_SPECIES 1
            lValue = String.valueOf(properties.get(LogoProperties.STATIC_SPECIES1.toString()));
            if (!lValue.equals("null")) {
                iSpCompositions.add(new SwissProtComposition(lValue.substring(0, lValue.indexOf("_|_")), lValue.substring(lValue.indexOf("_|_") + 3)));
            }
            //STATIC_SPECIES 2
            lValue = String.valueOf(properties.get(LogoProperties.STATIC_SPECIES2.toString()));
            if (!lValue.equals("null")) {
                iSpCompositions.add(new SwissProtComposition(lValue.substring(0, lValue.indexOf("_|_")), lValue.substring(lValue.indexOf("_|_") + 3)));
            }
            //STATIC_SPECIES 3
            lValue = String.valueOf(properties.get(LogoProperties.STATIC_SPECIES3.toString()));
            if (!lValue.equals("null")) {
                iSpCompositions.add(new SwissProtComposition(lValue.substring(0, lValue.indexOf("_|_")), lValue.substring(lValue.indexOf("_|_") + 3)));
            }
            //STATIC_SPECIES 4
            lValue = String.valueOf(properties.get(LogoProperties.STATIC_SPECIES4.toString()));
            if (!lValue.equals("null")) {
                iSpCompositions.add(new SwissProtComposition(lValue.substring(0, lValue.indexOf("_|_")), lValue.substring(lValue.indexOf("_|_") + 3)));
            }
            //STATIC_SPECIES 5
            lValue = String.valueOf(properties.get(LogoProperties.STATIC_SPECIES5.toString()));
            if (!lValue.equals("null")) {
                iSpCompositions.add(new SwissProtComposition(lValue.substring(0, lValue.indexOf("_|_")), lValue.substring(lValue.indexOf("_|_") + 3)));
            }
            //STATIC_SPECIES 6
            lValue = String.valueOf(properties.get(LogoProperties.STATIC_SPECIES6.toString()));
            if (!lValue.equals("null")) {
                iSpCompositions.add(new SwissProtComposition(lValue.substring(0, lValue.indexOf("_|_")), lValue.substring(lValue.indexOf("_|_") + 3)));
            }

            //read the composition file and make the compositions complete
            CompositionExtractor lExtractor = new CompositionExtractor(iSpCompositions, this);
            this.clearSwissProtComposition();
            lExtractor.addToMainInformationFeeder();


        } catch (IOException e) {
            // Failed to load params from classpath, don't mind because it will be created asap.
        } catch (NullPointerException e) {
            // Failed to load params from classpath, don't mind because it will be created asap.
        }
    }

    /**
     * Add a fasta file to the list.
     *
     * @return
     */
    public void addFastaFile(File aFile) {
        if (iFastaFiles == null) {
            iFastaFiles = new ArrayList<File>();
        }
        // List max three files ..
        // If there are allready 3 files,
        if (iFastaFiles.size() > 2) {
            // Remove the third file ..
            iFastaFiles.remove(2);
        }
        // Insert the new file in leading position.
        iFastaFiles.add(0, aFile);
        storeProperties();
        update(ObservableEnum.NOTIFY_FASTA_COMBOBOX);
    }

    /**
     * Add a SwissProtComposition to the list.
     */
    public void addSwissProtComposition(SwissProtComposition aSpComposition) {
        // List max six files ..
        // If there are allready 6 files,
        if (iSpCompositions.size() > 5) {
            // Remove the sixed file ..
            iSpCompositions.remove(5);
        }
        // Insert the new file in leading position.
        iSpCompositions.add(0, aSpComposition);
        update(ObservableEnum.NOTIFY_SPECIES_COMBOBOX);
        storeProperties();
    }

    /**
     * This method clears the SwissProtComposition ArrayList
     */
    public void clearSwissProtComposition() {
        iSpCompositions.clear();
    }

    /**
     * Store the properties into icelogo.properties.
     */
    public void storeProperties() {
        // Read properties file.
        Properties properties = new Properties();
        if (iFastaFiles != null) {
            File lFile;

            if (iFastaFiles.size() > 0) {
                lFile = iFastaFiles.get(0);
                if (lFile != null) {
                    properties.put(LogoProperties.FASTAFILE_1.toString(), lFile.getAbsolutePath());
                }
            }

            if (iFastaFiles.size() > 1) {
                lFile = iFastaFiles.get(1);
                if (lFile != null) {
                    properties.put(LogoProperties.FASTAFILE_2.toString(), lFile.getAbsolutePath());
                }
            }

            if (iFastaFiles.size() > 2) {
                lFile = iFastaFiles.get(2);
                if (lFile != null) {
                    properties.put(LogoProperties.FASTAFILE_3.toString(), lFile.getAbsolutePath());
                }
            }
        }
        properties.put(LogoProperties.PVALUE.toString(), String.valueOf(iPvalue));
        properties.put(LogoProperties.HEATMAP_MAGNITUDE.toString(), String.valueOf(iHeatMapMagnitude));
        properties.put(LogoProperties.STARTPOSITION.toString(), String.valueOf(iStartPosition));
        properties.put(LogoProperties.ITERATIONSIZE.toString(), String.valueOf(iIterationSize));
        for (int i = 0; i < iSpCompositions.size(); i++) {
            properties.put("STATIC_SPECIES" + (i + 1), String.valueOf(iSpCompositions.get(i).getSpecieLink() + "_|_" + iSpCompositions.get(i).getSpecieName()));
        }

        // Write properties file.
        try {
            String lPath = getParentPathToConfiguration();
            properties.store(new FileOutputStream(lPath + "icelogo.properties", false), "icelogo properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the parent path where the configuration files reside. Example /home/kenny/applications/icelogo/conf/
     * C:\program files\icelogo\conf\ Note, the path includes the trailing slash!
     *
     * @return
     */
    public String getParentPathToConfiguration() {
        // We will trust on the aaindex1 file as this should always reside in the wanted location.
        URL url = ClassLoader.getSystemResource("aaindex1");
        String lPathToFile = url.getPath();
        lPathToFile = lPathToFile.replace("%20", " ");
        int lEnd = 1;
        String lOS = getOsName();
        if (lOS.equals("mac") || lOS.equals("unix")) {
            lEnd = lPathToFile.lastIndexOf('/') + 1;
        } else if (lOS.equals("windows")) {
            lEnd = lPathToFile.lastIndexOf("/") + 1;
        }

        return lPathToFile.substring(0, lEnd);
    }

    /**
     * Returns the operating system in use.
     *
     * @return windows, mac or unix
     */
    public static String getOsName() {
        String os = "";
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
            os = "windows";
        } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
            os = "linux";
        } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
            os = "mac";
        }
        return os;
    }


// --------------------- GETTER / SETTER METHODS ---------------------

    public Vector<AAIndexMatrix> getAaParameterMatrixes() {
        if (iAaParameterMatrixes == null) {
            AAIndexReader lReader = new AAIndexReader();
            iAaParameterMatrixes = lReader.getAaParameterMatrixes(AAIndexEnum.AAINDEX1);
        }
        return iAaParameterMatrixes;
    }


    public Vector<AAIndexMatrix> getSubstitutionMatrixes() {
        if (iSubstitutionMatrices == null) {
            AAIndexReader lReader = new AAIndexReader();
            iSubstitutionMatrices = lReader.getAaParameterMatrixes(AAIndexEnum.AAINDEX2);
        }
        return iSubstitutionMatrices;
    }

    /**
     * Returns the active ReferenceSet.
     *
     * @return
     */
    public ISequenceSet getActiveReferenceSet() {
        return iActiveReferenceSet;
    }

    /**
     * Set the active referenceSet.
     *
     * @param aActiveReferenceSet
     */
    public void setActiveReferenceSet(final ISequenceSet aActiveReferenceSet) {
        iActiveReferenceSet = aActiveReferenceSet;
    }

    /**
     * Returns the anchor start position. (0-based)
     *
     * @return
     */
    public int getAnchorStartPosition() {
        return iStartPosition - 1;
    }

    /**
     * Set the anchor start position. (make sure it is 0-based!)
     *
     * @param aAnchorStartPosition
     */
    public void setAnchorStartPosition(final int aAnchorStartPosition) {
        setStartPosition(aAnchorStartPosition + 1);
    }

    //getters

    public ColorScheme getColorScheme() {
        return iColorScheme;
    }

    /**
     * Returns the fasta files that have been persisted to the information feeder.
     *
     * @return
     */
    public ArrayList<File> getFastaFiles() {
        return iFastaFiles;
    }

    public int getGraphableHeight() {
        return iGraphableHeight;
    }

    public int getGraphableWidth() {
        return iGraphableWidth;
    }

    public int getHorizontalSamplingLength() {
        return iHorizontalSamplingLength;
    }

    public void setHorizontalSamplingLength(final int aHorizontalSamplingLength) {
        iHorizontalSamplingLength = aHorizontalSamplingLength;
    }

    public IceLogoEnum getIceLogoType() {
        return iIceLogoEnum;
    }

    public void setIceLogoType(IceLogoEnum aIIceLogoEnum) {
        this.iIceLogoEnum = aIIceLogoEnum;
    }

    public int getIterationSize() {
        return iIterationSize;
    }

    public void setIterationSize(final int aIterationSize) {
        iIterationSize = aIterationSize;
    }


    public double getPvalue() {
        return iPvalue;
    }

    /**
     * Get the sampling direction.
     *
     * @return
     */
    public SamplingDirectionEnum getReferenceDirection() {
        return iReferenceDirection;
    }

    /**
     * Set the sampling direction.
     *
     * @param aReferenceDirection
     */
    public void setReferenceDirection(final SamplingDirectionEnum aReferenceDirection) {
        iReferenceDirection = aReferenceDirection;
    }

    public SamplingTypeEnum getSamplingType() {
        return iSamplingType;
    }

    public void setSamplingType(final SamplingTypeEnum aSamplingType) {
        iSamplingType = aSamplingType;
    }

    public ScoringTypeEnum getScoringType() {
        return iScoringType;
    }

    public int getSlidingWindowSize() {
        return iSlidingWindowSize;
    }

    public int getStartPosition() {
        return iStartPosition;
    }

    public int getSubLogoHeight() {
        return iSubLogoHeight;
    }

    public void setSubLogoHeight(int iSubLogoHeight) {
        this.iSubLogoHeight = iSubLogoHeight;
    }

    public int getYaxisValue() {
        return iYaxisValue;
    }

    public double getZscore() {
        return iZscore;
    }

    public boolean isFillWeblogo() {
        return iFillWeblogo;
    }

    /**
     * Returns whether there are one or two position sets.
     *
     * @return
     */
    public boolean isTwoExperiment() {
        return boolTwoExperiment;
    }

    public boolean isSlidingWindowInAaParameter() {
        return iSlidingWindowInAaParameter;
    }

    public boolean isUseAaParameterGraph() {
        return iUseAaParameterGraph;
    }

    public void setUseAaParameterGraph(boolean iUseAaParameterGraph) {
        this.iUseAaParameterGraph = iUseAaParameterGraph;
    }

    public boolean isUseConservationLogo() {
        return iUseConservationLogo;
    }

    public void setUseConservationLogo(boolean iUseConservationLogo) {
        this.iUseConservationLogo = iUseConservationLogo;
    }

    public boolean isUseBarchart() {
        return iUseBarchart;
    }

    public void setUseBarchart(boolean iUseBarchart) {
        this.iUseBarchart = iUseBarchart;
    }

    public boolean isUseHeatmap() {
        return iUseHeatmap;
    }

    public void setUseHeatmap(boolean iUseHeatmap) {
        this.iUseHeatmap = iUseHeatmap;
    }

    public boolean isUseIceLogo() {
        return iUseIceLogo;
    }

    public void setUseIceLogo(boolean iUseIceLogo) {
        this.iUseIceLogo = iUseIceLogo;
    }

    public boolean isUseSequenceLogo() {
        return iUseSequenceLogo;
    }

    public void setUseSequenceLogo(boolean iUseSequenceLogo) {
        this.iUseSequenceLogo = iUseSequenceLogo;
    }

    public boolean isUseSubLogo() {
        return iUseSubLogo;
    }

    public void setUseSubLogo(boolean iUseSubLogo) {
        this.iUseSubLogo = iUseSubLogo;
    }

    public boolean isWeblogoNegativeCorrection() {
        return iWeblogoNegativeCorrection;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Clears the content of the position sequence set.
     *
     * @param aType PositionSequenceSet type
     */
    public void addSequence(ExperimentTypeEnum aType, String aSequence) {
        switch (aType) {
            case EXPERIMENT:
                iExperimentSequenceSet.add(aSequence);
                break;

            case EXPERIMENT_TWO:
                iExperimentSequenceSetTwo.add(aSequence);
                break;
        }
    }


    /**
     * Clears the content of the position sequence set.
     *
     * @param aType PositionSequenceSet type
     */
    public void clearExperimentSequenceSet(ExperimentTypeEnum aType) {
        switch (aType) {
            case EXPERIMENT:
                iExperimentSequenceSet.clearContent();
                break;

            case EXPERIMENT_TWO:
                iExperimentSequenceSetTwo.clearContent();
                break;
        }
    }

    public double getHeatMapEndValue() {
        return iPvalue / (10 * iHeatMapMagnitude);
    }

    public double getHeatMapStartValue() {
        return iPvalue;
    }


    /**
     * Returns the number of positions on the PositionSequenceSet.
     *
     * @return
     */
    public int getNumberOfExperimentalPositions() {
        int lLength = 0;
        if (iExperimentSequenceSet != null & iExperimentSequenceSet.getNumberOfSequences() != 0) {
            iExperimentSequenceSet.reset();
            lLength = iExperimentSequenceSet.nextSequence().length();
            iExperimentSequenceSet.reset();
        }

        return lLength;
    }

    /**
     * Returns the anchor value for the position set
     *
     * @return
     */
    public int getExperimentAnchorValue() {
        return iExperimentAnchorValue;
    }

    /**
     * Returns the requested. PositionSequenceSet.
     *
     * @return
     */
    public RawSequenceSet getExperimentSequenceSet(ExperimentTypeEnum aType) {
        RawSequenceSet lResult = null;
        switch (aType) {
            case EXPERIMENT:
                lResult = iExperimentSequenceSet;
                break;

            case EXPERIMENT_TWO:
                lResult = iExperimentSequenceSetTwo;
                break;
        }
        return lResult;
    }

    public AAIndexParameterMatrix getSelectedAaParameterMatrix() {
        if (iSelectedAaParameterMatrix == null) {
            getAaParameterMatrixes();
            iSelectedAaParameterMatrix = iAaParameterMatrixes.get(0);
        }
        return (AAIndexParameterMatrix) iSelectedAaParameterMatrix;
    }

    public int getSubLogoWidth() {
        return iSubLogoWidht;
    }


    /**
     * Removes aFile from the list if it was there.
     *
     * @param aFile
     * @return boolean with the succes.
     */
    public boolean removeFastaFile(File aFile) {
        boolean lResult = false;
        if (iFastaFiles != null) {
            if (iFastaFiles.contains(aFile)) {
                iFastaFiles.remove(aFile);
                lResult = true;
            }
        }
        return lResult;
    }

    //setters

    public void setColorScheme(ColorScheme aColorScheme) {
        this.iColorScheme = aColorScheme;
        update(ObservableEnum.NOTIFY_COLOR_SCHEME);
    }

    public void setFillWeblogo(boolean aFillWeblogo) {
        if (iFillWeblogo != aFillWeblogo) {
            this.iFillWeblogo = aFillWeblogo;
            update(ObservableEnum.NOTIFY_SEQUENCE_LOGO);
        }
    }

    public void setGraphableHeight(int aGraphableHeight) {
        if (iGraphableHeight != aGraphableHeight) {
            this.iGraphableHeight = aGraphableHeight;
            update(ObservableEnum.NOTIFY_GRAPHABLE_FRAME_SIZE);
        }
    }

    public void setGraphableWidth(int aGraphableWidth) {
        if (aGraphableWidth != iGraphableWidth) {
            this.iGraphableWidth = aGraphableWidth;
            update(ObservableEnum.NOTIFY_GRAPHABLE_FRAME_SIZE);
        }
    }


    /**
     * Set the position achor value
     *
     * @param aExperimentAnchorValue
     */
    public void setExperimentAnchorValue(final int aExperimentAnchorValue) {
        iExperimentAnchorValue = aExperimentAnchorValue;
    }

    /**
     * This methods sets the P value. If this is done the corresponding Z score is also calculated and set.
     *
     * @param aPvalue The value to set.
     */
    public void setPvalue(double aPvalue) {
        //before we start we must round the p value because sometimes the spinner giver 0.0600000005 instad of 0.06
        aPvalue = Math.round(aPvalue * 1000.0) / 1000.0;
        if (iPvalue != aPvalue) {
            this.iPvalue = aPvalue;
            this.iZscore = StatisticsConversion.calculateTwoSidedZscore(1 - aPvalue);
            update(ObservableEnum.NOTIFY_STATISTICAL);
        }
    }

    public void setScoringType(ScoringTypeEnum aScoringType) {
        this.iScoringType = aScoringType;
        update(ObservableEnum.NOTIFY_SCORING_TYPE);
    }

    public void setSelectedAaParameterMatrix(AAIndexParameterMatrix aSelectedAaParameterMatrix) {
        this.iSelectedAaParameterMatrix = aSelectedAaParameterMatrix;
        update(ObservableEnum.NOTIFY_AA_PARAMETER);
    }

    public void setSlidingWindowInAaParameter(boolean iSlidingWindowInAaParameter) {
        if (this.iSlidingWindowInAaParameter != iSlidingWindowInAaParameter) {
            this.iSlidingWindowInAaParameter = iSlidingWindowInAaParameter;
            update(ObservableEnum.NOTIFY_AA_PARAMETER);
        }
    }

    public void setSlidingWindowSize(int iSlidingWindowSize) {
        if (this.iSlidingWindowSize != iSlidingWindowSize) {
            this.iSlidingWindowSize = iSlidingWindowSize;
            update(ObservableEnum.NOTIFY_AA_PARAMETER);
        }
    }

    public void setStartPosition(int aStartPosition) {
        if (iStartPosition != aStartPosition) {
            this.iStartPosition = aStartPosition;
            update(ObservableEnum.NOTIFY_START_POSITION);
        }
    }

    public void setSubLogoWidth(int iSubLogoWidht) {
        if (this.iSubLogoWidht != iSubLogoWidht) {
            this.iSubLogoWidht = iSubLogoWidht;
        }
    }

    public void setWeblogoNegativeCorrection(boolean aWeblogoNegativeCorrection) {
        if (iWeblogoNegativeCorrection != aWeblogoNegativeCorrection) {
            this.iWeblogoNegativeCorrection = aWeblogoNegativeCorrection;
            update(ObservableEnum.NOTIFY_SEQUENCE_LOGO);
        }
    }

    public void setYaxisValue(int aYaxisValue) {
        if (this.iYaxisValue != aYaxisValue) {
            this.iYaxisValue = aYaxisValue;
            update(ObservableEnum.NOTIFY_Y_AXIS);
        }
    }

    /**
     * This methods sets the Z score. If this is done the corresponding P value is also calculated and set.
     *
     * @param aZscore The value to set.
     */
    public void setZscore(double aZscore) {
        if (iZscore != aZscore) {
            this.iZscore = aZscore;
            this.iPvalue = StatisticsConversion.calculatePvalue(aZscore);
            update(ObservableEnum.NOTIFY_STATISTICAL);
        }

    }

    public Vector<SwissProtComposition> getSwissProtCompositions() {
        Vector<SwissProtComposition> lToGive = new Vector<SwissProtComposition>();
        for (int i = 0; i < iSpCompositions.size(); i++) {
            lToGive.add(iSpCompositions.get(i));
        }
        return lToGive;
    }

    /**
     * Set the samplingsize.
     *
     * @param aPositionSampleSize
     */
    public void setPositionSampleSize(int aPositionSampleSize) {
        iPositionSampleSize = aPositionSampleSize;
    }

    /**
     * Returns the samplesize.
     *
     * @return
     */
    public int getPositionSampleSize() {
        return iPositionSampleSize;
    }


    /**
     * Set the variable whether there are one or two positions sets.
     *
     * @param aTwoExperiment
     */
    public void setTwoExperiment(final boolean aTwoExperiment) {
        boolTwoExperiment = aTwoExperiment;
    }



    public void setUpdate(boolean lUpdate){
        iUpdate = lUpdate;
    }


    public void update(final ObservableEnum aObserableEnum) {

        if(iUpdate){

            final Messenger iMessenger = Messenger.getInstance();
            com.compomics.util.sun.SwingWorker updater = new com.compomics.util.sun.SwingWorker() {
            Boolean error = false;

                public Boolean construct() {
                    iMessenger.setProgressIndeterminate(true);
                    iMessenger.setProgressStringPainted(false);
                    iNotifiesRunning = iNotifiesRunning + 1;
                    //System.out.println("Start " + aObserableEnum + " " + iNotifiesRunning);
                    setChanged();
                    try{
                        notifyObservers(aObserableEnum);
                    } catch (Exception e){
                        System.out.println("__" + e.getLocalizedMessage());
                    }
                    return error;
                }

                public void finished() {
                    iNotifiesRunning = iNotifiesRunning - 1;
                    //System.out.println(aObserableEnum + " " + iNotifiesRunning);
                    if(iNotifiesRunning <= 0){
                        iNotifiesRunning = 0;
                        iMessenger.setProgressIndeterminate(false);
                    }

                }

            };
            updater.start();
        }

    }


    /**
     * Returns an AminoAcidEnum array with the experiment AminoAcids at the given index. Note that both both
     * experimental sets are summed if two experiment sets are availlable!!
     *
     * @param aIndex The index (0-based) to fetch the AminoAcids from the ExerimentalSet.
     * @return
     */
    public AminoAcidEnum[] getExperimentalAminoAcids(int aIndex) {
        // Get the experiment sets.
        RawSequenceSet lExperimentSequenceSet = getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        lExperimentSequenceSet.reset();
        RawSequenceSet lExperimentSequenceSetTwo = getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
        lExperimentSequenceSetTwo.reset();

        // Create a AA container.
        ArrayList<AminoAcidEnum> lAminoAcidEnumArrayList = new ArrayList<AminoAcidEnum>();
        String lSequence = null;
        while ((lSequence = lExperimentSequenceSet.nextSequence()) != null) {
            char aa = lSequence.charAt(aIndex);
            lAminoAcidEnumArrayList.add(AminoAcidFactory.getAminoAcid(aa));
        }

        // If two experiments availlable,
        if (isTwoExperiment()) {
            lSequence = null;
            while ((lSequence = lExperimentSequenceSetTwo.nextSequence()) != null) {
                char aa = lSequence.charAt(aIndex);
                lAminoAcidEnumArrayList.add(AminoAcidFactory.getAminoAcid(aa));
            }
        }

        AminoAcidEnum[] lResult = new AminoAcidEnum[lAminoAcidEnumArrayList.size()];
        lAminoAcidEnumArrayList.toArray(lResult);

        return lResult;
    }


    public AAIndexSubstitutionMatrix getSubstitutionMatrix() {
        //check if we have one
        if(iSubstitutionMatrix == null){
            //there is no substitution matrix
            //read the aaindex 2 table
             AAIndexReader reader = new AAIndexReader();
             Vector<AAIndexMatrix> lMatrices = reader.getAaParameterMatrixes(AAIndexEnum.AAINDEX2);
             iSubstitutionMatrix = (AAIndexSubstitutionMatrix) lMatrices.get(13);
        }
        return iSubstitutionMatrix;
    }

    public void setSubstitutionMatrix(AAIndexSubstitutionMatrix iSubstitutionMatrix) {
        if(this.iSubstitutionMatrix != iSubstitutionMatrix){
            this.iSubstitutionMatrix = iSubstitutionMatrix;
            update(ObservableEnum.NOTIFY_CONSERVATION_LINE);
        }
    }


    public boolean isConservationLineNormalized() {
        return iConservationLineNormalized;
    }

    public void setConservationLineNormalized(boolean iConservationLineNormalized) {
        this.iConservationLineNormalized = iConservationLineNormalized;
        update(ObservableEnum.NOTIFY_CONSERVATION_LINE);
    }

    public void setUseKLogoGraph(final boolean aSelected) {
        this.iUseKlogo = aSelected;
    }

    public boolean isUseKlogoGraph() {
        return iUseKlogo;
    }

    /**
     * Returns a SequenceClusterer for the current experimental sequenceset.
     *
     * @return
     */
    public SequenceClusterer getSequenceClusterer() {
        SequenceClusterer lClusterer;
        lClusterer = new SequenceClusterer(getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT));
        lClusterer.setClusterCount(4);

        return lClusterer;
    }

}