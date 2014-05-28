package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class WebServiceCallsChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  public static final String CALLS_SERIE_DESCRIPTION = DbChartTitleConstant.TRANSACTION_SERIE_DESC.
          replace("system database transactions", "calls to the web service");
  public static final String ERRORS_SERIE_DESCRIPTION = DbChartTitleConstant.TRANSACTION_ERROR_SERIE_DESC.
          replace("system database transactions", "calls to the web service");

  public WebServiceCallsChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_CALLS_TITLE,
            getObjectName(), WebService.KEY_CALLS));
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_ERROR_TITLE,
            getObjectName(), WebService.KEY_ERRORS));

    addDeltaSerie(DbChartTitleConstant.CALLS_SERIE_TITLE, CALLS_SERIE_DESCRIPTION, getObjectName(),
            WebService.KEY_CALLS);
    addDeltaSerie(DbChartTitleConstant.ERROR_TITLE, ERRORS_SERIE_DESCRIPTION, getObjectName(),
            WebService.KEY_ERRORS);
  }

}
