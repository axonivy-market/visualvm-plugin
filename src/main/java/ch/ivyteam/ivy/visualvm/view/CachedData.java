package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class CachedData {
  private static final Logger LOGGER = Logger.getLogger(CachedData.class.getName());
  private Map<ObjectName, String> protocolMap;

  private final MBeanServerConnection fMBeanServerConnection;

  public CachedData(MBeanServerConnection mBeanServerConnection) {
    fMBeanServerConnection = mBeanServerConnection;
  }

  private void initProtocolMap() {
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    try {
      List<ServerConnectorInfo> mappedConnectors = collector.getMappedConnectors(fMBeanServerConnection);
      protocolMap = new HashMap<>();
      for (ObjectName processorName : collector.getTomcatRequestProcessors(fMBeanServerConnection)) {
        String port = DataUtils.getPort(processorName);
        String protocol = DataUtils.findProtocol(mappedConnectors, port);
        protocolMap.put(processorName, protocol);
      }
    } catch (IvyJmxDataCollectException ex) {
      LOGGER.warning(ex.getMessage());
    }
  }

  public String getProtocol(ObjectName processorName) {
    if (protocolMap == null) {
      initProtocolMap();
    }
    return protocolMap.get(processorName);
  }

}
