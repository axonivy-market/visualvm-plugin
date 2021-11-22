package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class MaxValueChartLabelCalcSupport extends AbstractChartLabelCalcSupport {
  private final ObjectName fObjName;
  private final String fAttrKey;

  public MaxValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey, long scaledFactor) {
    this(text, objName, attrKey);
    setScaledFactor(scaledFactor);
  }

  public MaxValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    setText(text);
    fObjName = objName;
    fAttrKey = attrKey;
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    Object value = queryResult.getValue(fObjName, fAttrKey);
    long max = getValue();
    if (value instanceof Number) {
      long longValue = ((Number) value).longValue() / getScaledFactor();
      if (longValue > getValue()) {
        max = longValue;
      }
    }
    return max;
  }

}
