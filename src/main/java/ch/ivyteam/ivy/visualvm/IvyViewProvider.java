package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.application.type.ApplicationTypeFactory;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ReflectionException;

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
    boolean isSupported = isIvyApplicationType(application);
    JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
    if (jmx != null) {
      MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
      if (mbsc != null) {
        fDataBeanProvider = new DataBeanProvider(mbsc);
        if (!isAxonIvyApp(fDataBeanProvider)) {
          fDataBeanProvider = null;
        }
      }
    }
    return isSupported;
  }

  private boolean isIvyApplicationType(Application application) {
    return ApplicationTypeFactory.getApplicationTypeFor(application) instanceof IvyApplicationType;
  }

  public boolean isAxonIvyApp(DataBeanProvider dataBeanProvider) {
    boolean isAxonIvyApp;
    try {
      AttributeList attributes
              = dataBeanProvider.getMBeanServerConnection().getAttributes(IvyJmxConstant.IVY_ENGINE,
                      new String[]{});
      isAxonIvyApp = attributes != null;
    } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
      isAxonIvyApp = false;
    }
    return isAxonIvyApp;
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
