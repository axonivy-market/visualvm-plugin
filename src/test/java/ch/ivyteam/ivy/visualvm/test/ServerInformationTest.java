package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.exception.IvyVisualVMRuntimeException;
import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.model.OSInfo;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.model.SystemDatabaseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ServerInformationTest extends AbstractTest {

  private static final String VERSION_CUSTOM = "5.1.0 (revision 44832)";
  private static final String INSTALL_DIR = "D:\\teamup\\XpertIvyServer5.1.0.44832_Windows_x64";
  private static final String APPLICATION_NAME = "Xpert.ivy Server";
  private static final String DB_PRODUCT_NAME = "MySQL";
  private static final String DB_PRODUCT_VERSION = "5.5.21";
  private static final String DB_IVY_SYSDB_VERSION = "33";
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
  private static final String OS_NAME_CUSTOM = "Windows Server 2008 (64bit)";

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    Iterable<Object[]> data = TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/ServerInformationTest.xml");
    return data;
  }

  public ServerInformationTest(BeanTestData.Dataset dataset) {
    super(dataset);
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
  @Test
  public void testServerInformation() throws MalformedObjectNameException,
          IOException, IvyJmxDataCollectException, InstanceNotFoundException, ReflectionException {

    MBeanServerConnection mockedMBeanServer = createMockConnection();
    addTestData(mockedMBeanServer, getDataset());
    Set<ObjectName> connectorObjNames = new HashSet<>();
    connectorObjNames.add(new ObjectName("ivy:type=Connector,port=8009"));
    connectorObjNames.add(new ObjectName("ivy:type=Connector,port=8080"));
    connectorObjNames.add(new ObjectName("ivy:type=Connector,port=8443"));
    when(mockedMBeanServer.queryNames(new ObjectName("ivy:type=Connector,port=*"), null))
            .thenReturn(connectorObjNames);

    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    // retrieve information of server, database and connectors
    IvyApplicationInfo basicInformation = collector.getApplicationInfo(mockedMBeanServer);
    //verify server info
    assertEquals(VERSION_CUSTOM, basicInformation.getVersion());
    assertEquals(APPLICATION_NAME, basicInformation.getApplicationName());
    try {
      assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("11/09/2014"), basicInformation.getBuildDate());
    } catch (ParseException ex) {
      throw new IvyVisualVMRuntimeException(ex);
    }
    assertEquals(INSTALL_DIR, basicInformation.getInstallationDirectory());
    // verify os info
    OSInfo osInfo = collector.getOSInfo(mockedMBeanServer);
    assertEquals(OS_NAME_CUSTOM, osInfo.getName());
    // verify database info
    SystemDatabaseInfo sysDbInfo = collector.getSystemDatabaseInfo(mockedMBeanServer);
    assertEquals(DB_PRODUCT_NAME, sysDbInfo.getType());
    assertEquals(DB_PRODUCT_VERSION, sysDbInfo.getVersion());
    assertEquals(DB_IVY_SYSDB_VERSION, sysDbInfo.getIvySystemDbVersion());
    assertEquals(DB_DRIVER_NAME, sysDbInfo.getDriver());
    assertEquals(DB_CONNECTION_URL, sysDbInfo.getConnectionUrl());
    assertEquals(DB_USERNAME, sysDbInfo.getUsername());

//     verify connectors
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

}
