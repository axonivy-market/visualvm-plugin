package ch.ivyteam.ivy.visualvm.view.externaldb;

import ch.ivyteam.ivy.visualvm.model.IExecutionInfo;
import ch.ivyteam.ivy.visualvm.model.SQLInfo;
import ch.ivyteam.ivy.visualvm.view.common.NumberTableCellRenderer;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.beansbinding.Binding;

// CHECKSTYLE:OFF
@SuppressWarnings("PMD")
public class ExternalDbSlowQueriesPanel extends AbstractExternalDbQueriesPanel {

  private static final int COL_EXEC_TIME = 3;
  private static final int COL_STATEMENT = 4;
  private final NumberTableCellRenderer fNumberCellRenderer = new NumberTableCellRenderer();

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;
    bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    fSQLInfoList = new java.util.ArrayList<SQLInfo>();
    emptyLabel = new javax.swing.JLabel();
    btnRefresh = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableSlowQueries = new javax.swing.JTable();
    jScrollPane2 = new javax.swing.JScrollPane();
    txtSQL = new javax.swing.JTextArea();

    setBackground(new java.awt.Color(255, 255, 255));
    setLayout(new java.awt.GridBagLayout());

    org.openide.awt.Mnemonics.setLocalizedText(emptyLabel, org.openide.util.NbBundle.getMessage(ExternalDbSlowQueriesPanel.class, "ExternalDbSlowQueriesPanel.emptyLabel.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(emptyLabel, gridBagConstraints);

    org.openide.awt.Mnemonics.setLocalizedText(btnRefresh, org.openide.util.NbBundle.getMessage(ExternalDbSlowQueriesPanel.class, "ExternalDbSlowQueriesPanel.btnRefresh.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    add(btnRefresh, gridBagConstraints);

    jSplitPane1.setBorder(null);
    jSplitPane1.setDividerLocation(200);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    tableSlowQueries.setAutoCreateRowSorter(true);
    tableSlowQueries.getTableHeader().setReorderingAllowed(false);

    org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, fSQLInfoList, tableSlowQueries, "bindingSlowQueriesTable");
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
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${executionTime}"));
    columnBinding.setColumnName("Execution Time (ms)");
    columnBinding.setColumnClass(Long.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${statement}"));
    columnBinding.setColumnName("Statement");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    bindingGroup.addBinding(jTableBinding);
    jTableBinding.bind();
    jScrollPane1.setViewportView(tableSlowQueries);

    jSplitPane1.setTopComponent(jScrollPane1);

    jScrollPane2.setBorder(null);

    txtSQL.setEditable(false);
    txtSQL.setColumns(20);
    txtSQL.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    txtSQL.setRows(5);
    txtSQL.setLineWrap(true);
    txtSQL.setWrapStyleWord(true);

    org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, tableSlowQueries, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.statement}"), txtSQL, org.jdesktop.beansbinding.BeanProperty.create("text"), "bindingSQL");
    bindingGroup.addBinding(binding);

    jScrollPane2.setViewportView(txtSQL);

    jSplitPane1.setRightComponent(jScrollPane2);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(jSplitPane1, gridBagConstraints);

    bindingGroup.bind();
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnRefresh;
  private javax.swing.JLabel emptyLabel;
  private java.util.ArrayList<SQLInfo> fSQLInfoList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTable tableSlowQueries;
  private javax.swing.JTextArea txtSQL;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables

  public ExternalDbSlowQueriesPanel(ExternalDbView externalDbView) {
    super(externalDbView);
    initComponents();
    initTableCellRenderer();
    initListeners();
  }

  @Override
  protected JTable getTable() {
    return tableSlowQueries;
  }

  @Override
  protected JButton getRefreshButton() {
    return btnRefresh;
  }

  @Override
  protected List<? extends IExecutionInfo> getTableModelList() {
    return fSQLInfoList;
  }

  @Override
  protected int getDefaultSortColumnIndex() {
    return 3;
  }

  @Override
  protected void refreshTable() {
    Binding binding = bindingGroup.getBinding("bindingSlowQueriesTable");
    binding.unbind();
    fSQLInfoList.clear();
    fSQLInfoList.addAll(getExternalDbView().getSlowSQLInfoBuffer());
    binding.bind();
    TableColumnModel colModel = tableSlowQueries.getColumnModel();
    colModel.getColumn(COL_TIME).setCellRenderer(getDateCellRenderer());
    colModel.getColumn(COL_EXEC_TIME).setCellRenderer(fNumberCellRenderer);
    colModel.getColumn(COL_STATEMENT).setCellRenderer(getMultiLineCellRenderer());
    tableSlowQueries.repaint();
    txtSQL.setText(StringUtils.EMPTY);
  }

}
