package com.compomics.icelogo.gui.renderer;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.statistics.StatisticalCategoryDataset;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 28-aug-2008 Time: 14:09:52 The 'StatisticalBarRendererImpl ' class was
 * created for
 */
public class StatisticalBarRendererImpl extends StatisticalBarRenderer {
// ------------------------------ FIELDS ------------------------------

    private double iStandardDeviationFactor = 1.0;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Default constructor.
     */
    public StatisticalBarRendererImpl() {
        super();
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public void setStandardDeviationFactor(final double aStandardDeviationFactor) {
        iStandardDeviationFactor = aStandardDeviationFactor;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Draws an item for a plot with a horizontal orientation.
     *
     * @param g2         the graphics device.
     * @param state      the renderer state.
     * @param dataArea   the data area.
     * @param plot       the plot.
     * @param domainAxis the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset    the data.
     * @param row        the row index (zero-based).
     * @param column     the column index (zero-based).
     */
    protected void drawHorizontalItem(Graphics2D g2,
                                      CategoryItemRendererState state,
                                      Rectangle2D dataArea,
                                      CategoryPlot plot,
                                      CategoryAxis domainAxis,
                                      ValueAxis rangeAxis,
                                      StatisticalCategoryDataset dataset,
                                      int row,
                                      int column) {
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();

        // BAR Y
        double rectY = domainAxis.getCategoryStart(column, getColumnCount(),
                dataArea, xAxisLocation);

        int seriesCount = getRowCount();
        int categoryCount = getColumnCount();
        if (seriesCount > 1) {
            double seriesGap = dataArea.getHeight() * getItemMargin()
                    / (categoryCount * (seriesCount - 1));
            rectY = rectY + row * (state.getBarWidth() + seriesGap);
        } else {
            rectY = rectY + row * state.getBarWidth();
        }

        // BAR X
        Number meanValue = dataset.getMeanValue(row, column);
        if (meanValue == null) {
            return;
        }
        double value = meanValue.doubleValue();
        double base = 0.0;
        double lclip = getLowerClip();
        double uclip = getUpperClip();

        if (uclip <= 0.0) {  // cases 1, 2, 3 and 4
            if (value >= uclip) {
                return; // bar is not visible
            }
            base = uclip;
            if (value <= lclip) {
                value = lclip;
            }
        } else if (lclip <= 0.0) { // cases 5, 6, 7 and 8
            if (value >= uclip) {
                value = uclip;
            } else {
                if (value <= lclip) {
                    value = lclip;
                }
            }
        } else { // cases 9, 10, 11 and 12
            if (value <= lclip) {
                return; // bar is not visible
            }
            base = getLowerClip();
            if (value >= uclip) {
                value = uclip;
            }
        }

        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transY1 = rangeAxis.valueToJava2D(base, dataArea, yAxisLocation);
        double transY2 = rangeAxis.valueToJava2D(value, dataArea,
                yAxisLocation);
        double rectX = Math.min(transY2, transY1);

        double rectHeight = state.getBarWidth();
        double rectWidth = Math.abs(transY2 - transY1);

        Rectangle2D bar = new Rectangle2D.Double(rectX, rectY, rectWidth,
                rectHeight);
        Paint itemPaint = getItemPaint(row, column);
        GradientPaintTransformer t = getGradientPaintTransformer();
        if (t != null && itemPaint instanceof GradientPaint) {
            itemPaint = t.transform((GradientPaint) itemPaint, bar);
        }
        g2.setPaint(itemPaint);
        g2.fill(bar);

        // draw the outline...
        if (isDrawBarOutline()
                && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
            Stroke stroke = getItemOutlineStroke(row, column);
            Paint paint = getItemOutlinePaint(row, column);
            if (stroke != null && paint != null) {
                g2.setStroke(stroke);
                g2.setPaint(paint);
                g2.draw(bar);
            }
        }

        // standard deviation lines
        Number n = dataset.getStdDevValue(row, column);
        if (n != null) {
            double valueDelta = iStandardDeviationFactor * n.doubleValue();
            double highVal = rangeAxis.valueToJava2D(meanValue.doubleValue()
                    + valueDelta, dataArea, yAxisLocation);
            double lowVal = rangeAxis.valueToJava2D(meanValue.doubleValue()
                    - valueDelta, dataArea, yAxisLocation);

            if (getErrorIndicatorPaint() != null) {
                g2.setPaint(getErrorIndicatorPaint());
            } else {
                g2.setPaint(getItemOutlinePaint(row, column));
            }
            if (getErrorIndicatorStroke() != null) {
                g2.setStroke(getErrorIndicatorStroke());
            } else {
                g2.setStroke(getItemOutlineStroke(row, column));
            }
            Line2D line = null;
            line = new Line2D.Double(lowVal, rectY + rectHeight / 2.0d,
                    highVal, rectY + rectHeight / 2.0d);
            g2.draw(line);
            line = new Line2D.Double(highVal, rectY + rectHeight * 0.25,
                    highVal, rectY + rectHeight * 0.75);
            g2.draw(line);
            line = new Line2D.Double(lowVal, rectY + rectHeight * 0.25,
                    lowVal, rectY + rectHeight * 0.75);
            g2.draw(line);
        }

        CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
                column);
        if (generator != null && isItemLabelVisible(row, column)) {
            drawItemLabel(g2, dataset, row, column, plot, generator, bar,
                    (value < 0.0));
        }

        // addComponent an item entity, if this information is being collected
        EntityCollection entities = state.getEntityCollection();
        if (entities != null) {
            addItemEntity(entities, dataset, row, column, bar);
        }
    }

    /**
     * Draws an item for a plot with a vertical orientation.
     *
     * @param g2         the graphics device.
     * @param state      the renderer state.
     * @param dataArea   the data area.
     * @param plot       the plot.
     * @param domainAxis the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset    the data.
     * @param row        the row index (zero-based).
     * @param column     the column index (zero-based).
     */
    protected void drawVerticalItem(Graphics2D g2,
                                    CategoryItemRendererState state,
                                    Rectangle2D dataArea,
                                    CategoryPlot plot,
                                    CategoryAxis domainAxis,
                                    ValueAxis rangeAxis,
                                    StatisticalCategoryDataset dataset,
                                    int row,
                                    int column) {
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();

        // BAR X
        double rectX = domainAxis.getCategoryStart(column, getColumnCount(),
                dataArea, xAxisLocation);

        int seriesCount = getRowCount();
        int categoryCount = getColumnCount();
        if (seriesCount > 1) {
            double seriesGap = dataArea.getWidth() * getItemMargin()
                    / (categoryCount * (seriesCount - 1));
            rectX = rectX + row * (state.getBarWidth() + seriesGap);
        } else {
            rectX = rectX + row * state.getBarWidth();
        }

        // BAR Y
        Number meanValue = dataset.getMeanValue(row, column);
        if (meanValue == null) {
            return;
        }

        double value = meanValue.doubleValue();
        double base = 0.0;
        double lclip = getLowerClip();
        double uclip = getUpperClip();

        if (uclip <= 0.0) {  // cases 1, 2, 3 and 4
            if (value >= uclip) {
                return; // bar is not visible
            }
            base = uclip;
            if (value <= lclip) {
                value = lclip;
            }
        } else if (lclip <= 0.0) { // cases 5, 6, 7 and 8
            if (value >= uclip) {
                value = uclip;
            } else {
                if (value <= lclip) {
                    value = lclip;
                }
            }
        } else { // cases 9, 10, 11 and 12
            if (value <= lclip) {
                return; // bar is not visible
            }
            base = getLowerClip();
            if (value >= uclip) {
                value = uclip;
            }
        }

        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transY1 = rangeAxis.valueToJava2D(base, dataArea, yAxisLocation);
        double transY2 = rangeAxis.valueToJava2D(value, dataArea,
                yAxisLocation);
        double rectY = Math.min(transY2, transY1);

        double rectWidth = state.getBarWidth();
        double rectHeight = Math.abs(transY2 - transY1);

        Rectangle2D bar = new Rectangle2D.Double(rectX, rectY, rectWidth,
                rectHeight);
        Paint itemPaint = getItemPaint(row, column);
        GradientPaintTransformer t = getGradientPaintTransformer();
        if (t != null && itemPaint instanceof GradientPaint) {
            itemPaint = t.transform((GradientPaint) itemPaint, bar);
        }
        g2.setPaint(itemPaint);
        g2.fill(bar);
        // draw the outline...
        if (isDrawBarOutline()
                && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
            Stroke stroke = getItemOutlineStroke(row, column);
            Paint paint = getItemOutlinePaint(row, column);
            if (stroke != null && paint != null) {
                g2.setStroke(stroke);
                g2.setPaint(paint);
                g2.draw(bar);
            }
        }

        // standard deviation lines
        Number n = dataset.getStdDevValue(row, column);
        if (n != null) {
            double valueDelta = iStandardDeviationFactor * n.doubleValue();
            double highVal = rangeAxis.valueToJava2D(meanValue.doubleValue()
                    + valueDelta, dataArea, yAxisLocation);
            double lowVal = rangeAxis.valueToJava2D(meanValue.doubleValue()
                    - valueDelta, dataArea, yAxisLocation);

            if (getErrorIndicatorPaint() != null) {
                g2.setPaint(getErrorIndicatorPaint());
            } else {
                g2.setPaint(getItemOutlinePaint(row, column));
            }
            if (getErrorIndicatorStroke() != null) {
                g2.setStroke(getErrorIndicatorStroke());
            } else {
                g2.setStroke(getItemOutlineStroke(row, column));
            }

            Line2D line = null;
            line = new Line2D.Double(rectX + rectWidth / 2.0d, lowVal,
                    rectX + rectWidth / 2.0d, highVal);
            g2.draw(line);
            line = new Line2D.Double(rectX + rectWidth / 2.0d - 5.0d, highVal,
                    rectX + rectWidth / 2.0d + 5.0d, highVal);
            g2.draw(line);
            line = new Line2D.Double(rectX + rectWidth / 2.0d - 5.0d, lowVal,
                    rectX + rectWidth / 2.0d + 5.0d, lowVal);
            g2.draw(line);
        }

        CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
                column);
        if (generator != null && isItemLabelVisible(row, column)) {
            drawItemLabel(g2, dataset, row, column, plot, generator, bar,
                    (value < 0.0));
        }

        // addComponent an item entity, if this information is being collected
        EntityCollection entities = state.getEntityCollection();
        if (entities != null) {
            addItemEntity(entities, dataset, row, column, bar);
        }
    }
}
