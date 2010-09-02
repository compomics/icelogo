package com.compomics.icelogo.core.dbComposition;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 9-okt-2008
 * Time: 14:14:31
 * <p/>
 * This object holds the link to the composition file and the species name
 */
public class SwissProtComposition {

    public String iSpecieLink;
    public String iSpecieName;
    private String iComposition;

    /**
     * @param aSpeciesLink A link to the text file that holds the composition for a specific species
     * @param aSpeciesName The name of the specie
     */
    public SwissProtComposition(String aSpeciesLink, String aSpeciesName) {
        this.iSpecieLink = aSpeciesLink;
        this.iSpecieName = aSpeciesName;
    }

    public String getSpecieLink() {
        return iSpecieLink;
    }

    public String getSpecieName() {
        return iSpecieName;
    }

    public String toString() {
        return iSpecieName;
    }

    public void setComposition(String aComposition) {
        this.iComposition = aComposition;
    }

    public String getComposition() {
        return iComposition;
    }
}
