package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public abstract class AbstractExternalDbQueryBuffer implements IUpdatableUIObject {
  private static final Logger LOGGER
                              = Logger.getLogger(ExternalDbErrorQueryBuffer.class.getName());

  private final int fMaxBufferSize;
  private final MBeanServerConnection fConnection;
  private final Comparator<SQLInfo> fTimeComparator;
  private final List<ObjectName> fObjectNames = new ArrayList();
  private static final int DEFAULT_BUFFER_SIZE = 100;

  public AbstractExternalDbQueryBuffer(MBeanServerConnection mBeanServerConnection, int maxBufferSize) {
    fConnection = mBeanServerConnection;
    fMaxBufferSize = maxBufferSize;
    fTimeComparator = new TimeComparator();
  }

  public AbstractExternalDbQueryBuffer(MBeanServerConnection mBeanServerConnection) {
    this(mBeanServerConnection, DEFAULT_BUFFER_SIZE);
  }

  @Override
  public void updateQuery(Query query) {
    try {
      fObjectNames.clear();
      fObjectNames.addAll(fConnection.queryNames(ExternalDatabase.NAME_FILTER, null));
      for (ObjectName objectName : fObjectNames) {
        query.addSubQuery(objectName, ExternalDatabase.KEY_EXECUTION_HISTORY);
      }
    } catch (IOException ex) {
      LOGGER.warning(ex.getMessage());
    }
  }

  @Override
  public void updateValues(QueryResult result) {
    for (ObjectName objectName : fObjectNames) {
      CompositeData[] exeHistory = (CompositeData[]) result.getValue(objectName,
                                                                     ExternalDatabase.KEY_EXECUTION_HISTORY);
      for (CompositeData execution : exeHistory) {
        handleExecutionData(execution, objectName);
      }
    }
    sortAndTruncateBuffer();
  }

  protected abstract void sortAndTruncateBuffer();

  protected abstract void handleExecutionData(CompositeData execution, ObjectName objectName);

  protected void setSQLInfoProperties(SQLInfo sqlInfo, ObjectName objectName, CompositeData execution) {
    sqlInfo.setApplication(objectName.getKeyProperty(ExternalDatabase.KEY_APP));
    sqlInfo.setEnvironment(objectName.getKeyProperty(ExternalDatabase.KEY_ENVIRONMENT));
    sqlInfo.setConfigName(objectName.getKeyProperty(ExternalDatabase.KEY_CONFIG));
    sqlInfo.setTime((Date) execution.get(ExternalDatabase.KEY_EXECUTION_TIMESTAMP));
    sqlInfo.setStatement(execution.get(ExternalDatabase.KEY_SQL).toString());
    sqlInfo.setExecutionTime((long) execution.get(ExternalDatabase.KEY_EXECUTION_TIME));

    CompositeData databaseElement = (CompositeData) execution.get(ExternalDatabase.KEY_DATABASE_ELEMENT);
    sqlInfo.setProcessElementId(databaseElement.get(ExternalDatabase.KEY_PROCESS_ID).toString());
  }

  public int getMaxBufferSize() {
    return fMaxBufferSize;
  }

  public Comparator<SQLInfo> getTimeComparator() {
    return fTimeComparator;
  }

  private class TimeComparator implements Comparator<SQLInfo> {

    @Override
    public int compare(SQLInfo o1, SQLInfo o2) {
      return ((Date) o1.getTime()).compareTo((Date) o2.getTime());
    }

  }

}
