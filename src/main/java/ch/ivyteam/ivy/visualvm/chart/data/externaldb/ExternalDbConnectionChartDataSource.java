package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
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

public class ExternalDbConnectionChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  private static final Logger LOGGER = Logger.getLogger(ExternalDbConnectionChartDataSource.class.getName());
  private static final String MAX_TITLE = "Max";
  private static final String OPEN_SERIE_TITLE = "Open";
  private static final String USED_SERIE_TITLE = "Used";
  private static final String MAX_OPEN_TITLE = "Max open";
  private static final String MAX_USED_TITLE = "Max used";
  public static final String OPEN_SERIE_DESC = "The number of open connections to the external database";
  public static final String USED_SERIE_DESC = "The number of open connections to the external database for "
          + "which at least one";

  public ExternalDbConnectionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new StaticValueChartLabelCalcSupport(MAX_TITLE, getMaxConnection()));
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_USED_TITLE, getObjectName(),
            ExternalDatabase.KEY_USED_CONNECTION));
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OPEN_TITLE, getObjectName(),
            ExternalDatabase.KEY_OPEN_CONNECTION));

    addSerie(OPEN_SERIE_TITLE, OPEN_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_OPEN_CONNECTION);
    addSerie(USED_SERIE_TITLE, USED_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_USED_CONNECTION);
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
