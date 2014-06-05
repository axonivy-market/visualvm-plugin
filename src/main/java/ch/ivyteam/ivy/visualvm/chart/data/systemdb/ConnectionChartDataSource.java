package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ConnectionChartDataSource extends XYChartDataSource {

  public ConnectionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

    StaticValueChartLabelCalcSupport limitConnectionLabelSupport
            = new StaticValueChartLabelCalcSupport(DbChartTitleConstant.LIMIT_CONNECTION_TITLE,
                    DatabasePersistency.NAME, DatabasePersistency.KEY_MAX_CONNECTION);
    limitConnectionLabelSupport.setTooltip(DbChartTitleConstant.MAX_CONNECTION_DESC);
    addLabelCalcSupport(limitConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxOpenConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OPEN_CONNECTION_TITLE,
                    DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION);
    maxOpenConnectionLabelSupport.setTooltip(DbChartTitleConstant.MAX_OPEN_CONNECTION_DESC);
    addLabelCalcSupport(maxOpenConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxUsedConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_USED_CONNECTION_TITLE,
                    DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION);
    maxUsedConnectionLabelSupport.setTooltip(DbChartTitleConstant.MAX_USED_CONNECTION_DESC);
    addLabelCalcSupport(maxUsedConnectionLabelSupport);

    addSerie(DbChartTitleConstant.OPEN_SERIE_TITLE, DbChartTitleConstant.OPEN_SERIE_DESC,
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION);

    addSerie(DbChartTitleConstant.USED_SERIE_TITLE, DbChartTitleConstant.USED_SERIE_DESC,
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION);
  }

}
