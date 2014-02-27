package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MChartDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

class MChart implements IUpdatableUIObject {

  private final SimpleXYChartSupport chart;
  private final MChartDataSource fDataSource;
  private final long[] fMaxValues;
  private final long[] fLatestValues;

  MChart(MChartDataSource dataSource) {
    fDataSource = dataSource;
    int monitoredDataCache = GlobalPreferences.sharedInstance().getMonitoredDataCache();
    SimpleXYChartDescriptor chartDescriptor = SimpleXYChartDescriptor.decimal(10, true,
            60 * monitoredDataCache);
    dataSource.configureChart(chartDescriptor);
    chart = ChartFactory.createSimpleXYChart(chartDescriptor);
    JPopupMenu menu = createLayoutMenu();
    chart.getChart().setComponentPopupMenu(menu);
    fLatestValues = new long[dataSource.getSerieDataSources().size()];
    fMaxValues = new long[dataSource.getSerieDataSources().size()];
  }

  JComponent getUi() {
    return chart.getChart();
  }

  @Override
  public void updateValues(MQueryResult result) {
    long[] values = fDataSource.getValues(result);
    updateLatestValues(values);
    updateMaxValues(values);
    chart.addValues(System.currentTimeMillis(), values);
    updateChartDetails(values);
  }

  private void updateLatestValues(long[] values) {
    for (int i = 0; i < getLatestValues().length; i++) {
      if (i >= values.length) {
        break;
      }
      fLatestValues[i] = values[i];
    }
  }

  private void updateMaxValues(long[] values) {
    for (int i = 0; i < getMaxValues().length; i++) {
      if (i >= values.length) {
        break;
      }
      if (values[i] > getMaxValues()[i]) {
        fMaxValues[i] = values[i];
      }
    }
  }

  public void updateChartDetails(long[] values) {
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

  private JPopupMenu createLayoutMenu() {
    JPopupMenu menu = new JPopupMenu();
    JCheckBoxMenuItem legendVisibleItem = new JCheckBoxMenuItem("Show legends");
    legendVisibleItem.setSelected(true);
    legendVisibleItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem control = (JCheckBoxMenuItem) e.getSource();
        chart.setLegendVisible(control.isSelected());
      }

    });
    menu.add(legendVisibleItem);
    return menu;
  }

  protected long[] getMaxValues() {
    return fMaxValues;
  }

  protected long[] getLatestValues() {
    return fLatestValues;
  }

}
