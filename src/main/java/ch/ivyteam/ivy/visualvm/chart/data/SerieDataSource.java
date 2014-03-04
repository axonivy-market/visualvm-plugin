package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;

abstract class SerieDataSource {

  private long fScaleFactor = 1L;
  private final String fSerie;
  private SerieStyle fStyle;
  private String fLabel;

  SerieDataSource(String serie, long scaleFactor, SerieStyle style) {
    fSerie = serie;
    fLabel = serie;
    fScaleFactor = scaleFactor;
    fStyle = style;
    if (fStyle == null) {
      fStyle = SerieStyle.LINE_FILLED;
    }
  }

  public void setLabel(String label) {
    fLabel = label;
  }

  public String getLabel() {
    return fLabel;
  }

  abstract void updateQuery(Query query);

  abstract long getValue(QueryResult result);

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