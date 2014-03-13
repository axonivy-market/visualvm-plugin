package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ProcessingTimeChartDataSource extends XYChartDataSource {
  private static final String MIN_SERIE_TITLE = "Min";
  private static final String MEAN_SERIE_TITLE = "Mean";
  private static final String MAX_SERIE_TITLE = "Max";
  private static final String TOTAL_MEAN_TITLE = "Total mean";
  private static final String MAX_OF_MAX_TITLE = "Max of max";

  public static final String MAX_SERIE_DESC = "The maximum processing time of all system database "
          + "transactions that have finished since the last poll.";
  public static final String MEAN_SERIE_DESC = "The mean processing time of all system database "
          + "transactions that have finished since the last poll.";
  public static final String MIN_SERIE_DESC = "The minimum processing time of all system database "
          + "transactions that have finished since the last poll.";

  public ProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OF_MAX_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_MAX_EXE_DELTA_TIME));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(TOTAL_MEAN_TITLE, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_TOTAL_EXE_TIME, DatabasePersistency.KEY_TRANS_NUMBER));

    addSerie(MAX_SERIE_TITLE, MAX_SERIE_DESC, SerieStyle.FILLED,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addDeltaMeanSerie(MEAN_SERIE_TITLE, MEAN_SERIE_DESC, SerieStyle.FILLED,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_TOTAL_EXE_TIME,
            DatabasePersistency.KEY_TRANS_NUMBER);
    addSerie(MIN_SERIE_TITLE, MIN_SERIE_DESC, SerieStyle.FILLED,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_MIN_EXE_DELTA_TIME);
  }
}