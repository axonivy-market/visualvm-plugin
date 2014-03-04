package ch.ivyteam.ivy.visualvm.test.datasource.request;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.request.ProcessingTimeChartDataSource;
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
public class ProcessingTimeChartDataSourceTest extends AbstractTest {
  private static ProcessingTimeChartDataSource processTimeChartDataSource;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/request/ProcessingTimeChartDataSourceTest.xml",
            new Object[]{0, 0, 0},
            new Object[]{170, 560, 240},
            new Object[]{130, 640, 270}
    );
  }

  private final long fAjp;
  private final long fHttp;
  private final long fHttps;

  public ProcessingTimeChartDataSourceTest(BeanTestData.Dataset dataset,
          long ajp, long http, long https) {
    super(dataset);
    fAjp = ajp;
    fHttp = http;
    fHttps = https;
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

    IDataBeanProvider provider = new IDataBeanProvider() {
      @Override
      public MBeanServerConnection getMBeanServerConnection() {
        return mockConnection;
      }

    };
    if (processTimeChartDataSource == null) {
      processTimeChartDataSource = new ProcessingTimeChartDataSource(provider, "", "", "");
    }
    Query query = new Query();
    processTimeChartDataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    long[] values = processTimeChartDataSource.getValues(result);

    assertEquals(fAjp, values[0]);
    assertEquals(fHttp, values[1]);
    assertEquals(fHttps, values[2]);
  }

}
