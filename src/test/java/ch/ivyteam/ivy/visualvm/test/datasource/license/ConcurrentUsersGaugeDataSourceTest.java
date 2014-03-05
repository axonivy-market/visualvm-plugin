package ch.ivyteam.ivy.visualvm.test.datasource.license;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.RadialPanel;
import ch.ivyteam.ivy.visualvm.chart.data.license.ConcurrentUsersGaugeDataSource;
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
public class ConcurrentUsersGaugeDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/license/ConcurrentUsersGaugeDataSourceTest.xml",
            new Object[] {1}, new Object[] {6}, new Object[] {9});
  }

  private final long fConcurrentUsers;

  public ConcurrentUsersGaugeDataSourceTest(BeanTestData.Dataset dataset, long concurrentUsers) {
    super(dataset);
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
    ConcurrentUsersGaugeDataSource concurrentUsersGaugeDataSource = new ConcurrentUsersGaugeDataSource(
            provider, 40);
    RadialPanel radialPanel = new RadialPanel(concurrentUsersGaugeDataSource);
    Query query = new Query();
    concurrentUsersGaugeDataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    radialPanel.updateValues(result);

    double value = concurrentUsersGaugeDataSource.getValue(result);

    assertEquals(fConcurrentUsers, new Double(value).longValue());
    assertEquals(radialPanel.getUI().getValue(), value);
  }

}
