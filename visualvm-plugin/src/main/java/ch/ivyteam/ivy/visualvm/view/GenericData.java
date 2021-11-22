package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import java.util.List;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;

public class GenericData {
  private static final Logger LOGGER = Logger.getLogger(GenericData.class.getName());

  private IvyApplicationInfo fApplicationInfo;
  private List<ServerConnectorInfo> fServerConnectors;

  private final MBeanServerConnection fMBeanServerConnection;

  public GenericData(MBeanServerConnection mBeanServerConnection) {
    fMBeanServerConnection = mBeanServerConnection;
  }

  public List<ServerConnectorInfo> getServerConnectors() {
    if (fServerConnectors == null) {
      BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
      try {
        fServerConnectors = collector.getMappedConnectors(fMBeanServerConnection, getApplicationInfo());
      } catch (IvyJmxDataCollectException ex) {
        LOGGER.warning(ex.getMessage());
      }
    }
    return fServerConnectors;
  }

  public IvyApplicationInfo getApplicationInfo() {
    if (fApplicationInfo == null) {
      BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
      try {
        fApplicationInfo = collector.getApplicationInfo(fMBeanServerConnection);
      } catch (IvyJmxDataCollectException ex) {
        LOGGER.warning(ex.getMessage());
      }
    }
    return fApplicationInfo;
  }

}
