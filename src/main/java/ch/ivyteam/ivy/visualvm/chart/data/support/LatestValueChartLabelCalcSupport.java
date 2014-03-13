package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class LatestValueChartLabelCalcSupport extends AbstractChartLabelCalcSupport {
  private final ObjectName fObjName;
  private final String fAttrKey;

  public LatestValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    setText(text);
    fObjName = objName;
    fAttrKey = attrKey;
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    Object value = queryResult.getValue(fObjName, fAttrKey);
    long current = getValue();
    if (value instanceof Number) {
      current = ((Number) value).longValue();
    }
    return current;
  }

}
