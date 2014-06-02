package ch.ivyteam.ivy.visualvm.test.data.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * The object for test data, create a xml file then unmarshall it to generate.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dataset"})
@XmlRootElement(name = "beanTestData")
public class BeanTestData {

  @XmlElement(required = true)
  private List<Dataset> dataset;

  public List<Dataset> getDataset() {
    if (dataset == null) {
      dataset = new ArrayList<>();
    }
    return dataset;
  }

  public void setDataset(List<Dataset> pDataset) {
    this.dataset = pDataset;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"property"})
  public static class Dataset {
    @XmlElement(required = true)
    private List<Property> property;
    @XmlAttribute
    private String type;

    public Dataset() {
    }

    public List<Property> getProperty() {
      if (property == null) {
        property = new ArrayList<>();
      }
      return property;
    }

    public void setProperty(List<Property> pProperty) {
      this.property = pProperty;
    }

    public String getType() {
      return type;
    }

    public void setType(String pType) {
      this.type = pType;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"name", "value", "dataset"})
    public static class Property {
      @XmlElement(required = true)
      private String name;
      @XmlElement
      private Value value;
      @XmlElement
      private List<Dataset> dataset;

      public String getName() {
        return name;
      }

      public void setName(String pName) {
        this.name = pName;
      }

      public Value getValue() {
        return value;
      }

      public void setValue(Value pValue) {
        this.value = pValue;
      }

      public List<Dataset> getDataset() {
        if (dataset == null) {
          dataset = new ArrayList<>();
        }
        return dataset;
      }

      public void setDataset(List<Dataset> pDataset) {
        this.dataset = pDataset;
      }

      public Property() {

      }

      @XmlAccessorType(XmlAccessType.FIELD)
      public static class Value {
        @XmlValue
        private String value;
        @XmlAttribute
        private String type;

        public String getValue() {
          return value;
        }

        public void setValue(String pValue) {
          this.value = pValue;
        }

        public String getType() {
          return type;
        }

        public void setType(String pType) {
          this.type = pType;
        }

      }
    }
  }

}
