/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.MUtil;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
public class MChartDataSource {

  private String fChartName;
  private String fXAxisDescription;
  private String fYAxisDescription;
  private List<MSerieDataSource> serieDataSources = new ArrayList<MSerieDataSource>();
  private int serieCount = 0;

  public MChartDataSource(String chartName, String xAxisDescription,
          String yAxisDescription) {
    fChartName = chartName;
    fXAxisDescription = xAxisDescription;
    fYAxisDescription = yAxisDescription;
  }

  long[] getValues(MQueryResult result) {
    long[] values = new long[serieDataSources.size()];
    int pos = 0;
    for (MSerieDataSource dataSource : serieDataSources) {
      values[pos++] = dataSource.getValue(result);
    }
    return values;
  }

  public void addSerie(String serie, String mBeanName, String attribute) {
    addSerie(serie, MUtil.createObjectName(mBeanName), attribute);
  }

  public void addSerie(String serie, SerieStyle style, String mBeanName,
          String attribute) {
    addSerie(serie, style, MUtil.createObjectName(mBeanName), attribute);
  }

  public void addSerie(String serie, ObjectName mBeanName, String attribute) {
    addSerie(serie, null, mBeanName, attribute);
  }

  public void addSerie(String serie, SerieStyle style, ObjectName mBeanName,
          String attribute) {
    MSerieDataSource serieDataSource = new MAttributeDataSource(serie, 1L,
            style, mBeanName, attribute);
    serieDataSources.add(serieDataSource);
  }

  public void addDeltaSerie(String serie, String mBeanName, String attribute) {
    addDeltaSerie(serie, MUtil.createObjectName(mBeanName), attribute);
  }

  public void addDeltaSerie(String serie, SerieStyle style, String mBeanName,
          String attribute) {
    addDeltaSerie(serie, style, MUtil.createObjectName(mBeanName),
            attribute);
  }

  public void addDeltaSerie(String serie, ObjectName mBeanName,
          String attribute) {
    addDeltaSerie(serie, null, mBeanName, attribute);
  }

  public void addDeltaSerie(String serie, SerieStyle style,
          ObjectName mBeanName, String attribute) {
    MSerieDataSource serieDataSource = new MDeltaAttributeDataSource(serie,
            1L, style, mBeanName, attribute);
    serieDataSources.add(serieDataSource);
  }

  public void addMeanSerie(String serie, String mBeanName,
          String totalValueAttribute, String countAttribute) {
    addMeanSerie(serie, (SerieStyle) null, mBeanName, totalValueAttribute,
            countAttribute);
  }

  public void addMeanSerie(String serie, SerieStyle style, String mBeanName,
          String totalValueAttribute, String countAttribute) {
    addMeanSerie(serie, style, MUtil.createObjectName(mBeanName),
            totalValueAttribute, countAttribute);
  }

  public void addMeanSerie(String serie, ObjectName mBeanName,
          String totalValueAttribute, String countAttribute) {
    addMeanSerie(serie, null, mBeanName, totalValueAttribute,
            countAttribute);
  }

  public void addMeanSerie(String serie, SerieStyle style,
          ObjectName mBeanName, String totalValueAttribute,
          String countAttribute) {
    addMeanSerie(serie, style, mBeanName, totalValueAttribute, mBeanName,
            countAttribute);
  }

  public void addMeanSerie(String serie, String totalValueMBeanName,
          String totalValueAttribute, String countMBeanName,
          String countAttribute) {
    addMeanSerie(serie, null, totalValueMBeanName, totalValueAttribute,
            countMBeanName, countAttribute);
  }

  public void addMeanSerie(String serie, SerieStyle style,
          String totalValueMBeanName, String totalValueAttribute,
          String countMBeanName, String countAttribute) {
    addMeanSerie(serie, style, MUtil.createObjectName(totalValueMBeanName),
            totalValueAttribute, MUtil.createObjectName(countMBeanName),
            countAttribute);
  }

