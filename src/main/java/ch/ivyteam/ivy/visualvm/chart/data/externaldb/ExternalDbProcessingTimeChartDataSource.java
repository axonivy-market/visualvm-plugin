package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  public static final String MAX_SERIE_DESC = DbChartTitleConstant.MAX_SERIE_DESC.
          replace("system", "external");
  public static final String MEAN_SERIE_DESC = DbChartTitleConstant.MEAN_SERIE_DESC.
          replace("system", "external");
  public static final String MIN_SERIE_DESC = DbChartTitleConstant.MIN_SERIE_DESC.
          replace("system", "external");

  public ExternalDbProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OF_MAX_TITLE,
            getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(DbChartTitleConstant.TOTAL_MEAN_TITLE,
            getObjectName(), ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER));

    addSerie(DbChartTitleConstant.MAX_SERIE_TITLE, MAX_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addDeltaMeanSerie(DbChartTitleConstant.MEAN_SERIE_TITLE, MEAN_SERIE_DESC, SerieStyle.LINE_FILLED,
            getObjectName(), ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER);
    addSerie(DbChartTitleConstant.MIN_SERIE_TITLE, MIN_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_TRANS_MIN_EXE_DELTA_TIME);
  }

}
