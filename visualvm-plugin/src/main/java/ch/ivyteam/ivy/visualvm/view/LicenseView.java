package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.view.common.AbstractView;
import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.license.ConcurrentUsersChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.license.ConcurrentUsersGaugeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.license.NamedUsersGaugeDataSource;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.Date;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;

public class LicenseView extends AbstractView {

  private static final Logger LOGGER = Logger.getLogger(LicenseView.class.getName());
  private boolean uiComplete;
  private IvyLicenseInfo fLicenseInfo;
  private final LicenseInformationPanel fLicenseInformationPanel;

  public LicenseView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    retrieveLicenseInfo();
    fLicenseInformationPanel = new LicenseInformationPanel(fLicenseInfo);
    fLicenseInformationPanel.setLicenseData(Long.MAX_VALUE, 0, 0);
  }

  @Override
  protected String getMasterViewTitle() {
    return "";
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      createLicenseInfoView();
      createSessionsView();
      createUsersView();
      uiComplete = true;
    }
    return viewComponent;
  }

  private void createLicenseInfoView() {
    String generalInfo = ContentProvider.get("GeneralInfo");
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(generalInfo,
            null, 10, fLicenseInformationPanel, null), DataViewComponent.TOP_LEFT);
    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(
            generalInfo, false), DataViewComponent.TOP_LEFT);
  }

  private void createSessionsView() {
    String concurrentUsersDesc = ContentProvider.get("ConcurrentUsers");
    int serverSessionsLimit = fLicenseInfo.getServerSessionsLimit();
    if (serverSessionsLimit > 0) {
      ConcurrentUsersChartDataSource sessionDataSource = new ConcurrentUsersChartDataSource(
              getDataBeanProvider(), concurrentUsersDesc, serverSessionsLimit);
      ConcurrentUsersGaugeDataSource gaugeSessionsDataSource = new ConcurrentUsersGaugeDataSource(
              getDataBeanProvider(), fLicenseInfo.getServerSessionsLimit());

      ChartsPanel sessionsChart = new ChartsPanel(true);
      sessionsChart.addChart(sessionDataSource, getConcurrentUsersChartDescription());
      sessionsChart.addGauge(gaugeSessionsDataSource);
      registerScheduledUpdate(sessionsChart);

      super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(concurrentUsersDesc,
              null, 10, sessionsChart.getUIComponent(), null), DataViewComponent.BOTTOM_LEFT);
    }
  }

  private String getConcurrentUsersChartDescription() {
    return ContentProvider.getFormatted("ConcurrentUsersChartDescription");
  }

  private void createUsersView() {
    if (fLicenseInfo.getServerUsersLimit() > 0) {
      NamedUsersGaugeDataSource gaugeUsersDataSource = new NamedUsersGaugeDataSource(getDataBeanProvider(),
              fLicenseInfo.getServerUsersLimit());
      ChartsPanel userChart = new ChartsPanel(false);
      userChart.addGauge(gaugeUsersDataSource);
      registerScheduledUpdate(userChart);

      super.getViewComponent().addDetailsView(
              new DataViewComponent.DetailsView(ContentProvider.get("NamedUsers"), null, 10, userChart.
                      getUIComponent(), null),
              DataViewComponent.TOP_RIGHT);
    }
  }

  private void retrieveLicenseInfo() {
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    try {
      MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
      fLicenseInfo = collector.getLicenseInfo(connection);
    } catch (IvyJmxDataCollectException ex) {
      fLicenseInfo = null;
      LOGGER.warning(ex.getMessage());
    }
  }

  private int fStoredNamedUsers, fStoredConUsers;
  private boolean fNearlyExpiredUpdated, fExpiredUpdated;

  @Override
  public void updateDisplay(QueryResult queryResult) {
    super.updateDisplay(queryResult);
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    int namedUsers = 0;
    int concurrentUsers = 0;
    try {
      MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
      namedUsers = collector.getNamedUsers(connection);
      concurrentUsers = collector.getConcurrentUsers(connection);
    } catch (IvyJmxDataCollectException ex) {
      LOGGER.warning(ex.getMessage());
    }
    long remainingTime = Long.MAX_VALUE;
    if (fLicenseInfo.getLicenseValidUntil() != null) {
      remainingTime = fLicenseInfo.getLicenseValidUntil().getTime() - new Date().getTime();
    }
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
