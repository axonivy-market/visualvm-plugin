/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
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
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;

public class BasicIvyJmxDataCollector {

  public IvyApplicationInfo getApplicationInfo(
          MBeanServerConnection connection) throws IvyJmxDataCollectException {
    IvyApplicationInfo result = new IvyApplicationInfo();
    try {
      String[] attributeNames = new String[]{IvyJmxConstant.IvyServer.Server.KEY_VERSION,
        IvyJmxConstant.IvyServer.Server.KEY_BUILD_DATE,
        IvyJmxConstant.IvyServer.Server.KEY_INSTALLATION_DIRECTORY,
        IvyJmxConstant.IvyServer.Server.KEY_DEVELOPER_MODE,
        IvyJmxConstant.IvyServer.Server.KEY_RELEASE_CANDIDATE,
        IvyJmxConstant.IvyServer.Server.KEY_APPLICATION_NAME};

      AttributeList attributes = connection.getAttributes(IvyJmxConstant.IvyServer.Server.NAME,
              attributeNames);
      for (Attribute attribute : attributes.asList()) {
        if (attribute.getValue() != null) {
          switch (attribute.getName()) {
            case IvyJmxConstant.IvyServer.Server.KEY_VERSION:
              String version = DataUtils.getIvyServerVersion(attribute.getValue().toString());
              result.setVersion(version);
              break;
            case IvyJmxConstant.IvyServer.Server.KEY_BUILD_DATE:
              if (attribute.getValue() instanceof Date) {
                result.setBuildDate((Date) attribute.getValue());
              }
              break;
            case IvyJmxConstant.IvyServer.Server.KEY_APPLICATION_NAME:
              result.setApplicationName(attribute.getValue().toString());
              break;
            case IvyJmxConstant.IvyServer.Server.KEY_INSTALLATION_DIRECTORY:
              result.setInstallationDirectory(attribute.getValue().toString());
              break;
            case IvyJmxConstant.IvyServer.Server.KEY_DEVELOPER_MODE:
              result.setDeveloperMode(Boolean.TRUE.equals(attribute.getValue()));
              break;
            case IvyJmxConstant.IvyServer.Server.KEY_RELEASE_CANDIDATE:
              result.setReleaseCandidate(Boolean.TRUE.equals(attribute.getValue()));
              break;
          }
        }
      }
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return result;
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
        String fullProtocol = DataUtils.getFullConnectorProtocol(connector.getProtocol(),
                connector.getScheme());
        connector.setProtocol(fullProtocol);
        mappedConnectors.add(connector);
      }
    } catch (IOException | InstanceNotFoundException | ReflectionException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
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
      licenseInfo.setHostName(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_HOST_NAME));
      licenseInfo.setLicenseeIndividual(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_LICENSEE_INDIVIDUAL));
      licenseInfo.setLicenseeOrganisation(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_LICENSEE_ORGANISATION));
      licenseInfo.setLicenseKeyVersion(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_KEYVERSION));
      licenseInfo.setLicenseValidFrom(DataUtils.stringToDate(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_VALID_FROM)));
      licenseInfo.setLicenseValidUntil(DataUtils.stringToDate(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_VALID_UNTIL)));
      licenseInfo.setServerElementsLimit(Integer.parseInt(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_ELEMENTS_LIMIT)));
      licenseInfo.setServerRIA(Boolean.valueOf(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_RIA)));
      licenseInfo.setServerSessionsLimit(Integer.parseInt(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_SESSIONS_LIMIT)));
      licenseInfo.setServerUsersLimit(Integer.parseInt(getLicenseDetail(tabular,
              IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_USERS_LIMIT)));
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException |
            IOException ex) {
      throw new IvyJmxDataCollectException(ex);
    }
    return licenseInfo;
  }

  private String getLicenseDetail(TabularDataSupport tabular, String keys) {
    CompositeDataSupport data = (CompositeDataSupport) tabular.get(new String[]{keys});
    return data.get("propertyValue").toString();
  }

}
