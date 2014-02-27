package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.openide.util.Exceptions;

public class MRequestChartDataSource extends MChartDataSource {
  public MRequestChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    List<ServerConnectorInfo> mappedConnectors = new ArrayList<>();
    try {
      mappedConnectors = collector.getMappedConnectors(mBeanServerConnection);
    } catch (IvyJmxDataCollectException ex) {
      Exceptions.printStackTrace(ex);
    }
    for (ObjectName processorName : collector.getTomcatRequestProcessors(mBeanServerConnection)) {
      String port = DataUtils.getPort(processorName);
      String protocol = DataUtils.findProtocol(mappedConnectors, port);
      addDeltaSerie(protocol, processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
    }
  }

}
