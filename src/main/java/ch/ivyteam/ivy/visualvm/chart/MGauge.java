/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MGaugeDataSource;
import eu.hansolo.steelseries.gauges.RadialBargraph;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.Section;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Spline;

/**
 *
 * @author thtam
 */
public class MGauge {

  private RadialBargraph fRadial;
  private MGaugeDataSource fDataSource;
  private static final Rectangle2D DEFAULT_LCD_RECTANGLE = new Rectangle2D.Double(0.55D, 0.75D, 0.27D, 0.1D);
  private final Color[] fColors = new Color[]{Color.GREEN, Color.YELLOW, Color.RED};
  private static final Color DEFAULT_RANGE_COLOR = Color.GRAY;
  private final double[] fLevels = new double[]{0d, 5d, 10d, 100d};
  private List<Section> fSections;
  private double newValue;

  MGauge(MGaugeDataSource dataSource) {
    fDataSource = dataSource;
    initSections();
    initGauge();
  }

  private void initSections() {
    fSections = new ArrayList<>();
    for (int i = 0; i < fColors.length; i++) {
      Color sectionColor = fColors[i] == null ? DEFAULT_RANGE_COLOR : this.fColors[i];
      fSections.add(new Section(this.fLevels[i], this.fLevels[(i + 1)], sectionColor, setAlpha(
              sectionColor.brighter().brighter(), 0.5F), null, null));
    }
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
    fRadial.setValue(10);
    fRadial.setLcdColor(LcdColor.CUSTOM);
    fRadial.setCustomLcdBackground(Color.WHITE);
    fRadial.setCustomLcdForeground(Color.BLUE);
    animate(1);
  }

  public void animate(int second) {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        newValue = Math.random() * fRadial.getMaxValue();
        addTween(fRadial, "value", null, 2000, fRadial.getValue(), newValue);
      }

    }, second * 2000, second * 2000);

    fRadial.setValueAnimated(newValue);
    fRadial.setLcdValueAnimated(newValue);
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

  private Color setAlpha(Color color, float alpha) {
    if (alpha > 1.0F) {
      return setAlpha(color, 255);
    }
    if (alpha < 0.0F) {
      return setAlpha(color, 0);
    }
    return setAlpha(color, (int) Math.ceil(255.0F * alpha));
  }

  private Color setAlpha(Color color, int alpha) {
    return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
  }

  private void addTween(Object mainObject, String propertyName, TimelineCallback callback, int duration,
          double from, double to) {
    Timeline timeline = new Timeline(mainObject);
    timeline.addPropertyToInterpolate(propertyName, Double.valueOf(from), Double.valueOf(to));
    timeline.setDuration(duration);
    if (callback != null) {
      timeline.addCallback(callback);
    }
    timeline.setEase(new Spline(0.6F));
    timeline.play();
  }

}
