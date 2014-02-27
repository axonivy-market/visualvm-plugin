package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.test.data.model.MBeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.TabularDataSupport;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LicenseInfoTest extends AbstractTest {

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
            "/ch/ivyteam/ivy/visualvm/test/LicenseInfoTest.xml");
    return data;
  }

  public LicenseInfoTest(MBeanTestData.Dataset dataset) {
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
  public void testInfo() throws MalformedObjectNameException,
          IOException, IvyJmxDataCollectException, IOException,
          InstanceNotFoundException, ReflectionException, MBeanException, AttributeNotFoundException {

    MBeanServerConnection mockedMBeanServer = createMockConnection();
    addTestData(mockedMBeanServer, getDataset());
    ObjectName objectName = new ObjectName("Xpert.ivy Server:type=Server");
    TabularDataSupport tabular = (TabularDataSupport) mockedMBeanServer.getAttribute(objectName,
            "licenceParameters");
//    LicenseUtils.getLicenseDetail(tabular, "host.name");
    System.out.println(new BasicIvyJmxDataCollector().getLicenseInfo(mockedMBeanServer).toHTMLString());
  }

}
