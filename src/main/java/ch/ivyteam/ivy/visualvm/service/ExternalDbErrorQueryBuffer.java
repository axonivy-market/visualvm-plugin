package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class ExternalDbErrorQueryBuffer extends AbstractExternalDbQueryBuffer {

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
      SQLInfo sqlInfo = createSQLInfo(objectName, execution);
      addSQLInfoToBuffer(sqlInfo);
    }
  }

}
