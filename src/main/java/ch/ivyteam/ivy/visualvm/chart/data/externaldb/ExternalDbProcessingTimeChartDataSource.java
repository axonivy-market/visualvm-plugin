package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbProcessingTimeChartDataSource extends AbstractExternalDbDataSource {

  public static final String MIN_SERIE_TITLE = "Min";
  public static final String MAX_SERIE_TITLE = "Max";
  public static final String MEAN_SERIE_TITLE = "Total mean";
  public static final String MEAN_SERIE_DESC = "The mean time of all transactions that has finished in "
          + "last poll";
  public static final String MAX_SERIE_DESC = "The maximum time of all transactions that has finished in "
          + "last poll";
  public static final String MIN_SERIE_DESC = "The minimum time of all transactions that has finished in "
          + "last poll";

  public ExternalDbProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport("Max of max",
            getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_TIME));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(MEAN_SERIE_TITLE, getObjectName(),
            ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER));

    addDeltaSerie(MEAN_SERIE_TITLE, MEAN_SERIE_DESC, getObjectName(),
            ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME);
    addSerie(MAX_SERIE_TITLE, MAX_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addSerie(MIN_SERIE_TITLE, MIN_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_TRANS_MIN_EXE_DELTA_TIME);

  }

}
