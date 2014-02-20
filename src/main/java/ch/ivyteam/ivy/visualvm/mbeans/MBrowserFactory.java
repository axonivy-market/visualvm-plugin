/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

import javax.management.MBeanServerConnection;

/**
 *
 * @author rwei
 */
public class MBrowserFactory {
  public static IMBeanBrowser createMBeanBrowser(
          MBeanServerConnection serverConnection) {
    return new MBeanBrowser(serverConnection);
  }

  public static IMAttributeBrowser createMAttributeBrowser(
          MBeanServerConnection serverConnection) {
    return new MAttributeBrowser(serverConnection);
  }

}
