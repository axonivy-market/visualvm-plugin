package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

public class LicenseView extends AbstractView {

  private final LicenseInformationPanel fLicenseInformationPanel;
  private boolean uiComplete;

  public LicenseView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fLicenseInformationPanel = new LicenseInformationPanel();
    fLicenseInformationPanel.readInformation(getDataBeanProvider().getMBeanServerConnection());
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
    viewComponent.setBorder(new LineBorder(Color.GREEN));
    return viewComponent;
  }

  private void createLicenseChartView() {
    createSessionChart();
  }

  private void createSessionChart() {
    MChartDataSource sessionDataSource = new MChartDataSource("Sessions", null, "Sessions");
    sessionDataSource.addSerie("Licensed", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    long maxUsers = Long.parseLong(fLicenseInformationPanel.getServerUsersLimitValue().getText());
    sessionDataSource.addFixedSerie("Max", maxUsers);

    ChartsPanel sessionChart = new ChartsPanel();
    sessionChart.addChart(sessionDataSource);
    getUpdatableUIObjects().add(sessionChart);

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Sessions",
            null, 10, sessionChart.getUiComponent(), null), DataViewComponent.TOP_LEFT);
  }

}
