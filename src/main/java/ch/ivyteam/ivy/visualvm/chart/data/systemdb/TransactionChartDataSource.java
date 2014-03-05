package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class TransactionChartDataSource extends XYChartDataSource {
  private static final String TRANSACTION = "Transaction";

  public TransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    addSerie(TRANSACTION, SerieStyle.LINE, IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
            IvyJmxConstant.IvyServer.DatabasePersistency.KEY_TRANSACTION_NUMBER);
  }
}