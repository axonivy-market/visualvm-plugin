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

  private long scaleFactor = 1L;
  private String serie;
  private SerieStyle style;

  MSerieDataSource(String serie, long scaleFactor, SerieStyle style) {
    this.serie = serie;
    this.scaleFactor = scaleFactor;
    this.style = style;
    if (this.style == null) {
      this.style = SerieStyle.LINE_FILLED;
    }
  }

  abstract void updateQuery(MQuery query);

  abstract long getValue(MQueryResult result);

  protected long toScaledLong(Object value) {
    if (value instanceof Number) {
      return ((Number) value).longValue() / scaleFactor;
    }
    return 0L;
  }

  void configureSerie(SimpleXYChartDescriptor chartDescriptor) {
    switch (style) {
      case LINE_FILLED:
        chartDescriptor.addLineFillItems(serie);
        break;
      case LINE:
        chartDescriptor.addLineItems(serie);
        break;
      case FILLED:
        chartDescriptor.addFillItems(serie);
        break;
    }
  }

  String getSerie() {
    return serie;
  }

  SerieStyle getStyle() {
    return style;
  }

  public String toString() {
    return serie;
  }

}
