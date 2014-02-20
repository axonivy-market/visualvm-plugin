/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.DefaultListModel;

/**
 *
 * @author rwei
 */
class MAttributeListModel extends DefaultListModel {
  private List<String> NUMBER_TYPES = Arrays.asList(Long.class.getName(), Long.TYPE.getName(),
          Integer.class.getName(), Integer.TYPE.getName(), Short.class.getName(), Short.TYPE.getName(),
          Byte.class.getName(), Byte.TYPE.getName(), Float.class.getName(), Float.TYPE.getName(),
          Double.class.getName(), Double.TYPE.getName());

  MAttributeListModel(MBeanServerConnection mBeanServerConnection, String mBeanName) {
    MBeanInfo mBeanInfo;
    try {
      mBeanInfo = mBeanServerConnection.getMBeanInfo(new ObjectName(mBeanName));

      for (MBeanAttributeInfo attribute : mBeanInfo.getAttributes()) {
        if (isNumberType(attribute)) {
          addElement(attribute.getName());
        }
      }
    } catch (Exception ex) {
    }
  }

  private boolean isNumberType(MBeanAttributeInfo attribute) {
    return NUMBER_TYPES.contains(attribute.getType());
  }

  int[] getIndexes(String[] attributes) {
    List<Integer> indexes = new ArrayList<>();
    for (String attribute : attributes) {
      int index = indexOf(attribute);
      if (index >= 0) {
        indexes.add(index);
      }
    }
    int[] result = new int[indexes.size()];
    int pos = 0;
    for (Integer index : indexes) {
      result[pos++] = index;
    }
    return result;
  }

}
