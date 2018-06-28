/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.ivyteam.ivy.visualvm.view;

import static ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel.WS_ICON_PATH;
import static ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel.WS_RECORDING_ICON_PATH;


import javax.swing.Icon;

import org.openide.util.ImageUtilities;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceCallsChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceProcessingTimeChartDataSource;


public abstract class AbtractWebServicesView extends ExternalDbWsCommonView {

  private static final String CALLS = ContentProvider.get("Calls");
  private static final String PROCESSING_TIME = ContentProvider.get("ProcessingTime") + " ["
          + ContentProvider.get("MillisecondAbbr") + "]";

  public AbtractWebServicesView(DataBeanProvider dataBeanProvider) {
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

    configDataSources(getWebServiceNamePattern(), callsDataSource, processTimeDataSrc);
    chartPanel.addChart(callsDataSource, generateDescriptionForCallsChart());
    chartPanel.addChart(processTimeDataSrc, generateDescriptionForProcessingTimeChart());
    registerScheduledUpdate(chartPanel);
    return chartPanel;
  }

  protected abstract String getWebServiceNamePattern();

  private String generateDescriptionForCallsChart() {
    return ContentProvider.getFormatted("WebServiceCallChartDescription");
  }

  private String generateDescriptionForProcessingTimeChart() {
    return ContentProvider.getFormatted("WebServiceProcessingTimeChartDescription");
  }
}
