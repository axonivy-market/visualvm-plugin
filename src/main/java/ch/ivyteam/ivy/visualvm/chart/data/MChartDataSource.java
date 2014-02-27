package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.MQuery;
import ch.ivyteam.ivy.visualvm.chart.MQueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

public class MChartDataSource {

  private final String fChartName;
  private final String fXAxisDescription;
  private final String fYAxisDescription;
  private final IDataBeanProvider fDataBeanProvider;
  private final List<MSerieDataSource> serieDataSources = new ArrayList<>();

  public MChartDataSource(IDataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription, String yAxisDescription) {
    fDataBeanProvider = dataBeanProvider;
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

  public void addFixedSerie(String serie, long fixedValue) {
    MSerieDataSource serieDataSource = new MAttributeDataSource(serie, 1L, SerieStyle.FILLED, fixedValue);
    serieDataSources.add(serieDataSource);
  }

  public void addSerie(String serie, SerieStyle style, ObjectName mBeanName, String attribute) {
    MSerieDataSource serieDataSource = new MAttributeDataSource(serie, 1L,
            style, mBeanName, attribute);
    serieDataSources.add(serieDataSource);
  }

  public void addSerie(String serie, String label, SerieStyle style, ObjectName mBeanName, String attribute) {
    MSerieDataSource serieDataSource = new MAttributeDataSource(serie, 1L,
            style, mBeanName, attribute);
    serieDataSource.setLabel(label);
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

    String[] details = new String[serieDataSources.size()];
    int index = 0;
    for (MSerieDataSource dataSource : serieDataSources) {
      dataSource.configureSerie(chartDescriptor);
      details[index++] = dataSource.getLabel();
    }
    chartDescriptor.setDetailsItems(details);
  }

  public void updateQuery(MQuery query) {
    for (MSerieDataSource dataSource : serieDataSources) {
      dataSource.updateQuery(query);
    }
  }

  public IDataBeanProvider getDataBeanProvider() {
    return fDataBeanProvider;
  }

  public List<MSerieDataSource> getSerieDataSources() {
    return serieDataSources;
  }

}
