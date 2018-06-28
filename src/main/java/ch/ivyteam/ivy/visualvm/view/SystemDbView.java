package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.view.common.AbstractView;
import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.TransactionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JComponent;

public class SystemDbView extends AbstractView {
  private static final String CONNECTIONS = ContentProvider.get("Connections");
  private static final String TRANSACTIONS = ContentProvider.get("Transactions");
  private static final String PROCESSING_TIME = ContentProvider.get("ProcessingTime")
          + " [" + ContentProvider.get("MillisecondAbbr") + "]";

  public SystemDbView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private JComponent createSystemDbView() {
    ChartsPanel systemDbPanel = new ChartsPanel(false);
    ConnectionChartDataSource connectionNumberDataSource = new ConnectionChartDataSource(
            getDataBeanProvider(), null, null, CONNECTIONS);
    TransactionChartDataSource transactionNumberDataSource = new TransactionChartDataSource(
            getDataBeanProvider(), null, null, TRANSACTIONS);
    ProcessingTimeChartDataSource processTimeDataSource = new ProcessingTimeChartDataSource(
            getDataBeanProvider(), null, null, PROCESSING_TIME);

    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null,
            false), DataViewComponent.TOP_LEFT);

    systemDbPanel.addChart(connectionNumberDataSource, generateDescriptionForConnectionChart());
    systemDbPanel.addChart(transactionNumberDataSource, generateDescriptionForTransactionChart());
    systemDbPanel.addChart(processTimeDataSource, generateDescriptionForProcessingTimeChart());

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("", null, 10,
            systemDbPanel.getUIComponent(), null), DataViewComponent.TOP_LEFT);
    registerScheduledUpdate(systemDbPanel);
    return systemDbPanel.getUIComponent();
  }

  private String generateDescriptionForConnectionChart() {
    return ContentProvider.getFormatted("SysDbConnectionChartDescription");
  }

  private String generateDescriptionForTransactionChart() {
    return ContentProvider.getFormatted("SysDbTransactionChartDescription");
  }

  private String generateDescriptionForProcessingTimeChart() {
    return ContentProvider.getFormatted("SysDbProcessingTimeChartDescription");
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(createSystemDbView());
    return viewComponent;
  }

}
