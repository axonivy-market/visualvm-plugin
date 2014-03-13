package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class MeanDeltaValueChartLabelCalcSupport extends AbstractChartLabelCalcSupport {
  private final ObjectName fObjName;
  private final String fAttrKey;
  private long fLastValue;
  private long fTotal;
  private long fCount;

  public MeanDeltaValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    setText(text);
    fObjName = objName;
    fAttrKey = attrKey;
    fLastValue = Long.MIN_VALUE;
    fTotal = 0;
    fCount = 0;
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    Object value = queryResult.getValue(fObjName, fAttrKey);
    long result = getValue();
    if (value instanceof Number) {
      if (isLastValueValid()) {
        long currentValue = ((Number) value).longValue();
        long currentTotal = fTotal + Math.abs(currentValue - fLastValue);
        long currentCount = fCount + 1;
        result = currentTotal / currentCount;
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
      long nextValue = ((Number) value).longValue();
      if (isLastValueValid()) {
        fTotal = fTotal + Math.abs(nextValue - fLastValue);
      }
      fLastValue = nextValue;
      fCount++;
    }
  }

}
