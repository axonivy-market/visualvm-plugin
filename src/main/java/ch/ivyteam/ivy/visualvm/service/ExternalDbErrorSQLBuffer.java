package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLErrorInfo;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class ExternalDbErrorSQLBuffer extends AbstractExternalDbSQLBuffer {
  private final List<SQLErrorInfo> fErrorInfoBuffer = new LinkedList<>();

  public ExternalDbErrorSQLBuffer(MBeanServerConnection mBeanServerConnection, int maxBufferSize) {
    super(mBeanServerConnection, maxBufferSize);
    setComparator(new TimeComparator());
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
      if (getBuffer().size() >= getMaxBufferSize()) {
        getBuffer().remove(0);
      }
      getBuffer().add(errorInfo);
    }
  }

  private class TimeComparator implements Comparator<CompositeData> {

    @Override
    public int compare(CompositeData o1, CompositeData o2) {
      return ((Date) o1.get(IvyJmxConstant.IvyServer.ExternalDatabase.KEY_EXECUTION_TIMESTAMP)).compareTo(
              (Date) o2.get(
                      IvyJmxConstant.IvyServer.ExternalDatabase.KEY_EXECUTION_TIMESTAMP));
    }

  }

}
