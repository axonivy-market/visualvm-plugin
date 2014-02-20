package ch.ivyteam.ivy.visualvm.model;

public class ServerConnectorInfo {

  private String fProtocol;
  private String fPort;
  private String fScheme;

  public String getProtocol() {
    return fProtocol;
  }

  public void setProtocol(String protocol) {
    this.fProtocol = protocol;
  }

  public String getPort() {
    return fPort;
  }

  public void setPort(String port) {
    try {
      int parsedPort = Integer.parseInt(port);
      if (parsedPort <= 0) {
        throw new IllegalArgumentException(
                "The port param must be a positive integer");
      }
    } catch (NullPointerException e) {
      throw new IllegalArgumentException(
              "The port param must not be null", e);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
              "The port param must be an integer", e);
    }
    this.fPort = port;
  }

  public String getScheme() {
    return fScheme;
  }

  public void setScheme(String scheme) {
    this.fScheme = scheme;
  }

}
