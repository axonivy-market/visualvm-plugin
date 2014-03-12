package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.externaldb.AbstractDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class WebServiceProcessingTimeChartDataSource extends AbstractDataSource {
  public static final String MIN_SERIE_TITLE = "Min";
  public static final String MAX_SERIE_TITLE = "Max";
  public static final String MEAN_SERIE_TITLE = "Mean";
  public static final String MAX_OF_MAX = "Max of max";
  public static final String TOTAL_MEAN = "Total mean";

  public WebServiceProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OF_MAX, getObjectName(),
            WebService.KEY_CALL_MAX_EXE_TIME_DELTA));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(TOTAL_MEAN, DatabasePersistency.NAME,
            WebService.KEY_CALL_TOTAL_EXE_TIME,
            WebService.KEY_CALLS));

    addSerie(MAX_SERIE_TITLE, "The maximum time of all calls that has finished in last poll", SerieStyle.LINE, getObjectName(),
            WebService.KEY_CALL_MAX_EXE_TIME_DELTA);
    addSerie(MIN_SERIE_TITLE, "The minimum time of all calls that has finished in last poll", SerieStyle.LINE, getObjectName(),
            WebService.KEY_CALL_MIN_EXE_TIME_DELTA);
  }
}
