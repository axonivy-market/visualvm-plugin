package ch.ivyteam.ivy.visualvm.service.restwebservice;

import java.util.Comparator;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import ch.ivyteam.ivy.visualvm.model.IExecutionInfo;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.RESTWebService;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;

public class RESTSlowExecutiontBuffer extends AbstractRESTExecutionBuffer {
  
  private final Comparator<IExecutionInfo> fExecutionTimeComparator = new IExecutionInfo.ExecutionTimeComparator();

  public RESTSlowExecutiontBuffer(MBeanServerConnection serverConnection) {
    super(serverConnection);
  }

  @Override
  protected String getExecutionType() {
    return RESTWebService.KEY_EXECUTION_SLOWEST;
  }

  @Override
  protected void handleExecutionData(CompositeData executionData, ObjectName objectName) {
    Object error = executionData.get(RESTWebService.KEY_ERROR);
    if (error == null) {
      RESTWebServiceInfo createRESTWSInfo = createRESTWSInfo(executionData, objectName);
      addRESTWSInfoToBuffer(createRESTWSInfo);
    }
  }
  
  @Override
  protected void sortBuffer(List<RESTWebServiceInfo> list) {
    DataUtils.sort(list, fExecutionTimeComparator, getTimeComparator());
  }
}
