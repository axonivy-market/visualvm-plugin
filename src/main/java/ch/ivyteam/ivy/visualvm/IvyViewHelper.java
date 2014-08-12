package ch.ivyteam.ivy.visualvm;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import java.awt.Image;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

public final class IvyViewHelper {
  public static final String IVY_IMAGE_PATH = "resources/icons/ivy16.png";
  public static final String IVY_IMAGE_PATH_OLD = "resources/icons/ivy16_old.png";

  private IvyViewHelper() {
  }

  public static String getViewName(Application application) {
    IvyApplicationType applicationType = (IvyApplicationType) ApplicationTypeFactory
            .getApplicationTypeFor(application);
    if (applicationType.isXpertIvyDesigner()) {
      return ContentProvider.get("XpertIvy");
    } else {
      return ContentProvider.get("AxonIvy");
    }
  }

  public static Image getViewIcon(Application application) {
    IvyApplicationType applicationType = (IvyApplicationType) ApplicationTypeFactory
            .getApplicationTypeFor(application);
    if (applicationType.isXpertIvyDesigner()) {
      return new ImageIcon(ImageUtilities.loadImage(IVY_IMAGE_PATH_OLD, true)).getImage();
    } else {
      return new ImageIcon(ImageUtilities.loadImage(IVY_IMAGE_PATH, true)).getImage();
    }
  }

}
