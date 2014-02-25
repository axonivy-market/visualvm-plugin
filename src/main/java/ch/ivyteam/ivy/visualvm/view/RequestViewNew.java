package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.MErrorChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MProcessTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MRequestChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MSessionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

public class RequestViewNew extends AbstractView {

  private static final String REQUESTS = "Requests";
  private static final String ERRORS = "Errors";
  private static final String SESSIONS = "Sessions";
  private boolean uiComplete;

  public RequestViewNew(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    ChartsPanel requestChart = new ChartsPanel();
    ChartsPanel errorChart = new ChartsPanel();
    ChartsPanel processingTimeChart = new ChartsPanel();
    ChartsPanel sessionChart = new ChartsPanel();

    MRequestChartDataSource requestDataSource = new MRequestChartDataSource(
            getDataBeanProvider(), REQUESTS, null, REQUESTS);
    MErrorChartDataSource errorDataSource = new MErrorChartDataSource(
            getDataBeanProvider(), ERRORS, null, ERRORS);
    MProcessTimeChartDataSource processingTimeDataSource = new MProcessTimeChartDataSource(
            getDataBeanProvider(), "Processing Time", null, "Time [ms]");
    MSessionChartDataSource sessionDataSource = new MSessionChartDataSource(
            getDataBeanProvider(), SESSIONS, null, SESSIONS);

    requestChart.addChart(requestDataSource);
    errorChart.addChart(errorDataSource);
    processingTimeChart.addChart(processingTimeDataSource);
    sessionChart.addChart(sessionDataSource);

    getUpdatableUIObjects().add(requestChart);
    getUpdatableUIObjects().add(errorChart);
    getUpdatableUIObjects().add(processingTimeChart);
    getUpdatableUIObjects().add(sessionChart);

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Requests", null, 10,
            requestChart.getUiComponent(), null), DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Errors", null, 10, errorChart.
            getUiComponent(), null), DataViewComponent.TOP_RIGHT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Processing Time", null, 10,
            processingTimeChart.getUiComponent(), null), DataViewComponent.BOTTOM_LEFT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(SESSIONS, null, 10,
            sessionChart.getUiComponent(), null), DataViewComponent.BOTTOM_RIGHT);
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
