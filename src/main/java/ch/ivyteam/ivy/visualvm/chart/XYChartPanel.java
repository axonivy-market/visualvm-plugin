package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.SerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.apache.commons.lang.StringUtils;

class XYChartPanel extends JPanel implements IUpdatableUIObject {

  private static final int CONTAINER_LABEL_INDEX = 0;
  private static final int CONTAINER_CHART_INDEX = 1;
  private static final int COMPONENT_TOP_LABEL_INDEX = 0;
  private static final int COMPONENT_YAXIS_LABEL_INDEX = 0;
  private static final String ICON_HELP_URL = "/resources/icons/question16.png";

  private SimpleXYChartSupport chart;
  private final XYChartDataSource fDataSource;
  private String fYAxisHelpMessage;

  private final List<StorageItem> fStorage;

  XYChartPanel(XYChartDataSource dataSource) {
    this(dataSource, null);
  }

  XYChartPanel(XYChartDataSource dataSource, String yAxisMessage) {
    fDataSource = dataSource;
    fStorage = new ArrayList<>();
    fYAxisHelpMessage = yAxisMessage;
    createChart();
    createYAxisHelpMessage(yAxisMessage);
  }

  private void createChart() {
    int monitoredDataCache = GlobalPreferences.sharedInstance().getMonitoredDataCache();
    SimpleXYChartDescriptor chartDescriptor = SimpleXYChartDescriptor.decimal(10, true,
            60 * monitoredDataCache);
    configureChart(chartDescriptor);
    chart = ChartFactory.createSimpleXYChart(chartDescriptor);
    JPopupMenu menu = createLayoutMenu();
    chart.getChart().setComponentPopupMenu(menu);

    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    removeAll();
    add(chart.getChart(), new GridBagConstraints(
            0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
  }

  @Override
  public void updateValues(QueryResult result) {
    long[] values = fDataSource.getValues(result);
    chart.addValues(System.currentTimeMillis(), values);

    values = fDataSource.getLabels(result);
    chart.updateDetails(convert(values));

    long currentTime = System.currentTimeMillis();
    addStorageItem(currentTime, values);
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

  public String getYAxisHelpMessage() {
    return fYAxisHelpMessage;
  }

  private Component getLabelComponent() {
    Container chartContainer = (Container) chart.getChart();
    Container labelPanel = (Container) chartContainer.getComponent(CONTAINER_LABEL_INDEX);
    return labelPanel.getComponent(COMPONENT_TOP_LABEL_INDEX);
  }

  public final void createYAxisHelpMessage(String message) {
    if (StringUtils.isEmpty(message)) {
      return;
    }
    Container container = (Container) getComponent(CONTAINER_CHART_INDEX);
    JLabel yAxisLabel = (JLabel) container.getComponent(COMPONENT_YAXIS_LABEL_INDEX);
    JPanel panel = createYaxisDescriptorPanel(createYAxisHelpLabel(message), yAxisLabel);
    container.add(panel, BorderLayout.WEST);
  }

  private JLabel createYAxisHelpLabel(final String message) {
    ImageIcon helpIcon = new ImageIcon(getClass().getResource(ICON_HELP_URL));
    JLabel label = new JLabel(helpIcon);
    label.setToolTipText(message);
    return label;
  }

  private JPanel createYaxisDescriptorPanel(Component... components) {
    JPanel yAxisDescriptorPanel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(yAxisDescriptorPanel, BoxLayout.Y_AXIS);
    yAxisDescriptorPanel.setLayout(boxLayout);
    yAxisDescriptorPanel.setBackground(Color.WHITE);
    yAxisDescriptorPanel.add(Box.createVerticalGlue());
    for (Component component : components) {
      yAxisDescriptorPanel.add(component);
    }
    yAxisDescriptorPanel.add(Box.createVerticalGlue());
    return yAxisDescriptorPanel;
  }

  public void updateCachePeriod() {
    createChart();
    createYAxisHelpMessage(fYAxisHelpMessage);
    long buffer = GlobalPreferences.sharedInstance().getMonitoredDataCache() * 60 * 1000;
    long currentTime = System.currentTimeMillis();
    restoreDataFromStorage(buffer, currentTime);
  }

  private void restoreDataFromStorage(long cachePeriod, long currentTime) {
    removeOutOfDateStorageItem(cachePeriod, currentTime);
    for (StorageItem item : fStorage) {
      chart.addValues(item.getTimestamp(), item.getValues());
    }
  }

  private void addStorageItem(long currentTime, long[] values) {
    long buffer = GlobalPreferences.sharedInstance().getMonitoredDataCache() * 60 * 1000;
    fStorage.add(new StorageItem(currentTime, values));
    removeOutOfDateStorageItem(buffer, currentTime);
  }

  private void removeOutOfDateStorageItem(long cachePeriod, long currentTime) {
    Iterator<StorageItem> iterator = fStorage.iterator();
    while (iterator.hasNext()) {
      StorageItem item = iterator.next();
      if (item.getTimestamp() + cachePeriod < currentTime) {
        iterator.remove();
      } else {
        break;
      }
    }
  }

  private void configureChart(SimpleXYChartDescriptor chartDescriptor) {
    if (fDataSource.getChartName() != null) {
      chartDescriptor.setChartTitle(fDataSource.getChartName());
    }
    if (fDataSource.getXAxisDescription() != null) {
      chartDescriptor.setXAxisDescription(fDataSource.getXAxisDescription());
    }
    if (fDataSource.getYAxisDescription() != null) {
      chartDescriptor.setYAxisDescription(fDataSource.getYAxisDescription());
    }

    for (SerieDataSource dataSource : fDataSource.getSerieDataSources()) {
      dataSource.configureSerie(chartDescriptor);
    }

    String[] detailLabels = new String[fDataSource.getLabelCalcSupports().size()];
    int index = 0;
    for (ChartLabelCalcSupport support : fDataSource.getLabelCalcSupports()) {
      detailLabels[index++] = support.getText();
    }
    chartDescriptor.setDetailsItems(detailLabels);
  }

  private class StorageItem {
    private final long fTimestamp;
    private final long[] fValues;

    public StorageItem(long time, long[] values) {
      fTimestamp = time;
      fValues = new long[values.length];
      System.arraycopy(values, 0, fValues, 0, values.length);
    }

    public long getTimestamp() {
      return fTimestamp;
    }

    public long[] getValues() {
      return fValues;
    }

  }

}
