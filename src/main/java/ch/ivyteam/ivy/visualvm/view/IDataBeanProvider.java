package ch.ivyteam.ivy.visualvm.view;

import javax.management.MBeanServerConnection;

public interface IDataBeanProvider {
  public MBeanServerConnection getMBeanServerConnection();

}
