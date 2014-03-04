package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import eu.hansolo.steelseries.tools.Section;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

public class GaugeDataSource {
  private final List<Section> fSections = new ArrayList<>();
  private final IDataBeanProvider fDataBeanProvider;
  private final String fAttribute;
  private final ObjectName fMBeanName;
  private static final double SCALE_FACTOR = 1D;

  public GaugeDataSource(IDataBeanProvider dataBeanProvider, ObjectName mBeanName, String attribute) {
    fDataBeanProvider = dataBeanProvider;
    fMBeanName = mBeanName;
    fAttribute = attribute;
  }

  public List<Section> getSections() {
    return fSections;
  }

  public IDataBeanProvider getDataBeanProvider() {
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
