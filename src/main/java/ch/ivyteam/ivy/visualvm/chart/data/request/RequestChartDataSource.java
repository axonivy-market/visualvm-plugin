package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.text.MessageFormat;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class RequestChartDataSource extends XYChartDataSource {

//  private static final Logger LOGGER = Logger.getLogger(RequestChartDataSource.class.getName());
  public RequestChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    String legendDescription = "Number of new requests served by {0} connector since the last poll";
    for (ObjectName processorName : collector.getTomcatRequestProcessors(mBeanServerConnection)) {
      String protocol = dataBeanProvider.getCachedData().getProtocol(processorName);
      addDeltaSerie(protocol, MessageFormat.format(legendDescription, protocol), SerieStyle.LINE,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport("Max " + protocol,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT));
    }
  }

}
