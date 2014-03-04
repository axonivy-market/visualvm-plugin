package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.license.ConcurrentUsersChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.license.ConcurrentUsersGaugeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.license.NamedUsersGaugeDataSource;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.Date;
import javax.management.MBeanServerConnection;
import org.openide.util.Exceptions;

public class LicenseView extends AbstractView {

  private boolean uiComplete;
  private IvyLicenseInfo fLicenseInfo;
  private final LicenseInformationPanel fLicenseInformationPanel;

  public LicenseView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    retrieveLicenseInfo();
    fLicenseInformationPanel = new LicenseInformationPanel(fLicenseInfo);
    fLicenseInformationPanel.setLicenseData(Long.MAX_VALUE, 0, 0);
  }

  @Override
  protected String getMasterViewTitle() {
    return "License Information";
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    viewComponent.configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(
            "General Information", false), DataViewComponent.TOP_LEFT);
    if (!uiComplete) {
      createLicenseInfoView();
      createSessionsView();
      createUsersView();
      uiComplete = true;
    }
    return viewComponent;
  }

  private void createLicenseInfoView() {
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("General Information",
            null, 10, fLicenseInformationPanel, null), DataViewComponent.TOP_LEFT);

  }

  private void createSessionsView() {
    if (fLicenseInfo.getServerSessionsLimit() > 0) {
      ConcurrentUsersChartDataSource sessionDataSource = new ConcurrentUsersChartDataSource(
              getDataBeanProvider(), null, null, "Concurrent Users");
      ConcurrentUsersGaugeDataSource gaugeSessionsDataSource = new ConcurrentUsersGaugeDataSource(
              getDataBeanProvider(), fLicenseInfo.getServerSessionsLimit());

      ChartsPanel sessionsChart = new ChartsPanel(true);
      sessionsChart.addChart2(sessionDataSource);
      sessionsChart.addGauge(gaugeSessionsDataSource);
      registerScheduledUpdate(sessionsChart);

      super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Concurrent Users",
              null, 10, sessionsChart.getUIComponent(), null), DataViewComponent.BOTTOM_LEFT);
    }
  }

  private void createUsersView() {
    if (fLicenseInfo.getServerUsersLimit() > 0) {
      NamedUsersGaugeDataSource gaugeUsersDataSource = new NamedUsersGaugeDataSource(getDataBeanProvider(),
              fLicenseInfo.getServerUsersLimit());
      ChartsPanel userChart = new ChartsPanel(false);
      userChart.addLinear(gaugeUsersDataSource);
      registerScheduledUpdate(userChart);

      super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Named Users",
              null, 10, userChart.getUIComponent(), null), DataViewComponent.TOP_RIGHT);
    }
  }

  private void retrieveLicenseInfo() {
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    try {
      MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
      fLicenseInfo = collector.getLicenseInfo(connection);
    } catch (IvyJmxDataCollectException ex) {
      fLicenseInfo = null;
      Exceptions.printStackTrace(ex);
    }
  }

  private int fStoredNamedUsers, fStoredConUsers;
  private boolean fNearlyExpiredUpdated, fExpiredUpdated;

  @Override
  public void update() {
    super.update();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    int namedUsers = 0;
    int concurrentUsers = 0;
    try {
      MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
      namedUsers = collector.getNamedUsers(connection);
      concurrentUsers = collector.getConcurrentUsers(connection);
    } catch (IvyJmxDataCollectException ex) {
      Exceptions.printStackTrace(ex);
    }
    long remainingTime = fLicenseInfo.getLicenseValidUntil().getTime() - new Date().getTime();
    boolean nearlyExpired = remainingTime <= 30 * LicenseInformationPanel.MILISECONDS_IN_ONE_DAY;
    boolean expired = remainingTime <= 0;
    boolean valueChanged = fStoredConUsers != concurrentUsers || fStoredNamedUsers != namedUsers;

    if (valueChanged) {
      fLicenseInformationPanel.setLicenseData(remainingTime, namedUsers, concurrentUsers);
      fStoredNamedUsers = namedUsers;
      fStoredConUsers = concurrentUsers;
    }

    if (nearlyExpired && !fNearlyExpiredUpdated) {
      fLicenseInformationPanel.setLicenseData(remainingTime, namedUsers, concurrentUsers);
      fNearlyExpiredUpdated = true;
    }

    if (expired && !fExpiredUpdated) {
      fLicenseInformationPanel.setLicenseData(remainingTime, namedUsers, concurrentUsers);
      fExpiredUpdated = true;
    }
  }

}
