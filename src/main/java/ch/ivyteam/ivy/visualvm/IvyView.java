/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.MQuery;
import ch.ivyteam.ivy.visualvm.chart.MQueryResult;
import ch.ivyteam.ivy.visualvm.chart.NewMChartTopComponent;
import ch.ivyteam.ivy.visualvm.view.AbstractView;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import ch.ivyteam.ivy.visualvm.view.InformationView;
import ch.ivyteam.ivy.visualvm.view.RequestView;
import ch.ivyteam.ivy.visualvm.view.RequestViewNew;
import ch.ivyteam.ivy.visualvm.view.SystemDatabaseView;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
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
  private ChartsPanel chartsPanel;
  private static final ObjectName CLUSTER_CHANNEL_NAME = MUtil
          .createObjectName("Xpert.ivy Server:type=Cluster Channel");
  private final List<ChartsPanel> chartsPanels = new ArrayList<>();
  private final List<AbstractView> views = new ArrayList<>();
  private static final String ERRORS = "Errors";
  private static final String TIME_US = "Time [us]";
  private static final String MIN = "Min";
  private static final String MEAN = "Mean";
  private static final String MAX = "Max";
  private static final String TRANSACTIONS = "Transactions";
  private static final String XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE
                              = "Xpert.ivy Server:type=Database Persistency Service";
  private static final String NAME = "name";
  private static final String APPLICATION = "application";
  private IDataBeanProvider dataBeanProvider;

  public IvyView(Application application) {
    super(application, "Xpert.ivy", new ImageIcon(ImageUtilities.loadImage(
            IVY_IMAGE_PATH, true)).getImage(), 60, false);
  }

  @Override
  protected DataViewComponent createComponent() {
    // Data area for master view:
    // Master view:
    JTabbedPane tabbed = new JTabbedPane();
    // Add the master view and configuration view to the component:
    DataViewComponent dvcRoot = createDVC("", tabbed);
    dvcRoot.add(tabbed);
    DataViewComponent dvcSystemDB, dvcUserDefined;

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
    views.add(infoView);
    views.add(requestView);
    views.add(requestViewNew);
    views.add(sysDbView);

    dvcSystemDB = createDVC("", null);
    dvcUserDefined = createDVC("", null);

    tabbed.addTab("Information",
                  (Icon) ImageUtilities.loadImage(INFO_IMAGE_PATH, true), infoView.getViewComponent());
    tabbed.addTab("User Requests",
                  (Icon) ImageUtilities.loadImage(USER_REQ_IMAGE_PATH, true),
                  requestView.getViewComponent());
    tabbed.addTab("User Requests New",
                  (Icon) ImageUtilities.loadImage(USER_REQ_IMAGE_PATH, true),
                  requestViewNew.getViewComponent());
    tabbed.addTab("DB Connection",
                  (Icon) ImageUtilities.loadImage(DBCON_IMAGE_PATH, true),
                  dvcSystemDB);
    tabbed.addTab("DB Connection",
                  (Icon) ImageUtilities.loadImage(DBCON_IMAGE_PATH, true),
                  sysDbView.getViewComponent());
    tabbed.addTab("User Defined",
                  (Icon) ImageUtilities.loadImage(USER_DEF_IMAGE_PATH, true),
                  dvcUserDefined);

    createSystemDatabaseView(dvcSystemDB);
    createClusterView(dvcSystemDB);
    createExternalDatabase(dvcSystemDB);
    createExternalWebService(dvcSystemDB);
    createUserDefinedChartView(dvcUserDefined);
    updateTask = Scheduler.sharedInstance().schedule(
            new UpdateChartTask(),
            Quantum.seconds(GlobalPreferences.sharedInstance()
            .getMonitoredDataPoll()));
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

  private void addChart() {
    NewMChartTopComponent newMChart = new NewMChartTopComponent();
    newMChart.setMBeanServerConnection(getMBeanServerConnection());
    NotifyDescriptor descriptor = new NotifyDescriptor(newMChart,
                                                       "New chart", NotifyDescriptor.OK_CANCEL_OPTION,
                                                       NotifyDescriptor.PLAIN_MESSAGE, null,
                                                       NotifyDescriptor.YES_OPTION);

    if (DialogDisplayer.getDefault().notify(descriptor) == NotifyDescriptor.OK_OPTION) {
      chartsPanel.addChart(newMChart.getChartDataSource());
    }
  }

  MBeanServerConnection getMBeanServerConnection() {
    JmxModel jmx = JmxModelFactory
            .getJmxModelFor((Application) getDataSource());
    if (jmx != null
        && jmx.getConnectionState() == JmxModel.ConnectionState.CONNECTED) {
      return jmx.getMBeanServerConnection();
    } else {
      return null;
    }
  }

  private void createUserDefinedChartView(DataViewComponent dvc) {
    dvc.configureDetailsArea(
            new DataViewComponent.DetailsAreaConfiguration("User Defined",
                                                           false), DataViewComponent.BOTTOM_RIGHT);

    chartsPanel = new ChartsPanel();
    chartsPanels.add(chartsPanel);

    final JButton addChartButton = new JButton("Add ...");
    addChartButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        addChart();
      }

    });
    // Add detail views to the component:
    dvc.addDetailsView(new DataViewComponent.DetailsView("User Defined",
                                                         null, 10, chartsPanel.getUiComponent(),
                                                         new JComponent[]{addChartButton}),
                       DataViewComponent.BOTTOM_RIGHT);
  }

  private void createSystemDatabaseView(DataViewComponent dvc) {
    dvc.configureDetailsArea(
            new DataViewComponent.DetailsAreaConfiguration(
                    "External Systems", false),
            DataViewComponent.BOTTOM_LEFT);
    ChartsPanel systemDbPanel = new ChartsPanel();
    chartsPanels.add(systemDbPanel);

    MChartDataSource dataSource = new MChartDataSource(
            "Transaction Processing Time", null, TIME_US);
    dataSource.addSerie(MAX, XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE,
                        "transactionsMaxExecutionTimeDeltaInMicroSeconds");
    dataSource.addDeltaMeanSerie(MEAN,
                                 XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE,
                                 "transactionsTotalExecutionTimeInMicroSeconds",
                                 TRANSACTIONS.toLowerCase());
    dataSource.addSerie(MIN, XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE,
                        "transactionsMinExecutionTimeDeltaInMicroSeconds");
    systemDbPanel.addChart(dataSource);

    dataSource = new MChartDataSource(TRANSACTIONS, null, TRANSACTIONS);
    dataSource.addDeltaSerie(TRANSACTIONS,
                             XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE,
                             TRANSACTIONS.toLowerCase());
    dataSource.addDeltaSerie(ERRORS,
                             XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE, "errors");
    systemDbPanel.addChart(dataSource);

    dataSource = new MChartDataSource("Connections", null, "Connections");
    dataSource.addSerie(MAX, XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE,
                        "maxConnections");
    dataSource
            .addSerie("Open", XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE,
                      "openConnections");
    dataSource
            .addSerie("Used", XPERTIVY_SERVER_DATABASE_PERSISTENCY_SERVICE,
                      "usedConnections");
    systemDbPanel.addChart(dataSource);

    // Add detail views to the component:
    dvc.addDetailsView(new DataViewComponent.DetailsView("System Database",
                                                         null, 10, systemDbPanel.getUiComponent(), null),
                       DataViewComponent.BOTTOM_LEFT);
  }

  private void createExternalDatabase(DataViewComponent dvc) {
    ChartsPanel extDbPanel = new ChartsPanel();
    chartsPanels.add(extDbPanel);

    for (ObjectName externalDatabaseName : getExternalDatabases()) {
      String extDbName = externalDatabaseName.toString();
      String displayName = getExternalDatabaseName(externalDatabaseName);
      MChartDataSource dataSource = new MChartDataSource(
              "Transaction Processing Time (" + displayName + ")", null,
              TIME_US);
      dataSource.addSerie(MAX, extDbName,
                          "transactionsMaxExecutionTimeDeltaInMicroSeconds");
      dataSource.addDeltaMeanSerie(MEAN, extDbName,
                                   "transactionsTotalExecutionTimeInMicroSeconds",
                                   TRANSACTIONS.toLowerCase());
      dataSource.addSerie(MIN, extDbName,
                          "transactionsMinExecutionTimeDeltaInMicroSeconds");
      extDbPanel.addChart(dataSource);

      dataSource = new MChartDataSource("Transactions (" + displayName
                                        + ")", null, TRANSACTIONS);
      dataSource.addDeltaSerie(TRANSACTIONS, extDbName,
                               TRANSACTIONS.toLowerCase());
      dataSource.addDeltaSerie(ERRORS, extDbName, "errors");
      extDbPanel.addChart(dataSource);

      dataSource = new MChartDataSource("Connections (" + displayName
                                        + ")", null, "Connections");
      dataSource.addSerie(MAX, extDbName, "maxConnections");
      dataSource.addSerie("Open", extDbName, "openConnections");
      dataSource.addSerie("Used", extDbName, "usedConnections");
      extDbPanel.addChart(dataSource);
    }
    // Add detail views to the component:
    dvc.addDetailsView(new DataViewComponent.DetailsView(
            "External Database", null, 10, extDbPanel.getUiComponent(),
            null), DataViewComponent.BOTTOM_LEFT);
  }

  private void createExternalWebService(DataViewComponent dvc) {
    ChartsPanel extWebServicePanel = new ChartsPanel();
    chartsPanels.add(extWebServicePanel);

    for (ObjectName externalWebService : getExternalWebServices()) {
      String extWebServiceName = externalWebService.toString();
      String displayName = getExternalWebServiceName(externalWebService);
      MChartDataSource dataSource = new MChartDataSource(
              "Processing Time (" + displayName + ")", null, TIME_US);
      dataSource.addSerie(MAX, extWebServiceName,
                          "callsMaxExecutionTimeDeltaInMicroSeconds");
      dataSource.addDeltaMeanSerie(MEAN, extWebServiceName,
                                   "callsTotalExecutionTimeInMicroSeconds", "calls");
      dataSource.addSerie(MIN, extWebServiceName,
                          "callsMinExecutionTimeDeltaInMicroSeconds");
      extWebServicePanel.addChart(dataSource);

      dataSource = new MChartDataSource("Calls (" + displayName + ")",
                                        null, "Calls");
      dataSource.addDeltaSerie("Calls", extWebServiceName, "calls");
      dataSource.addDeltaSerie(ERRORS, extWebServiceName, "errors");
      extWebServicePanel.addChart(dataSource);
    }
    // Add detail views to the component:
    dvc.addDetailsView(
            new DataViewComponent.DetailsView("External Web Services",
                                              null, 10, extWebServicePanel.getUiComponent(), null),
            DataViewComponent.BOTTOM_LEFT);
  }

  private void createClusterView(DataViewComponent dvc) {
    if (isClusterAvailable()) {
      ChartsPanel clusterPanel = new ChartsPanel();
      chartsPanels.add(clusterPanel);
      MChartDataSource dataSource = new MChartDataSource(
              "Send Message Processing Time", null, TIME_US);
      dataSource.addSerie(MAX, CLUSTER_CHANNEL_NAME,
                          "sendMessagesMaxExecutionTimeDeltaInMicroSeconds");
      dataSource.addDeltaMeanSerie(MEAN, CLUSTER_CHANNEL_NAME,
                                   "sendMessagesTotalExecutionTimeInMicroSeconds",
                                   "sendMessages");
      dataSource.addSerie(MIN, CLUSTER_CHANNEL_NAME,
                          "sendMessagesMinExecutionTimeDeltaInMicroSeconds");
      clusterPanel.addChart(dataSource);

      dataSource = new MChartDataSource("Send Messages", null, MESSAGES);
      dataSource.addDeltaSerie(MESSAGES, CLUSTER_CHANNEL_NAME,
                               "sendMessages");
      dataSource
              .addDeltaSerie(ERRORS, CLUSTER_CHANNEL_NAME, "sendErrors");
      clusterPanel.addChart(dataSource);

      dataSource = new MChartDataSource(
              "Received Message Processing Time", null, TIME_US);
      dataSource.addSerie(MAX, CLUSTER_CHANNEL_NAME,
                          "receiveMessagesMaxExecutionTimeDeltaInMicroSeconds");
      dataSource.addDeltaMeanSerie(MEAN, CLUSTER_CHANNEL_NAME,
                                   "receiveMessagesTotalExecutionTimeInMicroSeconds",
                                   "receiveMessages");
      dataSource.addSerie(MIN, CLUSTER_CHANNEL_NAME,
                          "receiveMessagesMinExecutionTimeDeltaInMicroSeconds");
      clusterPanel.addChart(dataSource);

      dataSource = new MChartDataSource("Received Messages", null,
                                        MESSAGES);
      dataSource.addDeltaSerie(MESSAGES, CLUSTER_CHANNEL_NAME,
                               "receiveMessages");
      dataSource.addDeltaSerie(ERRORS, CLUSTER_CHANNEL_NAME,
                               "receiveErrors");
      clusterPanel.addChart(dataSource);

      dataSource = new MChartDataSource("Suspects/Views", null,
                                        "Suspects/Views");
      dataSource.addSerie("Views", CLUSTER_CHANNEL_NAME, "views");
      dataSource.addSerie("Suspects", CLUSTER_CHANNEL_NAME, "suspects");
      clusterPanel.addChart(dataSource);

      // Add detail views to the component:
      dvc.addDetailsView(new DataViewComponent.DetailsView("Cluster",
                                                           null, 10, clusterPanel.getUiComponent(), null),
                         DataViewComponent.BOTTOM_LEFT);
    }
  }

  public static final String MESSAGES = "Messages";

  private boolean isClusterAvailable() {
    try {
      return getMBeanServerConnection()
              .isRegistered(CLUSTER_CHANNEL_NAME);
    } catch (IOException ex) {
      return false;
    }
  }

  private Set<ObjectName> getExternalDatabases() {
    try {
      return getMBeanServerConnection().queryNames(new ObjectName(
              "Xpert.ivy Server:type=External Database,application=*,environment=*,name=*"), null);
    } catch (IOException | MalformedObjectNameException ex) {
      return Collections.emptySet();
    }
  }

  private Set<ObjectName> getExternalWebServices() {
    try {
      return getMBeanServerConnection().queryNames(new ObjectName(
              "Xpert.ivy Server:type=External Web Service,application=*,environment=*,name=*"), null);
    } catch (IOException | MalformedObjectNameException ex) {
      return Collections.emptySet();
    }
  }

  private String getExternalDatabaseName(ObjectName extDbName) {
    String extDatabaseName = extDbName.getKeyProperty(APPLICATION);
    extDatabaseName += "/";
    extDatabaseName += extDbName.getKeyProperty("environment");
    extDatabaseName += "/";
    extDatabaseName += extDbName.getKeyProperty(NAME);
    return extDatabaseName;
  }

  private String getExternalWebServiceName(ObjectName extWebServiceName) {
    String extDatabaseName = extWebServiceName.getKeyProperty(APPLICATION);
    extDatabaseName += "/";
    extDatabaseName += extWebServiceName.getKeyProperty("environment");
    extDatabaseName += "/";
    extDatabaseName += extWebServiceName.getKeyProperty(NAME);
    return extDatabaseName;
  }

  private String getProcessStartEventBeanName(
          ObjectName processStartEventBeanName) {
    String extDatabaseName = processStartEventBeanName
            .getKeyProperty(APPLICATION);
    extDatabaseName += "/";
    extDatabaseName += processStartEventBeanName.getKeyProperty("pm");
    extDatabaseName += "/";
    extDatabaseName += processStartEventBeanName.getKeyProperty("pmv");
    extDatabaseName += "/";
    extDatabaseName += processStartEventBeanName.getKeyProperty(NAME);
    return extDatabaseName;
  }

  private String getProcessIntermediateEventBeanName(
          ObjectName processIntermediateEventBeanName) {
    String extDatabaseName = processIntermediateEventBeanName
            .getKeyProperty(APPLICATION);
    extDatabaseName += "/";
    extDatabaseName += processIntermediateEventBeanName
            .getKeyProperty("pm");
    extDatabaseName += "/";
    extDatabaseName += processIntermediateEventBeanName
            .getKeyProperty("pmv");
    extDatabaseName += "/";
    extDatabaseName += processIntermediateEventBeanName
            .getKeyProperty(NAME);
    return extDatabaseName;
  }

  private ObjectName getTomcatManagerName() {
    Set<ObjectName> tomcatManagers = MUtil.queryNames(
            getMBeanServerConnection(),
            "*:type=Manager,context=*,host=localhost");
    if (tomcatManagers.size() >= 1) {
      return tomcatManagers.iterator().next();
    }
    return null;
  }

  private class UpdateChartTask implements SchedulerTask {

    @Override
    public void onSchedule(long l) {
      MBeanServerConnection serverConnection = getMBeanServerConnection();
      MQuery query = new MQuery();
      for (ChartsPanel panel : chartsPanels) {
        panel.updateQuery(query);
      }
      MQueryResult result = query.execute(serverConnection);
      for (ChartsPanel panel : chartsPanels) {
        panel.updateValues(result);
      }

      for (AbstractView view : views) {
        view.update();
      }
    }

  }
}
