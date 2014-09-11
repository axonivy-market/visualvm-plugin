package ch.ivyteam.ivy.visualvm.view.externaldb;

import java.awt.Component;
import javax.swing.JTable;

class MultiLineMergeTableCellRenderer extends EvenOddCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
    String stringValue = null;
    if (value != null) {
      stringValue = value.toString();
      stringValue = stringValue.replaceAll("[\\t\\r\\n]+", " ");
    }
    return super.getTableCellRendererComponent(table, stringValue, isSelected, hasFocus, row, column);
  }

}
