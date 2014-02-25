package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.io.IOException;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import org.openide.util.Exceptions;

public class MLicenseChartDataSource extends MChartDataSource {
  private int fServerSessionLimit;

  public MLicenseChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    retrieveLicenseInfo();
    addSerie("Current", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    addFixedSerie("Max", fServerSessionLimit);
  }

  private void retrieveLicenseInfo() {
    try {
      MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
      fServerSessionLimit = getSessionLimit(connection);
    } catch (IvyJmxDataCollectException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  public int getSessionLimit(MBeanServerConnection connection) throws IvyJmxDataCollectException {
    int result = 0;
    ObjectName objectName = IvyJmxConstant.IvyServer.Server.NAME;
    String attributeName = IvyJmxConstant.IvyServer.Server.KEY_LICENSE_PARAMETERS;
    try {
      TabularDataSupport tabular = (TabularDataSupport) connection.getAttribute(objectName, attributeName);
      result = Integer.parseInt(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_SESSIONS_LIMIT));
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException |
            IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return result;
  }

  private String getLicenseDetail(TabularDataSupport tabular, String keys) {
    CompositeDataSupport data = (CompositeDataSupport) tabular.get(new String[]{keys});
    return data.get("propertyValue").toString();
  }

}
