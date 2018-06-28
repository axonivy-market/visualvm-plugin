package ch.ivyteam.ivy.visualvm.service.restwebservice;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.ErrorMessage;
import ch.ivyteam.ivy.visualvm.model.IExecutionInfo;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.RESTWebService;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;

public abstract class AbstractRESTExecutionBuffer implements IUpdatableUIObject {

  private static final Logger LOGGER = Logger.getLogger(AbstractRESTExecutionBuffer.class.getName());
  private static final int DEFAULT_BUFFER_SIZE = 100;
  private static final String SPACE = " ";
  private MBeanServerConnection fConnection;
  private List<ObjectName> fObjectNames = new CopyOnWriteArrayList<>();
  private List<RESTWebServiceInfo> fRestWSInfoBuffer = new CopyOnWriteArrayList<>();
  private int fMaxBufferSize;
  private Comparator<IExecutionInfo> fTimeComparator = new IExecutionInfo.TimeComparator();

  public AbstractRESTExecutionBuffer(MBeanServerConnection serverConnection, int maxBufferSize) {
    fConnection = serverConnection;
    fMaxBufferSize = maxBufferSize;
  }

  public AbstractRESTExecutionBuffer(MBeanServerConnection serverConnection) {
    this(serverConnection, DEFAULT_BUFFER_SIZE);
  }

  @Override
  public void updateQuery(Query query) {
    try {
      fObjectNames.clear();
      fObjectNames.addAll(fConnection.queryNames(RESTWebService.NAME_FILTER, null));
      for (ObjectName objectName : fObjectNames) {
        query.addSubQuery(objectName, getExecutionType());
      }
    } catch (IOException ex) {
      LOGGER.warning(ex.getMessage());
    }
  }
  
  @Override
  public void updateValues(QueryResult result) {
    for (ObjectName objectName : fObjectNames) {
      CompositeData[] executionDatas = (CompositeData[]) result.getValue(objectName, getExecutionType());
      for (CompositeData executionData : executionDatas) {
        handleExecutionData(executionData, objectName);
      }
    }
    List<RESTWebServiceInfo> list = new LinkedList<>();
    list.addAll(fRestWSInfoBuffer);
    sortBuffer(list);
    truncateBuffer(list);
  }

  protected void sortBuffer(List<RESTWebServiceInfo> list) {
    DataUtils.sort(list, getTimeComparator());
  }

  private void truncateBuffer(List<RESTWebServiceInfo> list) {
    final int fromIndex = Math.max(0, list.size() - fMaxBufferSize);
    fRestWSInfoBuffer.clear();
    fRestWSInfoBuffer.addAll(list.subList(fromIndex, list.size()));
  }

  public List<ObjectName> getObjectNames() {
    return fObjectNames;
  }

  public List<RESTWebServiceInfo> getBuffer() {
    return fRestWSInfoBuffer;
  }

  protected abstract String getExecutionType();

  protected abstract void handleExecutionData(CompositeData executionData, ObjectName objectName);

  protected RESTWebServiceInfo createRESTWSInfo(CompositeData executionData, ObjectName objectName) {
    RESTWebServiceInfo restWebServiceInfo = new RESTWebServiceInfo();
    restWebServiceInfo.setApplication(objectName.getKeyProperty(RESTWebService.KEY_APP));
    restWebServiceInfo.setEnvironment(objectName.getKeyProperty(RESTWebService.KEY_ENVIRONMENT));
    restWebServiceInfo.setConfigName(objectName.getKeyProperty(RESTWebService.KEY_CONFIG));
    restWebServiceInfo.setProcessElementId(executionData.get(RESTWebService.PROCESS_ELEMENT_ID).toString());
    restWebServiceInfo.setExecutionTime((long) executionData.get(RESTWebService.EXECUTION_TIME));
    restWebServiceInfo.setTime((Date) executionData.get(RESTWebService.TIMESTAMP));
    restWebServiceInfo.setPMVName(executionData.get(RESTWebService.PMV_VERSION_NAME).toString());
    restWebServiceInfo.setRequestMethod(executionData.get(RESTWebService.REQUEST_METHOD).toString());
    restWebServiceInfo.setRequestUrl(executionData.get(RESTWebService.REQUEST_URL).toString());
    restWebServiceInfo.setResponseStatus(executionData.get(RESTWebService.RESPONSE_STATUS_CODE) + SPACE + executionData.get(RESTWebService.RESPONSE_STATUS));
    Object error = executionData.get(RESTWebService.KEY_ERROR);
    if (error instanceof CompositeData) {
      CompositeData errorData = (CompositeData)error;
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage(errorData.get(IvyJmxConstant.ErrorKey.KEY_MESSAGE).toString());
      errorMessage.setStacktrace(errorData.get(IvyJmxConstant.ErrorKey.KEY_STACK_TRACE).toString());
      errorMessage.setType(errorData.get(IvyJmxConstant.ErrorKey.KEY_TYPE).toString());
      restWebServiceInfo.setErrorMessage(errorMessage);
    }
    return restWebServiceInfo;
  }

  protected void addRESTWSInfoToBuffer(RESTWebServiceInfo info) {
    if (!getBuffer().contains(info)) {
      getBuffer().add(info);
    }
  }

  protected Comparator<IExecutionInfo> getTimeComparator() {
    return fTimeComparator;
  }

}
