/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.openide.util.Exceptions;

public final class DataUtils {

  private static final String[] DATABASE_PREFIXES = new String[]{
    "database=", "databasename=", "schema="};

  private DataUtils() {
  }

  public static Date stringToDate(String dateString) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return format.parse(dateString);
    } catch (ParseException ex) {
      Exceptions.printStackTrace(ex);
    }
    return new Date();
  }

  public static String toDateString(Date date) {
    DateFormat localizedFormat = DateFormat.getDateInstance(
            DateFormat.FULL, Locale.getDefault());
    return localizedFormat.format(date);
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
    if (osName != null) {
      if (osArch != null && osArch.toLowerCase().contains("64")) {
        result = osName + " (64bit)";
      }
    }
    return result;
  }

  public static String getFullConnectorProtocol(String protocol, String scheme) {
    String result = protocol;
    if (result != null) {
      if ("https".equals(scheme)) {
        result = result.replace("HTTP/", "HTTPS/");
      }
    }
    return result;
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
      index = Math.max(lowerExp.lastIndexOf("/"), lowerExp.lastIndexOf(":"));
    }
    return index;
  }

  public static String getHostFromConnectionUrl(String url) {
    String temp = url.replaceAll(" ", "");
    int index = findDatabasePrefixIndex(temp);

    int portIndex = -1;
    if (index > 0) {
      String port = temp.substring(0, index);
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

    String host = null;
    if (portIndex > 0) {
      host = temp.substring(0, portIndex);
      int endIndex;
      for (endIndex = host.length() - 1; endIndex >= 0; endIndex--) {
        if (isValidCharForSchema(host.charAt(endIndex))) {
          break;
        }
      }
      if (endIndex < 0) {
        return null;
      }
      int startIndex;
      for (startIndex = endIndex; startIndex >= 0; startIndex--) {
        if (!isValidCharForSchema(host.charAt(startIndex))) {
          break;
        }
      }
      host = host.substring(startIndex + 1, endIndex + 1);
    }
    return host;
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

  public static Set<ObjectName> queryNames(MBeanServerConnection serverConnection, String filter) {
    try {
      return serverConnection.queryNames(new ObjectName(filter), null);
    } catch (IOException | MalformedObjectNameException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

}
