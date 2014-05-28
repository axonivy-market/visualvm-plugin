package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ProcessingTimeChartDataSource extends XYChartDataSource {
  private static final long SCALED_FACTOR = 1000L;

  public ProcessingTimeChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    setScaleFactor(SCALED_FACTOR);

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OF_MAX_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_MAX_EXE_DELTA_TIME, SCALED_FACTOR));
    addLabelCalcSupport(new MeanTotalDeltaValueChartLabelCalcSupport(
            DbChartTitleConstant.TOTAL_MEAN_TITLE, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_TOTAL_EXE_TIME, DatabasePersistency.KEY_TRANS_NUMBER,
            SCALED_FACTOR));

    addSerie(DbChartTitleConstant.MAX_SERIE_TITLE,
            DbChartTitleConstant.MAX_SERIE_DESC,
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addDeltaMeanSerie(DbChartTitleConstant.MEAN_SERIE_TITLE,
            DbChartTitleConstant.MEAN_SERIE_DESC,
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_TOTAL_EXE_TIME,
            DatabasePersistency.KEY_TRANS_NUMBER);
    addSerie(DbChartTitleConstant.MIN_SERIE_TITLE,
            DbChartTitleConstant.MIN_SERIE_DESC,
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_MIN_EXE_DELTA_TIME);
  }

}
