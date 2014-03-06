package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.TransactionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JPanel;

public class SystemDbView extends AbstractView {

  private boolean uiComplete;

  public SystemDbView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    final ChartsPanel systemDbPanel = new ChartsPanel(false);

    ConnectionChartDataSource connectionNumberDataSource = new ConnectionChartDataSource(
            getDataBeanProvider(), null, null, "Connections");
    TransactionChartDataSource transactionNumberDataSource = new TransactionChartDataSource(
            getDataBeanProvider(), null, null, "Transactions");
    // ProcessingTimeChartDataSource transProcessTimeDataSource = new ProcessingTimeChartDataSource(
    // getDataBeanProvider(), null, null, "Processing Time");

    systemDbPanel.addChart(connectionNumberDataSource);
    systemDbPanel.addChart(transactionNumberDataSource);
    // systemDbPanel.addChart(transProcessTimeDataSource);

    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null,
            false), DataViewComponent.TOP_LEFT);
    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(null, null, 10,
            systemDbPanel.getUIComponent(), null), DataViewComponent.TOP_LEFT);
    registerScheduledUpdate(systemDbPanel);
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      JPanel panel = (JPanel) viewComponent.getComponent(0);
      JPanel c = (JPanel) panel.getComponent(0);
      c.remove(0);
      createRequestView();
      uiComplete = true;
    }
    return viewComponent;
  }

}
