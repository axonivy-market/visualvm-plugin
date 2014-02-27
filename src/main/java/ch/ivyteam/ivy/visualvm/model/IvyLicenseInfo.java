/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.model;

import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.LicenseInformationPanel;
import java.text.MessageFormat;
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
  private long fRemaingTime;
  private static final String EXPIRE_IN_30_DAYS_WARNING
          = "Your licence will expire on {0}. "
          + "If the licence is expired the server will no longer start. "
          + "Please request a new licence now!";
  private static final String EXPIRED_WARNING
          = "Your licence has expired on {0}. "
          + "You will not be able to restart your server. "
          + "Please request a new licence now!";
  private static final String USERS_EXCEEDED_WARNING
          = "Cannot create more users because the maximum users that are allowed by your licence has "
          + "exceeded";

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
    return fLicenseKeyVersion.replace("xpertline/", "");
  }

  public void setLicenseKeyVersion(String licenceKeyVersion) {
    fLicenseKeyVersion = licenceKeyVersion;
  }

  public Date getLicenseValidUntil() {
    return fLicenseValidUntil;
  }

  public void setLicenseValidUntil(Date licenceValidUntil) {
    fLicenseValidUntil = licenceValidUntil;
    setRemainingTime(fLicenseValidUntil.getTime() - new Date().getTime());
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

  public long getRemaingTime() {
    return fRemaingTime;
  }

  public void setRemainingTime(long remaingTime) {
    fRemaingTime = remaingTime;
  }

  public String toHTMLString() {
    StringBuilder html = new StringBuilder("<html><body style=\"font-family:tahoma;font-size:11\">");
    String expire = getExpireWarningInHTML();
    if (expire != null) {
      html.append("<table border='0' celspacing='10' celpadding='0'>");
      html.append("<tr><td>").append(expire);
      html.append("</td></tr></table>");
    }

    html.append("<table border='0' celspacing='10' celpadding='0'>");
    html.append("<tr><td>Organization: <td>");
    html.append(TD_TAG).append(getLicenseeOrganisation());
    html.append(TD_TR_END_TAG);

    html.append("<tr><td>Individual: <td>");
    html.append(TD_TAG).append(getLicenseeIndividual());
    html.append(TD_TR_END_TAG);

    if (getHostName() != null) {
      html.append("<tr><td>Host Name: <td>");
      html.append(TD_TAG).append(getHostName());
      html.append(TD_TR_END_TAG);
    }

    html.append("<tr><td>Version: <td>");
    html.append(TD_TAG).append(getLicenseKeyVersion());
    html.append(TD_TR_END_TAG);

    html.append("<tr><td>Valid From: <td>");
    html.append(TD_TAG).append(DataUtils.toDateString(getLicenseValidFrom()));
    html.append(TD_TR_END_TAG);

    html.append("<tr><td>Expires: <td>");
    html.append(TD_TAG).append(DataUtils.toDateString(getLicenseValidUntil()));
    html.append(TD_TR_END_TAG);

    html.append("<tr><td>Supports RIA: <td>");
    html.append(TD_TAG).append(isServerRIA() ? "yes" : "no");
    html.append(TD_TR_END_TAG);

    if (getServerElementsLimit() > 0) {
      html.append("<tr><td>Elements Limit: <td>");
      html.append(TD_TAG).append(getServerElementsLimit());
      html.append(TD_TR_END_TAG);
    }

    if (getServerUsersLimit() > 0) {
      html.append("<tr><td>Named Users Limit: <td>");
      html.append(TD_TAG).append(getServerUsersLimit());
      html.append(TD_TR_END_TAG);
    }

    if (getServerSessionsLimit() > 0) {
      html.append("<tr><td>Current Users Limit: <td>");
      html.append(TD_TAG).append(getServerSessionsLimit());
      html.append(TD_TR_END_TAG);
    }
    html.append("</table>");

    html.append("</body></html>");
    return html.toString();
  }

  public static final String TD_TR_END_TAG = "</td></tr>";
  private static final String TD_TAG = "<td>";

  public String getExpireWarningInHTML() {
    long delta = getRemaingTime();
    String expireWarning = null, color = "#F38630";
    String expireDateString = DataUtils.toDateString(getLicenseValidUntil());
    if (delta <= 0) {
      expireWarning = MessageFormat.format(EXPIRED_WARNING, expireDateString);
      color = "red";
    } else if (delta < 30 * LicenseInformationPanel.MILLISECONDS_IN_ONE_DAY) {
      expireWarning = MessageFormat.format(EXPIRE_IN_30_DAYS_WARNING, expireDateString);
    }
    if (expireWarning == null) {
      return null;
    }
    return "<font color='" + color + "'>" + expireWarning + "</font> ";
  }

}
