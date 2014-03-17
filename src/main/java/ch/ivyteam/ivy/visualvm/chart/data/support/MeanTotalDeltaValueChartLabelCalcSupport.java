package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class MeanTotalDeltaValueChartLabelCalcSupport extends AbstractChartLabelActionCalcSupport {
  private long fFirstTotal;
  private long fFirstCount;

  public MeanTotalDeltaValueChartLabelCalcSupport(String serie, ObjectName mBeanName,
          String firstAttribute, String secondAttribute, long scaledFactor) {
    this(serie, mBeanName, firstAttribute, secondAttribute);
    setScaledFactor(scaledFactor);
  }

  public MeanTotalDeltaValueChartLabelCalcSupport(String serie, ObjectName mBeanName,
          String firstAttribute, String secondAttribute) {
    super(serie, mBeanName, firstAttribute, secondAttribute);
    fFirstTotal = Long.MIN_VALUE;
    fFirstCount = Long.MIN_VALUE;
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    long result = getValue();
    if (fFirstTotal != Long.MIN_VALUE) {
      long currentDeltaTotal = ensurePositive(getFirstValue(queryResult) / getScaledFactor() - fFirstTotal);
      long currentDeltaCount = ensurePositive(getSecondValue(queryResult) - fFirstCount);
      if (currentDeltaCount != 0) {
        result = currentDeltaTotal / currentDeltaCount;
      }
    }
    return result;
  }

  @Override
  public void updateValues(QueryResult queryResult) {
    super.updateValues(queryResult);
    if (fFirstTotal == Long.MIN_VALUE) {
      fFirstTotal = getFirstValue(queryResult) / getScaledFactor();
      fFirstCount = getSecondValue(queryResult);
    }
  }

}
