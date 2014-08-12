package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.IvyViewProvider;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import ch.ivyteam.ivy.visualvm.view.GenericData;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class VersionCompatibilitiesTest extends AbstractTest {

  private final boolean fResult;

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException {
    Iterable<Object[]> data = TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/VersionCompatibilitiesTest.xml",
            new Object[]{true}, new Object[]{true},
            new Object[]{true}, new Object[]{true},
            new Object[]{false}, new Object[]{false}, new Object[]{false});
    return data;
  }

  public VersionCompatibilitiesTest(BeanTestData.Dataset dataset, boolean result) {
    super(dataset);
    fResult = result;
  }

  @Test
  public void testServerInformation() throws MalformedObjectNameException,
          IOException, IvyJmxDataCollectException, InstanceNotFoundException, ReflectionException {

    MBeanServerConnection mockedMBeanServer = createMockConnection();
    IvyViewProvider ivyViewProvider = new IvyViewProvider();
    DataBeanProvider dataBeanProvider = new DataBeanProvider(mockedMBeanServer);

    if (getDataset().getProperty().size() > 0) {
      addTestData(mockedMBeanServer, getDataset());
      if (getDataset().getProperty().size() == 2) {
        dataBeanProvider = mock(DataBeanProvider.class);
        GenericData mockedGenericData = mock(GenericData.class);
        when(mockedGenericData.getApplicationInfo()).thenReturn(null);
        when(dataBeanProvider.getMBeanServerConnection()).thenReturn(mockedMBeanServer);
        when(dataBeanProvider.getGenericData()).thenReturn(mockedGenericData);
      }
    } else {
      when(mockedMBeanServer.getAttributes((ObjectName) anyObject(), (String[]) anyObject()))
              .thenThrow(new InstanceNotFoundException());
    }

    boolean isIvy = ivyViewProvider.isAxonIvyApp(dataBeanProvider);
    assertEquals(fResult, isIvy);
  }

}
