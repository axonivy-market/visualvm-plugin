package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ExternalDbConnectionChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  public static final String OPEN_SERIE_DESC = ContentProvider.getFormatted(
          "ExtDbOpenConnectionSerieDescription");
  public static final String USED_SERIE_DESC = ContentProvider.getFormatted(
          "ExtDbUsedConnectionSerieDescription");
  public static final String MAX_OPEN_LABEL_DESC = ContentProvider.getFormatted(
          "MaxExtDbOpenConnectionDescription");
  public static final String MAX_USED_LABEL_DESC = ContentProvider.getFormatted(
          "MaxExtDbUsedConnectionDescription");

  public ExternalDbConnectionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    StaticValueChartLabelCalcSupport limitConnectionLabelSupport
            = new StaticValueChartLabelCalcSupport(ContentProvider.get("Limit"),
                    getObjectName(), ExternalDatabase.KEY_MAX_CONNECTION);
    limitConnectionLabelSupport.setTooltip(ContentProvider.getFormatted("LimitConnectionDescription"));
    addLabelCalcSupport(limitConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxOpenConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(ContentProvider.get("MaxOpen"),
                    getObjectName(), ExternalDatabase.KEY_OPEN_CONNECTION);
    maxOpenConnectionLabelSupport.setTooltip(MAX_OPEN_LABEL_DESC);
    addLabelCalcSupport(maxOpenConnectionLabelSupport);

    MaxValueChartLabelCalcSupport maxUsedConnectionLabelSupport
            = new MaxValueChartLabelCalcSupport(ContentProvider.get("MaxUsed"),
                    getObjectName(), ExternalDatabase.KEY_USED_CONNECTION);
    maxUsedConnectionLabelSupport.setTooltip(MAX_USED_LABEL_DESC);
    addLabelCalcSupport(maxUsedConnectionLabelSupport);

    addSerie(ContentProvider.get("Open"), OPEN_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_OPEN_CONNECTION);
    addSerie(ContentProvider.get("Used"), USED_SERIE_DESC, SerieStyle.LINE_FILLED, getObjectName(),
            ExternalDatabase.KEY_USED_CONNECTION);
  }

}
