package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class TransactionChartDataSource extends XYChartDataSource {

  public TransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_TRANSACTION_TITLE, DatabasePersistency.NAME,
            DatabasePersistency.KEY_TRANS_NUMBER));
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_ERROR_TITLE, DatabasePersistency.NAME,
            DatabasePersistency.KEY_ERROR));
    addDeltaSerie(DbChartTitleConstant.TRANSACTION_TITLE, DbChartTitleConstant.TRANSACTION_SERIE_DESC,
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_TRANS_NUMBER);
    addDeltaSerie(DbChartTitleConstant.ERROR_TITLE, DbChartTitleConstant.TRANSACTION_ERROR_SERIE_DESC,
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_ERROR);
  }
}
