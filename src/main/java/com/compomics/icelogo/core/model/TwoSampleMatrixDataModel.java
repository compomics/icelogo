package com.compomics.icelogo.core.model;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.RegulatedPosition;
import com.compomics.icelogo.core.data.RegulatedEntity;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.ExperimentTypeEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.StatisticalCategoryDataset;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 5-sep-2008 Time: 14:29:32 The 'MatrixDataModel ' class was created to
 * model distinct composition of AminoAcidMatrices.
 */
public class TwoSampleMatrixDataModel implements MatrixDataModel {
// ------------------------------ FIELDS ------------------------------

    AminoAcidStatistics[] iReferenceStatistics = null;
    protected AminoAcidStatistics[] iExperimentStatistics = null;
    protected AminoAcidStatistics[] iExperimentTwoStatistics = null;

    protected boolean iSingleReference = false;
    private RegulatedPosition[] iRegulatedPositions;
    private double iZscore = 0.0;
    private RegulatedPosition[] iAllPositions = new RegulatedPosition[0];
    private String iReferenceID;
    private double iCurrentZScore = 0.0;

// --------------------------- CONSTRUCTORS ---------------------------

    public TwoSampleMatrixDataModel(final AminoAcidStatistics[] aReferenceStatistics, final AminoAcidStatistics[] aExperimentStatistics, final AminoAcidStatistics[] aAExperimentTwoStatistics, final String aReferenceID) {
        iExperimentStatistics = aExperimentStatistics;
        iExperimentTwoStatistics = aAExperimentTwoStatistics;
        iReferenceStatistics = aReferenceStatistics;
        iReferenceID = aReferenceID;
        setSingleReference();
    }

// --------------------- GETTER / SETTER METHODS ---------------------


    private void setSingleReference() {
        if (iReferenceStatistics.length == 1) {
            iSingleReference = true;
        } else {
            iSingleReference = false;
        }
    }

    /**
     * Returns the number of 'Positions' in this MatrixDataModel.
     *
     * @return The number of 'Positions' in this MatrixDataModel.
     */
    public int getNumberOfPositions() {
        assert iExperimentStatistics.length == iExperimentTwoStatistics.length;

        return iExperimentStatistics.length;
    }

