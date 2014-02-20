/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.OSInfo;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.model.SystemDatabaseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerInformationTest extends TestCase {

  private static final String VERSION = "5.1.0.44832";
  private static final String VERSION_CUSTOM = "5.1.0 (revision 44832)";
  private static final Date BUILD_DATE = new Date();
  private static final String INSTALL_DIR = "D:\\teamup\\XpertIvyServer5.1.0.44832_Windows_x64";
  private static final String DEV_MODE = "";
  private static final String RELEASE_CANDIDATE = "";
  private static final String APPLICATION_NAME = "Xpert.ivy Server";
  private static final String DB_PRODUCT_NAME = "MySQL";
  private static final String DB_PRODUCT_VERSION = "5.5.21";
  private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/XpertIvySystemDatabase_jmx";
  private static final String DB_DRIVER_NAME = "com.jdbc.mysql.Driver";
  private static final String DB_USERNAME = "root";

  private static final String PORT_8009 = "8009";
  private static final String PORT_8443 = "8443";
  private static final String HTTPS = "https";
  private static final String AJP_13 = "AJP/1.3";
  private static final String HTTP = "http";
  private static final String HTTP_11 = "HTTP/1.1";
  private static final String HTTPS_11 = "HTTPS/1.1";
  private static final String PORT_8080 = "8080";

  private static final String OS_NAME = "Windows Server 2008";
  private static final String OS_ARCH = "amd64";
  private static final String OS_NAME_CUSTOM = "Windows Server 2008 (64bit)";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Test server, database and connectors info using mock
   *
   * @throws MalformedObjectNameException
   * @throws IOException
   * @throws ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException
   * @throws javax.management.InstanceNotFoundException
   * @throws javax.management.ReflectionException
   */
  public void testServerInformation() throws MalformedObjectNameException,
          IOException, IvyJmxDataCollectException, IOException,
          InstanceNotFoundException, ReflectionException {

    MBeanServerConnection mockedMBeanServer = mock(MBeanServerConnection.class);

    String[] serverAttributeNames = new String[]{
      IvyJmxConstant.IvyServer.Server.KEY_VERSION,
      IvyJmxConstant.IvyServer.Server.KEY_BUILD_DATE,
      IvyJmxConstant.IvyServer.Server.KEY_INSTALLATION_DIRECTORY,
      IvyJmxConstant.IvyServer.Server.KEY_DEVELOPER_MODE,
      IvyJmxConstant.IvyServer.Server.KEY_RELEASE_CANDIDATE,
      IvyJmxConstant.IvyServer.Server.KEY_APPLICATION_NAME
    };
    String[] osAttributeNames = new String[]{
      IvyJmxConstant.JavaLang.OperatingSystem.KEY_NAME,
      IvyJmxConstant.JavaLang.OperatingSystem.KEY_ARCH
    };
    String[] dbAttributeNames = new String[]{
      IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_NAME,
      IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_VERSION,
      IvyJmxConstant.IvyServer.DatabasePersistency.KEY_IVY_SYSDB_VERSION,
      IvyJmxConstant.IvyServer.DatabasePersistency.KEY_CONNECTION_URL,
      IvyJmxConstant.IvyServer.DatabasePersistency.KEY_DRIVER_NAME,
      IvyJmxConstant.IvyServer.DatabasePersistency.KEY_USERNAME
    };
    String[] connectorAttributeNames = new String[]{
      IvyJmxConstant.Ivy.Connector.KEY_PROTOCOL,
      IvyJmxConstant.Ivy.Connector.KEY_PORT,
      IvyJmxConstant.Ivy.Connector.KEY_SCHEME
    };

    AttributeList serverAttributeList = serverInfoMockData();
    AttributeList osAttributeList = osInfoMockData();
    AttributeList dbAttributeList = databaseInfoMockData();

    // mock data for connectors
    Set<ObjectName> mBeanNames = new HashSet<ObjectName>();
    mBeanNames.add(new ObjectName("ivy:type=Connector,port=8080"));
    mBeanNames.add(new ObjectName("ivy:type=Connector,port=8009"));
    mBeanNames.add(new ObjectName("ivy:type=Connector,port=8443"));
    Object[] mBeanNameArray = mBeanNames.toArray();
    when(mockedMBeanServer.queryNames(IvyJmxConstant.Ivy.Connector.PATTERN, null)).thenReturn(mBeanNames);

    AttributeList connectorAttributeList = connectorHTTPInfoMockData();
    AttributeList connectorAttributeList1 = connectorAJPInfoMockData();
    AttributeList connectorAttributeList2 = connectorHTPPSInfoMockData();

    // retrieve AttributeList from MBeans
    when(mockedMBeanServer.getAttributes(IvyJmxConstant.IvyServer.Server.NAME, serverAttributeNames)).
            thenReturn(serverAttributeList);
    when(mockedMBeanServer.getAttributes(IvyJmxConstant.JavaLang.OperatingSystem.NAME, osAttributeNames)).
            thenReturn(osAttributeList);
    when(mockedMBeanServer.getAttributes(IvyJmxConstant.IvyServer.DatabasePersistency.NAME, dbAttributeNames)).
            thenReturn(dbAttributeList);
    when(mockedMBeanServer.getAttributes((ObjectName) mBeanNameArray[0], connectorAttributeNames)).thenReturn(
            connectorAttributeList);
    when(mockedMBeanServer.getAttributes((ObjectName) mBeanNameArray[1], connectorAttributeNames)).thenReturn(
            connectorAttributeList1);
    when(mockedMBeanServer.getAttributes((ObjectName) mBeanNameArray[2], connectorAttributeNames)).thenReturn(
            connectorAttributeList2);

    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    // retrieve information of server, database and connectors
    IvyApplicationInfo basicInformation = collector.getApplicationInfo(mockedMBeanServer);
    //verify server info
    assertEquals(VERSION_CUSTOM, basicInformation.getVersion());
    assertEquals(APPLICATION_NAME, basicInformation.getApplicationName());
    assertEquals(BUILD_DATE, basicInformation.getBuildDate());
    assertEquals(INSTALL_DIR, basicInformation.getInstallationDirectory());
    // verify os info
    OSInfo osInfo = collector.getOSInfo(mockedMBeanServer);
    assertEquals(OS_NAME_CUSTOM, osInfo.getName());
    // verify database info
    SystemDatabaseInfo sysDbInfo = collector.getSystemDatabaseInfo(mockedMBeanServer);
    assertEquals(DB_PRODUCT_NAME, sysDbInfo.getType());
    assertEquals(DB_DRIVER_NAME, sysDbInfo.getDriver());
    assertEquals(DB_CONNECTION_URL, sysDbInfo.getConnectionUrl());
    assertEquals(DB_PRODUCT_VERSION, sysDbInfo.getVersion());
    assertEquals(DB_USERNAME, sysDbInfo.getUsername());

    // verify connectors
    List<ServerConnectorInfo> connectorInfo = collector.getMappedConnectors(mockedMBeanServer);
    assertEquals(PORT_8080, connectorInfo.get(0).getPort());
    assertEquals(HTTP_11, connectorInfo.get(0).getProtocol());
    assertEquals(HTTP, connectorInfo.get(0).getScheme());

    assertEquals(PORT_8009, connectorInfo.get(1).getPort());
    assertEquals(AJP_13, connectorInfo.get(1).getProtocol());
    assertEquals(HTTP, connectorInfo.get(1).getScheme());

    assertEquals(PORT_8443, connectorInfo.get(2).getPort());
    // HTTPS_11 because HTTP will be replaced by HTTPS if scheme is https
    assertEquals(HTTPS_11, connectorInfo.get(2).getProtocol());
    assertEquals(HTTPS, connectorInfo.get(2).getScheme());

  }

  private AttributeList connectorHTPPSInfoMockData() {
    Attribute protocolAtt2 = mock(Attribute.class);
    when(protocolAtt2.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_PROTOCOL);
    when(protocolAtt2.getValue()).thenReturn(HTTP_11);
    Attribute schemeAtt2 = mock(Attribute.class);
    when(schemeAtt2.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_SCHEME);
    when(schemeAtt2.getValue()).thenReturn(HTTPS);
    Attribute portAtt2 = mock(Attribute.class);
    when(portAtt2.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_PORT);
    when(portAtt2.getValue()).thenReturn(PORT_8443);
    AttributeList connectorAttributeList2 = mock(AttributeList.class);
    List<Attribute> connectorAttributes2 = new ArrayList<>();
    connectorAttributes2.add(protocolAtt2);
    connectorAttributes2.add(schemeAtt2);
    connectorAttributes2.add(portAtt2);
    when(connectorAttributeList2.asList()).thenReturn(connectorAttributes2);
    return connectorAttributeList2;
  }

  private AttributeList connectorAJPInfoMockData() {
    Attribute protocolAtt1 = mock(Attribute.class);
    when(protocolAtt1.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_PROTOCOL);
    when(protocolAtt1.getValue()).thenReturn(AJP_13);
    Attribute schemeAtt1 = mock(Attribute.class);
    when(schemeAtt1.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_SCHEME);
    when(schemeAtt1.getValue()).thenReturn(HTTP);
    Attribute portAtt1 = mock(Attribute.class);
    when(portAtt1.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_PORT);
    when(portAtt1.getValue()).thenReturn(PORT_8009);
    AttributeList connectorAttributeList1 = mock(AttributeList.class);
    List<Attribute> connectorAttributes1 = new ArrayList<>();
    connectorAttributes1.add(protocolAtt1);
    connectorAttributes1.add(schemeAtt1);
    connectorAttributes1.add(portAtt1);
    when(connectorAttributeList1.asList()).thenReturn(connectorAttributes1);
    return connectorAttributeList1;
  }

  private AttributeList connectorHTTPInfoMockData() {
    Attribute protocolAtt = mock(Attribute.class);
    when(protocolAtt.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_PROTOCOL);
    when(protocolAtt.getValue()).thenReturn(HTTP_11);
    Attribute schemeAtt = mock(Attribute.class);
    when(schemeAtt.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_SCHEME);
    when(schemeAtt.getValue()).thenReturn(HTTP);
    Attribute portAtt = mock(Attribute.class);
    when(portAtt.getName()).thenReturn(IvyJmxConstant.Ivy.Connector.KEY_PORT);
    when(portAtt.getValue()).thenReturn(PORT_8080);
    AttributeList connectorAttributeList = mock(AttributeList.class);
    List<Attribute> connectorAttributes = new ArrayList<>();
    connectorAttributes.add(protocolAtt);
    connectorAttributes.add(schemeAtt);
    connectorAttributes.add(portAtt);
    when(connectorAttributeList.asList()).thenReturn(connectorAttributes);
    return connectorAttributeList;
  }

  private AttributeList databaseInfoMockData() {
    // mock data for database
    Attribute dbProductName = mock(Attribute.class);
    when(dbProductName.getName()).thenReturn(IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_NAME);
    when(dbProductName.getValue()).thenReturn(DB_PRODUCT_NAME);
    Attribute dbProductVersion = mock(Attribute.class);
    when(dbProductVersion.getName()).thenReturn(
            IvyJmxConstant.IvyServer.DatabasePersistency.KEY_PRODUCT_VERSION);
    when(dbProductVersion.getValue()).thenReturn(DB_PRODUCT_VERSION);
    Attribute dbConnectionURL = mock(Attribute.class);
    when(dbConnectionURL.getName()).
            thenReturn(IvyJmxConstant.IvyServer.DatabasePersistency.KEY_CONNECTION_URL);
    when(dbConnectionURL.getValue()).thenReturn(DB_CONNECTION_URL);
    Attribute dbDriverName = mock(Attribute.class);
    when(dbDriverName.getName()).thenReturn(IvyJmxConstant.IvyServer.DatabasePersistency.KEY_DRIVER_NAME);
    when(dbDriverName.getValue()).thenReturn(DB_DRIVER_NAME);
    Attribute dbUsername = mock(Attribute.class);
    when(dbUsername.getName()).thenReturn(IvyJmxConstant.IvyServer.DatabasePersistency.KEY_USERNAME);
    when(dbUsername.getValue()).thenReturn(DB_USERNAME);
    List<Attribute> dbAttributes = new ArrayList<>();
    dbAttributes.add(dbProductName);
    dbAttributes.add(dbProductVersion);
    dbAttributes.add(dbConnectionURL);
    dbAttributes.add(dbDriverName);
    dbAttributes.add(dbUsername);
    AttributeList dbAttributeList = mock(AttributeList.class);
    when(dbAttributeList.asList()).thenReturn(dbAttributes);
    return dbAttributeList;
  }

  private AttributeList osInfoMockData() {
    Attribute osName = mock(Attribute.class);
    when(osName.getName()).thenReturn(IvyJmxConstant.JavaLang.OperatingSystem.KEY_NAME);
    when(osName.getValue()).thenReturn(OS_NAME);
    Attribute osArch = mock(Attribute.class);
    when(osArch.getName()).thenReturn(IvyJmxConstant.JavaLang.OperatingSystem.KEY_ARCH);
    when(osArch.getValue()).thenReturn(OS_ARCH);
    List<Attribute> serverAttributes = new ArrayList<>();
    serverAttributes.add(osName);
    serverAttributes.add(osArch);
    AttributeList serverAttributeList = mock(AttributeList.class);
    when(serverAttributeList.asList()).thenReturn(serverAttributes);
    return serverAttributeList;
  }

  private AttributeList serverInfoMockData() {
    Attribute version = mock(Attribute.class);
    when(version.getName()).thenReturn(IvyJmxConstant.IvyServer.Server.KEY_VERSION);
    when(version.getValue()).thenReturn(VERSION);
    Attribute buildDate = mock(Attribute.class);
    when(buildDate.getName()).thenReturn(IvyJmxConstant.IvyServer.Server.KEY_BUILD_DATE);
    when(buildDate.getValue()).thenReturn(BUILD_DATE);
    Attribute installDir = mock(Attribute.class);
    when(installDir.getName()).thenReturn(IvyJmxConstant.IvyServer.Server.KEY_INSTALLATION_DIRECTORY);
    when(installDir.getValue()).thenReturn(INSTALL_DIR);
    Attribute devMode = mock(Attribute.class);
    when(devMode.getName()).thenReturn(IvyJmxConstant.IvyServer.Server.KEY_DEVELOPER_MODE);
    when(devMode.getValue()).thenReturn(DEV_MODE);
    Attribute releaseCan = mock(Attribute.class);
    when(releaseCan.getName()).thenReturn(IvyJmxConstant.IvyServer.Server.KEY_RELEASE_CANDIDATE);
    when(releaseCan.getValue()).thenReturn(RELEASE_CANDIDATE);
    Attribute appName = mock(Attribute.class);
    when(appName.getName()).thenReturn(IvyJmxConstant.IvyServer.Server.KEY_APPLICATION_NAME);
    when(appName.getValue()).thenReturn(APPLICATION_NAME);
    List<Attribute> serverAttributes = new ArrayList<>();
    serverAttributes.add(version);
    serverAttributes.add(buildDate);
    serverAttributes.add(installDir);
    serverAttributes.add(devMode);
    serverAttributes.add(releaseCan);
    serverAttributes.add(appName);
    AttributeList serverAttributeList = mock(AttributeList.class);
    when(serverAttributeList.asList()).thenReturn(serverAttributes);
    return serverAttributeList;
  }

}
