package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class RequestChartDataSource extends XYChartDataSource {

//  private static final Logger LOGGER = Logger.getLogger(RequestChartDataSource.class.getName());
  public RequestChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    for (ObjectName processorName : collector.getTomcatRequestProcessors(mBeanServerConnection)) {
      String protocol = dataBeanProvider.getCachedData().getProtocol(processorName);
      addDeltaSerie(protocol, null, processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(protocol,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT));
    }
  }

}
