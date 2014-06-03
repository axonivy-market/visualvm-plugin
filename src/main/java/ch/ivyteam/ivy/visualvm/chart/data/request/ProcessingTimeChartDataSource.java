package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxMeanDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.text.MessageFormat;
import javax.management.ObjectName;

public class ProcessingTimeChartDataSource extends XYChartDataSource {

//  private static final Logger LOGGER = Logger.getLogger(ProcessingTimeChartDataSource.class.getName());
  public ProcessingTimeChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    String legendDesc = "Mean processing time for new requests served by {0} connector since the last polling";
    for (ServerConnectorInfo connector : dataBeanProvider.getGenericData().getServerConnectors()) {
      ObjectName processorName = connector.getGlobalRequestProcessorName();
      String protocol = connector.getDisplayProtocol();
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
