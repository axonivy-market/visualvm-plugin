package ch.ivyteam.ivy.visualvm.chart.data.systemdb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ReflectionException;
import org.apache.commons.lang.math.NumberUtils;

public class ConnectionChartDataSource extends XYChartDataSource {

  private static final Logger LOGGER = Logger.getLogger(ConnectionChartDataSource.class.getName());
  private static final String MAX_CONNECTION_TITLE = "Max";
  private static final String MAX_OPEN_CONNECTION_TITLE = "Max open";
  private static final String MAX_USED_CONNECTION_TITLE = "Max used";
  private static final String OPEN_SERIE_TITLE = "Open";
  private static final String USED_SERIE_TITLE = "Used";

  public ConnectionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    addLabelCalcSupport(new StaticValueChartLabelCalcSupport(MAX_CONNECTION_TITLE, getMaxConnection()));

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OPEN_CONNECTION_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION));

    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_USED_CONNECTION_TITLE,
            DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION));

    addSerie(OPEN_SERIE_TITLE, "The number of open connection to the system database",
            SerieStyle.LINE, DatabasePersistency.NAME, DatabasePersistency.KEY_OPEN_CONNECTION);

    addSerie(USED_SERIE_TITLE,
            "The number of open connections to the system database for which at least one",
            SerieStyle.LINE, DatabasePersistency.NAME, DatabasePersistency.KEY_USED_CONNECTION);
  }

  private long getMaxConnection() {
    long maxConnection = 0;
    MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
    try {
      Object maxConnectionObject = connection.getAttribute(DatabasePersistency.NAME,
              DatabasePersistency.KEY_MAX_CONNECTION);
      maxConnection = NumberUtils.toLong(maxConnectionObject.toString(), 0);
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException
            | IOException ex) {
      LOGGER.log(Level.WARNING, "Error when getting max connection", ex);
    }
    return maxConnection;
  }
}
