/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MGaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import eu.hansolo.steelseries.gauges.Linear;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.Section;
import java.awt.Color;
import java.util.List;
import javax.swing.JComponent;

public class MLinear implements IUpdatableUIObject {

  private Linear fLinear;
  private final List<Section> fSections;
  private final MGaugeDataSource fDataSource;

  public MLinear(MGaugeDataSource dataSource) {
    fDataSource = dataSource;
    fSections = dataSource.getSections();
    initGauge();
  }

  private void initGauge() {
    fLinear = new Linear();
    fLinear.setTitle("");
    fLinear.setUnitString("");
    fLinear.setFrameVisible(false);
    fLinear.setBackgroundVisible(false);
    fLinear.setBackgroundColor(BackgroundColor.TRANSPARENT);
    fLinear.setLedVisible(false);

    fLinear.setLcdColor(LcdColor.CUSTOM);
    fLinear.setCustomLcdBackground(Color.WHITE);

    setSections();
  }

  private void setSections() {
    if (!fSections.isEmpty()) {
      fLinear.setMinValue(fSections.get(0).getStart());
      fLinear.setMaxValue(fSections.get(fSections.size() - 1).getStop());
      for (Section section : fSections) {
        fLinear.addSection(section);
      }
    }
    fLinear.setSectionsVisible(true);
    fLinear.setNiceScale(false);
  }

  @Override
  public void updateValues(MQueryResult result) {
    double values = fDataSource.getValue(result);
    fLinear.setValue(values);
  }

  @Override
  public void updateQuery(MQuery query) {
    fDataSource.updateQuery(query);
  }

  public JComponent getUi() {
    return fLinear;
  }

}
