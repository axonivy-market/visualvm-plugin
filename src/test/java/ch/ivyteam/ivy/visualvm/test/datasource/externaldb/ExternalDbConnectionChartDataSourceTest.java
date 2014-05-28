package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
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
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ExternalDbConnectionChartDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb/ExternalDbConnectionChartDataSourceTest.xml",
            new Object[]{new String[]{"app1", "env1", "config1"}, 5, 1, 0},
            new Object[]{new String[]{"app1", "env1", "config2"}, 5, 1, 1},
            new Object[]{new String[]{"app1", "env2", "config1"}, 5, 0, 0}
    );
  }

  private final String[] fConfigs;
  private final int fMaxConnection;
  private final int fOpenConnection;
  private final int fUsedConnection;

  public ExternalDbConnectionChartDataSourceTest(BeanTestData.Dataset dataset,
          String[] configs, int maxConnection, int openConnection, int usedConnection) {
    super(dataset);
    fConfigs = new String[configs.length];
    System.arraycopy(configs, 0, fConfigs, 0, configs.length);
    fMaxConnection = maxConnection;
    fOpenConnection = openConnection;
    fUsedConnection = usedConnection;
  }

  @Test
  public void testConnectionChartDataSource() throws MalformedObjectNameException, IOException,
          ReflectionException, MBeanException, AttributeNotFoundException, InstanceNotFoundException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    ObjectName objectName = new ObjectName("Xpert.ivy Server:type=External Database,application="
            + fConfigs[0] + ",environment=" + fConfigs[1] + ",name=" + fConfigs[2]);
    when(mockConnection.getAttribute(objectName,
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_MAX_CONNECTION)).thenReturn(fMaxConnection);

    DataBeanProvider provider = mockDataProvider(mockConnection);

    ExternalDbConnectionChartDataSource dataSource = new ExternalDbConnectionChartDataSource(provider, "",
            "", "");
    dataSource.setNamePattern(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_PATTERN);
    dataSource.setApplication(fConfigs[0]);
    dataSource.setEnvironment(fConfigs[1]);
    dataSource.setConfigName(fConfigs[2]);
    dataSource.init();

    Query query = new Query();
    dataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    long[] values = dataSource.getValues(result);
    List<AbstractChartLabelCalcSupport> labelCalcSupports = dataSource.getLabelCalcSupports();
    for (AbstractChartLabelCalcSupport labelCal : labelCalcSupports) {
      labelCal.updateValues(result);
    }
    //Check series value
    assertEquals(fOpenConnection, values[0]);
    assertEquals(fUsedConnection, values[1]);

    //Check label  calculation support
    assertEquals("Limit", labelCalcSupports.get(0).getText());
    assertEquals(fMaxConnection, labelCalcSupports.get(0).getValue());
    assertEquals("Max open", labelCalcSupports.get(1).getText());
    assertEquals(fOpenConnection, labelCalcSupports.get(1).getValue());
    assertEquals("Max used", labelCalcSupports.get(2).getText());
    assertEquals(fUsedConnection, labelCalcSupports.get(2).getValue());

  }

}
