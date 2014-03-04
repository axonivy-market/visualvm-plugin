/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.model;

import java.util.Date;

/**
 *
 * @author thtam
 */
public class IvyLicenseInfo {

  private String fHostName;
  private int fServerElementsLimit;
  private String fLicenseeOrganisation;
  private Date fLicenseValidFrom;
  private String fLicenseKeyVersion;
  private Date fLicenseValidUntil;
  private String fLicenseeIndividual;
  private boolean fServerRIA;
  private int fServerUsersLimit;
  private int fServerSessionsLimit;
  
  public IvyLicenseInfo() {
  }

  public String getHostName() {
    return fHostName;
  }

  public void setHostName(String hostName) {
    this.fHostName = hostName;
  }

  public int getServerElementsLimit() {
    return fServerElementsLimit;
  }

  public void setServerElementsLimit(int serverElementsLimit) {
    this.fServerElementsLimit = serverElementsLimit;
  }

  public String getLicenseeOrganisation() {
    return fLicenseeOrganisation;
  }

  public void setLicenseeOrganisation(String licenseeOrganisation) {
    this.fLicenseeOrganisation = licenseeOrganisation;
  }

  public Date getLicenseValidFrom() {
    return fLicenseValidFrom;
  }

  public void setLicenseValidFrom(Date licenseValidFrom) {
    this.fLicenseValidFrom = licenseValidFrom;
  }

  public String getLicenseKeyVersion() {
    return fLicenseKeyVersion;
  }

  public void setLicenseKeyVersion(String licenseKeyVersion) {
    this.fLicenseKeyVersion = licenseKeyVersion;
  }

  public Date getLicenseValidUntil() {
    return fLicenseValidUntil;
  }

  public void setLicenseValidUntil(Date licenseValidUntil) {
    this.fLicenseValidUntil = licenseValidUntil;
  }

  public String getLicenseeIndividual() {
    return fLicenseeIndividual;
  }

  public void setLicenseeIndividual(String licenseeIndividual) {
    this.fLicenseeIndividual = licenseeIndividual;
  }

  public boolean isServerRIA() {
    return fServerRIA;
  }

  public void setServerRIA(boolean serverRIA) {
    this.fServerRIA = serverRIA;
  }

  public int getServerUsersLimit() {
    return fServerUsersLimit;
  }

  public void setServerUsersLimit(int serverUsersLimit) {
    this.fServerUsersLimit = serverUsersLimit;
  }

  public int getServerSessionsLimit() {
    return fServerSessionsLimit;
  }

  public void setServerSessionsLimit(int serverSessionsLimit) {
    this.fServerSessionsLimit = serverSessionsLimit;
  }

}
