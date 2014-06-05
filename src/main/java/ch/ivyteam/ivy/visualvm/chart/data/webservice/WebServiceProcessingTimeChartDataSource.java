package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class WebServiceProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  private static final long SCALED_FACTOR = 1000L;
  public static final String SYSTEM_DATABASE_TRANSACTIONS = "system database transactions";
  public static final String CALLS_TO_THE_WEB_SERVICE = "calls to the web service";
  public static final String MIN_SERIE_DESCRIPTION = DbChartTitleConstant.MIN_SERIE_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);
  public static final String MAX_SERIE_DESCRIPTION = DbChartTitleConstant.MAX_SERIE_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);
  public static final String MEAN_SERIE_DESCRIPTION = DbChartTitleConstant.MEAN_SERIE_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);
  public static final String MAX_OF_MAX_LABEL_DESCRIPTION = DbChartTitleConstant.MAX_OF_MAX_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);
  public static final String TOTAL_MEAN_LABEL_DESCRIPTION = DbChartTitleConstant.TOTAL_MEAN_DESC.
          replace(SYSTEM_DATABASE_TRANSACTIONS, CALLS_TO_THE_WEB_SERVICE);

  public WebServiceProcessingTimeChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    setScaleFactor(SCALED_FACTOR);
    MaxValueChartLabelCalcSupport maxOfMaxLabelSupport
            = new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OF_MAX_TITLE,
                    getObjectName(), WebService.KEY_CALL_MAX_EXE_TIME_DELTA, SCALED_FACTOR);
    maxOfMaxLabelSupport.setTooltip(MAX_OF_MAX_LABEL_DESCRIPTION);
    maxOfMaxLabelSupport.setUnit("ms");
    addLabelCalcSupport(maxOfMaxLabelSupport);

    MeanTotalDeltaValueChartLabelCalcSupport totalMeanLabelSupport
            = new MeanTotalDeltaValueChartLabelCalcSupport(DbChartTitleConstant.TOTAL_MEAN_TITLE,
                    getObjectName(), WebService.KEY_CALL_TOTAL_EXE_TIME,
                    WebService.KEY_CALLS, SCALED_FACTOR);
    totalMeanLabelSupport.setTooltip(TOTAL_MEAN_LABEL_DESCRIPTION);
    totalMeanLabelSupport.setUnit("ms");
    addLabelCalcSupport(totalMeanLabelSupport);

    addSerie(DbChartTitleConstant.MAX_SERIE_TITLE, MAX_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_MAX_EXE_TIME_DELTA);
    addDeltaMeanSerie(DbChartTitleConstant.MEAN_SERIE_TITLE, MEAN_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_TOTAL_EXE_TIME, WebService.KEY_CALLS);
    addSerie(DbChartTitleConstant.MIN_SERIE_TITLE, MIN_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_MIN_EXE_TIME_DELTA);
  }

}
