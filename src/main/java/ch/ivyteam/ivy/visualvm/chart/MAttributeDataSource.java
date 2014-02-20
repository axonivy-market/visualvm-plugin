/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
class MAttributeDataSource extends MSerieDataSource {
  private String fAttribute;
  private ObjectName fMBeanName;

  MAttributeDataSource(String serie, long scaleFactor, SerieStyle serieStyle, ObjectName mBeanName,
          String attribute) {
    super(serie, scaleFactor, serieStyle);
    this.fAttribute = attribute;
    this.fMBeanName = mBeanName;
  }

  @Override
  void updateQuery(MQuery query) {
    query.addSubQuery(fMBeanName, fAttribute);
  }

  @Override
  long getValue(MQueryResult result) {
    return toScaledLong(result.getValue(fMBeanName, fAttribute));
  }

}
