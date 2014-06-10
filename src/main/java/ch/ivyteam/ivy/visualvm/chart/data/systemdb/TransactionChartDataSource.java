package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class TransactionChartDataSource extends XYChartDataSource {

  public TransactionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

    MaxDeltaValueChartLabelCalcSupport maxTransactionLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(ContentProvider.get("MaxTransactions"),
                    DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_NUMBER);
    maxTransactionLabelSupport.setTooltip(ContentProvider.getFormatted("MaxSysDbTransactionDescription"));
    addLabelCalcSupport(maxTransactionLabelSupport);

    MaxDeltaValueChartLabelCalcSupport maxErrorLabelSupport = new MaxDeltaValueChartLabelCalcSupport(
            ContentProvider.get("MaxErrors"), DatabasePersistency.NAME, DatabasePersistency.KEY_ERROR);
    maxErrorLabelSupport.setTooltip(ContentProvider.getFormatted("MaxSysDbErrorTransactionDescription"));
    addLabelCalcSupport(maxErrorLabelSupport);

    addDeltaSerie(ContentProvider.get("Transactions"),
            ContentProvider.getFormatted("SysDbTransactionSerieDescription"),
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_NUMBER);
    addDeltaSerie(ContentProvider.get("Errors"),
            ContentProvider.getFormatted("SysDbErrorTransactionSerieDescription"),
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_ERROR);
  }

}
