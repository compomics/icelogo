package com.compomics.icelogo.core.aaindex;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 14, 2009
 * Time: 11:56:53 AM
 * <p/>
 * This class
 */
public abstract class AAIndexMatrix {
    /**
     * The AaIndex id
     */
    protected String iAaindexId;
    /**
     * The title
     */
    protected String iTitle;
    /**
     * The description (This is the title of the publication)
     */
    protected String iDescription;
    /**
     * Pubmed id of the publication
     */
    protected String iPubMedId;
    /**
     * The lit id
     */
    protected String iLitId;
    /**
     * The authors
     */
    protected String iAuthors;
    /**
     * The journal
     */
    protected String iJournal;
    /**
     * The matrix (in text)
     */
    protected String iMatrix;
    /**
     * A hashmap with all the values from the matrix for the different aa
     */
    protected HashMap iAaMap = new HashMap();


    /**
     * The constructor
     *
     * @param aAaindexId   a AaIndexId
     * @param aTitle       The title
     * @param aDescription The description
     * @param aPubMedId    The pubmedid
     * @param aLitId       The Litid
     * @param aAuthors     The authors
     * @param aJournal     The journal
     * @param aMatrix      The matrix
     */
    public AAIndexMatrix(String aAaindexId, String aTitle, String aDescription, String aPubMedId, String aLitId, String aAuthors, String aJournal, String aMatrix) {
        iAaindexId = aAaindexId;
        iTitle = aTitle;
        iDescription = aDescription;
        iPubMedId = aPubMedId;
        iLitId = aLitId;
        iAuthors = aAuthors;
        iJournal = aJournal;
        iMatrix = aMatrix;
    }

    /**
     * Abstract method parsing the actual matrix from an aaindex1 or aaindex2 element.
     */
    public abstract void parseMatrix();

    public abstract Object getValueForAminoAcid(AminoAcidEnum lAa);

    public abstract String toString();

    /**
     * Getter for the title parameter
     *
     * @return String with the title
     */
    public String getTitle() {
        return iTitle;
    }

    /**
     * Getter for the AaIndexId parameter
     *
     * @return String with the AaIndexId
     */
    public String getAaindexId() {
        return iAaindexId;
    }

    /**
     * Getter for the iDescription parameter
     *
     * @return String with the iDescription
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     * Getter for the iPubMedId parameter
     *
     * @return String with the iPubMedId
     */
    public String getPubMedId() {
        return iPubMedId;
    }

    /**
     * Getter for the iLitId parameter
     *
     * @return String with the iLitId
     */
    public String getLitId() {
        return iLitId;
    }

    /**
     * Getter for the iAuthors parameter
     *
     * @return String with the iAuthors
     */
    public String getAuthors() {
        return iAuthors;
    }

    /**
     * Getter for the iJournal parameter
     *
     * @return String with the iJournal
     */
    public String getJournal() {
        return iJournal;
    }

    /**
     * Getter for the iMatrix parameter
     *
     * @return String with the iMatrix
     */
    public String getMatrix() {
        return iMatrix;
    }

    public abstract HashMap getAaMap();
}
