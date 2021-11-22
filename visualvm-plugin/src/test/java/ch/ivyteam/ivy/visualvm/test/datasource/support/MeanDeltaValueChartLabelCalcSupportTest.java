package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.MeanDeltaValueChartLabelCalcSupport;
import static ch.ivyteam.ivy.visualvm.test.datasource.support.AbstractChartLabelCalcSupportTest.ATTR_KEY;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MeanDeltaValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testMeanDeltaValueCalc() {
    MeanDeltaValueChartLabelCalcSupport calcSupport = new MeanDeltaValueChartLabelCalcSupport(
            "text", ObjectName.WILDCARD, ATTR_KEY, ATTR_KEY2);
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(100L, 5L));
    //0 : 0 = 0
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(150L, 8L));
    //(150-100) : (8-5) = 16.667
    assertEquals(16L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(180L, 10L));
    //(180-150) : (10-8) = 15
    assertEquals(15L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(120L, 5L));
    //0 : 0 => current
    assertEquals(15L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(0L, 0L));
    //0 : 0 => current
    assertEquals(15L, calcSupport.getValue());
  }

}
