/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

public class ChartLabelPlusCalcSupport extends AbstractChartLabelActionCalcSupport {

  public ChartLabelPlusCalcSupport(String serie, ObjectName mBeanName, String firstAttribute,
          String secondAttribute) {
    super(serie, mBeanName, firstAttribute, secondAttribute);
  }

  @Override
  protected long calculateValue(QueryResult queryResult) {
    return getFirstValue(queryResult) + getSecondValue(queryResult);
  }

}
