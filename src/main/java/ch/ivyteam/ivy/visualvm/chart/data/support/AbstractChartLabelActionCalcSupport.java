/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.ivyteam.ivy.visualvm.chart.data.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.ObjectName;

abstract class AbstractChartLabelActionCalcSupport extends ChartLabelCalcSupport {

  private final ObjectName fObjectName;
  private final String fFirstAttribute;
  private final String fSecondAttribute;

  public AbstractChartLabelActionCalcSupport(String serie, ObjectName mBeanName, String firstAttribute,
          String secondAttribute) {
    fObjectName = mBeanName;
    fFirstAttribute = firstAttribute;
    fSecondAttribute = secondAttribute;
    setText(serie);
  }

  protected long getFirstValue(QueryResult queryResult) {
    return get(queryResult, fFirstAttribute);
  }

  protected long getSecondValue(QueryResult queryResult) {
    return get(queryResult, fSecondAttribute);
  }

  private long get(QueryResult queryResult, String attr) {
    Object attrValue = queryResult.getValue(fObjectName, attr);
    long result = 0;
    if (attrValue instanceof Number) {
      result = ((Number) attrValue).longValue();
    }
    return result;
  }
}