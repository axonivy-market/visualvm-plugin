package ch.ivyteam.ivy.visualvm.test.util;

import ch.ivyteam.ivy.visualvm.test.data.JAXBUtils;
import ch.ivyteam.ivy.visualvm.test.data.model.MBeanTestData;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.openide.util.Utilities;

public final class TestUtil {
  private TestUtil() {
  }

  public static Iterable<Object[]> createTestData(String filePathForInput, Object[]   ... expectedResults)
          throws JAXBException,
          URISyntaxException {
    MBeanTestData testData = JAXBUtils.unmarshall(
            Utilities.toFile(
                    TestUtil.class.getResource(filePathForInput).toURI())
    );

    List<Object[]> testSuite = new ArrayList<>();
    int index = 0;
    for (MBeanTestData.Dataset dataset : testData.getDataset()) {
      if (index < expectedResults.length) {
        Object[] results = expectedResults[index];
        Object[] record = new Object[results.length + 1];
        record[0] = dataset;
        int recIndex = 1;
        for (Object rs : results) {
          record[recIndex++] = rs;
        }
        testSuite.add(record);
        index++;
      } else {
        testSuite.add(new Object[]{dataset});
      }
    }
    return testSuite;
  }

}
