package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.util.ResourceBundle;

public final class ContentProvider {
  private static ResourceBundle fLabelResource;
  private static ResourceBundle fFormattedLabelResource;

  private ContentProvider() {
  }

  public static String get(String key) {
    ensureBundleInitialized();
    return fLabelResource.getString(key);
  }

  public static String getFormatted(String key) {
    ensureFormattedBundleInitialized();
    return fFormattedLabelResource.getString(key);
  }

  private static void ensureBundleInitialized() {
    if (fLabelResource == null) {
      fLabelResource = ResourceBundle.getBundle(
              "ch.ivyteam.ivy.visualvm.Message", DataUtils.getFormatLocale());
    }
  }

  private static void ensureFormattedBundleInitialized() {
    if (fFormattedLabelResource == null) {
      fFormattedLabelResource = ResourceBundle.getBundle(
              "ch.ivyteam.ivy.visualvm.FormattedMessage", DataUtils.getFormatLocale());
    }
  }

}
