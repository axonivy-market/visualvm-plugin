package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.exception.ClosedIvyServerConnectionException;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.AbstractView;
import ch.ivyteam.ivy.visualvm.view.ExternalDbView;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import ch.ivyteam.ivy.visualvm.view.InformationView;
import ch.ivyteam.ivy.visualvm.view.LicenseView;
import ch.ivyteam.ivy.visualvm.view.RequestView;
import ch.ivyteam.ivy.visualvm.view.SystemDbView;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import com.sun.tools.visualvm.core.scheduler.Quantum;
import com.sun.tools.visualvm.core.scheduler.ScheduledTask;
import com.sun.tools.visualvm.core.scheduler.Scheduler;
import com.sun.tools.visualvm.core.scheduler.SchedulerTask;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.management.MBeanServerConnection;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.openide.util.ImageUtilities;

class IvyView extends DataSourceView {

  private static final Logger LOGGER = Logger.getLogger(IvyView.class.getName());

  public static final String IVY_IMAGE_PATH = "resources/icons/ivy16.png";
  public static final String INFO_IMAGE_PATH = "resources/icons/info.png";
  public static final String DB_ICON_IMAGE_PATH = "resources/icons/db_icon.png";
  public static final String EXT_DB_ICON_IMAGE_PATH = "resources/icons/ext_db_icon.png";
  public static final String USER_REQ_IMAGE_PATH = "resources/icons/user_req.png";
  public static final String USER_DEF_IMAGE_PATH = "resources/icons/user_def.png";
  public static final String LICENSE_IMAGE_PATH = "resources/icons/license.png";
  public static final String IVY_SERVER_APP_NAME = "Xpert.ivy Server";
  public static final String IVY_DESIGNER_APP_NAME = "Xpert.ivy Designer";

  private ScheduledTask updateTask;
  private final List<AbstractView> views = new ArrayList<>();
  private final DataPollSettingChangeListener pollSettingChangeListener = new DataPollSettingChangeListener();
  private final boolean fIsIvyServer;
  private final Application fIvyApplication;

  public IvyView(Application application, boolean isServer) {
    super(application, "Xpert.ivy", new ImageIcon(ImageUtilities.loadImage(IVY_IMAGE_PATH, true)).
            getImage(), 60, false);
    fIvyApplication = application;
    fIsIvyServer = isServer;
  }

  @Override
  protected DataViewComponent createComponent() {
    // Data area for master view:
    // Master view:
    JTabbedPane tabbed = new JTabbedPane();
    // Add the master view and configuration view to the component:
    DataViewComponent dvcRoot = createDVC("", null);
    dvcRoot.add(tabbed);
    IDataBeanProvider dataBeanProvider = new IDataBeanProvider() {
      @Override
      public MBeanServerConnection getMBeanServerConnection() {
        return DataUtils.getMBeanServerConnection(fIvyApplication);
      }

    };

    // add views
    addInformationView(dataBeanProvider, tabbed);
    addLicenseView(dataBeanProvider, tabbed);
    addRequestView(dataBeanProvider, tabbed);
    addSystemDBView(dataBeanProvider, tabbed);
    addExternalDBView(dataBeanProvider, tabbed);

    // init scheduler
    updateTask = Scheduler.sharedInstance().schedule(new UpdateChartTask(),
            Quantum.seconds(GlobalPreferences.sharedInstance().getMonitoredDataPoll()), true);
    GlobalPreferences.sharedInstance().watchMonitoredDataPoll(pollSettingChangeListener);
    return dvcRoot;
  }

  private void addInformationView(IDataBeanProvider dataBeanProvider, JTabbedPane tabbed) {
    InformationView infoView = new InformationView(dataBeanProvider);
    views.add(infoView);
    tabbed.addTab("Information", (Icon) ImageUtilities.loadImage(INFO_IMAGE_PATH, true),
            infoView.getViewComponent());
  }

  private void addLicenseView(IDataBeanProvider dataBeanProvider, JTabbedPane tabbed) {
    if (fIsIvyServer) {
      LicenseView licenseView = new LicenseView(dataBeanProvider);
      views.add(licenseView);
      tabbed.addTab("License", (Icon) ImageUtilities.loadImage(LICENSE_IMAGE_PATH, true),
              licenseView.getViewComponent());
    }
  }

  private void addRequestView(IDataBeanProvider dataBeanProvider, JTabbedPane tabbed) {
    RequestView requestViewNew = new RequestView(dataBeanProvider);
    views.add(requestViewNew);
    tabbed.addTab("User Requests", (Icon) ImageUtilities.loadImage(USER_REQ_IMAGE_PATH, true),
            requestViewNew.getViewComponent());
  }

  private void addSystemDBView(IDataBeanProvider dataBeanProvider, JTabbedPane tabbed) {
    SystemDbView systemDbView = new SystemDbView(dataBeanProvider);
    views.add(systemDbView);
    tabbed.addTab("System Database", (Icon) ImageUtilities.loadImage(DB_ICON_IMAGE_PATH, true),
            systemDbView.getViewComponent());
  }

  private void addExternalDBView(IDataBeanProvider dataBeanProvider, JTabbedPane tabbed) {
    ExternalDbView extDbView = new ExternalDbView(dataBeanProvider, fIvyApplication);
    views.add(extDbView);
    tabbed.addTab("External Databases", (Icon) ImageUtilities.loadImage(EXT_DB_ICON_IMAGE_PATH, true),
            extDbView.getViewComponent());
  }

  private DataViewComponent createDVC(String masterViewTitle, JComponent comp) {
    // Add the master view and configuration view to the component:
    DataViewComponent.MasterView masterView = new DataViewComponent.MasterView(
            masterViewTitle, "", comp);

    // Configuration of master view:
    DataViewComponent.MasterViewConfiguration masterConfiguration
            = new DataViewComponent.MasterViewConfiguration(false);
    return new DataViewComponent(masterView, masterConfiguration);
  }

  private class UpdateChartTask implements SchedulerTask {
    @Override
    public void onSchedule(long l) {
      for (AbstractView view : views) {
        try {
          view.update();
        } catch (ClosedIvyServerConnectionException ex) {
          updateTask.suspend();
          LOGGER.warning(ex.getMessage());
          break;
        }
      }
    }

  }

  private class DataPollSettingChangeListener implements PreferenceChangeListener {
    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
      updateTask.setInterval(Quantum.seconds(GlobalPreferences.sharedInstance().
              getMonitoredDataPoll()));
    }

  }
}
