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
import java.text.SimpleDateFormat;
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
public class SlowQueryTest extends AbstractTest {
  private static final int MAX_BUFFER_ITEMS = 50;
  private static DataBeanProvider fProvider;
  private static ExternalDbSlowQueryBuffer fBuffer;
  private final int fExpectedNumOfSlows;
  private final Date fFirstDate = new Date();
  private final Date fLastDate = new Date();

  public SlowQueryTest(BeanTestData.Dataset dataset, int numOfQueries, Date firstDate, Date lastDate) {
    super(dataset);
    fExpectedNumOfSlows = numOfQueries;
    fFirstDate.setTime(firstDate.getTime());
    fLastDate.setTime(lastDate.getTime());
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb/ComplicatedSlowTest.xml",
            new Object[]{48, format.parse("28/05/2014 00:00:00"), format.parse("28/05/2014 00:00:47")},
            new Object[]{50, format.parse("28/05/2014 00:00:06"), format.parse("28/05/2014 00:00:55")},
            new Object[]{50, format.parse("28/05/2014 00:01:00"), format.parse("28/05/2014 00:00:58")}
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
      fBuffer = new ExternalDbSlowQueryBuffer(mockConnection, MAX_BUFFER_ITEMS);
    }
    Query query = new Query();
    fBuffer.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    fBuffer.updateValues(result);

    // Assert the buffer size
    assertEquals(fExpectedNumOfSlows, fBuffer.getBuffer().size());

    // Assert the buffer changes
    assertEquals(fFirstDate, fBuffer.getBuffer().get(0).getTime());
    assertEquals(fLastDate, fBuffer.getBuffer().get(fBuffer.getBuffer().size() - 1).getTime());

    // Assert the buffer duplication and sort
    for (int i = 0; i < fBuffer.getBuffer().size() - 1; i++) {
      for (int j = i + 1; j < fBuffer.getBuffer().size(); j++) {
        assertFalse(fBuffer.getBuffer().get(i).equals(fBuffer.getBuffer().get(j)));
        assertTrue(fBuffer.getBuffer().get(i).getExecutionTime() <= fBuffer.getBuffer().get(j).
                getExecutionTime());
        if (fBuffer.getBuffer().get(i).getExecutionTime() == fBuffer.getBuffer().get(j).getExecutionTime()) {
          assertTrue(fBuffer.getBuffer().get(i).getTime().before(fBuffer.getBuffer().get(j).getTime()));
        }
      }
    }
  }

}
