/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbTransactionChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author htnam
 */
public class ExternalDbView extends AbstractView {

  private static final String BR_TAG = "<br/>";

  private final ExternalDbPanel fExternalDBPanel;
  private String fCurrentAppName, fCurrentEnvName, fCurrentConfigName;
  private final Map<String, ChartsPanel> createdCharts;

  public ExternalDbView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
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

  private void configDataSources(AbstractExternalDbAndWebServiceDataSource... dataSources) {
    for (AbstractExternalDbAndWebServiceDataSource dataSource : dataSources) {
      dataSource.setNamePattern(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_PATTERN);
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
    Map<String, Map<String, Set<String>>> appEnvConfMap = DataUtils.getExternalDbConfigs(getDataBeanProvider()
            .getMBeanServerConnection());
    fExternalDBPanel.setTreeData(appEnvConfMap);
    return viewComponent;
  }

  void fireCreateChartsAction(String appName, String envName, String configName) {
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

  private String generateDescriptionForConnectionChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of open and the number of used connections to the external ");
    builder.append("database.").append(BR_TAG).append(BR_TAG);
    builder.append("<b>Open:</b>").append(ExternalDbConnectionChartDataSource.OPEN_SERIE_DESC).append(BR_TAG);
    builder.append("<b>Used:</b>").append(ExternalDbConnectionChartDataSource.USED_SERIE_DESC).append(BR_TAG);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForTransactionChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of transactions to the external database and the number of ");
    builder.append("them that were erroneous.<br><br>");
    builder.append("<b>Transactions:</b>")
            .append(ExternalDbTransactionChartDataSource.TRANSACTION_SERIE_DESC)
            .append(BR_TAG);
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
            .append(BR_TAG);
    builder.append("<b>Max</b>: ").append(ExternalDbProcessingTimeChartDataSource.MAX_SERIE_DESC)
            .append(BR_TAG);
    builder.append("<b>Min</b>: ").append(ExternalDbProcessingTimeChartDataSource.MIN_SERIE_DESC)
            .append(BR_TAG);
    builder.append("</html>");
    return builder.toString();
  }
}
