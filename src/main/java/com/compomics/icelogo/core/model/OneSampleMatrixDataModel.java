package com.compomics.icelogo.core.model;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.RegulatedEntity;
import com.compomics.icelogo.core.data.RegulatedPosition;
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
public class OneSampleMatrixDataModel implements MatrixDataModel {
// ------------------------------ FIELDS ------------------------------

    AminoAcidStatistics[] iExperimentStatistics = null;
    AminoAcidStatistics[] iReferenceStatistics = null;
    protected boolean iSingleReference = false;
    private RegulatedPosition[] iRegulatedPositions;
    private RegulatedPosition[] iAllPositions;
    private double iCurrentZScore = 0.0;
    private String iReferenceID;

// --------------------------- CONSTRUCTORS ---------------------------


    public OneSampleMatrixDataModel(final AminoAcidStatistics[] aReferenceStatistics, final AminoAcidStatistics[] aAExperimentStatistics, final String aReferenceID) {
        iExperimentStatistics = aAExperimentStatistics;
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
        return iExperimentStatistics.length;
    }

    public AminoAcidStatistics getExperimentalAminoAcidStatistics(int aIndex, ExperimentTypeEnum aType) {
        if(ExperimentTypeEnum.EXPERIMENT_TWO == aType){
            return null;
        }
        return iExperimentStatistics[aIndex];
    }

    public AminoAcidStatistics getReferenceAminoAcidStatistics(int aIndex) {
        if (hasSingleReference()) {
            aIndex = 0;
        }
        return iReferenceStatistics[aIndex];
    }

    public boolean hasSingleReference() {
        return iSingleReference;
    }

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
            dataset.add((Number) iExperimentStatistics[aPosition].getStatistics(lAminoAcidEnum).getMean(), 0, "Position", lAminoAcidEnum.getOneLetterCode());
        }
        return dataset;
    }

    /**
     * {@inheritDoc}
     * If aNewZScore is null, all AA for all positions will be returned.
     */
    public RegulatedPosition[] getRegulatedPositions(Double aNewZScore) {
        if (aNewZScore == null || iCurrentZScore != aNewZScore) {

            if (aNewZScore != null) {
                iCurrentZScore = aNewZScore;
            }
            iRegulatedPositions = new RegulatedPosition[this.getNumberOfPositions()];

            for (int i = 0; i < this.getNumberOfPositions(); i++) {
                RegulatedPosition position = new RegulatedPosition();

                AminoAcidStatistics lExperimentalMatrix = this.getExperimentalAminoAcidStatistics(i, ExperimentTypeEnum.EXPERIMENT);
                AminoAcidStatistics lReferenceMatrix = this.getReferenceAminoAcidStatistics(i);

                for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
                    if (lAminoAcidEnum != AminoAcidEnum.OTHER) {
                        StatisticalSummary lPositionStatisticalSummary = lExperimentalMatrix.getStatistics(lAminoAcidEnum);
                        StatisticalSummary lReferenceStatisticalSummary = lReferenceMatrix.getStatistics(lAminoAcidEnum);
                        RegulatedEntity regulatedElement;
                        double lSD = lReferenceStatisticalSummary.getStandardDeviation();
                        double lDiff = lPositionStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double calSD = lDiff / lSD;
                        double lPercDiff = lPositionStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double lFoldChange = lPositionStatisticalSummary.getMean() / lReferenceStatisticalSummary.getMean();
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
     * {@inheritDoc}
     * If aNewZScore is null, all AA for all positions will be returned.
     */
    public RegulatedPosition[] getAllPositions() {
        if (iAllPositions == null) {

            iAllPositions = new RegulatedPosition[this.getNumberOfPositions()];

            for (int i = 0; i < this.getNumberOfPositions(); i++) {
                RegulatedPosition position = new RegulatedPosition();

                AminoAcidStatistics lExperimentalMatrix = this.getExperimentalAminoAcidStatistics(i, ExperimentTypeEnum.EXPERIMENT);
                AminoAcidStatistics lReferenceMatrix = this.getReferenceAminoAcidStatistics(i);

                for (AminoAcidEnum lAminoAcidEnum : AminoAcidEnum.values()) {
                    if (lAminoAcidEnum != AminoAcidEnum.OTHER) {
                        StatisticalSummary lPositionStatisticalSummary = lExperimentalMatrix.getStatistics(lAminoAcidEnum);
                        StatisticalSummary lReferenceStatisticalSummary = lReferenceMatrix.getStatistics(lAminoAcidEnum);
                        RegulatedEntity regulatedElement;
                        double lSD = lReferenceStatisticalSummary.getStandardDeviation();
                        double lDiff = lPositionStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double calSD = lDiff / lSD;
                        double lPercDiff = lPositionStatisticalSummary.getMean() - lReferenceStatisticalSummary.getMean();
                        double lFoldChange = lPositionStatisticalSummary.getMean() / lReferenceStatisticalSummary.getMean();
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

// -------------------------- OTHER METHODS --------------------------

    /**
     * Returns the value for a given AminoAcid at a given position from the <b>Position SequenceSet</b>.
     *
     * @param aAminoAcid The aminoacid for which the value is returned.
     * @param aIndex     The index (position) for which the value is returned. (0-based!)
     * @return a double percentage for the given AminoAcid at the given Index.
     */
    public double getPositionValue(AminoAcidEnum aAminoAcid, int aIndex) {
        return iExperimentStatistics[aIndex].getStatistics(aAminoAcid).getMean();
    }

    /**
     * Returns a StatisticalCategoryDataset for the ExperimentalMatrix and ReferenceMatrix at the given position.
     *
     * @param aPosition The integer position for the dataset. (0-based!)
     *
     * @return StatisticalCategoryDataset containing two series. Series '0' represents the referenceset with statistics
     *         while Series '1' represents the position set.
     */

    /**
     * Returns the Reference Standard Deviation for a given AminoAcid.
     *
     * @param aAminoAcid The aminoacid for which the Standard Deviation is returned.
     * @param aIndex     The index (position) for which the value is returned. (0-based!)
     * @return a double Standard Deviation for the given Aminoacid.
     */
    public double getReferenceSD(final AminoAcidEnum aAminoAcid, final int aIndex) {
        return getReferenceAminoAcidStatistics(aIndex).getStatistics(aAminoAcid).getStandardDeviation();
    }

    /**
     * Returns the value for a given AminoAcid at a given position from the <b>Reference SequenceSet</b>.
     *
     * @param aAminoAcid The aminoacid for which the value is returned.
     * @param aIndex     The index (position) for which the value is returned. (0-based!)
     * @return a double percentage for the given AminoAcid at the given Index.
     */
    public double getReferenceValue(final AminoAcidEnum aAminoAcid, final int aIndex) {
        return getReferenceAminoAcidStatistics(aIndex).getStatistics(aAminoAcid).getMean();
    }
}
