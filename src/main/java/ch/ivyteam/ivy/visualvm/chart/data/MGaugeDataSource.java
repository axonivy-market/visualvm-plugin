/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data;

/**
 *
 * @author thtam
 */
public class MGaugeDataSource {
  private String fChartName;

  public MGaugeDataSource(String chartName) {
    fChartName = chartName;
  }

  public String getChartName() {
    return fChartName;
  }

}
