package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.MErrorChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MProcessTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MRequestChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MSessionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class RequestView extends AbstractView {

  private static final String REQUESTS = "Requests";
  private static final String ERRORS = "Errors";
  private static final String SESSIONS = "Sessions";
//  private static final String PROCESS_TIME = "Processing Time";
  private static final String TIME_MS = "Processing Time [ms]";
  private boolean uiComplete;

  public RequestView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    final ChartsPanel requestPanel = new ChartsPanel(true);
    ChartsPanel sessionPanel = new ChartsPanel(true);

    MRequestChartDataSource requestDataSource = new MRequestChartDataSource(
            getDataBeanProvider(), null, null, REQUESTS);
    MErrorChartDataSource errorDataSource = new MErrorChartDataSource(
            getDataBeanProvider(), null, null, ERRORS);
    MProcessTimeChartDataSource processingTimeDataSource = new MProcessTimeChartDataSource(
            getDataBeanProvider(), null, null, TIME_MS);
    MSessionChartDataSource sessionDataSource = new MSessionChartDataSource(
            getDataBeanProvider(), null, null, SESSIONS);

    requestPanel.addChart(requestDataSource);
    requestPanel.addChart(errorDataSource);
    requestPanel.addChart(processingTimeDataSource);
    sessionPanel.addChart(sessionDataSource);

    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null,
            false), DataViewComponent.TOP_LEFT);
    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(REQUESTS, null, 10,
            requestPanel.getUiComponent(), null), DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(SESSIONS, null, 10,
            sessionPanel.getUiComponent(), null), DataViewComponent.TOP_LEFT);
    registerScheduledUpdate(requestPanel);
    registerScheduledUpdate(sessionPanel);

    requestPanel.getUiComponent().registerKeyboardAction(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                requestPanel.switchLayoutOrientation();
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
