package ch.ivyteam.ivy.visualvm.service;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.Date;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;

public final class LicenseInfoCollector {

  private static IvyLicenseInfo fLicenseInfo;
  private static TabularDataSupport fTabular;

  private LicenseInfoCollector() {
  }

  public static IvyLicenseInfo getLicenseInfo(TabularDataSupport tabular) {
    fLicenseInfo = new IvyLicenseInfo();
    fTabular = tabular;
    applyLicenseInfo();
    return fLicenseInfo;
  }

  private static void applyLicenseInfo() {
    applyHostName();
    applyLicenseeIndividual();
    applyLicenseeOrganisation();
    applyKeyVersion();
    applyValidFrom();
    applyValidUntil();
    applySupportRIA();
    applyElementsLimit();
    applySessionsLimit();
    appyUsersLimit();
  }

  private static void appyUsersLimit() {
    String integerString = getLicenseDetail(
            IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_USERS_LIMIT);
    int usersLimit = DataUtils.stringToInteger(integerString);
    fLicenseInfo.setServerUsersLimit(usersLimit);
  }

  private static void applySessionsLimit() {
    String integerString = getLicenseDetail(
            IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_SESSIONS_LIMIT);
    int sessionsLimit = DataUtils.stringToInteger(integerString);
    fLicenseInfo.setServerSessionsLimit(sessionsLimit);
  }

  private static void applyElementsLimit() {
    String integerString = getLicenseDetail(
            IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_ELEMENTS_LIMIT);
    int elementsLimit = DataUtils.stringToInteger(integerString);
    fLicenseInfo.setServerElementsLimit(elementsLimit);
  }

  private static void applySupportRIA() {
    String booleanString = getLicenseDetail(IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_RIA);
    boolean isSupportRIA = Boolean.valueOf(booleanString);
    fLicenseInfo.setServerRIA(isSupportRIA);
  }

  private static void applyValidUntil() {
    String dateString = getLicenseDetail(IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_VALID_UNTIL);
    Date validUntil = DataUtils.stringToDate(dateString);
    fLicenseInfo.setLicenseValidUntil(validUntil);
  }

  private static void applyValidFrom() {
    String dateString = getLicenseDetail(IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_VALID_FROM);
    Date validFrom = DataUtils.stringToDate(dateString);
    fLicenseInfo.setLicenseValidFrom(validFrom);
  }

  private static void applyKeyVersion() {
    fLicenseInfo.setLicenseKeyVersion(getLicenseDetail(
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_KEYVERSION));
  }

  private static void applyLicenseeOrganisation() {
    String licenseeOrganisation = getLicenseDetail(
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSEE_ORGANISATION);
    fLicenseInfo.setLicenseeOrganisation(licenseeOrganisation);
  }

  private static void applyLicenseeIndividual() {
    String licenseeIndividual = getLicenseDetail(
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSEE_INDIVIDUAL);
    fLicenseInfo.setLicenseeIndividual(licenseeIndividual);
  }

  private static void applyHostName() {
    String hostName = getLicenseDetail(IvyJmxConstant.IvyServer.Server.License.KEY_HOST_NAME);
    fLicenseInfo.setHostName(hostName);
  }

  private static String getLicenseDetail(String keys) {
    String result = null;
    CompositeDataSupport data = (CompositeDataSupport) fTabular.get(new String[]{keys});
    if (data != null) {
      result = data.get("propertyValue").toString();
    }
    return result;
  }

}
