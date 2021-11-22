package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
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

    MaxValueChartLabelCalcSupport maxOfMaxLabelSupport = new MaxValueChartLabelCalcSupport(
            ContentProvider.get("MaxOfMax"), DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_MAX_EXE_DELTA_TIME, SCALED_FACTOR);
    maxOfMaxLabelSupport.setTooltip(ContentProvider.getFormatted("TotalMaxSysDbTransactionDescription"));
    maxOfMaxLabelSupport.setUnit(MILLISECOND);
    addLabelCalcSupport(maxOfMaxLabelSupport);

    MeanTotalDeltaValueChartLabelCalcSupport totalMeanLabelSupport
            = new MeanTotalDeltaValueChartLabelCalcSupport(ContentProvider.get("TotalAverage"),
                    DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_TOTAL_EXE_TIME,
                    DatabasePersistency.KEY_TRANS_NUMBER, SCALED_FACTOR);
    totalMeanLabelSupport.setTooltip(ContentProvider.getFormatted("TotalAverageSysDbTransactionDescription"));
    totalMeanLabelSupport.setUnit(MILLISECOND);
    addLabelCalcSupport(totalMeanLabelSupport);

    addSerie(ContentProvider.get("Max"),
            ContentProvider.getFormatted("MaxSysDbProcessingTimeSerieDescription"),
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addDeltaMeanSerie(ContentProvider.get("Average"),
            ContentProvider.getFormatted("AverageSysDbProcessingTimeSerieDescription"),
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_TOTAL_EXE_TIME,
            DatabasePersistency.KEY_TRANS_NUMBER);
    addSerie(ContentProvider.get("Min"),
            ContentProvider.getFormatted("MinSysDbProcessingTimeSerieDescription"),
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_MIN_EXE_DELTA_TIME);
  }

}
