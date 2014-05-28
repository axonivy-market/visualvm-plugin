package ch.ivyteam.ivy.visualvm.test.datasource.license;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.RadialPanel;
import ch.ivyteam.ivy.visualvm.chart.data.license.NamedUsersGaugeDataSource;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import eu.hansolo.steelseries.gauges.Radial;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class NamedUsersGaugeDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}: Current: {1} / Limit: {2}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/license/NamedUsersGaugeDataSourceTest.xml",
            new Object[]{1, 6, 3, new Double[]{0.0, 4.0, 5.0, 6.0}},
            new Object[]{6, 7, 3, new Double[]{0.0, 5.0, 6.0, 7.0}},
            new Object[]{9, 25, 3, new Double[]{0.0, 20.0, 22.0, 25.0}});
  }

  private final long fCurrentUsers;
  private final int fLimit;
  private final int fNumberofGaugeSections;
  private final Double[] fThresholds;

  public NamedUsersGaugeDataSourceTest(BeanTestData.Dataset dataset, long currentUsers, int limit,
          int numberOfGaugeSections, Double[] thresholds) {
    super(dataset);
    fCurrentUsers = currentUsers;
    fLimit = limit;
    fNumberofGaugeSections = numberOfGaugeSections;
    fThresholds = new Double[thresholds.length];
    System.arraycopy(thresholds, 0, fThresholds, 0, thresholds.length);
  }

  @Test
  public void testConcurrentUsers() throws InstanceNotFoundException, IOException, ReflectionException,
          MalformedObjectNameException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    DataBeanProvider provider = mockDataProvider(mockConnection);
    NamedUsersGaugeDataSource namedUsersGaugeDataSource = new NamedUsersGaugeDataSource(
            provider, fLimit);
    RadialPanel radialPanel = new RadialPanel(namedUsersGaugeDataSource);
    Radial radial = radialPanel.getUI();
    assertEquals(fNumberofGaugeSections, radial.getSections().size());
    for (int i = 0; i < fNumberofGaugeSections; i++) {
      assertEquals(fThresholds[i], radial.getSections().get(i).getStart());
      assertEquals(fThresholds[i + 1], radial.getSections().get(i).getStop());
    }
    Query query = new Query();
    namedUsersGaugeDataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    radialPanel.updateValues(result);

    double value = namedUsersGaugeDataSource.getValue(result);

    assertEquals(fCurrentUsers, new Double(value).longValue());
    assertEquals(radialPanel.getUI().getValue(), value);
  }

}
