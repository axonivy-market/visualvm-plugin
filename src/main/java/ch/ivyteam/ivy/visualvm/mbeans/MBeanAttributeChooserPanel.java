/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

import javax.management.MBeanServerConnection;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author rwei
 */
public class MBeanAttributeChooserPanel extends javax.swing.JPanel {
  private MBeanServerConnection mBeanServerConnection;

  /**
   * Creates new form MBeanAttributeChooserPanel
   */
  public MBeanAttributeChooserPanel() {
    initComponents();
  }

  public void setMBeanServerConnection(
          MBeanServerConnection mBeanServerConnection) {
    this.mBeanServerConnection = mBeanServerConnection;
  }

  public String getMBeanName() {
    return mBeanName.getText();
  }

  public String getAttribute() {
    return attribute.getText();
  }

  /* CHECKSTYLE:OFF */
  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
   * code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed"
  // desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    mBeanLabel = new javax.swing.JLabel();
    mBeanName = new javax.swing.JTextField();
    attributeLabel = new javax.swing.JLabel();
    attribute = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();

    org.openide.awt.Mnemonics.setLocalizedText(mBeanLabel,
            org.openide.util.NbBundle.getMessage(
                    MBeanAttributeChooserPanel.class,
                    "MBeanAttributeChooserPanel.mBeanLabel.text")); // NOI18N

    mBeanName.setText(org.openide.util.NbBundle.getMessage(
            MBeanAttributeChooserPanel.class,
            "MBeanAttributeChooserPanel.mBeanName.text")); // NOI18N
    mBeanName.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        mBeanNameActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(attributeLabel,
            org.openide.util.NbBundle.getMessage(
                    MBeanAttributeChooserPanel.class,
                    "MBeanAttributeChooserPanel.attributeLabel.text")); // NOI18N

    attribute.setText(org.openide.util.NbBundle.getMessage(
            MBeanAttributeChooserPanel.class,
            "MBeanAttributeChooserPanel.attribute.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jButton1,
            org.openide.util.NbBundle.getMessage(
                    MBeanAttributeChooserPanel.class,
                    "MBeanAttributeChooserPanel.jButton1.text")); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                    layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                    layout.createParallelGroup(
                                            javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(attributeLabel)
                                            .addComponent(mBeanLabel))
                            .addPreferredGap(
                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(
                                    layout.createParallelGroup(
                                            javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(mBeanName)
                                            .addComponent(
                                                    attribute,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    282, Short.MAX_VALUE))
                            .addPreferredGap(
                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton1).addContainerGap()));
    layout.setVerticalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                    layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                    layout.createParallelGroup(
                                            javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(mBeanLabel)
                                            .addComponent(
                                                    mBeanName,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(
                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(
                                    layout.createParallelGroup(
                                            javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(attributeLabel)
                                            .addComponent(
                                                    attribute,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton1))
                            .addContainerGap(
                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                    Short.MAX_VALUE)));
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton1ActionPerformed
  {// GEN-HEADEREND:event_jButton1ActionPerformed
    MBeanBrowserTopComponent mBeanBrowser = new MBeanBrowserTopComponent();
    mBeanBrowser
            .setMBeanTreeModel(new MBeanTreeModel(mBeanServerConnection));
    mBeanBrowser.setSelected(mBeanName.getText(), attribute.getText());

    NotifyDescriptor descriptor = new NotifyDescriptor(mBeanBrowser,
            "New chart", NotifyDescriptor.OK_CANCEL_OPTION,
            NotifyDescriptor.PLAIN_MESSAGE, null,
            NotifyDescriptor.YES_OPTION);

    if (DialogDisplayer.getDefault().notify(descriptor) == NotifyDescriptor.OK_OPTION) {
      mBeanName.setText(mBeanBrowser.getSelectedMBean());
      attribute.setText(mBeanBrowser.getSelectedAttribute());
    }
  }// GEN-LAST:event_jButton1ActionPerformed

  private void mBeanNameActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_mBeanNameActionPerformed
  {// GEN-HEADEREND:event_mBeanNameActionPerformed
    // TODO add your handling code here:
  }// GEN-LAST:event_mBeanNameActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField attribute;
  private javax.swing.JLabel attributeLabel;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel mBeanLabel;
  private javax.swing.JTextField mBeanName;
  // End of variables declaration//GEN-END:variables
  /* CHECKSTYLE:ON */
}
