/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.provider;

import ch.ivyteam.ivy.visualvm.chart.MChartDataSource;

/**
 *
 * @author rwei
 */
public interface IChart {
  String getName();

  MChartDataSource createDataSource();

}
