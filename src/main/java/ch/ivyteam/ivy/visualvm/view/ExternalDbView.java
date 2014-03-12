/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.AbstractExternalDbDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbTransactionChartDataSource;
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
    ExternalDbProcessingTimeChartDataSource transProcessTimeDataSource
            = new ExternalDbProcessingTimeChartDataSource(getDataBeanProvider(), null, null,
                    "Processing Time [µs]");

    configDataSources(connectionDataSource, transactionDataSource, transProcessTimeDataSource);
    chartPanel.addChart(connectionDataSource, generateDescriptionForConnectionChart());
    chartPanel.addChart(transactionDataSource, generateDescriptionForTransactionChart());
    chartPanel.addChart(transProcessTimeDataSource, generateDescriptionForProcessingTimeChart());
    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  private void configDataSources(AbstractExternalDbDataSource... dataSources) {
    for (AbstractExternalDbDataSource dataSource : dataSources) {
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
    initExternalDbData();
    return viewComponent;
  }

  private void initExternalDbData() {
    MBeanServerConnection mbeanConnection = DataUtils.getMBeanServerConnection(fIvyApplication);
    // List<String> includes string with pattern ApplicationName:EvironmentName:ExtDBConfiguration
    fExternalDbConfigList = DataUtils.getExternalDbConfigs(mbeanConnection);

    Map<String, Map<String, Set<String>>> appEnvConfMap = new TreeMap<>();
    for (ObjectName each : fExternalDbConfigList) {
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
    fExternalDBPanel.setTreeListData(appEnvConfMap);
  }

  void fireCreateChartsAction(String appName, String envName, String configName) {
    if (checkCorrectSelection(appName, envName, configName)) {
      fCurrentAppName = appName;
      fCurrentEnvName = envName;
      fCurrentConfigName = configName;
      String chartKey = appName + envName + configName;
      if (createdCharts.containsKey(chartKey)) {
        fExternalDBPanel.setChartPanelToVisible(createdCharts.get(chartKey));
      } else {
        ChartsPanel chart = createExternalDbChartPanel();
        fExternalDBPanel.setChartPanelToVisible(chart);
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

  private String generateDescriptionForConnectionChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of open and the number of used connections to the external ");
    builder.append("database.<br><br>");
    builder.append("<b>Open:</b>").append(ExternalDbConnectionChartDataSource.OPEN_SERIE_DESC).append("<br>");
    builder.append("<b>Used:</b>").append(ExternalDbConnectionChartDataSource.USED_SERIE_DESC).append("<br>");
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForTransactionChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of transactions to the external database and the number of ");
    builder.append("them that were erroneous.<br><br>");
    builder.append("<b>Transactions:</b>").append(ExternalDbTransactionChartDataSource.TRANSACTION_SERIE_DESC)
            .append("<br>");
    builder.append("<b>Errors:</b>").append(ExternalDbTransactionChartDataSource.ERRORS_SERIE_DESC);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForProcessingTimeChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the maximum and the minimum time needed to transaction a execute in ");
    builder.append("last poll<br><br>");
    builder.append("<b>Mean</b>: ").append(ExternalDbProcessingTimeChartDataSource.MEAN_SERIE_DESC)
            .append("<br>");
    builder.append("<b>Max</b>: ").append(ExternalDbProcessingTimeChartDataSource.MAX_SERIE_DESC)
            .append("<br>");
    builder.append("<b>Min</b>: ").append(ExternalDbProcessingTimeChartDataSource.MIN_SERIE_DESC)
            .append("<br>");
    builder.append("</html>");
    return builder.toString();
  }
}
