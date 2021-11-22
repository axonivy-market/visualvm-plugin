package ch.ivyteam.ivy.visualvm.chart.data.license;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ConcurrentUsersChartDataSource extends XYChartDataSource {

  public ConcurrentUsersChartDataSource(DataBeanProvider dataBeanProvider, String yAxisDescription,
          int sessionsLimit) {
    super(dataBeanProvider, null, null, yAxisDescription);
    addFixedSerie(ContentProvider.get("Limit"),
            ContentProvider.getFormatted("LimitConcurrentUsersDescription"),
            sessionsLimit);
    addSerie(ContentProvider.get("Now"), ContentProvider.getFormatted("ConcurrentUsersDescription"),
            SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    MaxValueChartLabelCalcSupport maxConcurrentUsersLabelSupport
            = new MaxValueChartLabelCalcSupport(ContentProvider.get("Max"),
                    IvyJmxConstant.IvyServer.SecurityManager.NAME,
                    IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    maxConcurrentUsersLabelSupport.setTooltip(ContentProvider.getFormatted("MaxConcurrentUsersDescription"));
    addLabelCalcSupport(maxConcurrentUsersLabelSupport);
  }

}
