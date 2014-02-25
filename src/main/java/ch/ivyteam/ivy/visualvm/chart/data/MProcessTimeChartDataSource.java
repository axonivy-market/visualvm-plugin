package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class MProcessTimeChartDataSource extends MChartDataSource {
  public MProcessTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    MBeanServerConnection mBeanServerConnection = getDataBeanProvider().getMBeanServerConnection();
    for (ObjectName processorName : DataUtils.getTomcatRequestProcessors(mBeanServerConnection)) {
      String protocol = DataUtils.getProtocol(processorName);
      addDeltaSerie(protocol, processorName, IvyJmxConstant.Ivy.Processor.KEY_PROCESS_TIME);
    }
  }

}
