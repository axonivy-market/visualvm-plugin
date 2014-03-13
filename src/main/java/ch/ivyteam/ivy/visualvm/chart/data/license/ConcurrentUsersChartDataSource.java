package ch.ivyteam.ivy.visualvm.chart.data.license;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ConcurrentUsersChartDataSource extends XYChartDataSource {

  public ConcurrentUsersChartDataSource(IDataBeanProvider dataBeanProvider, String yAxisDescription,
          int sessionsLimit) {
    super(dataBeanProvider, null, null, yAxisDescription);
    addFixedSerie("Limit", null, sessionsLimit);
    addSerie("Now", null, SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport("Max Now",
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS));
  }

}
