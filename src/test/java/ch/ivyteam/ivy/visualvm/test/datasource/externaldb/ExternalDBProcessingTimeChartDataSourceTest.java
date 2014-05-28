package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.net.URISyntaxException;
import java.util.List;
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
public class ExternalDBProcessingTimeChartDataSourceTest extends AbstractTest {

  private static final String DATA_FILE_PATH = "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb"
          + "/ExternalDbProcessingTimeChartDataSourceTest.xml";
  private final long fMax;
  private final long fMin;
  private final long fMean;
  private final long fMaxOfMax;
  private final long fTotalMean;

  private static DataBeanProvider fProvider;
  private static Query fQuery;
  private static ExternalDbProcessingTimeChartDataSource fDataSource;
  private static List<AbstractChartLabelCalcSupport> fLabelCalcSupports;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(DATA_FILE_PATH,
            //max:5 | min:3 | total:16 | trans:5
            new Object[]{5, 0, 3, 5, 0},
            //max:6 | min:2 | total:26 | trans:7
            new Object[]{6, 5, 2, 6, 5},
            //max:4 | min:3 | total:30 | trans:8
            new Object[]{4, 4, 3, 6, 4},
            //max:4 | min:3 | total:28 | trans:6
            new Object[]{4, 0, 3, 6, 12},
            //max:0 | min:0 | total:0 | trans:0
            new Object[]{0, 0, 0, 6, 12}
    );
  }

  public ExternalDBProcessingTimeChartDataSourceTest(BeanTestData.Dataset dataset,
          long max, long mean, long min, long maxOfMax, long totalMean) {
    super(dataset);
    fMax = max;
    fMin = min;
    fMean = mean;
    fMaxOfMax = maxOfMax;
    fTotalMean = totalMean;
  }

  @Test
  public void testExternalDbProcessingTimeChartDataSource() {

    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    if (fProvider == null) {
      fProvider = mock(DataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fDataSource == null) {
      fDataSource = new ExternalDbProcessingTimeChartDataSource(fProvider, "", "", "");
      fDataSource.setNamePattern(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_PATTERN);
      fDataSource.setApplication("test");
      fDataSource.setEnvironment("enviroment1");
      fDataSource.setConfigName("DBConfig1");
      fDataSource.init();
      fLabelCalcSupports = fDataSource.getLabelCalcSupports();

      fQuery = new Query();
      fDataSource.updateQuery(fQuery);
    }
    QueryResult result = fQuery.execute(mockConnection);
    long[] values = fDataSource.getValues(result);
    for (AbstractChartLabelCalcSupport labelCal : fLabelCalcSupports) {
      labelCal.updateValues(result);
    }

    //Check series value
    assertEquals(fMax, values[0]);
    assertEquals(fMean, values[1]);
    assertEquals(fMin, values[2]);

    //Check label  calculation support
    assertEquals("Max of max", fLabelCalcSupports.get(0).getText());
    assertEquals(fMaxOfMax, fLabelCalcSupports.get(0).getValue());
    assertEquals("Total mean", fLabelCalcSupports.get(1).getText());
    assertEquals(fTotalMean, fLabelCalcSupports.get(1).getValue());
  }

}
