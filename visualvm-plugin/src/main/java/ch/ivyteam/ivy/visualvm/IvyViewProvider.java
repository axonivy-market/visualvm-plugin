package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.type.ApplicationType;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import java.util.HashMap;
import java.util.Map;

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
    DataBeanProvider tmpProvider = IvyViewHelper.createMbeanProvider(application);

    ApplicationType appType = ApplicationTypeFactory.getApplicationTypeFor(application);
    if (appType instanceof IvyApplicationType) {
      IvyApplicationType ivyAppType = (IvyApplicationType) appType;
      if (ivyAppType.isAxonIvyApplication()) { // >= 5.1
        fDataBeanProvider = tmpProvider;
      } else {
        fDataBeanProvider = null;
      }
      return true;
    }

    if (tmpProvider != null) {
      if (IvyViewHelper.isRemoteIvyAppication51OrYounger(tmpProvider)) {
        fDataBeanProvider = tmpProvider;
        return true;
      } else if (IvyViewHelper.isRemoteIvyApplication50OrOlder(tmpProvider)) {
        fDataBeanProvider = null;
        return true;
      }
    }

    fDataBeanProvider = null;
    return false;
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
