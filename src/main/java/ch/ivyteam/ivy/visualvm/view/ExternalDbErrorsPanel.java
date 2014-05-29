/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.model.SQLErrorInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import org.jdesktop.beansbinding.Binding;

/**
 *
 * @author thnghia
 */
public class ExternalDbErrorsPanel extends javax.swing.JPanel {

  private final ExternalDbView fExternalDbView;
  private boolean fIsLoaded = false;

  /**
   * Creates new form ExternalDbPanel
   */
  public ExternalDbErrorsPanel(ExternalDbView view) {
    fExternalDbView = view;
    initComponents();
    tableErrors.setCellEditor(null);
    btnRefresh.addActionListener(new RefreshButtonActionListener());
    tableErrors.addMouseListener(new ErrorsTableMouseListener());
  }

  public void refresh(List<SQLErrorInfo> errors) {
    fIsLoaded = true;
    List<? extends SortKey> sortKeys = tableErrors.getRowSorter().getSortKeys();
    refreshErrorsTable(errors);
    if (sortKeys.isEmpty()) {
      tableErrors.getRowSorter().setSortKeys(createDefaultSortKey());
    } else {
      tableErrors.getRowSorter().setSortKeys(sortKeys);
    }
    clearDetailsArea();
  }

  private void refreshErrorsTable(List<SQLErrorInfo> errors) {
    Binding bindingTableErrors = bindingGroup.getBinding("bindingTableErrors");
    bindingTableErrors.unbind();
    fErrorInfoList.clear();
    fErrorInfoList.addAll(errors);
    bindingTableErrors.bind();
    tableErrors.repaint();
  }

  private void clearDetailsArea() {
    txtStacktrace.setText("");
    txtSQL.setText("");
  }

  public boolean isLoaded() {
    return fIsLoaded;
  }

  private List<SortKey> createDefaultSortKey() {
    List<SortKey> sortKeys = new ArrayList();
    SortKey sortKey = new SortKey(0, SortOrder.DESCENDING);
    sortKeys.add(sortKey);
    return sortKeys;
  }
  // CHECKSTYLE:OFF

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
   * code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;
    bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    fErrorInfoList = new java.util.ArrayList<ch.ivyteam.ivy.visualvm.model.SQLErrorInfo>();
    btnRefresh = new javax.swing.JButton();
    emptyLabel = new javax.swing.JLabel();
    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableErrors = new javax.swing.JTable();
    errorDetailsTabbedPane = new javax.swing.JTabbedPane();
    jScrollPane2 = new javax.swing.JScrollPane();
    txtStacktrace = new javax.swing.JTextArea();
    jScrollPane3 = new javax.swing.JScrollPane();
    txtSQL = new javax.swing.JTextArea();

    setBackground(new java.awt.Color(255, 255, 255));
    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(btnRefresh, org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.btnRefresh.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    add(btnRefresh, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(emptyLabel, org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.emptyLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(emptyLabel, gridBagConstraints);

    jSplitPane1.setBackground(new java.awt.Color(255, 255, 255));
    jSplitPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    jSplitPane1.setDividerLocation(150);
    jSplitPane1.setDividerSize(3);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

    tableErrors.setAutoCreateRowSorter(true);
    tableErrors.setIntercellSpacing(new java.awt.Dimension(2, 2));

    org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, fErrorInfoList, tableErrors, "bindingTableErrors");
    org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${executionTime}"));
    columnBinding.setColumnName("Execution Time");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dbConfig}"));
    columnBinding.setColumnName("Db Config");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${processElementId}"));
    columnBinding.setColumnName("Process Element Id");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${errorMessage}"));
    columnBinding.setColumnName("Error Message");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    bindingGroup.addBinding(jTableBinding);
    jTableBinding.bind();
    jScrollPane1.setViewportView(tableErrors);
    if (tableErrors.getColumnModel().getColumnCount() > 0) {
      tableErrors.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.tableErrors.columnModel.title0_1")); // NOI18N
      tableErrors.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.tableErrors.columnModel.title1_1")); // NOI18N
      tableErrors.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.tableErrors.columnModel.title2_1")); // NOI18N
      tableErrors.getColumnModel().getColumn(3).setHeaderValue(org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.tableErrors.columnModel.title3_1")); // NOI18N
    }

    jSplitPane1.setLeftComponent(jScrollPane1);

    jScrollPane2.setBorder(null);

    txtStacktrace.setEditable(false);
    txtStacktrace.setColumns(20);
    txtStacktrace.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    txtStacktrace.setRows(5);

    org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, tableErrors, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.stacktrace}"), txtStacktrace, org.jdesktop.beansbinding.BeanProperty.create("text"), "bindingTxtStacktrace");
    bindingGroup.addBinding(binding);

    jScrollPane2.setViewportView(txtStacktrace);

    errorDetailsTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.jScrollPane2.TabConstraints.tabTitle"), jScrollPane2); // NOI18N

    jScrollPane3.setBorder(null);

    txtSQL.setEditable(false);
    txtSQL.setColumns(20);
    txtSQL.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    txtSQL.setRows(5);

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, tableErrors, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.statement}"), txtSQL, org.jdesktop.beansbinding.BeanProperty.create("text"), "bindingTxtSQL");
    bindingGroup.addBinding(binding);

    jScrollPane3.setViewportView(txtSQL);

    errorDetailsTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ExternalDbErrorsPanel.class, "ExternalDbErrorsPanel.jScrollPane3.TabConstraints.tabTitle"), jScrollPane3); // NOI18N

    jSplitPane1.setRightComponent(errorDetailsTabbedPane);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(jSplitPane1, gridBagConstraints);

    bindingGroup.bind();
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnRefresh;
  private javax.swing.JLabel emptyLabel;
  private javax.swing.JTabbedPane errorDetailsTabbedPane;
  private java.util.ArrayList<ch.ivyteam.ivy.visualvm.model.SQLErrorInfo> fErrorInfoList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTable tableErrors;
  private javax.swing.JTextArea txtSQL;
  private javax.swing.JTextArea txtStacktrace;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON
  private class RefreshButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      fExternalDbView.refreshErrorTab();
    }

  }

  private class ErrorsTableMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        int selectedRow = tableErrors.getSelectedRow();
        SQLErrorInfo error = fErrorInfoList.get(tableErrors.convertRowIndexToModel(selectedRow));
        fExternalDbView.fireCreateChartsAction(error.getApplication(), error.getEnvironment(), error.
                getConfigName());
        fExternalDbView.switchToChartsTab();
        fExternalDbView.setSelectedNode(error.getApplication(), error.getEnvironment(), 
                error.getConfigName());
      }
    }

  }
}
