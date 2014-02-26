package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.view.html.TableGenerator;
import java.awt.Font;
import javax.swing.JTextPane;

public class HtmlLabelPanel extends JTextPane {
  private final TableGenerator fTableGenerator;

  public HtmlLabelPanel() {
    super();
    fTableGenerator = new TableGenerator();
    setContentType("text/html");
    setEditable(false);
    setFont(new Font("Tahoma", Font.PLAIN, 11));
    setText(fTableGenerator.toString());
  }

  public void addInfo(String label, String value) {
    fTableGenerator.addRow(label, value);
    setText(fTableGenerator.toString());
  }

  public void clear() {
    fTableGenerator.clear();
    setText(fTableGenerator.toString());
  }

}
