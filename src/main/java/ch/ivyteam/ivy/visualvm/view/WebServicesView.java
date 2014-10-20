package ch.ivyteam.ivy.visualvm.view;

import static ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel.WS_ICON_PATH;
import static ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel.WS_RECORDING_ICON_PATH;

import java.util.Map;
import java.util.Set;

import javax.swing.Icon;

import org.openide.util.ImageUtilities;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceCallsChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;

import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

public class WebServicesView extends ExternalDbWsCommonView {
  private static final String CALLS = ContentProvider.get("Calls");
  private static final String PROCESSING_TIME = ContentProvider.get("ProcessingTime") + " ["
          + ContentProvider.get("MillisecondAbbr") + "]";

  public WebServicesView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    setUIChartsPanel(new ExternalDbWsCommonPanel(this));
    getUIChartsPanel().setWsIcon((Icon) ImageUtilities.loadImage(WS_ICON_PATH, true));
    getUIChartsPanel().setRecordingIcon((Icon) ImageUtilities.loadImage(WS_RECORDING_ICON_PATH, true));
  }

  @Override
  protected ChartsPanel createChartPanel(String title) {
    ChartsPanel chartPanel;
    if (title != null) {
      chartPanel = new ChartsPanel(false, title);
    } else {
      chartPanel = new ChartsPanel(false);
    }
    WebServiceCallsChartDataSource callsDataSource = new WebServiceCallsChartDataSource(
            getDataBeanProvider(), null, null, CALLS);
    WebServiceProcessingTimeChartDataSource processTimeDataSrc = new WebServiceProcessingTimeChartDataSource(
            getDataBeanProvider(), null, null, PROCESSING_TIME);

    configDataSources(IvyJmxConstant.IvyServer.WebService.NAME_PATTERN, callsDataSource,
            processTimeDataSrc);
    chartPanel.addChart(callsDataSource, generateDescriptionForCallsChart());
    chartPanel.addChart(processTimeDataSrc, generateDescriptionForProcessingTimeChart());
    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.add(getUIChartsPanel());
    Map<String, Map<String, Set<String>>> appEnvWs = DataUtils.getWebServicesConfigs(getDataBeanProvider()
            .getMBeanServerConnection());
    getUIChartsPanel().setTreeData(appEnvWs);
    return viewComponent;
  }

  private String generateDescriptionForCallsChart() {
    return ContentProvider.getFormatted("WebServiceCallChartDescription");
  }

  private String generateDescriptionForProcessingTimeChart() {
    return ContentProvider.getFormatted("WebServiceProcessingTimeChartDescription");
  }
}
