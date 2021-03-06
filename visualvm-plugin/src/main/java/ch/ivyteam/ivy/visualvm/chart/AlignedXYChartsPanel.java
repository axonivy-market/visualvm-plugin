package ch.ivyteam.ivy.visualvm.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class AlignedXYChartsPanel extends JPanel {
  private static final int BASE_PADDING = 10;
  private final List<XYChartPanel> fCharts;

  public AlignedXYChartsPanel() {
    super();
    setLayout(new GridBagLayout());
    fCharts = new ArrayList<>();
    setBackground(Color.WHITE);
  }
  /**
   * @param chart 
   * In the future, we should dedicate below layout code to XYChartPanel.
   * This methods should layout only charts, not components inside the charts.
   */
  public void addChart(XYChartPanel chart) {
    int index = fCharts.size();
    chart.getYAxisDescription().setPreferredSize(new Dimension(18, 0));
    super.add(chart.getYAxisDescription(), new GridBagConstraints(0, index * 3, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(BASE_PADDING, BASE_PADDING, 0, 0), 0, 0));
    super.add(chart.getYAxis(), new GridBagConstraints(1, index * 3, 1, 2, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets(BASE_PADDING, 0, 0, 0), 0, 0));
    super.add(chart.getChartUI(), new GridBagConstraints(2, index * 3, 1, 1, 1, 1,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(BASE_PADDING, 0, 0, 0), 0, 0));
    super.add(chart.getXAxis(), new GridBagConstraints(1, index * 3 + 1, 2, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
    super.add(chart.getHtmlLabel(), new GridBagConstraints(3, index * 3, 1, 1, 0, 0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
            new Insets(BASE_PADDING, BASE_PADDING, 0, BASE_PADDING), 0, 0));
    super.add(chart.getLegend(), new GridBagConstraints(2, index * 3 + 2, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
    fCharts.add(chart);
  }

  @Override
  public void removeAll() {
    super.removeAll();
    fCharts.clear();
  }

}
