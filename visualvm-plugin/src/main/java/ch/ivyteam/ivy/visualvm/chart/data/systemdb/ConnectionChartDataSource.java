package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
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
            = new StaticValueChartLabelCalcSupport(ContentProvider.get("Limit"),
                    DatabasePersistency.NAME, DatabasePersistency.KEY_MAX_CONNECTION);
    limitConnectionLabelSupport.setTooltip(ContentProvider.getFormatted("LimitConnectionDescription"));
    addLabelCalcSupport(limitConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxOpenConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(ContentProvider.get("MaxOpen"),
                    DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION);
    maxOpenConnectionLabelSupport.
            setTooltip(ContentProvider.getFormatted("MaxSysDbOpenConnectionDescription"));
    addLabelCalcSupport(maxOpenConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxUsedConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(ContentProvider.get("MaxUsed"),
                    DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION);
    maxUsedConnectionLabelSupport.
            setTooltip(ContentProvider.getFormatted("MaxSysDbUsedConnectionDescription"));
    addLabelCalcSupport(maxUsedConnectionLabelSupport);

    addSerie(ContentProvider.get("Open"), ContentProvider.getFormatted("SysDbOpenConnectionSerieDescription"),
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION);

    addSerie(ContentProvider.get("Used"), ContentProvider.getFormatted("SysDbUsedConnectionSerieDescription"),
            SerieStyle.LINE_FILLED, DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION);
  }

}
