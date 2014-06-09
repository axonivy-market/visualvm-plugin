package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
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
import java.util.Date;
import java.util.HashSet;
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
public class ErrorQueryTest extends AbstractTest {
  private static final int MAX_BUFFER_ITEMS = 50;
  private static DataBeanProvider fProvider;
  private static ExternalDbErrorQueryBuffer fBuffer;
  private final int fExpectedNumOfErrors;
  private static final Date fOldestQuery = new Date(0);

  public ErrorQueryTest(BeanTestData.Dataset dataset, int max) {
    super(dataset);
    fExpectedNumOfErrors = max;
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb/ComplicatedErrorTest.xml",
            new Object[]{50},
            new Object[]{50},
            new Object[]{50}
    );
  }

  @Test
  public void testHandleDuplicateAndSort() throws MalformedObjectNameException, IOException {
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

    // Assert the buffer size
    assertEquals(fExpectedNumOfErrors, fBuffer.getBuffer().size());

    // Assert the buffer changes
    assertTrue(fOldestQuery.before(fBuffer.getBuffer().get(0).getTime()));
    fOldestQuery.setTime(fBuffer.getBuffer().get(0).getTime().getTime());

    // Assert the buffer duplication and sort
    for (int i = 0; i < fBuffer.getBuffer().size() - 1; i++) {
      for (int j = i + 1; j < fBuffer.getBuffer().size(); j++) {
        assertFalse(fBuffer.getBuffer().get(i).equals(fBuffer.getBuffer().get(j)));
        assertTrue(fBuffer.getBuffer().get(i).getTime().before(fBuffer.getBuffer().get(j).getTime()));
      }
    }
  }

}
