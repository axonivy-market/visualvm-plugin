package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.awt.Color;
import javax.management.MBeanServerConnection;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;
import org.openide.util.Exceptions;

public class LicenseView extends AbstractView {

  private boolean uiComplete;
  private IvyLicenseInfo fLicenseInfo;

  public LicenseView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    retrieveLicenseInfo();
  }

  @Override
  protected String getMasterViewTitle() {
    return "License Information";
  }

  @Override
  protected JComponent getMasterViewComponent() {
    return new LicenseInformationPanel(fLicenseInfo);
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      createLicenseChartView();
      uiComplete = true;
    }
    viewComponent.setBorder(new LineBorder(Color.GREEN));
    return viewComponent;
  }

  private void createLicenseChartView() {
    if (fLicenseInfo.getServerSessionsLimit() > 0) {
      createSessionChart();
    }
  }

  private void createSessionChart() {
    MChartDataSource sessionDataSource = new MChartDataSource("Sessions", null, "Sessions");
    sessionDataSource.addSerie("Licensed", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    sessionDataSource.addFixedSerie("Max", fLicenseInfo.getServerSessionsLimit());

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

}
