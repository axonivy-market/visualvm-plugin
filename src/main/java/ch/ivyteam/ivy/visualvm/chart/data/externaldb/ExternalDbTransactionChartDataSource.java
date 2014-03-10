package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbTransactionChartDataSource extends AbstractEDBDataSource {
  private static final String TRANSACTION_SERIE_TITLE = "Transaction";
  private static final String ERRORS_SERIE_TITLE = "Errors";

  public ExternalDbTransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(TRANSACTION_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_NUMBER));
    addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(ERRORS_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_ERROR_NUMBER));
    addDeltaSerie(TRANSACTION_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_NUMBER);
    addDeltaSerie(ERRORS_SERIE_TITLE, getObjectName(),
            IvyJmxConstant.IvyServer.ExternalDatabase.KEY_ERROR_NUMBER);
  }

}
