package ch.ivyteam.ivy.visualvm.view.externaldb;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class EvenOddCellRenderer extends DefaultTableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
    JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected,
            hasFocus, row, column);
    renderer.setOpaque(true);

    Color foreground, background;
    if (isSelected) {
      foreground = table.getSelectionForeground();
      background = table.getSelectionBackground();
    } else {
      foreground = table.getForeground();
      if (row % 2 == 0) {
        background = table.getBackground();
      } else {
        background = getDarkerColor(table.getBackground());
      }
    }
    renderer.setForeground(foreground);
    renderer.setBackground(background);

    Color gridColor = table.getGridColor();
    Border border = BorderFactory.createCompoundBorder();
    Border borderLine = BorderFactory.createMatteBorder(0, 0, 0, 1, gridColor);
    Border padding = BorderFactory.createEmptyBorder(1, 4, 1, 4);
    border = BorderFactory.createCompoundBorder(border, borderLine);
    border = BorderFactory.createCompoundBorder(border, padding);
    renderer.setBorder(border);
    return renderer;
  }

  private Color getDarkerColor(Color color) {
    return new Color(
            darkenColorValue(color.getRed()),
            darkenColorValue(color.getGreen()),
            darkenColorValue(color.getBlue()));
  }

  private int darkenColorValue(int colorValue) {
    if (colorValue == 0) {
      return colorValue;
    }
    return colorValue - 10;
  }

}
