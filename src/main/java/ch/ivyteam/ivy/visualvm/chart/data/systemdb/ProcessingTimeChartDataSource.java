package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ProcessingTimeChartDataSource extends XYChartDataSource {

  public ProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OF_MAX_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_MAX_EXE_DELTA_TIME));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(DbChartTitleConstant.TOTAL_MEAN_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_TOTAL_EXE_TIME,
            DatabasePersistency.KEY_TRANS_NUMBER));

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
