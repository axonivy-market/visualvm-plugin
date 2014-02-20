/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.provider;

import ch.ivyteam.ivy.visualvm.chart.MChartDataSource;

/**
 *
 * @author rwei
 */
class MChart implements IChart {
  private final MBeanInstance instance;
  private final String name;

  public MChart(MBeanInstance instance, String name) {
    this.instance = instance;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public MChartDataSource createDataSource() {
    return null;// type.createChartDataSource(instance, name);
  }

}
