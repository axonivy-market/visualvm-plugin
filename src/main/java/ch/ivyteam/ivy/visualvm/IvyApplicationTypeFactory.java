/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.jvm.Jvm;
import com.sun.tools.visualvm.application.type.ApplicationType;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.application.type.MainClassApplicationTypeFactory;

/**
 *
 * @author rwei
 */
class IvyApplicationTypeFactory extends MainClassApplicationTypeFactory {

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
    // TODO: Specify the name of the application's main class here:
    if (isIvyServer(mainClass)) {
      return new IvyApplicationType(app.getPid(), "Xpert.ivy Server");
    } else if (isIvyDesigner(mainClass, jvm)) {
      return new IvyApplicationType(app.getPid(), "Xpert.ivy Designer");
    }
    return null;

  }

  private boolean isIvyServer(String mainClass) {
    return "ch.ivyteam.ivy.server.ServerLauncher".equals(mainClass);
  }

  private boolean isIvyDesigner(String mainClass, Jvm jvm) {
    return isDesignerInstallation(mainClass) || isDesignerStartedFromIde(mainClass, jvm);
  }

  private boolean isDesignerStartedFromIde(String mainClass, Jvm jvm) {
    return "org.eclipse.equinox.launcher.Main".equals(mainClass)
            && jvm.getCommandLine().contains("-product ch.ivyteam.ivy.designer.branding.product");
  }

  private boolean isDesignerInstallation(String mainClass) {
    return "XpertIvyDesigner".equals(mainClass);
  }

}