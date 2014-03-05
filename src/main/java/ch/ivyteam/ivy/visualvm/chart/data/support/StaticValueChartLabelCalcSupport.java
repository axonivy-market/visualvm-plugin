package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;

public class StaticValueChartLabelCalcSupport extends ChartLabelCalcSupport {

  public StaticValueChartLabelCalcSupport(String text, long value) {
    setText(text);
    setValue(value);
  }

  @Override
  public long calculateValue(QueryResult queryResult) {
    return getValue();
  }

}
