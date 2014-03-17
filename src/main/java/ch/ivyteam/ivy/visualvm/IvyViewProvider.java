package ch.ivyteam.ivy.visualvm;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ReflectionException;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.DataSourceViewProvider;
import com.sun.tools.visualvm.core.ui.DataSourceViewsManager;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;

class IvyViewProvider extends DataSourceViewProvider<Application> {

  private static final DataSourceViewProvider<Application> INSTANCE = new IvyViewProvider();
  private IvyView ivyView = null;

  @Override
  protected boolean supportsViewFor(Application application) {
    try {
      JmxModel jmx = JmxModelFactory.getJmxModelFor(application);
      if (jmx != null) {
        MBeanServerConnection mbsc = jmx.getMBeanServerConnection();
        if (mbsc != null) {
          Object name = mbsc.getAttribute(IvyJmxConstant.IvyServer.Server.NAME,
                  IvyJmxConstant.IvyServer.Server.KEY_APPLICATION_NAME);
          if (name != null) {
            boolean isIvyServer = name.toString().equals(IvyView.IVY_SERVER_APP_NAME);
            boolean isIvyDesigner = name.toString().equals(IvyView.IVY_DESIGNER_APP_NAME);
            ivyView = new IvyView(application, isIvyServer);
            return isIvyServer || isIvyDesigner;
          }
        }
      }
    } catch (AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException |
            IOException e) {
      Logger.getLogger(IvyViewProvider.class.getName()).log(Level.WARNING,
              "Cannot detect Ivy application", e);
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
