package ch.ivyteam.ivy.visualvm.view.externaldb;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbTransactionChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.ExternalDbErrorQueryBuffer;
import ch.ivyteam.ivy.visualvm.service.ExternalDbSlowQueryBuffer;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel;
import ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent.DetailsView;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

public class ExternalDbView extends ExternalDbWsCommonView {

  private boolean uiComplete;
  private static final String CHARTS = "Charts";
  private static final String ERRORS = "Errors";
  private static final String SLOW_QUERIES = "Slow Queries";
  private final ExternalDbErrorQueryBuffer fErrorInfoBuffer;
  private final ExternalDbSlowQueryBuffer fSlowQueryBuffer;

  private DetailsView fChartsDetailsView;
  private ExternalDbErrorsPanel fUIErrorPanel;
  private ExternalDbSlowQueriesPanel fUISlowQueriesPanel;

  public ExternalDbView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fErrorInfoBuffer = new ExternalDbErrorQueryBuffer(getDataBeanProvider().getMBeanServerConnection());
    fSlowQueryBuffer = new ExternalDbSlowQueryBuffer(getDataBeanProvider().getMBeanServerConnection());
    registerScheduledUpdate(fErrorInfoBuffer);
    registerScheduledUpdate(fSlowQueryBuffer);
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
            = new ExternalDbProcessingTimeChartDataSource(
                    getDataBeanProvider(), null, null,
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
    if (!uiComplete) {
      JPanel panel = (JPanel) viewComponent.getComponent(0);
      panel.remove(0);
      createExternalDbView();
      Map<String, Map<String, Set<String>>> appEnvConfMap = DataUtils.getExternalDbConfigs(
              getDataBeanProvider()
              .getMBeanServerConnection());
      getUIChartsPanel().setTreeData(appEnvConfMap);
      uiComplete = true;
    }
    return viewComponent;
  }

  private void createExternalDbView() {
    ExternalDbWsCommonPanel chartsPanel = new ExternalDbWsCommonPanel(this);
    setUIChartsPanel(chartsPanel);
    fUIErrorPanel = new ExternalDbErrorsPanel(this);
    fUISlowQueriesPanel = new ExternalDbSlowQueriesPanel(this);

    fChartsDetailsView = new DataViewComponent.DetailsView(CHARTS, null, 10, chartsPanel, null);
    DetailsView fErrorsDetailsView = new DetailsView(ERRORS, null, 10, fUIErrorPanel, null);
    DetailsView fSlowQueriesView = new DetailsView(SLOW_QUERIES, null, 10, fUISlowQueriesPanel, null);

    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null,
            false),
            DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(fChartsDetailsView, DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(fErrorsDetailsView, DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(fSlowQueriesView, DataViewComponent.TOP_LEFT);
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
    builder.append("The chart shows the maximum, the average and the minimum time needed to execute"
            + " transactions since the last polling.<br><br>");
    builder.append("<b>Max: </b>").append(ExternalDbProcessingTimeChartDataSource.MAX_SERIE_DESC)
            .append(BR);
    builder.append("<b>Avg: </b>").append(ExternalDbProcessingTimeChartDataSource.MEAN_SERIE_DESC)
            .append(BR);
    builder.append("<b>Min: </b>").append(ExternalDbProcessingTimeChartDataSource.MIN_SERIE_DESC)
            .append(BR);
    builder.append("</html>");
    return builder.toString();
  }

  public void refreshErrorTab() {
    fUIErrorPanel.refresh(fErrorInfoBuffer.getBuffer());
  }

  public void refreshSlowQueriesTab() {
    fUISlowQueriesPanel.refresh(fSlowQueryBuffer.getBuffer());
  }

  public void showChart(String appName, String envName, String configName) {
    super.getViewComponent().selectDetailsView(fChartsDetailsView);
    fireCreateChartsAction(appName, envName, configName);
    setSelectedNode(appName, envName, configName);
  }

  @Override
  public void updateDisplay(QueryResult queryResult) {
    super.updateDisplay(queryResult);
    if (!fUIErrorPanel.isLoaded() && !fErrorInfoBuffer.getBuffer().isEmpty()) {
      refreshErrorTab();
    }
    if (!fUISlowQueriesPanel.isLoaded() && !fSlowQueryBuffer.getBuffer().isEmpty()) {
      refreshSlowQueriesTab();
    }
  }

}
