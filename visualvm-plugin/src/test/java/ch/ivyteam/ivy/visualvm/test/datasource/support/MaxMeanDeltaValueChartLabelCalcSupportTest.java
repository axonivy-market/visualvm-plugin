package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.MaxMeanDeltaValueChartLabelCalcSupport;
import static ch.ivyteam.ivy.visualvm.test.datasource.support.AbstractChartLabelCalcSupportTest.ATTR_KEY;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MaxMeanDeltaValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testDeltaValueCalc() {
    MaxMeanDeltaValueChartLabelCalcSupport calcSupport = new MaxMeanDeltaValueChartLabelCalcSupport("text",
            ObjectName.WILDCARD, ATTR_KEY, ATTR_KEY2);
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(100L, 5L));
    //0 : 0 = 0 => max 0
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(150L, 8L));
    //(150-100) : (8-5) = 16.667 => max 16
    assertEquals(16L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(180L, 10L));
    //(180-150) : (10-8) = 15 => max still 16
    assertEquals(16L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(120L, 5L));
    //0 : 0 => max still 16
    assertEquals(16L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(0L, 0L));
    //0 : 0 => max still 16
    assertEquals(16L, calcSupport.getValue());
  }

}
