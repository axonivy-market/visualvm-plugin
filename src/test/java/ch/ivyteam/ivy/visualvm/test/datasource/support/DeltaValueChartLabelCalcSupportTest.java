package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.data.support.DeltaValueChartLabelCalcSupport;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DeltaValueChartLabelCalcSupportTest {
  public static final String ATTR_KEY = "attrKey";

  @Test
  public void testDeltaCalc() {
    DeltaValueChartLabelCalcSupport calcSupport = new DeltaValueChartLabelCalcSupport("text",
            ObjectName.WILDCARD, ATTR_KEY);

    calcSupport.updateValues(createQueryResult(1L));
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(3L));
    assertEquals(2L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(10L));
    assertEquals(7L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(2L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(0L, calcSupport.getValue());
  }

  private QueryResult createQueryResult(long attributeValue) {
    QueryResult queryResult = new QueryResult();
    AttributeList attributeList = new AttributeList();
    attributeList.add(new Attribute(ATTR_KEY, attributeValue));
    queryResult.addSubResult(ObjectName.WILDCARD, attributeList);
    return queryResult;
  }

}
