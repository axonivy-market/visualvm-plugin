package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;

public abstract class AbstractChartLabelCalcSupport {
  private String fText;
  private long fValue;
  private long fScaledFactor = 1L;

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

  public void updateQuery(Query query) {
  }

  public static long ensurePositive(long number) {
    long positive = number;
    if (number < 0L) {
      positive = 0L;
    }
    return positive;
  }

  public long getScaledFactor() {
    return fScaledFactor;
  }

  public void setScaledFactor(long scaledFactor) {
    fScaledFactor = scaledFactor;
  }

}
