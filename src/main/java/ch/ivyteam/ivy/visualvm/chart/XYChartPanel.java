package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

class XYChartPanel implements IUpdatableUIObject {

  private final SimpleXYChartSupport chart;
  private final XYChartDataSource fDataSource;
  private final long[] fMaxValues;
  private final long[] fLatestValues;

  XYChartPanel(XYChartDataSource dataSource) {
    fDataSource = dataSource;
    fLatestValues = new long[dataSource.getSerieDataSources().size()];
    fMaxValues = new long[dataSource.getSerieDataSources().size()];

    int monitoredDataCache = GlobalPreferences.sharedInstance().getMonitoredDataCache();
    SimpleXYChartDescriptor chartDescriptor = SimpleXYChartDescriptor.decimal(10, true,
            60 * monitoredDataCache);
    dataSource.configureChart(chartDescriptor);
    chart = ChartFactory.createSimpleXYChart(chartDescriptor);
    JPopupMenu menu = createLayoutMenu();
    chart.getChart().setComponentPopupMenu(menu);
  }

  JComponent getUI() {
    return chart.getChart();
  }

  @Override
  public void updateValues(QueryResult result) {
    long[] values = fDataSource.getValues(result);
    updateLatestValues(values);
    updateMaxValues(values);
    chart.addValues(System.currentTimeMillis(), values);
    updateChartDetails(values);
  }

  private void updateLatestValues(long[] values) {
    System.arraycopy(values, 0, fLatestValues, 0,
            Math.min(values.length, fLatestValues.length));
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
  public void updateQuery(Query query) {
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

    JCheckBoxMenuItem labelVisibleItem = new JCheckBoxMenuItem("Show labels");
    labelVisibleItem.setSelected(true);
    labelVisibleItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem control = (JCheckBoxMenuItem) e.getSource();
        getLabelComponent().setVisible(control.isSelected());
      }

    });

    menu.add(legendVisibleItem);
    menu.add(labelVisibleItem);
    return menu;
  }

  protected long[] getMaxValues() {
    return fMaxValues;
  }

  protected long[] getLatestValues() {
    return fLatestValues;
  }

  private Component getLabelComponent() {
    Container chartContainer = (Container) chart.getChart();
    Container labelPanel = (Container) chartContainer.getComponent(0);
    return labelPanel.getComponent(0);
  }

}
