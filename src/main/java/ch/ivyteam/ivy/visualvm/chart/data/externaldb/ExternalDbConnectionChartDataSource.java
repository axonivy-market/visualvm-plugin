package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.support.LatestValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
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

public class ExternalDbConnectionChartDataSource extends AbstractExternalDbDataSource {
  private static final Logger LOGGER = Logger.getLogger(ExternalDbConnectionChartDataSource.class.getName());
  private static final String MAX_SERIE_TITLE = "Max";
  private static final String OPEN_SERIE_TITLE = "Open";
  private static final String USED_SERIE_TITLE = "Used";

  public ExternalDbConnectionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new StaticValueChartLabelCalcSupport(MAX_SERIE_TITLE, getMaxConnection()));

    addLabelCalcSupport(new LatestValueChartLabelCalcSupport(USED_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_USED_CONNECTION));

    addLabelCalcSupport(new LatestValueChartLabelCalcSupport(OPEN_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_OPEN_CONNECTION));

    addSerie(OPEN_SERIE_TITLE, null, SerieStyle.LINE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_OPEN_CONNECTION);

    addSerie(USED_SERIE_TITLE, null, SerieStyle.LINE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_USED_CONNECTION);
  }

  private long getMaxConnection() {
    long maxConnection = 0;
    MBeanServerConnection connection = getDataBeanProvider().getMBeanServerConnection();
    try {
      Object maxConnectionObject = connection.getAttribute(
              getObjectName(),
              IvyJmxConstant.IvyServer.ExternalDatabase.KEY_MAX_CONNECTION);
      maxConnection = NumberUtils.toLong(maxConnectionObject.toString(), 0);
    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException |
            ReflectionException | IOException ex) {
      LOGGER.log(Level.WARNING, "Error when getting max connection", ex);
    }
    return maxConnection;
  }

}
