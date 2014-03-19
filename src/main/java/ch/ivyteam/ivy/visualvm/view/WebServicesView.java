/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceCallsChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import static ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel.WS_ICON_PATH;
import static ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel.WS_RECORDING_ICON_PATH;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author htnam
 */
public class WebServicesView extends ExternalDbWsCommonView {

  public WebServicesView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fUIPanel = new ExternalDbWsCommonPanel(this);
    fUIPanel.setWsIcon((Icon) ImageUtilities.loadImage(WS_ICON_PATH, true));
    fUIPanel.setRecordingIcon((Icon) ImageUtilities.loadImage(WS_RECORDING_ICON_PATH, true));
  }

  @Override
  protected ChartsPanel createChartPanel() {
    ChartsPanel chartPanel = new ChartsPanel(false);
    WebServiceCallsChartDataSource callsDataSource = new WebServiceCallsChartDataSource(
            getDataBeanProvider(), null, null, "Calls");
    WebServiceProcessingTimeChartDataSource processTimeDataSource
            = new WebServiceProcessingTimeChartDataSource(getDataBeanProvider(), null, null,
                    "Processing Time [ms]");

    configDataSources(IvyJmxConstant.IvyServer.WebService.NAME_PATTERN,
            callsDataSource, processTimeDataSource);
    chartPanel.addChart(callsDataSource, generateDescriptionForCallsChart());
    chartPanel.addChart(processTimeDataSource, generateDescriptionForProcessingTimeChart());
    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(fUIPanel);
    Map<String, Map<String, Set<String>>> appEnvWs = DataUtils.getWebServicesConfigs(getDataBeanProvider()
            .getMBeanServerConnection());
    fUIPanel.setTreeData(appEnvWs);
    return viewComponent;
  }

  private String generateDescriptionForCallsChart() {
    String callDesc = "The chart shows the number of calls to the web service and "
            + "the number of them that were erroneous.";
    StringBuilder builder = new StringBuilder();
    builder.append("<html>").append(callDesc).append(BR).append(BR);
    builder.append("<b>Calls:</b> ").append(WebServiceCallsChartDataSource.CALLS_SERIE_DESCRIPTION)
            .append(BR);
    builder.append("<b>Errors:</b> ").append(WebServiceCallsChartDataSource.ERRORS_SERIE_DESCRIPTION);
    builder.append("</html>");
    return builder.toString();
  }

  private String generateDescriptionForProcessingTimeChart() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("The chart shows the maximum, the mean and the minimum time needed to execute ");
    builder.append("web service calls since the last poll.").append(BR).append(BR);
    builder.append("<b>Max</b>: ").append(WebServiceProcessingTimeChartDataSource.MAX_SERIE_DESCRIPTION)
            .append(BR);
    builder.append("<b>Mean</b>: ").append(WebServiceProcessingTimeChartDataSource.MEAN_SERIE_DESCRIPTION)
            .append(BR);
    builder.append("<b>Min</b>: ").append(WebServiceProcessingTimeChartDataSource.MIN_SERIE_DESCRIPTION);
    builder.append("</html>");
    return builder.toString();
  }

}
