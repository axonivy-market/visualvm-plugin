package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.chart.data.support.ChartLabelCalcSupport;
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
  private final List<ChartLabelCalcSupport> fLabelCalcSupports = new ArrayList<>();

  /**
   *
   * @param dataBeanProvider provider of chart
   * @param chartName        name of chart
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
    for (ChartLabelCalcSupport support : fLabelCalcSupports) {
      values[pos++] = support.calculateValue(result);
    }
    return values;
  }

  public void setLabels(long[] labels) {
    int index = 0;
    for (ChartLabelCalcSupport labelSupport : fLabelCalcSupports) {
      if (index >= labels.length) {
        break;
      }
      labelSupport.setValue(labels[index++]);
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

  private void addDeltaSerie(String serie, String description, SerieStyle style, ObjectName mBeanName,
          String attribute) {
    SerieDataSource serieDataSource = new DeltaAttributeDataSource(serie, 1L, style, mBeanName, attribute);
    serieDataSource.setDescription(description);
    serieDataSources.add(serieDataSource);
  }

  public void updateQuery(Query query) {
    for (SerieDataSource dataSource : serieDataSources) {
      dataSource.updateQuery(query);
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

  public void addLabelCalcSupport(ChartLabelCalcSupport labelCalcSupport) {
    getLabelCalcSupports().add(labelCalcSupport);
  }

  public List<ChartLabelCalcSupport> getLabelCalcSupports() {
    return fLabelCalcSupports;
  }

}
