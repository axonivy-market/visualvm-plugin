package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.model.OSInfo;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.model.SystemDatabaseInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.TabularDataSupport;

public class BasicIvyJmxDataCollector {

  private static final Logger LOGGER = Logger.getLogger(BasicIvyJmxDataCollector.class.getName());

  public IvyApplicationInfo getApplicationInfo(
          MBeanServerConnection connection) throws IvyJmxDataCollectException {

    IvyApplicationInfo result;
    try {
      String[] attributeNames = new String[]{IvyJmxConstant.IvyServer.Server.KEY_VERSION,
        IvyJmxConstant.IvyServer.Server.KEY_BUILD_DATE,
        IvyJmxConstant.IvyServer.Server.KEY_INSTALLATION_DIRECTORY,
        IvyJmxConstant.IvyServer.Server.KEY_DEVELOPER_MODE,
        IvyJmxConstant.IvyServer.Server.KEY_RELEASE_CANDIDATE,
        IvyJmxConstant.IvyServer.Server.KEY_APPLICATION_NAME};

      AttributeList attributes = connection.getAttributes(IvyJmxConstant.IvyServer.Server.NAME,
              attributeNames);
      result = createApplicationInfo(attributes);
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return result;
  }

  private IvyApplicationInfo createApplicationInfo(AttributeList attributes) {
    IvyApplicationInfo applicationInfo = new IvyApplicationInfo();
    for (Attribute attribute : attributes.asList()) {
      if (attribute.getValue() != null) {
        switch (attribute.getName()) {
          case IvyJmxConstant.IvyServer.Server.KEY_VERSION:
            String version = DataUtils.getIvyServerVersion(attribute.getValue().toString());
            applicationInfo.setVersion(version);
            break;
          case IvyJmxConstant.IvyServer.Server.KEY_BUILD_DATE:
            if (attribute.getValue() instanceof Date) {
              applicationInfo.setBuildDate((Date) attribute.getValue());
            }
            break;
          case IvyJmxConstant.IvyServer.Server.KEY_APPLICATION_NAME:
            applicationInfo.setApplicationName(attribute.getValue().toString());
            break;
          case IvyJmxConstant.IvyServer.Server.KEY_INSTALLATION_DIRECTORY:
            applicationInfo.setInstallationDirectory(attribute.getValue().toString());
            break;
          case IvyJmxConstant.IvyServer.Server.KEY_DEVELOPER_MODE:
            applicationInfo.setDeveloperMode(Boolean.TRUE.equals(attribute.getValue()));
            break;
          case IvyJmxConstant.IvyServer.Server.KEY_RELEASE_CANDIDATE:
            applicationInfo.setReleaseCandidate(Boolean.TRUE.equals(attribute.getValue()));
            break;
        }
      }
    }
    return applicationInfo;
  }

  public String getHostInfo(MBeanServerConnection connection) throws IvyJmxDataCollectException {
    String result = null;
    try {
      Object attribute = connection.getAttribute(IvyJmxConstant.JavaLang.Runtime.NAME,
              IvyJmxConstant.JavaLang.Runtime.KEY_NAME);
      if (attribute != null) {
        result = DataUtils.getHostNameFromRuntimeId(attribute.toString());
      }
    } catch (InstanceNotFoundException | ReflectionException | MBeanException | AttributeNotFoundException |
            IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return result;
  }

  public OSInfo getOSInfo(MBeanServerConnection connection) throws IvyJmxDataCollectException {
    OSInfo osInfo = new OSInfo();
    try {
      String[] attributeNames = new String[]{IvyJmxConstant.JavaLang.OperatingSystem.KEY_NAME,
        IvyJmxConstant.JavaLang.OperatingSystem.KEY_ARCH};

      AttributeList attributes = connection.getAttributes(IvyJmxConstant.JavaLang.OperatingSystem.NAME,
              attributeNames);
      for (Attribute attribute : attributes.asList()) {
        if (attribute.getValue() != null) {
          switch (attribute.getName()) {
            case IvyJmxConstant.JavaLang.OperatingSystem.KEY_NAME:
              osInfo.setName(attribute.getValue().toString());
              break;
            case IvyJmxConstant.JavaLang.OperatingSystem.KEY_ARCH:
              osInfo.setArch(attribute.getValue().toString());
              break;
          }
        }
      }
      String fullOSName = DataUtils.getFullOSName(osInfo.getName(), osInfo.getArch());
      osInfo.setName(fullOSName);
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return osInfo;
  }

  public List<ServerConnectorInfo> getMappedConnectors(MBeanServerConnection connection) throws
          IvyJmxDataCollectException {
    List<ServerConnectorInfo> mappedConnectors = new ArrayList<>();
    try {
      Set<ObjectName> mbeanNames = connection.queryNames(IvyJmxConstant.Ivy.Connector.PATTERN, null);
      String[] attributeNames = new String[]{
        IvyJmxConstant.Ivy.Connector.KEY_PROTOCOL,
        IvyJmxConstant.Ivy.Connector.KEY_PORT,
        IvyJmxConstant.Ivy.Connector.KEY_SCHEME};
      for (ObjectName beanName : mbeanNames) {
        ServerConnectorInfo connector = new ServerConnectorInfo();
        AttributeList attributes = connection.getAttributes(beanName, attributeNames);
        for (Attribute attribute : attributes.asList()) {
          if (attribute.getValue() != null) {
            switch (attribute.getName()) {
              case IvyJmxConstant.Ivy.Connector.KEY_PROTOCOL:
                connector.setProtocol(attribute.getValue().toString());
                break;
              case IvyJmxConstant.Ivy.Connector.KEY_PORT:
                connector.setPort(attribute.getValue().toString());
                break;
              case IvyJmxConstant.Ivy.Connector.KEY_SCHEME:
                connector.setScheme(attribute.getValue().toString());
                break;
            }
          }
        }
        mappedConnectors.add(connector);
      }
    } catch (IOException | InstanceNotFoundException | ReflectionException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    Collections.sort(mappedConnectors, new Comparator<ServerConnectorInfo>() {
      @Override
      public int compare(ServerConnectorInfo o1, ServerConnectorInfo o2) {
        int result = o1.getProtocol().compareTo(o2.getProtocol());
        if (result == 0) {
          result = o1.getScheme().compareTo(o2.getScheme());
        }
        return result;
      }

    });
    return mappedConnectors;
  }

  public SystemDatabaseInfo getSystemDatabaseInfo(MBeanServerConnection connection) throws
          IvyJmxDataCollectException {
    SystemDatabaseInfo systemDatabase = new SystemDatabaseInfo();
    try {
      String[] attributeNames = new String[]{
        IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_NAME,
        IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_VERSION,
        IvyJmxConstant.IvyServer.DatabasePersistency.KEY_IVY_SYSDB_VERSION,
        IvyJmxConstant.IvyServer.DatabasePersistency.KEY_CONNECTION_URL,
        IvyJmxConstant.IvyServer.DatabasePersistency.KEY_DRIVER_NAME,
        IvyJmxConstant.IvyServer.DatabasePersistency.KEY_USERNAME};
      AttributeList attributes = connection.getAttributes(
              IvyJmxConstant.IvyServer.DatabasePersistency.NAME, attributeNames);
      for (Attribute attribute : attributes.asList()) {
        if (attribute.getValue() != null) {
          switch (attribute.getName()) {
            case IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_NAME:
              systemDatabase.setType(attribute.getValue().toString());
              break;
            case IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_VERSION:
              systemDatabase.setVersion(attribute.getValue().toString());
              break;
            case IvyJmxConstant.IvyServer.DatabasePersistency.KEY_IVY_SYSDB_VERSION:
              systemDatabase.setIvySystemDbVersion(attribute.getValue().toString());
              break;
            case IvyJmxConstant.IvyServer.DatabasePersistency.KEY_DRIVER_NAME:
              systemDatabase.setDriver(attribute.getValue().toString());
              break;
            case IvyJmxConstant.IvyServer.DatabasePersistency.KEY_USERNAME:
              systemDatabase.setUsername(attribute.getValue().toString());
              break;
            case IvyJmxConstant.IvyServer.DatabasePersistency.KEY_CONNECTION_URL:
              systemDatabase.setConnectionUrl(attribute.getValue().toString());
              break;
          }
        }
      }
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return systemDatabase;
  }

  public IvyLicenseInfo getLicenseInfo(MBeanServerConnection connection) throws IvyJmxDataCollectException {
    IvyLicenseInfo licenseInfo = new IvyLicenseInfo();
    ObjectName objectName = IvyJmxConstant.IvyServer.Server.NAME;
    String attributeName = IvyJmxConstant.IvyServer.Server.KEY_LICENSE_PARAMETERS;
    try {
      TabularDataSupport tabular = (TabularDataSupport) connection.getAttribute(objectName, attributeName);
      licenseInfo = LicenseInfoCollector.getLicenseInfo(tabular);
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException |
            IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return licenseInfo;
  }

  public ObjectName getTomcatManagerName(MBeanServerConnection serverConnection) {
    Set<ObjectName> tomcatManagers = queryNames(serverConnection,
            IvyJmxConstant.Ivy.Manager.PATTERN);
    if (tomcatManagers.size() >= 1) {
      return tomcatManagers.iterator().next();
    }
    return null;
  }

  public Set<ObjectName> queryNames(MBeanServerConnection serverConnection, ObjectName filter) {
    try {
      return serverConnection.queryNames(filter, null);
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public Set<ObjectName> queryNames(MBeanServerConnection serverConnection, String filter) {
    try {
      return queryNames(serverConnection, new ObjectName(filter));
    } catch (MalformedObjectNameException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public Set<ObjectName> getTomcatRequestProcessors(MBeanServerConnection serverConnection) {
    try {
      return serverConnection.queryNames(IvyJmxConstant.Ivy.Processor.PATTERN, null);
    } catch (IOException ex) {
      LOGGER.warning(ex.getMessage());
      return Collections.emptySet();
    }
  }

  public int getNamedUsers(MBeanServerConnection connection) throws IvyJmxDataCollectException {
    int namedUsers = 0;
    ObjectName objectName = IvyJmxConstant.IvyServer.SecurityManager.NAME;
    String attributeName = IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_USERS;
    try {
      Object licensedUsers = connection.getAttribute(objectName, attributeName);
      if (licensedUsers instanceof Number) {
        namedUsers = ((Number) licensedUsers).intValue();
      }
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException |
            IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return namedUsers;
  }

  public int getConcurrentUsers(MBeanServerConnection connection) throws IvyJmxDataCollectException {
    int concurrentUsers = 0;
    ObjectName objectName = IvyJmxConstant.IvyServer.SecurityManager.NAME;
    String attributeName = IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS;
    try {
      Object licensedSessions = connection.getAttribute(objectName, attributeName);
      if (licensedSessions instanceof Number) {
        concurrentUsers = ((Number) licensedSessions).intValue();
      }
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException |
            IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return concurrentUsers;
  }

}
