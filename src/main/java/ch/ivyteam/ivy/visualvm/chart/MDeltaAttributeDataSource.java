/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
public class MDeltaAttributeDataSource extends MAttributeDataSource {
  private long lastValue = Long.MIN_VALUE;

  public MDeltaAttributeDataSource(String serie, long scaleFactor,
          SerieStyle serieStyle, ObjectName mBeanName, String attribute) {
    super(serie, scaleFactor, serieStyle, mBeanName, attribute);
  }

  @Override
  long getValue(MQueryResult result) {
    return toDeltaValue(super.getValue(result));
  }

  private long toDeltaValue(long val) {
    if (isLastValueValid()) {
      long delta = val - lastValue;
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
