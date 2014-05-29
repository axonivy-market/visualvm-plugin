package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.Calendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DataUtilsTest {

//  private static final Logger LOGGER = Logger.getLogger(DataUtilsTest.class.getName());
  private static final String XPERT_IVY_SYSTEM_DATABASE = "XpertIvySystemDatabase";

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
  public void testGetHostFromConnectionUrl() {
    assertEquals("localhost", DataUtils.getHostFromConnectionUrl(
            "jdbc:mysql://localhost:5432/XpertIvySystemDatabase"));
    assertEquals("localhost", DataUtils.getHostFromConnectionUrl(
            "jdbc:oracle:thin:tam/thai@localhost:1521:XpertIvySystemDatabase"));
    assertEquals(null, DataUtils.getHostFromConnectionUrl("jdbc:oracle:oci:@XpertIvySystemDatabase"));
    assertEquals(null, DataUtils.getHostFromConnectionUrl("jdbc:db2:XpertIvySystemDatabase"));
    assertEquals("localhost", DataUtils.getHostFromConnectionUrl(
            "jdbc:microsoft:sqlserver://localhost:1443;databaseName= XpertIvySystemDatabase;"
            + "SelectMethod=cursor"));
    assertEquals(null, DataUtils.getHostFromConnectionUrl("jdbc:hsqldb:mem:XpertIvySystemDatabase"));
  }

  @Test
  public void testGetPortFromConnectionUrl() {
    assertEquals("5432", DataUtils.getPortFromConnectionUrl(
            "jdbc:mysql://localhost:5432/XpertIvySystemDatabase"));
    assertEquals("1521", DataUtils.getPortFromConnectionUrl(
            "jdbc:oracle:thin:tam/thai@localhost:1521:XpertIvySystemDatabase"));
    assertEquals(null, DataUtils.getPortFromConnectionUrl("jdbc:oracle:oci:@XpertIvySystemDatabase"));
    assertEquals(null, DataUtils.getPortFromConnectionUrl("jdbc:db2:XpertIvySystemDatabase"));
    assertEquals("1443", DataUtils.getPortFromConnectionUrl(
            "jdbc:microsoft:sqlserver://localhost:1443;databaseName=XpertIvySystemDatabase;"
            + "SelectMethod=cursor"));
    assertEquals(null, DataUtils.getPortFromConnectionUrl("jdbc:hsqldb:mem:XpertIvySystemDatabase"));
  }

  @Test
  public void testGetSchemaFromConnectionUrl() {
    assertEquals(XPERT_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:mysql://localhost:5432/XpertIvySystemDatabase"));
    assertEquals(XPERT_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:oracle:thin:tam/thai@localhost:1521:XpertIvySystemDatabase"));
    assertEquals(XPERT_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:oracle:oci:@XpertIvySystemDatabase"));
    assertEquals(XPERT_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:db2:XpertIvySystemDatabase"));
    assertEquals(XPERT_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:microsoft:sqlserver://localhost:1443;databaseName=XpertIvySystemDatabase;"
            + "SelectMethod=cursor"));
    assertEquals(XPERT_IVY_SYSTEM_DATABASE, DataUtils.getSchemaFromConnectionUrl(
            "jdbc:hsqldb:mem:XpertIvySystemDatabase"));
  }

}
