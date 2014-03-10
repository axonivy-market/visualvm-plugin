package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class DeltaValueChartLabelCalcSupport extends ChartLabelCalcSupport {
  private final ObjectName fObjName;
  private final String fAttrKey;

  public DeltaValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    setText(text);
    super.setValue(Long.MIN_VALUE);
    fObjName = objName;
    fAttrKey = attrKey;
  }

  @Override
  public long calculateValue(QueryResult queryResult) {
    Object value = queryResult.getValue(fObjName, fAttrKey);
    long result = getValue();
    if (value instanceof Number) {
      result = ((Number) value).longValue();
      if (isLastValueValid()) {
        result = result - getValue();
      } else {
        result = 0;
      }
    }
    return result;
  }

  private boolean isLastValueValid() {
    return getValue() != Long.MIN_VALUE;
  }

  @Override
  public void setValue(long value) {
    if (isLastValueValid()) {
      super.setValue(getValue() + value);
    } else {
      super.setValue(value);
    }
  }

}
