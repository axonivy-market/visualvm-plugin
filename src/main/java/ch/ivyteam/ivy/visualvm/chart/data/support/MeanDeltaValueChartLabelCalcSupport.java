package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class MeanDeltaValueChartLabelCalcSupport extends AbstractChartLabelCalcSupport {
  private DeltaValueChartLabelCalcSupport fDeltaOfTotal;
  private DeltaValueChartLabelCalcSupport fDeltaOfCounter;

  public MeanDeltaValueChartLabelCalcSupport(String text, ObjectName objName,
          String totalAttrKey, String counterAttrKey) {
    setText(text);
    fDeltaOfTotal = new DeltaValueChartLabelCalcSupport("", objName, totalAttrKey);
    fDeltaOfCounter = new DeltaValueChartLabelCalcSupport("", objName, counterAttrKey);
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    long total = fDeltaOfTotal.calculateValue(queryResult);
    long count = fDeltaOfCounter.calculateValue(queryResult);
    long result = getValue();
    if (count != 0) {
      result = total / count;
    }
    return result;
  }

  @Override
  public void updateQuery(Query query) {
    fDeltaOfTotal.updateQuery(query);
    fDeltaOfCounter.updateQuery(query);
  }

  @Override
  public void updateValues(QueryResult queryResult) {
    super.updateValues(queryResult);
    fDeltaOfTotal.updateValues(queryResult);
    fDeltaOfCounter.updateValues(queryResult);
  }

}
