package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.MaxDeltaValueChartLabelCalcSupport;
import static ch.ivyteam.ivy.visualvm.test.datasource.support.AbstractChartLabelCalcSupportTest.ATTR_KEY;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MaxDeltaValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testMaxDeltaValueCalc() {
    MaxDeltaValueChartLabelCalcSupport calcSupport = new MaxDeltaValueChartLabelCalcSupport("text",
            ObjectName.WILDCARD, ATTR_KEY);
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(1L));
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(3L));
    assertEquals(2L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(10L));
    assertEquals(7L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(15L));
    assertEquals(7L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(7L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(7L, calcSupport.getValue());
  }

}
