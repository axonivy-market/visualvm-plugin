package ch.ivyteam.ivy.visualvm.model;

import java.util.Date;

public class IvyApplicationInfo {

  private String fApplicationName;
  private String fVersion;
  private Date fBuildDate;
  private String fInstallationDirectory;
  private boolean fDeveloperMode;
  private boolean fReleaseCandidate;

  public IvyApplicationInfo() {
  }

  public String getApplicationName() {
    return fApplicationName;
  }

  public void setApplicationName(String applicationName) {
    this.fApplicationName = applicationName;
  }

  public String getVersion() {
    return fVersion;
  }

  public void setVersion(String version) {
    this.fVersion = version;
  }

  public Date getBuildDate() {
    return fBuildDate;
  }

  public void setBuildDate(Date buildDate) {
    this.fBuildDate = buildDate;
  }

  public String getInstallationDirectory() {
    return fInstallationDirectory;
  }

  public void setInstallationDirectory(String installationDirectory) {
    this.fInstallationDirectory = installationDirectory;
  }

  public boolean isDeveloperMode() {
    return fDeveloperMode;
  }

  public void setDeveloperMode(boolean developerMode) {
    this.fDeveloperMode = developerMode;
  }

  public boolean isReleaseCandidate() {
    return fReleaseCandidate;
  }

  public void setReleaseCandidate(boolean releaseCandidate) {
    this.fReleaseCandidate = releaseCandidate;
  }

}
