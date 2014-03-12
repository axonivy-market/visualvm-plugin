package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;

import javax.management.MBeanServerConnection;
import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

/**
 * 
 * @author ntnam
 */
@RunWith(Parameterized.class)
public class ExternalDBProcessingTimeChartDataSourceTest extends AbstractTest {

  private static final String DATA_FILE_PATH = "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb"
          + "/ExternalDbProcessingTimeChartDataSourceTest.xml";
  private final String[] fConfigs;
  private final long fMax;
  private final long fMin;
  private final long fTotalDelta;

  private static IDataBeanProvider fProvider;
  private static Query fQuery;
  private static ExternalDbProcessingTimeChartDataSource fDataSource;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil
            .createTestData(
                    DATA_FILE_PATH,
                    new Object[] {new String[] {"test", "enviroment1", "DBConfig1"}, 2122, 2122, 0},
                    new Object[] {new String[] {"test", "enviroment1", "DBConfig1"}, 1343, 1343, 1343},
                    new Object[] {new String[] {"test", "enviroment1", "DBConfig1"}, 3344, 3344, 3344}
            );
  }

  public ExternalDBProcessingTimeChartDataSourceTest(BeanTestData.Dataset dataset,
          String[] configs, long max, long min, long total) {
    super(dataset);
    fConfigs = new String[configs.length];
    System.arraycopy(configs, 0, fConfigs, 0, configs.length);
    fMax = max;
    fMin = min;
    fTotalDelta = total;
  }

  @Test
  public void testExternalDbProcessingTimeChartDataSource() {

    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    if (fProvider == null) {
      fProvider = mock(IDataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fDataSource == null) {
      fDataSource = new ExternalDbProcessingTimeChartDataSource(fProvider, "",
              "", "");
      fDataSource.setApplication(fConfigs[0]);
      fDataSource.setEnvironment(fConfigs[1]);
      fDataSource.setConfigName(fConfigs[2]);
      fDataSource.init();

      fQuery = new Query();
      fDataSource.updateQuery(fQuery);
    }
    QueryResult result = fQuery.execute(mockConnection);
    long[] values = fDataSource.getValues(result);
    assertEquals(fMax, values[0]);
    assertEquals(fMin, values[1]);
    assertEquals(fTotalDelta, values[2]);
  }

}
