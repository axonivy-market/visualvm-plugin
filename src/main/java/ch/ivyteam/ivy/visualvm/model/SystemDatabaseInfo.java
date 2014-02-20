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
    this.fType = type;
  }

  public String getVersion() {
    return fVersion;
  }

  public void setVersion(String version) {
    this.fVersion = version;
  }

  public String getDriver() {
    return fDriver;
  }

  public void setDriver(String driver) {
    this.fDriver = driver;
  }

  public String getConnectionUrl() {
    return fConnectionUrl;
  }

  public void setConnectionUrl(String connectionUrl) {
    this.fConnectionUrl = connectionUrl;
  }

  public String getUsername() {
    return fUsername;
  }

  public void setUsername(String username) {
    this.fUsername = username;
  }

  public String getIvySystemDbVersion() {
    return fIvySystemDbVersion;
  }

  public void setIvySystemDbVersion(String ivySystemDbVersion) {
    this.fIvySystemDbVersion = ivySystemDbVersion;
  }

}
