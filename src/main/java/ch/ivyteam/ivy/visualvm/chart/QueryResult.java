package ch.ivyteam.ivy.visualvm.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;

public class QueryResult {
  private final Map<ObjectName, List<Attribute>> results = new HashMap<>();

  public void addSubResult(ObjectName mBeanName, AttributeList attributes) {
    results.put(mBeanName, attributes.asList());
  }

  public Object getValue(ObjectName mBeanName, String attributeName) {
    List<Attribute> attributes = getAttributes(mBeanName);
    if (attributes == null) {
      return null;
    }
    for (Attribute attribute : attributes) {
      if (attribute.getName().equals(attributeName)) {
        return attribute.getValue();
      }
    }
    return null;
  }

  List<Attribute> getAttributes(ObjectName mBeanName) {
    return results.get(mBeanName);
  }

}
