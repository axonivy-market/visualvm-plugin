package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

public class GaugeDataSource {
  private final DataBeanProvider fDataBeanProvider;
  private final String fAttribute;
  private final ObjectName fMBeanName;
  private static final double SCALE_FACTOR = 1D;
  private final List<Double> fThresholds = new ArrayList();

  public GaugeDataSource(DataBeanProvider dataBeanProvider, ObjectName mBeanName, String attribute) {
    fDataBeanProvider = dataBeanProvider;
    fMBeanName = mBeanName;
    fAttribute = attribute;
  }

  public List<Double> getThresholds() {
    return fThresholds;
  }

  public DataBeanProvider getDataBeanProvider() {
    return fDataBeanProvider;
  }

  public void updateQuery(Query query) {
    query.addSubQuery(fMBeanName, fAttribute);
  }

  public double getValue(QueryResult result) {
    return toScaledDouble(result.getValue(fMBeanName, fAttribute));
  }

  private double toScaledDouble(Object value) {
    if (value instanceof Number) {
      return ((Number) value).doubleValue() / SCALE_FACTOR;
    }
    return 0D;
  }

}
