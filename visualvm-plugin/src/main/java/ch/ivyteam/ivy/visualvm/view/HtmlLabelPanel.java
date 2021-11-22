package ch.ivyteam.ivy.visualvm.view;

import java.awt.Font;
import javax.swing.JTextPane;

public class HtmlLabelPanel extends JTextPane {
  public HtmlLabelPanel() {
    super();
    setContentType("text/html");
    setEditable(false);
    setFont(new Font("Tahoma", Font.PLAIN, 11));
  }

}
