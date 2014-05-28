package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ExternalDbConnectionChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  public static final String OPEN_SERIE_DESC = DbChartTitleConstant.OPEN_SERIE_DESC.
          replace("system", "external");
  public static final String USED_SERIE_DESC = DbChartTitleConstant.USED_SERIE_DESC.
          replace("system", "external");

  public ExternalDbConnectionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new StaticValueChartLabelCalcSupport(DbChartTitleConstant.LIMIT_CONNECTION_TITLE,
            getObjectName(), ExternalDatabase.KEY_MAX_CONNECTION));
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OPEN_CONNECTION_TITLE,
            getObjectName(), ExternalDatabase.KEY_OPEN_CONNECTION));
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_USED_CONNECTION_TITLE,
            getObjectName(), ExternalDatabase.KEY_USED_CONNECTION));

    addSerie(DbChartTitleConstant.OPEN_SERIE_TITLE, OPEN_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_OPEN_CONNECTION);
    addSerie(DbChartTitleConstant.USED_SERIE_TITLE, USED_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_USED_CONNECTION);
  }

}
