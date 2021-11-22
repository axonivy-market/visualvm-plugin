package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.MaxValueChartLabelCalcSupport;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MaxValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testMaxValueCalc() {
    MaxValueChartLabelCalcSupport calcSupport = new MaxValueChartLabelCalcSupport("text",
            ObjectName.WILDCARD, ATTR_KEY);
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(1L));
    assertEquals(1L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(3L));
    assertEquals(3L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(10L));
    assertEquals(10L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(10L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(18L));
    assertEquals(18L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(18L));
    assertEquals(18L, calcSupport.getValue());
  }

}
