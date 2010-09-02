package com.compomics.icelogo.gui.runner;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.MatrixAminoAcidStatistics;
import com.compomics.icelogo.core.data.sequenceset.FastaSequenceSet;
import com.compomics.icelogo.core.data.sequenceset.RegionalFastaSequenceSet;
import com.compomics.icelogo.core.enumeration.*;
import com.compomics.icelogo.core.factory.AminoAcidStatisticsFactory;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.interfaces.ISequenceSet;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import com.compomics.icelogo.core.model.TwoSampleMatrixDataModel;
import com.compomics.icelogo.gui.component.Messenger;
import com.compomics.icelogo.gui.graph.*;
import com.compomics.icelogo.gui.forms.BarChartForm;
import com.compomics.icelogo.gui.forms.wizard.SamplingWizard;
import com.compomics.icelogo.gui.interfaces.GraphableAcceptor;
import com.compomics.util.sun.SwingWorker;


/**
 * Created by IntelliJ IDEA. User: kenny Date: Jun 24, 2009 Time: 11:46:32 PM
 * <p/>
 * This class
 */
public class SamplingRunner extends SwingWorker {
    private SamplingTypeEnum iSamplingTypeEnum;
    private SamplingWizard iSamplingWizard;
    private GraphableAcceptor iAcceptor = null;
    private MainInformationFeeder iFeeder;
    private Messenger iMessenger;

    public SamplingRunner(SamplingTypeEnum aSamplingTypeEnum, final SamplingWizard aSamplingWizard, final GraphableAcceptor aAcceptor) {
        iSamplingTypeEnum = aSamplingTypeEnum;
        iSamplingWizard = aSamplingWizard;
        iAcceptor = aAcceptor;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    public Object construct() {

        iSamplingWizard.setForwordButtonEnabled(false);
        iMessenger = Messenger.getInstance();


        // First degree of separation is the Sampling type for creating the reference set.
        AminoAcidStatistics[] lReferenceStatistics = null;
        iFeeder = MainInformationFeeder.getInstance();

        message(iSamplingTypeEnum.getName() + " sampling for the reference set");

        lReferenceStatistics = createReferenceStatistics(lReferenceStatistics);

        // Now create the position sets.
        // Always create the first position set.
        message(" Constructing the position set .. ");

        AminoAcidStatistics[] lPositionStatistics = createPositionStatistics();
        AminoAcidStatistics[] lPositionTwoStatistics = createPositionTwoStatistics();

        // The final lModel to be created.
        MatrixDataModel lModel;

        String lReferenceID = iFeeder.getActiveReferenceSet().getID();

        //create the datamodel

        if (lPositionTwoStatistics == null) {
            // Create a OneSampleMatrixDataModel
            lModel = new OneSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lReferenceID);
        } else {
            // Create a TwoSampleMatrixDataModel
            lModel = new TwoSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lPositionTwoStatistics, lReferenceID);

        }

