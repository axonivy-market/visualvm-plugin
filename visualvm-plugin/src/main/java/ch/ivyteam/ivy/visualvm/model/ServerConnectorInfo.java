package ch.ivyteam.ivy.visualvm.model;

import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class ServerConnectorInfo {
  private static final Logger LOGGER = Logger.getLogger(ServerConnectorInfo.class.getName());
  private static final String GLOBAL_REQUEST_PROCESSOR_NAME_OLD = "ivy:type=GlobalRequestProcessor,name=\"{0}-bio-{1}\"";
  private static final String GLOBAL_REQUEST_PROCESSOR_NAME_NEW = "ivy:type=GlobalRequestProcessor,name=\"{0}-nio-{1}\"";
  private String fProtocol;
  private String fDisplayProtocol;
  private String fPort;
  private String fScheme;
  private ObjectName fGlobalRequestProcessorName;
  private IvyApplicationInfo fIvyApplicationInfo;
  
  public ServerConnectorInfo(IvyApplicationInfo ivyApplicationInfo) {
    fIvyApplicationInfo = ivyApplicationInfo;
  }

  public String getProtocol() {
    return fProtocol;
  }

  public void setProtocol(String protocol) {
    fProtocol = protocol;
    buildData();
  }

  public String getPort() {
    return fPort;
  }

  public void setPort(String port) {
    try {
      int parsedPort = Integer.parseInt(port);
      if (parsedPort <= 0) {
        throw new IllegalArgumentException("The port param must be a positive integer");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("The port param must be an integer", e);
    }
    fPort = port;
    buildData();
  }

  public String getScheme() {
    return fScheme;
  }

  public void setScheme(String scheme) {
    fScheme = scheme;
    buildData();
  }

  public ObjectName getGlobalRequestProcessorName() {
    return fGlobalRequestProcessorName;
  }

  private void buildData() {
    if ((fProtocol != null) && (fPort != null)) {
      try {
        boolean isAJP = fProtocol.toLowerCase().contains("ajp");
        String protocol = "http";
        if (isAJP) {
          protocol = "ajp";
          fDisplayProtocol = "AJP";
        } else {
          fDisplayProtocol = "https".equals(fScheme) ? "HTTPS" : "HTTP";
        }
        if (fIvyApplicationInfo != null && !DataUtils.checkIvyVersion(fIvyApplicationInfo.getVersion(), 6, 2)) {
          fGlobalRequestProcessorName = new ObjectName(MessageFormat.format(
                GLOBAL_REQUEST_PROCESSOR_NAME_OLD, protocol, fPort)); 
        } else {
          fGlobalRequestProcessorName = new ObjectName(MessageFormat.format(
                GLOBAL_REQUEST_PROCESSOR_NAME_NEW, protocol, fPort));
        }
      } catch (MalformedObjectNameException ex) {
        LOGGER.log(Level.WARNING, ex.getMessage(), ex);
      }
    }
  }

  public String getDisplayProtocol() {
    return fDisplayProtocol;
  }

}
