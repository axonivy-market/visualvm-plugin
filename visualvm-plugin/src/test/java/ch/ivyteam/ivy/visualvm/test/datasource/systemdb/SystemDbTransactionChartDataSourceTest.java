package ch.ivyteam.ivy.visualvm.test.datasource.systemdb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.systemdb.TransactionChartDataSource;
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
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SystemDbTransactionChartDataSourceTest extends AbstractTest {
  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/systemdb/SystemDbTransChartDataSourceTest.xml",
            new Object[]{0, 0, 0, 0},
            new Object[]{2, 1, 2, 1},
            new Object[]{1, 0, 2, 1}
    );
  }

  private static DataBeanProvider fProvider;
  private static Query fQuery;
  private static TransactionChartDataSource fDataSource;
  private static List<AbstractChartLabelCalcSupport> fLabelCalcSupports;

  private final int fTransactions;
  private final int fErrors;
  private final int fMaxTransaction;
  private final int fMaxError;

  public SystemDbTransactionChartDataSourceTest(BeanTestData.Dataset dataset,
          int transactions, int errors, int maxTrans, int maxError) {
    super(dataset);
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
      fProvider = mock(DataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);

    if (fDataSource == null) {
      fDataSource = new TransactionChartDataSource(fProvider, "", "", "");
      fLabelCalcSupports = fDataSource.getLabelCalcSupports();
      fQuery = new Query();
      fDataSource.updateQuery(fQuery);
    }
    QueryResult result = fQuery.execute(mockConnection);
    long[] values = fDataSource.getValues(result);
    for (AbstractChartLabelCalcSupport labelCal : fLabelCalcSupports) {
      labelCal.updateValues(result);
    }

    assertEquals(fTransactions, values[0]);
    assertEquals(fErrors, values[1]);
    //Check label  calculation support
    assertEquals("Max transactions", fLabelCalcSupports.get(0).getText());
    assertEquals(fMaxTransaction, fLabelCalcSupports.get(0).getValue());
    assertEquals("Max errors", fLabelCalcSupports.get(1).getText());
    assertEquals(fMaxError, fLabelCalcSupports.get(1).getValue());

  }

}
