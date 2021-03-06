package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.SerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.view.HtmlLabelComponent;
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
import java.awt.Font;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import org.apache.commons.lang.StringUtils;

/**
 * This is just a composite of UI components. Not a JPanel.
 */
public class XYChartPanel implements IUpdatableUIObject {

  private static final String CLIENT_PROPERTY_LEGEND_PANEL = "legendPanel";
  private static final int CONTAINER_CHART_INDEX = 1;
  private static final int CHART_MAIN_AREA_INDEX = 1;
  private static final int CHART_MAIN_AREA_YAXIS_INDEX = 2;
  private static final int CHART_YAXIS_LABEL_INDEX = 0;
  private static final String ICON_HELP_URL = "/resources/icons/question16.png";

  private SimpleXYChartSupport fChart;
  private HtmlLabelComponent fHtmlLabel;
  private Component fYAxis;
  private Component fXAxis;
  private Component fYAxisDescription;
  private Component fChartArea;
  private Component fChartLegend;
  private final XYChartDataSource fDataSource;
  private String fYAxisTooltip;

  private final List<StorageItem> fStorage;
  private long fMaxValueFromChartCreated = 0;
  private long fMaxValueBeingDisplayed = 0;
  private final ChartsPanel fchartsContainer;

  XYChartPanel(ChartsPanel chartsContainer, XYChartDataSource dataSource) {
    this(chartsContainer, dataSource, null);
  }

  XYChartPanel(ChartsPanel chartsContainer, XYChartDataSource dataSource, String yAxisTooltip) {
    fchartsContainer = chartsContainer;
    fDataSource = dataSource;
    fStorage = Collections.synchronizedList(new LinkedList<StorageItem>());
    fYAxisTooltip = yAxisTooltip;
    createUI();
  }

  private void createUI() {
    createChart();
    createYAxisHeader(fYAxisTooltip);
    createLegendTooltips();
    createLabels();
  }

  private void createChart() {
    int monitoredDataCache = GlobalPreferences.sharedInstance().getMonitoredDataCache();
    int monitoredDataPoll = GlobalPreferences.sharedInstance().getMonitoredDataPoll();
    int chartPoints = 60 * monitoredDataCache / monitoredDataPoll;
    SimpleXYChartDescriptor chartDescriptor = SimpleXYChartDescriptor.decimal(10, true, chartPoints);
    configureChart(chartDescriptor);

    fChart = ChartFactory.createSimpleXYChart(chartDescriptor);
    fHtmlLabel = new HtmlLabelComponent();
    fYAxis = identifyYAxis();
    fXAxis = identifyXAxis();
    fChartArea = identifyChartMainArea();
  }

  private boolean needRecreateUI() {
    boolean rs = false;
    if (fMaxValueFromChartCreated > 0) {
      rs = (fMaxValueBeingDisplayed <= fMaxValueFromChartCreated / 2);
    }
    return rs;
  }

  private void recreateUI() {
    createUI();
    long currentTime = System.currentTimeMillis();
    fMaxValueFromChartCreated = 0;
    fMaxValueBeingDisplayed = 0;
    restoreDataFromStorage(currentTime);
    fchartsContainer.recreateUIAlignedXYCharts();
  }

  @Override
  public void updateValues(QueryResult result) {
    long[] values = fDataSource.getValues(result);
    long currentTime = System.currentTimeMillis();

    if (needRecreateUI()) {
      recreateUI();
    }
    addValuesToChart(currentTime, values);

    long[] labels = fDataSource.calculateDetailValues(result);
    fHtmlLabel.updateValues(labels);
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

  public void updateCachePeriod() {
    createUI();
    long currentTime = System.currentTimeMillis();
    restoreDataFromStorage(currentTime);
  }

  private void restoreDataFromStorage(long currentTime) {
    removeOutOfDateStorageItem(currentTime);
    synchronized (fStorage) {
      for (StorageItem item : fStorage) {
        addValuesToChart(item.getTimestamp(), item.getValues());
      }
    }
  }

  /**
   * Please do not call fChart.addValues(currentTime, values) directly. Please
   * use this method so that it can automatically calculate the topValue and
   * maxValue
   *
   * @param currentTime
   * @param values
   */
  private void addValuesToChart(long currentTime, long[] values) {
    fChart.addValues(currentTime, values);
    for (long val : values) {
      if (this.fMaxValueFromChartCreated < val) {
        this.fMaxValueFromChartCreated = val;
        this.fMaxValueBeingDisplayed = val;
      } else if (this.fMaxValueBeingDisplayed < val) {
        this.fMaxValueBeingDisplayed = val;
      }
    }
  }

  private void addStorageItem(long currentTime, long[] values) {
    fStorage.add(new StorageItem(currentTime, values));
    removeOutOfDateStorageItem(currentTime);
  }

  private void removeOutOfDateStorageItem(long currentTime) {
    long cachePeriod = GlobalPreferences.sharedInstance().getMonitoredDataCache() * 60 * 1000;
    boolean needRecalculateMaxValue = false;
    synchronized (fStorage) {
      Iterator<StorageItem> iterator = fStorage.iterator();
      while (iterator.hasNext()) {
        StorageItem item = iterator.next();
        if (item.getTimestamp() + cachePeriod < currentTime) {
          iterator.remove();
          for (long removedValue : item.fValues) {
            if (removedValue >= fMaxValueBeingDisplayed) {
              needRecalculateMaxValue = true;
              break;
            }
          }
        } else {
          break;
        }
      }
    }
    if (needRecalculateMaxValue) {
      recalculateMaxValueInCache();
    }

  }

  private void recalculateMaxValueInCache() {
    this.fMaxValueBeingDisplayed = 0;
    synchronized (fStorage) {
      Iterator<StorageItem> iterator = fStorage.iterator();
      while (iterator.hasNext()) {
        StorageItem storageItem = iterator.next();
        for (long val : storageItem.fValues) {
          if (this.fMaxValueBeingDisplayed < val) {
            this.fMaxValueBeingDisplayed = val;
          }
        }
      }
    }
  }

  //<editor-fold defaultstate="collapsed" desc="UI Components">
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
    Component legendPanelVerion138 = identifyLegend138AndLater();
    if (legendPanelVerion138 != null) {
      return legendPanelVerion138;
    }

    Component legendPanelVersion137 = identifyLegend137AndBefore();
    if (isLegendContainer(legendPanelVersion137)) {
      return legendPanelVersion137;
    }
    return null;
  }

