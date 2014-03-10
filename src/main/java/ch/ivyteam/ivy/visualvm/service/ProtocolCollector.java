package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public final class ProtocolCollector {
  private static final Logger LOGGER = Logger.getLogger(ProtocolCollector.class.getName());
  private static Map<ObjectName, String> protocolMap;

  private ProtocolCollector() {
  }

  private static void initProtocolMap(MBeanServerConnection mBeanServerConnection) {
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    try {
      List<ServerConnectorInfo> mappedConnectors = collector.getMappedConnectors(mBeanServerConnection);
      protocolMap = new HashMap<>();
      for (ObjectName processorName : collector.getTomcatRequestProcessors(mBeanServerConnection)) {
        String port = DataUtils.getPort(processorName);
        String protocol = DataUtils.findProtocol(mappedConnectors, port);
        protocolMap.put(processorName, protocol);
      }
    } catch (IvyJmxDataCollectException ex) {
      LOGGER.warning(ex.getMessage());
    }
  }

  public static String getProtocol(MBeanServerConnection mBeanServerConnection, ObjectName processorName) {
    if (protocolMap == null) {
      initProtocolMap(mBeanServerConnection);
    }
    return protocolMap.get(processorName);
  }

}
