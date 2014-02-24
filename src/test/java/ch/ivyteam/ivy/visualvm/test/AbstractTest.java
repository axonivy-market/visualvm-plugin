package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.test.data.model.MBeanTestData;
import ch.ivyteam.ivy.visualvm.test.data.model.MBeanTestData.Dataset.Property;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
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

  public static void addTestData(MBeanServerConnection mockConnection, MBeanTestData.Dataset dataset)
          throws InstanceNotFoundException, InstanceNotFoundException,
          InstanceNotFoundException, IOException, ReflectionException, MalformedObjectNameException {
    for (Property property : dataset.getProperty()) {
      List<Attribute> mockAttrs = new ArrayList<>();
      ObjectName objectName = new ObjectName(property.getName());

      String[] attrNames = new String[property.getDataset().getProperty().size()];
      int index = 0;
      for (Property attr : property.getDataset().getProperty()) {
        attrNames[index++] = attr.getName();
        Attribute mockAttr = mock(Attribute.class);
        when(mockAttr.getName()).thenReturn(attr.getName());
        when(mockAttr.getValue()).thenReturn(getConvertedValue(attr));
        mockAttrs.add(mockAttr);
      }
      AttributeList mockAttrList = mock(AttributeList.class);
      when(mockAttrList.asList()).thenReturn(mockAttrs);
      when(mockConnection.getAttributes(objectName, attrNames)).thenReturn(mockAttrList);
      System.out.println("123");
    }
  }

  private static Object getConvertedValue(Property attr) throws RuntimeException {
    if ("java.util.Date".equals(attr.getValue().getType())) {
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      try {
        return format.parse(attr.getValue().getValue());
      } catch (ParseException ex) {
        throw new RuntimeException(ex);
      }
    } else {
      return attr.getValue().getValue();
    }
  }

}
