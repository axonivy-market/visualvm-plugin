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
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractTest extends TestCase {
  private final MBeanTestData.Dataset fDataset;

  public AbstractTest(MBeanTestData.Dataset dataset) {
    this.fDataset = dataset;
  }

  public static MBeanServerConnection createMockConnection() {
    MBeanServerConnection connection = mock(MBeanServerConnection.class);
    return connection;
  }

  public static void addTestData(MBeanServerConnection mockConnection, MBeanTestData.Dataset dataset) {
    try {
      for (Property property : dataset.getProperty()) {
        List<Attribute> mockAttrs = new ArrayList<>();
        ObjectName objectName = new ObjectName(property.getName());

        String[] attrNames = new String[property.getDataset().getProperty().size()];
        int index = 0;
        for (Property attr : property.getDataset().getProperty()) {
          attrNames[index++] = attr.getName();
          Attribute mockAttr = mock(Attribute.class);
          when(mockAttr.getName()).thenReturn(attr.getName());
          if (attr.getValue() != null) {
            when(mockAttr.getValue()).thenReturn(getConvertedValue(attr));
          } else {
            TabularDataSupport tabular = mock(TabularDataSupport.class);
            when(mockConnection.getAttribute(objectName, attr.getName())).thenReturn(tabular);
            for (Property element : attr.getDataset().getProperty()) {
              CompositeDataSupport compositeData = mock(CompositeDataSupport.class);
              when(tabular.get(new String[]{element.getName()})).thenReturn(compositeData);
              when(compositeData.get("propertyValue")).thenReturn(element.getValue().getValue());
            }
          }
          mockAttrs.add(mockAttr);
        }
        AttributeList mockAttrList = mock(AttributeList.class);
        when(mockAttrList.asList()).thenReturn(mockAttrs);
        when(mockConnection.getAttributes(objectName, attrNames)).thenReturn(mockAttrList);
      }
    } catch (AttributeNotFoundException | InstanceNotFoundException |
            IOException | ReflectionException | MalformedObjectNameException |
            MBeanException e) {
      throw new RuntimeException();
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
    } else if ("java.lang.Long".equals(attr.getValue().getType())) {
      return Long.parseLong(attr.getValue().getValue());
    } else {
      return attr.getValue().getValue();
    }
  }

  protected MBeanTestData.Dataset getDataset() {
    return fDataset;
  }

}
