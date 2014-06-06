package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class SessionChartDataSource extends XYChartDataSource {
  public SessionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    initDisplayForHttpSessions();
    initDisplayForIvySessions();
    initDisplayForConcurrentUsers();
    initDisplayForRDSessions();
  }

  private void initDisplayForHttpSessions() {
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    ObjectName tomcatManager = collector.getTomcatManagerName(mBeanServerConnection);
    if (tomcatManager != null) {
      addSerie("HTTP", "Number of HTTP sessions", SerieStyle.LINE, tomcatManager,
              IvyJmxConstant.Ivy.Manager.KEY_ACTIVE_SESSION);
      MaxValueChartLabelCalcSupport maxHttpSessionLabelSupport
              = new MaxValueChartLabelCalcSupport("Max HTTP",
                      tomcatManager, IvyJmxConstant.Ivy.Manager.KEY_ACTIVE_SESSION);
      maxHttpSessionLabelSupport.setTooltip("The maximum number of HTTP sessions."
              + " Measured since the last start of VisualVM");
      addLabelCalcSupport(maxHttpSessionLabelSupport);
    }
  }

  private void initDisplayForIvySessions() {
    addSerie("Ivy", "Number of HTTP sessions that run requests against the Xpert.ivy core", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_SESSIONS);
    MaxValueChartLabelCalcSupport maxIvySessionLabelSupport
            = new MaxValueChartLabelCalcSupport("Max Ivy",
                    IvyJmxConstant.IvyServer.SecurityManager.NAME,
                    IvyJmxConstant.IvyServer.SecurityManager.KEY_SESSIONS);
    maxIvySessionLabelSupport.setTooltip(
            "The maximum number of HTTP sessions that run requests against the Xpert.ivy core. "
            + "Measured since the last start of VisualVM");
    addLabelCalcSupport(maxIvySessionLabelSupport);
  }

  private void initDisplayForConcurrentUsers() {
    addSerie("Concurrent Users", "Number of Xpert.ivy users that are currently logged-in", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    MaxValueChartLabelCalcSupport maxConUsersLabelSupp
            = new MaxValueChartLabelCalcSupport("Max Concurrent Users",
                    IvyJmxConstant.IvyServer.SecurityManager.NAME,
                    IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    maxConUsersLabelSupp.setTooltip("The maximum number of users that were logged-in in a polling interval. "
            + "Measured since the last start of VisualVM");
    addLabelCalcSupport(maxConUsersLabelSupp);
  }

  private void initDisplayForRDSessions() {
    addSerie("Rich Dialog", "Number of Xpert.ivy sessions that use Rich Dialogs", SerieStyle.LINE,
            IvyJmxConstant.IvyServer.RichDialogExecution.NAME,
            IvyJmxConstant.IvyServer.RichDialogExecution.KEY_RD_SESSIONS);
    MaxValueChartLabelCalcSupport maxRDSessionLabelSupport
            = new MaxValueChartLabelCalcSupport("Max Rich Dialog",
                    IvyJmxConstant.IvyServer.RichDialogExecution.NAME,
                    IvyJmxConstant.IvyServer.RichDialogExecution.KEY_RD_SESSIONS);
    maxRDSessionLabelSupport.setTooltip(
            "The maximum number of Xpert.ivy sessions that were using Rich Dialogs. "
            + "Measured since the last start of VisualVM");
    addLabelCalcSupport(maxRDSessionLabelSupport);
  }

}
