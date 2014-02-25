package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class MSessionChartDataSource extends MChartDataSource {
  public MSessionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    ObjectName tomcatManager = DataUtils.getTomcatManagerName(mBeanServerConnection);
    if (tomcatManager != null) {
      addSerie("Http", SerieStyle.LINE, tomcatManager, "sessionCounter");
    }
    addSerie("Ivy", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            "sessions");
    addSerie("Licensed", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    addSerie("RD", SerieStyle.LINE, IvyJmxConstant.IvyServer.RichDialogExecution.NAME,
            IvyJmxConstant.IvyServer.RichDialogExecution.KEY_RD_SESSIONS);
  }

}
