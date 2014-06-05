package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ExternalDbTransactionChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  public static final String SYSTEM = "system";
  public static final String EXTERNAL = "external";
  public static final String TRANSACTION_ERROR_SERIE_DESC = DbChartTitleConstant.TRANSACTION_ERROR_SERIE_DESC
          .replace(SYSTEM, EXTERNAL);
  public static final String TRANSACTION_SERIE_DESC = DbChartTitleConstant.TRANSACTION_SERIE_DESC
          .replace(SYSTEM, EXTERNAL);
  public static final String MAX_TRANSACTION_ERROR_LABEL_DESC = DbChartTitleConstant.MAX_ERROR_DESC
          .replace(SYSTEM, EXTERNAL);
  public static final String MAX_TRANSACTION_LABEL_DESC = DbChartTitleConstant.MAX_TRANSACTION_DESC
          .replace(SYSTEM, EXTERNAL);

  public ExternalDbTransactionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    MaxDeltaValueChartLabelCalcSupport maxTransactionLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_TRANSACTION_TITLE,
                    getObjectName(), ExternalDatabase.KEY_TRANS_NUMBER);
    maxTransactionLabelSupport.setTooltip(MAX_TRANSACTION_LABEL_DESC);
    addLabelCalcSupport(maxTransactionLabelSupport);

    MaxDeltaValueChartLabelCalcSupport maxErrorLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_ERROR_TITLE,
                    getObjectName(), ExternalDatabase.KEY_ERROR_NUMBER);
    maxErrorLabelSupport.setTooltip(MAX_TRANSACTION_ERROR_LABEL_DESC);
    addLabelCalcSupport(maxErrorLabelSupport);

    addDeltaSerie(DbChartTitleConstant.TRANSACTION_TITLE, TRANSACTION_SERIE_DESC,
            SerieStyle.LINE_FILLED, getObjectName(), ExternalDatabase.KEY_TRANS_NUMBER);
    addDeltaSerie(DbChartTitleConstant.ERROR_TITLE, TRANSACTION_ERROR_SERIE_DESC,
            SerieStyle.LINE_FILLED, getObjectName(), ExternalDatabase.KEY_ERROR_NUMBER);
  }

}
