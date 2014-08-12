package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServerConnection;

public class IvyViewProvider extends DataSourceViewProvider<Application> {

  private static final DataSourceViewProvider<Application> INSTANCE = new IvyViewProvider();
  /**
   * Super class has a viewsCache. But in method createView(), calling getCachedView(application) always
   * returns null. That's why we have to create our own cached.
   */
  private final Map<Application, DataSourceView> cachedViews = new HashMap<>();
  private DataBeanProvider fDataBeanProvider;

  @Override
  protected boolean supportsViewFor(Application application) {
    if (isIvyApplicationType(application)) {
      IvyApplicationType appType = (IvyApplicationType) ApplicationTypeFactory
              .getApplicationTypeFor(application);

      if (appType.isAxonIvyApplicaton()) {
        fDataBeanProvider = createMbeanProvider(application);
      } else {
        fDataBeanProvider = null;
      }
      return true;
    }
    return false;
  }

  private DataBeanProvider createMbeanProvider(Application application) {
    JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        return new DataBeanProvider(mbsc);
      }
    }
    return null;
  }

  private boolean isIvyApplicationType(Application application) {
    return ApplicationTypeFactory.getApplicationTypeFor(application) instanceof IvyApplicationType;
  }

  @Override
  protected DataSourceView createView(Application application) {
    DataSourceView view = this.cachedViews.get(application);
    if (view != null) {
      return view;
    }
    if (fDataBeanProvider != null) {
      view = new IvyView(application);
      ((IvyView) view).setDataBeanProvider(fDataBeanProvider);
    } else {
      view = new IvyViewEmpty(application);
    }
    this.cachedViews.put(application, view);
    return view;
  }

  static void initialize() {
    DataSourceViewsManager.sharedInstance().addViewProvider(INSTANCE, Application.class);
  }

  static void unregister() {
    DataSourceViewsManager.sharedInstance().removeViewProvider(INSTANCE);
  }

}
