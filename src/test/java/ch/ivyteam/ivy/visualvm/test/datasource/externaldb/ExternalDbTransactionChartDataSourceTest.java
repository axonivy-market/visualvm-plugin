package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.ExternalDbTransactionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.addTestData;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.createMockConnection;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
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
public class ExternalDbTransactionChartDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externaldb/ExternalDbTransChartDataSourceTest.xml",
            new Object[]{new String[]{"app1", "env1", "config1"}, 0, 0, 0, 0},
            new Object[]{new String[]{"app1", "env1", "config1"}, 2, 1, 2, 1},
            new Object[]{new String[]{"app1", "env1", "config1"}, 1, 0, 2, 1}
    );
  }

  private static IDataBeanProvider fProvider;
  private static Query fQuery;
  private static ExternalDbTransactionChartDataSource fDataSource;
  private static List<AbstractChartLabelCalcSupport> fLabelCalcSupports;

  private final String[] fConfigs;
  private final int fTransactions;
  private final int fErrors;
  private final int fMaxTransaction;
  private final int fMaxError;

  public ExternalDbTransactionChartDataSourceTest(BeanTestData.Dataset dataset,
          String[] configs, int transactions, int errors, int maxTrans, int maxError) {
    super(dataset);
    fConfigs = new String[configs.length];
    System.arraycopy(configs, 0, fConfigs, 0, configs.length);
    fTransactions = transactions;
    fErrors = errors;
    fMaxTransaction = maxTrans;
    fMaxError = maxError;
  }

  @Test
  public void testConnectionChartDataSource() throws MalformedObjectNameException, IOException,
          ReflectionException, MBeanException, AttributeNotFoundException, InstanceNotFoundException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    if (fProvider == null) {
      fProvider = mock(IDataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fDataSource == null) {
      fDataSource
              = new ExternalDbTransactionChartDataSource(fProvider, "",
                      "", "");
      fDataSource.setNamePattern(IvyJmxConstant.IvyServer.ExternalDatabase.NAME_PATTERN);
      fDataSource.setApplication(fConfigs[0]);
      fDataSource.setEnvironment(fConfigs[1]);
      fDataSource.setConfigName(fConfigs[2]);
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
    assertEquals(fTransactions, values[0]);
    assertEquals(fErrors, values[1]);

    //Check label  calculation support
    assertEquals("Max transactions", fLabelCalcSupports.get(0).getText());
    assertEquals(fMaxTransaction, fLabelCalcSupports.get(0).getValue());
    assertEquals("Max errors", fLabelCalcSupports.get(1).getText());
    assertEquals(fMaxError, fLabelCalcSupports.get(1).getValue());
  }

}
