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
  private Long fRemainingTime;
  private int fNamedUsers;
  private int fConcurrentUsers;
  private static final String TD_TR_END_TAG = "</td></tr>";
  private static final String TD_TAG = "<td>";
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
  private static final String SESSIONS_EXCEEDED_WARNING
          = "The maximum sessions that are allowed by your licence has been reached.";
  private static final String SESSIONS_EXCEEDED_50_PERCENT_WARNING
          = "Cannot create session because the maximum session that are allowed by your licence has exceeded "
          + "by a factor of 50%.";

  public IvyLicenseInfo() {
  }

  public void setHostName(String hostName) {
    fHostName = hostName;
  }

  public void setServerElementsLimit(int serverElementsLimit) {
    fServerElementsLimit = serverElementsLimit;
  }

  public void setLicenseeOrganisation(String licenceeOrganisation) {
    fLicenseeOrganisation = licenceeOrganisation;
  }

  public void setLicenseValidFrom(Date licenceValidFrom) {
    fLicenseValidFrom = licenceValidFrom;
  }

  public void setLicenseKeyVersion(String licenceKeyVersion) {
    fLicenseKeyVersion = licenceKeyVersion;
  }

  public void setLicenseValidUntil(Date licenceValidUntil) {
    fLicenseValidUntil = licenceValidUntil;
    if (fLicenseValidUntil != null) {
      setRemainingTime(fLicenseValidUntil.getTime() - new Date().getTime());
    } else {
      setRemainingTime(null);
    }
  }

  public void setLicenseeIndividual(String licenceeIndividual) {
    fLicenseeIndividual = licenceeIndividual;
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

  public long getRemainingTime() {
    return fRemainingTime;
  }

  public void setRemainingTime(Long remainingTime) {
    fRemainingTime = remainingTime;
  }

  public String toHTMLString() {
    StringBuilder html = new StringBuilder("<html><body style=\"font-family:tahoma;font-size:11\">");
    appendExpireWarning(html);
    appendUsersLimitWarning(html);
    appendSessionsLimitWarning(html);
    appendLicenseeOrganisation(html);
    appendLicenseeIndividual(html);
    appendHostName(html);
    appendKeyVersion(html);
    appendValidFrom(html);
    appendValidUntil(html);
    appendSupportRIA(html);
    appendElementsLimit(html);
    appendUsersLimit(html);
    appendSessionsLimit(html);

    html.append("</table>");
    html.append("</body></html>");
    return html.toString();
  }

  private void appendSessionsLimit(StringBuilder html) {
    if (fServerSessionsLimit > 0) {
      html.append("<tr><td>Concurrent Users Limit: <td>");
      html.append(TD_TAG).append(fServerSessionsLimit);
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendUsersLimit(StringBuilder html) {
    if (fServerUsersLimit > 0) {
      html.append("<tr><td>Named Users Limit: <td>");
      html.append(TD_TAG).append(fServerUsersLimit);
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendElementsLimit(StringBuilder html) {
    if (fServerElementsLimit > 0) {
      html.append("<tr><td>Elements Limit: <td>");
      html.append(TD_TAG).append(fServerElementsLimit);
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendSupportRIA(StringBuilder html) {
    html.append("<tr><td>Supports RIA: <td>");
    html.append(TD_TAG).append(fServerRIA ? "yes" : "no");
    html.append(TD_TR_END_TAG);
  }

  private void appendValidUntil(StringBuilder html) {
    if (fLicenseValidUntil != null) {
      html.append("<tr><td>Expires: <td>");
      html.append(TD_TAG).append(DataUtils.toDateString(fLicenseValidUntil));
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendValidFrom(StringBuilder html) {
    if (fLicenseValidFrom != null) {
      html.append("<tr><td>Valid From: <td>");
      html.append(TD_TAG).append(DataUtils.toDateString(fLicenseValidFrom));
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendKeyVersion(StringBuilder html) {
    if (fLicenseKeyVersion != null) {
      html.append("<tr><td>Version: <td>");
      html.append(TD_TAG).append(fLicenseKeyVersion.replace("xpertline/", ""));
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendHostName(StringBuilder html) {
    if (fHostName != null) {
      html.append("<tr><td>Host Name: <td>");
      html.append(TD_TAG).append(fHostName);
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendLicenseeIndividual(StringBuilder html) {
    if (fLicenseeIndividual != null) {
      html.append("<tr><td>Individual: <td>");
      html.append(TD_TAG).append(fLicenseeIndividual);
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendLicenseeOrganisation(StringBuilder html) {
    if (fLicenseeOrganisation != null) {
      html.append("<table border='0' celspacing='10' celpadding='0'>");
      html.append("<tr><td>Organization: <td>");
      html.append(TD_TAG).append(fLicenseeOrganisation);
      html.append(TD_TR_END_TAG);
    }
  }

  private void appendSessionsLimitWarning(StringBuilder html) {
    if (fServerSessionsLimit > 0 && getConcurrentUserLimitWarningInHTML() != null) {
      html.append("<table border='0' celspacing='10' celpadding='0'>");
      html.append("<tr><td>").append(getConcurrentUserLimitWarningInHTML());
      html.append("</td></tr></table>");
    }
  }

  private void appendUsersLimitWarning(StringBuilder html) {
    if (fServerUsersLimit > 0 && getNamedUserLimitWarningInHTML() != null) {
      html.append("<table border='0' celspacing='10' celpadding='0'>");
      html.append("<tr><td>").append(getNamedUserLimitWarningInHTML());
      html.append("</td></tr></table>");
    }
  }

  private void appendExpireWarning(StringBuilder html) {
    if (getExpireWarningInHTML() != null) {
      html.append("<table border='0' celspacing='10' celpadding='0'>");
      html.append("<tr><td>").append(getExpireWarningInHTML());
      html.append("</td></tr></table>");
    }
  }

  private String getExpireWarningInHTML() {
    long delta = getRemainingTime();
    String expireWarning = null, color = "#F38630";
    String expireDateString = DataUtils.toDateString(fLicenseValidUntil);
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

  public void setNamedUsers(int namedUsers) {
    fNamedUsers = namedUsers;
  }

  public void setConcurrentUsers(int concurrentUsers) {
    fConcurrentUsers = concurrentUsers;
  }

  private String getNamedUserLimitWarningInHTML() {
    String warning = null, color = "red";
    if (fNamedUsers >= fServerUsersLimit) {
      warning = USERS_EXCEEDED_WARNING;
    }
    if (warning == null) {
      return null;
    }
    return "<font color='" + color + "'>" + warning + "</font> ";
  }

  private String getConcurrentUserLimitWarningInHTML() {
    String warning = null, color = "red";
    double factor = fConcurrentUsers / fServerSessionsLimit;
    if (factor >= 1 && factor < 1.5) {
      warning = SESSIONS_EXCEEDED_WARNING;
    } else if (factor >= 1.5) {
      warning = SESSIONS_EXCEEDED_50_PERCENT_WARNING;
    }
    if (warning == null) {
      return null;
    }
    return "<font color='" + color + "'>" + warning + "</font> ";
  }

}
