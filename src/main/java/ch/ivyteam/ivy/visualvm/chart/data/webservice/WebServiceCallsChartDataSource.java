package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.LatestValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class WebServiceCallsChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  private static final String CALLS_SERIE_DESCRIPTION = "The number of calls to the web service that have "
          + "finished since the last poll";
  private static final String ERRORS_SERIE_DESCRIPTION = "The number of calls to the web service that have "
          + "finished since the last poll and were erroneous";
  private static final String CALLS_SERIE_TITLE = "Calls";
  private static final String ERRORS_SERIE_TITLE = "Errors";
  private static final String TOTAL_CALLS = "Total calls";
  private static final String TOTAL_ERRORS = "Total errors";

  public WebServiceCallsChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport(TOTAL_CALLS, getObjectName(),
            WebService.KEY_CALLS));
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport(TOTAL_ERRORS, getObjectName(),
            WebService.KEY_ERRORS));

    addDeltaSerie(CALLS_SERIE_TITLE,
            CALLS_SERIE_DESCRIPTION, getObjectName(),
            WebService.KEY_CALLS);
    addDeltaSerie(
            ERRORS_SERIE_TITLE,
            ERRORS_SERIE_DESCRIPTION,
            getObjectName(), WebService.KEY_ERRORS);
  }

}
