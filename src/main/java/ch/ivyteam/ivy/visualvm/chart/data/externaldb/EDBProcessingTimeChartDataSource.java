package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;

public class EDBProcessingTimeChartDataSource extends AbstractEDBDataSource {
    public static final String PROCESSING_TIME = "Processing time";

    public EDBProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
            String xAxisDescription, String yAxisDescription) {
        super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

    }

    @Override
    public void init() {
        super.init();
        addLabelCalcSupport(new DeltaValueChartLabelCalcSupport(PROCESSING_TIME, getObjectName(),
                IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS));
        addDeltaSerie(PROCESSING_TIME, getObjectName(),
                IvyJmxConstant.IvyServer.ExternalDatabase.KEY_TRANSACTION_TOTAL_EXE_TIME_IN_MS);
    }

}
