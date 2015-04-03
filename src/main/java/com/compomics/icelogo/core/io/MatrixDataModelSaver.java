package com.compomics.icelogo.core.io;

import com.compomics.icelogo.core.data.RegulatedEntity;
import com.compomics.icelogo.core.data.RegulatedPosition;
import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.core.model.TwoSampleMatrixDataModel;
import com.compomics.icelogo.gui.interfaces.Savable;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: kennyhelsens
 * Date: Oct 21, 2010
 * Time: 5:29:58 PM
 */

public class MatrixDataModelSaver implements Savable {
    private MatrixDataModel iMatrixDataModel;
    private String iContent = null;
    private char iSep = ',';

    public MatrixDataModelSaver(MatrixDataModel aMatrixDataModel) {
        iMatrixDataModel = aMatrixDataModel;
    }

    /**
     * This method converts the MatrixDataModel into a comma separated String.
     */
    private void buildContent() {
        // Verify the MatrixDataModel type.
        boolean isTwoSampleModel = false;
        if (iMatrixDataModel instanceof TwoSampleMatrixDataModel) {
            isTwoSampleModel = true;
        }

        StringBuffer lContent = new StringBuffer();

        // Ok, first append the headers.
        AminoAcidEnum[] lAminoAcids = AminoAcidEnum.values();
        lContent.append("Parameter" + iSep);
        for (AminoAcidEnum lAminoAcid : lAminoAcids) {
            lContent.append("" + lAminoAcid.getOneLetterCode() + iSep);
        }

        // First get all info from the RegulatedPositions.
        RegulatedPosition[] lAllPositions = iMatrixDataModel.getAllPositions();

        // POSITION FOLD CHANGE METRICS.
        for (int i1 = 0; i1 < lAllPositions.length; i1++) {

            lContent.append("\nFold_Change_POS_P" + (i1 + 1) + iSep);
            // Each entry in this array is a Position (equals the experimental peptide length)
            RegulatedPosition lRegulatedPosition = lAllPositions[i1];
            // Fold change for positions.
            RegulatedEntity[] lRegulatedEntities = lRegulatedPosition.getPositiveRegulatedEntity(ScoringTypeEnum.FOLDCHANGE);
            HashMap lRegulatedEntitiesMap = new HashMap();

            for (RegulatedEntity lRegulatedEntity : lRegulatedEntities) {
                lRegulatedEntitiesMap.put(lRegulatedEntity.getAminoAcid(), lRegulatedEntity.iFoldChange);
            }

            String s = arrangeByAminoAcid(lAminoAcids, lRegulatedEntitiesMap);
            lContent.append(s);

        }

        // POSITION SD METRICS.
        for (int i1 = 0; i1 < lAllPositions.length; i1++) {

            lContent.append("\nZ-score_POS_P" + (i1 + 1) + iSep);
            // Each entry in this array is a Position (equals the experimental peptide length)
            RegulatedPosition lRegulatedPosition = lAllPositions[i1];
            // Fold change for positions.
            RegulatedEntity[] lRegulatedEntities = lRegulatedPosition.getPositiveRegulatedEntity(ScoringTypeEnum.STANDARD_DEVIATION);
            HashMap lRegulatedEntitiesMap = new HashMap();

            for (RegulatedEntity lRegulatedEntity : lRegulatedEntities) {
                lRegulatedEntitiesMap.put(lRegulatedEntity.getAminoAcid(), lRegulatedEntity.iSDchange);
            }

            String s = arrangeByAminoAcid(lAminoAcids, lRegulatedEntitiesMap);
            lContent.append(s);

        }

        // POSITION FREQUENCY METRICS.
        for (int i1 = 0; i1 < lAllPositions.length; i1++) {

            lContent.append("\nFREQ_POS_P" + (i1 + 1) + iSep);
            // Each entry in this array is a Position (equals the experimental peptide length)
            RegulatedPosition lRegulatedPosition = lAllPositions[i1];
            // Fold change for positions.
            RegulatedEntity[] lRegulatedEntities = lRegulatedPosition.getPositiveRegulatedEntity(ScoringTypeEnum.FREQUENCY);
            HashMap lRegulatedEntitiesMap = new HashMap();

            for (RegulatedEntity lRegulatedEntity : lRegulatedEntities) {
                lRegulatedEntitiesMap.put(lRegulatedEntity.getAminoAcid(), lRegulatedEntity.iFrequency);
            }

            String s = arrangeByAminoAcid(lAminoAcids, lRegulatedEntitiesMap);
            lContent.append(s);

        }

        // REFERENCE FREQUENCY METRICS.
        if (iMatrixDataModel.hasSingleReference()) {
            AminoAcidStatistics lReferenceStatistics = iMatrixDataModel.getReferenceAminoAcidStatistics(0);
            lContent.append("\nFREQ_REF");
            lContent.append(iSep);
            for (AminoAcidEnum lAminoAcid : lAminoAcids) {
                StatisticalSummary lStatistics = lReferenceStatistics.getStatistics(lAminoAcid);
                lContent.append(lStatistics.getMean());
                lContent.append(iSep);
            }
        } else {
            int lNumberOfPositions = iMatrixDataModel.getNumberOfPositions();
            for (int i1 = 0; i1 < lNumberOfPositions; i1++) {
                AminoAcidStatistics lReferenceStatistics = iMatrixDataModel.getReferenceAminoAcidStatistics(i1);
                lContent.append("\nFREQ_REF_P" + (i1 + 1 ));
                lContent.append(iSep);

                for (AminoAcidEnum lAminoAcid : lAminoAcids) {
                    StatisticalSummary lStatistics = lReferenceStatistics.getStatistics(lAminoAcid);
                    lContent.append(lStatistics.getMean());
                    lContent.append(iSep);
                }

            }
        }


        // REFERENCE STANDARD DEVIATION METRICS.
        if (iMatrixDataModel.hasSingleReference()) {
            AminoAcidStatistics lReferenceStatistics = iMatrixDataModel.getReferenceAminoAcidStatistics(0);
            lContent.append("\nSD_REF");
            lContent.append(iSep);
            for (AminoAcidEnum lAminoAcid : lAminoAcids) {
                if (lAminoAcid.getOneLetterCode() != 'X') {
                    StatisticalSummary lStatistics = lReferenceStatistics.getStatistics(lAminoAcid);
                    lContent.append(lStatistics.getStandardDeviation());
                    lContent.append(iSep);
                }
            }
        } else {
            int lNumberOfPositions = iMatrixDataModel.getNumberOfPositions();
            for (int i1 = 0; i1 < lNumberOfPositions; i1++) {
                AminoAcidStatistics lReferenceStatistics = iMatrixDataModel.getReferenceAminoAcidStatistics(i1);
                lContent.append("\nSD_REF_P" + (i1 + 1 ));
                lContent.append(iSep);

                for (AminoAcidEnum lAminoAcid : lAminoAcids) {
                    if (lAminoAcid.getOneLetterCode() != 'X') {
                        StatisticalSummary lStatistics = lReferenceStatistics.getStatistics(lAminoAcid);
                        lContent.append(lStatistics.getStandardDeviation());
                        lContent.append(iSep);
                    }
                }
            }
        }

        

        iContent = lContent.toString();
    }

