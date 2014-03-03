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
  private static final String TABLE_START = "<table border='0' celspacing='5' celpadding='0'>";
  private static final String TABLE_END = "</table>";
  private static final String TR_TD_START = "<tr><td>";
  private static final String TD_TR_END = "</td></tr>";
  private static final String TD_START = "<td>";
  private static final String TD_END = "</td>";

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

  public void setNamedUsers(int namedUsers) {
    fNamedUsers = namedUsers;
  }

  public void setConcurrentUsers(int concurrentUsers) {
    fConcurrentUsers = concurrentUsers;
  }

  public String toHTMLString() {
    StringBuilder html = new StringBuilder("<html><body style=\"font-family:tahoma;font-size:11\">");
    appendWarningMessage(html);

    html.append(TABLE_START);
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

    html.append(TABLE_END);
    html.append("</body></html>");
    return html.toString();
  }

  private void appendWarningMessage(StringBuilder html) {
    html.append(TABLE_START);
    appendWarningIcon(html);
    appendExpireWarning(html);
    appendUsersLimitWarning(html);
    appendSessionsLimitWarning(html);
    html.append(TABLE_END);
  }

  private void appendSessionsLimit(StringBuilder html) {
    if (fServerSessionsLimit > 0) {
      html.append(TR_TD_START);
      html.append("Concurrent Users Limit: ").append(TD_END);
      html.append(TD_START).append(fServerSessionsLimit);
      html.append(TD_TR_END);
    }
  }

  private void appendUsersLimit(StringBuilder html) {
    if (fServerUsersLimit > 0) {
      html.append(TR_TD_START);
      html.append("Named Users Limit: ").append(TD_END);
      html.append(TD_START).append(fServerUsersLimit);
      html.append(TD_TR_END);
    }
  }

  private void appendElementsLimit(StringBuilder html) {
    if (fServerElementsLimit > 0) {
      html.append(TR_TD_START);
      html.append("Elements Limit: ").append(TD_END);
      html.append(TD_START).append(fServerElementsLimit);
      html.append(TD_TR_END);
    }
  }

  private void appendSupportRIA(StringBuilder html) {
    html.append(TR_TD_START);
    html.append("Supports RIA: ").append(TD_END);
    html.append(TD_START).append(fServerRIA ? "yes" : "no");
    html.append(TD_TR_END);
  }

  private void appendValidUntil(StringBuilder html) {
    if (fLicenseValidUntil != null) {
      html.append(TR_TD_START);
      html.append("Expires: ").append(TD_END);
      html.append(TD_START).append(DataUtils.toDateString(fLicenseValidUntil));
      html.append(TD_TR_END);
    }
  }

  private void appendValidFrom(StringBuilder html) {
    if (fLicenseValidFrom != null) {
      html.append(TR_TD_START);
      html.append("Valid From: ").append(TD_END);
      html.append(TD_START).append(DataUtils.toDateString(fLicenseValidFrom));
      html.append(TD_TR_END);
    }
  }

  private void appendKeyVersion(StringBuilder html) {
    if (fLicenseKeyVersion != null) {
      html.append(TR_TD_START);
      html.append("Version: ").append(TD_END);
      html.append(TD_START).append(fLicenseKeyVersion.replace("xpertline/", ""));
      html.append(TD_TR_END);
    }
  }

  private void appendHostName(StringBuilder html) {
    if (fHostName != null) {
      html.append(TR_TD_START);
      html.append("Host Name: ").append(TD_END);
      html.append(TD_START).append(fHostName);
      html.append(TD_TR_END);
    }
  }

  private void appendLicenseeIndividual(StringBuilder html) {
    if (fLicenseeIndividual != null) {
      html.append(TR_TD_START);
      html.append("Individual: ").append(TD_END);
      html.append(TD_START).append(fLicenseeIndividual);
      html.append(TD_TR_END);
    }
  }

  private void appendLicenseeOrganisation(StringBuilder html) {
    if (fLicenseeOrganisation != null) {
      html.append(TR_TD_START);
      html.append("Organization: ").append(TD_END);
      html.append(TD_START).append(fLicenseeOrganisation);
      html.append(TD_TR_END);
    }
  }

  private void appendSessionsLimitWarning(StringBuilder html) {
    if (fServerSessionsLimit > 0 && getConcurrentUserLimitWarningInHTML() != null) {
      html.append(TR_TD_START).append(getConcurrentUserLimitWarningInHTML()).append(TD_TR_END);
    }
  }

  private void appendUsersLimitWarning(StringBuilder html) {
    if (fServerUsersLimit > 0 && getNamedUserLimitWarningInHTML() != null) {
      html.append(TR_TD_START).append(getNamedUserLimitWarningInHTML()).append(TD_TR_END);
    }
  }

  private void appendExpireWarning(StringBuilder html) {
    String expireWarning = null;
    String color = "#F38630"; //yellow
    String expireDateString = DataUtils.toDateString(fLicenseValidUntil);

    if (isLicenseError()) {
      expireWarning = MessageFormat.format(EXPIRED_WARNING, expireDateString);
      color = "red";
    } else if (isLicenseWarning()) {
      expireWarning = MessageFormat.format(EXPIRE_IN_30_DAYS_WARNING, expireDateString);
    }

    if (expireWarning == null) {
      return;
    }
    String warningMsg = "<font color='" + color + "'>" + expireWarning + "</font> ";

    html.append(TD_START).append(warningMsg).append(TD_END);
  }

  private void appendWarningIcon(StringBuilder html) {
    String iconPath = null;

    if (isLicenseError()) {
      iconPath = "/resources/icons/license_error.png";
    } else if (isLicenseWarning()) {
      iconPath = "/resources/icons/license_warning.png";
    }
    if (iconPath == null) {
      return;
    }
    String icon = "<img src='" + getClass().getResource(iconPath) + "'>";
    html.append("<td rowspan=3>").append(icon).append(TD_END);
  }

  private boolean isLicenseWarning() {
    return getRemainingTime() < 30 * LicenseInformationPanel.MILLISECONDS_IN_ONE_DAY;
  }

  private boolean isLicenseError() {
    return getRemainingTime() <= 0;
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
