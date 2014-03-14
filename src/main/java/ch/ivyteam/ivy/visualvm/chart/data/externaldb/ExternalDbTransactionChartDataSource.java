package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DbChartTitleConstant;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class ExternalDbTransactionChartDataSource extends AbstractExternalDbAndWebServiceDataSource {

  public ExternalDbTransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }
  public static final String TRANSACTION_ERROR_SERIE_DESC = DbChartTitleConstant.TRANSACTION_ERROR_SERIE_DESC
          .replace("system", "external");
  public static final String TRANSACTION_SERIE_DESC = DbChartTitleConstant.TRANSACTION_SERIE_DESC
          .replace("system", "external");

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_TRANSACTION_TITLE, getObjectName(),
            ExternalDatabase.KEY_TRANS_NUMBER));
    addLabelCalcSupport(new MaxDeltaValueChartLabelCalcSupport(DbChartTitleConstant.MAX_ERROR_TITLE, getObjectName(),
            ExternalDatabase.KEY_ERROR_NUMBER));
    addDeltaSerie(DbChartTitleConstant.TRANSACTION_TITLE, TRANSACTION_SERIE_DESC,
            SerieStyle.LINE_FILLED, getObjectName(), ExternalDatabase.KEY_TRANS_NUMBER);
    addDeltaSerie(DbChartTitleConstant.ERROR_TITLE, TRANSACTION_ERROR_SERIE_DESC,
            SerieStyle.LINE_FILLED, getObjectName(), ExternalDatabase.KEY_ERROR_NUMBER);
  }

}
