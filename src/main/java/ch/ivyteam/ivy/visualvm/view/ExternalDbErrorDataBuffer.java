package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.SQLErrorInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class ExternalDbErrorDataBuffer implements IUpdatableUIObject {
  private static final String KEY_EXECUTION_HISTORY = "executionHistory";
  private static final String KEY_APP = "application";
  private static final String KEY_ENVIRONMENT = "environment";
  private static final String KEY_CONFIG = "name";
  private static final String KEY_EXECUTION_TIME = "executionTimestamp";
  private static final String KEY_SQL = "sql";
  private static final String KEY_DATABASE_ELEMENT = "databaseElement";
  private static final String KEY_PROCESS_ID = "processElementId";
  private static final String KEY_ERROR = "error";
  private static final String KEY_STACKTRACE = "stackTrace";
  private static final String KEY_MESSAGE = "message";
  private static final String KEY_OBJECT_NAME_PATTERN
          = "Xpert.ivy Server:type=External Database,application=*,environment=*,name=*";

  private static final Logger LOGGER
          = Logger.getLogger(ExternalDbErrorDataBuffer.class.getName());

  private final int fMaxBufferSize;
  private final MBeanServerConnection fConnection;
  private ObjectName fObjectNamePattern;
  private final List<ObjectName> fObjectNames = new ArrayList();
  private final List<SQLErrorInfo> fErrorInfoBuffer = new LinkedList<>();

  ExternalDbErrorDataBuffer(MBeanServerConnection mBeanServerConnection, int maxBufferSize) {
    fConnection = mBeanServerConnection;
    fMaxBufferSize = maxBufferSize;
  }

  @Override
  public void updateQuery(Query query) {
    try {
      fObjectNames.clear();
      fObjectNames.addAll(fConnection.queryNames(getObjectNamePattern(), null));
      for (ObjectName objectName : fObjectNames) {
        query.addSubQuery(objectName, KEY_EXECUTION_HISTORY);
      }
    } catch (IOException ex) {
      LOGGER.warning(ex.getMessage());
    }
  }

  @Override
  public void updateValues(QueryResult result) {
    for (ObjectName objectName : fObjectNames) {
      CompositeData[] exeHistory = (CompositeData[]) result.getValue(objectName, KEY_EXECUTION_HISTORY);
      for (CompositeData execution : exeHistory) {
        Object error = execution.get(KEY_ERROR);
        if (error != null) {
          addErrorToBuffer(createErrorInfo(objectName, execution));
        }
      }
    }
  }

  private ObjectName getObjectNamePattern() {
    if (fObjectNamePattern == null) {
      try {
        fObjectNamePattern = new ObjectName(KEY_OBJECT_NAME_PATTERN);
      } catch (MalformedObjectNameException ex) {
        LOGGER.warning(ex.getMessage());
      }
    }
    return fObjectNamePattern;
  }

  private void addErrorToBuffer(SQLErrorInfo errorInfo) {
    if (!fErrorInfoBuffer.contains(errorInfo)) {
      if (getErrorInfoBuffer().size() >= fMaxBufferSize) {
        getErrorInfoBuffer().remove(0);
      }
      getErrorInfoBuffer().add(errorInfo);
    }
  }

  private SQLErrorInfo createErrorInfo(ObjectName objectName, CompositeData execution) {
    SQLErrorInfo errorInfo = new SQLErrorInfo();

    errorInfo.setApplication(objectName.getKeyProperty(KEY_APP));
    errorInfo.setEnvironment(objectName.getKeyProperty(KEY_ENVIRONMENT));
    errorInfo.setConfigName(objectName.getKeyProperty(KEY_CONFIG));
    errorInfo.setTime((Date) execution.get(KEY_EXECUTION_TIME));
    errorInfo.setStatement(execution.get(KEY_SQL).toString());

    CompositeData databaseElement = (CompositeData) execution.get(KEY_DATABASE_ELEMENT);
    errorInfo.setProcessElementId(databaseElement.get(KEY_PROCESS_ID).toString());

    CompositeData error = (CompositeData) execution.get(KEY_ERROR);
    errorInfo.setErrorMessage(error.get(KEY_MESSAGE).toString());
    errorInfo.setStacktrace(error.get(KEY_STACKTRACE).toString());

    return errorInfo;
  }

  public List<SQLErrorInfo> getErrorInfoBuffer() {
    return fErrorInfoBuffer;
  }

}
