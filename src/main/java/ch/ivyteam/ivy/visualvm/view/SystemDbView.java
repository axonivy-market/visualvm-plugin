package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.TransactionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JComponent;

public class SystemDbView extends AbstractView {

  public SystemDbView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private JComponent createSystemDbView() {
    ChartsPanel systemDbPanel = new ChartsPanel(false);
    ConnectionChartDataSource connectionNumberDataSource = new ConnectionChartDataSource(
            getDataBeanProvider(), null, null, "Connections");
    TransactionChartDataSource transactionNumberDataSource = new TransactionChartDataSource(
            getDataBeanProvider(), null, null, "Transactions");
    ProcessingTimeChartDataSource processTimeDataSource = new ProcessingTimeChartDataSource(
            getDataBeanProvider(), null, null, "Processing Time [ms]");

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
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of open and the number of used connections to the system ");
    builder.append("database.<br><br>");
    builder.append("<b>Open:</b> " + ConnectionChartDataSource.OPEN_SERIE_DESC + "<br>");
    builder.append("<b>Used:</b> " + ConnectionChartDataSource.USED_SERIE_DESC);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForTransactionChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the number of transactions to the system database and the number of ");
    builder.append("them that were erroneous.<br><br>");
    builder.append("<b>Transactions:</b> " + TransactionChartDataSource.TRANSACTION_SERIE_DESC + "<br>");
    builder.append("<b>Errors:</b> " + TransactionChartDataSource.ERROR_SERIE_DESC);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForProcessingTimeChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the maximum and the minimum time needed to transaction a execute in ");
    builder.append("last poll.<br><br>");
    builder.append("<b>Max</b>: " + ProcessingTimeChartDataSource.MAX_SERIE_DESC + "<br>");
    builder.append("<b>Mean</b>: " + ProcessingTimeChartDataSource.MEAN_SERIE_DESC + "<br>");
    builder.append("<b>Min</b>: " + ProcessingTimeChartDataSource.MIN_SERIE_DESC);
    builder.append("</html>");
    return builder.toString();
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(createSystemDbView());
    return viewComponent;
  }
}
