/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.AbstractEDBDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.EDBConnectionChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.EDBProcessingTimeChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.EDBTransactionChartDataSource;
import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

/**
 *
 * @author htnam
 */
public class ExternalDbView extends AbstractView {

  private final Application fIvyApplication;

  public ExternalDbView(IDataBeanProvider dataBeanProvider, Application application) {
    super(dataBeanProvider);
    fIvyApplication = application;
  }

// call when user select environment & db configuration
  private ChartsPanel createExternalDatabaseView() {
    final ChartsPanel externalDbPanel = new ChartsPanel(false);
    EDBConnectionChartDataSource connectionDataSource = new EDBConnectionChartDataSource(
            getDataBeanProvider(), null, null, "Connections");
    EDBTransactionChartDataSource transactionDataSource = new EDBTransactionChartDataSource(
            getDataBeanProvider(), null, null, "Transactions");
    EDBProcessingTimeChartDataSource transProcessTimeDataSource = new EDBProcessingTimeChartDataSource(
            getDataBeanProvider(), null, null, "Processing Time [ms]");

    configDataSources(connectionDataSource, transactionDataSource, transProcessTimeDataSource);
    externalDbPanel.addChart(connectionDataSource);
    externalDbPanel.addChart(transactionDataSource);
    externalDbPanel.addChart(transProcessTimeDataSource);

    return externalDbPanel;
  }

  private void configDataSources(AbstractEDBDataSource... dataSources) {
    for (AbstractEDBDataSource dataSource : dataSources) {
      dataSource.setApplication("Test");
//      dataSource.setEnvironment("Test");
      dataSource.setConfigName("MySQL2");
      dataSource.init();
    }
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    ExternalDbPanel fExternalDBPanel = new ExternalDbPanel(fIvyApplication);
    fExternalDBPanel.addChartPanel(createExternalDatabaseView());
    viewComponent.add(fExternalDBPanel);
    return viewComponent;
  }
}
