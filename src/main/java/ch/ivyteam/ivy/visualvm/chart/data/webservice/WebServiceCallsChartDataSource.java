package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class WebServiceCallsChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  public static final String SYSTEM_DATABASE_TRANSACTIONS = "system database transactions";
  public static final String CALLS_TO_THE_WEB_SERVICE = "calls to the web service";

  public static final String CALLS_SERIE_DESCRIPTION = DbChartTitleConstant.TRANSACTION_SERIE_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);
  public static final String ERRORS_SERIE_DESCRIPTION = DbChartTitleConstant.TRANSACTION_ERROR_SERIE_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);
  public static final String MAX_CALLS_LABEL_DESCRIPTION = DbChartTitleConstant.MAX_TRANSACTION_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);
  public static final String MAX_ERRORS_LABEL_DESCRIPTION = DbChartTitleConstant.MAX_ERROR_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);

  public WebServiceCallsChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    MaxDeltaValueChartLabelCalcSupport maxCallsLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_CALLS_TITLE,
                    getObjectName(), WebService.KEY_CALLS);
    maxCallsLabelSupport.setTooltip(MAX_CALLS_LABEL_DESCRIPTION);
    addLabelCalcSupport(maxCallsLabelSupport);

    MaxDeltaValueChartLabelCalcSupport maxErrorLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_ERROR_TITLE,
                    getObjectName(), WebService.KEY_ERRORS);
    maxErrorLabelSupport.setTooltip(MAX_ERRORS_LABEL_DESCRIPTION);
    addLabelCalcSupport(maxErrorLabelSupport);

    addDeltaSerie(DbChartTitleConstant.CALLS_SERIE_TITLE, CALLS_SERIE_DESCRIPTION, getObjectName(),
            WebService.KEY_CALLS);
    addDeltaSerie(DbChartTitleConstant.ERROR_TITLE, ERRORS_SERIE_DESCRIPTION, getObjectName(),
            WebService.KEY_ERRORS);
  }

}
