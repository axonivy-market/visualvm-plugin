package ch.ivyteam.ivy.visualvm.view.externaldb;

import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JTable;
import org.jdesktop.beansbinding.Binding;

public class ExternalDbErrorsPanel extends AbstractExternalDbQueriesPanel {
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

    fErrorInfoList = new java.util.ArrayList<SQLInfo>();
    btnRefresh = new javax.swing.JButton();
    emptyLabel = new javax.swing.JLabel();
    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableErrors = new javax.swing.JTable();
    jScrollPane3 = new javax.swing.JScrollPane();
    txtDetails = new javax.swing.JTextArea();

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
    jSplitPane1.setBorder(null);
    jSplitPane1.setDividerLocation(200);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

    tableErrors.setAutoCreateRowSorter(true);
    tableErrors.getTableHeader().setReorderingAllowed(false);

    org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, fErrorInfoList, tableErrors, "bindingTableErrors");
    org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${time}"));
    columnBinding.setColumnName("Time");
    columnBinding.setColumnClass(java.util.Date.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dbConfig}"));
    columnBinding.setColumnName("DB Config");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${processElementId}"));
    columnBinding.setColumnName("Process Element ID");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${errorMessage}"));
    columnBinding.setColumnName("Error Message");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    bindingGroup.addBinding(jTableBinding);
    jTableBinding.bind();
    jScrollPane1.setViewportView(tableErrors);

    jSplitPane1.setLeftComponent(jScrollPane1);

    jScrollPane3.setBorder(null);

    txtDetails.setEditable(false);
    txtDetails.setColumns(20);
    txtDetails.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    txtDetails.setRows(5);

    org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, tableErrors, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.errorMessage}\n\n${selectedElement.statement}"), txtDetails, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    jScrollPane3.setViewportView(txtDetails);

    jSplitPane1.setRightComponent(jScrollPane3);

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
  private java.util.ArrayList<SQLInfo> fErrorInfoList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTable tableErrors;
  private javax.swing.JTextArea txtDetails;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON

  public ExternalDbErrorsPanel(ExternalDbView view) {
    super(view);
    initComponents();
    initTableCellRenderer();
    btnRefresh.addActionListener(new RefreshButtonActionListener());
    tableErrors.addMouseListener(new ErrorsTableMouseListener());
  }

  @Override
  protected List<SQLInfo> getSQLInfoList() {
    return fErrorInfoList;
  }

  @Override
  protected JTable getQueriesTable() {
    return tableErrors;
  }

  @Override
  protected void clearDetailsArea() {
    txtDetails.setText("");
  }

  @Override
  protected int getDefaultSortColumnIndex() {
    return 0;
  }

  @Override
  protected void refreshQueriesTable(List<SQLInfo> sqlInfoList) {
    Binding bindingTableErrors = bindingGroup.getBinding("bindingTableErrors");
    bindingTableErrors.unbind();
    fErrorInfoList.clear();
    fErrorInfoList.addAll(sqlInfoList);
    bindingTableErrors.bind();
    tableErrors.getColumnModel().getColumn(0).setCellRenderer(getDateCellRenderer());
    tableErrors.repaint();
  }

  private class RefreshButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      getExternalDbView().refreshErrorTab();
      adjustColumns();
    }

  }

  private class ErrorsTableMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      handleDoubleClick(e);
    }

  }
}
