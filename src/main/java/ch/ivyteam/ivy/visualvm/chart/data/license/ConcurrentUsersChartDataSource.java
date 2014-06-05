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
    addFixedSerie("Limit", "The maximum number of concurrent users restricted by the license",
            sessionsLimit);
    addSerie("Now", "The number of users that are currently logged-in.", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    MaxValueChartLabelCalcSupport maxConcurrentUsersLabelSupport
            = new MaxValueChartLabelCalcSupport("Max",
                    IvyJmxConstant.IvyServer.SecurityManager.NAME,
                    IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    maxConcurrentUsersLabelSupport.setTooltip("The maximum number of users that were logged-in "
            + "in a polling interval. Measured since the last start of VisualVM");
    addLabelCalcSupport(maxConcurrentUsersLabelSupport);
  }

}
