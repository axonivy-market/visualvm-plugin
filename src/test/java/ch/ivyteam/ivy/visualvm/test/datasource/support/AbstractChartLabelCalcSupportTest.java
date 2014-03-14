package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;

class AbstractChartLabelCalcSupportTest {
  public static final String ATTR_KEY = "attrKey";
  public static final String ATTR_KEY2 = "attrKey2";
  public static final String ATTR_KEY_NO_USE = "attrKey-no-use";

  protected QueryResult createQueryResult(long attributeValue) {
    QueryResult queryResult = new QueryResult();
    AttributeList attributeList = new AttributeList();
    attributeList.add(new Attribute(ATTR_KEY, attributeValue));
    attributeList.add(new Attribute(ATTR_KEY_NO_USE, 100));
    queryResult.addSubResult(ObjectName.WILDCARD, attributeList);
    return queryResult;
  }

  protected QueryResult createQueryResult(long value1, long value2) {
    QueryResult queryResult = new QueryResult();
    AttributeList attributeList = new AttributeList();
    attributeList.add(new Attribute(ATTR_KEY, value1));
    attributeList.add(new Attribute(ATTR_KEY2, value2));
    attributeList.add(new Attribute(ATTR_KEY_NO_USE, 100));
    queryResult.addSubResult(ObjectName.WILDCARD, attributeList);
    return queryResult;
  }

  protected QueryResult createEmptyQueryResult() {
    return new QueryResult();
  }

}
