package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.interfaces.MatrixDataModel;
import com.compomics.icelogo.core.enumeration.ObservableEnum;
import com.compomics.icelogo.core.enumeration.IceLogoEnum;
import com.compomics.icelogo.core.model.TwoSampleMatrixDataModel;
import com.compomics.icelogo.gui.factory.ChartFactory;
import com.compomics.icelogo.gui.interfaces.Savable;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 2-sep-2008 Time: 15:18:54 The 'SlidingBarChartPanel ' class was created
 * for
 */
public class BarChartForm extends JPanel implements Observer, Savable {
// ------------------------------ FIELDS ------------------------------

    /**
     * The content panel.
     */
    private JPanel jpanContent;
    /**
     * The chart panel.
     */
    private JPanel jpanChart;

    /**
     * The slider panel.
     */
    private JPanel jpanSlider;
    /**
     * The position slider.
     */
    private JSlider sldPosition;
    private JPanel jpanLegend;
    private JLabel lblRefColor;
    private JLabel lblPosColor;
    private JLabel lblPosTwoColor;
    private JLabel lblRefText;
    private JLabel lblPosText;
    private JLabel lblPosTwoText;
    private JPanel jpanGraphable;

    /**
     * The MatrixDataModel that is shown.
     */
    private MatrixDataModel iMatrixDataModel;


    private JFreeChart iBarChart;
    private int iLastSliderValue;

// --------------------------- CONSTRUCTORS ---------------------------

    public BarChartForm(final MatrixDataModel aMatrixDataModel) {
        super();

        MainInformationFeeder.getInstance().addObserver(this);
        iMatrixDataModel = aMatrixDataModel;

        $$$setupUI$$$();

        updateGraph(MainInformationFeeder.getInstance().getStartPosition());

        this.add(jpanContent);
    }


    private void buildSlider() {
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();
        DefaultBoundedRangeModel slideModel =
                new DefaultBoundedRangeModel(lFeeder.getStartPosition(), 0, lFeeder.getStartPosition(), lFeeder.getStartPosition() + (lFeeder.getNumberOfExperimentalPositions() - 1));
        sldPosition = new JSlider(slideModel);
        sldPosition.setBackground(new Color(-1));


        sldPosition.setMajorTickSpacing(1);
        sldPosition.setPaintTicks(true);
        sldPosition.setPaintTrack(true);
        sldPosition.setPaintLabels(true);

        sldPosition.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
                iLastSliderValue = ((JSlider) (e.getSource())).getValue();
                updateGraph(iLastSliderValue);
            }
        });

        sldPosition.addKeyListener(new KeyAdapter() {
            public void keyReleased(final KeyEvent e) {
                if (sldPosition.hasFocus()) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        iLastSliderValue = ((JSlider) (e.getSource())).getValue();
                        updateGraph(iLastSliderValue);
                    }
                }
            }
        });
    }

    private void createUIComponents() {
        buildSlider();
    }

    /**
     * Depending on the content, this method creates an appropriate label for the Barchart.
     */
    private void makeLegend() {
        MainInformationFeeder lFeeder = MainInformationFeeder.getInstance();

        // Set the legend colors.
        lblRefColor.setBackground(ChartFactory.iReferenceBarColor);
        lblPosColor.setBackground(ChartFactory.iPositionBarColor);
        lblPosTwoColor.setBackground(ChartFactory.iPositionTwoBarColor);

        // Legend text container.
        StringBuilder sb;

        // Reference legend Text.
        sb = new StringBuilder();

        // Append the sampling type if needed.
        IceLogoEnum lIceLogoType = lFeeder.getIceLogoType();
        if (lIceLogoType == IceLogoEnum.SAMPLING) {
            sb.append("random sampling - ");
        }

        // Append the Identifier for the reference set.
        String lReferenceID = iMatrixDataModel.getReferenceID();
        sb.append(lReferenceID + " - ");

        // Append the position in the reference set.
        int lReferenceIndex = sldPosition.getValue();
        sb.append("position " + lReferenceIndex);

        lblRefText.setText(sb.toString());


        // Position legend Text.
        String lExperimentID = "experiment";
        if (iMatrixDataModel instanceof TwoSampleMatrixDataModel) {
            lExperimentID = lExperimentID + " 1";
        }
        String lExperimentText = lExperimentID + " position " + lReferenceIndex;
        lblPosText.setText(lExperimentText);

        if (iMatrixDataModel instanceof TwoSampleMatrixDataModel) {
            // Experiment Two legend Text.
            String lExperimentTwoID = "experiment 2";
            String lExperimentTwoText = lExperimentTwoID + " position " + lReferenceIndex;
            lblPosTwoText.setText(lExperimentTwoText);
            lblPosTwoColor.setVisible(true);
            lblPosTwoText.setVisible(true);
        } else {
            lblPosTwoColor.setVisible(false);
            lblPosTwoText.setVisible(false);
        }

    }

    private void updateGraph(final int i) {
        int index = i - sldPosition.getMinimum();

        if (jpanChart.getComponentCount() == 1) {
            jpanChart.remove(0);
        }

        iBarChart = ChartFactory.createBarchartPanel(iMatrixDataModel, index);
        jpanChart.add(new ChartPanel(iBarChart), 0);

        // Construct the legend.
        makeLegend();

        jpanContent.updateUI();
        jpanContent.repaint();
    }

