package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.application.Application;
import javax.management.MBeanServerConnection;

public class DataBeanProvider implements IDataBeanProvider {
  private final MBeanServerConnection fMBeanServerConnection;
  private final CachedData fCachedData;

  public DataBeanProvider(Application ivyApplication) {
    fMBeanServerConnection = DataUtils.getMBeanServerConnection(ivyApplication);
    fCachedData = new CachedData(fMBeanServerConnection);
  }

  @Override
  public MBeanServerConnection getMBeanServerConnection() {
    return fMBeanServerConnection;
  }

  @Override
  public CachedData getCachedData() {
    return fCachedData;
  }

}
