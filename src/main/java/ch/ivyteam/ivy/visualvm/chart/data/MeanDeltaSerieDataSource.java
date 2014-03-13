/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import javax.management.ObjectName;

/**
 * 
 * @author lthoang
 */
public class MeanDeltaSerieDataSource extends SerieDataSource {

  private final DeltaAttributeDataSource fMeanDeltaTotalDataSource;
  private final DeltaAttributeDataSource fMeanDeltaNumberDataSource;

  MeanDeltaSerieDataSource(String serie, String description, ObjectName objName, String totalAttrKey,
          String numberAttrKey) {
    super(serie, 1L, SerieStyle.LINE);
    fMeanDeltaTotalDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
            objName, totalAttrKey);
    fMeanDeltaNumberDataSource = new DeltaAttributeDataSource("", 1L, SerieStyle.LINE,
            objName, numberAttrKey);
  }

  @Override
  public void updateQuery(Query query) {
    fMeanDeltaTotalDataSource.updateQuery(query);
    fMeanDeltaNumberDataSource.updateQuery(query);
  }

  @Override
  public long getValue(QueryResult result) {
    long deltaTime = fMeanDeltaTotalDataSource.getValue(result);
    long deltaNumber = fMeanDeltaNumberDataSource.getValue(result);
    return deltaNumber != 0 ? deltaTime / deltaNumber : 0;
  }
}
