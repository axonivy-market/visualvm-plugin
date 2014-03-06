package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.SerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.ui.RotateLabel;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

class XYChartPanel extends JPanel implements IUpdatableUIObject {
  
  private static final Logger LOGGER = Logger.getLogger(XYChartPanel.class.getName());
  private static final int CONTAINER_LABEL_INDEX = 0;
  private static final int COMPONENT_TOP_LABEL_INDEX = 0;
  private static final String ICON_HELP_URL = "/resources/icons/question16a90.png";
  
  private SimpleXYChartSupport chart;
  private RotateLabel fYAxisLabel;
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
    fYAxisLabel = new RotateLabel(fDataSource.getYAxisDescription(), getQuestionImage());
    add(fYAxisLabel, new GridBagConstraints(
            1, 0, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));
  }
  
  @Override
  public void updateValues(QueryResult result) {
    long[] values = fDataSource.getValues(result);
    long currentTime = System.currentTimeMillis();
    chart.addValues(currentTime, values);
    
    long[] labels = fDataSource.calculateDetailValues(result);
    chart.updateDetails(convert(labels));
    fDataSource.setLabels(labels);
    
    addStorageItem(currentTime, values);
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
    fYAxisLabel.setToolTipText(message);
    fYAxisLabel.setIcon(message == null ? null : getQuestionImage());
  }
  
  private Image getQuestionImage() {
    Image image = null;
    try {
      image = ImageIO.read(getClass().getResource(ICON_HELP_URL));
    } catch (IOException ex) {
      LOGGER.warning(ex.getMessage());
    }
    return image;
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
