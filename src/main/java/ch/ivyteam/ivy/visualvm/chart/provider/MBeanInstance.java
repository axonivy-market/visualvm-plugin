/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.provider;

import java.util.List;
import javax.management.ObjectInstance;

/**
 *
 * @author rwei
 */
class MBeanInstance implements IObjectInstance {
  private final MBeanType fType;
  private final ObjectInstance fMBean;
  private final String fDisplayName;

  public MBeanInstance(MBeanType type, ObjectInstance mBean,
          String displayName) {
    fType = type;
    fMBean = mBean;
    fDisplayName = displayName;
  }

  @Override
  public String getName() {
    return fDisplayName;
  }

  @Override
  public List<IChart> getCharts() {
    return fType.getCharts(this);
  }

}
