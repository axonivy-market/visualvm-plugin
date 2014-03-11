package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;

public abstract class ChartLabelCalcSupport {
  private String fText;
  private long fValue;

  protected abstract long calculateValue(QueryResult queryResult);

  public String getText() {
    return fText;
  }

  public void setText(String text) {
    fText = text;
  }

  public void updateValues(QueryResult queryResult) {
    setValueInternal(calculateValue(queryResult));
  }

  protected void setValueInternal(long value) {
    fValue = value;
  }

  public long getValue() {
    return fValue;
  }

}
