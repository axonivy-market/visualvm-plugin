package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import com.sun.tools.visualvm.application.type.ApplicationType;
import java.awt.Image;
import org.openide.util.ImageUtilities;

class IvyApplicationType extends ApplicationType {

  private final int fAppPID;
  private final String fName;

  IvyApplicationType(int appPID, String name) {
    fAppPID = appPID;
    fName = name;
  }

  public int getAppPID() {
    return fAppPID;
  }

  @Override
  public String getName() {
    return fName;
  }

  @Override
  public String getVersion() {
    return "1.0";
  }

  @Override
  public String getDescription() {
    return ContentProvider.get("IvyApplication");
  }

  @Override
  public Image getIcon() {
    return ImageUtilities.loadImage(isAxonIvyApplicaton() ? IvyViewHelper.IVY_IMAGE_PATH
            : IvyViewHelper.IVY_IMAGE_PATH_OLD, true);
  }
  
  public boolean isAxonIvyApplicaton() {
    return isAxonIvyEngine() || isAxonIvyDesigner();
  }

  public boolean isAxonIvyEngine() {
    return fName.equals(IvyApplicationInfo.IVY_ENGINE_APP_NAME);
  }

  public boolean isXpertIvyServer() {
    return fName.equals(IvyApplicationInfo.IVY_ENGINE_APP_NAME_OLD);
  }

  public boolean isIvyDesigner() {
    return isAxonIvyDesigner() || isXpertIvyDesigner();
  }

  public boolean isAxonIvyDesigner() {
    return fName.equals(IvyApplicationInfo.IVY_DESIGNER_APP_NAME);
  }

  public boolean isXpertIvyDesigner() {
    return fName.equals(IvyApplicationInfo.IVY_DESIGNER_APP_NAME_OLD);
  }

}
