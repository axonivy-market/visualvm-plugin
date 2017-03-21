package ch.ivyteam.ivy.visualvm.test.datasource.externalrestws;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.ErrorMessage;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import ch.ivyteam.ivy.visualvm.service.restwebservice.AbstractRESTExecutionBuffer;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTErrorExecutionBuffer;
import ch.ivyteam.ivy.visualvm.test.AbstractTest;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.addTestData;
import static ch.ivyteam.ivy.visualvm.test.AbstractTest.createMockConnection;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.util.TestUtil;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.JAXBException;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ErrorCallsBufferTest extends AbstractTest {

  private int fExpectedNumberOfCalls;
  private List<RESTWebServiceInfo> fExpectedInfos;
  private static DataBeanProvider fProvider;
  private static AbstractRESTExecutionBuffer fBuffer;
  private static SimpleDateFormat fDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  public ErrorCallsBufferTest(BeanTestData.Dataset dataset, int numberOfCalls, List<RESTWebServiceInfo> infos) {
    super(dataset);
    fExpectedNumberOfCalls = numberOfCalls;
    fExpectedInfos = infos;
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    List<RESTWebServiceInfo> errorInfos = new ArrayList<>();
    final ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setMessage("ivy:error:script");
    errorMessage.setType("ch.ivyteam.ivy.bpm.error.BpmError");
    errorMessage.setStacktrace("BpmError ivy:error:script  Unique ID: 15AEEA814764B74E");
    final RESTWebServiceInfo errorInfo1 = new RESTWebServiceInfo();
    errorInfo1.setApplication("designer");
    errorInfo1.setEnvironment("Default");
    errorInfo1.setConfigName("approvalService");
    errorInfo1.setProcessElementId("157B33AB7587F1F1-f6");
    errorInfo1.setPMVName("designer/ConnectivityDemos$1");
    errorInfo1.setTime(fDateFormat.parse("21/03/2017 09:17:22"));
    errorInfo1.setExecutionTime(35016698);
    errorInfo1.setRequestMethod("GET");
    errorInfo1.setRequestUrl("http://127.0.0.1:8081/ivy/api/designer/approve");
    errorInfo1.setResponseStatus("");
    errorInfo1.setErrorMessage(errorMessage);
    
    final RESTWebServiceInfo errorInfo2 = new RESTWebServiceInfo();
    errorInfo2.setApplication("designer");
    errorInfo2.setEnvironment("Default");
    errorInfo2.setConfigName("approvalService");
    errorInfo2.setProcessElementId("157B33AB7587F1F1-f6");
    errorInfo2.setPMVName("designer/ConnectivityDemos$1");
    errorInfo2.setTime(fDateFormat.parse("21/03/2017 09:48:23"));
    errorInfo2.setExecutionTime(32094694);
    errorInfo2.setRequestMethod("GET");
    errorInfo2.setRequestUrl("http://127.0.0.1:8081/ivy/api/designer/approve");
    errorInfo2.setResponseStatus("");
    errorInfo2.setErrorMessage(errorMessage);
    
    errorInfos.add(errorInfo1);
    errorInfos.add(errorInfo2);

    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externalrestws/ErrorCallsTest.xml",
            new Object[]{0, Collections.EMPTY_LIST},
            new Object[]{0, Collections.EMPTY_LIST},
            new Object[]{2, errorInfos}
    );
  }

  @Test
  public void testData() throws IOException, MalformedObjectNameException {
    final MBeanServerConnection mockConnection = createMockConnection();
    addTestData(mockConnection, getDataset());
    Set<ObjectName> extDbObjNames = new HashSet<>();
    extDbObjNames.add(new ObjectName(
            "ivy Engine:type=External REST Web Service,application=designer,environment=Default,name=personService"));
    extDbObjNames.add(new ObjectName(
            "ivy Engine:type=External REST Web Service,application=designer,environment=Default,name=approvalService"));
    when(mockConnection.queryNames(IvyJmxConstant.IvyServer.RESTWebService.NAME_FILTER, null))
            .thenReturn(extDbObjNames);

    if (fProvider == null) {
      fProvider = mock(DataBeanProvider.class);
    }
    when(fProvider.getMBeanServerConnection()).thenReturn(mockConnection);
    if (fBuffer == null) {
      fBuffer = new RESTErrorExecutionBuffer(mockConnection);
    }

    Query query = new Query();
    fBuffer.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    fBuffer.updateValues(result);
    assertEquals(fExpectedNumberOfCalls, fBuffer.getBuffer().size());
    for(int index = 0; index < fBuffer.getBuffer().size(); index++) {
      RESTWebServiceInfo expectedInfo = fExpectedInfos.get(index);
      RESTWebServiceInfo actualInfo = fBuffer.getBuffer().get(index);
      assertEquals(expectedInfo, actualInfo);
      assertEquals(expectedInfo.getErrorMessage().getMessage(), actualInfo.getErrorMessage().getMessage());
      assertEquals(expectedInfo.getErrorMessage().getStacktrace(), actualInfo.getErrorMessage().getStacktrace());
      assertEquals(expectedInfo.getErrorMessage().getType(), actualInfo.getErrorMessage().getType());
    }
  }
}
