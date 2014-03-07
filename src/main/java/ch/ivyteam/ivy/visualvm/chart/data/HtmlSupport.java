package ch.ivyteam.ivy.visualvm.chart.data;

public class HtmlSupport {
  private static final String TAG_BODY_START = "<html><body style='font-family:tahoma;font-size:11'>";
  private static final String TAG_BODY_END = "</body></html>";
  private static final String TAG_TABLE_START = "<table>";
  private static final String TAG_TABLE_END = "</table>";
  private static final String TAG_ROW_START = "<tr>";
  private static final String TAG_ROW_END = "</tr>";
  private static final String TAG_CELL_STATIC_WIDTH = "<td width='40'>";
  private static final String TAG_CELL_BOLD_START = "<td style='font-weight:bold'>";
  private static final String TAG_CELL_END = "</td>";
  private static final String CHAR_COLON = ":";

  public static String toOneRowHtmlTable(String[] labels, String[] value) {
    StringBuilder sb = new StringBuilder(TAG_BODY_START);
    sb.append(TAG_TABLE_START);
    sb.append(TAG_ROW_START);
    for (int i = 0; i < labels.length; i++) {
      sb.append(TAG_CELL_BOLD_START);
      sb.append(labels[i]);
      sb.append(CHAR_COLON);
      sb.append(TAG_CELL_END);
      sb.append(TAG_CELL_STATIC_WIDTH);
      if (i < value.length) {
        sb.append(value[i]);
      }
      sb.append(TAG_CELL_END);
    }
    sb.append(TAG_ROW_END);
    sb.append(TAG_TABLE_END);
    sb.append(TAG_BODY_END);
    return sb.toString();
  }

  public static String toOneColumnHtmlTable(String[] labels, String[] value) {
    StringBuilder sb = new StringBuilder(TAG_BODY_START);
    sb.append(TAG_TABLE_START);
    for (int i = 0; i < labels.length; i++) {
      sb.append(TAG_ROW_START);
      sb.append(TAG_CELL_BOLD_START);
      sb.append(labels[i]);
      sb.append(CHAR_COLON);
      sb.append(TAG_CELL_END);
      sb.append(TAG_CELL_STATIC_WIDTH);
      if (i < value.length) {
        sb.append(value[i]);
      }
      sb.append(TAG_CELL_END);
      sb.append(TAG_ROW_END);
    }
    sb.append(TAG_TABLE_END);
    sb.append(TAG_BODY_END);
    return sb.toString();
  }

}
