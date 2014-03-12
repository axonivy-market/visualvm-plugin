package ch.ivyteam.ivy.visualvm.chart.data.externaldb;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.DeltaAttributeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.SerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.ExternalDatabase;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import javax.management.ObjectName;

public class ExternalDbProcessingTimeChartDataSource extends AbstractDataSource {

  public static final String MIN_SERIE_TITLE = "Min";
  public static final String MAX_SERIE_TITLE = "Max";
  public static final String MEAN_SERIE_TITLE = "Mean";
  public static final String TOTAL_MEAN_TITLE = "Total mean";
  public static final String MAX_OF_MAX_TITLE = "Max of max";
  public static final String MEAN_SERIE_DESC = "The mean time of all transactions that has finished in "
          + "last poll";
  public static final String MAX_SERIE_DESC = "The maximum time of all transactions that has finished in "
          + "last poll";
  public static final String MIN_SERIE_DESC = "The minimum time of all transactions that has finished in "
          + "last poll";

  public ExternalDbProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OF_MAX_TITLE,
            getObjectName(), ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(TOTAL_MEAN_TITLE, getObjectName(),
            ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME, ExternalDatabase.KEY_TRANS_NUMBER));

    addSerie(MAX_SERIE_TITLE, MAX_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_TRANS_MAX_EXE_DELTA_TIME);
    addSerie(new MeanSerieDataSource(getObjectName()));
    addSerie(MIN_SERIE_TITLE, MIN_SERIE_DESC, SerieStyle.LINE, getObjectName(),
            ExternalDatabase.KEY_TRANS_MIN_EXE_DELTA_TIME);

  }

  class MeanSerieDataSource extends SerieDataSource {

    private final DeltaAttributeDataSource fDeltaTimeTransDataSource;
    private final DeltaAttributeDataSource fDeltaNumberTransDataSource;
    private ObjectName fObjectName;

    MeanSerieDataSource(ObjectName objectName) {
      this(objectName, MEAN_SERIE_TITLE, 1L, SerieStyle.LINE);
      setDescription(MEAN_SERIE_DESC);
    }

    MeanSerieDataSource(ObjectName objectName, String serie, long scaleFactor, SerieStyle style) {
      super(serie, scaleFactor, style);
      fObjectName = objectName;
      fDeltaTimeTransDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
              fObjectName,
              ExternalDatabase.KEY_TRANS_TOTAL_EXE_TIME);
      fDeltaNumberTransDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
              fObjectName,
              ExternalDatabase.KEY_TRANS_NUMBER);
    }

    @Override
    public void updateQuery(Query query) {
      fDeltaTimeTransDataSource.updateQuery(query);
      fDeltaNumberTransDataSource.updateQuery(query);
    }

    @Override
    public long getValue(QueryResult result) {
      long deltaTime = fDeltaTimeTransDataSource.getValue(result);
      long deltaNumber = fDeltaNumberTransDataSource.getValue(result);
      return (deltaNumber != 0) ? (deltaTime / deltaNumber) : 0;
    }

  }
}
