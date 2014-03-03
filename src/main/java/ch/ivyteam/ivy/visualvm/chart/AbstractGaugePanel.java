/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.license.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import eu.hansolo.steelseries.gauges.AbstractGauge;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.Section;
import java.util.List;

public abstract class AbstractGaugePanel implements IUpdatableUIObject {
  private final List<Section> fSections;
  private final GaugeDataSource fDataSource;
  private final AbstractGauge fGauge;

  public AbstractGaugePanel(GaugeDataSource dataSource, AbstractGauge gauge) {
    fDataSource = dataSource;
    fSections = dataSource.getSections();
    fGauge = gauge;
    initGauge();
  }

  private void initGauge() {
    fGauge.setTitle("");
    fGauge.setUnitString("");
    fGauge.setFrameVisible(false);
    fGauge.setBackgroundVisible(false);
    fGauge.setBackgroundColor(BackgroundColor.TRANSPARENT);
    fGauge.setLedVisible(false);
    setSections();
  }

  private void setSections() {
    if (!getSections().isEmpty()) {
      fGauge.setMinValue(getSections().get(0).getStart());
      fGauge.setMaxValue(getSections().get(getSections().size() - 1).getStop());
      for (Section section : getSections()) {
        fGauge.addSection(section);
      }
    }
    fGauge.setSectionsVisible(true);
    fGauge.setNiceScale(false);
  }

  @Override
  public void updateValues(QueryResult result) {
    double values = fDataSource.getValue(result);
    fGauge.setValue(values);
  }

  @Override
  public void updateQuery(Query query) {
    fDataSource.updateQuery(query);
  }

  public AbstractGauge getUI() {
    return fGauge;
  }

  public List<Section> getSections() {
    return fSections;
  }

}