  public void addMeanSerie(String serie, ObjectName totalValueMBeanName,
          String totalValueAttribute, ObjectName countMBeanName,
          String countAttribute) {
    addMeanSerie(serie, null, totalValueMBeanName, totalValueAttribute,
            countMBeanName, countAttribute);
  }

  public void addMeanSerie(String serie, SerieStyle style,
          ObjectName totalValueMBeanName, String totalValueAttribute,
          ObjectName countMBeanName, String countAttribute) {
    MSerieDataSource totalValueDataSource = new MAttributeDataSource(serie,
            1L, style, totalValueMBeanName, totalValueAttribute);
    MSerieDataSource countDataSource = new MAttributeDataSource(serie, 1L,
            style, countMBeanName, countAttribute);
    addMeanSerie(serie, style, totalValueDataSource, countDataSource);
  }

  public void addDeltaMeanSerie(String serie, String totalValueMBeanName,
          String totalValueAttribute, String countMBeanName,
          String countAttribute) {
    addDeltaMeanSerie(serie, null, totalValueMBeanName,
            totalValueAttribute, countMBeanName, countAttribute);
  }

  public void addDeltaMeanSerie(String serie, ObjectName totalValueMBeanName,
          String totalValueAttribute, ObjectName countMBeanName,
          String countAttribute) {
    addDeltaMeanSerie(serie, null, totalValueMBeanName,
            totalValueAttribute, countMBeanName, countAttribute);
  }

  public void addDeltaMeanSerie(String serie, SerieStyle style,
          String totalValueMBeanName, String totalValueAttribute,
          String countMBeanName, String countAttribute) {
    addDeltaMeanSerie(serie, style,
            MUtil.createObjectName(totalValueMBeanName),
            totalValueAttribute, MUtil.createObjectName(countMBeanName),
            countAttribute);
  }

  public void addDeltaMeanSerie(String serie, String mBeanName,
          String totalValueAttribute, String countAttribute) {
    addDeltaMeanSerie(serie, (SerieStyle) null, mBeanName,
            totalValueAttribute, countAttribute);
  }

  public void addDeltaMeanSerie(String serie, SerieStyle style,
          String mBeanName, String totalValueAttribute, String countAttribute) {
    addDeltaMeanSerie(serie, style, MUtil.createObjectName(mBeanName),
            totalValueAttribute, countAttribute);
  }

  public void addDeltaMeanSerie(String serie, ObjectName mBeanName,
          String totalValueAttribute, String countAttribute) {
    addDeltaMeanSerie(serie, null, mBeanName, totalValueAttribute,
            countAttribute);
  }

  public void addDeltaMeanSerie(String serie, SerieStyle style,
          ObjectName mBeanName, String totalValueAttribute,
          String countAttribute) {
    addDeltaMeanSerie(serie, style, mBeanName, totalValueAttribute,
            mBeanName, countAttribute);
  }

  public void addDeltaMeanSerie(String serie, SerieStyle style,
          ObjectName totalValueMBeanName, String totalValueAttribute,
          ObjectName countMBeanName, String countAttribute) {
    MSerieDataSource totalValueDataSource = new MDeltaAttributeDataSource(
            serie, 1L, style, totalValueMBeanName, totalValueAttribute);
    MSerieDataSource countDataSource = new MDeltaAttributeDataSource(serie,
            1L, style, countMBeanName, countAttribute);
    addMeanSerie(serie, style, totalValueDataSource, countDataSource);
  }

  private void addMeanSerie(String serie, SerieStyle style,
          MSerieDataSource totalValueDataSource,
          MSerieDataSource countDataSource) {
    MMeanDataSource meanDataSource = new MMeanDataSource(serie, style,
            totalValueDataSource, countDataSource);
    serieDataSources.add(meanDataSource);
  }

  void addSerie(MSerieDataSource serieDataSource) {
    serieDataSources.add(serieDataSource);
  }

  void configureChart(SimpleXYChartDescriptor chartDescriptor) {
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

  void updateQuery(MQuery query) {
    for (MSerieDataSource dataSource : serieDataSources) {
      dataSource.updateQuery(query);
    }
  }

}
