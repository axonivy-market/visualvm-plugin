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
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class IvyApplicationTypeFactory extends MainClassApplicationTypeFactory {

  private static final IvyApplicationTypeFactory INSTANCE = new IvyApplicationTypeFactory();
  private static final int TIME_OUT = 10000;
  private static final String SERVER_LAUNCHER = "ch.ivyteam.ivy.server.ServerLauncher";
  private static final String OSGI_LAUNCHER = "org.eclipse.equinox.launcher.Main";
  private static final String ARGUMENT_OSGI_LAUNCHER_SERVER = "-application ch.ivyteam.ivy.server.exec.engine";
  private static final String ARGUMENT_OSGI_LAUNCHER_DESIGNER = "-product ch.ivyteam.ivy.designer.branding.product";
  private static final String MAIN_CLASS_XPERT_IVY_DESIGNER = "XpertIvyDesigner";
  private static final String MAIN_CLASS_AXON_IVY_DESIGNER = "AxonIvyDesigner";

  static void initialize() {
    ApplicationTypeFactory.getDefault().registerProvider(INSTANCE);
  }

  static void unregister() {
    ApplicationTypeFactory.getDefault().unregisterProvider(INSTANCE);
  }

  @Override
  public ApplicationType createApplicationTypeFor(Application app, Jvm jvm,
          String mainClass) {
    if (isIvyServer(mainClass, jvm)) {
      return new IvyApplicationType(app.getPid(), isAxonIvyServer(app)
              ? IvyApplicationInfo.IVY_ENGINE_APP_NAME : IvyApplicationInfo.IVY_ENGINE_APP_NAME_OLD);
    } else if (isDesigner(mainClass, jvm)) {
      return new IvyApplicationType(app.getPid(), isXpertIvyDesigner(mainClass)
              ? IvyApplicationInfo.IVY_DESIGNER_APP_NAME_OLD : IvyApplicationInfo.IVY_DESIGNER_APP_NAME);
    }
    return null;
  }

  private boolean isIvyServer(String mainClass, Jvm jvm) {
    return isOldTypeIvyEngine(mainClass) || isOSGiBasedIvyEngine(mainClass, jvm);
  }

    private boolean isOldTypeIvyEngine(String mainClass) {
        return SERVER_LAUNCHER.equals(mainClass);
    }

    private boolean isOSGiBasedIvyEngine(String mainClass, Jvm jvm) {
        return OSGI_LAUNCHER.equals(mainClass) && jvm.getCommandLine().contains(ARGUMENT_OSGI_LAUNCHER_SERVER);
    }

  private boolean isDesigner(String mainClass, Jvm jvm) {
    return isDesignerInstallation(mainClass) || isDesignerStartedFromIde(mainClass, jvm);
  }

  private boolean isDesignerStartedFromIde(String mainClass, Jvm jvm) {
    return OSGI_LAUNCHER.equals(mainClass)
            && jvm.getCommandLine().contains(ARGUMENT_OSGI_LAUNCHER_DESIGNER);
  }

  private boolean isDesignerInstallation(String mainClass) {
    return isAxonIvyDesigner(mainClass) || isXpertIvyDesigner(mainClass);
  }

  private boolean isXpertIvyDesigner(String mainClass) {
    return MAIN_CLASS_XPERT_IVY_DESIGNER.equals(mainClass);
  }

  private boolean isAxonIvyDesigner(String mainClass) {
    return MAIN_CLASS_AXON_IVY_DESIGNER.equals(mainClass);
  }

  public boolean isAxonIvyAppMBeanProvider(DataBeanProvider dataBeanProvider) {
    return checkAvailableMBean(dataBeanProvider, IvyJmxConstant.IVY_ENGINE);
  }
  
  private boolean isXpertIvyAppMBeanProvider(DataBeanProvider dataBeanProvider) {
    return checkAvailableMBean(dataBeanProvider, IvyJmxConstant.XPERT_IVY_ENGINE);
  }
  
  private boolean checkAvailableMBean(DataBeanProvider dataBeanProvider, ObjectName objectName) {
     try {
      AttributeList attributes
              = dataBeanProvider.getMBeanServerConnection().getAttributes(objectName,
                      new String[]{});
      return attributes != null;
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      return false;
    } 
  }
  
  private DataBeanProvider getDataBeanProvider(Application app) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(app);
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        return new DataBeanProvider(mbsc);
      }
    }
    return null;
  }
  
  private boolean isAxonIvyServer(Application app) {
    long start = System.currentTimeMillis();
    do {
      DataBeanProvider dataBeanProvider = getDataBeanProvider(app);
      if (dataBeanProvider != null) {
        if (isAxonIvyAppMBeanProvider(dataBeanProvider)) {
          return true;
        } else if (isXpertIvyAppMBeanProvider(dataBeanProvider)) {
          return false;
        }
      }
    } while (System.currentTimeMillis() - start < TIME_OUT);
    return false;
  }


}
