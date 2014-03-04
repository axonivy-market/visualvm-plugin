package ch.ivyteam.ivy.visualvm.test.datasource.license;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.license.ConcurrentUsersChartDataSource;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.io.IOException;
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
public class ConcurrentUsersChartDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/license/ConcurrentUsersChartDataSourceTest.xml",
            new Object[] {6, 1}, new Object[] {6, 6}, new Object[] {6, 9});
  }

  private final long fConcurrentUsers;
  private final long fConcurrentUsersLimit;

  public ConcurrentUsersChartDataSourceTest(BeanTestData.Dataset dataset, long concurrentUsersLimit,
          long concurrentUsers) {
    super(dataset);
    fConcurrentUsersLimit = concurrentUsersLimit;
    fConcurrentUsers = concurrentUsers;
  }

  @Test
  public void testConcurrentUsers() throws InstanceNotFoundException, IOException, ReflectionException,
          MalformedObjectNameException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    IDataBeanProvider provider = new IDataBeanProvider() {
      @Override
      public MBeanServerConnection getMBeanServerConnection() {
        return mockConnection;
      }

    };
    ConcurrentUsersChartDataSource concurrentUsersChartDataSource = new ConcurrentUsersChartDataSource(
            provider, "", "", "");
    Query query = new Query();
    concurrentUsersChartDataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    long[] values = concurrentUsersChartDataSource.getValues(result);

    assertEquals(fConcurrentUsersLimit, values[0]);
    assertEquals(fConcurrentUsers, values[1]);
  }

}