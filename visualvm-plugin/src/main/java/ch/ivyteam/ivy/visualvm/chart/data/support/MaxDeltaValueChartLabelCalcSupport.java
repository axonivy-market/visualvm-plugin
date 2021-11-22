package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class MaxDeltaValueChartLabelCalcSupport extends DeltaValueChartLabelCalcSupport {
  private long fMaxDelta = 0;

  public MaxDeltaValueChartLabelCalcSupport(String text, ObjectName objName, String attrKey) {
    super(text, objName, attrKey);
  }

  public MaxDeltaValueChartLabelCalcSupport(String text, ObjectName objName,
          String attrKey, long scaledFactor) {
    this(text, objName, attrKey);
    setScaledFactor(scaledFactor);
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    long nextDelta = super.calculateValue(queryResult);
    long result = fMaxDelta;
    if (nextDelta > result) {
      result = nextDelta;
    }
    return result;
  }

  @Override
  public void updateValues(QueryResult queryResult) {
    if (isLastValueValid()) {
      long nextDelta = calculateValue(queryResult);
      if (nextDelta > fMaxDelta) {
        fMaxDelta = nextDelta;
      }
    } else {
      fMaxDelta = 0;
    }
    super.updateValues(queryResult);
  }

}
