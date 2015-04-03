package com.compomics.icelogo.core.data;

import com.compomics.icelogo.core.enumeration.ScoringTypeEnum;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 9-okt-2008
 * Time: 8:38:17
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class stores a vector with PositiveRegulatedEntities an a vector with NegativeRegulatedEntities for one position
 */
public class RegulatedPosition {

    /**
     * Vector with the PositiveRegulatedEntities
     */
    public Vector iPositveRegulatedEntity = new Vector();
    /**
     * Vector with the NegativeRegulatedEntities
     */
    public Vector iNegativeRegulatedEntity = new Vector();

    /**
     * Constructor
     */
    public RegulatedPosition() {

    }

    /**
     * Add a positive regulated entity
     *
     * @param pos A RegulatedEntity
     */
    public void addPositveRegulatedEntity(RegulatedEntity pos) {
        iPositveRegulatedEntity.add(pos);
    }

    /**
     * Add a negativ regulated entity
     *
     * @param neg A RegulatedEntity
     */
    public void addNegativeRegulatedEntity(RegulatedEntity neg) {
        iNegativeRegulatedEntity.add(neg);
    }

    /**
     * This method will get the PositiveRegulatedEntities, meanwhile it will set the scoring type of that RegulatedEntity
     *
     * @param aScoreType ScoringTypeEnum
     * @return RegulatedEntity[]
     */
    public RegulatedEntity[] getPositiveRegulatedEntity(ScoringTypeEnum aScoreType) {
        RegulatedEntity[] entities = new RegulatedEntity[iPositveRegulatedEntity.size()];
        for (int e = 0; e < iPositveRegulatedEntity.size(); e++) {
            ((RegulatedEntity) iPositveRegulatedEntity.get(e)).setScoreType(aScoreType);
            ;
        }
        Collections.sort(iPositveRegulatedEntity);
        iPositveRegulatedEntity.toArray(entities);
        return entities;
    }

    /**
     * This method will get the NegativeRegulatedEntities, meanwhile it will set the scoring type of that RegulatedEntity and re-order the array from lesser to larger values.
     *
     * @param aScoreType ScoringTypeEnum
     * @return RegulatedEntity[]
     */
    public RegulatedEntity[] getNegativeRegulatedEntity(ScoringTypeEnum aScoreType) {
        RegulatedEntity[] entities = new RegulatedEntity[iNegativeRegulatedEntity.size()];
        for (int e = 0; e < iNegativeRegulatedEntity.size(); e++) {
            ((RegulatedEntity) iNegativeRegulatedEntity.get(e)).setScoreType(aScoreType);
            ;
        }
        Collections.sort(iNegativeRegulatedEntity);
        for (int i = 0; i < iNegativeRegulatedEntity.size(); i++) {
            entities[i] = (RegulatedEntity) iNegativeRegulatedEntity.get(iNegativeRegulatedEntity.size() - 1 - i);
        }
        return entities;
    }
}
