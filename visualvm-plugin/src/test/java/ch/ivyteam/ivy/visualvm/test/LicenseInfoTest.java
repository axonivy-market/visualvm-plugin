package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.LicenseInformationPanel;
import java.net.URISyntaxException;
import java.util.Date;
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

  /*
   * CHECKSTYLE:OFF
   */
  @Parameterized.Parameters(name = "{index}: {2}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    Iterable<Object[]> data = TestUtil.createTestData("/ch/ivyteam/ivy/visualvm/test/LicenseInfoTest.xml",
            new Object[]{
              "<tr style='white-space: nowrap;'><td>Organization: </td><td>Axon Active Vietnam</td></tr><tr style='white-space: nowrap;'><td>Individual: </td><td>Tung Le</td></tr><tr style='white-space: nowrap;'><td>Host Name: </td><td>aavn-ws-147</td></tr><tr style='white-space: nowrap;'><td>Version: </td><td>5100</td></tr><tr style='white-space: nowrap;'><td>Valid From: </td><td>Tuesday, June 4, 2013</td></tr><tr style='white-space: nowrap;'><td>Expires: </td><td>Monday, June 30, 2014</td></tr><tr style='white-space: nowrap;'><td>Supports RIA: </td><td>Yes</td></tr><tr style='white-space: nowrap;'><td>Elements Limit: </td><td>10</td></tr><tr style='white-space: nowrap;'><td>Named Users Limit: </td><td>10</td></tr><tr style='white-space: nowrap;'><td>Concurrent Users Limit: </td><td>10</td></tr>",
              "Limitted license"},
            new Object[]{
              "<html><body style=\"font-family:tahoma;font-size:11\"><table border='0' celspacing='5' celpadding='0'><tr style='white-space: nowrap;'><td>Organization: </td><td>Axon Active Vietnam</td></tr><tr style='white-space: nowrap;'><td>Individual: </td><td>Tam Thai</td></tr><tr style='white-space: nowrap;'><td>Host Name: </td><td>aavn-ws-175</td></tr><tr style='white-space: nowrap;'><td>Version: </td><td>5100</td></tr><tr style='white-space: nowrap;'><td>Valid From: </td><td>Tuesday, June 4, 2013</td></tr><tr style='white-space: nowrap;'><td>Expires: </td><td>Monday, June 30, 2014</td></tr><tr style='white-space: nowrap;'><td>Supports RIA: </td><td>No</td></tr></table></body></html>",
              "Un-limitted license"},
            new Object[]{
              "Your license will expire on Tuesday, April 15, 2014. If the license is expired the server will no longer start. Please contact your sales representative to request a new license!",
              "Expired-in-30-days license"},
            new Object[]{
              "Your license has expired on Tuesday, March 25, 2014. You will not be able to restart your server. Contact your sales representative to request a new license!",
              "Expired license"},
            new Object[]{
              "The number of users is close to the limit. Please contact your sales representative to request a new license!",
              "Named users limit nearly exceeded (reach yellow area in the gauge)"},
            new Object[]{
              "The number of users has almost reached the license limit. Please contact your sales representative to request a new license!",
              "Named users limit almost exceeded (reach red area in the gauge)"},
            new Object[]{
              "Cannot create more users because the maximum users that are allowed by your license has exceeded. Contact your sales representative to request a new license!",
              "Named users limit exceeded (reach the end of the the gauge)"},
            new Object[]{
              "The number of sessions has almost reached the license limit. Please contact your sales representative to request a new license!",
              "Concurrent users limit almost exceeded (reach yellow area in the gauge)"},
            new Object[]{
              "The maximum sessions that are allowed by your license has been exceeded. Contact your sales representative to request a new license!",
              "Concurrent users limit exceeded (reach red area in the gauge)"},
            new Object[]{
              "Cannot create more sessions because the maximum session that are allowed by your license has exceeded by a factor of 50%. Contact your sales representative to request a new license!",
              "Concurrent users limit exceeded 50% (reach the end of the gauge)"});
    return data;
  }

  /*
   * CHECKSTYLE:ON
   */
  public LicenseInfoTest(BeanTestData.Dataset dataset, String htmlLicenseInfo,
          @SuppressWarnings("unused") String description) {
    super(dataset);
    fHtmlLicenseInfo = htmlLicenseInfo;
  }

  @Test
  public void testHtmlLicenseInfo() throws MalformedObjectNameException, IvyJmxDataCollectException,
          InstanceNotFoundException, ReflectionException {
    MBeanServerConnection mockedMBeanServer = createMockConnection();
    addTestData(mockedMBeanServer, getDataset());
    IvyLicenseInfo licenseInfo = new BasicIvyJmxDataCollector().getLicenseInfo(mockedMBeanServer);
    Date currentDate = DataUtils.stringToDate("2014-03-26");
    long remainingTime = Long.MAX_VALUE;
    if (licenseInfo.getLicenseValidUntil() != null) {
      remainingTime = licenseInfo.getLicenseValidUntil().getTime() - currentDate.getTime();
    }
    LicenseInformationPanel infoPanel = new LicenseInformationPanel(licenseInfo);
    infoPanel.setLicenseData(remainingTime, 5, 9);
    String htmlLicenseInfo = infoPanel.toHTMLString();
    assertTrue(htmlLicenseInfo.contains(fHtmlLicenseInfo));
  }

}
