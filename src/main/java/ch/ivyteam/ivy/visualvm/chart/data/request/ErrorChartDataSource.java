package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.service.ProtocolCollector;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class ErrorChartDataSource extends XYChartDataSource {

//  private static final Logger LOGGER = Logger.getLogger(ErrorChartDataSource.class.getName());
  public ErrorChartDataSource(IDataBeanProvider dataBeanProvider, String chartName, String xAxisDescription,
          String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    for (ObjectName processorName : collector.getTomcatRequestProcessors(mBeanServerConnection)) {
      String protocol = ProtocolCollector.getProtocol(mBeanServerConnection, processorName);
      addDeltaSerie(protocol, "Number of request that return status code from 400-599", processorName,
              IvyJmxConstant.Ivy.Processor.KEY_ERROR_COUNT);
      addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(protocol,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_ERROR_COUNT));
    }
  }

}
