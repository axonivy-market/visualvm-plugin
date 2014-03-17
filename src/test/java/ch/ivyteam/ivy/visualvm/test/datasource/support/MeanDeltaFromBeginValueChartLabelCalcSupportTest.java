package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.MeanTotalDeltaValueChartLabelCalcSupport;
import static ch.ivyteam.ivy.visualvm.test.datasource.support.AbstractChartLabelCalcSupportTest.ATTR_KEY;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MeanDeltaFromBeginValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testDeltaValueCalc() {
    MeanTotalDeltaValueChartLabelCalcSupport calc = new MeanTotalDeltaValueChartLabelCalcSupport(
            "text", ObjectName.WILDCARD, ATTR_KEY, ATTR_KEY2);
    assertEquals(0L, calc.getValue());
    calc.updateValues(createQueryResult(100L, 5L));
    //0 : 0 = 0 => max 0
    assertEquals(0L, calc.getValue());
    calc.updateValues(createQueryResult(150L, 8L));
    //(150-100) : (8-5) = 16.667
    assertEquals(16L, calc.getValue());
    calc.updateValues(createQueryResult(280L, 10L));
    //(280-100) : (10-5) = 36
    assertEquals(36L, calc.getValue());
    calc.updateValues(createQueryResult(380L, 13L));
    //(380-100) : (13-5) = 35
    assertEquals(35L, calc.getValue());
    calc.updateValues(createQueryResult(120L, 5L));
    //0 : 0 => mean still 35
    assertEquals(35L, calc.getValue());
    calc.updateValues(createQueryResult(0L, 0L));
    //0 : 0 => mean still 35
    assertEquals(35L, calc.getValue());
  }

}
