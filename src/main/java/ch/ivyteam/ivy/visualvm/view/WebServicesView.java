/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceCallsChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.management.ObjectName;

/**
 *
 * @author htnam
 */
public class WebServicesView extends AbstractView {

  private static final String APP_STRING_KEY = "application";
  private static final String ENVIRONMENT_STRING_KEY = "environment";
  private static final String CONFIG_STRING_KEY = "name";

  private final Application fIvyApplication;
  private final WebServicesPanel fWebServicesPanel;
  private Set<ObjectName> fWebServicesConfigList;
  private String fCurrentAppName, fCurrentEnvName, fCurrentConfigName;
  private final Map<String, ChartsPanel> createdCharts;

  public WebServicesView(IDataBeanProvider dataBeanProvider, Application application) {
    super(dataBeanProvider);
    fIvyApplication = application;
    createdCharts = new HashMap<>();
    fWebServicesPanel = new WebServicesPanel(this);
  }

  // call when user select environment & db configuration
  private ChartsPanel createWebServicesChartPanel() {

    ChartsPanel chartPanel = new ChartsPanel(false);
    WebServiceCallsChartDataSource callsDataSource = new WebServiceCallsChartDataSource(
            getDataBeanProvider(), null, null, "Calls");
    WebServiceProcessingTimeChartDataSource processTimeDataSource
            = new WebServiceProcessingTimeChartDataSource(getDataBeanProvider(), null, null,
                    "Processing Time [µs]");

    configDataSources(callsDataSource, processTimeDataSource);
    chartPanel.addChart(callsDataSource);
    chartPanel.addChart(processTimeDataSource);
    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  private void configDataSources(AbstractExternalDbAndWebServiceDataSource... dataSources) {
    for (AbstractExternalDbAndWebServiceDataSource dataSource : dataSources) {
      dataSource.setNamePattern(IvyJmxConstant.IvyServer.WebService.NAME_PATTERN);
      dataSource.setApplication(fCurrentAppName);
      dataSource.setEnvironment(fCurrentEnvName);
      dataSource.setConfigName(fCurrentConfigName);
      dataSource.init();
    }
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(fWebServicesPanel);
    Map<String, Map<String, Set<String>>> appEnvWs = DataUtils.getWebServicesConfigs(getDataBeanProvider()
            .getMBeanServerConnection());
    fWebServicesPanel.setTreeData(appEnvWs);
    return viewComponent;
  }

  void fireCreateChartsAction(String appName, String envName, String configName) {
    fCurrentAppName = appName;
    fCurrentEnvName = envName;
    fCurrentConfigName = configName;
    String chartKey = appName + envName + configName;
    if (createdCharts.containsKey(chartKey)) {
      fWebServicesPanel.setChartPanelToVisible(createdCharts.get(chartKey));
    } else {
      ChartsPanel chart = createWebServicesChartPanel();
      fWebServicesPanel.setChartPanelToVisible(chart);
      createdCharts.put(chartKey, chart);
    }
  }
}
