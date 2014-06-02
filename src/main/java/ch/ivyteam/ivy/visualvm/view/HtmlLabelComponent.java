package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.view.html.TableGenerator;
import java.awt.Font;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class HtmlLabelComponent extends JLabel {
  private final TableGenerator fTableGenerator;

  public HtmlLabelComponent() {
    super();
    fTableGenerator = new TableGenerator();
    setVerticalAlignment(JLabel.TOP);
    setFont(new Font("Tahoma", Font.PLAIN, 11));
    setText(fTableGenerator.toString());
    setToolTipText("");
  }

  public void addInfo(String key, String label, String value, String unit, String tooltip) {
    fTableGenerator.addRow(key, label, value, unit, tooltip);
    setText(fTableGenerator.toString());
  }

  public void updateValues(long[] values) {
    fTableGenerator.updateValues(values);
    setText(fTableGenerator.toString());
  }

  public void clear() {
    fTableGenerator.clear();
    setText(fTableGenerator.toString());
  }

  @Override
  public String getToolTipText(MouseEvent event) {
    int rowIndex = (int) (event.getY() / 14);
    return fTableGenerator.getTooltip(rowIndex);
  }

}
