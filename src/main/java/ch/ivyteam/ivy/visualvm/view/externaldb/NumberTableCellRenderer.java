package ch.ivyteam.ivy.visualvm.view.externaldb;

import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;

class NumberTableCellRenderer extends EvenOddCellRenderer {

  private static final int SCALE = 1000;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
    String stringValue = value.toString();
    if (value instanceof Long) {
      long longValue = (Long) value;
      if (0 < longValue && longValue < SCALE) {
        stringValue = "<1";
      } else {
        stringValue = NumberFormat.getNumberInstance().format(longValue / SCALE);
      }
    }
    setHorizontalAlignment(JLabel.RIGHT);
    return super.getTableCellRendererComponent(table, stringValue, isSelected, hasFocus, row, column);
  }

}
