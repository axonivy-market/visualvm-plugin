package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ExternalDbTransactionChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  public static final String TRANSACTION_ERROR_SERIE_DESC = ContentProvider.getFormatted(
          "ExtDbErrorTransactionSerieDescription");
  public static final String TRANSACTION_SERIE_DESC = ContentProvider.getFormatted(
          "ExtDbTransactionSerieDescription");
  public static final String MAX_TRANSACTION_ERROR_LABEL_DESC = ContentProvider.getFormatted(
          "MaxExtDbErrorTransactionDescription");
  public static final String MAX_TRANSACTION_LABEL_DESC = ContentProvider.getFormatted(
          "MaxExtDbTransactionDescription");

  public ExternalDbTransactionChartDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    MaxDeltaValueChartLabelCalcSupport maxTransactionLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(ContentProvider.get("MaxTransactions"),
                    getObjectName(), ExternalDatabase.KEY_TRANS_NUMBER);
    maxTransactionLabelSupport.setTooltip(MAX_TRANSACTION_LABEL_DESC);
    addLabelCalcSupport(maxTransactionLabelSupport);

    MaxDeltaValueChartLabelCalcSupport maxErrorLabelSupport
            = new MaxDeltaValueChartLabelCalcSupport(ContentProvider.get("MaxErrors"),
                    getObjectName(), ExternalDatabase.KEY_ERROR_NUMBER);
    maxErrorLabelSupport.setTooltip(MAX_TRANSACTION_ERROR_LABEL_DESC);
    addLabelCalcSupport(maxErrorLabelSupport);

    addDeltaSerie(ContentProvider.get("Transactions"), TRANSACTION_SERIE_DESC,
            SerieStyle.LINE_FILLED, getObjectName(), ExternalDatabase.KEY_TRANS_NUMBER);
    addDeltaSerie(ContentProvider.get("Errors"), TRANSACTION_ERROR_SERIE_DESC,
            SerieStyle.LINE_FILLED, getObjectName(), ExternalDatabase.KEY_ERROR_NUMBER);
  }

}
