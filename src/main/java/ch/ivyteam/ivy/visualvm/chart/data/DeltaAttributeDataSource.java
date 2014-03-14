package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import javax.management.ObjectName;

public class DeltaAttributeDataSource extends AttributeDataSource {
  private long lastValue = Long.MIN_VALUE;

  public DeltaAttributeDataSource(String serie, long scaleFactor, SerieStyle serieStyle,
          ObjectName mBeanName, String attribute) {
    super(serie, scaleFactor, serieStyle, mBeanName, attribute);
  }

  @Override
  public long getValue(QueryResult result) {
    return toDeltaValue(super.getValue(result));
  }

  private long toDeltaValue(long paramVal) {
    long val = paramVal;
    if (isLastValueValid()) {
      long delta = val - lastValue;
      delta = (delta < 0) ? 0 : delta;
      lastValue = val;
      val = delta;
    } else {
      lastValue = val;
      val = 0;
    }
    return val;
  }

  private boolean isLastValueValid() {
    return lastValue != Long.MIN_VALUE;
  }

}
