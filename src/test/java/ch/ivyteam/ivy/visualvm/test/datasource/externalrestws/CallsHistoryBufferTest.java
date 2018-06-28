package ch.ivyteam.ivy.visualvm.test.datasource.externalrestws;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.ErrorMessage;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTExecutionHistoryBuffer;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class CallsHistoryBufferTest extends AbstractTest {

  public static final String PMV_NAME = "designer/ConnectivityDemos$1";
  public static final String ENVIROMENT_NAME = "Default";
  public static final String APPLICATION_NAME = "designer";
  public static final String APPROVAL_SERVICE = "approvalService";
  public static final String PERSON_SERVICE = "personService";
  public static final String APPROVAL_SERVICE_URL = "http://127.0.0.1:8081/ivy/api/designer/approve";
  public static final String PERSON_SERVICE_URL = "http://127.0.0.1:8081/ivy/api/designer/persons";
  private static SimpleDateFormat fDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  private int fExpectedNumberOfCalls;
  private List<RESTWebServiceInfo> fExpectedInfos;
  private static DataBeanProvider fProvider;
  private static RESTExecutionHistoryBuffer fBuffer;

  public CallsHistoryBufferTest(BeanTestData.Dataset dataset, int numberOfCalls, List<RESTWebServiceInfo> infos) {
    super(dataset);
    fExpectedNumberOfCalls = numberOfCalls;
    fExpectedInfos = infos;
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    List<RESTWebServiceInfo> historyInfos1 = new ArrayList<>();
    List<RESTWebServiceInfo> historyInfos2 = new ArrayList<>();
    List<RESTWebServiceInfo> historyInfos3 = new ArrayList<>();

    final RESTWebServiceInfo executionInfo1 = new RESTWebServiceInfo();
    executionInfo1.setApplication(APPLICATION_NAME);
    executionInfo1.setEnvironment(ENVIROMENT_NAME);
    executionInfo1.setConfigName(PERSON_SERVICE);
    executionInfo1.setProcessElementId("154616078A1D629D-f14");
    executionInfo1.setPMVName(PMV_NAME);
    executionInfo1.setTime(fDateFormat.parse("21/03/2017 09:17:12"));
    executionInfo1.setExecutionTime(17856586);
    executionInfo1.setRequestMethod(GET_METHOD);
    executionInfo1.setRequestUrl(PERSON_SERVICE_URL);
    executionInfo1.setResponseStatus(OK_STATUS);

    final RESTWebServiceInfo executionInfo2 = new RESTWebServiceInfo();
    executionInfo2.setApplication(APPLICATION_NAME);
    executionInfo2.setEnvironment(ENVIROMENT_NAME);
    executionInfo2.setConfigName(APPROVAL_SERVICE);
    executionInfo2.setProcessElementId("1549FE9E911A1812-f14");
    executionInfo2.setPMVName(PMV_NAME);
    executionInfo2.setTime(fDateFormat.parse("21/03/2017 09:17:20"));
    executionInfo2.setExecutionTime(10347299);
    executionInfo2.setRequestMethod("PUT");
    executionInfo2.setRequestUrl(APPROVAL_SERVICE_URL);
    executionInfo2.setResponseStatus("201 Created");

    final RESTWebServiceInfo executionInfo3 = new RESTWebServiceInfo();
    executionInfo3.setApplication(APPLICATION_NAME);
    executionInfo3.setEnvironment(ENVIROMENT_NAME);
    executionInfo3.setConfigName(PERSON_SERVICE);
    executionInfo3.setProcessElementId("15470DE765DF45FC-f19");
    executionInfo3.setPMVName(PMV_NAME);
    executionInfo3.setTime(fDateFormat.parse("21/03/2017 09:17:21"));
    executionInfo3.setExecutionTime(9358874);
    executionInfo3.setRequestMethod(GET_METHOD);
    executionInfo3.setRequestUrl(PERSON_SERVICE_URL);
    executionInfo3.setResponseStatus(OK_STATUS);

    final ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setMessage("ivy:error:script");
    errorMessage.setType("ch.ivyteam.ivy.bpm.error.BpmError");
    errorMessage.setStacktrace("BpmError ivy:error:script  Unique ID: 15AEEA814764B74E");
    final RESTWebServiceInfo errorInfo1 = new RESTWebServiceInfo();
    errorInfo1.setApplication(APPLICATION_NAME);
    errorInfo1.setEnvironment(ENVIROMENT_NAME);
    errorInfo1.setConfigName(APPROVAL_SERVICE);
    errorInfo1.setProcessElementId("157B33AB7587F1F1-f6");
    errorInfo1.setPMVName(PMV_NAME);
    errorInfo1.setTime(fDateFormat.parse("21/03/2017 09:17:22"));
    errorInfo1.setExecutionTime(35016698);
    errorInfo1.setRequestMethod(GET_METHOD);
    errorInfo1.setRequestUrl(APPROVAL_SERVICE_URL);
    errorInfo1.setResponseStatus("-1 ");
    errorInfo1.setErrorMessage(errorMessage);

    final RESTWebServiceInfo errorInfo2 = new RESTWebServiceInfo();
    errorInfo2.setApplication(APPLICATION_NAME);
    errorInfo2.setEnvironment(ENVIROMENT_NAME);
    errorInfo2.setConfigName(APPROVAL_SERVICE);
    errorInfo2.setProcessElementId("157B33AB7587F1F1-f6");
    errorInfo2.setPMVName(PMV_NAME);
    errorInfo2.setTime(fDateFormat.parse("21/03/2017 09:48:23"));
    errorInfo2.setExecutionTime(32094694);
    errorInfo2.setRequestMethod(GET_METHOD);
    errorInfo2.setRequestUrl(APPROVAL_SERVICE_URL);
    errorInfo2.setResponseStatus(OK_STATUS);
    errorInfo2.setErrorMessage(errorMessage);

    historyInfos1.add(executionInfo1);
    historyInfos1.add(executionInfo2);

    historyInfos2.add(executionInfo1);
    historyInfos2.add(executionInfo2);
    historyInfos2.add(executionInfo3);

    historyInfos3.addAll(historyInfos2);
    historyInfos3.add(errorInfo1);
    historyInfos3.add(errorInfo2);

    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externalrestws/CallsHistoryTest.xml",
            new Object[]{2, historyInfos1},
            new Object[]{3, historyInfos2},
            new Object[]{5, historyInfos3}
    );
  }
  public static final String GET_METHOD = "GET";
  public static final String OK_STATUS = "200 OK";

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
      fBuffer = new RESTExecutionHistoryBuffer(mockConnection);
    }

    Query query = new Query();
    fBuffer.updateQuery(query);
    QueryResult result = query.execute(mockConnection);
    fBuffer.updateValues(result);
    assertEquals(fExpectedNumberOfCalls, fBuffer.getBuffer().size());
    for (int index = 0; index < fBuffer.getBuffer().size(); index++) {
      RESTWebServiceInfo expectedInfo = fExpectedInfos.get(index);
      RESTWebServiceInfo actualInfo = fBuffer.getBuffer().get(index);
      assertEquals(expectedInfo, actualInfo);
      assertEquals(expectedInfo.getResponseStatus(), actualInfo.getResponseStatus());
      if (expectedInfo.getErrorMessage() != null) {
        assertEquals(expectedInfo.getErrorMessage().getMessage(), actualInfo.getErrorMessage().getMessage());
        assertEquals(expectedInfo.getErrorMessage().getStacktrace(), actualInfo.getErrorMessage().getStacktrace());
        assertEquals(expectedInfo.getErrorMessage().getType(), actualInfo.getErrorMessage().getType());
      }
    }
  }

}
