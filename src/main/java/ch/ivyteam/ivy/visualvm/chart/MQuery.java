package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.exception.ClosedIvyServerConnectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class MQuery {
  private final Map<ObjectName, List<String>> queries = new HashMap<>();
  private static final Logger LOGGER = Logger.getLogger(MQuery.class.getName());

  public void addSubQuery(ObjectName mBeanName, Set<String> attributesToAdd) {
    List<String> attributes = queries.get(mBeanName);
    if (attributes == null) {
      attributes = new ArrayList<>();
      queries.put(mBeanName, attributes);
    }
    for (String attribute : attributesToAdd) {
      if (!attributes.contains(attribute)) {
        attributes.add(attribute);
      }
    }
  }

  public void addSubQuery(ObjectName mBeanName, String attribute) {
    List<String> attributes = queries.get(mBeanName);
    if (attributes == null) {
      attributes = new ArrayList<>();
      queries.put(mBeanName, attributes);
    }
    if (!attributes.contains(attribute)) {
      attributes.add(attribute);
    }
  }

  public MQueryResult execute(MBeanServerConnection serverConnection) {
    MQueryResult result = new MQueryResult();
    if (serverConnection == null) {
      throw new ClosedIvyServerConnectionException("");
    }
    for (Map.Entry<ObjectName, List<String>> entry : queries.entrySet()) {
      List<String> attributes = entry.getValue();
      String[] attribs = new String[attributes.size()];
      attribs = attributes.toArray(attribs);
      ObjectName mBeanName = entry.getKey();
      try {
        AttributeList subResult = serverConnection.getAttributes(mBeanName, attribs);
        result.addSubResult(mBeanName, subResult);
      } catch (InstanceNotFoundException | ReflectionException | IOException ex) {
        LOGGER.warning(ex.getMessage());
      }
    }
    return result;
  }

}
