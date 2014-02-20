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
  private final MBeanType type;
  private final ObjectInstance mBean;
  private final String displayName;

  public MBeanInstance(MBeanType type, ObjectInstance mBean,
          String displayName) {
    this.type = type;
    this.mBean = mBean;
    this.displayName = displayName;
  }

  @Override
  public String getName() {
    return displayName;
  }

  @Override
  public List<IChart> getCharts() {
    return type.getCharts(this);
  }

}
