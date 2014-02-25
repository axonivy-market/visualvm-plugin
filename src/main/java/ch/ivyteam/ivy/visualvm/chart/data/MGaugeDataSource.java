/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.MQuery;
import ch.ivyteam.ivy.visualvm.chart.MQueryResult;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import eu.hansolo.steelseries.tools.Section;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

/**
 *
 * @author thtam
 */
public class MGaugeDataSource {
  private List<Section> fSections = new ArrayList<>();
  private final IDataBeanProvider fDataBeanProvider;
  private final String fAttribute;
  private final ObjectName fMBeanName;
  private final double fScaleFactor = 1D;

  public MGaugeDataSource(IDataBeanProvider dataBeanProvider, List<Section> sections,
          ObjectName mBeanName, String attribute) {
    fDataBeanProvider = dataBeanProvider;
    fSections = sections;
    fMBeanName = mBeanName;
    fAttribute = attribute;
  }

  public List<Section> getSections() {
    return fSections;
  }

  public IDataBeanProvider getDataBeanProvider() {
    return fDataBeanProvider;
  }

  public void updateQuery(MQuery query) {
    query.addSubQuery(fMBeanName, fAttribute);
  }

  public double getValue(MQueryResult result) {
    return toScaledDouble(result.getValue(fMBeanName, fAttribute));
  }

  private double toScaledDouble(Object value) {
    if (value instanceof Number) {
      return ((Number) value).doubleValue() / fScaleFactor;
    }
    return 0D;
  }

}