    private String arrangeByAminoAcid(AminoAcidEnum[] aAminoAcids, HashMap aRegulatedEntitiesMap) {
        String s = "";
        for (int i = 0; i < aAminoAcids.length; i++) {
            Character lAminoAcid = aAminoAcids[i].getOneLetterCode();
            Object o = aRegulatedEntitiesMap.get(lAminoAcid);
            if (o != null) {
                s = s + o.toString() + iSep;
            }
        }
        return s;
    }

    /**
     * Gives a boolean that indicates if the saveble is an svg
     *
     * @return boolean
     */

    public boolean isSvg() {
        return false;
    }

    /**
     * Gives a boolean that indicates if the saveble is a chart
     *
     * @return boolean
     */
    public boolean isChart() {
        return false;
    }

    /**
     * This method gives a panel with the savable information
     *
     * @return JPanel
     */
    public JPanel getContentPanel() {
        return null;
    }

    /**
     * This method gives a SVG document if available
     *
     * @return SVGDocument
     */
    public SVGDocument getSVG() {
        return null;
    }

    /**
     * Getter for the title
     *
     * @return String with the title
     */
    public String getTitle() {
        return null;
    }

    /**
     * Getter for the description
     *
     * @return String with the description
     */
    public String getDescription() {
        return "CSV file with the Data model of the analysis";
    }

    /**
     * Gives a boolean that indicates if the saveble is text.
     *
     * @return
     */
    public boolean isText() {
        return true;
    }

    /**
     * Returns the text, if the Savable is Text type. Else returns null.
     *
     * @return
     */
    public String getText() {
        if (iContent == null) {
            buildContent();
        }
        ;
        return iContent;
    }
}
