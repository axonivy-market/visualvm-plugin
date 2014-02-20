/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.model;

/**
 *
 * @author thtam
 */
public class IvyLicenseInfo {

  private String fLicenseExpirationDay;
  private String fExpirationWarning;

  public IvyLicenseInfo() {
  }

  public String getLicenseExpirationDay() {
    return fLicenseExpirationDay;
  }

  public void setLicenseExpirationDay(String licenseExpirationDay) {
    fLicenseExpirationDay = licenseExpirationDay;
  }

  public String getExpirationWarning() {
    return fExpirationWarning;
  }

  public void setExpirationWarning(String expirationWarning) {
    fExpirationWarning = expirationWarning;
  }

}
