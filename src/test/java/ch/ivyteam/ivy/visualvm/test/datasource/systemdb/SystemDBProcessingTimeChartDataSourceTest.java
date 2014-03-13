package ch.ivyteam.ivy.visualvm.test.datasource.systemdb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.ProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.net.URISyntaxException;
import javax.management.MBeanServerConnection;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author ntnam
 */
@RunWith(Parameterized.class)
public class SystemDBProcessingTimeChartDataSourceTest extends AbstractTest {

  private static final String DATA_FILE_PATH = "/ch/ivyteam/ivy/visualvm/test/datasource/systemdb/"
          + "SystemDbProcessingTimeChartDataSourceTest.xml";
  private final long fMax;
  private final long fMin;
  private final long fTotalDelta;

  private static IDataBeanProvider fProvider;
  private static Query fQuery;
  private static ProcessingTimeChartDataSource fDataSource;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil
            .createTestData(
                    DATA_FILE_PATH,
                    new Object[]{2122, 0, 2122},
                    new Object[]{1343, 1343, 1343},
                    new Object[]{3344, 3344, 3344}
            );
  }

  public SystemDBProcessingTimeChartDataSourceTest(BeanTestData.Dataset dataset,
          long max, long min, long total) {
    super(dataset);
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
      fDataSource = new ProcessingTimeChartDataSource(fProvider, "",
              "", "");

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
