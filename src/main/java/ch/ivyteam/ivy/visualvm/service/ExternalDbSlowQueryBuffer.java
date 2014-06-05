package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.Comparator;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class ExternalDbSlowQueryBuffer extends AbstractExternalDbQueryBuffer {

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
      SQLInfo sqlInfo = createSQLInfo(objectName, execution);
      addSQLInfoToBuffer(sqlInfo);
    }
  }

  @Override
  protected void sortBuffer(List<SQLInfo> list) {
    DataUtils.sort(list, fExecutionTimeComparator, getTimeComparator());
  }

  private class ExecutionTimeComparator implements Comparator<SQLInfo> {

    @Override
    public int compare(SQLInfo o1, SQLInfo o2) {
      return Long.compare(o1.getExecutionTime(), o2.getExecutionTime());
    }

  }
}
