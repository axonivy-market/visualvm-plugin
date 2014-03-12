package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbTransactionChartDataSource extends AbstractExternalDbDataSource {

  private static final String TRANSACTION_SERIE_TITLE = "Transaction";
  private static final String ERRORS_SERIE_TITLE = "Errors";
  private static final String MAX_TRANSACTION_SERIE_TITLE = "Max transaction";
  private static final String MAX_ERRORS_SERIE_TITLE = "Max error";
  public static final String TRANSACTION_SERIE_DESC = "The number of database transactions that "
          + "have finished since the last poll";
  public static final String ERRORS_SERIE_DESC = "The number of database transactions that "
          + "finished since the last poll";

  public ExternalDbTransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(MAX_TRANSACTION_SERIE_TITLE, getObjectName(),
            ExternalDatabase.KEY_TRANS_NUMBER));
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(MAX_ERRORS_SERIE_TITLE, getObjectName(),
            ExternalDatabase.KEY_ERROR_NUMBER));
    addDeltaSerie(TRANSACTION_SERIE_TITLE, TRANSACTION_SERIE_DESC, getObjectName(),
            ExternalDatabase.KEY_TRANS_NUMBER);
    addDeltaSerie(ERRORS_SERIE_TITLE, ERRORS_SERIE_DESC, getObjectName(), ExternalDatabase.KEY_ERROR_NUMBER);
  }

}
