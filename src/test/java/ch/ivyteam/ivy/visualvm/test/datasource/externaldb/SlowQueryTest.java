package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
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
import java.util.HashSet;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.JAXBException;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SlowQueryTest extends AbstractTest {
  private static final int MAX_BUFFER_ITEMS = 50;
  private static DataBeanProvider fProvider;
  private static ExternalDbSlowQueryBuffer fBuffer;

  private final int fExpectedNumOfSlows;

  public SlowQueryTest(BeanTestData.Dataset dataset, int max) {
    super(dataset);
    fExpectedNumOfSlows = max;
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb/ComplicatedSlowTest.xml",
            new Object[]{50}
    );
  }

  @Test
  public void testLimit() throws MalformedObjectNameException, IOException {
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
      fBuffer = new ExternalDbSlowQueryBuffer(mockConnection, MAX_BUFFER_ITEMS);
    }
    Query query = new Query();
    fBuffer.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    fBuffer.updateValues(result);

    // Assert the buffer
    assertEquals(fExpectedNumOfSlows, fBuffer.getBuffer().size());
    // refresh several times
    for (int index = 0; index < 10; index++) {
      fBuffer.updateQuery(query);
      result = query.execute(mockConnection);
      fBuffer.updateValues(result);
      assertEquals(fExpectedNumOfSlows, fBuffer.getBuffer().size());
    }

  }

  @Test
  public void testNoDuplicate() throws MalformedObjectNameException, IOException {
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
      fBuffer = new ExternalDbSlowQueryBuffer(mockConnection, MAX_BUFFER_ITEMS);
    }
    Query query = new Query();
    fBuffer.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    fBuffer.updateValues(result);

    // Assert the buffer
    assertEquals(fExpectedNumOfSlows, fBuffer.getBuffer().size());
    // refresh several times
    for (int index = 0; index < 10; index++) {
      fBuffer.updateQuery(query);
      result = query.execute(mockConnection);
      fBuffer.updateValues(result);
      assertEquals(fExpectedNumOfSlows, fBuffer.getBuffer().size());

      for (int i = 0; i < fBuffer.getBuffer().size() - 1; i++) {
        for (int j = i + 1; j < fBuffer.getBuffer().size(); j++) {
          assertFalse(fBuffer.getBuffer().get(i).equals(fBuffer.getBuffer().get(j)));
        }
      }
    }
  }

}
