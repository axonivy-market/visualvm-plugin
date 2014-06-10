package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.request.ErrorChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.request.ProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.request.RequestChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.request.SessionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JPanel;

public class RequestView extends AbstractView {

  private static final String REQUESTS = ContentProvider.get("Requests");
  private static final String ERRORS = ContentProvider.get("Errors");
  private static final String SESSIONS = ContentProvider.get("Sessions");
  private static final String PROCESSING_TIME = ContentProvider.get("ProcessingTime")
          + " [" + ContentProvider.get("MillisecondAbbr") + "]";
  private boolean uiComplete;

  public RequestView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    final ChartsPanel requestPanel = new ChartsPanel(false);
    ChartsPanel sessionPanel = new ChartsPanel(false);

    RequestChartDataSource requestDataSource = new RequestChartDataSource(
            getDataBeanProvider(), null, null, REQUESTS);
    ErrorChartDataSource errorDataSource = new ErrorChartDataSource(
            getDataBeanProvider(), null, null, ERRORS);
    ProcessingTimeChartDataSource processingTimeDataSource = new ProcessingTimeChartDataSource(
            getDataBeanProvider(), null, null, PROCESSING_TIME);
    SessionChartDataSource sessionDataSource = new SessionChartDataSource(
            getDataBeanProvider(), null, null, SESSIONS);

    requestPanel.addChart(requestDataSource, getRequestChartDescription());
    requestPanel.addChart(errorDataSource, getErrorChartDescription());
    requestPanel.addChart(processingTimeDataSource, getProcessTimeChartDescription());
    sessionPanel.addChart(sessionDataSource, getSessionChartDescription());

    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null,
            false), DataViewComponent.TOP_LEFT);
    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(REQUESTS, null, 10,
            requestPanel.getUIComponent(), null), DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(SESSIONS, null, 10,
            sessionPanel.getUIComponent(), null), DataViewComponent.TOP_LEFT);
    registerScheduledUpdate(requestPanel);
    registerScheduledUpdate(sessionPanel);
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

  private String getRequestChartDescription() {
    return ContentProvider.getFormatted("RequestChartDescription");
  }

  private String getErrorChartDescription() {
    return ContentProvider.getFormatted("RequestErrorChartDescription");
  }

  private String getProcessTimeChartDescription() {
    return ContentProvider.getFormatted("RequestProcessingTimeChartDescription");
  }

  private String getSessionChartDescription() {
    return ContentProvider.getFormatted("SessionChartDescription");
  }

}
