package com.compomics.icelogo.core.interfaces;

import com.compomics.icelogo.core.data.RegulatedPosition;
import com.compomics.icelogo.core.enumeration.ExperimentTypeEnum;
import org.jfree.data.statistics.StatisticalCategoryDataset;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 19-sep-2008 Time: 15:07:28 The 'MatrixDataModel ' class was created for
 */
public interface MatrixDataModel {

    /**
     * Returns a StatisticalCategoryDataset for the ExperimentalMatrix and ReferenceMatrix at the given position.
     *
     * @param aPosition The integer position for the dataset. (0-based!)
     * @return StatisticalCategoryDataset containing two series. Series '0' represents the referenceset with statistics
     *         while Series '1' represents the position set.
     */
    public abstract StatisticalCategoryDataset getReferenceAndPositionStatisticalCategoryDataset(int aPosition);

    /**
     * Returns a AminoAcidStatistics for the ExperimentalMatrix at the given position.
     *
     * @param aIndex The integer position for the dataset. (0-based!)
     * @return AminoAcidStatistics  Returns a Statistics object for the given AminoAcid. The statistics are generated
     *         over the different columns of the Matrix.
     */
    public abstract AminoAcidStatistics getExperimentalAminoAcidStatistics(int aIndex, ExperimentTypeEnum aType);

    /**
     * Returns a AminoAcidStatistics for the ReferenceMatrix at the given position.
     *
     * @param aIndex The integer position for the dataset. (0-based!)
     * @return AminoAcidStatistics  Returns a Statistics object for the given AminoAcid. The statistics are generated
     *         over the different columns of the Matrix.
     */
    public abstract AminoAcidStatistics getReferenceAminoAcidStatistics(int aIndex);

    /**
     * Returns the number of positions this MatrixDataModel can perform statistics on.
     *
     * @return int The number of positions within this MatrixDataModel.
     */
    public abstract int getNumberOfPositions();

    /**
     * This method will give the significantly regulated positions
     *
     * @param aStandardDeviation The Z-score that will be used to check the significance. This value can be <b>null</b>,
     *                           all AminoAcids for all Positions will then be returned.
     * @return An array with the regulated positions
     */
    public RegulatedPosition[] getRegulatedPositions(Double aStandardDeviation);

    /**
     * This method will give the all positions as positively regulated
     *
     * @return An array with the regulated positions
     */
    public RegulatedPosition[] getAllPositions();

    public boolean hasSingleReference();

    /**
     * Returns a String Identifier for the reference set used to build this MatrixDataModel.
     *
     * @return String name for the reference set.
     */
    public String getReferenceID();

}
