package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.LatestValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbProcessingTimeChartDataSource extends AbstractExternalDbDataSource {
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
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANS_MAX_EXE_DEL_TIME));
    addLabelCalcSupport(new LatestValueChartLabelCalcSupport(MIN_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANS_MIN_EXE_DEL_TIME));
    addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(MEAN_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME));

    addSerie(MAX_SERIE_TITLE, null, SerieStyle.LINE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANS_MAX_EXE_DEL_TIME);
    addSerie(MIN_SERIE_TITLE, null, SerieStyle.LINE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANS_MIN_EXE_DEL_TIME);
    addDeltaSerie(MEAN_SERIE_TITLE, null, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME);

  }

}
