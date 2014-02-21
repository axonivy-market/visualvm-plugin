/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
public class MChartDataSource {

  private final String fChartName;
  private final String fXAxisDescription;
  private final String fYAxisDescription;
  private final List<MSerieDataSource> serieDataSources = new ArrayList<>();

  public MChartDataSource(String chartName, String xAxisDescription, String yAxisDescription) {
    fChartName = chartName;
    fXAxisDescription = xAxisDescription;
    fYAxisDescription = yAxisDescription;
  }

  public long[] getValues(MQueryResult result) {
    long[] values = new long[serieDataSources.size()];
    int pos = 0;
    for (MSerieDataSource dataSource : serieDataSources) {
      values[pos++] = dataSource.getValue(result);
    }
    return values;
  }

  public void addSerie(String serie, SerieStyle style, ObjectName mBeanName, String attribute) {
    MSerieDataSource serieDataSource = new MAttributeDataSource(serie, 1L,
            style, mBeanName, attribute);
    serieDataSources.add(serieDataSource);
  }

  public void addDeltaSerie(String serie, ObjectName mBeanName, String attribute) {
    addDeltaSerie(serie, null, mBeanName, attribute);
  }

  private void addDeltaSerie(String serie, SerieStyle style, ObjectName mBeanName, String attribute) {
    MSerieDataSource serieDataSource = new MDeltaAttributeDataSource(serie, 1L, style, mBeanName, attribute);
    serieDataSources.add(serieDataSource);
  }

  public void configureChart(SimpleXYChartDescriptor chartDescriptor) {
    if (fChartName != null) {
      chartDescriptor.setChartTitle(fChartName);
    }
    if (fXAxisDescription != null) {
      chartDescriptor.setXAxisDescription(fXAxisDescription);
    }
    if (fYAxisDescription != null) {
      chartDescriptor.setYAxisDescription(fYAxisDescription);
    }

    for (MSerieDataSource dataSource : serieDataSources) {
      dataSource.configureSerie(chartDescriptor);
    }
  }

  public void updateQuery(MQuery query) {
    for (MSerieDataSource dataSource : serieDataSources) {
      dataSource.updateQuery(query);
    }
  }

}
