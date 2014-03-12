package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.StaticValueChartLabelCalcSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StaticValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testStaticValueCalc() {
    StaticValueChartLabelCalcSupport calcSupport = new StaticValueChartLabelCalcSupport("text", 100L);
    assertEquals(100L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(1L));
    assertEquals(100L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(3L));
    assertEquals(100L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(10L));
    assertEquals(100L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(100L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(18L));
    assertEquals(100L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(1800L));
    assertEquals(100L, calcSupport.getValue());
  }

}
