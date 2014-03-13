package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class TransactionChartDataSource extends XYChartDataSource {

  private static final String TOTAL_TRANSACTION = "Max transactions";
  private static final String MAX_ERROR = "Max errors";
  private static final String TRANSACTION = "Transactions";
  private static final String ERROR = "Errors";
  public static final String ERROR_SERIE_DESC = "The number of system database transactions that have "
          + "finished since the last poll and were erroneous.";
  public static final String TRANSACTION_SERIE_DESC = "The number of system database transactions that have "
          + "finished since the last poll.";

  public TransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(TOTAL_TRANSACTION, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_NUMBER));
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(MAX_ERROR, DatabasePersistency.NAME,
            DatabasePersistency.KEY_ERROR));
    addDeltaSerie(TRANSACTION, TRANSACTION_SERIE_DESC,
            SerieStyle.FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_NUMBER);
    addDeltaSerie(ERROR, ERROR_SERIE_DESC,
            SerieStyle.FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_ERROR);
  }
}
