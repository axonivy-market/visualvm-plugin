package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.MQuery;
import ch.ivyteam.ivy.visualvm.chart.MQueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;

abstract class MSerieDataSource {

  private long fScaleFactor = 1L;
  private final String fSerie;
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

  @Override
  public String toString() {
    return fSerie;
  }

}
