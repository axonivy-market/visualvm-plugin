package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public abstract class AbstractExternalDbQueryBuffer implements IUpdatableUIObject {
  private static final Logger LOGGER
                              = Logger.getLogger(ExternalDbErrorQueryBuffer.class.getName());
  private static final int DEFAULT_BUFFER_SIZE = 100;

  private final int fMaxBufferSize;
  private final MBeanServerConnection fConnection;
  private final Comparator<SQLInfo> fTimeComparator = new TimeComparator();
  private final List<ObjectName> fObjectNames = new ArrayList();
  private List<SQLInfo> fSQLInfoBuffer = new CopyOnWriteArrayList<>();

  public AbstractExternalDbQueryBuffer(MBeanServerConnection mBeanServerConnection, int maxBufferSize) {
    fConnection = mBeanServerConnection;
    fMaxBufferSize = maxBufferSize;
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
    List<SQLInfo> list = new LinkedList<>();
    list.addAll(fSQLInfoBuffer);
    sortBuffer(list);
    truncateBuffer(list);
  }

  protected void sortBuffer(List<SQLInfo> list) {
    DataUtils.sort(list, getTimeComparator());
  }

  protected void addSQLInfoToBuffer(SQLInfo sqlInfo) {
    if (!getBuffer().contains(sqlInfo)) {
      getBuffer().add(sqlInfo);
    }
  }

  private void truncateBuffer(List<SQLInfo> list) {
    final int fromIndex = Math.max(0, list.size() - fMaxBufferSize);
    fSQLInfoBuffer = list.subList(fromIndex, list.size());
  }

  protected abstract void handleExecutionData(CompositeData execution, ObjectName objectName);

  protected SQLInfo createSQLInfo(ObjectName objectName, CompositeData execution) {
    SQLInfo sqlInfo = new SQLInfo();
    sqlInfo.setApplication(objectName.getKeyProperty(ExternalDatabase.KEY_APP));
    sqlInfo.setEnvironment(objectName.getKeyProperty(ExternalDatabase.KEY_ENVIRONMENT));
    sqlInfo.setConfigName(objectName.getKeyProperty(ExternalDatabase.KEY_CONFIG));
    sqlInfo.setTime((Date) execution.get(ExternalDatabase.KEY_EXECUTION_TIMESTAMP));
    sqlInfo.setStatement(execution.get(ExternalDatabase.KEY_SQL).toString());
    sqlInfo.setExecutionTime((long) execution.get(ExternalDatabase.KEY_EXECUTION_TIME));

    CompositeData databaseElement = (CompositeData) execution.get(ExternalDatabase.KEY_DATABASE_ELEMENT);
    sqlInfo.setProcessElementId(databaseElement.get(ExternalDatabase.KEY_PROCESS_ID).toString());
    CompositeData error = (CompositeData) execution.get(ExternalDatabase.KEY_ERROR);
    if (error != null) {
      sqlInfo.setErrorMessage(error.get(ExternalDatabase.KEY_MESSAGE).toString());
    }
    return sqlInfo;
  }

  public List<SQLInfo> getBuffer() {
    return fSQLInfoBuffer;
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
