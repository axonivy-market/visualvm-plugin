package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class WebServiceProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  private static final long SCALED_FACTOR = 1000L;
  public static final String MIN_SERIE_DESCRIPTION = DbChartTitleConstant.MIN_SERIE_DESC.
          replace("system database transactions", "calls to the web service");
  public static final String MAX_SERIE_DESCRIPTION = DbChartTitleConstant.MAX_SERIE_DESC.
          replace("system database transactions", "calls to the web service");
  public static final String MEAN_SERIE_DESCRIPTION = DbChartTitleConstant.MEAN_SERIE_DESC.
          replace("system database transactions", "calls to the web service");

  public WebServiceProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    setScaleFactor(SCALED_FACTOR);
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OF_MAX_TITLE,
            getObjectName(), WebService.KEY_CALL_MAX_EXE_TIME_DELTA, SCALED_FACTOR));
    addLabelCalcSupport(new MeanTotalDeltaValueChartLabelCalcSupport(DbChartTitleConstant.TOTAL_MEAN_TITLE,
            getObjectName(), WebService.KEY_CALL_TOTAL_EXE_TIME, WebService.KEY_CALLS, SCALED_FACTOR));

    addSerie(DbChartTitleConstant.MAX_SERIE_TITLE, MAX_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_MAX_EXE_TIME_DELTA);
    addDeltaMeanSerie(DbChartTitleConstant.MEAN_SERIE_TITLE, MEAN_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_TOTAL_EXE_TIME, WebService.KEY_CALLS);
    addSerie(DbChartTitleConstant.MIN_SERIE_TITLE, MIN_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_MIN_EXE_TIME_DELTA);
  }

}
