package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.test.data.model.MBeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import java.net.URISyntaxException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LicenseInfoTest extends AbstractTest {

  private final String fHtmlLicenseInfo;

  /* CHECKSTYLE:OFF */
  @Parameterized.Parameters(name = "{index}: {2}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    Iterable<Object[]> data = TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/LicenseInfoTest.xml",
            new Object[]{
              "<html><body style=\"font-family:tahoma;font-size:11\"><table border='0' celspacing='10' celpadding='0'><tr><td>Organization: <td><td>Axon Active Vietnam</td></tr><tr><td>Individual: <td><td>Tung Le</td></tr><tr><td>Host Name: <td><td>aavn-ws-147</td></tr><tr><td>Version: <td><td>5000</td></tr><tr><td>Valid From: <td><td>Tuesday, June 4, 2013</td></tr><tr><td>Expires: <td><td>Monday, June 30, 2014</td></tr><tr><td>Supports RIA: <td><td>yes</td></tr><tr><td>Elements Limit: <td><td>10</td></tr><tr><td>Named Users Limit: <td><td>10</td></tr><tr><td>Concurrent Users Limit: <td><td>10</td></tr></table></body></html>",
              "Tung Le's license"},
            new Object[]{
              "<html><body style=\"font-family:tahoma;font-size:11\"><table border='0' celspacing='10' celpadding='0'><tr><td>Organization: <td><td>Axon Active Vietnam</td></tr><tr><td>Individual: <td><td>Tam Thai</td></tr><tr><td>Host Name: <td><td>aavn-ws-175</td></tr><tr><td>Version: <td><td>5000</td></tr><tr><td>Valid From: <td><td>Tuesday, June 4, 2013</td></tr><tr><td>Expires: <td><td>Monday, June 30, 2014</td></tr><tr><td>Supports RIA: <td><td>no</td></tr></table></body></html>",
              "Tam Thai's license"});
    return data;
  }
  /* CHECKSTYLE:ON */

  public LicenseInfoTest(MBeanTestData.Dataset dataset, String htmlLicenseInfo,
          @SuppressWarnings("unused") String description) {
    super(dataset);
    fHtmlLicenseInfo = htmlLicenseInfo;
  }

  @Test
  public void testHtmlLicenseInfo() throws MalformedObjectNameException, IvyJmxDataCollectException,
          InstanceNotFoundException, ReflectionException {
    MBeanServerConnection mockedMBeanServer = createMockConnection();
    addTestData(mockedMBeanServer, getDataset());
    String licenseInfo = new BasicIvyJmxDataCollector().getLicenseInfo(mockedMBeanServer).toHTMLString();
    assertEquals(fHtmlLicenseInfo, licenseInfo);
  }

}
