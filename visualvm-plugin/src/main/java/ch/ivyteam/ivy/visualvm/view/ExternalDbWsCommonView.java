/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.view.common.AbstractView;
import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ExternalDbWsCommonView extends AbstractView {

  private String fCurrentAppName, fCurrentEnvName, fCurrentConfigName;
  private final Map<String, ChartsPanel> fCreatedCharts;
  private ExternalDbWsCommonPanel fUIChartsPanel;
  private boolean fUICompleted;

  public ExternalDbWsCommonView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fCreatedCharts = new HashMap<>();
  }

  protected void fireCreateChartsAction(String appName, String envName, String configName) {
    fCurrentAppName = appName;
    fCurrentEnvName = envName;
    fCurrentConfigName = configName;
    String chartKey = appName + "_" + envName + "_" + configName;
    if (fCreatedCharts.containsKey(chartKey)) {
      fUIChartsPanel.setChartPanelToVisible(fCreatedCharts.get(chartKey));
    } else {
      String title = "<html><b>" + appName + " > " + envName + " > "
              + ExternalDbWsCommonPanel.cutNodeTextToView(configName)
              + "</b></html>";
      ChartsPanel chartsPanel = createChartPanel(title);
      fUIChartsPanel.setChartPanelToVisible(chartsPanel);
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

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!fUICompleted) {
      addPanelsToView(viewComponent);
      fUICompleted = true;
    }
    return viewComponent;
  }
  
  private void addPanelsToView(DataViewComponent viewComponent) {
    createPanels(viewComponent);
    updateConfigTreeNodes();
  }
  
  protected abstract void createPanels(DataViewComponent viewComponent);
  protected abstract void updateConfigTreeNodes();

  public void setUIChartsPanel(ExternalDbWsCommonPanel uiPanel) {
    this.fUIChartsPanel = uiPanel;
  }

  public ExternalDbWsCommonPanel getUIChartsPanel() {
    return fUIChartsPanel;
  }

  protected abstract ChartsPanel createChartPanel(String title);

  protected ChartsPanel createChartPanel() {
    return createChartPanel(null);
  }

  public void setSelectedNode(String appName, String envName, String confEnvName) {
    fUIChartsPanel.setSelectedNode(appName, envName, confEnvName);
  }

  public Set<String> getCreatedChartKeySet() {
    return fCreatedCharts.keySet();
  }
  
  public void showChart(String appName, String envName, String configName) {
    getViewComponent().selectDetailsView(getChartsDetailsView());
    fireCreateChartsAction(appName, envName, configName);
    if (!getUIChartsPanel().containsNode(appName, envName, configName)) {
      updateConfigTreeNodes();
    }
    setSelectedNode(appName, envName, configName);
    getUIChartsPanel().refreshOpenedNodes();
  }
  
  protected DataViewComponent.DetailsView getChartsDetailsView() {
    return null;
  }
  
}
