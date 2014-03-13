package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ConnectionChartDataSource extends XYChartDataSource {
  private static final String MAX_CONNECTION_TITLE = "Max";
  private static final String MAX_OPEN_CONNECTION_TITLE = "Max open";
  private static final String MAX_USED_CONNECTION_TITLE = "Max used";
  private static final String OPEN_SERIE_TITLE = "Open";
  private static final String USED_SERIE_TITLE = "Used";

  public static final String OPEN_SERIE_DESC = "The number of open connection to the system database";
  public static final String USED_SERIE_DESC = "The number of open connections to the system database for "
          + "which at least one";

  public ConnectionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

    addLabelCalcSupport(new StaticValueChartLabelCalcSupport(MAX_CONNECTION_TITLE, DatabasePersistency.NAME,
            DatabasePersistency.KEY_MAX_CONNECTION));

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OPEN_CONNECTION_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION));

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_USED_CONNECTION_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION));

    addSerie(OPEN_SERIE_TITLE, OPEN_SERIE_DESC,
            SerieStyle.LINE, DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION);

    addSerie(USED_SERIE_TITLE, USED_SERIE_DESC,
            SerieStyle.LINE, DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION);
  }
}
