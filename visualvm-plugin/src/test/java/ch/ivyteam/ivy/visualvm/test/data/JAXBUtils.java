package ch.ivyteam.ivy.visualvm.test.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ch.ivyteam.ivy.visualvm.exception.IvyVisualVMRuntimeException;
import ch.ivyteam.ivy.visualvm.test.data.model.BeanTestData;

public final class JAXBUtils {

  private JAXBUtils() {
  }

  public static BeanTestData unmarshall(File inputFile) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(BeanTestData.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    unmarshaller.setSchema(getSchema());
    return (BeanTestData) unmarshaller.unmarshal(inputFile);
  }

  public static File marshall(BeanTestData beanTestData, String filePath) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(BeanTestData.class);
    Marshaller marshaller = jc.createMarshaller();
    marshaller.setSchema(getSchema());
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    File file = new File(filePath);
    marshaller.marshal(beanTestData, file);
    return file;
  }

  public static Schema getSchema() {
    SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try (InputStream inputStream = JAXBUtils.class.getClassLoader().getResourceAsStream(
            "ch/ivyteam/ivy/visualvm/test/data/model/BeanTestData.xsd");) {
      Source soureSchema = new SAXSource(new InputSource(inputStream));
      return schemaFactory.newSchema(soureSchema);
    } catch (SAXException | IOException e) {
      throw new IvyVisualVMRuntimeException(e);
    }
  }

}
