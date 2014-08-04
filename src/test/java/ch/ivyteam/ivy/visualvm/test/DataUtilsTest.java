package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.Calendar;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DataUtilsTest {

//  private static final Logger LOGGER = Logger.getLogger(DataUtilsTest.class.getName());
  private static final String AXON_IVY_SYSTEM_DATABASE = "AxonIvySystemDatabase";

  public DataUtilsTest() {
  }

  @Test
  public void testGetIvyServerVersion() {
    assertEquals("5.1.0 (revision 44657)", DataUtils.getIvyServerVersion("5.1.0.44657"));
  }

  @Test
  public void testCheckIvyVersion() {
    assertTrue(DataUtils.checkIvyVersion("10.1.0.44657", 5, 1));
    assertTrue(DataUtils.checkIvyVersion("5.2.0.44657", 5, 1));
    assertTrue(DataUtils.checkIvyVersion("5.1.0.44657", 5, 1));
    assertFalse(DataUtils.checkIvyVersion("5.0.0.44657", 5, 1));
    assertFalse(DataUtils.checkIvyVersion("4.3.0.44657", 5, 1));

    assertTrue(DataUtils.checkIvyVersion("10", 5, 1));
    assertFalse(DataUtils.checkIvyVersion(null, 5, 1));
    assertFalse(DataUtils.checkIvyVersion("4", 5, 1));
    assertFalse(DataUtils.checkIvyVersion("5", 5, 1));
    assertFalse(DataUtils.checkIvyVersion("5.0", 5, 1));
    assertTrue(DataUtils.checkIvyVersion("5.1", 5, 1));
    assertTrue(DataUtils.checkIvyVersion("5.2", 5, 1));
  }

  @Test
  public void testGetFullOSName() {
    assertEquals("Windows 7 (64bit)", DataUtils.getFullOSName("Windows 7", "amd64"));

  }

  @Test
  public void testGetHostNameFromRuntimeId() {
    assertEquals("AAVN-WS-04", DataUtils.getHostNameFromRuntimeId("1234@AAVN-WS-04"));
  }

  @Test
  public void testToDateString() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2014, 1, 14);
    assertEquals("Friday, February 14, 2014", DataUtils.dateToString(calendar.getTime()));
  }

  @Test
  public void testDateTimeToString() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2014, 1, 13,8,12,56);
    Locale formatLocale = DataUtils.getFormatLocale();
    if ("de_CH".equals(formatLocale.toString())){
      assertEquals("13.01.14 08:12:56", DataUtils.dateTimeToString(calendar.getTime()));
    }else if ("en_US".equals(""+formatLocale.toString())){
      assertEquals("2/13/14 8:12:56 AM", DataUtils.dateTimeToString(calendar.getTime()));
    }else if ("es_US".equals(""+formatLocale.toString())){
      assertEquals("2/13/14 8:12:56 a.m.", DataUtils.dateTimeToString(calendar.getTime()));
    }
  }
  
  @Test
  public void testGetHostFromConnectionUrl() {
    assertEquals("localhost", DataUtils.getHostFromConnectionUrl(
            "jdbc:mysql://localhost:5432/AxonIvySystemDatabase"));
    assertEquals("localhost", DataUtils.getHostFromConnectionUrl(
            "jdbc:oracle:thin:tam/thai@localhost:1521:AxonIvySystemDatabase"));
    assertEquals(null, DataUtils.getHostFromConnectionUrl("jdbc:oracle:oci:@AxonIvySystemDatabase"));
    assertEquals(null, DataUtils.getHostFromConnectionUrl("jdbc:db2:AxonIvySystemDatabase"));
    assertEquals("localhost", DataUtils.getHostFromConnectionUrl(
            "jdbc:microsoft:sqlserver://localhost:1443;databaseName= AxonIvySystemDatabase;"
            + "SelectMethod=cursor"));
    assertEquals(null, DataUtils.getHostFromConnectionUrl("jdbc:hsqldb:mem:AxonIvySystemDatabase"));
  }

  @Test
  public void testGetPortFromConnectionUrl() {
    assertEquals("5432", DataUtils.getPortFromConnectionUrl(
            "jdbc:mysql://localhost:5432/AxonIvySystemDatabase"));
    assertEquals("1521", DataUtils.getPortFromConnectionUrl(
            "jdbc:oracle:thin:tam/thai@localhost:1521:AxonIvySystemDatabase"));
    assertEquals(null, DataUtils.getPortFromConnectionUrl("jdbc:oracle:oci:@AxonIvySystemDatabase"));
    assertEquals(null, DataUtils.getPortFromConnectionUrl("jdbc:db2:AxonIvySystemDatabase"));
    assertEquals("1443", DataUtils.getPortFromConnectionUrl(
            "jdbc:microsoft:sqlserver://localhost:1443;databaseName=AxonIvySystemDatabase;"
            + "SelectMethod=cursor"));
    assertEquals(null, DataUtils.getPortFromConnectionUrl("jdbc:hsqldb:mem:AxonIvySystemDatabase"));
  }

  @Test
  public void testGetSchemaFromConnectionUrl() {
    assertEquals(AXON_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:mysql://localhost:5432/AxonIvySystemDatabase"));
    assertEquals(AXON_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:oracle:thin:tam/thai@localhost:1521:AxonIvySystemDatabase"));
    assertEquals(AXON_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:oracle:oci:@AxonIvySystemDatabase"));
    assertEquals(AXON_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:db2:AxonIvySystemDatabase"));
    assertEquals(AXON_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:microsoft:sqlserver://localhost:1443;databaseName=AxonIvySystemDatabase;"
            + "SelectMethod=cursor"));
    assertEquals(AXON_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:hsqldb:mem:AxonIvySystemDatabase"));
  }

}
