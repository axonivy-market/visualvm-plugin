package ch.ivyteam.ivy.visualvm.view;

import javax.management.MBeanServerConnection;

public class DataBeanProvider {
  private final MBeanServerConnection fMBeanServerConnection;
  private final GenericData fCachedData;

  public DataBeanProvider(MBeanServerConnection connection) {
    fMBeanServerConnection = connection;
    fCachedData = new GenericData(fMBeanServerConnection);
  }

  public MBeanServerConnection getMBeanServerConnection() {
    return fMBeanServerConnection;
  }

  public GenericData getGenericData() {
    return fCachedData;
  }

}
