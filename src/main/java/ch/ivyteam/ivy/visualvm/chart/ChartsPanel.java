/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.MChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MGaugeDataSource;
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
   *                             charts vertically
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

  public JComponent getUiComponent() {
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

  public void addChart(MChartDataSource dataSource) {
    final MChart chart = new MChart(dataSource);
    updatableObjects.add(chart);
    chartPanel.add(chart.getUi());
  }

  public void addGauge(MGaugeDataSource dataSource) {
    final MGauge gauge = new MGauge(dataSource);
    updatableObjects.add(gauge);
    chartPanel.add(gauge.getUi());
  }

  public void addLinear(MGaugeDataSource dataSource) {
    final MLinear linear = new MLinear(dataSource);
    updatableObjects.add(linear);
    chartPanel.add(linear.getUi());
  }

  @Override
  public void updateValues(MQueryResult result) {
    for (IUpdatableUIObject updatableObject : updatableObjects) {
      updatableObject.updateValues(result);
    }
  }

  @Override
  public void updateQuery(MQuery query) {
    for (IUpdatableUIObject updatableObject : updatableObjects) {
      updatableObject.updateQuery(query);
    }
  }

}
