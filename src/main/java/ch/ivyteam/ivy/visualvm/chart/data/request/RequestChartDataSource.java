package ch.ivyteam.ivy.visualvm.chart.data.request;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.XYChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.text.MessageFormat;
import javax.management.ObjectName;

public class RequestChartDataSource extends XYChartDataSource {

//  private static final Logger LOGGER = Logger.getLogger(RequestChartDataSource.class.getName());
  public RequestChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    String legendDescription = "Number of new requests served by {0} connector since the last polling";
    for (ServerConnectorInfo connector : dataBeanProvider.getGenericData().getServerConnectors()) {
      String protocol = connector.getDisplayProtocol();
      ObjectName processorName = connector.getGlobalRequestProcessorName();
      addDeltaSerie(protocol, MessageFormat.format(legendDescription, protocol),
              SerieStyle.LINE, processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT);
      addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport("Max " + protocol,
              processorName, IvyJmxConstant.Ivy.Processor.KEY_REQUEST_COUNT));
    }
  }

}
