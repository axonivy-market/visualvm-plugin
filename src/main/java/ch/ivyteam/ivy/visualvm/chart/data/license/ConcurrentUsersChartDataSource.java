package ch.ivyteam.ivy.visualvm.chart.data.license;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.io.IOException;
import java.util.logging.Logger;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;

public class ConcurrentUsersChartDataSource extends XYChartDataSource {
  private static final Logger LOGGER = Logger.getLogger(ConcurrentUsersChartDataSource.class.getName());

  public ConcurrentUsersChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    addFixedSerie("Limit", getSessionLimit());
    addSerie("Now", "Max Occurs", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
  }

  private int getSessionLimit() {
    MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
    int result = 0;
    ObjectName objectName = IvyJmxConstant.IvyServer.Server.NAME;
    String attributeName = IvyJmxConstant.IvyServer.Server.KEY_LICENSE_PARAMETERS;
    try {
      TabularDataSupport tabular = (TabularDataSupport) connection.getAttribute(objectName, attributeName);
      if (tabular != null) {
        CompositeDataSupport data = (CompositeDataSupport) tabular.get(new String[]{
          IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_SESSIONS_LIMIT});
        if (data != null) {
          result = Integer.parseInt(data.get("propertyValue").toString());
        }
      }
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException |
            IOException ex) {
      LOGGER.warning(ex.getMessage());
    }
    return result;
  }

}
