/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm;

import java.io.IOException;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
public final class MUtil {

  private MUtil() {
  }

  public static ObjectName createObjectName(String name) {
    try {
      return new ObjectName(name);
    } catch (MalformedObjectNameException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public static Set<ObjectName> queryNames(MBeanServerConnection serverConnection,
          String filter) {
    try {
      return serverConnection.queryNames(new ObjectName(filter), null);
    } catch (IOException | MalformedObjectNameException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

}
