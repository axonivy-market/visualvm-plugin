package ch.ivyteam.ivy.visualvm.model;

import java.util.Date;

public class SQLErrorInfo {
  private String fApplication;
  private String fEnvironment;
  private String fConfigName;
  private String fProcessElementId;
  private String fStatement;
  private String fErrorMessage;
  private String fStacktrace;
  private Date fTimestamp;

  public String getProcessElementId() {
    return fProcessElementId;
  }

  public void setProcessElementId(String processElementId) {
    this.fProcessElementId = processElementId;
  }

  public Date getTimestamp() {
    return fTimestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.fTimestamp = timestamp;
  }

  public String getStatement() {
    return fStatement;
  }

  public void setStatement(String statement) {
    this.fStatement = statement;
  }

  public String getApplication() {
    return fApplication;
  }

  public void setApplication(String application) {
    this.fApplication = application;
  }

  public String getEnvironment() {
    return fEnvironment;
  }

  public void setEnvironment(String environment) {
    this.fEnvironment = environment;
  }

  public String getConfigName() {
    return fConfigName;
  }

  public void setConfigName(String databaseName) {
    this.fConfigName = databaseName;
  }

  public String getErrorMessage() {
    return fErrorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.fErrorMessage = errorMessage;
  }

  public String getStacktrace() {
    return fStacktrace;
  }

  public void setStacktrace(String stacktrace) {
    this.fStacktrace = stacktrace;
  }

  private String getKey() {
    return fApplication + "$" + fEnvironment + "$" + fConfigName + "$" + fTimestamp.getTime();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SQLErrorInfo) {
      SQLErrorInfo errorInfo = (SQLErrorInfo) obj;
      return getKey().equals(errorInfo.getKey());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getKey().hashCode();
  }

}
