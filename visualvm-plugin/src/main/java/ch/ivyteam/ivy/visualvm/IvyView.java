package ch.ivyteam.ivy.visualvm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.openide.util.ImageUtilities;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import com.sun.tools.visualvm.core.scheduler.Quantum;
import com.sun.tools.visualvm.core.scheduler.ScheduledTask;
import com.sun.tools.visualvm.core.scheduler.Scheduler;
import com.sun.tools.visualvm.core.scheduler.SchedulerTask;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.exception.ClosedIvyServerConnectionException;
import ch.ivyteam.ivy.visualvm.view.common.AbstractView;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import ch.ivyteam.ivy.visualvm.view.InformationView;
import ch.ivyteam.ivy.visualvm.view.LicenseView;
import ch.ivyteam.ivy.visualvm.view.RequestView;
import ch.ivyteam.ivy.visualvm.view.soapwebservice.SOAPWebServicesView;
import ch.ivyteam.ivy.visualvm.view.SystemDbView;
import ch.ivyteam.ivy.visualvm.view.externaldb.ExternalDbView;
import ch.ivyteam.ivy.visualvm.view.restwebservice.RESTWebServicesView;

class IvyView extends DataSourceView {

  private static final Logger LOGGER = Logger.getLogger(IvyView.class.getName());

  public static final String INFO_IMAGE_PATH = "resources/icons/info.png";
  public static final String DB_ICON_IMAGE_PATH = "resources/icons/db_icon.png";
  public static final String EXT_DB_ICON_IMAGE_PATH = "resources/icons/ext_db_icon.png";
  public static final String WEB_SERVICE_ICON_IMAGE_PATH = "resources/icons/web_services_icon.png";
  public static final String USER_REQ_IMAGE_PATH = "resources/icons/user_req.png";
  public static final String LICENSE_IMAGE_PATH = "resources/icons/license.png";

  private ScheduledTask fUpdateTask;
  private final List<AbstractView> fViews = new ArrayList<>();
  private final DataPollSettingChangeListener fPollSettingChangeListener = new DataPollSettingChangeListener();
  private DataBeanProvider fDataBeanProvider;

  public IvyView(Application application) {
    super(application, IvyViewHelper.getViewName(application), IvyViewHelper.getViewIcon(application), 60,
            false);
  }

  @Override
  protected DataViewComponent createComponent() {
    // Data area for master view:
    // Master view:
    JTabbedPane tabbed = new JTabbedPane();
    // Add the master view and configuration view to the component:
    DataViewComponent dvcRoot = createDVC("", null);
    dvcRoot.add(tabbed);

    // add views
    addInformationView(tabbed);
    addLicenseView(tabbed);
    addRequestView(tabbed);
    addSystemDBView(tabbed);
    addExternalDBView(tabbed);
    addWebServicesView(tabbed);
    addRESTWebServicesView(tabbed);
    // init scheduler
    fUpdateTask = Scheduler.sharedInstance().schedule(new UpdateChartTask(),
            Quantum.seconds(GlobalPreferences.sharedInstance().getMonitoredDataPoll()), true);
    GlobalPreferences.sharedInstance().watchMonitoredDataPoll(fPollSettingChangeListener);
    return dvcRoot;
  }

  // CHECKSTYLE:OFF
  private void addInformationView(JTabbedPane tabbed) {
    String viewName = "Information";
    try {
      InformationView infoView = new InformationView(fDataBeanProvider);
      fViews.add(infoView);
      tabbed.addTab(ContentProvider.get(viewName),
              (Icon) ImageUtilities.loadImage(INFO_IMAGE_PATH, true), infoView.getViewComponent());
    } catch (Exception ex) {
      writeLogException(viewName, ex);
    }
  }

  private void addLicenseView(JTabbedPane tabbed) {
    String viewName = "License";
    try {
      if (fDataBeanProvider.getGenericData().getApplicationInfo().isServer()) {
        LicenseView licenseView = new LicenseView(fDataBeanProvider);
        fViews.add(licenseView);
        tabbed.addTab(ContentProvider.get(viewName),
                (Icon) ImageUtilities.loadImage(LICENSE_IMAGE_PATH, true), licenseView.getViewComponent());
      }
    } catch (Exception ex) {
      writeLogException(viewName, ex);
    }
  }

