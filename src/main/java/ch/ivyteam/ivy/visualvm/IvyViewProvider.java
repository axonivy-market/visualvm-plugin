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
        fDataBeanProvider = produceDataBeanProvider(mbsc);
        result = (fDataBeanProvider != null);
      }
    }
    return result;
  }

  public DataBeanProvider produceDataBeanProvider(MBeanServerConnection mbsc) {
    DataBeanProvider dataBeanProvider = new DataBeanProvider(mbsc);
    IvyApplicationInfo appInfo = dataBeanProvider.getGenericData().getApplicationInfo();
    if ((appInfo != null) && DataUtils.checkIvyVersion(appInfo.getVersion(), 5, 1)) {
      switch (appInfo.getApplicationName()) {
        case IvyApplicationInfo.IVY_SERVER_APP_NAME:
        case IvyApplicationInfo.IVY_DESIGNER_APP_NAME:
          break;
        default:
          dataBeanProvider = null;
          break;
      }
    } else {
      dataBeanProvider = null;
    }
    return dataBeanProvider;
  }

  @Override
  protected DataSourceView createView(Application application) {
    IvyView ivyView = new IvyView(application);
    ivyView.setDataBeanProvider(fDataBeanProvider);
    return ivyView;
  }

  static void initialize() {
    DataSourceViewsManager.sharedInstance().addViewProvider(INSTANCE, Application.class);
  }

  static void unregister() {
    DataSourceViewsManager.sharedInstance().removeViewProvider(INSTANCE);
  }

}
