package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class WebServiceProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  private static final long SCALED_FACTOR = 1000L;
  public static final String MIN_SERIE_DESCRIPTION = ContentProvider.getFormatted(
          "MinWebServiceProcessingTimeSerieDescription");
  public static final String MAX_SERIE_DESCRIPTION = ContentProvider.getFormatted(
          "MaxWebServiceProcessingTimeSerieDescription");
  public static final String MEAN_SERIE_DESCRIPTION = ContentProvider.getFormatted(
          "AverageWebServiceProcessingTimeSerieDescription");
  public static final String MAX_OF_MAX_LABEL_DESCRIPTION = ContentProvider.getFormatted(
          "TotalMaxWebServiceProcessingTimeDescription");
  public static final String TOTAL_MEAN_LABEL_DESCRIPTION = ContentProvider.getFormatted(
          "TotalAverageWebServiceProcessingTimeDescription");

  public WebServiceProcessingTimeChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    setScaleFactor(SCALED_FACTOR);
    MaxValueChartLabelCalcSupport maxOfMaxLabelSupport
            = new MaxValueChartLabelCalcSupport(ContentProvider.get("MaxOfMax"),
                    getObjectName(), WebService.KEY_CALL_MAX_EXE_TIME_DELTA, SCALED_FACTOR);
    maxOfMaxLabelSupport.setTooltip(MAX_OF_MAX_LABEL_DESCRIPTION);
    maxOfMaxLabelSupport.setUnit(MILLISECOND);
    addLabelCalcSupport(maxOfMaxLabelSupport);

    MeanTotalDeltaValueChartLabelCalcSupport totalMeanLabelSupport
            = new MeanTotalDeltaValueChartLabelCalcSupport(ContentProvider.get("TotalAverage"),
                    getObjectName(), WebService.KEY_CALL_TOTAL_EXE_TIME,
                    WebService.KEY_CALLS, SCALED_FACTOR);
    totalMeanLabelSupport.setTooltip(TOTAL_MEAN_LABEL_DESCRIPTION);
    totalMeanLabelSupport.setUnit(MILLISECOND);
    addLabelCalcSupport(totalMeanLabelSupport);

    addSerie(ContentProvider.get("Max"), MAX_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_MAX_EXE_TIME_DELTA);
    addDeltaMeanSerie(ContentProvider.get("Average"), MEAN_SERIE_DESCRIPTION,
            SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_TOTAL_EXE_TIME, WebService.KEY_CALLS);
    addSerie(ContentProvider.get("Min"), MIN_SERIE_DESCRIPTION, SerieStyle.LINE_FILLED,
            getObjectName(), WebService.KEY_CALL_MIN_EXE_TIME_DELTA);
  }

}
