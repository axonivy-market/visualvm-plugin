package ch.ivyteam.ivy.visualvm.test.datasource.request;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.request.SessionChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SessionChartDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/request/SessionChartDataSourceTest.xml",
            new Object[]{0, 0, 0, 0},
            new Object[]{5, 5, 3, 2},
            new Object[]{500, 500, 300, 200}
    );
  }

  private final long fHttp;
  private final long fIvy;
  private final long fLicense;
  private final long fRD;

  public SessionChartDataSourceTest(BeanTestData.Dataset dataset,
          long http, long ivy, long license, long rd) {
    super(dataset);
    fHttp = http;
    fIvy = ivy;
    fLicense = license;
    fRD = rd;
  }

  @Test
  public void testCollectData() throws InstanceNotFoundException, IOException, ReflectionException,
          MalformedObjectNameException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    Set<ObjectName> connectorObjNames = new HashSet<>();
    connectorObjNames.add(new ObjectName("ivy:type=Manager,context=/ivy,host=localhost"));
    when(mockConnection.queryNames(IvyJmxConstant.Ivy.Manager.PATTERN, null))
            .thenReturn(connectorObjNames);

    IDataBeanProvider provider = new IDataBeanProvider() {
      @Override
      public MBeanServerConnection getMBeanServerConnection() {
        return mockConnection;
      }

    };
    SessionChartDataSource sessionChartDataSource = new SessionChartDataSource(provider, "", "", "");
    Query query = new Query();
    sessionChartDataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    long[] values = sessionChartDataSource.getValues(result);

    assertEquals(fHttp, values[0]);
    assertEquals(fIvy, values[1]);
    assertEquals(fLicense, values[2]);
    assertEquals(fRD, values[3]);
  }

}
