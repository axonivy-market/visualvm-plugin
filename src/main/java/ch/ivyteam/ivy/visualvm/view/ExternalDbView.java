/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.AbstractEDBDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbTransactionChartDataSource;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author htnam
 */
public class ExternalDbView extends AbstractView {

  private static final String APP_STRING_KEY = "application";
  private static final String ENVIRONMENT_STRING_KEY = "environment";
  private static final String CONFIG_STRING_KEY = "name";

  private final Application fIvyApplication;
  private final ExternalDbPanel fExternalDBPanel;
  private Set<ObjectName> fExternalDbConfigList;
  private String fCurrentAppName, fCurrentEnvName, fCurrentConfigName;
  private final Map<String, ChartsPanel> createdCharts;

  public ExternalDbView(IDataBeanProvider dataBeanProvider, Application application) {
    super(dataBeanProvider);
    fIvyApplication = application;
    createdCharts = new HashMap<>();
    fExternalDBPanel = new ExternalDbPanel(this);
  }

  // call when user select environment & db configuration
  private ChartsPanel createExternalDbChartPanel() {

    ChartsPanel chartPanel = new ChartsPanel(false);
    ExternalDbConnectionChartDataSource connectionDataSource = new ExternalDbConnectionChartDataSource(
            getDataBeanProvider(), null, null, "Connections");
    ExternalDbTransactionChartDataSource transactionDataSource = new ExternalDbTransactionChartDataSource(
            getDataBeanProvider(), null, null, "Transactions");
    ExternalDbProcessingTimeChartDataSource transProcessTimeDataSource = new ExternalDbProcessingTimeChartDataSource(
            getDataBeanProvider(), null, null,
            "Processing Time [ms]");

    configDataSources(connectionDataSource, transactionDataSource, transProcessTimeDataSource);
    chartPanel.addChart(connectionDataSource);
    chartPanel.addChart(transactionDataSource);
    chartPanel.addChart(transProcessTimeDataSource);
    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  private void configDataSources(AbstractEDBDataSource... dataSources) {
    for (AbstractEDBDataSource dataSource : dataSources) {
      dataSource.setApplication(fCurrentAppName);
      dataSource.setEnvironment(fCurrentEnvName);
      dataSource.setConfigName(fCurrentConfigName);
      dataSource.init();
    }
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(fExternalDBPanel);
    initExtDbData();
    return viewComponent;
  }

  private void initExtDbData() {
    MBeanServerConnection mbeanConnection = DataUtils.getMBeanConnection(fIvyApplication);
    // List<String> includes string with pattern ApplicationName:EvironmentName:ExtDBConfiguration
    fExternalDbConfigList = DataUtils.getExternalDbConfigs(mbeanConnection);

    Map<String, Set<String>> appEnvMap = new HashMap<>();
    Set<String> configs = new TreeSet<>();
    for (ObjectName each : fExternalDbConfigList) {

      if (appEnvMap.containsKey(each.getKeyProperty(APP_STRING_KEY))) {
        Set<String> env = appEnvMap.get(each.getKeyProperty(APP_STRING_KEY));
        env.add(each.getKeyProperty(ENVIRONMENT_STRING_KEY));
      } else {
        Set<String> envList = new TreeSet();
        envList.add(each.getKeyProperty(ENVIRONMENT_STRING_KEY));
        appEnvMap.put(each.getKeyProperty(APP_STRING_KEY), envList);
      }

      configs.add(each.getKeyProperty(CONFIG_STRING_KEY)); // add all db configs into the list
    }
    fExternalDBPanel.initTreeData(appEnvMap);
    fExternalDBPanel.initListData(configs);
  }

  void fireCreateChartsAction(String appName, String envName, String configName) {
    if (checkCorrectSelection(appName, envName, configName)) {
      fCurrentAppName = appName;
      fCurrentEnvName = envName;
      fCurrentConfigName = configName;
      String chartKey = appName + envName + configName;
      if (createdCharts.containsKey(chartKey)) {
        fExternalDBPanel.addChartPanel(createdCharts.get(chartKey));
      } else {
        ChartsPanel chart = createExternalDbChartPanel();
        fExternalDBPanel.addChartPanel(chart);
        createdCharts.put(chartKey, chart);
      }
    }
  }

  private boolean checkCorrectSelection(String appName, String envName, String confName) {
    if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(envName) || StringUtils.isEmpty(confName)) {
      return false;
    }

    for (ObjectName appEnvConf : fExternalDbConfigList) {
      if (appEnvConf.getKeyProperty(APP_STRING_KEY).equals(appName)
              && appEnvConf.getKeyProperty(ENVIRONMENT_STRING_KEY).equals(envName)
              && appEnvConf.getKeyProperty(CONFIG_STRING_KEY).equals(confName)) {
        return true;
      }
    }
    return false;
  }
}
