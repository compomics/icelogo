package com.compomics.icelogo.core.adapter;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.StatisticsTypeEnum;
import com.compomics.icelogo.gui.interfaces.HeatMapDataSupplier;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 11-sep-2008 Time: 15:18:06 The 'DataMatrixModelAdapter ' class was
 * created for
 */
public abstract class HeatmapAdapter implements HeatMapDataSupplier {
// ------------------------------ FIELDS ------------------------------

    private double iMinValue = 0.5;
    private double iMaxValue = 0.5;
    /**
     * This 2D-double array holds the values for the HeatMap. The first dimension targets the rows, whereas the second
     * dimension targets the columns.
     */
    protected HashMap<AminoAcidEnum, ArrayList<Double>> values;

    protected StatisticsTypeEnum iStatisticsType = StatisticsTypeEnum.PVALUE;

// --------------------------- CONSTRUCTORS ---------------------------


    protected abstract void createValues();

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * {@inheritDoc}
     */
    public double getMaxValue() {
        if (iMaxValue == 0.5) {
            // Iterate over all aminoAcids
            for (final AminoAcidEnum lAminoAcidEnum : values.keySet()) {
                // Iterate over the double array for each amino acid.
                ArrayList list = values.get(lAminoAcidEnum);
                for (int i = 0; i < list.size(); i++) {
                    double v = (Double) list.get(i);
                    if (iMaxValue < v) {
                        iMaxValue = v;
                    }
                }
            }
        }

        return iMaxValue;
    }

    /**
     * {@inheritDoc}
     */
    public double getMinValue() {
        if (iMinValue == 0.5) {
            // Iterate over all aminoAcids
            for (final AminoAcidEnum lAminoAcidEnum : values.keySet()) {
                // Iterate over the double array for each amino acid.
                ArrayList list = values.get(lAminoAcidEnum);
                for (int i = 0; i < list.size(); i++) {
                    double v = (Double) list.get(i);
                    if (v < iMinValue) {
                        iMinValue = v;
                    }
                }
            }
        }

        return iMinValue;
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface HeatMapDataSupplier ---------------------


    /**
     * {@inheritDoc}
     */
    public int getColumnCount() {
        return values.get(AminoAcidEnum.values()[0]).size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount() {
        return values.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public double getValueAt(final int aRow, int aColumn) {
        // todo: Strategy for sorting the rows! ..
        return values.get(getRowAminoAcid(aRow)).get(aColumn);
    }

    public AminoAcidEnum getRowAminoAcid(int aRow) {
        return getRows()[aRow];
    }

    public AminoAcidEnum[] getRows() {
        return AminoAcidEnum.OTHER.valuesByPhysicoChemicalProperties();
    }

    public void setStatisticsType(final StatisticsTypeEnum aStatisticsType) {
        iStatisticsType = aStatisticsType;
    }

    public StatisticsTypeEnum getStatisticsType() {
        return iStatisticsType;
    }
}
