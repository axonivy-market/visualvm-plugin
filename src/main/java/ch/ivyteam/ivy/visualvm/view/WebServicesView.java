/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceCallsChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;

import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

/**
 *
 * @author htnam
 */
public class WebServicesView extends AbstractView {

  private final WebServicesPanel fWebServicesPanel;
  private String fCurrentAppName, fCurrentEnvName, fCurrentConfigName;
  private final Map<String, ChartsPanel> createdCharts;

  public WebServicesView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
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
                    "Processing Time [ms]");

    configDataSources(callsDataSource, processTimeDataSource);
    chartPanel.addChart(callsDataSource, generateDescriptionForCallsChart());
    chartPanel.addChart(processTimeDataSource, generateDescriptionForProcessingTimeChart());
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
  public static final String BR = "<br>";

  private String generateDescriptionForCallsChart() {
    String callDesc = "The chart shows the number of calls to the web service and "
            + "the number of them that were erroneous.";
    StringBuilder builder = new StringBuilder();
    builder.append("<html>").append(callDesc).append(BR).append(BR);
    builder.append("<b>Calls:</b> ").append(WebServiceCallsChartDataSource.CALLS_SERIE_DESCRIPTION)
            .append(BR);
    builder.append("<b>Errors:</b> ").append(WebServiceCallsChartDataSource.ERRORS_SERIE_DESCRIPTION);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForProcessingTimeChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the maximum, the mean and the minimum time needed to execute a ");
    builder.append("call since the last poll.").append(BR).append(BR);
    builder.append("<b>Max</b>: ").append(WebServiceProcessingTimeChartDataSource.MAX_SERIE_DESCRIPTION)
            .append(BR);
    builder.append("<b>Mean</b>: ").append(WebServiceProcessingTimeChartDataSource.MEAN_SERIE_DESCRIPTION)
            .append(BR);
    builder.append("<b>Min</b>: ").append(WebServiceProcessingTimeChartDataSource.MIN_SERIE_DESCRIPTION);
    builder.append("</html>");
    return builder.toString();
  }

}
