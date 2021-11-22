package ch.ivyteam.ivy.visualvm.test.datasource.systemdb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.addTestData;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.createMockConnection;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SystemDbConnectionChartDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/systemdb/SystemDbConnectionChartDataSourceTest.xml",
            new Object[]{50, 1, 0},
            new Object[]{40, 1, 1},
            new Object[]{30, 0, 0}
    );
  }

  private final int fOpenConnection;
  private final int fUsedConnection;
  private final int fMaxConnection;

  public SystemDbConnectionChartDataSourceTest(BeanTestData.Dataset dataset,
          int maxConnection, int openConnection, int usedConnection) {
    super(dataset);
    fMaxConnection = maxConnection;
    fOpenConnection = openConnection;
    fUsedConnection = usedConnection;
  }

  @Test
  public void testConnectionChartDataSource() throws MalformedObjectNameException, IOException,
          ReflectionException, MBeanException, AttributeNotFoundException, InstanceNotFoundException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    DataBeanProvider provider = mockDataProvider(mockConnection);

    ConnectionChartDataSource dataSource = new ConnectionChartDataSource(provider, "",
            "", "");

    Query query = new Query();
    dataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    long[] values = dataSource.getValues(result);
    assertEquals(fOpenConnection, values[0]);
    assertEquals(fUsedConnection, values[1]);
    List<AbstractChartLabelCalcSupport> labelCalcSupports = dataSource.getLabelCalcSupports();
    for (AbstractChartLabelCalcSupport labelCal : labelCalcSupports) {
      labelCal.updateValues(result);
    }
    assertEquals("Limit", labelCalcSupports.get(0).getText());
    assertEquals(fMaxConnection, labelCalcSupports.get(0).getValue());
    assertEquals("Max open", labelCalcSupports.get(1).getText());
    assertEquals(fOpenConnection, labelCalcSupports.get(1).getValue());
    assertEquals("Max used", labelCalcSupports.get(2).getText());
    assertEquals(fUsedConnection, labelCalcSupports.get(2).getValue());

  }

}
