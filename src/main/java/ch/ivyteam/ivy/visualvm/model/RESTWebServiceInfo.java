
package ch.ivyteam.ivy.visualvm.model;

import java.util.Date;

public class RESTWebServiceInfo implements IExecutionInfo {
  
  private static final String FAILED_MESSAGE = "REST service %s call to %s %s failed.\n\n%s";
  private String fApplication;
  private String fEnvironment;
  private String fConfigName;
  private String fProcessElementId;
  private Date fTimestamp;
  private long fExecutionTime;
  private ErrorMessage fErrorMessage;
  private String fRespondStatus;
  private String fRequestUrl;
  private String fRequestMethod;
  private String fPMVVersionName;

  public String getApplication() {
    return fApplication;
  }

  public void setApplication(String Application) {
    fApplication = Application;
  }

  public String getEnvironment() {
    return fEnvironment;
  }

  public void setEnvironment(String Environment) {
    fEnvironment = Environment;
  }

  public String getConfigName() {
    return fConfigName;
  }

  public void setConfigName(String ConfigName) {
    fConfigName = ConfigName;
  }

  public String getProcessElementId() {
    return fProcessElementId;
  }

  public void setProcessElementId(String ProcessElementId) {
    fProcessElementId = ProcessElementId;
  }

  @Override
  public Date getTime() {
    return fTimestamp;
  }

  public void setTime(Date Timestamp) {
    fTimestamp = Timestamp;
  }

  @Override
  public long getExecutionTime() {
    return fExecutionTime;
  }

  public void setExecutionTime(long ExecutionTime) {
    fExecutionTime = ExecutionTime;
  }

  public String getResponseStatus() {
    return fRespondStatus;
  }

  public void setResponseStatus(String RespondStatus) {
    fRespondStatus = RespondStatus;
  }

  public String getRequestUrl() {
    return fRequestUrl;
  }

  public void setRequestUrl(String RequestUrl) {
    fRequestUrl = RequestUrl;
  }

  public String getRequestMethod() {
    return fRequestMethod;
  }

  public void setRequestMethod(String RequestMethod) {
    fRequestMethod = RequestMethod;
  }

  public String getPMVName() {
    return fPMVVersionName;
  }

  public void setPMVName(String PMVVersionName) {
    fPMVVersionName = PMVVersionName;
  }
  
  public String getWebServiceConfig() {
    return fApplication + BACKSLASH + fEnvironment + BACKSLASH + fConfigName;
  }
  
  private String getKey() {
    return getWebServiceConfig() + BACKSLASH + fPMVVersionName + BACKSLASH + fProcessElementId 
            + BACKSLASH + fRequestUrl + BACKSLASH + fTimestamp + BACKSLASH + fExecutionTime;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof RESTWebServiceInfo) {
      RESTWebServiceInfo restWebServiceInfo = (RESTWebServiceInfo) obj;
      return getKey().equals(restWebServiceInfo.getKey());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getKey().hashCode();
  }

  public ErrorMessage getErrorMessage() {
    return fErrorMessage;
  }

  public void setErrorMessage(ErrorMessage errorInfo) {
    fErrorMessage = errorInfo;
  }
  
  public String getErrorDetail() {
    if (getErrorMessage() != null) {
      return String.format(FAILED_MESSAGE, fConfigName, fRequestMethod, fRequestUrl, getErrorMessage().toString());
    }
    return null;
  }
  
}
