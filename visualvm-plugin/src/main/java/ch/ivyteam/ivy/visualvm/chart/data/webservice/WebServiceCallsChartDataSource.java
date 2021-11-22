package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class WebServiceCallsChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  public static final String CALLS_SERIE_DESCRIPTION = ContentProvider.getFormatted(
          "WebServiceCallSerieDescription");
  public static final String ERRORS_SERIE_DESCRIPTION = ContentProvider.getFormatted(
          "WebServiceErrorSerieDescription");
  public static final String MAX_CALLS_LABEL_DESCRIPTION = ContentProvider.getFormatted(
          "MaxWebServiceCallDescription");
  public static final String MAX_ERRORS_LABEL_DESCRIPTION = ContentProvider.getFormatted(
          "MaxWebServiceErrorDescription");

  public WebServiceCallsChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    MaxDeltaValueChartLabelCalcSupport maxCallsLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(ContentProvider.get("MaxCalls"),
                    getObjectName(), WebService.KEY_CALLS);
    maxCallsLabelSupport.setTooltip(MAX_CALLS_LABEL_DESCRIPTION);
    addLabelCalcSupport(maxCallsLabelSupport);

    MaxDeltaValueChartLabelCalcSupport maxErrorLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(ContentProvider.get("MaxErrors"),
                    getObjectName(), WebService.KEY_ERRORS);
    maxErrorLabelSupport.setTooltip(MAX_ERRORS_LABEL_DESCRIPTION);
    addLabelCalcSupport(maxErrorLabelSupport);

    addDeltaSerie(ContentProvider.get("Calls"), CALLS_SERIE_DESCRIPTION, getObjectName(),
            WebService.KEY_CALLS);
    addDeltaSerie(ContentProvider.get("Errors"), ERRORS_SERIE_DESCRIPTION, getObjectName(),
            WebService.KEY_ERRORS);
  }

}
