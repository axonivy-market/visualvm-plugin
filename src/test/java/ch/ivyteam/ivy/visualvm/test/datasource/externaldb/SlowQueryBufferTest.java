package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import ch.ivyteam.ivy.visualvm.service.ExternalDbSlowQueryBuffer;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.addTestData;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.createMockConnection;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.JAXBException;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SlowQueryBufferTest extends AbstractTest {
  private static final int MAX_BUFFER_ITEMS = 2;
  private static DataBeanProvider fProvider;
  private static ExternalDbSlowQueryBuffer fBuffer;

  private final List<SQLInfo> fExpectedSlowSQLs;
  private final int fExpectedNumOfSlowSQLs;

  public SlowQueryBufferTest(BeanTestData.Dataset dataset, int numErrors, List<SQLInfo> sqlInfo) {
    super(dataset);
    fExpectedSlowSQLs = sqlInfo;
    fExpectedNumOfSlowSQLs = numErrors;
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    SQLInfo sqlInfo1 = new SQLInfo();
    sqlInfo1.setApplication("app1");
    sqlInfo1.setEnvironment("env1");
    sqlInfo1.setConfigName("config1");
    sqlInfo1.setProcessElementId("144918C0A385EAC6-f7-bean");
    sqlInfo1.setTime(dateFormat.parse("28/05/2014 10:45:11"));
    sqlInfo1.setExecutionTime(123456);
    sqlInfo1.setStatement(
            "INSERT INTO authors (last_name, first_name) VALUES ('\"upteam\"', '\"ivyteam\"')");

    SQLInfo sqlInfoConfig2 = new SQLInfo();
    sqlInfoConfig2.setApplication("app1");
    sqlInfoConfig2.setEnvironment("env1");
    sqlInfoConfig2.setConfigName("config2");
    sqlInfoConfig2.setProcessElementId("144918C0A385EAC6-f7-bean");
    sqlInfoConfig2.setTime(dateFormat.parse("28/05/2014 10:45:12"));
    sqlInfoConfig2.setExecutionTime(123456);
    sqlInfoConfig2.setStatement(
            "INSERT INTO authors (last_name, first_name) VALUES ('\"upteam2\"', '\"ivyteam2\"')");

    SQLInfo sqlInfo2 = new SQLInfo();
    sqlInfo2.setApplication("app1");
    sqlInfo2.setEnvironment("env1");
    sqlInfo2.setConfigName("config1");
    sqlInfo2.setProcessElementId("144918C0A385EAC6-f7-bean");
    sqlInfo2.setTime(dateFormat.parse("28/05/2014 11:35:32"));
    sqlInfo2.setExecutionTime(123457);
    sqlInfo2.setStatement("INSERT INTO authors (last_name, first_name) VALUES ('\"up\"', '\"ivy\"')");

    SQLInfo sqlInfo3 = new SQLInfo();
    sqlInfo3.setApplication("app1");
    sqlInfo3.setEnvironment("env1");
    sqlInfo3.setConfigName("config1");
    sqlInfo3.setProcessElementId("144918C0A385EAC6-f7-bean");
    sqlInfo3.setTime(dateFormat.parse("28/05/2014 14:57:41"));
    sqlInfo3.setExecutionTime(123458);
    sqlInfo3.setStatement("INSERT INTO authors (last_name, first_name) VALUES ('\"up\"', '\"team\"')");

    List<SQLInfo> sqlInfos1 = new ArrayList<>();
    sqlInfos1.add(sqlInfo1);
    sqlInfos1.add(sqlInfoConfig2);

    List<SQLInfo> sqlInfos2 = new ArrayList<>();
    sqlInfos2.add(sqlInfoConfig2);
    sqlInfos2.add(sqlInfo2);

    List<SQLInfo> sqlInfos3 = new ArrayList<>();
    sqlInfos3.add(sqlInfo2);
    sqlInfos3.add(sqlInfo3);

    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb/SlowQueryTest.xml",
            new Object[]{2, sqlInfos1},
            new Object[]{2, sqlInfos2},
            new Object[]{2, sqlInfos3}
    );
  }

  @Test
  public void testCollectData() throws MalformedObjectNameException, IOException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    Set<ObjectName> extDbObjNames = new HashSet<>();
    extDbObjNames.add(new ObjectName(
            "ivy Engine:type=External Database,application=app1,environment=env1,name=config1"));
    extDbObjNames.add(new ObjectName(
            "ivy Engine:type=External Database,application=app1,environment=env1,name=config2"));
    when(mockConnection.queryNames(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_FILTER, null))
            .thenReturn(extDbObjNames);

    if (fProvider == null) {
      fProvider = mock(DataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fBuffer == null) {
      fBuffer = new ExternalDbSlowQueryBuffer(mockConnection, MAX_BUFFER_ITEMS);
    }
    Query query = new Query();
    fBuffer.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    fBuffer.updateValues(result);

    // Assert the buffer
    assertEquals(fExpectedNumOfSlowSQLs, fBuffer.getBuffer().size());
    for (int index = 0; index < fExpectedNumOfSlowSQLs; index++) {
      SQLInfo expectedSlow = fExpectedSlowSQLs.get(index);
      SQLInfo actualSlow = fBuffer.getBuffer().get(index);
      assertEquals(expectedSlow, actualSlow);
      assertEquals(expectedSlow.getTime(), actualSlow.getTime());
      assertEquals(expectedSlow.getStatement(), actualSlow.getStatement());
      assertEquals(expectedSlow.getExecutionTime(), actualSlow.getExecutionTime());
      assertEquals(expectedSlow.getProcessElementId(), actualSlow.getProcessElementId());
    }

  }

}
