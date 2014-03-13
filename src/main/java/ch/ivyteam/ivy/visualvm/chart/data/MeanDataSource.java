/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;

class MeanDataSource extends SerieDataSource {
  private final SerieDataSource fTotalValueDataSource;
  private final SerieDataSource fCountValueDataSource;

  public MeanDataSource(String serie, SerieStyle serieStyle,
          SerieDataSource totalValueDataSource,
          SerieDataSource countValueDataSource) {
    super(serie, 1L, serieStyle);
    fTotalValueDataSource = totalValueDataSource;
    fCountValueDataSource = countValueDataSource;

  }

  @Override
  public void updateQuery(Query query) {
    fTotalValueDataSource.updateQuery(query);
    fCountValueDataSource.updateQuery(query);
  }

  @Override
  public long getValue(QueryResult result) {
    long totalValue = fTotalValueDataSource.getValue(result);
    long count = fCountValueDataSource.getValue(result);
    if (count == 0L) {
      return 0L;
    }
    return totalValue / count;
  }
}