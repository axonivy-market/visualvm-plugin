package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.text.MessageFormat;
import javax.management.ObjectName;

public class ErrorChartDataSource extends XYChartDataSource {

//  private static final Logger LOGGER = Logger.getLogger(ErrorChartDataSource.class.getName());
  public ErrorChartDataSource(DataBeanProvider dataBeanProvider, String chartName, String xAxisDescription,
          String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    String legend = ContentProvider.getFormatted("RequestErrorSerieDescription");
    String labelDescription = ContentProvider.getFormatted("MaxRequestErrorDescription");
    for (ServerConnectorInfo connector : dataBeanProvider.getGenericData().getServerConnectors()) {
      String protocol = connector.getDisplayProtocol();
      ObjectName processorName = connector.getGlobalRequestProcessorName();
      addDeltaSerie(protocol, MessageFormat.format(legend, protocol),
              SerieStyle.LINE, processorName, IvyJmxConstant.Ivy.Processor.KEY_ERROR_COUNT);
      MaxDeltaValueChartLabelCalcSupport maxDeltaValueLabelSupport
              = new MaxDeltaValueChartLabelCalcSupport(MessageFormat.format(MAX_OF, protocol),
                      processorName, IvyJmxConstant.Ivy.Processor.KEY_ERROR_COUNT);
      maxDeltaValueLabelSupport.setTooltip(MessageFormat.format(labelDescription, protocol));
      addLabelCalcSupport(maxDeltaValueLabelSupport);
    }
  }

}
