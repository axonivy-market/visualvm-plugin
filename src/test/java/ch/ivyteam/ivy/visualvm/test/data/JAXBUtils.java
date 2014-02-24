package ch.ivyteam.ivy.visualvm.test.data;

import ch.ivyteam.ivy.visualvm.test.data.model.MBeanTestData;
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

public class JAXBUtils {
  public static MBeanTestData unmarshall(File inputFile) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(MBeanTestData.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    unmarshaller.setSchema(getSchema());
    return (MBeanTestData) unmarshaller.unmarshal(inputFile);
  }

  public static File marshall(MBeanTestData mBeanTestData, String filePath) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(MBeanTestData.class);
    Marshaller marshaller = jc.createMarshaller();
    marshaller.setSchema(getSchema());
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    File file = new File(filePath);
    marshaller.marshal(mBeanTestData, file);
    return file;
  }

  public static Schema getSchema() {
    SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try (InputStream inputStream = JAXBUtils.class.getClassLoader().getResourceAsStream(
            "\\ch\\ivyteam\\ivy\\visualvm\\test\\data\\model\\MBeanTestData.xsd");) {
      Source soureSchema = new SAXSource(new InputSource(inputStream));
      return schemaFactory.newSchema(soureSchema);
    } catch (SAXException | IOException e) {
      throw new RuntimeException(e);
    }
  }

}
