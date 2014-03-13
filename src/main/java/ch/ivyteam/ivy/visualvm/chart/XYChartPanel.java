package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.HtmlSupport;
import ch.ivyteam.ivy.visualvm.chart.data.SerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import org.apache.commons.lang.StringUtils;

public class XYChartPanel extends JPanel implements IUpdatableUIObject {

  private static final int CONTAINER_CHART_INDEX = 1;
  private static final int CHART_MAIN_AREA_INDEX = 1;
  private static final int CHART_MAIN_AREA_YAXIS_INDEX = 2;
  private static final int CHART_YAXIS_LABEL_INDEX = 0;
  private static final String ICON_HELP_URL = "/resources/icons/question16_with_padding.png";

  private SimpleXYChartSupport fChart;
  private JLabel fHtmlLabel;
  private Component fYAxis;
  private Component fXAxis;
  private Component fYAxisDescription;
  private Component fChartArea;
  private Component fChartLegend;
  private final XYChartDataSource fDataSource;
  private NumberFormat fNumberFormat;
  private String fYAxisHelpMessage;
  private String[] fDetailLabels;

  private final List<StorageItem> fStorage;

  XYChartPanel(XYChartDataSource dataSource) {
    this(dataSource, null);
  }

  XYChartPanel(XYChartDataSource dataSource, String yAxisMessage) {
    fDataSource = dataSource;
    fStorage = new ArrayList<>();
    fNumberFormat = NumberFormat.getNumberInstance();

    fYAxisHelpMessage = yAxisMessage;
    createChart();
    createYAxisHelpMessage(yAxisMessage);
  }

  private void createChart() {
    int monitoredDataCache = GlobalPreferences.sharedInstance().getMonitoredDataCache();
    SimpleXYChartDescriptor chartDescriptor = SimpleXYChartDescriptor.decimal(10, true,
            60 * monitoredDataCache);
    configureChart(chartDescriptor);
    fChart = ChartFactory.createSimpleXYChart(chartDescriptor);

    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    removeAll();
    add(fChart.getChart(), new GridBagConstraints(
            0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
    fHtmlLabel = new JLabel();
    fHtmlLabel.setVerticalAlignment(JLabel.TOP);
    add(fHtmlLabel, new GridBagConstraints(
            1, 0, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
    fYAxis = identifyYAxis();
    fXAxis = identifyXAxis();
    fChartArea = identifyChartMainArea();
    fChartLegend = identifyLegend();

    createLegendTooltips();
  }

  @Override
  public void updateValues(QueryResult result) {
    long[] values = fDataSource.getValues(result);
    long currentTime = System.currentTimeMillis();
    fChart.addValues(currentTime, values);

    long[] labels = fDataSource.calculateDetailValues(result);
    fDataSource.setLabels(result);
    String text = HtmlSupport.toOneColumnHtmlTable(fDetailLabels, format(labels));
    fHtmlLabel.setText(text);
    View view = (View) fHtmlLabel.getClientProperty(BasicHTML.propertyKey);
    if (view != null) {
      int w = (int) view.getPreferredSpan(View.X_AXIS);
      fHtmlLabel.setPreferredSize(new Dimension(w, 0));
    }

    addStorageItem(currentTime, values);
  }

  @Override
  public void updateQuery(Query query) {
    fDataSource.updateQuery(query);
  }

  private String[] format(long[] nums) {
    String[] result = new String[nums.length];
    int index = 0;
    for (long num : nums) {
      result[index++] = fNumberFormat.format(num);
    }
    return result;
  }

  private Component identifyYAxis() {
    Container container = (Container) fChart.getChart().getComponent(CONTAINER_CHART_INDEX);
    Container mainArea = (Container) container.getComponent(CHART_MAIN_AREA_INDEX);
    return mainArea.getComponent(CHART_MAIN_AREA_YAXIS_INDEX);
  }

  private Component identifyXAxis() {
    Container container = (Container) fChart.getChart().getComponent(CONTAINER_CHART_INDEX);
    Container mainArea = (Container) container.getComponent(CHART_MAIN_AREA_INDEX);
    return mainArea.getComponent(1);
  }

  private Component identifyChartMainArea() {
    Container container = (Container) fChart.getChart().getComponent(CONTAINER_CHART_INDEX);
    Container mainArea = (Container) container.getComponent(CHART_MAIN_AREA_INDEX);
    return mainArea.getComponent(0);
  }

  private Component identifyLegend() {
    return fChart.getChart().getComponent(2);
  }

  public Component getYAxis() {
    return fYAxis;
  }

  public Component getXAxis() {
    return fXAxis;
  }

  public String getYAxisHelpMessage() {
    return fYAxisHelpMessage;
  }

  private void createYAxisHelpMessage(String message) {
    Container container = (Container) fChart.getChart().getComponent(CONTAINER_CHART_INDEX);
    JLabel yAxisLabel = (JLabel) container.getComponent(CHART_YAXIS_LABEL_INDEX);
    if (StringUtils.isEmpty(message)) {
      fYAxisDescription = yAxisLabel;
      return;
    }
    fYAxisDescription = createYaxisDescriptorPanel(createYAxisHelpLabel(message), yAxisLabel);
    container.add(fYAxisDescription, BorderLayout.WEST);
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
      fChart.addValues(item.getTimestamp(), item.getValues());
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

    fDetailLabels = new String[fDataSource.getLabelCalcSupports().size()];
    int index = 0;
    for (AbstractChartLabelCalcSupport support : fDataSource.getLabelCalcSupports()) {
      fDetailLabels[index++] = support.getText();
    }
  }

  public Component getYAxisDescription() {
    return fYAxisDescription;
  }

  public JLabel getHtmlLabel() {
    return fHtmlLabel;
  }

  public Component getChartUI() {
    return fChartArea;
  }

  public Component getLegend() {
    return fChartLegend;
  }

  private void createLegendTooltips() {
    Container legendContainer = (Container) ((Container) fChartLegend).getComponent(0);
    int index = 0;
    for (Component legend : legendContainer.getComponents()) {
      AbstractButton legendButton = (AbstractButton) legend;
      if (index < fDataSource.getSerieDataSources().size()) {
        legendButton.setToolTipText(fDataSource.getSerieDataSources().get(index++).getDescription());
      }
    }
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
