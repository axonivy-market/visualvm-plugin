package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;

public abstract class ChartLabelCalcSupport {
  private String fText;
  private long fValue;

  public abstract long calculateValue(QueryResult queryResult);

  public String getText() {
    return fText;
  }

  public void setText(String text) {
    this.fText = text;
  }

  public long getValue() {
    return fValue;
  }

  public void setValue(long value) {
    this.fValue = value;
  }

}
