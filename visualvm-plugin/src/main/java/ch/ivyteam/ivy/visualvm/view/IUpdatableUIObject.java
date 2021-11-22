package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;

public interface IUpdatableUIObject {
  void updateValues(QueryResult result);

  void updateQuery(Query query);

}
