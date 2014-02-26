package ch.ivyteam.ivy.visualvm.view.html;

import java.util.ArrayList;
import java.util.List;

public class TableGenerator {
  private static final String TAG_TABLE_START = "<table>";
  private static final String TAG_TABLE_END = "</table>";
  private static final String TAG_ROW_START = "<tr>";
  private static final String TAG_ROW_END = "</tr>";
  private static final String TAG_CELL_START = "<td>";
  private static final String TAG_CELL_BOLD_START = "<td style='font-weight:bold'>";
  private static final String TAG_CELL_END = "</td>";

  private final List<Row> fRows;

  public TableGenerator() {
    fRows = new ArrayList<>();
  }

  public void addRow(String label, String value) {
    fRows.add(new Row(label, value));
  }

  public void clear() {
    fRows.clear();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(TAG_TABLE_START);
    for (Row row : fRows) {
      sb.append(TAG_ROW_START);
      addCellBold(sb, row.fLabel);
      addCell(sb, row.fValue);
      sb.append(TAG_ROW_END);
    }
    sb.append(TAG_TABLE_END);
    return sb.toString();
  }

  private void addCell(StringBuilder sb, String content) {
    sb.append(TAG_CELL_START);
    addContentCell(content, sb);
  }

  private void addCellBold(StringBuilder sb, String content) {
    sb.append(TAG_CELL_BOLD_START);
    addContentCell(content, sb);
  }

  private void addContentCell(String content, StringBuilder sb) {
    if (content != null) {
      sb.append(content);
    }
    sb.append(TAG_CELL_END);
  }

  private class Row {
    private String fLabel;
    private String fValue;

    private Row(String label, String value) {
      fLabel = label;
      fValue = value;
    }

    public String getLabel() {
      return fLabel;
    }

    public void setLabel(String label) {
      this.fLabel = label;
    }

    public String getValue() {
      return fValue;
    }

    public void setValue(String value) {
      this.fValue = value;
    }

  }
}
