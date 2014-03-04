/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import eu.hansolo.steelseries.gauges.Linear;
import eu.hansolo.steelseries.tools.LcdColor;
import java.awt.Color;

public class LinearPanel extends GaugePanel {

  public LinearPanel(GaugeDataSource dataSource) {
    super(dataSource, new Linear());
    initLinear();
  }

  private void initLinear() {
    getUI().setLcdColor(LcdColor.CUSTOM);
    getUI().setCustomLcdBackground(Color.WHITE);
  }

  @Override
  public Linear getUI() {
    return (Linear) super.getUI();
  }

}
