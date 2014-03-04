package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class DeltaValueChartLabelCalcSupport extends ChartLabelCalcSupport {
  private final ObjectName fObjName;
  private final String fAttrKey;

  public DeltaValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    setText(text);
    setValue(Long.MIN_VALUE);
    fObjName = objName;
    fAttrKey = attrKey;
  }

  @Override
  public long calculateValue(QueryResult queryResult) {
    Object value = queryResult.getValue(fObjName, fAttrKey);
    long max = getValue();
    if (value instanceof Number) {
      max = ((Number) value).longValue();
      if (isLastValueValid()) {
        long delta = max - getValue();
        setValue(max);
        max = delta;
      } else {
        setValue(max);
        max = 0;
      }
    }
    return max;
  }

  private boolean isLastValueValid() {
    return getValue() != Long.MIN_VALUE;
  }

}
