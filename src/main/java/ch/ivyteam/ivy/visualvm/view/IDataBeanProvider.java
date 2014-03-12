package ch.ivyteam.ivy.visualvm.view;

import javax.management.MBeanServerConnection;

public interface IDataBeanProvider {
  MBeanServerConnection getMBeanServerConnection();

  CachedData getCachedData();

}
