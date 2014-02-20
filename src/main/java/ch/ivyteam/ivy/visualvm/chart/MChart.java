/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import javax.swing.JComponent;

/**
 *
 * @author rwei
 */
class MChart implements IUpdatableUIObject {

  private final SimpleXYChartSupport chart;
  private final MChartDataSource fDataSource;

  MChart(MChartDataSource dataSource) {
    fDataSource = dataSource;
    SimpleXYChartDescriptor chartDescriptor = SimpleXYChartDescriptor
            .decimal(10, true, 1000);
    dataSource.configureChart(chartDescriptor);
    chart = ChartFactory.createSimpleXYChart(chartDescriptor);
  }

  JComponent getUi() {
    return chart.getChart();
  }

  @Override
  public void updateValues(MQueryResult result) {
    long[] values = fDataSource.getValues(result);
    chart.addValues(System.currentTimeMillis(), values);
  }

  @Override
  public void updateQuery(MQuery query) {
    fDataSource.updateQuery(query);
  }

}