    /**
     * Not implemented!!
     */
    public RegulatedPosition[] getRegulatedPositions(final Double aNewZScore) {
        if (aNewZScore == null || iCurrentZScore != aNewZScore) {

            if (aNewZScore != null) {
                iCurrentZScore = aNewZScore;
            }
            iRegulatedPositions = new RegulatedPosition[this.getNumberOfPositions()];

            for (int i = 0; i < this.getNumberOfPositions(); i++) {
                RegulatedPosition position = new RegulatedPosition();

                AminoAcidStatistics lExperimentalMatrix = this.getExperimentalAminoAcidStatistics(i, ExperimentTypeEnum.EXPERIMENT);
                AminoAcidStatistics lExperimentalMatrixTwo = this.getExperimentalAminoAcidStatistics(i, ExperimentTypeEnum.EXPERIMENT_TWO);
                AminoAcidStatistics lReferenceMatrix = this.getReferenceAminoAcidStatistics(i);

                for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
                    if (lAminoAcidEnum != AminoAcidEnum.OTHER) {
                        StatisticalSummary lPositionStatisticalSummary = lExperimentalMatrix.getStatistics(lAminoAcidEnum);
                        StatisticalSummary lPositionTwoStatisticalSummary = lExperimentalMatrixTwo.getStatistics(lAminoAcidEnum);
                        StatisticalSummary lReferenceStatisticalSummary = lReferenceMatrix.getStatistics(lAminoAcidEnum);
                        RegulatedEntity regulatedElement;

                        double lSD = lReferenceStatisticalSummary.getStandardDeviation();
                        double lDiff1 = lPositionStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double lDiff2 = lPositionTwoStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double calSD = lDiff1 - lDiff2 / lSD;
                        double lPercDiff = lPositionStatisticalSummary.getMean() - lPositionTwoStatisticalSummary.getMean();
                        double lFoldChange = lPositionStatisticalSummary.getMean() / lPositionTwoStatisticalSummary.getMean();
                        // check and change 0 values
                        boolean infinite = false;
                        if (lPositionStatisticalSummary.getMean() == 0) {
                            lFoldChange = -100.101010;
                            infinite = true;
                        } else {
                            if (lPositionStatisticalSummary.getMean() == 0) {
                                lFoldChange = 100.101010;
                                infinite = true;
                            } else {
                                // go from 0.5 to -1 and from 0.1 to -10
                                if (lFoldChange < 1) {
                                    lFoldChange = lFoldChange * (1 / lFoldChange) * (1 / lFoldChange) * -1;
                                }
                            }
                        }
                        if (lReferenceStatisticalSummary.getMean() == 0 && lReferenceStatisticalSummary.getMean() == 0) {
                            lFoldChange = 0.0;
                        }
                        
                        if (aNewZScore == null) {
                            regulatedElement = new RegulatedEntity(lAminoAcidEnum, lPercDiff, lFoldChange, calSD, lPositionStatisticalSummary.getMean());
                            regulatedElement.setInfinite(infinite);
                            position.addPositveRegulatedEntity(regulatedElement);

                        } else {
                            if (calSD > iCurrentZScore) {
                                regulatedElement = new RegulatedEntity(lAminoAcidEnum, lPercDiff, lFoldChange, calSD, lPositionStatisticalSummary.getMean());
                                regulatedElement.setInfinite(infinite);
                                position.addPositveRegulatedEntity(regulatedElement);
                            }
                            if (calSD < -iCurrentZScore) {
                                regulatedElement = new RegulatedEntity(lAminoAcidEnum, lPercDiff, lFoldChange, calSD, lPositionStatisticalSummary.getMean());
                                regulatedElement.setInfinite(infinite);
                                position.addNegativeRegulatedEntity(regulatedElement);
                            }
                        }
                    }
                }
                iRegulatedPositions[i] = position;
            }
        }
        return iRegulatedPositions;
    }

    /**
     * Not implemented!!
     */
    public RegulatedPosition[] getAllPositions(){
        if (iAllPositions == null) {

            iAllPositions = new RegulatedPosition[this.getNumberOfPositions()];

            for (int i = 0; i < this.getNumberOfPositions(); i++) {
                RegulatedPosition position = new RegulatedPosition();

                AminoAcidStatistics lExperimentalMatrix = this.getExperimentalAminoAcidStatistics(i, ExperimentTypeEnum.EXPERIMENT);
                AminoAcidStatistics lExperimentalMatrixTwo = this.getExperimentalAminoAcidStatistics(i, ExperimentTypeEnum.EXPERIMENT_TWO);
                AminoAcidStatistics lReferenceMatrix = this.getReferenceAminoAcidStatistics(i);

                for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
                    if (lAminoAcidEnum != AminoAcidEnum.OTHER) {
                        StatisticalSummary lPositionStatisticalSummary = lExperimentalMatrix.getStatistics(lAminoAcidEnum);
                        StatisticalSummary lPositionTwoStatisticalSummary = lExperimentalMatrixTwo.getStatistics(lAminoAcidEnum);
                        StatisticalSummary lReferenceStatisticalSummary = lReferenceMatrix.getStatistics(lAminoAcidEnum);
                        RegulatedEntity regulatedElement;

                        double lSD = lReferenceStatisticalSummary.getStandardDeviation();
                        double lDiff1 = lPositionStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double lDiff2 = lPositionTwoStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double calSD = lDiff1 - lDiff2 / lSD;
                        double lPercDiff = lPositionStatisticalSummary.getMean() - lPositionTwoStatisticalSummary.getMean();
                        double lFoldChange = lPositionStatisticalSummary.getMean() / lPositionTwoStatisticalSummary.getMean();
                        // check and change 0 values
                        boolean infinite = false;
                        if (lPositionStatisticalSummary.getMean() == 0) {
                            lFoldChange = -100.101010;
                            infinite = true;
                        } else {
                            if (lPositionStatisticalSummary.getMean() == 0) {
                                lFoldChange = 100.101010;
                                infinite = true;
                            } else {
                                // go from 0.5 to -1 and from 0.1 to -10
                                if (lFoldChange < 1) {
                                    lFoldChange = lFoldChange * (1 / lFoldChange) * (1 / lFoldChange) * -1;
                                }
                            }
                        }
                        if (lReferenceStatisticalSummary.getMean() == 0 && lReferenceStatisticalSummary.getMean() == 0) {
                            lFoldChange = 0.0;
                        }

                        regulatedElement = new RegulatedEntity(lAminoAcidEnum, lPercDiff, lFoldChange, calSD, lPositionStatisticalSummary.getMean());
                        regulatedElement.setInfinite(infinite);
                        position.addPositveRegulatedEntity(regulatedElement);

                    }
                }
                iAllPositions[i] = position;
            }
        }
        return iAllPositions;
    }

    public AminoAcidStatistics getExperimentalAminoAcidStatistics(int aIndex, ExperimentTypeEnum aType) {
        if (aType == ExperimentTypeEnum.EXPERIMENT) {
            return iExperimentStatistics[aIndex];
        } else if (aType == ExperimentTypeEnum.EXPERIMENT_TWO) {
            return iExperimentTwoStatistics[aIndex];
        } else {
            throw new IllegalArgumentException("PositionType " + aType + " is in valid for two sample model.");
        }
    }

    public AminoAcidStatistics getReferenceAminoAcidStatistics(int aIndex) {
        if (hasSingleReference()) {
            aIndex = 0;
        }
        return iReferenceStatistics[aIndex];
    }

    ;

    public boolean hasSingleReference() {
        return iSingleReference;
    }

    /**
     * Returns a String Identifier for the reference set used to build this MatrixDataModel.
     *
     * @return String name for the reference set.
     */
    public String getReferenceID() {
        return iReferenceID;
    }

