package ch.ivyteam.ivy.visualvm.model;

import java.util.Comparator;
import java.util.Date;

public interface IExecutionInfo {

  String BACKSLASH = "\\";

  String getApplication();

  String getEnvironment();

  String getConfigName();

  Date getTime();

  long getExecutionTime();

  class TimeComparator implements Comparator<IExecutionInfo> {

    @Override
    public int compare(IExecutionInfo o1, IExecutionInfo o2) {
      return o1.getTime().compareTo(o2.getTime());
    }
  }

  class ExecutionTimeComparator implements Comparator<IExecutionInfo> {

    @Override
    public int compare(IExecutionInfo o1, IExecutionInfo o2) {
      return Long.compare(o1.getExecutionTime(), o2.getExecutionTime());
    }
  }

}
