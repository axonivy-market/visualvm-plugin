/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.KnobStyle;
import eu.hansolo.steelseries.tools.KnobType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.PointerType;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class RadialPanel extends AbstractGaugePanel {

  private static final Rectangle2D DEFAULT_LCD_RECTANGLE = new Rectangle2D.Double(0.27, 0.6, 0.27, 0.12);

  public RadialPanel(GaugeDataSource dataSource) {
    super(dataSource, new Radial());
    initRadial();
  }

  private void initRadial() {
    getUI().setLcdColor(LcdColor.CUSTOM);
    getUI().setCustomLcdBackground(Color.WHITE);

    getUI().setPointerType(PointerType.TYPE1);
    getUI().setPointerShadowVisible(false);
    getUI().setPointerColor(ColorDef.BLACK);

    getUI().setKnobType(KnobType.SMALL_STD_KNOB);
    getUI().setKnobStyle(KnobStyle.BLACK);

    // hide the two bullets at the boundaries of the gauge
    getUI().setPostsVisible(false);
    getUI().getGaugeType().LCD_FACTORS.setRect(DEFAULT_LCD_RECTANGLE);

  }

  @Override
  public Radial getUI() {
    return (Radial) super.getUI();
  }

}
