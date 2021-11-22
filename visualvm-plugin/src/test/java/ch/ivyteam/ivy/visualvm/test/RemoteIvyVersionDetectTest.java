package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.IvyViewHelper;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class RemoteIvyVersionDetectTest extends AbstractTest {

  private final boolean fResult;
  private final int fIndex;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    Iterable<Object[]> data = TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/RemoteVersionDetectTest.xml",
            new Object[]{0, true}, new Object[]{1, false},
            new Object[]{2, true}, new Object[]{3, false});
    return data;
  }

  public RemoteIvyVersionDetectTest(BeanTestData.Dataset dataset, int index, boolean result) {
    super(dataset);
    fResult = result;
    fIndex = index;
  }

  @Test
  public void testServerInformation() throws MalformedObjectNameException,
          IOException, IvyJmxDataCollectException, InstanceNotFoundException, ReflectionException {

    MBeanServerConnection mockedMBeanServer = createMockConnection();
    DataBeanProvider dataBeanProvider = new DataBeanProvider(mockedMBeanServer);

    addTestData(mockedMBeanServer, getDataset());
    if (fIndex < 2) {
      boolean res = IvyViewHelper.isRemoteIvyApplication50OrOlder(dataBeanProvider);
      assertEquals(fResult, res);
    } else {
      boolean res = IvyViewHelper.isRemoteIvyAppication51OrYounger(dataBeanProvider);
      assertEquals(fResult, res);
    }
  }
}
