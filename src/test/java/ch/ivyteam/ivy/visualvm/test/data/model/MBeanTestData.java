package ch.ivyteam.ivy.visualvm.test.data.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The object for test data, create a xml file then unmarshall it to generate.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dataset"})
@XmlRootElement(name = "mBeanTestData")
public class MBeanTestData {

  @XmlElement(required = true)
  protected List<Dataset> dataset;

  public List<Dataset> getDataset() {
    if (dataset == null) {
      dataset = new ArrayList<>();
    }
    return dataset;
  }

  public void setDataset(List<Dataset> dataset) {
    this.dataset = dataset;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"property"})
  public static class Dataset {
    @XmlElement(required = true)
    protected List<Property> property;

    public List<Property> getProperty() {
      if (property == null) {
        property = new ArrayList<>();
      }
      return property;
    }

    public void setProperty(List<Property> property) {
      this.property = property;
    }

    public Dataset() {
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"name", "value", "dataset"})
    public static class Property {
      @XmlElement(required = true)
      protected String name;
      @XmlElement
      protected String value;
      @XmlElement
      protected Dataset dataset;

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public String getValue() {
        return value;
      }

      public void setValue(String value) {
        this.value = value;
      }

      public Dataset getDataset() {
        return dataset;
      }

      public void setDataset(Dataset dataset) {
        this.dataset = dataset;
      }

      public Property() {

      }

    }
  }

}
