package ch.ivyteam.ivy.visualvm.test.datasource.externalrestws;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceProcessingTimeChartDataSource;
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

@RunWith(Parameterized.class)
public class ExternaRESTWSCallingTimeChartDataSourceTest extends AbstractTest {

  private static final String DATA_FILE_PATH = "/ch/ivyteam/ivy/visualvm/test/datasource/externalrestws"
          + "/ExternalRESTWSCallingTimeChartDataSourceTest.xml";
  private final String[] fConfigs;
  private final long fMax;
  private final long fMin;
  private final long fMean;
  private final long fMaxOfMax;
  private final long fTotalMean;

  private static DataBeanProvider fProvider;
  private static Query fQuery;
  private static WebServiceProcessingTimeChartDataSource fDataSource;
  private static List<AbstractChartLabelCalcSupport> fLabelCalcSupports;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil
            .createTestData(
                    DATA_FILE_PATH,
                    new Object[] {new String[] {"test", "enviroment1", "personService"}, 5, 0, 3, 5, 0},
                    new Object[] {new String[] {"test", "enviroment1", "personService"}, 5, 6, 2, 5, 6},
                    new Object[] {new String[] {"test", "enviroment1", "personService"}, 6, 3, 2, 6, 4}
            );
  }

  public ExternaRESTWSCallingTimeChartDataSourceTest(BeanTestData.Dataset dataset,
          String[] configs, long max, long mean, long min, long maxOfMax, long totalMean) {
    super(dataset);
    fConfigs = new String[configs.length];
    System.arraycopy(configs, 0, fConfigs, 0, configs.length);
    fMax = max;
    fMin = min;
    fMean = mean;
    fMaxOfMax = maxOfMax;
    fTotalMean = totalMean;
  }

  @Test
  public void testExternalWSCallingTimeChartDataSource() {

    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    if (fProvider == null) {
      fProvider = mock(DataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fDataSource == null) {
      fDataSource = new WebServiceProcessingTimeChartDataSource(fProvider, "",
              "", "");
      fDataSource.setApplication(fConfigs[0]);
      fDataSource.setEnvironment(fConfigs[1]);
      fDataSource.setConfigName(fConfigs[2]);
      fDataSource.setNamePattern(IvyJmxConstant.IvyServer.RESTWebService.NAME_PATTERN);
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
    assertEquals(fMax, values[0]);
    assertEquals(fMean, values[1]);
    assertEquals(fMin, values[2]);

    assertEquals("Max of max", fLabelCalcSupports.get(0).getText());
    assertEquals(fMaxOfMax, fLabelCalcSupports.get(0).getValue());
    assertEquals("Total avg", fLabelCalcSupports.get(1).getText());
    assertEquals(fTotalMean, fLabelCalcSupports.get(1).getValue());
  }

}
