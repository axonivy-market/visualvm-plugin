package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import javax.management.ObjectName;

class AttributeDataSource extends SerieDataSource {
  private final String fAttribute;
  private final ObjectName fMBeanName;
  private final long fFixedValue;

  AttributeDataSource(String serie, long scaleFactor, SerieStyle serieStyle, ObjectName mBeanName,
          String attribute) {
    super(serie, scaleFactor, serieStyle);
    fAttribute = attribute;
    fMBeanName = mBeanName;
    fFixedValue = -1;
  }

  AttributeDataSource(String serie, long scaleFactor, SerieStyle serieStyle, long fixedValue) {
    super(serie, scaleFactor, serieStyle);
    fAttribute = null;
    fMBeanName = null;
    fFixedValue = fixedValue;
  }

  @Override
  public void updateQuery(Query query) {
    if (fFixedValue == -1) {
      query.addSubQuery(fMBeanName, fAttribute);
    }
  }

  @Override
  public long getValue(QueryResult result) {
    long value;
    if (fFixedValue > -1) {
      value = fFixedValue;
    } else {
      value = toScaledLong(result.getValue(fMBeanName, fAttribute));
    }
    return value;
  }

}
