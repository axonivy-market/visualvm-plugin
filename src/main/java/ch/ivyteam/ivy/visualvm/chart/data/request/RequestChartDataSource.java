package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class RequestChartDataSource extends XYChartDataSource {

  private static final Logger LOGGER = Logger.getLogger(RequestChartDataSource.class.getName());

  public RequestChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    List<ServerConnectorInfo> mappedConnectors = new ArrayList<>();
    try {
      mappedConnectors = collector.getMappedConnectors(mBeanServerConnection);
    } catch (IvyJmxDataCollectException ex) {
      LOGGER.warning(ex.getMessage());
    }
    for (ObjectName processorName : collector.getTomcatRequestProcessors(mBeanServerConnection)) {
      String port = DataUtils.getPort(processorName);
      String protocol = DataUtils.findProtocol(mappedConnectors, port);
      addDeltaSerie(protocol, processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(protocol,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT));
    }
  }

}
