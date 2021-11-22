package ch.ivyteam.ivy.visualvm.test.datasource.externalws;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.webservice.WebServiceCallsChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ExternalWSCallChartDataSourceTest extends AbstractTest {

  private static final String DATA_FILE_PATH = "/ch/ivyteam/ivy/visualvm/test/datasource/externalws"
          + "/ExternalWSCallChartDataSourceTest.xml";
  private static DataBeanProvider fProvider;
  private static Query fQuery;
  private static WebServiceCallsChartDataSource fDataSource;
  private static List<AbstractChartLabelCalcSupport> fLabelCalcSupports;

  private final String[] fConfigs;
  private final int fCalls;
  private final int fErrors;
  private final int fMaxCalls;
  private final int fMaxErrors;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            DATA_FILE_PATH,
            new Object[] {new String[] {"test", "enviroment1", "WSConfig1"}, 0, 0, 0, 0},
            new Object[] {new String[] {"test", "enviroment1", "WSConfig1"}, 2, 1, 2, 1},
            new Object[] {new String[] {"test", "enviroment1", "WSConfig1"}, 1, 0, 2, 1}
            );
  }

  public ExternalWSCallChartDataSourceTest(BeanTestData.Dataset dataset,
          String[] configs, int calls, int errors, int maxCalls, int maxErrors) {
    super(dataset);
    fConfigs = new String[configs.length];
    System.arraycopy(configs, 0, fConfigs, 0, configs.length);
    fCalls = calls;
    fErrors = errors;
    fMaxCalls = maxCalls;
    fMaxErrors = maxErrors;
  }

  @Test
  public void testConnectionChartDataSource() throws MalformedObjectNameException, IOException,
          ReflectionException, MBeanException, AttributeNotFoundException, InstanceNotFoundException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    if (fProvider == null) {
      fProvider = mock(DataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fDataSource == null) {
      fDataSource = new WebServiceCallsChartDataSource(fProvider, "",
              "", "");
      fDataSource.setApplication(fConfigs[0]);
      fDataSource.setEnvironment(fConfigs[1]);
      fDataSource.setConfigName(fConfigs[2]);
      fDataSource.setNamePattern(IvyJmxConstant.IvyServer.SOAPWebService.NAME_PATTERN);
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
    assertEquals(fCalls, values[0]);
    assertEquals(fErrors, values[1]);

    assertEquals("Max calls", fLabelCalcSupports.get(0).getText());
    assertEquals(fMaxCalls, fLabelCalcSupports.get(0).getValue());
    assertEquals("Max errors", fLabelCalcSupports.get(1).getText());
    assertEquals(fMaxErrors, fLabelCalcSupports.get(1).getValue());
  }

}
