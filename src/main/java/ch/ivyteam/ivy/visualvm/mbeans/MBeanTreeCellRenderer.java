/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.openide.util.ImageUtilities;

/**
 *
 * @author rwei
 */
class MBeanTreeCellRenderer extends DefaultTreeCellRenderer {
  private static final ImageIcon MBEAN_ICON = new ImageIcon(ImageUtilities.loadImage(
          "resources/icons/mbean.gif", true).getScaledInstance(16, 16, Image.SCALE_SMOOTH));

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
          boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    if (value instanceof MBeanNode) {
      if (((MBeanNode) value).isMBean()) {
        setIcon(MBEAN_ICON);
      }
    }
    return this;
  }

}
