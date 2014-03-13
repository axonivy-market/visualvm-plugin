package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.MeanDeltaValueChartLabelCalcSupport;
import static ch.ivyteam.ivy.visualvm.test.datasource.support.AbstractChartLabelCalcSupportTest.ATTR_KEY;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MeanDeltaValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testMeanDeltaValueCalc() {
    MeanDeltaValueChartLabelCalcSupport calcSupport = new MeanDeltaValueChartLabelCalcSupport("text",
            ObjectName.WILDCARD, ATTR_KEY);
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(1L));
    //0 : 1 = 0
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(3L));
    //(0+(3-1)) : 2 = 1
    assertEquals(1L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(50L));
    //(0+(3-1)+(50-3)) : 3 = 16.333 => 16
    assertEquals(16L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    //(0+(3-1)+(50-3)+|8-50=0|) : 4 = 12.25 => 12
    assertEquals(12L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    //(0+(3-1)+(50-3)+|8-50=0|+(8-8)) : 5 = 9.8.2 => 9
    assertEquals(9L, calcSupport.getValue());
  }

}
