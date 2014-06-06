package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import javax.management.MBeanServerConnection;

public class IvyViewProvider extends DataSourceViewProvider<Application> {

  private static final DataSourceViewProvider<Application> INSTANCE = new IvyViewProvider();
  private DataBeanProvider fDataBeanProvider;

  @Override
  protected boolean supportsViewFor(Application application) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
    boolean result = false;
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        fDataBeanProvider = new DataBeanProvider(mbsc);
        result = checkAppropriateIvyApp(fDataBeanProvider);
      }
    }
    if (!result) {
      fDataBeanProvider = null;
    }
    return true;
  }

  public boolean checkAppropriateIvyApp(DataBeanProvider dataBeanProvider) {
    boolean result = false;
    IvyApplicationInfo appInfo = dataBeanProvider.getGenericData().getApplicationInfo();
    if ((appInfo != null) && DataUtils.checkIvyVersion(appInfo.getVersion(), 5, 1)) {
      switch (appInfo.getApplicationName()) {
        case IvyApplicationInfo.IVY_SERVER_APP_NAME:
        case IvyApplicationInfo.IVY_DESIGNER_APP_NAME:
          result = true;
          break;
        default:
          break;
      }
    }
    return result;
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
