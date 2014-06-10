package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import static ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource.MAX_OF;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.text.MessageFormat;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class SessionChartDataSource extends XYChartDataSource {
  public static final String HTTP = ContentProvider.get("Http");
  public static final String IVY = ContentProvider.get("Ivy");
  public static final String CONCURRENT_USERS = ContentProvider.get("ConcurrentUsers");
  public static final String RICH_DIALOG = ContentProvider.get("RichDialog");

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
      addSerie(HTTP, ContentProvider.getFormatted("HttpSessionSerieDescription"), SerieStyle.LINE,
              tomcatManager,
              IvyJmxConstant.Ivy.Manager.KEY_ACTIVE_SESSION);
      MaxValueChartLabelCalcSupport maxHttpSessionLabelSupport
              = new MaxValueChartLabelCalcSupport(MessageFormat.format(MAX_OF, HTTP),
                      tomcatManager, IvyJmxConstant.Ivy.Manager.KEY_ACTIVE_SESSION);
      maxHttpSessionLabelSupport.setTooltip(ContentProvider.getFormatted("MaxHttpSessionDescription"));
      addLabelCalcSupport(maxHttpSessionLabelSupport);
    }
  }

  private void initDisplayForIvySessions() {
    addSerie(IVY, ContentProvider.getFormatted("IvySessionSerieDescription"), SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_SESSIONS);
    MaxValueChartLabelCalcSupport maxIvySessionLabelSupport
            = new MaxValueChartLabelCalcSupport(MessageFormat.format(MAX_OF, IVY),
                    IvyJmxConstant.IvyServer.SecurityManager.NAME,
                    IvyJmxConstant.IvyServer.SecurityManager.KEY_SESSIONS);
    maxIvySessionLabelSupport.setTooltip(ContentProvider.getFormatted("MaxIvySessionDescription"));
    addLabelCalcSupport(maxIvySessionLabelSupport);
  }

  private void initDisplayForConcurrentUsers() {
    addSerie(CONCURRENT_USERS, ContentProvider.getFormatted("ConcurrentUsersDescription"), SerieStyle.LINE,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    MaxValueChartLabelCalcSupport maxConUsersLabelSupp
            = new MaxValueChartLabelCalcSupport(MessageFormat.format(MAX_OF, CONCURRENT_USERS),
                    IvyJmxConstant.IvyServer.SecurityManager.NAME,
                    IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    maxConUsersLabelSupp.setTooltip(ContentProvider.getFormatted("MaxConcurrentUsersDescription"));
    addLabelCalcSupport(maxConUsersLabelSupp);
  }

  private void initDisplayForRDSessions() {
    addSerie(RICH_DIALOG, ContentProvider.getFormatted("RichDialogSessionSerieDescription"), SerieStyle.LINE,
            IvyJmxConstant.IvyServer.RichDialogExecution.NAME,
            IvyJmxConstant.IvyServer.RichDialogExecution.KEY_RD_SESSIONS);
    MaxValueChartLabelCalcSupport maxRDSessionLabelSupport
            = new MaxValueChartLabelCalcSupport(MessageFormat.format(MAX_OF, RICH_DIALOG),
                    IvyJmxConstant.IvyServer.RichDialogExecution.NAME,
                    IvyJmxConstant.IvyServer.RichDialogExecution.KEY_RD_SESSIONS);
    maxRDSessionLabelSupport.setTooltip(ContentProvider.getFormatted("MaxRichDialogSessionDescription"));
    addLabelCalcSupport(maxRDSessionLabelSupport);
  }

}
