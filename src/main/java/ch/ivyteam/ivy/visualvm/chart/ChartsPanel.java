package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ChartsPanel implements IUpdatableUIObject {

  private final List<IUpdatableUIObject> fUpdatableObjects = new ArrayList<>();
  private final JPanel fChartPanel;
  private final XYChartsPanel fXYChartsPanel;
  private final CacheSettingChangeListener fCacheSettingChangeListener = new CacheSettingChangeListener();

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
    fChartPanel = new JPanel(layout);
    fChartPanel.setBackground(Color.WHITE);
    fXYChartsPanel = new XYChartsPanel();
    GlobalPreferences.sharedInstance().watchMonitoredDataCache(fCacheSettingChangeListener);
  }

  public JComponent getUIComponent() {
    return fChartPanel;
  }

  public void setLayoutOrientation(boolean horizontalOrVertical) {
    if (horizontalOrVertical) {
      fChartPanel.setLayout(new GridLayout(1, 0));
    } else {
      fChartPanel.setLayout(new GridLayout(0, 1));
    }
  }

  public void switchLayoutOrientation() {
    GridLayout layout = (GridLayout) fChartPanel.getLayout();
    setLayoutOrientation(layout.getRows() == 0);
  }

  public void addChart(XYChartDataSource dataSource) {
    final XYChartPanel chart = new XYChartPanel(dataSource);
    addXYChart(chart);
  }

  private void addXYChart(final XYChartPanel chart) {
    fUpdatableObjects.add(chart);
    addXYChartInternal(chart);
  }

  private void addXYChartInternal(final XYChartPanel chart) {
    fXYChartsPanel.addChart(chart);
    if (!fChartPanel.isAncestorOf(fXYChartsPanel)) {
      fChartPanel.add(fXYChartsPanel);
    }
  }

  public void addChart(XYChartDataSource dataSource, String yAxisMessage) {
    final XYChartPanel chart = new XYChartPanel(dataSource, yAxisMessage);
    addXYChart(chart);
  }

  public void addGauge(GaugeDataSource dataSource) {
    final RadialPanel gauge = new RadialPanel(dataSource);
    fUpdatableObjects.add(gauge);
    fChartPanel.add(gauge.getUI());
  }

  @Override
  public void updateValues(QueryResult result) {
    for (IUpdatableUIObject updatableObject : fUpdatableObjects) {
      updatableObject.updateValues(result);
    }
  }

  @Override
  public void updateQuery(Query query) {
    for (IUpdatableUIObject updatableObject : fUpdatableObjects) {
      updatableObject.updateQuery(query);
    }
  }

  private void updateChartsCachePeriod() {
    fXYChartsPanel.removeAll();
    for (IUpdatableUIObject object : fUpdatableObjects) {
      if (object instanceof XYChartPanel) {
        XYChartPanel chart = (XYChartPanel) object;
        chart.updateCachePeriod();
        addXYChartInternal(chart);
      }
    }
  }

  private class CacheSettingChangeListener implements PreferenceChangeListener {
    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
      updateChartsCachePeriod();
    }

  }
}
