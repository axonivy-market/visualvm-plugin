package ch.ivyteam.ivy.visualvm.model;

public class SQLErrorInfo extends SQLInfo {
  private String fErrorMessage;
  private String fStacktrace;

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

}
