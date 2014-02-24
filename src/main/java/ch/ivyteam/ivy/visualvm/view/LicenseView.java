package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MLicenseChartDataSource;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import com.sun.tools.visualvm.core.options.GlobalPreferences;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.management.MBeanServerConnection;
import javax.swing.JComponent;
import org.openide.util.Exceptions;

public class LicenseView extends AbstractView {

  private boolean uiComplete;
  private IvyLicenseInfo fLicenseInfo;
  private LicenseInformationPanel fLicenseInformationPanel;

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
  protected JComponent getMasterViewComponent() {
    return fLicenseInformationPanel;
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      createLicenseChartView();
      uiComplete = true;
    }
    return viewComponent;
  }

  private void createLicenseChartView() {
    if (fLicenseInfo.getServerSessionsLimit() > 0) {
      createSessionChart();
    }
  }

  private void createSessionChart() {
    MLicenseChartDataSource sessionDataSource = new MLicenseChartDataSource(
            getDataBeanProvider(), "Sessions", null, "Sessions");

    ChartsPanel sessionChart = new ChartsPanel();
    sessionChart.addChart(sessionDataSource);
    getUpdatableUIObjects().add(sessionChart);

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Concurrent Users",
            null, 10, sessionChart.getUiComponent(), null), DataViewComponent.TOP_LEFT);
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
    fLicenseInformationPanel.setValidUntilInfo();
  }

}
