package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ExternalDbConnectionChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  public static final String SYSTEM = "system";
  public static final String EXTERNAL = "external";

  public static final String OPEN_SERIE_DESC = DbChartTitleConstant.OPEN_SERIE_DESC.
          replace(SYSTEM, EXTERNAL);
  public static final String USED_SERIE_DESC = DbChartTitleConstant.USED_SERIE_DESC.
          replace(SYSTEM, EXTERNAL);
  public static final String MAX_OPEN_LABEL_DESC = DbChartTitleConstant.MAX_OPEN_CONNECTION_DESC.
          replace(SYSTEM, EXTERNAL);
  public static final String MAX_USED_LABEL_DESC = DbChartTitleConstant.MAX_USED_CONNECTION_DESC.
          replace(SYSTEM, EXTERNAL);

  public ExternalDbConnectionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    StaticValueChartLabelCalcSupport limitConnectionLabelSupport
            = new StaticValueChartLabelCalcSupport(DbChartTitleConstant.LIMIT_CONNECTION_TITLE,
                    getObjectName(), ExternalDatabase.KEY_MAX_CONNECTION);
    limitConnectionLabelSupport.setTooltip(DbChartTitleConstant.MAX_CONNECTION_DESC);
    addLabelCalcSupport(limitConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxOpenConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_OPEN_CONNECTION_TITLE,
                    getObjectName(), ExternalDatabase.KEY_OPEN_CONNECTION);
    maxOpenConnectionLabelSupport.setTooltip(MAX_OPEN_LABEL_DESC);
    addLabelCalcSupport(maxOpenConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxUsedConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(DbChartTitleConstant.MAX_USED_CONNECTION_TITLE,
                    getObjectName(), ExternalDatabase.KEY_USED_CONNECTION);
    maxUsedConnectionLabelSupport.setTooltip(MAX_USED_LABEL_DESC);
    addLabelCalcSupport(maxUsedConnectionLabelSupport);

    addSerie(DbChartTitleConstant.OPEN_SERIE_TITLE, OPEN_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_OPEN_CONNECTION);
    addSerie(DbChartTitleConstant.USED_SERIE_TITLE, USED_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_USED_CONNECTION);
  }

}
