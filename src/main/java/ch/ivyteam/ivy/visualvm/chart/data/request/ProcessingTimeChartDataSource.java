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
    String legend = "Average processing time for new requests served by {0} connector since the last polling";
    String maxMean = "The maximum of the average processing time for new {0} requests in a polling interval. "
            + "Measured since the last start of VisualVM";
    String totalMean = "The total average processing time of all {0} requests. "
            + "Measured since the last start of VisualVM";
    for (ServerConnectorInfo connector : dataBeanProvider.getGenericData().getServerConnectors()) {
      ObjectName processorName = connector.getGlobalRequestProcessorName();
      String protocol = connector.getDisplayProtocol();
      addDeltaMeanSerie(protocol, MessageFormat.format(legend, protocol), processorName,
              IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
              IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);

      MaxMeanDeltaValueChartLabelCalcSupport maxMeanDeltaValueLabelSupport
              = new MaxMeanDeltaValueChartLabelCalcSupport("Max " + protocol,
                      processorName, IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
                      IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      maxMeanDeltaValueLabelSupport.setTooltip(MessageFormat.format(maxMean, protocol));
      maxMeanDeltaValueLabelSupport.setUnit("ms");
      addLabelCalcSupport(maxMeanDeltaValueLabelSupport);

      MeanTotalDeltaValueChartLabelCalcSupport totalMeanDeltaValueLabelSupport
              = new MeanTotalDeltaValueChartLabelCalcSupport("Total avg " + protocol,
                      processorName, IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
                      IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      totalMeanDeltaValueLabelSupport.setTooltip(MessageFormat.format(totalMean, protocol));
      totalMeanDeltaValueLabelSupport.setUnit("ms");
      addLabelCalcSupport(totalMeanDeltaValueLabelSupport);
    }
  }

}
