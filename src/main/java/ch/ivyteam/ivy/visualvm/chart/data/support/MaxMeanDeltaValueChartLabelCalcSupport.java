package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class MaxMeanDeltaValueChartLabelCalcSupport extends MeanDeltaValueChartLabelCalcSupport {
  public MaxMeanDeltaValueChartLabelCalcSupport(String text, ObjectName objName,
          String totalAttrKey, String counterAttrKey) {
    super(text, objName, totalAttrKey, counterAttrKey);
  }

  public MaxMeanDeltaValueChartLabelCalcSupport(String text, ObjectName objName,
          String totalAttrKey, String counterAttrKey, long scaledFactor) {
    this(text, objName, totalAttrKey, counterAttrKey);
    setScaledFactor(scaledFactor);
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    long current = super.calculateValue(queryResult);
    long currentMax = getValue();
    if (current > currentMax) {
      currentMax = current;
    }
    return currentMax;
  }

}
