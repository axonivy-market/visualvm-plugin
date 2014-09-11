package ch.ivyteam.ivy.visualvm.view.externaldb;

import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;

public abstract class AbstractExternalDbQueriesPanel extends JPanel {
  private static final Color TABLE_VERTICAL_GRID_COLOR = new Color(214, 223, 247);
  private static final Color TABLE_SELECTION_BACKGROUND_COLOR = new Color(193, 210, 238);
  private static final int TABLE_ROW_HEIGHT = 18;
  private boolean fIsLoaded = false;
  private final ExternalDbView fExternalDbView;
  private final DateTableCellRenderer fDateCellRenderer = new DateTableCellRenderer();
  private final NumberTableCellRenderer fNumberCellRenderer = new NumberTableCellRenderer();
  private final MultiLineMergeTableCellRenderer fmultiLineCellRenderer = new MultiLineMergeTableCellRenderer();
  
  private final List<Integer> fColumnsWidths = new ArrayList<>();
  private List<? extends RowSorter.SortKey> fSortKeys;
  private volatile boolean isRefreshing = false;

  public AbstractExternalDbQueriesPanel(ExternalDbView externalDbView) {
    fExternalDbView = externalDbView;
  }

  public boolean isLoaded() {
    return fIsLoaded;
  }

  protected void setLoaded(boolean isLoaded) {
    this.fIsLoaded = isLoaded;
  }

  protected ExternalDbView getExternalDbView() {
    return fExternalDbView;
  }

  protected DateTableCellRenderer getDateCellRenderer() {
    return fDateCellRenderer;
  }

  protected NumberTableCellRenderer getNumberCellRenderer() {
    return fNumberCellRenderer;
  }

  protected void initTableCellRenderer() {
    JTable queriesTable = getQueriesTable();
    EvenOddCellRenderer renderer = new EvenOddCellRenderer();
    queriesTable.setDefaultRenderer(Object.class, renderer);
    queriesTable.setShowGrid(false);
    queriesTable.setIntercellSpacing(new Dimension(0, 0));
    queriesTable.setGridColor(TABLE_VERTICAL_GRID_COLOR);
    queriesTable.setSelectionBackground(TABLE_SELECTION_BACKGROUND_COLOR);
    queriesTable.setSelectionForeground(queriesTable.getForeground());
    queriesTable.setRowHeight(TABLE_ROW_HEIGHT);
    queriesTable.repaint();
  }

  protected List<RowSorter.SortKey> createDefaultSortKey(int columnIndex, SortOrder order) {
    List<RowSorter.SortKey> sortKeys = new ArrayList();
    RowSorter.SortKey sortKey = new RowSorter.SortKey(columnIndex, order);
    sortKeys.add(sortKey);
    return sortKeys;
  }

  public void refresh(List<SQLInfo> sqlInfoList) {
    synchronized (this) {
      if (isRefreshing) {
        return;//prevent other threads from calling this method when there is a running thread.
      }
      isRefreshing = true;
    }
    storeQueriesTableConfig(getQueriesTable());
    refreshQueriesTable(sqlInfoList);
    restoreQueriesTableConfig();
    clearDetailsArea();
    adjustColumns();
    setLoaded(true);
    isRefreshing = false;
  }

  protected void adjustColumns() {
    TableColumnAdjuster adjuster = new TableColumnAdjuster(getQueriesTable());
    adjuster.adjustColumns();
  }

  protected abstract List<SQLInfo> getSQLInfoList();

  protected abstract JTable getQueriesTable();

  protected abstract void refreshQueriesTable(List<SQLInfo> sqlInfoList);

  protected abstract void clearDetailsArea();

  protected void storeQueriesTableConfig(JTable table) {
    fSortKeys = table.getRowSorter().getSortKeys();
    fColumnsWidths.clear();
    Enumeration<TableColumn> columns = getQueriesTable().getColumnModel().getColumns();
    while (columns.hasMoreElements()) {
      TableColumn column = columns.nextElement();
      int colWidth = column.getWidth();
      fColumnsWidths.add(colWidth);
    }
  }

  protected void restoreQueriesTableConfig() {
    JTable queriesTable = getQueriesTable();
    if (fSortKeys.isEmpty()) {
      queriesTable.getRowSorter().setSortKeys(createDefaultSortKey(getDefaultSortColumnIndex(),
              SortOrder.DESCENDING));
    } else {
      queriesTable.getRowSorter().setSortKeys(fSortKeys);
    }

    for (int i = 0; i < queriesTable.getColumnCount(); i++) {
      queriesTable.getColumnModel().getColumn(i).setPreferredWidth(fColumnsWidths.get(i));
    }
  }

  protected abstract int getDefaultSortColumnIndex();

  protected void handleDoubleClick(MouseEvent e) {
    if (e.getClickCount() == 2) {
      int selectedRow = getQueriesTable().getSelectedRow();
      SQLInfo info = getSQLInfoList().get(getQueriesTable().convertRowIndexToModel(selectedRow));
      getExternalDbView().showChart(info.getApplication(), info.getEnvironment(), info.getConfigName());
    }
  }

  public MultiLineMergeTableCellRenderer getMultiLineCellRenderer() {
    return fmultiLineCellRenderer;
  }

}
