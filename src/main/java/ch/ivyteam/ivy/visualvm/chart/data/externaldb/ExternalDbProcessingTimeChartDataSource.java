package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.LatestValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbProcessingTimeChartDataSource extends AbstractEDBDataSource {
  public static final String MIN_SERIE_TITLE = "Min";
  public static final String MAX_SERIE_TITLE = "Max";
  public static final String MEAN_SERIE_TITLE = "Mean";

  public ExternalDbProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport(MAX_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_MAX_EXE_DELTA_TIME_IN_MS));
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport(MIN_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_MIN_EXE_DELTA_TIME_IN_MS));
    addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(MEAN_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS));

    addSerie(MAX_SERIE_TITLE, SerieStyle.LINE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_MAX_EXE_DELTA_TIME_IN_MS);
    addSerie(MIN_SERIE_TITLE, SerieStyle.LINE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_MIN_EXE_DELTA_TIME_IN_MS);
    addDeltaSerie(MEAN_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS);

  }

}
