package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class DeltaValueChartLabelCalcSupport extends ChartLabelCalcSupport {
  private final ObjectName fObjName;
  private final String fAttrKey;
  private long fLastValue;

  public DeltaValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    setText(text);
    fLastValue = Long.MIN_VALUE;
    fObjName = objName;
    fAttrKey = attrKey;
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    Object value = queryResult.getValue(fObjName, fAttrKey);
    long result = fLastValue;
    if (value instanceof Number) {
      result = ((Number) value).longValue();
      if (isLastValueValid()) {
        result = result - fLastValue;
      } else {
        result = 0;
      }
    }
    return result;
  }

  protected boolean isLastValueValid() {
    return fLastValue != Long.MIN_VALUE;
  }

  @Override
  public void updateValues(QueryResult queryResult) {
    super.updateValues(queryResult);
    Object value = queryResult.getValue(fObjName, fAttrKey);
    if (value instanceof Number) {
      fLastValue = ((Number) value).longValue();
    }
  }

}
