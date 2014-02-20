package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MChartDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

public class SystemDatabaseView extends AbstractView {

  private static final String TIME_US = "Time [us]";
  private static final String MIN = "Min";
  private static final String MEAN = "Mean";
  private static final String MAX = "Max";
  private static final String ERRORS = "Errors";
  private static final String TRANSACTIONS = "Transactions";
  private boolean uiComplete;

  public SystemDatabaseView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createSystemDbView() {
    super.getViewComponent().configureDetailsArea(
            new DataViewComponent.DetailsAreaConfiguration(
                    "External Systems", false),
            DataViewComponent.BOTTOM_LEFT);
    ChartsPanel systemDbPanel = new ChartsPanel();
    getUpdatableUIObjects().add(systemDbPanel);

    MChartDataSource dataSource = new MChartDataSource(
            "Transaction Processing Time", null, TIME_US);
    dataSource.addSerie(MAX, IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
                        "transactionsMaxExecutionTimeDeltaInMicroSeconds");
    dataSource.addDeltaMeanSerie(MEAN,
                                 IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
                                 "transactionsTotalExecutionTimeInMicroSeconds",
                                 TRANSACTIONS.toLowerCase());
    dataSource.addSerie(MIN, IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
                        "transactionsMinExecutionTimeDeltaInMicroSeconds");
    systemDbPanel.addChart(dataSource);

    dataSource = new MChartDataSource(TRANSACTIONS, null, TRANSACTIONS);
    dataSource.addDeltaSerie(TRANSACTIONS,
                             IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
                             TRANSACTIONS.toLowerCase());
    dataSource.addDeltaSerie(ERRORS,
                             IvyJmxConstant.IvyServer.DatabasePersistency.NAME, "errors");
    systemDbPanel.addChart(dataSource);

    dataSource = new MChartDataSource("Connections", null, "Connections");
    dataSource.addSerie(MAX, IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
                        "maxConnections");
    dataSource
            .addSerie("Open", IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
                      "openConnections");
    dataSource
            .addSerie("Used", IvyJmxConstant.IvyServer.DatabasePersistency.NAME,
                      "usedConnections");
    systemDbPanel.addChart(dataSource);

    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("System Database",
                                                                              null, 10, systemDbPanel.
            getUiComponent(), null),
                                            DataViewComponent.BOTTOM_LEFT);
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      createSystemDbView();
      uiComplete = true;
    }
    return viewComponent;
  }

}
