package ch.ivyteam.ivy.visualvm.view;

import javax.management.MBeanServerConnection;

public class DataBeanProvider {
  private final MBeanServerConnection fMBeanServerConnection;
  private final GenericData fGenericData;

  public DataBeanProvider(MBeanServerConnection connection) {
    fMBeanServerConnection = connection;
    fGenericData = new GenericData(fMBeanServerConnection);
  }

  public MBeanServerConnection getMBeanServerConnection() {
    return fMBeanServerConnection;
  }

  public GenericData getGenericData() {
    return fGenericData;
  }

}
