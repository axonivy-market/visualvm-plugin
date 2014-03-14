package ch.ivyteam.ivy.visualvm.chart.data.webservice;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.AbstractExternalDbAndWebServiceDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.DeltaAttributeDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.SerieDataSource;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelDivideCalcSupport;
import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.DatabasePersistency;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant.IvyServer.WebService;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import javax.management.ObjectName;

public class WebServiceProcessingTimeChartDataSource extends AbstractExternalDbAndWebServiceDataSource {
  private static final String MIN_SERIE_DESCRIPTION = "The minimum time of all calls that has finished in the"
          + "last poll";
  private static final String MAX_SERIE_DESCRIPTION = "The maximum time of all calls that has finished in the"
          + "last poll";
  public static final String MIN_SERIE_TITLE = "Min";
  public static final String MAX_SERIE_TITLE = "Max";
  public static final String MEAN_SERIE_TITLE = "Mean";
  public static final String MAX_OF_MAX = "Max of max";
  public static final String TOTAL_MEAN = "Total mean";
  private static final String MEAN_SERIE_DESCRIPTION = "The mean time of all calls that has finished since "
          + "the  last poll";

  public WebServiceProcessingTimeChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);

  }

  @Override
  public void init() {
    super.init();
    addLabelCalcSupport(new MaxValueChartLabelCalcSupport(MAX_OF_MAX, getObjectName(),
            WebService.KEY_CALL_MAX_EXE_TIME_DELTA));
    addLabelCalcSupport(new ChartLabelDivideCalcSupport(TOTAL_MEAN, DatabasePersistency.NAME,
            WebService.KEY_CALL_TOTAL_EXE_TIME,
            WebService.KEY_CALLS));

    addSerie(MAX_SERIE_TITLE, MAX_SERIE_DESCRIPTION,
            SerieStyle.LINE, getObjectName(),
            WebService.KEY_CALL_MAX_EXE_TIME_DELTA);
    addSerie(new MeanSerieDataSource(getObjectName()));
    addSerie(MIN_SERIE_TITLE, MIN_SERIE_DESCRIPTION,
            SerieStyle.LINE, getObjectName(),
            WebService.KEY_CALL_MIN_EXE_TIME_DELTA);
  }

  class MeanSerieDataSource extends SerieDataSource {

    private final DeltaAttributeDataSource fDeltaTimeCallsDataSource;
    private final DeltaAttributeDataSource fDeltaNumberCallsDataSource;
    private ObjectName fObjectName;

    MeanSerieDataSource(ObjectName objectName) {
      this(objectName, MEAN_SERIE_TITLE, 1L, SerieStyle.LINE);
      setDescription(MEAN_SERIE_DESCRIPTION);
    }

    MeanSerieDataSource(ObjectName objectName, String serie, long scaleFactor, SerieStyle style) {
      super(serie, scaleFactor, style);
      fObjectName = objectName;
      fDeltaTimeCallsDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
              fObjectName,
              WebService.KEY_CALL_TOTAL_EXE_TIME);
      fDeltaNumberCallsDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
              fObjectName,
              WebService.KEY_CALLS);
    }

    @Override
    public void updateQuery(Query query) {
      fDeltaTimeCallsDataSource.updateQuery(query);
      fDeltaNumberCallsDataSource.updateQuery(query);
    }

    @Override
    public long getValue(QueryResult result) {
      long deltaTime = fDeltaTimeCallsDataSource.getValue(result);
      long deltaNumber = fDeltaNumberCallsDataSource.getValue(result);
      return (deltaNumber != 0) ? (deltaTime / deltaNumber) : 0;
    }

  }
}
