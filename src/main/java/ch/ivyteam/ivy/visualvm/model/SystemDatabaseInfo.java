package ch.ivyteam.ivy.visualvm.model;

public class SystemDatabaseInfo {

  private String fType;
  private String fVersion;
  private String fIvySystemDbVersion;
  private String fDriver;
  private String fConnectionUrl;
  private String fUsername;

  public String getType() {
    return fType;
  }

  public void setType(String type) {
    fType = type;
  }

  public String getVersion() {
    return fVersion;
  }

  public void setVersion(String version) {
    fVersion = version;
  }

  public String getDriver() {
    return fDriver;
  }

  public void setDriver(String driver) {
    fDriver = driver;
  }

  public String getConnectionUrl() {
    return fConnectionUrl;
  }

  public void setConnectionUrl(String connectionUrl) {
    fConnectionUrl = connectionUrl;
  }

  public String getUsername() {
    return fUsername;
  }

  public void setUsername(String username) {
    fUsername = username;
  }

  public String getIvySystemDbVersion() {
    return fIvySystemDbVersion;
  }

  public void setIvySystemDbVersion(String ivySystemDbVersion) {
    fIvySystemDbVersion = ivySystemDbVersion;
  }

}
