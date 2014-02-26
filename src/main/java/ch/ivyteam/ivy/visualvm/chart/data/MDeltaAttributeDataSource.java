package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.MQueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import javax.management.ObjectName;

public class MDeltaAttributeDataSource extends MAttributeDataSource {
  private long lastValue = Long.MIN_VALUE;

  public MDeltaAttributeDataSource(String serie, long scaleFactor, SerieStyle serieStyle,
          ObjectName mBeanName, String attribute) {
    super(serie, scaleFactor, serieStyle, mBeanName, attribute);
  }

  @Override
  long getValue(MQueryResult result) {
    return toDeltaValue(super.getValue(result));
  }

  private long toDeltaValue(long paramVal) {
    long val = paramVal;
    if (isLastValueValid()) {
      long delta = val - lastValue;
      lastValue = val;
      val = delta;
    } else {
      lastValue = val;
      val = paramVal;
    }
    return val;
  }

  private boolean isLastValueValid() {
    return lastValue != Long.MIN_VALUE;
  }

}
