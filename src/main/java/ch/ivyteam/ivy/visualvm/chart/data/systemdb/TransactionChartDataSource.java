package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class TransactionChartDataSource extends XYChartDataSource {

  private static final String TOTAL_TRANSACTION = "Max transaction";
  private static final String MAX_ERROR = "Max error";
  private static final String TRANSACTION = "Transactions";
  private static final String ERROR = "Errors";

  public TransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(TOTAL_TRANSACTION, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANSACTION_NUMBER));
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_ERROR, DatabasePersistency.NAME,
            DatabasePersistency.KEY_ERROR));
    addDeltaSerie(TRANSACTION,
            "The number of system database transactions that have finished since the last poll",
            SerieStyle.LINE, DatabasePersistency.NAME, DatabasePersistency.KEY_TRANSACTION_NUMBER);
    addSerie(ERROR, "The number of system database transactions that finished since the last poll"
            + " and were erroneous.",
            SerieStyle.LINE, DatabasePersistency.NAME, DatabasePersistency.KEY_ERROR);
  }
}
