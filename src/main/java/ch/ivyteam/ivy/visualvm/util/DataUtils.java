package ch.ivyteam.ivy.visualvm.util;

import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public final class DataUtils {

  private static final String APP_STRING_KEY = "application";
  private static final String ENVIRONMENT_STRING_KEY = "environment";
  private static final String CONFIG_STRING_KEY = "name";
  private static final String FILTER_STRING = "ivy Engine:type={0},application=*,environment=*,name=*";
  private static final String WS_FILTER_STRING = FILTER_STRING.replace("{0}", "External Web Service");
  private static final String EXT_DB_FILTER_STRING = FILTER_STRING.replace("{0}", "External Database");

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

  /**
   * @param date
   * @return date string with current locale (note: only date, no time value)
   */
  public static String dateToString(Date date) {
    Locale formatLocale = getFormatLocale();
    DateFormat localizedFormat = DateFormat.getDateInstance(DateFormat.FULL, formatLocale);
    return localizedFormat.format(date);
  }

  /**
   * @param date
   * @return date string with current locale
   */
  public static String dateTimeToString(Date date) {
    Locale formatLocale = getFormatLocale();
    DateFormat localizedFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM,
            formatLocale);
    return localizedFormat.format(date);
  }

  public static Locale getFormatLocale() {
    String langFormat = System.getProperty("user.language.format");
    String countryFormat = System.getProperty("user.country.format");
    Locale formatLocale;
    if (langFormat != null) {
      formatLocale = new Locale(langFormat, countryFormat);
    } else {
      formatLocale = Locale.getDefault();
    }
    return formatLocale;
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

  public static boolean checkIvyVersion(String version, int fstV, int secV) {
    boolean result = false;
    if (version != null) {
      String[] splits = version.split("\\.");
      if (splits.length > 0) {
        try {
          int v1 = Integer.parseInt(splits[0]);
          if (v1 > fstV) {
            result = true;
          } else if ((v1 == fstV) && (splits.length > 1)) {
            try {
              int v2 = Integer.parseInt(splits[1]);
              if (v2 >= secV) {
                result = true;
              }
            } catch (NumberFormatException ex) {
            }
          }
        } catch (NumberFormatException ex) {
        }
      }
    }
    return result;
  }

  public static String getFullOSName(String osName, String osArch) {
    String result = osName;
    if (osName != null && osArch != null && osArch.toLowerCase().contains("64")) {
      result = osName + " (64bit)";
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

  public static Map<String, Map<String, Set<String>>> getExternalDbConfigs(MBeanServerConnection conn) {
    Set<ObjectName> externalDbConfigs = new TreeSet<>();
    try {
      ObjectName objName = new ObjectName(EXT_DB_FILTER_STRING);
      externalDbConfigs = conn.queryNames(objName, null);
    } catch (IOException | MalformedObjectNameException ex) {
      LOGGER.warning(ex.getMessage());
    }
    return createDataMap(externalDbConfigs);
  }

  public static Map<String, Map<String, Set<String>>> getWebServicesConfigs(MBeanServerConnection conn) {
    Set<ObjectName> webServicesConfigs = new TreeSet<>();
    try {
      ObjectName objName = new ObjectName(WS_FILTER_STRING);
      webServicesConfigs = conn.queryNames(objName, null);
    } catch (IOException | MalformedObjectNameException ex) {
      LOGGER.warning(ex.getMessage());
    }
    return createDataMap(webServicesConfigs);
  }

  private static Map<String, Map<String, Set<String>>> createDataMap(Set<ObjectName> externalDbConfigs) {
    Map<String, Map<String, Set<String>>> appEnvConfMap = new TreeMap<>();
    for (ObjectName each : externalDbConfigs) {
      String app = each.getKeyProperty(APP_STRING_KEY);
      String env = each.getKeyProperty(ENVIRONMENT_STRING_KEY);
      String conf = each.getKeyProperty(CONFIG_STRING_KEY);

      if (appEnvConfMap.containsKey(app)) {
        Map<String, Set<String>> envConfMap = appEnvConfMap.get(app);
        if (envConfMap.containsKey(env)) {
          Set<String> confs = envConfMap.get(env);
          confs.add(conf);
        } else {
          Set<String> confList = new TreeSet<>();
          confList.add(conf);
          envConfMap.put(env, confList);
        }
      } else {
        Set<String> confs = new TreeSet<>();
        confs.add(conf);
        Map<String, Set<String>> envConfMap = new TreeMap<>();
        envConfMap.put(env, confs);
        appEnvConfMap.put(app, envConfMap);
      }
    }
    return appEnvConfMap;
  }

  public static void sort(List<? extends SQLInfo> c, final Comparator<SQLInfo>... comparators) {
    Collections.sort(c, new Comparator<SQLInfo>() {

      @Override
      public int compare(SQLInfo a, SQLInfo b) {
        for (Comparator<SQLInfo> cmp : comparators) {
          int delta = cmp.compare(a, b);
          if (delta != 0) {
            return delta;
          }
        }
        return 0;
      }

    });
  }

}
