/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.util;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import java.util.logging.Logger;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;

/**
 *
 * @author thtam
 */
public final class LicenseUtils {

  private static final Logger LOGGER = Logger.getLogger(LicenseUtils.class.getName());

  private LicenseUtils() {
  }

  public static IvyLicenseInfo getLisenseInfoData(TabularDataSupport tabular) {
    IvyLicenseInfo licenseInfo = new IvyLicenseInfo();
    licenseInfo.setHostName(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_HOST_NAME));
    licenseInfo.setLicenseeIndividual(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSEE_INDIVIDUAL));
    licenseInfo.setLicenseeOrganisation(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSEE_ORGANISATION));
    licenseInfo.setLicenseKeyVersion(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_KEYVERSION));
    licenseInfo.setLicenseValidFrom(DataUtils.stringToDate(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_VALID_FROM)));
    licenseInfo.setLicenseValidUntil(DataUtils.stringToDate(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_LICENSE_VALID_UNTIL)));
    licenseInfo.setServerRIA(Boolean.valueOf(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_RIA)));
    licenseInfo.setServerElementsLimit(Integer.parseInt(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_ELEMENTS_LIMIT)));
    licenseInfo.setServerSessionsLimit(Integer.parseInt(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_SESSIONS_LIMIT)));
    licenseInfo.setServerUsersLimit(Integer.parseInt(getLicenseDetail(tabular,
            IvyJmxConstant.IvyServer.Server.License.KEY_SERVER_USERS_LIMIT)));
    return licenseInfo;
  }

  public static String getLicenseDetail(TabularDataSupport tabular, String keys) {
    CompositeDataSupport data = (CompositeDataSupport) tabular.get(new String[]{keys});
    if (data != null) {
      return data.get("propertyValue").toString();
    }
    return null;
  }

}
