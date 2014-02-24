package ch.ivyteam.ivy.visualvm.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractTest extends TestCase {
  public static MBeanServerConnection createMockConnection() {
    MBeanServerConnection connection = mock(MBeanServerConnection.class);
    return connection;
  }

  public static void addTestData(MBeanServerConnection mockConnection, ObjectName objName,
          Map<String, Object> attributes)
          throws InstanceNotFoundException, InstanceNotFoundException,
          InstanceNotFoundException, IOException, ReflectionException {
    List<Attribute> mockAttrs = new ArrayList<>();
    String[] attrNames = new String[attributes.entrySet().size()];
    int index = 0;
    for (Entry<String, Object> attr : attributes.entrySet()) {
      attrNames[index++] = attr.getKey();
      Attribute mockAttr = mock(Attribute.class);
      when(mockAttr.getName()).thenReturn(attr.getKey());
      when(mockAttr.getValue()).thenReturn(attr.getValue());
      mockAttrs.add(mockAttr);
    }
    AttributeList mockAttrList = mock(AttributeList.class);
    when(mockAttrList.asList()).thenReturn(mockAttrs);
    when(mockConnection.getAttributes(objName, attrNames)).thenReturn(mockAttrList);
  }

}
