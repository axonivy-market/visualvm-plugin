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

  private final Application fIvyApplication;
  private final ExternalDbPanel fExternalDBPanel;
  private Set<ObjectName> fExternalDbConfigList;
  private String fCurrentAppName, fCurrentEnvName, fCurrentConfigName;
  private ChartsPanel fExternalDbChartPanel;

  public ExternalDbView(IDataBeanProvider dataBeanProvider, Application application) {
    super(dataBeanProvider);
    fIvyApplication = application;
    fExternalDBPanel = new ExternalDbPanel(this);
  }

// call when user select environment & db configuration
  private ChartsPanel createExternalDbChartPanel() {
    unregisterScheduledUpdate(fExternalDbChartPanel);

    fExternalDbChartPanel = new ChartsPanel(false);
    ExternalDbConnectionChartDataSource connectionDataSource = new ExternalDbConnectionChartDataSource(
            getDataBeanProvider(), null, null, "Connections");
    ExternalDbTransactionChartDataSource transactionDataSource = new ExternalDbTransactionChartDataSource(
            getDataBeanProvider(), null, null, "Transactions");
    ExternalDbProcessingTimeChartDataSource transProcessTimeDataSource
            = new ExternalDbProcessingTimeChartDataSource(
                    getDataBeanProvider(), null, null, "Processing Time [ms]");

    configDataSources(connectionDataSource, transactionDataSource, transProcessTimeDataSource);
    fExternalDbChartPanel.addChart(connectionDataSource);
    fExternalDbChartPanel.addChart(transactionDataSource);
    fExternalDbChartPanel.addChart(transProcessTimeDataSource);

    registerScheduledUpdate(fExternalDbChartPanel);
    return fExternalDbChartPanel;
  }

  // String[] appEnvConf includes Application, Environment, Configuration
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

    Map<String, Set<String>> appEnvMap = new HashMap();
    Set<String> configs = new TreeSet();
    String appString = "application", envString = "environment", configString = "name";
    for (ObjectName each : fExternalDbConfigList) {

      if (appEnvMap.containsKey(each.getKeyProperty(appString))) {
        Set<String> env = appEnvMap.get(each.getKeyProperty(appString));
        env.add(each.getKeyProperty(envString));
      } else {
        Set<String> envList = new TreeSet();
        envList.add(each.getKeyProperty(envString));
        appEnvMap.put(each.getKeyProperty(appString), envList);
      }

      configs.add(each.getKeyProperty(configString)); // add all db configs into the list
    }
    fExternalDBPanel.initTreeData(appEnvMap);
    fExternalDBPanel.initListData(configs);
  }

  void fireCreateChartsAction(String appName, String envName, String configName) {
    if (checkCorrectSelection(appName, envName, configName)) {
      fCurrentAppName = appName;
      fCurrentEnvName = envName;
      fCurrentConfigName = configName;
      fExternalDBPanel.addChartPanel(createExternalDbChartPanel());
    }
  }

  private boolean checkCorrectSelection(String appName, String envName, String confName) {
    if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(envName) || StringUtils.isEmpty(confName)) {
      return false;
    }

    for (ObjectName appEnvConf : fExternalDbConfigList) {
      if (appEnvConf.getKeyProperty("application").equals(appName)
              && appEnvConf.getKeyProperty("environment").equals(envName)
              && appEnvConf.getKeyProperty("name").equals(confName)) {
        return true;
      }
    }
    return false;
  }

}
