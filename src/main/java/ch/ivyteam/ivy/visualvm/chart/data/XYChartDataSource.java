package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.support.AbstractChartLabelCalcSupport;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

public class XYChartDataSource {

  private final String fChartName;
  private final String fXAxisDescription;
  private final String fYAxisDescription;
  private final IDataBeanProvider fDataBeanProvider;
  private final List<SerieDataSource> serieDataSources = new ArrayList<>();
  private final List<AbstractChartLabelCalcSupport> fLabelCalcSupports = new ArrayList<>();

  /**
   * 
   * @param dataBeanProvider provider of chart
   * @param chartName name of chart
   * @param xAxisDescription description of x axis
   * @param yAxisDescription description of y axis
   */
  public XYChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    fDataBeanProvider = dataBeanProvider;
    fChartName = chartName;
    fXAxisDescription = xAxisDescription;
    fYAxisDescription = yAxisDescription;
  }

  public long[] getValues(QueryResult result) {
    long[] values = new long[serieDataSources.size()];
    int pos = 0;
    for (SerieDataSource dataSource : serieDataSources) {
      values[pos++] = dataSource.getValue(result);
    }
    return values;
  }

  public long[] calculateDetailValues(QueryResult result) {
    long[] values = new long[fLabelCalcSupports.size()];
    int pos = 0;
    for (AbstractChartLabelCalcSupport support : fLabelCalcSupports) {
      support.updateValues(result);
      values[pos++] = support.getValue();
    }
    return values;
  }

  public void setLabels(QueryResult queryResult) {
    for (AbstractChartLabelCalcSupport labelSupport : fLabelCalcSupports) {
      labelSupport.updateValues(queryResult);
    }
  }

  public void addFixedSerie(String serie, String description, long fixedValue) {
    SerieDataSource serieDataSource = new AttributeDataSource(serie, 1L, SerieStyle.FILLED, fixedValue);
    serieDataSource.setDescription(description);
    serieDataSources.add(serieDataSource);
  }

  public void addSerie(String serie, String description, SerieStyle style, ObjectName mBeanName,
          String attribute) {
    SerieDataSource serieDataSource = new AttributeDataSource(serie, 1L,
            style, mBeanName, attribute);
    serieDataSource.setDescription(description);
    serieDataSources.add(serieDataSource);
  }

  public void addDeltaSerie(String serie, String description, ObjectName mBeanName, String attribute) {
    addDeltaSerie(serie, description, null, mBeanName, attribute);
  }

  public void addDeltaSerie(String serie, String description, SerieStyle style, ObjectName mBeanName,
          String attribute) {
    SerieDataSource serieDataSource = new DeltaAttributeDataSource(serie, 1L, style, mBeanName, attribute);
    serieDataSource.setDescription(description);
    serieDataSources.add(serieDataSource);
  }

  public void addSerie(SerieDataSource serieDataSource) {
    serieDataSources.add(serieDataSource);
  }

  public void updateQuery(Query query) {
    for (SerieDataSource dataSource : serieDataSources) {
      dataSource.updateQuery(query);
    }
    for (AbstractChartLabelCalcSupport labelSupport : fLabelCalcSupports) {
      labelSupport.updateQuery(query);
    }
  }

  public IDataBeanProvider getDataBeanProvider() {
    return fDataBeanProvider;
  }

  public List<SerieDataSource> getSerieDataSources() {
    return serieDataSources;
  }

  public String getYAxisDescription() {
    return fYAxisDescription;
  }

  public String getXAxisDescription() {
    return fXAxisDescription;
  }

  public String getChartName() {
    return fChartName;
  }

  public void addLabelCalcSupport(AbstractChartLabelCalcSupport labelCalcSupport) {
    getLabelCalcSupports().add(labelCalcSupport);
  }

  public List<AbstractChartLabelCalcSupport> getLabelCalcSupports() {
    return fLabelCalcSupports;
  }

  public void addDeltaMeanSerie(String serie, String description, ObjectName mBeanName,
          String totalValueAttribute, String countAttribute) {
    SerieDataSource totalValueDataSource = new DeltaAttributeDataSource(
            serie, 1L, SerieStyle.LINE, mBeanName, totalValueAttribute);
    SerieDataSource countDataSource = new DeltaAttributeDataSource(serie,
            1L, SerieStyle.LINE, mBeanName, countAttribute);
    addMeanSerie(serie, description, SerieStyle.LINE, totalValueDataSource, countDataSource);
  }

  private void addMeanSerie(String serie, String description, SerieStyle style,
          SerieDataSource totalValueDataSource,
          SerieDataSource countDataSource) {
    MeanDataSource meanDataSource = new MeanDataSource(serie, style,
            totalValueDataSource, countDataSource);
    meanDataSource.setDescription(description);
    serieDataSources.add(meanDataSource);
  }
}