  private Component identifyLegend138AndLater() {
    JPanel chartPanel = (JPanel) fChart.getChart();
    return (Component) chartPanel.getClientProperty(CLIENT_PROPERTY_LEGEND_PANEL);
  }

  private Component identifyLegend137AndBefore() {
    return fChart.getChart().getComponent(2);
  }

  private boolean isLegendContainer(Component component) {
    if (component instanceof JPanel) {
      JPanel legendContainer = (JPanel) component;
      Component legendPanelComponent = legendContainer.getComponent(0);
      if (legendPanelComponent instanceof JPanel) {
        JPanel legendPanel = (JPanel) legendPanelComponent;
        if (legendPanel.getComponentCount() > 0 && legendPanel.getComponent(0) instanceof JCheckBox) {
          return true;
        }
      }
    }
    return false;
  }

  public Component getYAxis() {
    return fYAxis;
  }

  public Component getXAxis() {
    return fXAxis;
  }

  public String getYAxisTooltip() {
    return fYAxisTooltip;
  }

  private void createYAxisHeader(String yAxisTooltip) {
    Container container = (Container) fChart.getChart().getComponent(CONTAINER_CHART_INDEX);
    JLabel yAxisLabel = (JLabel) container.getComponent(CHART_YAXIS_LABEL_INDEX);
    Font font = new Font(yAxisLabel.getFont().getFontName(), Font.BOLD,
            yAxisLabel.getFont().getSize());
    yAxisLabel.setFont(font);
    if (StringUtils.isEmpty(yAxisTooltip)) {
      fYAxisDescription = yAxisLabel;
      return;
    }
    fYAxisDescription = createYAxisDescriptorPanel(createYAxisHelpLabel(yAxisTooltip), yAxisLabel);
    container.add(fYAxisDescription, BorderLayout.WEST);
  }

  private JLabel createYAxisHelpLabel(final String yAxisTooltip) {
    ImageIcon helpIcon = new ImageIcon(getClass().getResource(ICON_HELP_URL));
    JLabel label = new JLabel(helpIcon);
    label.setToolTipText(yAxisTooltip);
    return label;
  }

  private JPanel createYAxisDescriptorPanel(Component... components) {
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
  }

  public Component getYAxisDescription() {
    return fYAxisDescription;
  }

  public HtmlLabelComponent getHtmlLabel() {
    return fHtmlLabel;
  }

  public Component getChartUI() {
    return fChartArea;
  }

  public Component getLegend() {
    return fChartLegend;
  }

  private void createLegendTooltips() {
    fChartLegend = identifyLegend();
    Container legendContainer = (Container) ((Container) fChartLegend).getComponent(0);
    int index = 0;
    for (Component legend : legendContainer.getComponents()) {
      AbstractButton legendButton = (AbstractButton) legend;
      if (index < fDataSource.getSerieDataSources().size()) {
        legendButton.setToolTipText(fDataSource.getSerieDataSources().get(index++).getDescription());
      }
    }
  }

  private void createLabels() {
    for (AbstractChartLabelCalcSupport calc : fDataSource.getLabelCalcSupports()) {
      fHtmlLabel.addInfo("key", calc.getText(), "0", calc.getUnit(), calc.getTooltip());
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Inner classes">
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
  //</editor-fold>

}
