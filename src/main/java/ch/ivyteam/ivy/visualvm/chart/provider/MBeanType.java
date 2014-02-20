/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
public class MBeanType implements IObjectType {
  private ObjectName instanceFilter;

  private String displayName;
  private List<String> chartNames;

  private MBeanServerConnection connection;

  @Override
  public String getName() {
    return displayName;
  }

  @Override
  public List<IObjectInstance> getInstances() throws IOException {
    Set<ObjectInstance> mBeans = connection.queryMBeans(instanceFilter, null);
    List<IObjectInstance> instances = new ArrayList<>(mBeans.size());
    for (ObjectInstance mBean : mBeans) {
      instances
              .add(new MBeanInstance(this, mBean, toInstanceName(mBean)));
    }
    return instances;
  }

  List<IChart> getCharts(MBeanInstance instance) {
    List<IChart> charts = new ArrayList<>();
    for (String name : chartNames) {
      charts.add(new MChart(instance, name));
    }
    return charts;
  }

  protected String toInstanceName(ObjectInstance mBean) {
    return mBean.getObjectName().toString();
  }

}
