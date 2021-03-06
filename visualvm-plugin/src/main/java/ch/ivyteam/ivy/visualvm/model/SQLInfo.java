package ch.ivyteam.ivy.visualvm.model;

import java.util.Date;

public class SQLInfo implements IExecutionInfo {
  
  private String fApplication;
  private String fEnvironment;
  private String fConfigName;
  private String fProcessElementId;
  private String fStatement;
  private Date fTimestamp;
  private long fExecutionTime;
  private String fErrorMessage;

  public String getErrorMessage() {
    return fErrorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.fErrorMessage = errorMessage;
  }

  public String getProcessElementId() {
    return fProcessElementId.replace("-bean", "");
  }

  public void setProcessElementId(String processElementId) {
    this.fProcessElementId = processElementId;
  }

  @Override
  public Date getTime() {
    return fTimestamp;
  }

  public void setTime(Date timestamp) {
    this.fTimestamp = timestamp;
  }

  public String getStatement() {
    return fStatement;
  }

  public void setStatement(String statement) {
    this.fStatement = statement;
  }

  @Override
  public String getApplication() {
    return fApplication;
  }

  public void setApplication(String application) {
    this.fApplication = application;
  }

  @Override
  public String getEnvironment() {
    return fEnvironment;
  }

  public void setEnvironment(String environment) {
    this.fEnvironment = environment;
  }

  @Override
  public String getConfigName() {
    return fConfigName;
  }

  public void setConfigName(String databaseName) {
    this.fConfigName = databaseName;
  }

  @Override
  public long getExecutionTime() {
    return fExecutionTime;
  }

  public void setExecutionTime(long executionTime) {
    this.fExecutionTime = executionTime;
  }

  public String getDbConfig() {
    return fApplication + BACKSLASH + fEnvironment + BACKSLASH + fConfigName;
  }

  private String getKey() {
    return fApplication + BACKSLASH + fEnvironment + BACKSLASH + fConfigName
            + BACKSLASH + fTimestamp.getTime() + BACKSLASH + fExecutionTime;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SQLInfo) {
      SQLInfo sqlInfo = (SQLInfo) obj;
      return getKey().equals(sqlInfo.getKey());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getKey().hashCode();
  }

}
