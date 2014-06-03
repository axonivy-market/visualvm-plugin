package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.request.ErrorChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.request.ProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.request.RequestChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.request.SessionChartDataSource;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JPanel;

public class RequestView extends AbstractView {

  public static final String END_LINE = "<br/>";
  private static final String REQUESTS = "Requests";
  private static final String ERRORS = "Errors";
  private static final String SESSIONS = "Sessions";
  private static final String TIME_MS = "Processing Time [ms]";
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
            getDataBeanProvider(), null, null, TIME_MS);
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
    StringBuilder sb = new StringBuilder(
            "The chart shows the number of new requests served by each connector since the last polling");
    return sb.toString();
  }

  private String getErrorChartDescription() {
    StringBuilder sb = new StringBuilder("<html>");
    sb.append("The chart shows the number of new errors in requests ");
    sb.append("served by each connector since the last polling,");
    sb.append(END_LINE);
    sb.append(" i.e. number of requests with a HTTP response code between 400 and 599");
    return sb.toString();
  }

  private String getProcessTimeChartDescription() {
    StringBuilder sb = new StringBuilder("The chart shows the mean processing time for new requests ");
    sb.append("served by each connector since the last polling");
    return sb.toString();
  }

  private String getSessionChartDescription() {
    StringBuilder sb = new StringBuilder("<html>");
    sb.append("The chart shows the infomation about open sessions of Xpert.ivy server");
    sb.append(END_LINE);
    sb.append(END_LINE);
    sb.append("<b>HTTP:</b> Number of HTTP sessions");
    sb.append(END_LINE);
    sb.append("<b>Ivy:</b> Number of HTTP sessions that do request against Xpert.ivy core");
    sb.append(END_LINE);
    sb.append("<b>Concurrent users:</b> ");
    sb.append("Number of Xpert.ivy users that are currently logged-in");
    sb.append(END_LINE);
    sb.append("<b>RD:</b> Number of Xpert.ivy sessions that use Rich Dialogs");
    sb.append("</html>");
    return sb.toString();
  }

}
