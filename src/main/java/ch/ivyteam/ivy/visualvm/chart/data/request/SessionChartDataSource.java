package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.LatestValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class SessionChartDataSource extends XYChartDataSource {
  public SessionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    ObjectName tomcatManager = collector.getTomcatManagerName(mBeanServerConnection);
    if (tomcatManager != null) {
      addSerie("HTTP", "Number of HTTP sessions", SerieStyle.LINE, tomcatManager,
              IvyJmxConstant.Ivy.Manager.KEY_ACTIVE_SESSION);
      addLabelCalcSupport(new LatestValueChartLabelCalcSupport("HTTP",
              tomcatManager, IvyJmxConstant.Ivy.Manager.KEY_ACTIVE_SESSION));
    }
    addSerie("Ivy", "Number of HTTP sessions that do request against Xpert.ivy core", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_SESSIONS);
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport("Ivy",
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_SESSIONS));

    addSerie("Concurrent Users", "Number of Xpert.ivy users that are currently logged-in", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport("Concurrent Users",
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS));

    addSerie("RD", "Number of Xpert.ivy sessions that use Rich Dialogs", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.RichDialogExecution.NAME,
            IvyJmxConstant.IvyServer.RichDialogExecution.KEY_RD_SESSIONS);
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport("RD",
            IvyJmxConstant.IvyServer.RichDialogExecution.NAME,
            IvyJmxConstant.IvyServer.RichDialogExecution.KEY_RD_SESSIONS));
  }

}
