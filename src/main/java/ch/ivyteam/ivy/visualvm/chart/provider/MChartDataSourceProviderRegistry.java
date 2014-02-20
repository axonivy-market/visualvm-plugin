/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.provider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rwei
 */
public class MChartDataSourceProviderRegistry {
  private List<IObjectType> providers = new ArrayList<>();

  public List<IObjectType> getObjectTypes() {
    return providers;
  }

}
