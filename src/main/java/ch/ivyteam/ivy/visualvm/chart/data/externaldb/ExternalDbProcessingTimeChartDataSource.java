package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DeltaAttributeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MeanSerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  public static final String MIN_SERIE_TITLE = "Min";
  public static final String MAX_SERIE_TITLE = "Max";
  public static final String MEAN_SERIE_TITLE = "Mean";
  public static final String TOTAL_MEAN_TITLE = "Total mean";
  public static final String MAX_OF_MAX_TITLE = "Max of max";

  public static final String MAX_SERIE_DESC = "The maximum processing time of external database "
          + "transactions that have finished since the last poll.";
  public static final String MEAN_SERIE_DESC = "The mean processing time of external database "
          + "transactions that have finished since the last poll.";
  public static final String MIN_SERIE_DESC = "The minimum processing time of external database "
          + "transactions that have finished since the last poll.";

  public ExternalDbProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OF_MAX_TITLE,
            getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(TOTAL_MEAN_TITLE, getObjectName(),
            ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER));

    DeltaAttributeDataSource totalProcessingTimeDataSource = new DeltaAttributeDataSource("",
            1L, SerieStyle.LINE, getObjectName(), ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME);
    DeltaAttributeDataSource numTransDataSource = new DeltaAttributeDataSource("", 1L,
            SerieStyle.LINE, getObjectName(), ExternalDatabase.KEY_TRANS_NUMBER);
    MeanSerieDataSource meanProcessingTimeDataSource = new MeanSerieDataSource(MEAN_SERIE_TITLE,
            SerieStyle.LINE,
            totalProcessingTimeDataSource,
            numTransDataSource);
    meanProcessingTimeDataSource.setDescription(MEAN_SERIE_DESC);

    addSerie(MAX_SERIE_TITLE, MAX_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addDeltaMeanSerie(MEAN_SERIE_TITLE, MEAN_SERIE_DESC, getObjectName(),
            ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER);
    addSerie(MIN_SERIE_TITLE, MIN_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_TRANS_MIN_EXE_DELTA_TIME);
  }

}
