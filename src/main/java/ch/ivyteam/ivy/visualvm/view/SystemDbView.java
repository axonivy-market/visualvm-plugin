package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.TransactionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ProcessingTimeChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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

    systemDbPanel.getUIComponent().registerKeyboardAction(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                systemDbPanel.switchLayoutOrientation();
              }

            }, KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
            );
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      JPanel panel = (JPanel) viewComponent.getComponent(0);
      panel.remove(0);
      createRequestView();
      uiComplete = true;
    }
    return viewComponent;
  }

}
