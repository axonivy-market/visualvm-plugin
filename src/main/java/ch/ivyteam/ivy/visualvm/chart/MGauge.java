/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MGaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.KnobStyle;
import eu.hansolo.steelseries.tools.KnobType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.PointerType;
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

  private Radial fRadial;
  private static final Rectangle2D DEFAULT_LCD_RECTANGLE = new Rectangle2D.Double(0.27, 0.6, 0.27, 0.12);
  private final List<Section> fSections;
  private final MGaugeDataSource fDataSource;

  public MGauge(MGaugeDataSource dataSource) {
    fDataSource = dataSource;
    fSections = dataSource.getSections();
    initGauge();
  }

  private void initGauge() {
    fRadial = new Radial();
    fRadial.setTitle("");
    fRadial.setUnitString("");
    fRadial.setFrameVisible(false);
    fRadial.setBackgroundVisible(false);
    fRadial.setBackgroundColor(BackgroundColor.TRANSPARENT);
    fRadial.setLedVisible(false);

    fRadial.setLcdColor(LcdColor.CUSTOM);
    fRadial.setCustomLcdBackground(Color.WHITE);

    fRadial.setPointerType(PointerType.TYPE1);
    fRadial.setPointerShadowVisible(false);
    fRadial.setPointerColor(ColorDef.BLACK);

    fRadial.setKnobType(KnobType.SMALL_STD_KNOB);
    fRadial.setKnobStyle(KnobStyle.BLACK);

    // hide the two bullets at the boundaries of the gauge
    fRadial.setPostsVisible(false);
    fRadial.getGaugeType().LCD_FACTORS.setRect(DEFAULT_LCD_RECTANGLE);

    setSections();
  }

  private void setSections() {
    if (!fSections.isEmpty()) {
      fRadial.setMinValue(fSections.get(0).getStart());
      fRadial.setMaxValue(fSections.get(fSections.size() - 1).getStop());
      for (Section section : fSections) {
        fRadial.addSection(section);
      }
    }
    fRadial.setSectionsVisible(true);
    fRadial.setNiceScale(false);

    if (!fSections.isEmpty()) {
      double delta = fSections.get(fSections.size() - 1).getStop() - fSections.get(0).getStart();
      int digit = (int) Math.floor(Math.log10(delta));

      double tickSpacing = chooseGaugeTickSpacing(Double.valueOf(delta), digit).doubleValue();
      fRadial.setMinorTickSpacing(tickSpacing / 10.0D);
      fRadial.setMajorTickSpacing(tickSpacing);
    }
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
