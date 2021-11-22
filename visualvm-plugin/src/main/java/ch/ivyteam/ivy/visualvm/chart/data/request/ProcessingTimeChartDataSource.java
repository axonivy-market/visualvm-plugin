package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.ContentProvider;
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
    String legend = ContentProvider.getFormatted("RequestProcessingTimeSerieDescription");
    String maxMean = ContentProvider.getFormatted("MaxRequestProcessingTimeDescription");
    String totalMean = ContentProvider.getFormatted("TotalAverageRequestProcessingTimeDescription");
    for (ServerConnectorInfo connector : dataBeanProvider.getGenericData().getServerConnectors()) {
      ObjectName processorName = connector.getGlobalRequestProcessorName();
      String protocol = connector.getDisplayProtocol();
      addDeltaMeanSerie(protocol, MessageFormat.format(legend, protocol), processorName,
              IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
              IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);

      MaxMeanDeltaValueChartLabelCalcSupport maxMeanDeltaValueLabelSupport
              = new MaxMeanDeltaValueChartLabelCalcSupport(
                      MessageFormat.format(MAX_OF, protocol),
                      processorName, IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
                      IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      maxMeanDeltaValueLabelSupport.setTooltip(MessageFormat.format(maxMean, protocol));
      maxMeanDeltaValueLabelSupport.setUnit(MILLISECOND);
      addLabelCalcSupport(maxMeanDeltaValueLabelSupport);

      MeanTotalDeltaValueChartLabelCalcSupport totalMeanDeltaValueLabelSupport
              = new MeanTotalDeltaValueChartLabelCalcSupport(
                      MessageFormat.format(TOTAL_AVG_OF, protocol),
                      processorName, IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME,
                      IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      totalMeanDeltaValueLabelSupport.setTooltip(MessageFormat.format(totalMean, protocol));
      totalMeanDeltaValueLabelSupport.setUnit(MILLISECOND);
      addLabelCalcSupport(totalMeanDeltaValueLabelSupport);
    }
  }

}
