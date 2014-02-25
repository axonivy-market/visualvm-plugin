/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MGaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import eu.hansolo.steelseries.gauges.RadialBargraph;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.Section;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author thtam
 */
public class MGauge implements IUpdatableUIObject {

  private RadialBargraph fRadial;
  private static final Rectangle2D DEFAULT_LCD_RECTANGLE = new Rectangle2D.Double(0.55D, 0.75D, 0.27D, 0.1D);
  private final List<Section> fSections;
  private final MGaugeDataSource fDataSource;

  public MGauge(MGaugeDataSource dataSource) {
    fDataSource = dataSource;
    fSections = dataSource.getSections();
    initGauge();
  }

  private void initGauge() {
    fRadial = new RadialBargraph();
    if (!fSections.isEmpty()) {
      ((RadialBargraph) fRadial).setMinValue(((Section) fSections.get(0)).getStart());
      ((RadialBargraph) fRadial).setMaxValue(((Section) fSections.get(fSections.size() - 1)).getStop());

      for (Section section : fSections) {
        ((RadialBargraph) fRadial).addSection(section);
      }
    }
    fRadial.setSectionsVisible(true);
    fRadial.setNiceScale(false);

    if (!fSections.isEmpty()) {
      double delta = ((Section) fSections.get(fSections.size() - 1)).getStop() - ((Section) fSections.get(0)).
              getStart();
      int digit = (int) Math.floor(Math.log10(delta));

      double tickSpacing = chooseGaugeTickSpacing(Double.valueOf(delta), digit).doubleValue();
      ((RadialBargraph) fRadial).setMinorTickSpacing(tickSpacing / 10.0D);
      ((RadialBargraph) fRadial).setMajorTickSpacing(tickSpacing);
    }
    fRadial.setTitle("");
    fRadial.setUserLedVisible(false);
    fRadial.setBackgroundVisible(false);
    fRadial.setBackgroundColor(BackgroundColor.TRANSPARENT);
    fRadial.setUnitString("");
    fRadial.getGaugeType().LCD_FACTORS.setRect(DEFAULT_LCD_RECTANGLE);
    fRadial.setFrameVisible(false);
    fRadial.setLedVisible(false);
    fRadial.getModel().setGaugeType(GaugeType.TYPE4);
    fRadial.setLcdColor(LcdColor.CUSTOM);
    fRadial.setCustomLcdBackground(Color.WHITE);
    fRadial.setCustomLcdForeground(Color.BLUE);
  }

  public JComponent getUi() {
    return fRadial;
  }

  private Double chooseGaugeTickSpacing(Double range, int digit) {
    double d = 1.0D;
    if (digit == -2) {
      d = 0.01D;
    } else if (digit == -1) {
      d = 0.1D;
    } else if (digit == 0) {
      d = 1.0D;
    } else if (digit > 0) {
      d = calculateGaugeTickSpacing(range, digit).doubleValue();
    }
    return Double.valueOf(d);
  }

  private Double calculateGaugeTickSpacing(Double range, int digit) {
    double round = Math.round(range.doubleValue());
    int tenPower = (int) Math.pow(10.0D, digit);
    double fraction = round / tenPower;
    double tickSpacing;
    if (digit < 3) {
      tickSpacing = Math.round(fraction) * tenPower / 10L;
    } else {
      tickSpacing = Math.ceil(fraction) * tenPower / 10.0D;
    }
    return Double.valueOf(tickSpacing);
  }

  @Override
  public void updateValues(MQueryResult result) {
    double values = fDataSource.getValue(result);
    fRadial.setValue(values);
  }

  @Override
  public void updateQuery(MQuery query) {
    fDataSource.updateQuery(query);
  }

}
