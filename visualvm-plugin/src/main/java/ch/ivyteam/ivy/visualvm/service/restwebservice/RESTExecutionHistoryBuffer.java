package ch.ivyteam.ivy.visualvm.service.restwebservice;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class RESTExecutionHistoryBuffer extends AbstractRESTExecutionBuffer {

  public RESTExecutionHistoryBuffer(MBeanServerConnection serverConnection) {
    super(serverConnection);
  }

  @Override
  protected String getExecutionType() {
    return IvyJmxConstant.IvyServer.RESTWebService.KEY_EXECUTION_HISTORY;
  }

  @Override
  protected void handleExecutionData(CompositeData executionData, ObjectName objectName) {
    RESTWebServiceInfo createRESTWSInfo = createRESTWSInfo(executionData, objectName);
    addRESTWSInfoToBuffer(createRESTWSInfo);
  }

}
