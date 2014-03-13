package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class StaticValueChartLabelCalcSupport extends AbstractChartLabelCalcSupport {

  private final ObjectName fObjName;
  private final String fAttrKey;

  public StaticValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    setText(text);
    setValueInternal(0);
    fObjName = objName;
    fAttrKey = attrKey;
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    long result = getValue();
    if (getValue() == 0) {
      Object objectValue = queryResult.getValue(fObjName, fAttrKey);
      if (objectValue instanceof Number) {
        result = ((Number) objectValue).longValue();
      }
    }
    return result;
  }

  @Override
  public void updateQuery(Query query) {
    query.addSubQuery(fObjName, fAttrKey);
  }
}
