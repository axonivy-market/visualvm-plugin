package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MErrorChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.MProcessTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.MRequestChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.MSessionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JPanel;

public class RequestView extends AbstractView {

  private static final String REQUESTS = "Requests";
  private static final String ERRORS = "Errors";
  private static final String SESSIONS = "Sessions";
  private boolean uiComplete;

  public RequestView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(REQUESTS,
            false), DataViewComponent.TOP_RIGHT);
    ChartsPanel requestPanel = new ChartsPanel();

    MRequestChartDataSource requestDataSource = new MRequestChartDataSource(
            getDataBeanProvider(), REQUESTS, null, REQUESTS);
    MErrorChartDataSource errorDataSource = new MErrorChartDataSource(
            getDataBeanProvider(), ERRORS, null, ERRORS);
    MProcessTimeChartDataSource processingTimeDataSource = new MProcessTimeChartDataSource(
            getDataBeanProvider(), "Processing Time", null, "Time [ms]");
    MSessionChartDataSource sessionDataSource = new MSessionChartDataSource(
            getDataBeanProvider(), SESSIONS, null, SESSIONS);

    requestPanel.addChart(requestDataSource);
    requestPanel.addChart(errorDataSource);
    requestPanel.addChart(processingTimeDataSource);
    requestPanel.addChart(sessionDataSource);

    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(REQUESTS, null, 10,
            requestPanel.getUiComponent(), null), DataViewComponent.TOP_RIGHT);
    getUpdatableUIObjects().add(requestPanel);
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    JPanel panel = (JPanel) viewComponent.getComponent(0);
    if (panel != null) {
      panel.remove(0);
    }
    if (!uiComplete) {
      createRequestView();
      uiComplete = true;
    }
    return viewComponent;
  }

}
