/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.model.IvyLicenseInfo;

/**
 *
 * @author thtam
 */
@SuppressWarnings("PMD.SingularField")
public class LicenseInformationPanel extends javax.swing.JPanel {

  private final IvyLicenseInfo fLicenseInfo;
  private static final long MILLISECONDS_IN_ONE_SECOND = 1000;
  private static final long MILLISECONDS_IN_ONE_MINUTE = 60 * MILLISECONDS_IN_ONE_SECOND;
  private static final long MILLISECONDS_IN_ONE_HOUR = 60 * MILLISECONDS_IN_ONE_MINUTE;
  public static final long MILLISECONDS_IN_ONE_DAY = 24 * MILLISECONDS_IN_ONE_HOUR;

  /**
   * Creates new form LicenseInformationPanel
   */
  public LicenseInformationPanel(IvyLicenseInfo licenseInfo) {
    initComponents();
    fLicenseInfo = licenseInfo;
  }

  /* CHECKSTYLE:OFF */
  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
   * code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings({"unchecked", "PMD"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jScrollPane2 = new javax.swing.JScrollPane();
    htmlLabelPanel1 = new ch.ivyteam.ivy.visualvm.view.HtmlLabelPanel();

    setBackground(new java.awt.Color(255, 255, 255));
    setLayout(new java.awt.GridBagLayout());

    jScrollPane2.setBorder(null);
    jScrollPane2.setViewportView(htmlLabelPanel1);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(jScrollPane2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private ch.ivyteam.ivy.visualvm.view.HtmlLabelPanel htmlLabelPanel1;
  private javax.swing.JScrollPane jScrollPane2;
  // End of variables declaration//GEN-END:variables
  /* CHECKSTYLE:ON */

  public void setLicenseData() {
    if (fLicenseInfo != null) {
      htmlLabelPanel1.setText(fLicenseInfo.toHTMLString());
    }
  }

  public void setRemainingTimeInfo() {
    StringBuilder licenseExpirationInfo = new StringBuilder();
    licenseExpirationInfo.append("(in ");
    long delta = fLicenseInfo.getRemaingTime();
    if (delta > MILLISECONDS_IN_ONE_DAY) {
      int day = (int) (delta / MILLISECONDS_IN_ONE_DAY);
      licenseExpirationInfo.append(" ").append(day).append(" day");
      appendPluralIndicator(licenseExpirationInfo, day);
      delta = delta - day * MILLISECONDS_IN_ONE_DAY;
    }
    if (delta > MILLISECONDS_IN_ONE_HOUR) {
      int hour = (int) (delta / MILLISECONDS_IN_ONE_HOUR);
      licenseExpirationInfo.append(" ").append(hour).append(" hour");
      appendPluralIndicator(licenseExpirationInfo, hour);
      delta = delta - hour * MILLISECONDS_IN_ONE_HOUR;
    }
    if (delta > MILLISECONDS_IN_ONE_MINUTE) {
      int minute = (int) (delta / MILLISECONDS_IN_ONE_MINUTE);
      licenseExpirationInfo.append(" ").append(minute).append(" minute");
      appendPluralIndicator(licenseExpirationInfo, minute);
      delta = delta - minute * MILLISECONDS_IN_ONE_MINUTE;
    }
    delta = delta / MILLISECONDS_IN_ONE_SECOND;
    licenseExpirationInfo.append(" ").append(delta).append(" second");
    appendPluralIndicator(licenseExpirationInfo, (int) delta);
    licenseExpirationInfo.append(" )");
  }

  private void appendPluralIndicator(StringBuilder licenseExpirationInfo, int amount) {
    if (amount > 1) {
      licenseExpirationInfo.append("s");
    }
  }

}
