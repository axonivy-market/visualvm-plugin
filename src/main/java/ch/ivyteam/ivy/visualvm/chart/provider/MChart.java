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
  private final MBeanInstance fInstance;
  private final String fName;

  public MChart(MBeanInstance instance, String name) {
    fInstance = instance;
    fName = name;
  }

  @Override
  public String getName() {
    return fName;
  }

  @Override
  public MChartDataSource createDataSource() {
    return null;// type.createChartDataSource(instance, name);
  }

}
