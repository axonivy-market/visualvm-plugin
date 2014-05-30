package ch.ivyteam.ivy.visualvm.view;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class DateTableCellRenderer extends DefaultTableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
    String dateValue = value.toString();
    if (value instanceof Date) {
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      dateValue = format.format(value);
    }
    return super.getTableCellRendererComponent(table, dateValue, isSelected, hasFocus, row, column);
  }

}