// -------------------------- OTHER METHODS --------------------------

    public void update(final Observable o, final Object arg) {

        if (arg.equals(ObservableEnum.NOTIFY_STATISTICAL)) {
            this.updateGraph(sldPosition.getValue());
        } else if (arg.equals(ObservableEnum.NOTIFY_START_POSITION)) {
            this.buildSlider();
            jpanSlider.remove(1);
            GridBagConstraints gbc;
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            jpanSlider.add(sldPosition, gbc);
            sldPosition.repaint();
            sldPosition.updateUI();
            this.updateGraph(sldPosition.getValue());            
        }
    }

    public boolean isSvg() {
        return false;
    }

    public boolean isChart() {
        return true;
    }

    public JPanel getContentPanel() {
        return jpanContent;
    }

    public SVGDocument getSVG() {
        return null;
    }

    public String getTitle() {
        return "slidingBar";
    }

    public String getDescription() {
        return "Barchart with aa occurence for position: " + sldPosition.getValue();
    }

    /**
     * Gives a boolean that indicates if the saveble is text.
     *
     * @return
     */
    public boolean isText() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public JFreeChart getChart() {
        //return iBarChart;
        return null;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        jpanContent.setBackground(new Color(-1));
        jpanSlider = new JPanel();
        jpanSlider.setLayout(new GridBagLayout());
        jpanSlider.setBackground(new Color(-1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        jpanContent.add(jpanSlider, gbc);
        final JLabel label1 = new JLabel();
        label1.setBackground(new Color(-1));
        label1.setHorizontalAlignment(11);
        label1.setText("Position: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 40;
        jpanSlider.add(label1, gbc);
        sldPosition.setBackground(new Color(-1));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanSlider.add(sldPosition, gbc);
        jpanGraphable = new JPanel();
        jpanGraphable.setLayout(new GridBagLayout());
        jpanGraphable.setBackground(new Color(-1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanGraphable, gbc);
        jpanChart = new JPanel();
        jpanChart.setLayout(new BorderLayout(10, 10));
        jpanChart.setBackground(new Color(-1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        jpanGraphable.add(jpanChart, gbc);
        jpanChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(jpanChart.getFont().getName(), jpanChart.getFont().getStyle(), jpanChart.getFont().getSize()), new Color(-16777216)));
        jpanLegend = new JPanel();
        jpanLegend.setLayout(new GridBagLayout());
        jpanLegend.setBackground(new Color(-1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 10, 0, 10);
        jpanGraphable.add(jpanLegend, gbc);
        lblRefColor = new JLabel();
        lblRefColor.setBackground(new Color(-6710887));
        lblRefColor.setMaximumSize(new Dimension(15, 15));
        lblRefColor.setMinimumSize(new Dimension(15, 15));
        lblRefColor.setOpaque(true);
        lblRefColor.setPreferredSize(new Dimension(15, 15));
        lblRefColor.setText(" ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);
        jpanLegend.add(lblRefColor, gbc);
        lblPosColor = new JLabel();
        lblPosColor.setBackground(new Color(-6710887));
        lblPosColor.setMaximumSize(new Dimension(15, 15));
        lblPosColor.setMinimumSize(new Dimension(15, 15));
        lblPosColor.setOpaque(true);
        lblPosColor.setPreferredSize(new Dimension(15, 15));
        lblPosColor.setText(" ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);
        jpanLegend.add(lblPosColor, gbc);
        lblPosTwoColor = new JLabel();
        lblPosTwoColor.setBackground(new Color(-6710887));
        lblPosTwoColor.setMaximumSize(new Dimension(15, 15));
        lblPosTwoColor.setMinimumSize(new Dimension(15, 15));
        lblPosTwoColor.setOpaque(true);
        lblPosTwoColor.setPreferredSize(new Dimension(15, 15));
        lblPosTwoColor.setText(" ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);
        jpanLegend.add(lblPosTwoColor, gbc);
        lblRefText = new JLabel();
        lblRefText.setText("RefText");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 0);
        jpanLegend.add(lblRefText, gbc);
        lblPosText = new JLabel();
        lblPosText.setText("PosText");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 0);
        jpanLegend.add(lblPosText, gbc);
        lblPosTwoText = new JLabel();
        lblPosTwoText.setText("PosTwoText");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 0);
        jpanLegend.add(lblPosTwoText, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
