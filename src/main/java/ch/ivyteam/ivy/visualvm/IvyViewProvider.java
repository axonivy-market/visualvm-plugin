package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import javax.management.MBeanServerConnection;

class IvyViewProvider extends DataSourceViewProvider<Application> {

  private static final DataSourceViewProvider<Application> INSTANCE = new IvyViewProvider();
  private IvyView ivyView = null;
  private DataBeanProvider fDataBeanProvider;

  @Override
  protected boolean supportsViewFor(Application application) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        fDataBeanProvider = new DataBeanProvider(mbsc);
        IvyApplicationInfo appInfo = fDataBeanProvider.getGenericData().getApplicationInfo();
        if ((appInfo != null)
                && ("5.1".compareTo(appInfo.getVersion()) < 0)) {
          boolean isIvyServer = IvyApplicationInfo.IVY_SERVER_APP_NAME.equals(appInfo.getApplicationName());
          boolean isIvyDesigner = IvyApplicationInfo.IVY_DESIGNER_APP_NAME.
                  equals(appInfo.getApplicationName());
          if (isIvyServer || isIvyDesigner) {
            ivyView = new IvyView(application);
            ivyView.setDataBeanProvider(fDataBeanProvider);
            return true;
          }
          return false;
        }
      }
    }
    return false;
  }

  @Override
  protected DataSourceView createView(Application application) {
    return ivyView;
  }

  static void initialize() {
    DataSourceViewsManager.sharedInstance().addViewProvider(INSTANCE, Application.class);
  }

  static void unregister() {
    DataSourceViewsManager.sharedInstance().removeViewProvider(INSTANCE);
  }

}
