package ch.ivyteam.ivy.visualvm.test.datasource.support;

import ch.ivyteam.ivy.visualvm.chart.data.support.LatestValueChartLabelCalcSupport;
import static ch.ivyteam.ivy.visualvm.test.datasource.support.AbstractChartLabelCalcSupportTest.ATTR_KEY;
import javax.management.ObjectName;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LatestValueChartLabelCalcSupportTest extends AbstractChartLabelCalcSupportTest {

  @Test
  public void testLatestValueCalc() {
    LatestValueChartLabelCalcSupport calcSupport = new LatestValueChartLabelCalcSupport("text",
            ObjectName.WILDCARD, ATTR_KEY);
    assertEquals(0L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(1L));
    assertEquals(1L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(3L));
    assertEquals(3L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(10L));
    assertEquals(10L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(8L, calcSupport.getValue());
    calcSupport.updateValues(createQueryResult(8L));
    assertEquals(8L, calcSupport.getValue());
  }

}
