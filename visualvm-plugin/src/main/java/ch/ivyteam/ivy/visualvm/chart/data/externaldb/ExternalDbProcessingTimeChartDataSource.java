package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ExternalDbProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  private static final long SCALED_FACTOR = 1000L;
  public static final String MAX_SERIE_DESC = ContentProvider.getFormatted(
          "MaxExtDbProcessingTimeSerieDescription");
  public static final String MEAN_SERIE_DESC = ContentProvider.getFormatted(
          "AverageExtDbProcessingSerieTimeDescription");
  public static final String MIN_SERIE_DESC = ContentProvider.getFormatted(
          "MinExtDbProcessingTimeSerieDescription");
  public static final String TOTAL_MEAN_LABEL_DESC = ContentProvider.getFormatted(
          "TotalAverageExtDbProcessingTimeDescription");
  public static final String MAX_OF_MAX_LABEL_DESC = ContentProvider.getFormatted(
          "TotalMaxExtDbProcessingTimeDescription");

  public ExternalDbProcessingTimeChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    setScaleFactor(SCALED_FACTOR);

    MaxValueChartLabelCalcSupport maxOfMaxLabelSupport
            = new MaxValueChartLabelCalcSupport(ContentProvider.get("MaxOfMax"),
                    getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME, SCALED_FACTOR);
    maxOfMaxLabelSupport.setTooltip(MAX_OF_MAX_LABEL_DESC);
    maxOfMaxLabelSupport.setUnit(MILLISECOND);
    addLabelCalcSupport(maxOfMaxLabelSupport);

    MeanTotalDeltaValueChartLabelCalcSupport totalMeanLabelSupport
            = new MeanTotalDeltaValueChartLabelCalcSupport(ContentProvider.get("TotalAverage"),
                    getObjectName(), ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME,
                    ExternalDatabase.KEY_TRANS_NUMBER, SCALED_FACTOR);
    totalMeanLabelSupport.setTooltip(TOTAL_MEAN_LABEL_DESC);
    totalMeanLabelSupport.setUnit(MILLISECOND);
    addLabelCalcSupport(totalMeanLabelSupport);

    addSerie(ContentProvider.get("Max"), MAX_SERIE_DESC, SerieStyle.LINE_FILLED,
            getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addDeltaMeanSerie(ContentProvider.get("Average"), MEAN_SERIE_DESC, SerieStyle.LINE_FILLED,
            getObjectName(), ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER);
    addSerie(ContentProvider.get("Min"), MIN_SERIE_DESC, SerieStyle.LINE_FILLED,
            getObjectName(), ExternalDatabase.KEY_TRANS_MIN_EXE_DELTA_TIME);
  }

}
