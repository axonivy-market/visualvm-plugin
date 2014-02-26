package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.MGaugeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.MLicenseChartDataSource;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import eu.hansolo.steelseries.tools.Section;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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
      createLicenseChartView();
      createUserGaugeView();
      createSessionGaugeView();
      uiComplete = true;
    }
    return viewComponent;
  }

  private void createLicenseInfoView() {
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("General Information",
            null, 10, fLicenseInformationPanel, null), DataViewComponent.TOP_LEFT);

  }

  private void createLicenseChartView() {
    if (fLicenseInfo.getServerSessionsLimit() > 0) {
      createSessionChart();
    }
  }

  private void createSessionChart() {
    MLicenseChartDataSource sessionDataSource = new MLicenseChartDataSource(
            getDataBeanProvider(), null, null, "Concurrent Users");

    ChartsPanel sessionChart = new ChartsPanel(false);
    sessionChart.addChart(sessionDataSource);
    registerScheduledUpdate(sessionChart);

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Concurrent Users",
            null, 10, sessionChart.getUiComponent(), null), DataViewComponent.TOP_RIGHT);
  }

  private void createUserGaugeView() {
    if (fLicenseInfo.getServerUsersLimit() > 0) {
      createUserGauge();
    }
  }

  private void createUserGauge() {
    List<Section> sections = new ArrayList<>();
    int usersLimit = fLicenseInfo.getServerUsersLimit();
    Section greenSection = new Section(0, Math.round(usersLimit * 0.9), Color.GREEN);
    Section orangeSection = new Section(Math.round(usersLimit * 0.9), Math.round(usersLimit * 0.95),
            Color.ORANGE);
    Section redSection = new Section(Math.round(usersLimit * 0.95), usersLimit, Color.RED);
    sections.add(greenSection);
    sections.add(orangeSection);
    sections.add(redSection);
    MGaugeDataSource dataSource = new MGaugeDataSource(getDataBeanProvider(), sections,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_USERS);
    ChartsPanel userChart = new ChartsPanel(false);
    userChart.addGauge(dataSource);
    registerScheduledUpdate(userChart);

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Named Users",
            null, 10, userChart.getUiComponent(), null), DataViewComponent.BOTTOM_RIGHT);
  }

  private void createSessionGaugeView() {
    if (fLicenseInfo.getServerSessionsLimit() > 0) {
      createSessionGauge();
    }
  }

  private void createSessionGauge() {
    List<Section> sections = new ArrayList<>();
    int sessionsLimit = fLicenseInfo.getServerSessionsLimit();
    Section greenSection = new Section(0, Math.round(sessionsLimit * 0.9), Color.GREEN);
    Section orangeSection = new Section(Math.round(sessionsLimit * 0.9), sessionsLimit, Color.ORANGE);
    Section redSection = new Section(sessionsLimit, Math.round(sessionsLimit * 1.5), Color.RED);
    sections.add(greenSection);
    sections.add(orangeSection);
    sections.add(redSection);
    MGaugeDataSource dataSource = new MGaugeDataSource(getDataBeanProvider(), sections,
            IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    ChartsPanel sessionsChart = new ChartsPanel(false);
    sessionsChart.addGauge(dataSource);
    registerScheduledUpdate(sessionsChart);

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Concurrent Users",
            null, 10, sessionsChart.getUiComponent(), null), DataViewComponent.BOTTOM_LEFT);
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

  @Override
  public void update() {
    super.update();
    fLicenseInfo.setRemainingTime(fLicenseInfo.getRemaingTime() - 1000 * GlobalPreferences.sharedInstance().
            getMonitoredDataPoll());
    fLicenseInformationPanel.setRemainingTimeInfo();
  }

}
