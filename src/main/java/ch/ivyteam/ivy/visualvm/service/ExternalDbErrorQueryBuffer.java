package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLErrorInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.LinkedList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class ExternalDbErrorQueryBuffer extends AbstractExternalDbQueryBuffer {
  private List<SQLErrorInfo> fErrorInfoBuffer = new LinkedList<>();

  public ExternalDbErrorQueryBuffer(MBeanServerConnection mBeanServerConnection, int maxBufferSize) {
    super(mBeanServerConnection, maxBufferSize);
  }

  public ExternalDbErrorQueryBuffer(MBeanServerConnection mBeanServerConnection) {
    super(mBeanServerConnection);
  }

  @Override
  protected void handleExecutionData(CompositeData execution, ObjectName objectName) {
    Object error = execution.get(ExternalDatabase.KEY_ERROR);
    if (error != null) {
      SQLErrorInfo errorInfo = new SQLErrorInfo();
      setSQLInfoProperties(errorInfo, objectName, execution);
      setErrorInfoProperties(errorInfo, execution);
      addErrorToBuffer(errorInfo);
    }
  }

  @Override
  protected void sortAndTruncateBuffer() {
    DataUtils.sort(fErrorInfoBuffer, getTimeComparator());
    final int fromIndex = Math.max(0, fErrorInfoBuffer.size() - getMaxBufferSize());
    fErrorInfoBuffer = fErrorInfoBuffer.subList(fromIndex, fErrorInfoBuffer.size());
  }

  public List<SQLErrorInfo> getBuffer() {
    return fErrorInfoBuffer;
  }

  private void setErrorInfoProperties(SQLErrorInfo errorInfo, CompositeData execution) {
    CompositeData error = (CompositeData) execution.get(ExternalDatabase.KEY_ERROR);
    errorInfo.setErrorMessage(error.get(ExternalDatabase.KEY_MESSAGE).toString());
    errorInfo.setStacktrace(error.get(ExternalDatabase.KEY_STACKTRACE).toString());
  }

  private void addErrorToBuffer(SQLErrorInfo errorInfo) {
    if (!fErrorInfoBuffer.contains(errorInfo)) {
      getBuffer().add(errorInfo);
    }
  }

}
