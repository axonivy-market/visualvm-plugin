package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class ExternalDbSlowQueryBuffer extends AbstractExternalDbQueryBuffer {

  private List<SQLInfo> fSQLInfoBuffer = new LinkedList<>();
  private final Comparator<SQLInfo> fExecutionTimeComparator = new ExecutionTimeComparator();

  public ExternalDbSlowQueryBuffer(MBeanServerConnection mBeanServerConnection, int maxBufferSize) {
    super(mBeanServerConnection, maxBufferSize);
  }

  public ExternalDbSlowQueryBuffer(MBeanServerConnection mBeanServerConnection) {
    super(mBeanServerConnection);
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

  @Override
  protected void sortAndTruncateBuffer() {
    DataUtils.sort(fSQLInfoBuffer, fExecutionTimeComparator, getTimeComparator());
    final int fromIndex = Math.max(0, fSQLInfoBuffer.size() - getMaxBufferSize());
    fSQLInfoBuffer = fSQLInfoBuffer.subList(fromIndex, fSQLInfoBuffer.size());
  }

  public List<SQLInfo> getBuffer() {
    return fSQLInfoBuffer;
  }

  private void addSQLInfoToBuffer(SQLInfo errorInfo) {
    if (!getBuffer().contains(errorInfo)) {
      getBuffer().add(errorInfo);
    }
  }

  private class ExecutionTimeComparator implements Comparator<SQLInfo> {

    @Override
    public int compare(SQLInfo o1, SQLInfo o2) {
      return Long.compare(o1.getExecutionTime(), o2.getExecutionTime());
    }

  }
}
