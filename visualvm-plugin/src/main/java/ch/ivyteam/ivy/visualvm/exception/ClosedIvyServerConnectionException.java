package ch.ivyteam.ivy.visualvm.exception;

public class ClosedIvyServerConnectionException extends RuntimeException {
  public ClosedIvyServerConnectionException(Exception ex) {
    super(ex);
  }

  public ClosedIvyServerConnectionException(String message) {
    super(message);
  }

}
