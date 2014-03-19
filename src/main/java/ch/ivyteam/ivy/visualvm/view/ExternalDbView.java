/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbTransactionChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author htnam
 */
public class ExternalDbView extends ExternalDbWsCommonView {

  public ExternalDbView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fUIPanel = new ExternalDbWsCommonPanel(this);
  }

  // call when user select environment & db configuration
  @Override
  protected ChartsPanel createChartPanel() {
    ChartsPanel chartPanel = new ChartsPanel(false);
    ExternalDbConnectionChartDataSource connectionDataSource = new ExternalDbConnectionChartDataSource(
            getDataBeanProvider(), null, null, "Connections");
    ExternalDbTransactionChartDataSource transactionDataSource = new ExternalDbTransactionChartDataSource(
            getDataBeanProvider(), null, null, "Transactions");
    ExternalDbProcessingTimeChartDataSource transProcessTimeDataSource
            = new ExternalDbProcessingTimeChartDataSource(getDataBeanProvider(), null, null,
                    "Processing Time [ms]");

    configDataSources(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_PATTERN,
            connectionDataSource, transactionDataSource, transProcessTimeDataSource);
    chartPanel.addChart(connectionDataSource, generateDescriptionForConnectionChart());
    chartPanel.addChart(transactionDataSource, generateDescriptionForTransactionChart());
    chartPanel.addChart(transProcessTimeDataSource, generateDescriptionForProcessingTimeChart());
    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(fUIPanel);
    Map<String, Map<String, Set<String>>> appEnvConfMap = DataUtils.getExternalDbConfigs(getDataBeanProvider()
            .getMBeanServerConnection());
    fUIPanel.setTreeData(appEnvConfMap);
    return viewComponent;
  }

  private String generateDescriptionForConnectionChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of open and the number of used connections to the external ");
    builder.append("database.").append(BR).append(BR);
    builder.append("<b>Open: </b>").append(ExternalDbConnectionChartDataSource.OPEN_SERIE_DESC)
            .append(BR);
    builder.append("<b>Used: </b>").append(ExternalDbConnectionChartDataSource.USED_SERIE_DESC)
            .append(BR);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForTransactionChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of transactions to the external database and the number of ");
    builder.append("them that were erroneous.<br><br>");
    builder.append("<b>Transactions: </b>").
            append(ExternalDbTransactionChartDataSource.TRANSACTION_SERIE_DESC).append(BR);
    builder.append("<b>Errors: </b>").
            append(ExternalDbTransactionChartDataSource.TRANSACTION_ERROR_SERIE_DESC);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForProcessingTimeChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the maximum, the mean and the minimum time needed to execute"
            + " transactions since the last poll.<br><br>");
    builder.append("<b>Max: </b>").append(ExternalDbProcessingTimeChartDataSource.MAX_SERIE_DESC)
            .append(BR);
    builder.append("<b>Mean: </b>").append(ExternalDbProcessingTimeChartDataSource.MEAN_SERIE_DESC)
            .append(BR);
    builder.append("<b>Min: </b>").append(ExternalDbProcessingTimeChartDataSource.MIN_SERIE_DESC)
            .append(BR);
    builder.append("</html>");
    return builder.toString();
  }

}
