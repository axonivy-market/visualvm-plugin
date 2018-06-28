package ch.ivyteam.ivy.visualvm.view.common;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableColumnAdjuster {
  private JTable fTable;
  private int fSpacing;
  private boolean fIsColumnHeaderIncluded;
  private boolean fIsColumnDataIncluded;
  private boolean fIsOnlyAdjustLarger;
  private final Map<TableColumn, Integer> columnSizes = new HashMap<>();

  public TableColumnAdjuster(JTable table) {
    this(table, 10);
  }

  public TableColumnAdjuster(JTable table, int spacing) {
    fTable = table;
    fSpacing = spacing;
    fIsColumnHeaderIncluded = true;
    fIsColumnDataIncluded = true;
    fIsOnlyAdjustLarger = true;
  }

  public void adjustColumns() {
    TableColumnModel tcm = fTable.getColumnModel();

    for (int i = 0; i < tcm.getColumnCount(); i++) {
      adjustColumn(i);
    }
  }

  public void adjustColumn(final int column) {
    TableColumn tableColumn = fTable.getColumnModel().getColumn(column);

    if (!tableColumn.getResizable()) {
      return;
    }

    int columnHeaderWidth = getColumnHeaderWidth(column);
    int columnDataWidth = getColumnDataWidth(column);
    int preferredWidth = Math.max(columnHeaderWidth, columnDataWidth);

    updateTableColumn(column, preferredWidth);
  }

  private int getColumnHeaderWidth(int column) {
    if (!fIsColumnHeaderIncluded) {
      return 0;
    }

    TableColumn tableColumn = fTable.getColumnModel().getColumn(column);
    Object value = tableColumn.getHeaderValue();
    TableCellRenderer renderer = tableColumn.getHeaderRenderer();

    if (renderer == null) {
      renderer = fTable.getTableHeader().getDefaultRenderer();
    }

    Component c = renderer.getTableCellRendererComponent(fTable, value, false, false, -1, column);
    return c.getPreferredSize().width;
  }

  private int getColumnDataWidth(int column) {
    if (!fIsColumnDataIncluded) {
      return 0;
    }

    int preferredWidth = 0;
    int maxWidth = fTable.getColumnModel().getColumn(column).getMaxWidth();

    for (int row = 0; row < fTable.getRowCount(); row++) {
      preferredWidth = Math.max(preferredWidth, getCellDataWidth(row, column));

      //  We've exceeded the maximum width, no need to check other rows
      if (preferredWidth >= maxWidth) {
        break;
      }
    }

    return preferredWidth;
  }

  private int getCellDataWidth(int row, int column) {
    //  Inovke the renderer for the cell to calculate the preferred width

    TableCellRenderer cellRenderer = fTable.getCellRenderer(row, column);
    Component c = fTable.prepareRenderer(cellRenderer, row, column);
    int width = c.getPreferredSize().width + fTable.getIntercellSpacing().width;

    return width;
  }

  private void updateTableColumn(int column, int paramWith) {
    int width = paramWith;
    final TableColumn tableColumn = fTable.getColumnModel().getColumn(column);

    if (!tableColumn.getResizable()) {
      return;
    }

    width += fSpacing;

    //  Don't shrink the column width
    if (fIsOnlyAdjustLarger) {
      width = Math.max(width, tableColumn.getPreferredWidth());
    }

    columnSizes.put(tableColumn, Integer.valueOf(tableColumn.getWidth()));
    fTable.getTableHeader().setResizingColumn(tableColumn);
    tableColumn.setWidth(width);
  }

  public void setColumnHeaderIncluded(boolean isColumnHeaderIncluded) {
    fIsColumnHeaderIncluded = isColumnHeaderIncluded;
  }

  public void setColumnDataIncluded(boolean isColumnDataIncluded) {
    fIsColumnDataIncluded = isColumnDataIncluded;
  }

  public void setOnlyAdjustLarger(boolean isOnlyAdjustLarger) {
    fIsOnlyAdjustLarger = isOnlyAdjustLarger;
  }

}
