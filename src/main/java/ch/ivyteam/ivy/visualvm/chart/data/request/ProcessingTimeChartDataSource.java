package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxMeanDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.text.MessageFormat;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class ProcessingTimeChartDataSource extends XYChartDataSource {

//  private static final Logger LOGGER = Logger.getLogger(ProcessingTimeChartDataSource.class.getName());
  public ProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();
    String legendDesc = "Mean processing time for new requests served by {0} connector since the last poll";
    for (ObjectName processorName : collector.getTomcatRequestProcessors(mBeanServerConnection)) {
      String protocol = dataBeanProvider.getCachedData().getProtocol(processorName);
      addDeltaMeanSerie(protocol, MessageFormat.format(legendDesc, protocol), processorName,
              IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
              IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      addLabelCalcSupport(new MaxMeanDeltaValueChartLabelCalcSupport("Max " + protocol,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
              IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT));
      addLabelCalcSupport(new MeanTotalDeltaValueChartLabelCalcSupport("Total mean " + protocol,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
              IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT));
    }
  }

}
