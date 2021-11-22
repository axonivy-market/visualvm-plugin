
package ch.ivyteam.ivy.visualvm.service.restwebservice;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;

public class RESTErrorExecutionBuffer extends AbstractRESTExecutionBuffer {

  public RESTErrorExecutionBuffer(MBeanServerConnection serverConnection) {
    super(serverConnection);
  }

  @Override
  protected String getExecutionType() {
    return IvyJmxConstant.IvyServer.RESTWebService.KEY_EXECUTION_HISTORY;
  }

  @Override
  protected void handleExecutionData(CompositeData executionData, ObjectName objectName) {
    Object error = executionData.get(IvyJmxConstant.IvyServer.RESTWebService.KEY_ERROR);
    if (error != null) {
      RESTWebServiceInfo createRESTWSInfo = createRESTWSInfo(executionData, objectName);
      addRESTWSInfoToBuffer(createRESTWSInfo);
    }
  }
  
}
