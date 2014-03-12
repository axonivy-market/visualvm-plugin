package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;

class AbstractChartLabelCalcSupportTest {
  public static final String ATTR_KEY = "attrKey";

  protected QueryResult createQueryResult(long attributeValue) {
    QueryResult queryResult = new QueryResult();
    AttributeList attributeList = new AttributeList();
    attributeList.add(new Attribute(ATTR_KEY, attributeValue));
    attributeList.add(new Attribute("attrKey-no-use", 100));
    queryResult.addSubResult(ObjectName.WILDCARD, attributeList);
    return queryResult;
  }

}
