package com.compomics.icelogo.gui.interfaces;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 11-sep-2008 Time: 15:05:45 The 'HeatMapDataSupplier ' class was created
 * for
 */
public interface HeatMapDataSupplier {
// -------------------------- OTHER METHODS --------------------------

    /**
     * Returns the number of columns for the HeatMap.
     *
     * @return The number of columns of the heatmap.
     */
    public int getColumnCount();

    /**
     * Returns the maximum value for the HeatMap.
     *
     * @return The Maximum value for the HeatMap.
     */
    public double getMaxValue();

    /**
     * Returns the Minimum value for the HeatMap.
     *
     * @return The Minimum value for the HeatMap.
     */
    public double getMinValue();

    /**
     * Returns the number of rows for the HeatMap.
     *
     * @return The number of rows of the heatmap.
     */
    public int getRowCount();

    /**
     * Returns the value for the given row and column.
     *
     * @param aRow    The rownumber of the value to be returned. (0-based!)
     * @param aColumn The columnnumber of the value to be returned. (0-based!)
     * @return The value for the given row and column.
     */
    public double getValueAt(int aRow, int aColumn);

    AminoAcidEnum getRowAminoAcid(int aRow);

    AminoAcidEnum[] getRows();
}
