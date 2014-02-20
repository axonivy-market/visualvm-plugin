package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.MQuery;
import ch.ivyteam.ivy.visualvm.chart.MQueryResult;

public interface IUpdatableUIObject {
  void updateValues(MQueryResult result);

  void updateQuery(MQuery query);

}
