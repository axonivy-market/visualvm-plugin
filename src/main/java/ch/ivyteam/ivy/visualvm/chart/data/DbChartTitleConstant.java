package ch.ivyteam.ivy.visualvm.chart.data;

public final class DbChartTitleConstant {

  public static final String LIMIT_CONNECTION_TITLE = "Limit";
  public static final String MAX_OPEN_CONNECTION_TITLE = "Max open";
  public static final String MAX_USED_CONNECTION_TITLE = "Max used";
  public static final String OPEN_SERIE_TITLE = "Open";
  public static final String USED_SERIE_TITLE = "Used";
  public static final String MIN_SERIE_TITLE = "Min";
  public static final String MEAN_SERIE_TITLE = "Mean";
  public static final String MAX_SERIE_TITLE = "Max";
  public static final String TOTAL_MEAN_TITLE = "Total mean";
  public static final String MAX_OF_MAX_TITLE = "Max of max";
  public static final String MAX_TRANSACTION_TITLE = "Max transactions";
  public static final String MAX_ERROR_TITLE = "Max errors";
  public static final String TRANSACTION_TITLE = "Transactions";
  public static final String ERROR_TITLE = "Errors";
  public static final String CALLS_SERIE_TITLE = "Calls";
  public static final String MAX_CALLS_TITLE = "Max calls";

  public static final String TRANSACTION_ERROR_SERIE_DESC = "The number of system database transactions"
          + " that have finished since the last poll and were erroneous.";
  public static final String TRANSACTION_SERIE_DESC = "The number of system database transactions that have "
          + "finished since the last poll.";
  public static final String OPEN_SERIE_DESC = "The number of open connections to the system database.";
  public static final String USED_SERIE_DESC = "The number of used connections to the system database"
          + " for which at least one statement was executed since the last poll.";
  public static final String MAX_SERIE_DESC = "The maximum processing time of all system database "
          + "transactions that have finished since the last poll.";
  public static final String MEAN_SERIE_DESC = "The mean processing time of all system database "
          + "transactions that have finished since the last poll.";
  public static final String MIN_SERIE_DESC = "The minimum processing time of all system database "
          + "transactions that have finished since the last poll.";

  private DbChartTitleConstant() {
  }

}
