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
  private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

  public IvyLicenseInfo() {
  }

  public String getHostName() {
    return fHostName;
  }

  public void setHostName(String hostName) {
    fHostName = hostName;
  }

  public int getServerElementsLimit() {
    return fServerElementsLimit;
  }

  public void setServerElementsLimit(int serverElementsLimit) {
    fServerElementsLimit = serverElementsLimit;
  }

  public String getLicenseeOrganisation() {
    return fLicenseeOrganisation;
  }

  public void setLicenseeOrganisation(String licenceeOrganisation) {
    fLicenseeOrganisation = licenceeOrganisation;
  }

  public Date getLicenseValidFrom() {
    return fLicenseValidFrom;
  }

  public void setLicenseValidFrom(Date licenceValidFrom) {
    fLicenseValidFrom = licenceValidFrom;
  }

  public String getLicenseKeyVersion() {
    return fLicenseKeyVersion;
  }

  public void setLicenseKeyVersion(String licenceKeyVersion) {
    fLicenseKeyVersion = licenceKeyVersion;
  }

  public Date getLicenseValidUntil() {
    return fLicenseValidUntil;
  }

  public void setLicenseValidUntil(Date licenceValidUntil) {
    fLicenseValidUntil = licenceValidUntil;
  }

  public String getLicenseeIndividual() {
    return fLicenseeIndividual;
  }

  public void setLicenseeIndividual(String licenceeIndividual) {
    fLicenseeIndividual = licenceeIndividual;
  }

  public boolean isServerRIA() {
    return fServerRIA;
  }

  public void setServerRIA(boolean isServerRIA) {
    fServerRIA = isServerRIA;
  }

  public int getServerUsersLimit() {
    return fServerUsersLimit;
  }

  public void setServerUsersLimit(int serverUsersLimit) {
    fServerUsersLimit = serverUsersLimit;
  }

  public int getServerSessionsLimit() {
    return fServerSessionsLimit;
  }

  public void setServerSessionsLimit(int serverSessionsLimit) {
    fServerSessionsLimit = serverSessionsLimit;
  }

  public int getRemaingDays() {
    long delta = fLicenseValidUntil.getTime() - new Date().getTime();
    int remaingDays = (int) (delta / MILLISECONDS_IN_DAY);
    return remaingDays;
  }

}
