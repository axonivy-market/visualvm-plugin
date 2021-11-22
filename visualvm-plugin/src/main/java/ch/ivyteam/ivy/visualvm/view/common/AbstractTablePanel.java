package ch.ivyteam.ivy.visualvm.view.common;

import ch.ivyteam.ivy.visualvm.model.IExecutionInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;

public abstract class AbstractTablePanel extends JPanel {

  public static final int COL_TIME = 0;
  private static final Color TABLE_VERTICAL_GRID_COLOR = new Color(214, 223, 247);
  private static final Color TABLE_SELECTION_BACKGROUND_COLOR = new Color(193, 210, 238);
  private static final int TABLE_ROW_HEIGHT = 18;
  private final DateTableCellRenderer fDateCellRenderer = new DateTableCellRenderer();
  private boolean fIsLoaded = false;

  private final List<Integer> fColumnsWidths = new ArrayList<>();
  private List<? extends RowSorter.SortKey> fSortKeys;
  private volatile boolean fIsRefreshing = false;

  public void refresh() {
    synchronized (this) {
      if (fIsRefreshing) {
        return;// prevent other threads from calling this method when there is a running thread.
      }
      fIsRefreshing = true;
    }
    storeTableConfig(getTable());
    refreshTable();
    restoreTableConfig();
    adjustColumns();
    setLoaded(true);
    fIsRefreshing = false;
  }

  public boolean isLoaded() {
    return fIsLoaded;
  }

  protected void setLoaded(boolean isLoaded) {
    this.fIsLoaded = isLoaded;
  }

  protected DateTableCellRenderer getDateCellRenderer() {
    return fDateCellRenderer;
  }

  protected void initTableCellRenderer() {
    JTable table = getTable();
    EvenOddCellRenderer renderer = new EvenOddCellRenderer();
    table.setDefaultRenderer(Object.class, renderer);
    table.setShowGrid(false);
    table.setIntercellSpacing(new Dimension(0, 0));
    table.setGridColor(TABLE_VERTICAL_GRID_COLOR);
    table.setSelectionBackground(TABLE_SELECTION_BACKGROUND_COLOR);
    table.setSelectionForeground(table.getForeground());
    table.setRowHeight(TABLE_ROW_HEIGHT);
    table.repaint();
  }

  protected abstract void executeDoubleClick(IExecutionInfo info);

  protected abstract List<? extends IExecutionInfo> getTableModelList();

  protected abstract int getDefaultSortColumnIndex();

  protected abstract void refreshTable();

  protected void initListeners() {
    addMouseListenerOnTable();
    addActionListenerOnRefreshButton();
  }

  protected abstract JButton getRefreshButton();

  protected abstract JTable getTable();

  private List<RowSorter.SortKey> createDefaultSortKey(int columnIndex, SortOrder order) {
    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
    RowSorter.SortKey sortKey = new RowSorter.SortKey(columnIndex, order);
    sortKeys.add(sortKey);
    return sortKeys;
  }

  private void storeTableConfig(JTable table) {
    fSortKeys = table.getRowSorter().getSortKeys();
    fColumnsWidths.clear();
    Enumeration<TableColumn> columns = getTable().getColumnModel().getColumns();
    while (columns.hasMoreElements()) {
      TableColumn column = columns.nextElement();
      int colWidth = column.getWidth();
      fColumnsWidths.add(colWidth);
    }
  }

  private void restoreTableConfig() {
    JTable queriesTable = getTable();
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

  private void adjustColumns() {
    TableColumnAdjuster adjuster = new TableColumnAdjuster(getTable());
    adjuster.adjustColumns();
  }

  private void handleDoubleClick(MouseEvent e) {
    if (e.getClickCount() == 2) {
      int selectedRow = getTable().getSelectedRow();
      IExecutionInfo info = getTableModelList().get(getTable().convertRowIndexToModel(selectedRow));
      executeDoubleClick(info);
    }
  }

  private void addMouseListenerOnTable() {
    getTable().addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        handleDoubleClick(e);
      }
    });
  }

  private void addActionListenerOnRefreshButton() {
    getRefreshButton().addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        refresh();
      }
    });
  }

}
