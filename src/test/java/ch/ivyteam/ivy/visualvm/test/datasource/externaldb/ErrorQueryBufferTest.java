package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.SQLErrorInfo;
import ch.ivyteam.ivy.visualvm.service.ExternalDbErrorQueryBuffer;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ErrorQueryBufferTest extends AbstractTest {
  private static final int MAX_BUFFER_ITEMS = 2;
  private static DataBeanProvider fProvider;
  private static ExternalDbErrorQueryBuffer fBuffer;

  private final List<SQLErrorInfo> fExpectedSQLErrors;
  private final int fExpectedNumOfErrors;

  public ErrorQueryBufferTest(BeanTestData.Dataset dataset, int numErrors, List<SQLErrorInfo> sqlErrorInfos) {
    super(dataset);
    fExpectedSQLErrors = sqlErrorInfos;
    fExpectedNumOfErrors = numErrors;
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    SQLErrorInfo sqlErrorInfo1 = new SQLErrorInfo();
    sqlErrorInfo1.setApplication("app1");
    sqlErrorInfo1.setEnvironment("env1");
    sqlErrorInfo1.setConfigName("config1");
    sqlErrorInfo1.setProcessElementId("144918C0A385EAC6-f7-bean");
    sqlErrorInfo1.setTime(dateFormat.parse("28/05/2014 10:45:11"));
    sqlErrorInfo1.setErrorMessage("ERROR: value too long for type character varying(30)");
    sqlErrorInfo1.setStacktrace("org.postgresql.util.PSQLException: ERROR: value too long");
    sqlErrorInfo1.setStatement(
            "INSERT INTO authors (last_name, first_name)"
            + " VALUES ('\"last_name of insert over 30 characters\"',"
            + " '\"first_name of insert over 30 characters\"')");

    SQLErrorInfo sqlErrorInfo2 = new SQLErrorInfo();
    sqlErrorInfo2.setApplication("app1");
    sqlErrorInfo2.setEnvironment("env1");
    sqlErrorInfo2.setConfigName("config1");
    sqlErrorInfo2.setProcessElementId("144918C0A385EAC6-f7-bean");
    sqlErrorInfo2.setTime(dateFormat.parse("28/05/2014 11:35:32"));
    sqlErrorInfo2.setErrorMessage("ERROR: value too long for type character varying(30)");
    sqlErrorInfo2.setStacktrace("org.postgresql.util.PSQLException: ERROR: value too long");
    sqlErrorInfo2.setStatement(
            "INSERT INTO authors (last_name, first_name)"
            + " VALUES ('\"last_name of insert 2 over 30 characters\"',"
            + " '\"first_name of insert over 30 characters\"')");

    SQLErrorInfo sqlErrorInfo3 = new SQLErrorInfo();
    sqlErrorInfo3.setApplication("app1");
    sqlErrorInfo3.setEnvironment("env1");
    sqlErrorInfo3.setConfigName("config1");
    sqlErrorInfo3.setProcessElementId("144918C0A385EAC6-f7-bean");
    sqlErrorInfo3.setTime(dateFormat.parse("28/05/2014 14:57:41"));
    sqlErrorInfo3.setErrorMessage("ERROR: value too long for type character varying(30)");
    sqlErrorInfo3.setStacktrace("org.postgresql.util.PSQLException: ERROR: value too long");
    sqlErrorInfo3.setStatement(
            "INSERT INTO authors (last_name, first_name)"
            + " VALUES ('\"last_name of insert 3 over 30 characters\"',"
            + " '\"first_name of insert over 30 characters\"')");

    List<SQLErrorInfo> sqlErrorInfos1 = new ArrayList<>();
    sqlErrorInfos1.add(sqlErrorInfo1);

    List<SQLErrorInfo> sqlErrorInfos2 = new ArrayList<>();
    sqlErrorInfos2.add(sqlErrorInfo1);
    sqlErrorInfos2.add(sqlErrorInfo2);

    List<SQLErrorInfo> sqlErrorInfos3 = new ArrayList<>();
    sqlErrorInfos3.add(sqlErrorInfo2);
    sqlErrorInfos3.add(sqlErrorInfo3);

    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb/ErrorQueryTest.xml",
            new Object[]{1, sqlErrorInfos1},
            new Object[]{2, sqlErrorInfos2},
            new Object[]{2, sqlErrorInfos3}
    );
  }

  @Test
  public void testDBErrorQueriesBuffer() throws MalformedObjectNameException, IOException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    Set<ObjectName> extDbObjNames = new HashSet<>();
    extDbObjNames.add(new ObjectName(
            "Xpert.ivy Server:type=External Database,application=app1,environment=env1,name=config1"));
    extDbObjNames.add(new ObjectName(
            "Xpert.ivy Server:type=External Database,application=app1,environment=env1,name=config2"));
    when(mockConnection.queryNames(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_FILTER, null))
            .thenReturn(extDbObjNames);

    if (fProvider == null) {
      fProvider = mock(DataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fBuffer == null) {
      fBuffer = new ExternalDbErrorQueryBuffer(mockConnection, MAX_BUFFER_ITEMS);
    }
    Query query = new Query();
    fBuffer.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    fBuffer.updateValues(result);

    // Assert the buffer
    assertEquals(fExpectedNumOfErrors, fBuffer.getBuffer().size());
    for (int index = 0; index < fExpectedNumOfErrors; index++) {
      SQLErrorInfo expectedError = fExpectedSQLErrors.get(index);
      SQLErrorInfo actualError = fBuffer.getBuffer().get(index);
      assertEquals(expectedError, actualError);
      assertEquals(expectedError.getTime(), actualError.getTime());
      assertEquals(expectedError.getStatement(), actualError.getStatement());
      assertEquals(expectedError.getStacktrace(), actualError.getStacktrace());
      assertEquals(expectedError.getErrorMessage(), actualError.getErrorMessage());
      assertEquals(expectedError.getProcessElementId(), actualError.getProcessElementId());
    }

  }

}