  private void addRequestView(JTabbedPane tabbed) {
    String viewName = "UserRequests";
    try {
      RequestView requestViewNew = new RequestView(fDataBeanProvider);
      fViews.add(requestViewNew);
      tabbed.addTab(ContentProvider.get(viewName),
              (Icon) ImageUtilities.loadImage(USER_REQ_IMAGE_PATH, true), requestViewNew.getViewComponent());
    } catch (Exception ex) {
      writeLogException(viewName, ex);
    }
  }

  private void addSystemDBView(JTabbedPane tabbed) {
    String viewName = "SystemDatabase";
    try {
      SystemDbView systemDbView = new SystemDbView(fDataBeanProvider);
      fViews.add(systemDbView);
      tabbed.addTab(ContentProvider.get("SystemDatabase"),
              (Icon) ImageUtilities.loadImage(DB_ICON_IMAGE_PATH, true), systemDbView.getViewComponent());
    } catch (Exception ex) {
      writeLogException(viewName, ex);
    }
  }

  private void addExternalDBView(JTabbedPane tabbed) {
    String viewName = "ExternalDatabases";
    try {
      ExternalDbView extDbView = new ExternalDbView(fDataBeanProvider);
      fViews.add(extDbView);
      tabbed.addTab(ContentProvider.get(viewName),
              (Icon) ImageUtilities.loadImage(EXT_DB_ICON_IMAGE_PATH, true), extDbView.getViewComponent());
    } catch (Exception ex) {
      writeLogException(viewName, ex);
    }
  }

  private void addWebServicesView(JTabbedPane tabbed) {
    String viewName = "SOAPWebServices";
    try {
      SOAPWebServicesView wsView = new SOAPWebServicesView(fDataBeanProvider);
      fViews.add(wsView);
      tabbed.addTab(ContentProvider.get(viewName),
              (Icon) ImageUtilities.loadImage(WEB_SERVICE_ICON_IMAGE_PATH, true), wsView.getViewComponent());
    } catch (Exception ex) {
      writeLogException(viewName, ex);
    }
  }

  private void addRESTWebServicesView(JTabbedPane tabbed) {
    String viewName = "RESTWebServices";
    try {
      RESTWebServicesView restWSView = new RESTWebServicesView(fDataBeanProvider);
      fViews.add(restWSView);
      tabbed.addTab(ContentProvider.get(viewName),
              (Icon) ImageUtilities.loadImage(WEB_SERVICE_ICON_IMAGE_PATH, true), restWSView.getViewComponent());
    } catch (Exception ex) {
      writeLogException(viewName, ex);
    }
  }

  // CHECKSTYLE:ON

  private DataViewComponent createDVC(String masterViewTitle, JComponent comp) {
    // Add the master view and configuration view to the component:
    DataViewComponent.MasterView masterView = new DataViewComponent.MasterView(masterViewTitle, "", comp);

    // Configuration of master view:
    DataViewComponent.MasterViewConfiguration masterConfiguration = new DataViewComponent.MasterViewConfiguration(false);
    return new DataViewComponent(masterView, masterConfiguration);
  }

  public void setDataBeanProvider(DataBeanProvider dataBeanProvider) {
    fDataBeanProvider = dataBeanProvider;
  }

  private void writeLogException(String viewName, Throwable cause) {
    String message = "An exception occurred when adding " + viewName + " view";
    LOGGER.log(Level.WARNING, message, cause);
  }

  private class UpdateChartTask implements SchedulerTask {

    @Override
    public void onSchedule(long l) {
      Query query = new Query();
      for (AbstractView view : fViews) {
        view.updateQuery(query);
      }
      QueryResult result = query.execute(fDataBeanProvider.getMBeanServerConnection());
      for (AbstractView view : fViews) {
        try {
          view.updateDisplay(result);
        } catch (ClosedIvyServerConnectionException ex) {
          fUpdateTask.suspend();
          LOGGER.warning(ex.getMessage());
          break;
        }
      }
    }
  }

  private class DataPollSettingChangeListener implements PreferenceChangeListener {

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
      fUpdateTask.setInterval(Quantum.seconds(GlobalPreferences.sharedInstance().getMonitoredDataPoll()));
    }
  }
}
