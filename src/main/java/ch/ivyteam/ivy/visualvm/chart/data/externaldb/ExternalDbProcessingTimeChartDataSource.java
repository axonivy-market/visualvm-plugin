package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ExternalDbProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  private static final long SCALED_FACTOR = 1000L;
  public static final String SYSTEM = "system";
  public static final String EXTERNAL = "external";
  public static final String MAX_SERIE_DESC = DbChartTitleConstant.MAX_SERIE_DESC.
          replace(SYSTEM, EXTERNAL);
  public static final String MEAN_SERIE_DESC = DbChartTitleConstant.MEAN_SERIE_DESC.
          replace(SYSTEM, EXTERNAL);
  public static final String MIN_SERIE_DESC = DbChartTitleConstant.MIN_SERIE_DESC.
          replace(SYSTEM, EXTERNAL);
  public static final String TOTAL_MEAN_LABEL_DESC = DbChartTitleConstant.TOTAL_MEAN_DESC.
          replace(SYSTEM, EXTERNAL);
  public static final String MAX_OF_MAX_LABEL_DESC = DbChartTitleConstant.MAX_OF_MAX_DESC.
          replace(SYSTEM, EXTERNAL);

  public ExternalDbProcessingTimeChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    setScaleFactor(SCALED_FACTOR);

    MaxValueChartLabelCalcSupport maxOfMaxLabelSupport
            = new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OF_MAX_TITLE,
                    getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME, SCALED_FACTOR);
    maxOfMaxLabelSupport.setTooltip(MAX_OF_MAX_LABEL_DESC);
    maxOfMaxLabelSupport.setUnit("ms");
    addLabelCalcSupport(maxOfMaxLabelSupport);

    MeanTotalDeltaValueChartLabelCalcSupport totalMeanLabelSupport
            = new MeanTotalDeltaValueChartLabelCalcSupport(DbChartTitleConstant.TOTAL_MEAN_TITLE,
                    getObjectName(), ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME,
                    ExternalDatabase.KEY_TRANS_NUMBER, SCALED_FACTOR);
    totalMeanLabelSupport.setTooltip(TOTAL_MEAN_LABEL_DESC);
    totalMeanLabelSupport.setUnit("ms");
    addLabelCalcSupport(totalMeanLabelSupport);

    addSerie(DbChartTitleConstant.MAX_SERIE_TITLE, MAX_SERIE_DESC, SerieStyle.LINE_FILLED,
            getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addDeltaMeanSerie(DbChartTitleConstant.MEAN_SERIE_TITLE, MEAN_SERIE_DESC, SerieStyle.LINE_FILLED,
            getObjectName(), ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER);
    addSerie(DbChartTitleConstant.MIN_SERIE_TITLE, MIN_SERIE_DESC, SerieStyle.LINE_FILLED,
            getObjectName(), ExternalDatabase.KEY_TRANS_MIN_EXE_DELTA_TIME);
  }

}
