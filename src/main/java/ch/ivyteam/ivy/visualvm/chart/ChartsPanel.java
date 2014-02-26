/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MGaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ChartsPanel implements IUpdatableUIObject {

  private final List<MChart> barCharts = new ArrayList<>();
  private final List<MGauge> gauges = new ArrayList<>();
  private final JPanel chartPanel;

  /**
   * Constructor
   *
   * @param horizontalOrVertical <code>true</code> to lay charts horizontally, <code>false</code> to lay
   *                             charts vertically
   */
  public ChartsPanel(boolean horizontalOrVertical) {
    LayoutManager layout;
    if (horizontalOrVertical) {
      layout = new GridLayout(1, 0);
    } else {
      layout = new GridLayout(0, 1);
    }
    chartPanel = new JPanel(layout);
    chartPanel.setBackground(Color.WHITE);
  }

  public JComponent getUiComponent() {
    return chartPanel;
  }

  public void addChart(MChartDataSource dataSource) {
    final MChart chart = new MChart(dataSource);
    barCharts.add(chart);
    chartPanel.add(chart.getUi());
  }

  public void addGauge(MGaugeDataSource dataSource) {
    final MGauge gauge = new MGauge(dataSource);
    gauges.add(gauge);
    chartPanel.add(gauge.getUi());
  }

  @Override
  public void updateValues(MQueryResult result) {
    for (MChart chart : barCharts) {
      chart.updateValues(result);
    }
    for (MGauge gauge : gauges) {
      gauge.updateValues(result);
    }
  }

  @Override
  public void updateQuery(MQuery query) {
    for (MChart chart : barCharts) {
      chart.updateQuery(query);
    }
    for (MGauge gauge : gauges) {
      gauge.updateQuery(query);
    }
  }

}