        updateGraphics(lModel);
        return null;
    }

    private AminoAcidStatistics[] createPositionTwoStatistics() {
        AminoAcidStatistics[] lPositionTwoStatistics = null;
        if (iFeeder.isTwoExperiment()) {
            message(" Constructing the second position set");
            ISequenceSet lPositionSetTwo = iFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
            lPositionTwoStatistics = createSequenceSetStatistics(lPositionSetTwo);
        }
        return lPositionTwoStatistics;
    }

    private AminoAcidStatistics[] createPositionStatistics() {
        ISequenceSet lPositionSet = iFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        AminoAcidStatistics[] lPositionStatistics = createSequenceSetStatistics(lPositionSet);

        return lPositionStatistics;
    }

    private AminoAcidStatistics[] createSequenceSetStatistics(final ISequenceSet aPositionSet) {
        SamplingDirectionEnum lDirection = SamplingDirectionEnum.NtermToCterm;
        int lVerticalOffset = 0;
        int lIterationSize = 1;
        int lLength = iFeeder.getNumberOfExperimentalPositions();

        AminoAcidStatistics[] lPositionStatistics = null;
        lPositionStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(
                aPositionSet,
                lIterationSize,
                lVerticalOffset,
                lLength,
                lDirection);
        return lPositionStatistics;
    }

    private AminoAcidStatistics[] createReferenceStatistics(AminoAcidStatistics[] aReferenceStatistics) {
        ISequenceSet lSequenceSet = iFeeder.getActiveReferenceSet();
        switch (iSamplingTypeEnum) {

            case HORIZONTAL:
                int lHorizontalSamplingLength = iFeeder.getHorizontalSamplingLength();
                int lHorizontalSamplingOffset = iFeeder.getAnchorStartPosition();
                aReferenceStatistics = new MatrixAminoAcidStatistics[]{AminoAcidStatisticsFactory.createHorizontalPositionAminoAcidMatrix(
                        lSequenceSet,
                        lHorizontalSamplingLength,
                        lHorizontalSamplingOffset)};
                break;

            case RANDOM:
                int lSamplingSize = iFeeder.getPositionSampleSize();
                SamplingStrategy lStrategy = SamplingStrategy.ALL;
                int lNumberOfIterations = iFeeder.getIterationSize();
                aReferenceStatistics = new MatrixAminoAcidStatistics[]{AminoAcidStatisticsFactory.createRandomSampleAminoAcidMatrix(
                        lSequenceSet,
                        lSamplingSize,
                        lNumberOfIterations,
                        lStrategy.getInstance())};
                break;

            case REGIONAL:
                // In order to perform the Regional sampling, first we need to
                // fetch the anchored amino acids.
                int lExperimentAnchorValue = iFeeder.getExperimentAnchorValue();
                int lNumberOfExperimentalPositions = iFeeder.getNumberOfExperimentalPositions();
                int lPrefix = lExperimentAnchorValue; // If anchor is set to 5 (0-based), then there are 5 preceding amino acids.
                int lSuffix = lNumberOfExperimentalPositions - 1 - lExperimentAnchorValue; // If there are 12 positions, there are 6(=12-5-1) following amino acids.

                AminoAcidEnum[] lAminoAcids = iFeeder.getExperimentalAminoAcids(lExperimentAnchorValue);

                // Make sure the sequenceSet is a FastaSequenceSet.
                assert lSequenceSet instanceof FastaSequenceSet;
                // Create a RegionalFastaSequenceSet for the given experimental anchor and the required preceding and following aminoacids.
                RegionalFastaSequenceSet lRegionalFastaSequenceSet =
                        ((FastaSequenceSet) lSequenceSet).deriveRegionalSequenceSet(lAminoAcids, lPrefix, lSuffix);

                int lRegionalOffset = 0;
                int lRegionalIterationSize = iFeeder.getIterationSize();
                int lRegionalLength = iFeeder.getNumberOfExperimentalPositions();

                aReferenceStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(
                        lRegionalFastaSequenceSet,
                        lRegionalIterationSize,
                        lRegionalOffset,
                        lRegionalLength,
                        SamplingDirectionEnum.NtermToCterm);
                break;

            case TERMINAL:
                int lVerticalOffset = iFeeder.getAnchorStartPosition();
                int lIterationSize = iFeeder.getIterationSize();
                int lSubsetSize = iFeeder.getPositionSampleSize();
                int lLength = iFeeder.getNumberOfExperimentalPositions();
                SamplingDirectionEnum lDirection = iFeeder.getReferenceDirection();

                aReferenceStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(
                        lSequenceSet,
                        lIterationSize,
                        lVerticalOffset,
                        lLength,
                        lSubsetSize,
                        lDirection);
                break;
        }
        return aReferenceStatistics;
    }


    /**
     * Update the graphic panels after the MatrixDataModel has been created.
     *
     * @param aModel
     */
    private void updateGraphics(final MatrixDataModel aModel) {
        iSamplingWizard.setForwordButtonEnabled(true);
        iAcceptor.removeAll();
        iAcceptor.removeAllSavables();

        iFeeder.setGraphableHeight(iSamplingWizard.getContent().getHeight());
        iFeeder.setGraphableWidth(iSamplingWizard.getContent().getWidth());

        iAcceptor.addComponent(iSamplingWizard.getContent(), "Home");

        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        // Add the logo
        if (lFeeder.isUseIceLogo()) {
            IceLogoComponent logo = new IceLogoComponent(aModel, false);
            iAcceptor.addComponent(logo, "iceLogo");
            iAcceptor.addGraphable(logo);
        }

        //Add the barchart
        if (lFeeder.isUseBarchart()) {
            BarChartForm lSliding = new BarChartForm(aModel);
            iAcceptor.addComponent(lSliding.$$$getRootComponent$$$(), "Bar chart");
            iAcceptor.addGraphable(lSliding);
        }

        /*if (lFeeder.isUseKlogoGraph()) {
            // Get the clustered sequence sets from the information feeder.
            SequenceClusterer k = MainInformationFeeder.getInstance().getSequenceClusterer();
            ISequenceSet[] lClusteredSequenceSets = k.getSequenceClusters();

            // The cluster models to be.
            MatrixDataModel[] lClusteredMatrixDatamodels = new MatrixDataModel[k.getClusterCount()];

            // Get the reference statistics.
            int lNumberOfPositions = aModel.getNumberOfPositions();
            AminoAcidStatistics[] lReferenceStatistics = new AminoAcidStatistics[lNumberOfPositions];
            for (int i = 0; i < lNumberOfPositions; i++) {
                lReferenceStatistics[i] = aModel.getReferenceAminoAcidStatistics(i);
            }

            // Now re-make the models based on the distinct clusters.
            for (int i = 0; i < lClusteredSequenceSets.length; i++) {
                // Get aminoacid statistics per sequence set cluster.
                AminoAcidStatistics[] lClusterExperimentStatistics = createSequenceSetStatistics(lClusteredSequenceSets[i]);

                String lName = "cluster" + (i + 1);
                lClusteredMatrixDatamodels[i] =
                        new OneSampleMatrixDataModel(lReferenceStatistics, lClusterExperimentStatistics, lName);
            }

            KLogoComponent lKlogo = new KLogoComponent(lClusteredMatrixDatamodels);

            iAcceptor.addComponent(lKlogo.getContentPanel(), "K-Logo");

        }    */


        // Add the sequence logo
        if (lFeeder.isUseSequenceLogo() && !iFeeder.isTwoExperiment()) {
            SequenceLogoComponent lSequenceLogo = new SequenceLogoComponent(aModel);
            iAcceptor.addComponent(lSequenceLogo, "Sequence logo");
            iAcceptor.addGraphable(lSequenceLogo);
        }

        // Add the AaParamterComponent
        if (lFeeder.isUseAaParameterGraph()) {
            AAIndexComponent lAaParam = new AAIndexComponent(aModel);
            iAcceptor.addComponent(lAaParam, "Aa Parameter");
            iAcceptor.addGraphable(lAaParam);
        }
        // Add the HeatMap
        if (lFeeder.isUseHeatmap()) {
            HeatMapComponent lHeatmap = new HeatMapComponent(aModel);
            iAcceptor.addComponent(lHeatmap, "Heat map");
            iAcceptor.addGraphable(lHeatmap);
        }

        // Add the conservation logo
        if (lFeeder.isUseConservationLogo()) {
            ConservationComponent lCons = new ConservationComponent(aModel);
            iAcceptor.addComponent(lCons, "Conservation line");
            iAcceptor.addGraphable(lCons);
        }
        iSamplingWizard.getContent().updateUI();
    }

    /**
     * Called on the event dispatching thread (not on the worker thread) after the <code>construct</code> method has
     * returned.
     */
    @Override
    public void finished() {
        message("Finished sampling.");
        iMessenger.setProgressIndeterminate(false);

        super.finished();
    }

    /**
     * Send a message to the progressbar.
     *
     * @param aMessage
     */
    private void message(Object aMessage) {
        iMessenger.sendMessage(aMessage);
    }
}
