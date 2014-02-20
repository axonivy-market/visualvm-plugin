/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 *
 * @author rwei
 */
public class MQuery {
  private Map<ObjectName, List<String>> queries = new HashMap<ObjectName, List<String>>();

  void addSubQuery(ObjectName mBeanName, Set<String> attributesToAdd) {
    List<String> attributes = queries.get(mBeanName);
    if (attributes == null) {
      attributes = new ArrayList<String>();
      queries.put(mBeanName, attributes);
    }
    for (String attribute : attributesToAdd) {
      if (!attributes.contains(attribute)) {
        attributes.add(attribute);
      }
    }
  }

  void addSubQuery(ObjectName mBeanName, String attribute) {
    List<String> attributes = queries.get(mBeanName);
    if (attributes == null) {
      attributes = new ArrayList<String>();
      queries.put(mBeanName, attributes);
    }
    if (!attributes.contains(attribute)) {
      attributes.add(attribute);
    }
  }

  public MQueryResult execute(MBeanServerConnection serverConnection) {
    MQueryResult result = new MQueryResult();
    for (Map.Entry<ObjectName, List<String>> entry : queries.entrySet()) {
      List<String> attributes = entry.getValue();
      String[] attribs = new String[attributes.size()];
      attribs = attributes.toArray(attribs);
      ObjectName mBeanName = entry.getKey();
      try {
        AttributeList subResult = serverConnection.getAttributes(
                mBeanName, attribs);
        result.addSubResult(mBeanName, subResult);
      } catch (Exception ex) {
      }
    }
    return result;
  }

}
