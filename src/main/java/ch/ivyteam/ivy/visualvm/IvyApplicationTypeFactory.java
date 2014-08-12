package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.jvm.Jvm;
import com.sun.tools.visualvm.application.type.ApplicationType;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.application.type.MainClassApplicationTypeFactory;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.io.IOException;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ReflectionException;

public class IvyApplicationTypeFactory extends MainClassApplicationTypeFactory {

  private static final IvyApplicationTypeFactory INSTANCE = new IvyApplicationTypeFactory();

  static void initialize() {
    ApplicationTypeFactory.getDefault().registerProvider(INSTANCE);
  }

  static void unregister() {
    ApplicationTypeFactory.getDefault().unregisterProvider(INSTANCE);
  }

  @Override
  public ApplicationType createApplicationTypeFor(Application app, Jvm jvm,
          String mainClass) {
    if (isIvyServer(mainClass)) {
      return new IvyApplicationType(app.getPid(), isAxonIvyApp(app) ? IvyApplicationInfo.IVY_ENGINE_APP_NAME
              : IvyApplicationInfo.IVY_ENGINE_APP_NAME_OLD);
    } else if (isDesigner(mainClass, jvm)) {
      return new IvyApplicationType(app.getPid(), isXpertIvyDesigner(mainClass)
              ? IvyApplicationInfo.IVY_DESIGNER_APP_NAME_OLD : IvyApplicationInfo.IVY_DESIGNER_APP_NAME);
    }
    return null;
  }

  private boolean isIvyServer(String mainClass) {
    return "ch.ivyteam.ivy.server.ServerLauncher".equals(mainClass);
  }

  private boolean isDesigner(String mainClass, Jvm jvm) {
    return isDesignerInstallation(mainClass) || isDesignerStartedFromIde(mainClass, jvm);
  }

  private boolean isDesignerStartedFromIde(String mainClass, Jvm jvm) {
    return "org.eclipse.equinox.launcher.Main".equals(mainClass)
            && jvm.getCommandLine().contains("-product ch.ivyteam.ivy.designer.branding.product");
  }

  private boolean isDesignerInstallation(String mainClass) {
    return isAxonIvyDesigner(mainClass) || isXpertIvyDesigner(mainClass);
  }

  private boolean isXpertIvyDesigner(String mainClass) {
    return "XpertIvyDesigner".equals(mainClass);
  }

  private boolean isAxonIvyDesigner(String mainClass) {
    return "AxonIvyDesigner".equals(mainClass);
  }

  private boolean isAxonIvyApp(Application app) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(app);
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        DataBeanProvider fDataBeanProvider = new DataBeanProvider(mbsc);
        if (isAxonIvyAppMBeanProvider(fDataBeanProvider)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isAxonIvyAppMBeanProvider(DataBeanProvider dataBeanProvider) {
    try {
      AttributeList attributes
              = dataBeanProvider.getMBeanServerConnection().getAttributes(IvyJmxConstant.IVY_ENGINE,
                      new String[]{});
      return attributes != null;
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      return false;
    }
  }

}
