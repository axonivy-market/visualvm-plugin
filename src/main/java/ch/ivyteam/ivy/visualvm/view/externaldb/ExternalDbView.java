package ch.ivyteam.ivy.visualvm.view.externaldb;

import ch.ivyteam.ivy.visualvm.ContentProvider;
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

  private static final String CHARTS = ContentProvider.get("Charts");
  private static final String ERRORS = ContentProvider.get("Errors");
  private static final String SLOW_QUERIES = ContentProvider.get("SlowQueries");
  private static final String CONNECTIONS = ContentProvider.get("Connections");
  private static final String TRANSACTIONS = ContentProvider.get("Transactions");
  private static final String PROCESSING_TIME = ContentProvider.get("ProcessingTime")
          + " [" + ContentProvider.get("MillisecondAbbr") + "]";

  private boolean uiComplete;
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

  @Override
  protected ChartsPanel createChartPanel(String title) {
    ChartsPanel chartPanel;
    if (title != null) {
      chartPanel = new ChartsPanel(false, title);
    } else {
      chartPanel = new ChartsPanel(false);
    }
    ExternalDbConnectionChartDataSource connectionDataSource = new ExternalDbConnectionChartDataSource(
            getDataBeanProvider(), null, null, CONNECTIONS);
    ExternalDbTransactionChartDataSource transactionDataSource = new ExternalDbTransactionChartDataSource(
            getDataBeanProvider(), null, null, TRANSACTIONS);
    ExternalDbProcessingTimeChartDataSource transProcessTimeDataSource
            = new ExternalDbProcessingTimeChartDataSource(getDataBeanProvider(), null, null, PROCESSING_TIME);

    configDataSources(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_PATTERN, connectionDataSource,
            transactionDataSource, transProcessTimeDataSource);
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
      updateConfigTreeNodes();
      uiComplete = true;
    }
    return viewComponent;
  }

  private void updateConfigTreeNodes() {
    Map<String, Map<String, Set<String>>> appEnvConfMap = DataUtils.getExternalDbConfigs(
            getDataBeanProvider().getMBeanServerConnection());
    getUIChartsPanel().setTreeData(appEnvConfMap);
  }

  private void createExternalDbView() {
    fChartsPanel = new ExternalDbWsCommonPanel(this);
    setUIChartsPanel(fChartsPanel);
    fUIErrorPanel = new ExternalDbErrorsPanel(this);
    fUISlowQueriesPanel = new ExternalDbSlowQueriesPanel(this);

    fChartsDetailsView = new DataViewComponent.DetailsView(CHARTS, null, 10, fChartsPanel, null);
    DetailsView fErrorsDetailsView = new DetailsView(ERRORS, null, 10, fUIErrorPanel, null);
    DetailsView fSlowQueriesView = new DetailsView(SLOW_QUERIES, null, 10, fUISlowQueriesPanel, null);

    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null, false),
            DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(fChartsDetailsView, DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(fErrorsDetailsView, DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(fSlowQueriesView, DataViewComponent.TOP_LEFT);
  }

  private ExternalDbWsCommonPanel fChartsPanel;

  private String generateDescriptionForConnectionChart() {
    return ContentProvider.getFormatted("ExtDbConnectionChartDescription");
  }

  private String generateDescriptionForTransactionChart() {
    return ContentProvider.getFormatted("ExtDbTransactionChartDescription");
  }

  private String generateDescriptionForProcessingTimeChart() {
    return ContentProvider.getFormatted("ExtDbProcessingTimeChartDescription");
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
    if (!fChartsPanel.containsNode(appName, envName, configName)) {
      updateConfigTreeNodes();
    }
    setSelectedNode(appName, envName, configName);
    fChartsPanel.refreshOpenedNodes();
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
