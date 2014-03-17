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
    long latestValue = super.getValue(result);
    long deltaValue = toDeltaValue(latestValue);
    storeLatestValue(latestValue);
    return toScaledLong(deltaValue);
  }

  private long toDeltaValue(long paramVal) {
    long val = paramVal;
    if (isLastValueValid()) {
      long delta = val - lastValue;
      val = (delta < 0) ? 0 : delta;
    } else {
      val = 0;
    }
    return val;
  }

  private boolean isLastValueValid() {
    return lastValue != Long.MIN_VALUE;
  }

  private void storeLatestValue(long value) {
    lastValue = value;
  }

}
