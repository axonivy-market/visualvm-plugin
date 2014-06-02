package ch.ivyteam.ivy.visualvm.view.html;

import java.util.ArrayList;
import java.util.List;

public class TableGenerator {
  private static final String TAG_HTML_START = "<html>";
  private static final String TAG_HTML_END = "</html>";
  private static final String TAG_TABLE_START
          = "<table cellpadding='0' cellspacing='0' style='font-family:tahoma;font-size:11'>";
  private static final String TAG_TABLE_END = "</table>";
  private static final String TAG_ROW_START = "<tr>";
  private static final String TAG_ROW_END = "</tr>";
  private static final String TAG_CELL_START = "<td>";
  private static final String TAG_CELL2_START
          = "<td style='padding-left:3px;text-align:right'>";
  private static final String TAG_CELL_END = "</td>";

  private final List<Row> fRows;

  public TableGenerator() {
    fRows = new ArrayList<>();
  }

  public void addRow(String key, String label, String value, String unit, String tooltip) {
    fRows.add(new Row(key, label, value, unit, tooltip));
  }

  public void clear() {
    fRows.clear();
  }

  public int rowCount() {
    return fRows.size();
  }

  public void updateValue(String key, String value) {
    for (Row row : fRows) {
      if (row.fKey.equals(key)) {
        row.fValue = value;
      }
    }
  }

  public void updateValues(long[] values) {
    for (int i = 0; (i < fRows.size()) && (i < values.length); i++) {
      fRows.get(i).fValue = Long.toString(values[i]);
    }
  }

  public String getTooltip(int row) {
    if (row >= fRows.size()) {
      return null;
    }
    return fRows.get(row).fTooltip;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(TAG_HTML_START);
    sb.append(TAG_TABLE_START);
    for (Row row : fRows) {
      sb.append(TAG_ROW_START);
      addCell(sb, row.fLabel);
      addCell2(sb, row.fValue, row.fUnit);
      sb.append(TAG_ROW_END);
    }
    sb.append(TAG_TABLE_END);
    sb.append(TAG_HTML_END);
    return sb.toString();
  }

  private void addCell(StringBuilder sb, String content) {
    sb.append(TAG_CELL_START);
    if (content != null) {
      sb.append(content);
      sb.append(":");
    }
    sb.append(TAG_CELL_END);
  }

  private void addCell2(StringBuilder sb, String content, String unit) {
    sb.append(TAG_CELL2_START);
    if (content != null) {
      sb.append(content);
      if (unit != null) {
        sb.append("&nbsp;");
        sb.append(unit);
      }
    }
    sb.append(TAG_CELL_END);
  }

  private final class Row {
    private String fKey;
    private String fLabel;
    private String fValue;
    private String fUnit;
    private String fTooltip;

    private Row(String key, String label, String value, String unit, String tooltip) {
      fKey = key;
      fLabel = label;
      fValue = value;
      fUnit = unit;
      fTooltip = tooltip;
    }

  }
}
