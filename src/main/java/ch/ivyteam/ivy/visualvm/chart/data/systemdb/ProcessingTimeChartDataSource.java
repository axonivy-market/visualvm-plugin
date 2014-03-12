package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.DeltaAttributeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.SerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ProcessingTimeChartDataSource extends XYChartDataSource {

  public ProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport("Max of max",
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANSACTION_MAX_EXE_TIME_IN_MS));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport("Total mean", DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS,
            DatabasePersistency.KEY_TRANSACTION_NUMBER));

    addSerie(new MeanSerieDataSource());
    addSerie("Max", "The maximum time of all transactions that has finished in last poll", SerieStyle.LINE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANSACTION_MAX_EXE_TIME_IN_MS);
    addSerie("Min", "The minimum time of all transactions that has finished in last poll", SerieStyle.LINE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_TRANSACTION_MIN_EXE_TIME_IN_MS);
  }

  class MeanSerieDataSource extends SerieDataSource {

    private final DeltaAttributeDataSource fDeltaTimeTransDataSource;
    private final DeltaAttributeDataSource fDeltaNumberTransDataSource;

    MeanSerieDataSource() {
      this("Mean", 1L, SerieStyle.LINE);
    }

    MeanSerieDataSource(String serie, long scaleFactor, SerieStyle style) {
      super(serie, scaleFactor, style);
      fDeltaTimeTransDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
              DatabasePersistency.NAME, DatabasePersistency.KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS);
      fDeltaNumberTransDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
              DatabasePersistency.NAME, DatabasePersistency.KEY_TRANSACTION_NUMBER);
    }

    @Override
    public void updateQuery(Query query) {
      fDeltaTimeTransDataSource.updateQuery(query);
      fDeltaNumberTransDataSource.updateQuery(query);
    }

    @Override
    public long getValue(QueryResult result) {
      long deltaTime = fDeltaTimeTransDataSource.getValue(result);
      long deltaNumber = fDeltaNumberTransDataSource.getValue(result);
      return (deltaNumber != 0) ? (deltaTime / deltaNumber) : 0;
    }
  }
}
