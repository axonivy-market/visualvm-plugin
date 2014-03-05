package ch.ivyteam.ivy.visualvm.chart.data.license.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ProcessingTimeChartDataSource extends XYChartDataSource {

  public ProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    addSerie("Max", SerieStyle.LINE, IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
            IvyJmxConstant.IvyServer.DatabasePersistency.KEY_TRANSACTION_MAX_EXE_TIME_IN_MS);
    addDeltaSerie("Max delta", IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
            IvyJmxConstant.IvyServer.DatabasePersistency.KEY_TRANSACTION_MAX_EXE_DELTA_TIME_IN_MS);
    addDeltaSerie("Min delta", IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
            IvyJmxConstant.IvyServer.DatabasePersistency.KEY_TRANSACTION_MIN_EXE_DELTA_TIME_IN_MS);
    addSerie("Min", SerieStyle.LINE, IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
            IvyJmxConstant.IvyServer.DatabasePersistency.KEY_TRANSACTION_MIN_EXE_TIME_IN_MS);
  }
}
