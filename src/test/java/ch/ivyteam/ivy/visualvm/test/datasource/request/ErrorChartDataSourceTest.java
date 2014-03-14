package ch.ivyteam.ivy.visualvm.test.datasource.request;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.request.ErrorChartDataSource;
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
public class ErrorChartDataSourceTest extends AbstractTest {
  private static ErrorChartDataSource errorChartDataSource;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/request/ErrorChartDataSourceTest.xml",
            new Object[]{0, 0, 0, 0, 0, 0},
            new Object[]{6, 8, 7, 6, 8, 7},
            new Object[]{14, 12, 13, 14, 12, 13},
            new Object[]{8, 8, 8, 14, 12, 13},
            new Object[]{0, 0, 0, 14, 12, 13}
    );
  }

  private final long fAjp;
  private final long fHttp;
  private final long fHttps;
  private final long fMaxAjp;
  private final long fMaxHttp;
  private final long fMaxHttps;

  public ErrorChartDataSourceTest(BeanTestData.Dataset dataset,
          long ajp, long http, long https, long maxAjp, long maxHttp, long maxHttps) {
    super(dataset);
    fAjp = ajp;
    fHttp = http;
    fHttps = https;
    fMaxAjp = maxAjp;
    fMaxHttp = maxHttp;
    fMaxHttps = maxHttps;
  }

  @Test
  public void testCollectData() throws InstanceNotFoundException, IOException, ReflectionException,
          MalformedObjectNameException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    Set<ObjectName> connectorObjNames = new HashSet<>();
    connectorObjNames.add(new ObjectName("ivy:type=GlobalRequestProcessor,name=\"ajp-bio-8009\""));
    connectorObjNames.add(new ObjectName("ivy:type=GlobalRequestProcessor,name=\"http-bio-8080\""));
    connectorObjNames.add(new ObjectName("ivy:type=GlobalRequestProcessor,name=\"http-bio-8443\""));
    when(mockConnection.queryNames(IvyJmxConstant.Ivy.Processor.PATTERN, null))
            .thenReturn(connectorObjNames);

    IDataBeanProvider provider = mockDataProvider(mockConnection);

    if (errorChartDataSource == null) {
      errorChartDataSource = new ErrorChartDataSource(provider, "", "", "");
    }
    Query query = new Query();
    errorChartDataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    long[] values = errorChartDataSource.getValues(result);
    long[] labels = errorChartDataSource.calculateDetailValues(result);

    assertEquals(fAjp, values[0]);
    assertEquals(fHttp, values[1]);
    assertEquals(fHttps, values[2]);
    assertEquals(fMaxAjp, labels[0]);
    assertEquals(fMaxHttp, labels[1]);
    assertEquals(fMaxHttps, labels[2]);
  }

}
