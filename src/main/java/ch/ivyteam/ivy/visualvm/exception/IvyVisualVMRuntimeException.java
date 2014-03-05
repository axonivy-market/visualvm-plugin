package ch.ivyteam.ivy.visualvm.exception;

public class IvyVisualVMRuntimeException extends RuntimeException {

  public IvyVisualVMRuntimeException(Exception e) {
    super(e);
  }

  public IvyVisualVMRuntimeException(String message, Exception e) {
    super(message, e);
  }

}
