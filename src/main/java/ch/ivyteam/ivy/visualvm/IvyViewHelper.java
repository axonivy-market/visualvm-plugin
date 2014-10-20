package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.type.ApplicationType;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.awt.Image;
import java.io.IOException;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

public final class IvyViewHelper {

  public static final String IVY_IMAGE_PATH = "resources/icons/ivy16.png";
  public static final String IVY_IMAGE_PATH_OLD = "resources/icons/ivy16_old.png";

  private IvyViewHelper() {
  }

  public static String getViewName(Application application) {
    if (isLocalAxonIvyApplication(application) || isRemoteAxonIvyApplication(application)) {
      return ContentProvider.get("AxonIvy");
    } else {
      return ContentProvider.get("XpertIvy");
    }
  }

  public static Image getViewIcon(Application application) {
    if (isLocalAxonIvyApplication(application) || isRemoteAxonIvyApplication(application)) {
      return new ImageIcon(ImageUtilities.loadImage(IVY_IMAGE_PATH, true)).getImage();
    } else {
      return new ImageIcon(ImageUtilities.loadImage(IVY_IMAGE_PATH_OLD, true)).getImage();
    }
  }

  private static boolean isLocalAxonIvyApplication(Application application) {
    ApplicationType app = ApplicationTypeFactory.getApplicationTypeFor(application);
    if (app instanceof IvyApplicationType) {
      IvyApplicationType applicationType = (IvyApplicationType) app;
      return applicationType.isAxonIvyApplication();
    } else {
      return false;
    }
  }

  // axon ivy 5.1 or younger
  private static boolean isRemoteAxonIvyApplication(Application application) {
    DataBeanProvider beanProvider = createMbeanProvider(application);
    return beanProvider == null ? false : isRemoteIvyAppication51OrYounger(beanProvider);
  }

  /**
   * This method also returns true for 5.1. Use isRemoteIvyAppication51OrYounger() to check for 5.1 and
   * younger
   */
  public static boolean isRemoteIvyApplication50OrOlder(DataBeanProvider dataBeanProvider) {
    if (dataBeanProvider == null) {
      return false;
    }

    boolean isIvyApp = false;
    try {
      MBeanServerConnection conn = dataBeanProvider.getMBeanServerConnection();
      if (conn != null) {
        AttributeList attributes = conn.getAttributes(new ObjectName("ivy:type=Engine"), new String[]{});
        isIvyApp = attributes != null;
      }
    } catch (MalformedObjectNameException | IOException | InstanceNotFoundException | ReflectionException ex) {
      isIvyApp = false;
    }
    return isIvyApp;
  }

  public static boolean isRemoteIvyAppication51OrYounger(DataBeanProvider dataBeanProvider) {
    IvyApplicationInfo appInfo = dataBeanProvider.getGenericData().getApplicationInfo();
    if ((appInfo != null) && DataUtils.checkIvyVersion(appInfo.getVersion(), 5, 1)) {
      return IvyApplicationInfo.IVY_ENGINE_APP_NAME.equals(appInfo.getApplicationName())
              || IvyApplicationInfo.IVY_DESIGNER_APP_NAME.equals(appInfo.getApplicationName());
    }
    return false;
  }

  public static DataBeanProvider createMbeanProvider(Application application) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        return new DataBeanProvider(mbsc);
      }
    }
    return null;
  }
}
