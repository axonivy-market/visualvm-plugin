/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.license.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ChartsPanel implements IUpdatableUIObject {

  private final List<IUpdatableUIObject> updatableObjects = new ArrayList<>();
  private final JPanel chartPanel;

  /**
   * Constructor
   * 
   * @param horizontalOrVertical <code>true</code> to lay charts horizontally, <code>false</code> to lay
   *          charts vertically
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

  public JComponent getUIComponent() {
    return chartPanel;
  }

  public void setLayoutOrientation(boolean horizontalOrVertical) {
    if (horizontalOrVertical) {
      chartPanel.setLayout(new GridLayout(1, 0));
    } else {
      chartPanel.setLayout(new GridLayout(0, 1));
    }
  }

  public void switchLayoutOrientation() {
    GridLayout layout = (GridLayout) chartPanel.getLayout();
    setLayoutOrientation(layout.getRows() == 0);
  }

  public void addChart(XYChartDataSource dataSource) {
    final XYChartPanel chart = new XYChartPanel(dataSource);
    updatableObjects.add(chart);
    chartPanel.add(chart.getUI());
  }

  public void addChart(XYChartDataSource dataSource, String yAxisMessage) {
    final XYChartPanel chart = new XYChartPanel(dataSource);
    updatableObjects.add(chart);
    chartPanel.add(chart.getUI());
    chart.setYaxisHelpMessage(yAxisMessage);
  }

  public void addChart2(XYChartDataSource dataSource) {
    final XYChartPanel chart = new XYChartPanel(dataSource) {
      @Override
      public void updateChartDetails(long[] values) {
        super.updateChartDetails(getMaxValues());
      }

    };
    updatableObjects.add(chart);
    chartPanel.add(chart.getUI());
  }

  public void addGauge(GaugeDataSource dataSource) {
    final RadialPanel gauge = new RadialPanel(dataSource);
    updatableObjects.add(gauge);
    chartPanel.add(gauge.getUI());
  }

  public void addLinear(GaugeDataSource dataSource) {
    final LinearPanel linear = new LinearPanel(dataSource);
    updatableObjects.add(linear);
    chartPanel.add(linear.getUI());
  }

  @Override
  public void updateValues(QueryResult result) {
    for (IUpdatableUIObject updatableObject : updatableObjects) {
      updatableObject.updateValues(result);
    }
  }

  @Override
  public void updateQuery(Query query) {
    for (IUpdatableUIObject updatableObject : updatableObjects) {
      updatableObject.updateQuery(query);
    }
  }

}
