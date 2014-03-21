/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import java.util.HashMap;
import java.util.Map;

abstract class ExternalDbWsCommonView extends AbstractView {

  private String fCurrentAppName, fCurrentEnvName, fCurrentConfigName;
  private final Map<String, ChartsPanel> fCreatedCharts;
  private ExternalDbWsCommonPanel fUIPanel;

  public ExternalDbWsCommonView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fCreatedCharts = new HashMap<>();
  }

  protected void fireCreateChartsAction(String appName, String envName, String configName) {
    fCurrentAppName = appName;
    fCurrentEnvName = envName;
    fCurrentConfigName = configName;
    String chartKey = appName + envName + configName;
    if (fCreatedCharts.containsKey(chartKey)) {
      fUIPanel.setChartPanelToVisible(fCreatedCharts.get(chartKey));
    } else {
      ChartsPanel chartsPanel = createChartPanel();
      fUIPanel.setChartPanelToVisible(chartsPanel);
      fCreatedCharts.put(chartKey, chartsPanel);
    }
  }

  protected void configDataSources(String namePattern,
          AbstractExternalDbAndWebServiceDataSource... dataSources) {
    for (AbstractExternalDbAndWebServiceDataSource dataSource : dataSources) {
      dataSource.setNamePattern(namePattern);
      dataSource.setApplication(fCurrentAppName);
      dataSource.setEnvironment(fCurrentEnvName);
      dataSource.setConfigName(fCurrentConfigName);
      dataSource.init();
    }
  }

  public void setUIPanel(ExternalDbWsCommonPanel uiPanel) {
    this.fUIPanel = uiPanel;
  }

  public ExternalDbWsCommonPanel getUIPanel() {
    return fUIPanel;
  }

  protected abstract ChartsPanel createChartPanel();
}