// ------------------------ CANONICAL METHODS ------------------------

    public String toString() {
        return "OneSampleMatrixDataModel{" +
                iReferenceStatistics.length +
                "iReferenceStatistics legnth = " +
                "GeneralOneSampleMatrixDataModel{" +
                iReferenceStatistics[0].getDimension() +
                '}';
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface MatrixDataModel ---------------------


    public StatisticalCategoryDataset getReferenceAndPositionStatisticalCategoryDataset(int aPosition) {
        // Get the information feeder.
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();

        // create the dataset...
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();

        for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
            // For the given position, retrieve the descriptive statistics for the current aminoacid.
            StatisticalSummary stat = getReferenceAminoAcidStatistics(aPosition).getStatistics(lAminoAcidEnum);

            Number lReferenceMean = stat.getMean();
            Number lReferenceStandardDeviation = stat.getStandardDeviation();

            dataset.add(lReferenceMean, lReferenceStandardDeviation, getReferenceAminoAcidStatistics(aPosition).toString(), lAminoAcidEnum.getOneLetterCode());
            dataset.add((Number) iExperimentStatistics[aPosition].getStatistics(lAminoAcidEnum).getMean(), 0, "Position A", lAminoAcidEnum.getOneLetterCode());
            dataset.add((Number) iExperimentTwoStatistics[aPosition].getStatistics(lAminoAcidEnum).getMean(), 0, "Position B", lAminoAcidEnum.getOneLetterCode());
        }

        return dataset;
    }


// -------------------------- OTHER METHODS --------------------------

    /**
     * Returns the value for a given AminoAcid at a given position from the <b>Position SequenceSet</b>.
     *
     * @param aAminoAcid          The aminoacid for which the value is returned.
     * @param aIndex              The index (position) for which the value is returned. (0-based!)
     * @param aExperimentalMatrix The ExperimentalMatrix of choince, '0' for the first position matrix and '1' for the second
     *                            ExperimentalMatrix.
     * @return a double percentage for the given AminoAcid at the given Index.
     */
    public double getPositionValue(AminoAcidEnum aAminoAcid, int aIndex, int aExperimentalMatrix) {
        if (aExperimentalMatrix == 0) {
            return iExperimentStatistics[aIndex].getStatistics(aAminoAcid).getMean();
        } else if (aExperimentalMatrix == 1) {
            return iExperimentTwoStatistics[aIndex].getStatistics(aAminoAcid).getMean();
        } else {
            assert false;
            return -1;
        }
    }

    /**
     * Returns the Reference Standard Deviation for a given AminoAcid.
     *
     * @param aAminoAcid The aminoacid for which the Standard Deviation is returned.
     * @param aIndex     The index (position) for which the value is returned. (0-based!)
     * @return a double Standard Deviation for the given Aminoacid.
     */
    public double getReferenceSD(AminoAcidEnum aAminoAcid, int aIndex) {
        return getReferenceAminoAcidStatistics(aIndex).getStatistics(aAminoAcid).getStandardDeviation();
    }

    ;


    /**
     * Returns the value for a given AminoAcid at a given position from the <b>Reference SequenceSet</b>.
     *
     * @param aAminoAcid The aminoacid for which the value is returned.
     * @param aIndex     The index (position) for which the value is returned. (0-based!)
     * @return a double percentage for the given AminoAcid at the given Index.
     */
    public double getReferenceValue(AminoAcidEnum aAminoAcid, int aIndex) {
        return getReferenceAminoAcidStatistics(aIndex).getStatistics(aAminoAcid).getMean();
    }
}