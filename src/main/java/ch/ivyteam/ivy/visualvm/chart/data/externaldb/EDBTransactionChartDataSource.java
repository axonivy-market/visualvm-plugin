package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class EDBTransactionChartDataSource extends AbstractEDBDataSource {
    private static final String TRANSACTION = "Transaction";

    public EDBTransactionChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
            String xAxisDescription, String yAxisDescription) {
        super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
    }

    @Override
    public void init() {
        super.init();
        addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(TRANSACTION, getObjectName(),
                IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_NUMBER));
        addDeltaSerie(TRANSACTION, getObjectName(),
                IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_NUMBER);
    }

}
