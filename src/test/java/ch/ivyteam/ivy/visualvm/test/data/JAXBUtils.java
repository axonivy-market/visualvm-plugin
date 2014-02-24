package ch.ivyteam.ivy.visualvm.test.data;

import ch.ivyteam.ivy.visualvm.test.data.model.MBeanTestData;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBUtils {
  public static MBeanTestData unmarshall(File inputFile) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(MBeanTestData.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
//    unmarshaller.setSchema();
    return (MBeanTestData) unmarshaller.unmarshal(inputFile);
  }

  public static File marshall(MBeanTestData mBeanTestData, String filePath) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(MBeanTestData.class);
    Marshaller marshaller = jc.createMarshaller();
//      m.setSchema();
//      m.setProperty(, );
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    File file = new File(filePath);
    marshaller.marshal(mBeanTestData, file);
    return file;
  }

}
