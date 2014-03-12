package ch.ivyteam.ivy.visualvm.util;

import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public final class DataUtils {

  private static final Logger LOGGER = Logger.getLogger(DataUtils.class.getName());
  private static final String[] DATABASE_PREFIXES = new String[]{
    "database=", "databasename=", "schema="};

  private DataUtils() {
  }

  public static Date stringToDate(String dateString) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    if (dateString != null && !dateString.isEmpty()) {
      try {
        return format.parse(dateString);
      } catch (ParseException ex) {
        LOGGER.warning(ex.getMessage());
      }
    }
    return null;
  }

  public static String dateToString(Date date) {
    DateFormat localizedFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
    return localizedFormat.format(date);
  }

  public static Integer stringToInteger(String valueString) {
    int result = 0;
    if (valueString != null) {
      result = Integer.parseInt(valueString);
    }
    return result;
  }

  public static String getIvyServerVersion(String versionData) {
    String version = versionData;
    int revIndex = version.lastIndexOf('.');
    String ivyVersion = version.substring(0, revIndex);
    String revision = version.substring(revIndex + 1, version.length());
    version = MessageFormat.format("{0} (revision {1})", ivyVersion, revision);
    return version;
  }

  public static String getFullOSName(String osName, String osArch) {
    String result = osName;
    if (osName != null && osArch != null && osArch.toLowerCase().contains("64")) {
      result = osName + " (64bit)";
    }
    return result;
  }

  public static String getFullConnectorProtocol(String protocol, String scheme) {
    String result = protocol;
    if (result != null && "https".equals(scheme)) {
      result = result.replace("HTTP/", "HTTPS/");
    }
    return result;
  }

  public static String getShortConnectorProtocol(String protocol, String scheme) {
    String result = getFullConnectorProtocol(protocol, scheme);
    if (result != null) {
      int slashIndex = result.indexOf('/');
      result = result.substring(0, slashIndex);
    }
    return result;
  }

  public static String findProtocol(List<ServerConnectorInfo> mappedConnectors, String port) {
    String protocol = "";
    for (ServerConnectorInfo connector : mappedConnectors) {
      if (port.equals(connector.getPort())) {
        protocol = DataUtils.getShortConnectorProtocol(connector.getProtocol(), connector.getScheme());
        break;
      }
    }
    return protocol;
  }

  public static String getHostNameFromRuntimeId(String runtimeId) {
    String hostName = runtimeId;
    if (runtimeId != null) {
      int atIndex = runtimeId.indexOf('@');
      if ((atIndex >= 0) && (atIndex < runtimeId.length() - 1)) {
        hostName = hostName.substring(atIndex + 1);
      }
    }
    return hostName;
  }

  public static String getSchemaFromConnectionUrl(String url) {
    String temp = url.replaceAll(" ", "");
    int index = findDatabasePrefixIndex(temp);

    String schema = null;
    if (index >= 0) {
      schema = temp;
      int startIndex;
      for (startIndex = index; startIndex < schema.length(); startIndex++) {
        if (isValidCharForSchema(schema.charAt(startIndex))) {
          break;
        }
      }
      int endIndex;
      for (endIndex = startIndex + 1; endIndex < schema.length(); endIndex++) {
        if (!isValidCharForSchema(schema.charAt(endIndex))) {
          break;
        }
      }
      schema = schema.substring(startIndex, endIndex);
    }
    return schema;
  }

  public static String getPortFromConnectionUrl(String url) {
    String temp = url.replaceAll(" ", "");
    int index = findDatabasePrefixIndex(temp);

    String port = null;
    if (index > 0) {
      port = temp.substring(0, index);
      int endIndex = firstDigitFromEnd(port);
      if (endIndex < 0) {
        return null;
      }
      int startIndex;
      for (startIndex = endIndex; startIndex >= 0; startIndex--) {
        if (!Character.isDigit(port.charAt(startIndex))) {
          if (port.charAt(startIndex) != ':') {
            return null;
          }
          break;
        }
      }

      port = port.substring(startIndex + 1, endIndex + 1);
    }
    return port;
  }

  private static int findDatabasePrefixIndex(String exp) {
    String lowerExp = exp.toLowerCase();
    int index = -1;
    for (String prefix : DATABASE_PREFIXES) {
      index = lowerExp.indexOf(prefix);
      if (index >= 0) {
        index += prefix.length();
        break;
      }
    }
    if (index < 0) {
      index = Math.max(lowerExp.lastIndexOf('/'), lowerExp.lastIndexOf(':'));
    }
    return index;
  }

  public static String getHostFromConnectionUrl(String url) {
    String compactedURL = url.replaceAll(" ", "");
    int index = findDatabasePrefixIndex(compactedURL);

    int portIndex = -1;
    if (index > 0) {
      String port = compactedURL.substring(0, index);
      int endIndex = firstDigitFromEnd(port);
      if (endIndex < 0) {
        return null;
      }
      int startIndex;
      for (startIndex = endIndex; startIndex >= 0; startIndex--) {
        if (!Character.isDigit(port.charAt(startIndex))) {
          if (port.charAt(startIndex) != ':') {
            return null;
          }
          break;
        }
      }
      portIndex = startIndex;
    }
    return getHost(portIndex, compactedURL);
  }

  private static String getHost(int portIndex, String url) {
    String tmpHost;
    if (portIndex > 0) {
      tmpHost = url.substring(0, portIndex);
      int endIndex;
      for (endIndex = tmpHost.length() - 1; endIndex >= 0; endIndex--) {
        if (isValidCharForSchema(tmpHost.charAt(endIndex))) {
          break;
        }
      }
      if (endIndex < 0) {
        return null;
      }
      int startIndex;
      for (startIndex = endIndex; startIndex >= 0; startIndex--) {
        if (!isValidCharForSchema(tmpHost.charAt(startIndex))) {
          break;
        }
      }
      tmpHost = tmpHost.substring(startIndex + 1, endIndex + 1);
    } else {
      tmpHost = null;
    }
    return tmpHost;
  }

  private static int firstDigitFromEnd(String port) {
    int endIndex;
    for (endIndex = port.length() - 1; endIndex >= 0; endIndex--) {
      if (Character.isDigit(port.charAt(endIndex))) {
        break;
      }
    }
    return endIndex;
  }

  private static boolean isValidCharForSchema(char c) {
    return Character.isDigit(c) || Character.isLetter(c) || ('_' == c);
  }

  public static String getProtocol(ObjectName processorName) {
    String[] splits = processorName.getKeyProperty("name")
            .replaceAll("\"", "")
            .replace("-bio", "")
            .split("-");
    String protocol = splits[0].toUpperCase();
    return MessageFormat.format("{0} ({1})", protocol, splits[1]);
  }

  public static String getPort(ObjectName processorName) {
    String[] splits = processorName.getKeyProperty("name")
            .replaceAll("\"", "")
            .split("-bio-");
    return splits[1];
  }

  public static MBeanServerConnection getMBeanServerConnection(Application app) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(app);
    if (jmx != null && jmx.getConnectionState() == JmxModel.ConnectionState.CONNECTED) {
      return jmx.getMBeanServerConnection();
    } else {
      return null;
    }
  }

  public static Set<ObjectName> getExternalDbConfigs(MBeanServerConnection connection) {
    Set<ObjectName> externalDbConfigs = new TreeSet<>();
    try {
      String filterName = "Xpert.ivy Server:type=External Database,application=*,environment=*,name=*";
      ObjectName objName = new ObjectName(filterName);
      externalDbConfigs = connection.queryNames(objName, null);
    } catch (IOException | MalformedObjectNameException ex) {
      LOGGER.warning(ex.getMessage());
    }
    return externalDbConfigs;
  }

  public static Set<ObjectName> getWebServicesConfigs(MBeanServerConnection connection) {
    Set<ObjectName> webServicesConfigs = new TreeSet<>();
    try {
      String filterName = "Xpert.ivy Server:type=External Web Service,application=*,environment=*,name=*";
      ObjectName objName = new ObjectName(filterName);
      webServicesConfigs = connection.queryNames(objName, null);
    } catch (IOException | MalformedObjectNameException ex) {
      LOGGER.warning(ex.getMessage());
    }
    return webServicesConfigs;
  }

}
