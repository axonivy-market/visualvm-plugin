/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MChartDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
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
    int monitoredDataCache = GlobalPreferences.sharedInstance().getMonitoredDataCache();
    SimpleXYChartDescriptor chartDescriptor = SimpleXYChartDescriptor.decimal(10, true,
            60 * monitoredDataCache);
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
    chart.updateDetails(convert(values));
  }

  @Override
  public void updateQuery(MQuery query) {
    fDataSource.updateQuery(query);
  }

  private String[] convert(long[] nums) {
    String[] result = new String[nums.length];
    int index = 0;
    for (long num : nums) {
      result[index++] = Long.toString(num);
    }
    return result;
  }

}
