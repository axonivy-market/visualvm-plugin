package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class ExternalDbSlowSQLBuffer extends AbstractExternalDbSQLBuffer {

  private final List<SQLInfo> fSQLInfoBuffer = new LinkedList<>();

  public ExternalDbSlowSQLBuffer(MBeanServerConnection mBeanServerConnection, int maxBufferSize) {
    super(mBeanServerConnection, maxBufferSize);
    setComparator(new ExecutionTimeComparator());
  }

  @Override
  protected void handleExecutionData(CompositeData execution, ObjectName objectName) {
    Object error = execution.get(ExternalDatabase.KEY_ERROR);
    if (error == null) {
      SQLInfo sqlInfo = new SQLInfo();
      setSQLInfoProperties(sqlInfo, objectName, execution);
      addSQLInfoToBuffer(sqlInfo);
    }

  }

  public List<SQLInfo> getBuffer() {
    return fSQLInfoBuffer;
  }

  private void addSQLInfoToBuffer(SQLInfo errorInfo) {
    if (!getBuffer().contains(errorInfo)) {
      if (getBuffer().size() >= getMaxBufferSize()) {
        getBuffer().remove(0);
      }
      getBuffer().add(errorInfo);
    }
  }

  private class ExecutionTimeComparator implements Comparator<CompositeData> {

    @Override
    public int compare(CompositeData o1, CompositeData o2) {
      return ((Long) o1.get(ExternalDatabase.KEY_EXECUTION_TIME)).compareTo((Long) o2.get(
              ExternalDatabase.KEY_EXECUTION_TIME));
    }

  }

}
