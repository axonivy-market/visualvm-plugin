/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;

/**
 *
 * @author rwei
 */
abstract class MSerieDataSource {

  private long fScaleFactor = 1L;
  private String fSerie;
  private SerieStyle fStyle;

  MSerieDataSource(String serie, long scaleFactor, SerieStyle style) {
    fSerie = serie;
    fScaleFactor = scaleFactor;
    fStyle = style;
    if (fStyle == null) {
      fStyle = SerieStyle.LINE_FILLED;
    }
  }

  abstract void updateQuery(MQuery query);

  abstract long getValue(MQueryResult result);

  protected long toScaledLong(Object value) {
    if (value instanceof Number) {
      return ((Number) value).longValue() / fScaleFactor;
    }
    return 0L;
  }

  void configureSerie(SimpleXYChartDescriptor chartDescriptor) {
    switch (fStyle) {
      case LINE_FILLED:
        chartDescriptor.addLineFillItems(fSerie);
        break;
      case LINE:
        chartDescriptor.addLineItems(fSerie);
        break;
      case FILLED:
        chartDescriptor.addFillItems(fSerie);
        break;
    }
  }

  String getSerie() {
    return fSerie;
  }

  SerieStyle getStyle() {
    return fStyle;
  }

  public String toString() {
    return fSerie;
  }

}
