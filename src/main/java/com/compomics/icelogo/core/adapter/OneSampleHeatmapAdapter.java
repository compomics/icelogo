package com.compomics.icelogo.core.adapter;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.StatisticsTypeEnum;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import com.compomics.icelogo.core.stat.StatisticsConversion;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 19-sep-2008 Time: 15:43:52 The 'OneSampleMatrixModelAdapter ' class was
 * created for
 */
public class OneSampleHeatmapAdapter extends HeatmapAdapter {
    private OneSampleMatrixDataModel iOneSampleMatrixDataModel;


    public OneSampleHeatmapAdapter(OneSampleMatrixDataModel aOneSampleMatrixDataModel) {
        iOneSampleMatrixDataModel = aOneSampleMatrixDataModel;
        createValues();

    }

    protected void createValues() {

        int lNumberOfPositions = iOneSampleMatrixDataModel.getNumberOfPositions();

        values = new HashMap<AminoAcidEnum, ArrayList<Double>>(AminoAcidEnum.values().length);
        int i = 0;
        while (i < AminoAcidEnum.values().length) {
            AminoAcidEnum lAminoAcid = AminoAcidEnum.values()[i];
            values.put(lAminoAcid, new ArrayList<Double>(lNumberOfPositions));
            for (int j = 0; j < lNumberOfPositions; j++) {
                double lReferenceSD = iOneSampleMatrixDataModel.getReferenceSD(lAminoAcid, j);
                double lReferenceValue = iOneSampleMatrixDataModel.getReferenceValue(lAminoAcid, j);
                double lPositionValue = iOneSampleMatrixDataModel.getPositionValue(lAminoAcid, j);
                if (lReferenceSD == 0) {
                    lReferenceSD = 0.001;
                }
                double lCellValue = (lPositionValue - lReferenceValue) / lReferenceSD;

                // If the p-value is wanted, make a conversion!
                if (iStatisticsType == StatisticsTypeEnum.PVALUE) {
                    lCellValue = StatisticsConversion.cumulativeProbability(lCellValue);
                    if (lCellValue <= 0) {
                        lCellValue = 0.000000000000001;
                    } else if (lCellValue >= 1) {
                        lCellValue = 0.999999999999999;

                    }
                }
                values.get(lAminoAcid).add(j, lCellValue);

            }
            i++;
        }
    }
}
