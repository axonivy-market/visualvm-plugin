package ch.ivyteam.ivy.visualvm.test;

import ch.ivyteam.ivy.visualvm.exception.IvyVisualVMRuntimeException;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData.Dataset.Property;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import ch.ivyteam.ivy.visualvm.view.GenericData;
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
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import junit.framework.TestCase;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class AbstractTest extends TestCase {

  private final BeanTestData.Dataset fDataset;

  public AbstractTest(BeanTestData.Dataset dataset) {
    this.fDataset = dataset;
  }

  public static MBeanServerConnection createMockConnection() {
    return mock(MBeanServerConnection.class);
  }

  public static void addTestData(MBeanServerConnection mockConnection, BeanTestData.Dataset dataset) {
    try {
      for (Property property : dataset.getProperty()) {
        List<Attribute> mockAttrs = new ArrayList<>();
        ObjectName objectName = new ObjectName(property.getName());

        for (Property attr : property.getDataset().get(0).getProperty()) {
          Attribute mockAttr = mock(Attribute.class);
          when(mockAttr.getName()).thenReturn(attr.getName());
          if (attr.getValue() != null) {
            when(mockAttr.getValue()).thenReturn(getConvertedValue(attr));
          } else {
            if (attr.getDataset().size() >= 1) {
              if ("javax.management.openmbean.TabularData".equals(attr.getDataset().get(0).getType())) {
                TabularDataSupport tabular = handleTabularData(mockConnection, objectName, attr);
                when(mockAttr.getValue()).thenReturn(tabular);
              } else if ("javax.management.openmbean.CompositeData".equals(attr.getDataset().get(0).getType())) {
                CompositeData[] compositeData = handleCompositeData(mockConnection, objectName, attr);
                when(mockAttr.getValue()).thenReturn(compositeData);
              }
            }
          }
          mockAttrs.add(mockAttr);
        }
        final AttributeList attrList = new AttributeList();
        attrList.addAll(mockAttrs);
        when(mockConnection.getAttributes(eq(objectName), (String[]) anyObject())).then(
                new Answer<AttributeList>() {
                  @Override
                  public AttributeList answer(InvocationOnMock invocation) {
                    String[] attribListString = (String[]) invocation.getArguments()[1];
                    AttributeList result = new AttributeList();
                    for (String attribString : attribListString) {
                      for (Attribute attrib : attrList.asList()) {
                        if (attrib.getName().equals(attribString)) {
                          result.add(attrib);
                        }
                      }
                    }
                    return result;
                  }

                });
      }
    } catch (AttributeNotFoundException | InstanceNotFoundException |
            IOException | ReflectionException | MalformedObjectNameException |
            MBeanException e) {
      throw new IvyVisualVMRuntimeException(e);
    }
  }

  public DataBeanProvider mockDataProvider(MBeanServerConnection connection) {
    DataBeanProvider provider = Mockito.mock(DataBeanProvider.class);
    when(provider.getMBeanServerConnection()).thenReturn(connection);
    when(provider.getGenericData()).thenReturn(new GenericData(connection));
    return provider;
  }

  private static TabularDataSupport handleTabularData(MBeanServerConnection mockConnection,
          ObjectName objectName,
          Property attr) throws ReflectionException, IOException, InstanceNotFoundException, MBeanException,
          AttributeNotFoundException {
    TabularDataSupport tabular = mock(TabularDataSupport.class);
    when(mockConnection.getAttribute(objectName, attr.getName())).thenReturn(tabular);
    for (Property element : attr.getDataset().get(0).getProperty()) {
      CompositeDataSupport compositeData = mock(CompositeDataSupport.class);
      when(tabular.get(new String[]{element.getName()})).thenReturn(compositeData);
      when(compositeData.get("propertyValue")).thenReturn(element.getValue().getValue());
    }
    return tabular;
  }

  private static CompositeData[] handleCompositeData(
          MBeanServerConnection mockConnection, ObjectName objectName, Property attr)
          throws ReflectionException, IOException, InstanceNotFoundException, MBeanException,
          AttributeNotFoundException {
    CompositeData[] mockList = new CompositeData[attr.getDataset().size()];
    int index = 0;
    for (BeanTestData.Dataset comp1 : attr.getDataset()) {
      CompositeData mockElement = mock(CompositeData.class);
      for (Property comp2 : comp1.getProperty()) {
        if (comp2.getValue() != null) {
          when(mockElement.get(comp2.getName())).thenReturn(getConvertedValue(comp2));
        } else {
          CompositeData mockComp3 = mock(CompositeData.class);
          for (Property comp3 : comp2.getDataset().get(0).getProperty()) {
            when(mockComp3.get(comp3.getName())).thenReturn(getConvertedValue(comp3));
          }
          when(mockElement.get(comp2.getName())).thenReturn(mockComp3);
        }
      }
      mockList[index++] = mockElement;
    }
    when(mockConnection.getAttribute(objectName, attr.getName())).thenReturn(mockList);
    return mockList;
  }

  private static Object getConvertedValue(Property attr) {
    if ("java.util.Date".equals(attr.getValue().getType())) {
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      SimpleDateFormat formatLong = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      try {
        if (attr.getValue().getValue().length() > 10) {
          return formatLong.parse(attr.getValue().getValue());
        }
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

  protected BeanTestData.Dataset getDataset() {
    return fDataset;
  }

}
