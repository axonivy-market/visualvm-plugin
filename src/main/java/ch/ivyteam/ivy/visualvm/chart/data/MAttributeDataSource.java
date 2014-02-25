/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.MQuery;
import ch.ivyteam.ivy.visualvm.chart.MQueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
class MAttributeDataSource extends MSerieDataSource {
  private final String fAttribute;
  private final ObjectName fMBeanName;
  private final long fFixedValue;

  MAttributeDataSource(String serie, long scaleFactor, SerieStyle serieStyle, ObjectName mBeanName,
          String attribute) {
    super(serie, scaleFactor, serieStyle);
    fAttribute = attribute;
    fMBeanName = mBeanName;
    fFixedValue = -1;
  }

  MAttributeDataSource(String serie, long scaleFactor, SerieStyle serieStyle, long fixedValue) {
    super(serie, scaleFactor, serieStyle);
    fAttribute = null;
    fMBeanName = null;
    fFixedValue = fixedValue;
  }

  @Override
  void updateQuery(MQuery query) {
    if (fFixedValue == -1) {
      query.addSubQuery(fMBeanName, fAttribute);
    }
  }

  @Override
  long getValue(MQueryResult result) {
    long value;
    if (fFixedValue > -1) {
      value = fFixedValue;
    } else {
      value = toScaledLong(result.getValue(fMBeanName, fAttribute));
    }
    return value;
  }

}
