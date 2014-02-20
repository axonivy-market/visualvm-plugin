package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.view.AbstractView;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import ch.ivyteam.ivy.visualvm.view.InformationView;
import ch.ivyteam.ivy.visualvm.view.RequestView;
import ch.ivyteam.ivy.visualvm.view.RequestViewNew;
import ch.ivyteam.ivy.visualvm.view.SystemDatabaseView;
import ch.ivyteam.ivy.visualvm.view.UserDefinedView;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import com.sun.tools.visualvm.core.scheduler.Quantum;
import com.sun.tools.visualvm.core.scheduler.ScheduledTask;
import com.sun.tools.visualvm.core.scheduler.Scheduler;
import com.sun.tools.visualvm.core.scheduler.SchedulerTask;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.openide.util.ImageUtilities;

/**
 *
 * @author rwei
 */
class IvyView extends DataSourceView {

  public static final String IVY_IMAGE_PATH = "resources/icons/ivy16.png";
  public static final String INFO_IMAGE_PATH = "resources/icons/info.png";
  public static final String DBCON_IMAGE_PATH = "resources/icons/dbcon.png";
  public static final String USER_REQ_IMAGE_PATH = "resources/icons/user_req.png";
  public static final String USER_DEF_IMAGE_PATH = "resources/icons/user_def.png";

  @SuppressWarnings("unused")
  private ScheduledTask updateTask;
  private final List<ChartsPanel> chartsPanels = new ArrayList<>();
  private final List<AbstractView> views = new ArrayList<>();
  private IDataBeanProvider dataBeanProvider;

  public IvyView(Application application) {
    super(application, "Xpert.ivy", new ImageIcon(ImageUtilities.loadImage(IVY_IMAGE_PATH, true)).getImage(),
            60, false);
  }

  @Override
  protected DataViewComponent createComponent() {
    // Data area for master view:
    // Master view:
    JTabbedPane tabbed = new JTabbedPane();
    // Add the master view and configuration view to the component:
    DataViewComponent dvcRoot = createDVC("", tabbed);
    dvcRoot.add(tabbed);

    dataBeanProvider = new IDataBeanProvider() {
      @Override
      public MBeanServerConnection getMBeanServerConnection() {
        return IvyView.this.getMBeanServerConnection();
      }

    };

    InformationView infoView = new InformationView(dataBeanProvider);
    RequestView requestView = new RequestView(dataBeanProvider);
    RequestViewNew requestViewNew = new RequestViewNew(dataBeanProvider);
    SystemDatabaseView sysDbView = new SystemDatabaseView(dataBeanProvider);
    UserDefinedView userDefinedView = new UserDefinedView(dataBeanProvider);
    views.add(infoView);
    views.add(requestView);
    views.add(requestViewNew);
    views.add(sysDbView);
    views.add(userDefinedView);

    tabbed.addTab("Information", (Icon) ImageUtilities.loadImage(INFO_IMAGE_PATH, true),
            infoView.getViewComponent());
    tabbed.addTab("User Requests", (Icon) ImageUtilities.loadImage(USER_REQ_IMAGE_PATH, true),
            requestView.getViewComponent());
    tabbed.addTab("User Requests New", (Icon) ImageUtilities.loadImage(USER_REQ_IMAGE_PATH, true),
            requestViewNew.getViewComponent());
    tabbed.addTab("DB Connection", (Icon) ImageUtilities.loadImage(DBCON_IMAGE_PATH, true),
            sysDbView.getViewComponent());
    tabbed.addTab("User Defined", (Icon) ImageUtilities.loadImage(USER_DEF_IMAGE_PATH, true),
            userDefinedView.getViewComponent());

    updateTask = Scheduler.sharedInstance().schedule(new UpdateChartTask(),
            Quantum.seconds(GlobalPreferences.sharedInstance().
                    getMonitoredDataPoll()));
    return dvcRoot;
  }

  private DataViewComponent createDVC(String masterViewTitle, JComponent comp) {
    // Add the master view and configuration view to the component:
    DataViewComponent.MasterView masterView = new DataViewComponent.MasterView(
            masterViewTitle, "", comp);

    // Configuration of master view:
    DataViewComponent.MasterViewConfiguration masterConfiguration
            = new DataViewComponent.MasterViewConfiguration(
                    false);
    return new DataViewComponent(masterView, masterConfiguration);
  }

  MBeanServerConnection getMBeanServerConnection() {
    JmxModel jmx = JmxModelFactory.getJmxModelFor((Application) getDataSource());
    if (jmx != null && jmx.getConnectionState() == JmxModel.ConnectionState.CONNECTED) {
      return jmx.getMBeanServerConnection();
    } else {
      return null;
    }
  }

  private class UpdateChartTask implements SchedulerTask {

    @Override
    public void onSchedule(long l) {
      for (AbstractView view : views) {
        view.update();
      }
    }

  }
}
