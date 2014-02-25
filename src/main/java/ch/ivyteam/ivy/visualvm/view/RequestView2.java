package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MErrorChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.MProcessTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.MRequestChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.MSessionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

public class RequestView2 extends AbstractView {

  private static final String REQUESTS = "Requests";
  private static final String ERRORS = "Errors";
  private static final String SESSIONS = "Sessions";
  public static final String PROCESS_TIME = "Processing Time";
  public static final String TIME_MS = "Time [ms]";
  private boolean uiComplete;

  public RequestView2(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(REQUESTS,
            false), DataViewComponent.TOP_RIGHT);
    ChartsPanel requestPanel = new ChartsPanel();
    ChartsPanel sessionPanel = new ChartsPanel();

    MRequestChartDataSource requestDataSource = new MRequestChartDataSource(
            getDataBeanProvider(), REQUESTS, null, REQUESTS);
    MErrorChartDataSource errorDataSource = new MErrorChartDataSource(
            getDataBeanProvider(), ERRORS, null, ERRORS);
    MProcessTimeChartDataSource processingTimeDataSource = new MProcessTimeChartDataSource(
            getDataBeanProvider(), PROCESS_TIME, null, TIME_MS);
    MSessionChartDataSource sessionDataSource = new MSessionChartDataSource(
            getDataBeanProvider(), SESSIONS, null, SESSIONS);

    requestPanel.addChart(requestDataSource);
    requestPanel.addChart(errorDataSource);
    requestPanel.addChart(processingTimeDataSource);
    sessionPanel.addChart(sessionDataSource);

    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(REQUESTS, null, 10,
            requestPanel.getUiComponent(), null), DataViewComponent.TOP_RIGHT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(SESSIONS, null, 10,
            sessionPanel.getUiComponent(), null), DataViewComponent.TOP_RIGHT);
    getUpdatableUIObjects().add(requestPanel);
    getUpdatableUIObjects().add(sessionPanel);
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      createRequestView();
      uiComplete = true;
    }
    return viewComponent;
  }

}
