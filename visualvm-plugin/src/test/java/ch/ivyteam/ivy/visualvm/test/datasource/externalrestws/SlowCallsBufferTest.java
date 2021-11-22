package ch.ivyteam.ivy.visualvm.test.datasource.externalrestws;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import ch.ivyteam.ivy.visualvm.service.restwebservice.AbstractRESTExecutionBuffer;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTSlowExecutiontBuffer;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SlowCallsBufferTest extends AbstractTest{
  public static final String PMV_NAME = "designer/ConnectivityDemos$1";
  public static final String ENVIROMENT_NAME = "Default";
  public static final String APPLICATION_NAME = "designer";
  private int fExpectedNumberOfCalls;
  private List<RESTWebServiceInfo> fExpectedInfos;
  private static DataBeanProvider fProvider;
  private static AbstractRESTExecutionBuffer fBuffer;
  private static SimpleDateFormat fDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  
  public SlowCallsBufferTest(BeanTestData.Dataset dataset, int numberOfCalls, List<RESTWebServiceInfo> infos) {
    super(dataset);
    fExpectedNumberOfCalls = numberOfCalls;
    fExpectedInfos = infos;
  }

  @Parameterized.Parameters(name = "{index}")
  public static Iterable<Object[]> data() throws JAXBException, URISyntaxException, ParseException {
    List<RESTWebServiceInfo> slowInfos1 = new ArrayList<>();
    List<RESTWebServiceInfo> slowInfos2 = new ArrayList<>();
    
    final RESTWebServiceInfo slowInfo1 = new RESTWebServiceInfo();
    slowInfo1.setApplication(APPLICATION_NAME);
    slowInfo1.setEnvironment(ENVIROMENT_NAME);
    slowInfo1.setConfigName("personService");
    slowInfo1.setProcessElementId("154616078A1D629D-f14");
    slowInfo1.setPMVName(PMV_NAME);
    slowInfo1.setTime(fDateFormat.parse("21/03/2017 09:17:12"));
    slowInfo1.setExecutionTime(17856586);
    slowInfo1.setRequestMethod("GET");
    slowInfo1.setRequestUrl("http://127.0.0.1:8081/ivy/api/designer/persons");
    slowInfo1.setResponseStatus("200 OK");
    
    final RESTWebServiceInfo slowInfo2 = new RESTWebServiceInfo();
    slowInfo2.setApplication(APPLICATION_NAME);
    slowInfo2.setEnvironment(ENVIROMENT_NAME);
    slowInfo2.setConfigName("approvalService");
    slowInfo2.setProcessElementId("1549FE9E911A1812-f14");
    slowInfo2.setPMVName(PMV_NAME);
    slowInfo2.setTime(fDateFormat.parse("21/03/2017 09:17:20"));
    slowInfo2.setExecutionTime(10347299);
    slowInfo2.setRequestMethod("PUT");
    slowInfo2.setRequestUrl("http://127.0.0.1:8081/ivy/api/designer/approve");
    slowInfo2.setResponseStatus("201 Created");
    
    final RESTWebServiceInfo slowInfo3 = new RESTWebServiceInfo();
    slowInfo3.setApplication(APPLICATION_NAME);
    slowInfo3.setEnvironment(ENVIROMENT_NAME);
    slowInfo3.setConfigName("personService");
    slowInfo3.setProcessElementId("15470DE765DF45FC-f19");
    slowInfo3.setPMVName(PMV_NAME);
    slowInfo3.setTime(fDateFormat.parse("21/03/2017 09:17:21"));
    slowInfo3.setExecutionTime(9358874);
    slowInfo3.setRequestMethod("GET");
    slowInfo3.setRequestUrl("http://127.0.0.1:8081/ivy/api/designer/persons");
    slowInfo3.setResponseStatus("200 OK");
    
    slowInfos1.add(slowInfo2);
    slowInfos1.add(slowInfo1);
    
    slowInfos2.add(slowInfo3);
    slowInfos2.add(slowInfo2);
    slowInfos2.add(slowInfo1);

    return TestUtil.createTestData(
            "/ch/ivyteam/ivy/visualvm/test/datasource/externalrestws/SlowCallsTest.xml",
            new Object[]{2, slowInfos1},
            new Object[]{3, slowInfos2},
            new Object[]{3, slowInfos2}
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
      fBuffer = new RESTSlowExecutiontBuffer(mockConnection);
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
      assertEquals(expectedInfo.getResponseStatus(), actualInfo.getResponseStatus());
    }
  }
}
