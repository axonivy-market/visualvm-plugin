package ch.ivyteam.ivy.visualvm.chart.data.license;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ConcurrentUsersChartDataSource extends XYChartDataSource {

  public ConcurrentUsersChartDataSource(DataBeanProvider dataBeanProvider, String yAxisDescription,
          int sessionsLimit) {
    super(dataBeanProvider, null, null, yAxisDescription);
    addFixedSerie("Limit", "The maximum number of the concurrent users restricted by the license",
            sessionsLimit);
    addSerie("Now", "The current number of the concurrent users", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport("Max Now",
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS));
  }

}
