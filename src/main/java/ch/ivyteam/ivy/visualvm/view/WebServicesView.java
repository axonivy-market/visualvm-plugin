/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.AbstractExternalDbDataSource;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.apache.commons.lang.StringUtils;

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
//    ExternalDbConnectionChartDataSource connectionDataSource = new ExternalDbConnectionChartDataSource(
//            getDataBeanProvider(), null, null, "Connections");
//    ExternalDbTransactionChartDataSource transactionDataSource = new ExternalDbTransactionChartDataSource(
//            getDataBeanProvider(), null, null, "Transactions");
//    ExternalDbProcessingTimeChartDataSource transProcessTimeDataSource
//            = new ExternalDbProcessingTimeChartDataSource(getDataBeanProvider(), null, null,
//                    "Processing Time [µs]");
//
//    configDataSources(connectionDataSource, transactionDataSource, transProcessTimeDataSource);
//    chartPanel.addChart(connectionDataSource);
//    chartPanel.addChart(transactionDataSource);
//    chartPanel.addChart(transProcessTimeDataSource);
//    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  private void configDataSources(AbstractExternalDbDataSource... dataSources) {
//    for (AbstractExternalDbDataSource dataSource : dataSources) {
//      dataSource.setApplication(fCurrentAppName);
//      dataSource.setEnvironment(fCurrentEnvName);
//      dataSource.setConfigName(fCurrentConfigName);
//      dataSource.init();
//    }
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(fWebServicesPanel);
    initExternalDbData();
    return viewComponent;
  }

  private void initExternalDbData() {
    MBeanServerConnection mbeanConnection = DataUtils.getMBeanServerConnection(fIvyApplication);
    // List<String> includes string with pattern ApplicationName:EvironmentName:ExtDBConfiguration
    fWebServicesConfigList = DataUtils.getWebServicesConfigs(mbeanConnection);

    Map<String, Map<String, Set<String>>> appEnvConfMap = new TreeMap<>();
    for (ObjectName each : fWebServicesConfigList) {
      String app = each.getKeyProperty(APP_STRING_KEY);
      String env = each.getKeyProperty(ENVIRONMENT_STRING_KEY);
      String conf = each.getKeyProperty(CONFIG_STRING_KEY);

      if (appEnvConfMap.containsKey(app)) {
        Map<String, Set<String>> envConfMap = appEnvConfMap.get(app);
        if (envConfMap.containsKey(env)) {
          Set<String> confs = envConfMap.get(env);
          confs.add(conf);
        } else {
          Set<String> confList = new TreeSet();
          confList.add(conf);
          envConfMap.put(env, confList);
        }
      } else {
        Set<String> confs = new TreeSet<>();
        confs.add(conf);
        Map<String, Set<String>> envConfMap = new TreeMap<>();
        envConfMap.put(env, confs);
        appEnvConfMap.put(app, envConfMap);
      }
    }
    fWebServicesPanel.setTreeListData(appEnvConfMap);
  }

  void fireCreateChartsAction(String appName, String envName, String configName) {
    if (checkCorrectSelection(appName, envName, configName)) {
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

  private boolean checkCorrectSelection(String appName, String envName, String confName) {
    if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(envName) || StringUtils.isEmpty(confName)) {
      return false;
    }

    for (ObjectName appEnvConf : fWebServicesConfigList) {
      if (appEnvConf.getKeyProperty(APP_STRING_KEY).equals(appName)
              && appEnvConf.getKeyProperty(ENVIRONMENT_STRING_KEY).equals(envName)
              && appEnvConf.getKeyProperty(CONFIG_STRING_KEY).equals(confName)) {
        return true;
      }
    }
    return false;
  }

}
