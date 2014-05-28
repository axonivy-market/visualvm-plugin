/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.exception.IvyJmxDataCollectException;
import ch.ivyteam.ivy.visualvm.model.IvyApplicationInfo;
import ch.ivyteam.ivy.visualvm.model.OSInfo;
import ch.ivyteam.ivy.visualvm.model.ServerConnectorInfo;
import ch.ivyteam.ivy.visualvm.model.SystemDatabaseInfo;
import ch.ivyteam.ivy.visualvm.service.BasicIvyJmxDataCollector;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.NbBundle.Messages;

@Messages({"CTL_InformationPanelAction=InformationPanel",
  "CTL_InformationPanelTopComponent=InformationPanel Window",
  "HINT_InformationPanelTopComponent=This is a InformationPanel window"})
@SuppressWarnings("PMD.SingularField")
public final class InformationPanelTopComponent extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(InformationPanelTopComponent.class.getName());
  private static final Insets LABEL_INSETS = new Insets(5, 5, 5, 5);

  public InformationPanelTopComponent() {
    initComponents();
    setName(Bundle.CTL_InformationPanelTopComponent());
    setToolTipText(Bundle.HINT_InformationPanelTopComponent());
  }

  /*
   * CHECKSTYLE:OFF
   */
  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
   * code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("PMD")
  // <editor-fold defaultstate="collapsed"
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    generalPanel = new javax.swing.JPanel();
    generalLabel = new javax.swing.JPanel();
    versionLabel = new javax.swing.JLabel();
    buildDateLabel = new javax.swing.JLabel();
    installDirLabel = new javax.swing.JLabel();
    operatingSystemLabel = new javax.swing.JLabel();
    filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
    hostNameLabel = new javax.swing.JLabel();
    generalValue = new javax.swing.JPanel();
    version = new javax.swing.JLabel();
    buildDate = new javax.swing.JLabel();
    installationDirectory = new javax.swing.JLabel();
    filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
    operatingSystem = new javax.swing.JLabel();
    hostNameValue = new javax.swing.JLabel();
    sysDbScrollPane = new javax.swing.JScrollPane();
    sysDbPanel = new javax.swing.JPanel();
    sysDbLabel = new javax.swing.JPanel();
    dbIvySysDbVersionLabel = new javax.swing.JLabel();
    dbTypeLabel = new javax.swing.JLabel();
    dbVersionLabel = new javax.swing.JLabel();
    dbDriverNameLabel = new javax.swing.JLabel();
    dbConnectionUrlLabel = new javax.swing.JLabel();
    dbCredentialUsernameLabel = new javax.swing.JLabel();
    dbHostLabel = new javax.swing.JLabel();
    dbPortLabel = new javax.swing.JLabel();
    filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
    dbNameLabel = new javax.swing.JLabel();
    sysDbValue = new javax.swing.JPanel();
    dbIvySysDbVersion = new javax.swing.JLabel();
    dbType = new javax.swing.JLabel();
    dbVersion = new javax.swing.JLabel();
    dbDriverName = new javax.swing.JLabel();
    dbConnectionUrl = new javax.swing.JLabel();
    dbCredentialUsername = new javax.swing.JLabel();
    dbHost = new javax.swing.JLabel();
    dbPort = new javax.swing.JLabel();
    filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
    dbName = new javax.swing.JLabel();
    connectorsContainScrollPane = new javax.swing.JScrollPane();
    connectorsContainPanel = new javax.swing.JPanel();
    connectorsPanel = new javax.swing.JPanel();
    filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

    setLayout(new java.awt.GridBagLayout());

    generalPanel.setBackground(new java.awt.Color(255, 255, 255));
    generalPanel.setMinimumSize(new java.awt.Dimension(200, 80));
    generalPanel.setPreferredSize(new java.awt.Dimension(200, 130));
    generalPanel.setLayout(new java.awt.GridBagLayout());

    generalLabel.setBackground(new java.awt.Color(255, 255, 255));
    generalLabel.setMinimumSize(new java.awt.Dimension(120, 72));
    generalLabel.setPreferredSize(new java.awt.Dimension(120, 72));
    generalLabel.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(versionLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.versionLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalLabel.add(versionLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(buildDateLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.buildDateLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalLabel.add(buildDateLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(installDirLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.installDirLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalLabel.add(installDirLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(operatingSystemLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.operatingSystemLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalLabel.add(operatingSystemLabel, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    generalLabel.add(filler6, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(hostNameLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.hostNameLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalLabel.add(hostNameLabel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 0.01;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
    generalPanel.add(generalLabel, gridBagConstraints);

    generalValue.setBackground(new java.awt.Color(255, 255, 255));
    generalValue.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(version, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.version.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalValue.add(version, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(buildDate, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.buildDate.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalValue.add(buildDate, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(installationDirectory, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.installationDirectory.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalValue.add(installationDirectory, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    generalValue.add(filler7, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(operatingSystem, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.operatingSystem.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalValue.add(operatingSystem, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(hostNameValue, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.hostNameValue.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    generalValue.add(hostNameValue, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
    generalPanel.add(generalValue, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
    add(generalPanel, gridBagConstraints);

    sysDbScrollPane.setBorder(null);

    sysDbPanel.setBackground(new java.awt.Color(255, 255, 255));
    sysDbPanel.setMinimumSize(new java.awt.Dimension(500, 230));
    sysDbPanel.setPreferredSize(new java.awt.Dimension(220, 250));
    sysDbPanel.setLayout(new java.awt.GridBagLayout());

    sysDbLabel.setBackground(new java.awt.Color(255, 255, 255));
    sysDbLabel.setMinimumSize(new java.awt.Dimension(120, 194));
    sysDbLabel.setPreferredSize(new java.awt.Dimension(120, 194));
    sysDbLabel.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(dbIvySysDbVersionLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbIvySysDbVersionLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbIvySysDbVersionLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbTypeLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbTypeLabel.text")); // NOI18N
    dbTypeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbTypeLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbVersionLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbVersionLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbVersionLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbDriverNameLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbDriverNameLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbDriverNameLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbConnectionUrlLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbConnectionUrlLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbConnectionUrlLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbCredentialUsernameLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbCredentialUsernameLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbCredentialUsernameLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbHostLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbHostLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbHostLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbPortLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbPortLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbPortLabel, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    sysDbLabel.add(filler2, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbNameLabel, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbNameLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbLabel.add(dbNameLabel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 0.01;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
    sysDbPanel.add(sysDbLabel, gridBagConstraints);

    sysDbValue.setBackground(new java.awt.Color(255, 255, 255));
    sysDbValue.setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(dbIvySysDbVersion, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbIvySysDbVersion.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbIvySysDbVersion, gridBagConstraints);

    dbType.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    org.openide.awt.Mnemonics.setLocalizedText(dbType, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbType.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbType, gridBagConstraints);

    dbVersion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    org.openide.awt.Mnemonics.setLocalizedText(dbVersion, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbVersion.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbVersion, gridBagConstraints);

    dbDriverName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    org.openide.awt.Mnemonics.setLocalizedText(dbDriverName, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbDriverName.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbDriverName, gridBagConstraints);

    dbConnectionUrl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    org.openide.awt.Mnemonics.setLocalizedText(dbConnectionUrl, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbConnectionUrl.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbConnectionUrl, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbCredentialUsername, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbCredentialUsername.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbCredentialUsername, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbHost, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbHost.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbHost, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbPort, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbPort.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbPort, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    sysDbValue.add(filler3, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(dbName, org.openide.util.NbBundle.getMessage(InformationPanelTopComponent.class, "InformationPanelTopComponent.dbName.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    sysDbValue.add(dbName, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
    sysDbPanel.add(sysDbValue, gridBagConstraints);

    sysDbScrollPane.setViewportView(sysDbPanel);

    add(sysDbScrollPane, new java.awt.GridBagConstraints());

    connectorsContainScrollPane.setBorder(null);

    connectorsContainPanel.setBackground(new java.awt.Color(255, 255, 255));
    connectorsContainPanel.setLayout(new java.awt.GridBagLayout());

    connectorsPanel.setBackground(new java.awt.Color(255, 255, 255));
    connectorsPanel.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
    connectorsContainPanel.add(connectorsPanel, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    connectorsContainPanel.add(filler1, gridBagConstraints);

    connectorsContainScrollPane.setViewportView(connectorsContainPanel);

    add(connectorsContainScrollPane, new java.awt.GridBagConstraints());
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel buildDate;
  private javax.swing.JLabel buildDateLabel;
  private javax.swing.JPanel connectorsContainPanel;
  private javax.swing.JScrollPane connectorsContainScrollPane;
  private javax.swing.JPanel connectorsPanel;
  private javax.swing.JLabel dbConnectionUrl;
  private javax.swing.JLabel dbConnectionUrlLabel;
  private javax.swing.JLabel dbCredentialUsername;
  private javax.swing.JLabel dbCredentialUsernameLabel;
  private javax.swing.JLabel dbDriverName;
  private javax.swing.JLabel dbDriverNameLabel;
  private javax.swing.JLabel dbHost;
  private javax.swing.JLabel dbHostLabel;
  private javax.swing.JLabel dbIvySysDbVersion;
  private javax.swing.JLabel dbIvySysDbVersionLabel;
  private javax.swing.JLabel dbName;
  private javax.swing.JLabel dbNameLabel;
  private javax.swing.JLabel dbPort;
  private javax.swing.JLabel dbPortLabel;
  private javax.swing.JLabel dbType;
  private javax.swing.JLabel dbTypeLabel;
  private javax.swing.JLabel dbVersion;
  private javax.swing.JLabel dbVersionLabel;
  private javax.swing.Box.Filler filler1;
  private javax.swing.Box.Filler filler2;
  private javax.swing.Box.Filler filler3;
  private javax.swing.Box.Filler filler6;
  private javax.swing.Box.Filler filler7;
  private javax.swing.JPanel generalLabel;
  private javax.swing.JPanel generalPanel;
  private javax.swing.JPanel generalValue;
  private javax.swing.JLabel hostNameLabel;
  private javax.swing.JLabel hostNameValue;
  private javax.swing.JLabel installDirLabel;
  private javax.swing.JLabel installationDirectory;
  private javax.swing.JLabel operatingSystem;
  private javax.swing.JLabel operatingSystemLabel;
  private javax.swing.JPanel sysDbLabel;
  private javax.swing.JPanel sysDbPanel;
  private javax.swing.JScrollPane sysDbScrollPane;
  private javax.swing.JPanel sysDbValue;
  private javax.swing.JLabel version;
  private javax.swing.JLabel versionLabel;
  // End of variables declaration//GEN-END:variables

  void writeProperties(java.util.Properties p) {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    p.setProperty("version", "1.0");
    // TO DO store your settings
  }

  /*
   * CHECKSTYLE:ON
   */
  public void readInformation(DataBeanProvider dataBeanProvider) {
    MBeanServerConnection connection = dataBeanProvider.getMBeanServerConnection();
    BasicIvyJmxDataCollector collector = new BasicIvyJmxDataCollector();

    IvyApplicationInfo basicInfo = dataBeanProvider.getGenericData().getApplicationInfo();
    if (basicInfo != null) {
      setLabelText(versionLabel, version, basicInfo.getVersion());
      setLabelText(buildDateLabel, buildDate, DataUtils.dateToString(basicInfo.getBuildDate()));
      setLabelText(installDirLabel, installationDirectory, basicInfo.getInstallationDirectory());
    } else {
      hideInfoLabels(versionLabel, version);
      hideInfoLabels(buildDateLabel, buildDate);
      hideInfoLabels(installDirLabel, installationDirectory);
    }

    try {
      OSInfo osInfo = collector.getOSInfo(connection);
      setLabelText(operatingSystemLabel, operatingSystem, osInfo.getName());
    } catch (IvyJmxDataCollectException ex) {
      hideInfoLabels(operatingSystemLabel, operatingSystem);
      LOGGER.warning(ex.getMessage());
    }

    try {
      SystemDatabaseInfo sysDbInfo = collector.getSystemDatabaseInfo(connection);
      setLabelText(dbIvySysDbVersionLabel, dbIvySysDbVersion, sysDbInfo.getIvySystemDbVersion());
      setLabelText(dbTypeLabel, dbType, sysDbInfo.getType());
      setLabelText(dbVersionLabel, dbVersion, sysDbInfo.getVersion());
      setLabelText(dbDriverNameLabel, dbDriverName, sysDbInfo.getDriver());
      setLabelText(dbHostLabel, dbHost, DataUtils.getHostFromConnectionUrl(sysDbInfo.getConnectionUrl()));
      setLabelText(dbPortLabel, dbPort, DataUtils.getPortFromConnectionUrl(sysDbInfo.getConnectionUrl()));
      setLabelText(dbNameLabel, dbName, DataUtils.getSchemaFromConnectionUrl(sysDbInfo.getConnectionUrl()));
      setLabelText(dbConnectionUrlLabel, dbConnectionUrl, sysDbInfo.getConnectionUrl());

      String username = sysDbInfo.getUsername();
      if ((username == null) || "".equals(username.trim())) {
        hideInfoLabels(dbCredentialUsernameLabel, dbCredentialUsername);
      } else {
        dbCredentialUsernameLabel.setVisible(true);
        dbCredentialUsername.setText(username);
        dbCredentialUsername.setVisible(true);
      }
    } catch (IvyJmxDataCollectException ex) {
      hideInfoLabels(dbIvySysDbVersionLabel, dbIvySysDbVersion);
      hideInfoLabels(dbTypeLabel, dbType);
      hideInfoLabels(dbVersionLabel, dbVersion);
      hideInfoLabels(dbDriverNameLabel, dbDriverName);
      hideInfoLabels(dbHostLabel, dbHost);
      hideInfoLabels(dbPortLabel, dbPort);
      hideInfoLabels(dbNameLabel, dbName);
      hideInfoLabels(dbConnectionUrlLabel, dbConnectionUrl);
      hideInfoLabels(dbCredentialUsernameLabel, dbCredentialUsername);
      LOGGER.warning(ex.getMessage());
    }

    List<ServerConnectorInfo> connectorInfo = dataBeanProvider.getGenericData().getServerConnectors();
    connectorsPanel.removeAll();
    if (connectorInfo.size() > 0) {
      int index = 0;
      for (ServerConnectorInfo connector : connectorInfo) {
        JLabel label = new JLabel(connector.getDisplayProtocol() + " - " + connector.getPort());
        connectorsPanel.add(label, new GridBagConstraints(0, index++, 1, 1, 1, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, LABEL_INSETS, 0, 0));
      }
    } else {
      JLabel label = new JLabel("No connector!");
      connectorsPanel.add(label, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
              GridBagConstraints.NONE, LABEL_INSETS, 0, 0));
    }

    try {
      String hostInfo = collector.getHostInfo(connection);
      setLabelText(hostNameLabel, hostNameValue, hostInfo);
    } catch (IvyJmxDataCollectException ex) {
      LOGGER.warning(ex.getMessage());
    }
  }

  private void setLabelText(JLabel titleLabel, JLabel valueLabel, Object data) {
    if (data != null) {
      titleLabel.setVisible(true);
      valueLabel.setVisible(true);
      if (data instanceof String) {
        valueLabel.setText((String) data);
      } else {
        valueLabel.setText(data.toString());
      }
    } else {
      hideInfoLabels(titleLabel, valueLabel);
    }
  }

  private void hideInfoLabels(JLabel titleLabel, JLabel valueLabel) {
    titleLabel.setVisible(false);
    valueLabel.setVisible(false);
  }

  public JComponent getConnectorsPanel() {
    return connectorsContainScrollPane;
  }

  public JComponent getGeneralPanel() {
    return generalPanel;
  }

  public JComponent getSysDbPanel() {
    return sysDbScrollPane;
  }

}
