package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.io.IOException;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ReflectionException;

public class IvyViewProvider extends DataSourceViewProvider<Application> {

  private static final DataSourceViewProvider<Application> INSTANCE = new IvyViewProvider();
  private DataBeanProvider fDataBeanProvider;

  @Override
  protected boolean supportsViewFor(Application application) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
    boolean isSupported = false;
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        fDataBeanProvider = new DataBeanProvider(mbsc);
        isSupported = isSupportedIvyApp50AndLater(fDataBeanProvider);
      }
    }
    if (!isSupported && isIvyApplicationType(application)) {
      // Ivy 4.x
      isSupported = true;
      fDataBeanProvider = null;
    }
    return isSupported;
  }

  private boolean isIvyApplicationType(Application application) {
    return ApplicationTypeFactory.getApplicationTypeFor(application) instanceof IvyApplicationType;
  }

  public boolean isSupportedIvyApp50AndLater(DataBeanProvider dataBeanProvider) {
    boolean isSupported = isSupportedIvyApp51AndLater(dataBeanProvider);
    if (!isSupported) {
      isSupported = isSupportedIvyApp50(dataBeanProvider);
      if (isSupported) {
        fDataBeanProvider = null;
      }
    }
    return isSupported;
  }

  private boolean isSupportedIvyApp50(DataBeanProvider dataBeanProvider) {
    boolean isIvyApp;
    try {
      AttributeList attributes
              = dataBeanProvider.getMBeanServerConnection().getAttributes(IvyJmxConstant.ENGINE,
                      new String[]{});
      isIvyApp = attributes != null;
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      isIvyApp = false;
    }
    return isIvyApp;
  }

  private boolean isSupportedIvyApp51AndLater(DataBeanProvider dataBeanProvider) {
    IvyApplicationInfo appInfo = dataBeanProvider.getGenericData().getApplicationInfo();
    if ((appInfo != null) && DataUtils.checkIvyVersion(appInfo.getVersion(), 5, 1)) {
      return IvyApplicationInfo.IVY_SERVER_APP_NAME.equals(appInfo.getApplicationName())
              || IvyApplicationInfo.IVY_DESIGNER_APP_NAME.equals(appInfo.getApplicationName());
    }
    return false;
  }

  @Override
  protected DataSourceView createView(Application application) {
    DataSourceView view;
    if (fDataBeanProvider != null) {
      view = new IvyView(application);
      ((IvyView) view).setDataBeanProvider(fDataBeanProvider);
    } else {
      view = new EmptyIvyView(application);
    }
    return view;
  }

  static void initialize() {
    DataSourceViewsManager.sharedInstance().addViewProvider(INSTANCE, Application.class);
  }

  static void unregister() {
    DataSourceViewsManager.sharedInstance().removeViewProvider(INSTANCE);
  }

}
