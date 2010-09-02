package com.compomics.icelogo.core.data;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.enumeration.ColorEnum;
import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;

/**
 * Created by IntelliJ IDEA. User: Niklaas Colaert Date: 8-aug-2008 Time: 10:16:45 To change this template use File |
 * Settings | File Templates.
 */

/**
 * This class holds values for a RegulatedEntity.
 */
public class RegulatedEntity implements Comparable {
// ------------------------------ FIELDS ------------------------------

    /**
     * The regulated amino acid
     */
    public AminoAcidEnum iAminoAcid = null;
    /**
     * double with the percentage change
     */
    public double iPercentageChange;
    /**
     * double with the fold change
     */
    public double iFoldChange;
    /**
     * double with the standard deviation change
     */
    public double iSDchange;
    /**
     * The color of this AminoAcid/RegulatedEntity
     */
    public ColorEnum iColor = ColorEnum.BLACK;
    /**
     * Is the infinite (if one of the values to calculate the foldchange with is zero)
     */
    public boolean iInfinite = false;
    /**
     * The scoring type
     */
    public ScoringTypeEnum iScoreType;
    /**
     * double with the frequence
     */
    public double iFrequency;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructor
     *
     * @param aAminoAcid        The AminoAcidEnum
     * @param aPercentageChange The percentage change
     * @param aFoldChange       The fold change
     * @param aSDchange         The standard deviation change
     * @param aFrequency        The frequence
     */
    public RegulatedEntity(AminoAcidEnum aAminoAcid, double aPercentageChange, double aFoldChange, double aSDchange, double aFrequency) {
        this.iAminoAcid = aAminoAcid;
        this.iPercentageChange = aPercentageChange;
        this.iFoldChange = aFoldChange;
        this.iSDchange = aSDchange;
        this.iFrequency = aFrequency;

    }

// --------------------- GETTER / SETTER METHODS ---------------------


    /**
     * Getter for the color
     *
     * @return ColorEnum
     */
    public ColorEnum getColorName() {
        return iColor;
    }

    /**
     * Getter for the AminoAcid
     *
     * @return AminoAcidEnum
     */
    public char getAminoAcid() {
        return iAminoAcid.getOneLetterCode();
    }

    /**
     * Setter for the AminoAcid
     *
     * @param aAminoAcid The aminoacid to set
     */
    public void setAminoAcid(AminoAcidEnum aAminoAcid) {
        this.iAminoAcid = aAminoAcid;
    }

    /**
     * Setter for the color
     *
     * @param aColor The color to set
     */
    public void setColor(ColorEnum aColor) {
        this.iColor = aColor;
    }

    /**
     * This method will give the change depending on the giver ScoringTypeEnum
     *
     * @param aType ScoringTypeEnum
     * @return double with the change
     */
    public double getChange(ScoringTypeEnum aType) {
        if (aType == ScoringTypeEnum.FOLDCHANGE) {
            return iFoldChange;
        } else if (aType == ScoringTypeEnum.PERCENTAGE) {
            return iPercentageChange * 100;
        } else if (aType == ScoringTypeEnum.STANDARD_DEVIATION) {
            return iSDchange;
        } else {
            return iFrequency;
        }
    }

    /**
     * This method can set the change only if the giver ScoringTypeEnum is FOLDCHANGE
     *
     * @param aType   ScoringTypeEnum
     * @param aChange The change to set
     */
    public void setChange(ScoringTypeEnum aType, double aChange) {
        if (aType == ScoringTypeEnum.FOLDCHANGE) {
            iFoldChange = aChange;
        }
    }

    /**
     * Getter for the Infinite parameter
     *
     * @return boolean
     */
    public boolean getInfinite() {
        return iInfinite;
    }

    /**
     * Getter for the frequencey
     *
     * @return double with the frequencey
     */
    public double getFrequency() {
        return iFrequency;
    }

    /**
     * Setter for the Infinite
     *
     * @param aInfinite boolean
     */
    public void setInfinite(boolean aInfinite) {
        iInfinite = aInfinite;
    }

    /**
     * This method compares one RegulatedEntity with an other
     *
     * @param o The RegulatedEntity
     * @return int with the status of the comparing
     */
    public int compareTo(Object o) {
        RegulatedEntity ent = (RegulatedEntity) o;
        if (ent.getChange(iScoreType) > this.getChange(iScoreType)) {
            return -1;
        } else if (ent.getChange(iScoreType) < this.getChange(iScoreType)) {
            return 1;
        }
        return 0;
    }

    /**
     * Setter for the scoring type
     *
     * @param aScoreType The ScoringTypeEnum to set
     */
    public void setScoreType(ScoringTypeEnum aScoreType) {
        this.iScoreType = aScoreType;
    }
}
