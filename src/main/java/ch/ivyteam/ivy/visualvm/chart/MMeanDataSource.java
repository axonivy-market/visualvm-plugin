/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

/**
 *
 * @author rwei
 */
class MMeanDataSource extends MSerieDataSource {
  private MSerieDataSource fTotalValueDataSource;
  private MSerieDataSource fCountValueDataSource;

  public MMeanDataSource(String serie, SerieStyle serieStyle, MSerieDataSource totalValueDataSource,
          MSerieDataSource countValueDataSource) {
    super(serie, 1L, serieStyle);
    fTotalValueDataSource = totalValueDataSource;
    fCountValueDataSource = countValueDataSource;

  }

  @Override
  void updateQuery(MQuery query) {
    fTotalValueDataSource.updateQuery(query);
    fCountValueDataSource.updateQuery(query);
  }

  @Override
  long getValue(MQueryResult result) {
    long totalValue = fTotalValueDataSource.getValue(result);
    long count = fCountValueDataSource.getValue(result);
    if (count == 0L) {
      return 0L;
    }
    return totalValue / count;
  }

}
