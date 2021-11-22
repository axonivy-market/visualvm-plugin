package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;

public abstract class SerieDataSource {

  private long fScaleFactor = 1L;
  private final String fSerie;
  private String fDescription;
  private SerieStyle fStyle;

  public SerieDataSource(String serie, long scaleFactor, SerieStyle style) {
    fSerie = serie;
    fScaleFactor = scaleFactor;
    fStyle = style;
    if (fStyle == null) {
      fStyle = SerieStyle.LINE_FILLED;
    }
  }

  public abstract void updateQuery(Query query);

  public abstract long getValue(QueryResult result);

  protected long toScaledLong(Object value) {
    if (value instanceof Number) {
      return ((Number) value).longValue() / fScaleFactor;
    }
    return -1L;
  }

  public void configureSerie(SimpleXYChartDescriptor chartDescriptor) {
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

  public String getSerie() {
    return fSerie;
  }

  public SerieStyle getStyle() {
    return fStyle;
  }

  @Override
  public String toString() {
    return fSerie;
  }

  public String getDescription() {
    return fDescription;
  }

  public void setDescription(String description) {
    this.fDescription = description;
  }

}
