package ch.ivyteam.ivy.visualvm.model;

public class ErrorMessage {

  private String fMessage;
  private String fType;
  private String fStacktrace;

  public String getMessage() {
    return fMessage;
  }

  public void setMessage(String message) {
    fMessage = message;
  }

  public String getType() {
    return fType;
  }

  public void setType(String type) {
    fType = type;
  }

  public String getStacktrace() {
    return fStacktrace;
  }

  public void setStacktrace(String stacktrace) {
    fStacktrace = stacktrace;
  }

  @Override
  public String toString() {
    return fStacktrace;
  }
  
}
