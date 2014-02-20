/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.provider;

import java.util.List;

/**
 *
 * @author rwei
 */
interface IObjectInstance {
  String getName();

  List<IChart> getCharts();

}
