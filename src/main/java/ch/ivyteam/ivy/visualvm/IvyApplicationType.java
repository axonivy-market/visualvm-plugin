package ch.ivyteam.ivy.visualvm;

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
    return ContentProvider.get("AxonIvy");
  }

  @Override
  public Image getIcon() {
    return ImageUtilities.loadImage(IvyView.IVY_IMAGE_PATH, true);
  }

}
