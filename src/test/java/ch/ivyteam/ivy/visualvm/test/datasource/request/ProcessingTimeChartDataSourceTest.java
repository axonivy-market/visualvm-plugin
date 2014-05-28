package ch.ivyteam.ivy.visualvm.test.datasource.request;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.request.ProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
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
            //ajp:34000/200 | http:98000/600 | https:151000/920
            new Object[]{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //ajp:34170/204 | http:98560/611 | https:151240/1000
            new Object[]{42, 50, 3, 42, 50, 3, 42, 50, 3},
            //ajp:34300/250 | http:99200/690 | https:151510/1080
            new Object[]{2, 8, 3, 42, 50, 3, 6, 13, 3},
            //ajp:30300/200 | http:90200/600 | https:150510/980
            new Object[]{0, 0, 0, 42, 50, 3, 6, 13, 0},
            //ajp:0/0 | http:0/0 | https:0/0
            new Object[]{0, 0, 0, 42, 50, 3, 6, 13, 0}
    );
  }

  private final long fAjp;
  private final long fHttp;
  private final long fHttps;
  private final long fMaxAjp;
  private final long fMaxHttp;
  private final long fMaxHttps;
  private final long fTotalMeanAjp;
  private final long fTotalMeanHttp;
  private final long fTotalMeanHttps;

  public ProcessingTimeChartDataSourceTest(BeanTestData.Dataset dataset,
          long ajp, long http, long https,
          long maxAjp, long maxHttp, long maxHttps,
          long totalMeanAjp, long totalMeanHttp, long totalMeanHttps) {
    super(dataset);
    fAjp = ajp;
    fHttp = http;
    fHttps = https;
    fMaxAjp = maxAjp;
    fMaxHttp = maxHttp;
    fMaxHttps = maxHttps;
    fTotalMeanAjp = totalMeanAjp;
    fTotalMeanHttp = totalMeanHttp;
    fTotalMeanHttps = totalMeanHttps;
  }

  @Test
  public void testCollectData() throws InstanceNotFoundException, IOException, ReflectionException,
          MalformedObjectNameException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    Set<ObjectName> connectorObjNames = new HashSet<>();
    connectorObjNames.add(new ObjectName("ivy:type=Connector,port=8009"));
    connectorObjNames.add(new ObjectName("ivy:type=Connector,port=8080"));
    connectorObjNames.add(new ObjectName("ivy:type=Connector,port=8443"));
    when(mockConnection.queryNames(IvyJmxConstant.Ivy.Connector.PATTERN, null))
            .thenReturn(connectorObjNames);

    DataBeanProvider provider = mockDataProvider(mockConnection);

    if (processTimeChartDataSource == null) {
      processTimeChartDataSource = new ProcessingTimeChartDataSource(provider, "", "", "");
    }
    Query query = new Query();
    processTimeChartDataSource.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    long[] values = processTimeChartDataSource.getValues(result);
    long[] labels = processTimeChartDataSource.calculateDetailValues(result);

    assertEquals(fAjp, values[0]);
    assertEquals(fHttp, values[1]);
    assertEquals(fHttps, values[2]);
    assertEquals(fMaxAjp, labels[0]);
    assertEquals(fTotalMeanAjp, labels[1]);
    assertEquals(fMaxHttp, labels[2]);
    assertEquals(fTotalMeanHttp, labels[3]);
    assertEquals(fMaxHttps, labels[4]);
    assertEquals(fTotalMeanHttps, labels[5]);
  }

}
