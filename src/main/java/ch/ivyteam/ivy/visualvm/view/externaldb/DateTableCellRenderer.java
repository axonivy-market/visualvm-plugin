package ch.ivyteam.ivy.visualvm.view.externaldb;

import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.awt.Component;
import java.util.Date;
import javax.swing.JTable;

class DateTableCellRenderer extends EvenOddCellRenderer {

  public DateTableCellRenderer() {
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
    String strDate = "";
    if (value instanceof Date) {
      strDate = DataUtils.dateTimeToString((Date) value);
    }
    return super.getTableCellRendererComponent(table, strDate, isSelected, hasFocus, row, column);
  }

}
