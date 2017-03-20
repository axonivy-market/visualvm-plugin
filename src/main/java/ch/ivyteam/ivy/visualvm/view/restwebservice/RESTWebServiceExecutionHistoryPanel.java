/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view.restwebservice;

import ch.ivyteam.ivy.visualvm.model.IExecutionInfo;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import ch.ivyteam.ivy.visualvm.view.common.NumberTableCellRenderer;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import org.jdesktop.beansbinding.Binding;

// CHECKSTYLE:OFF
@SuppressWarnings("PMD")
public class RESTWebServiceExecutionHistoryPanel extends AbstractRESTWebServiceExecutionPanel {

  public static final int COL_EXEC_TIME = 3;
  private final NumberTableCellRenderer fNumberCellRenderer = new NumberTableCellRenderer();

  public RESTWebServiceExecutionHistoryPanel(RESTWebServicesView view) {
    super(view);
    initComponents();
    initTableCellRenderer();
    initListeners();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the FormEditor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;
    bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    fExecutionsInfos = new java.util.ArrayList<RESTWebServiceInfo>();
    btnRefresh = new javax.swing.JButton();
    lblEmpty = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableExecution = new javax.swing.JTable();

    setBackground(new java.awt.Color(255, 255, 255));
    setLayout(new java.awt.GridBagLayout());

    btnRefresh.setText("Refresh");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    add(btnRefresh, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(lblEmpty, gridBagConstraints);

    tableExecution.setAutoCreateRowSorter(true);
    tableExecution.getTableHeader().setReorderingAllowed(false);

    org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, fExecutionsInfos, tableExecution, "bindingExecutionTable");
    org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${time}"));
    columnBinding.setColumnName("Time");
    columnBinding.setColumnClass(java.util.Date.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${webServiceConfig}"));
    columnBinding.setColumnName("Web Service Config");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${processElementId}"));
    columnBinding.setColumnName("Process Element Id");
    columnBinding.setColumnClass(String.class);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${executionTime}"));
    columnBinding.setColumnName("Execution Time (ms)");
    columnBinding.setColumnClass(Long.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${requestMethod}"));
    columnBinding.setColumnName("Method");
    columnBinding.setColumnClass(String.class);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${requestUrl}"));
    columnBinding.setColumnName("Request Url");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${responseStatus}"));
    columnBinding.setColumnName("Response Status");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    bindingGroup.addBinding(jTableBinding);
    jTableBinding.bind();
    jScrollPane1.setViewportView(tableExecution);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(jScrollPane1, gridBagConstraints);

    bindingGroup.bind();
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnRefresh;
  private java.util.ArrayList<RESTWebServiceInfo> fExecutionsInfos;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel lblEmpty;
  private javax.swing.JTable tableExecution;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables

  @Override
  protected JTable getTable() {
    return tableExecution;
  }

  @Override
  protected JButton getRefreshButton() {
    return btnRefresh;
  }

  @Override
  protected void refreshTable() {
    Binding binding = bindingGroup.getBinding("bindingExecutionTable");
    binding.unbind();
    fExecutionsInfos.clear();
    fExecutionsInfos.addAll(getBuffer());
    binding.bind();
    TableColumnModel columnModel = tableExecution.getColumnModel();
    columnModel.getColumn(COL_TIME).setCellRenderer(getDateCellRenderer());
    columnModel.getColumn(COL_EXEC_TIME).setCellRenderer(fNumberCellRenderer);
    tableExecution.repaint();
  }
  
  protected List<RESTWebServiceInfo> getBuffer() {
    return getRESTWebServicesView().getExecutionHistoryInfoBuffer();
  }

  @Override
  protected List<? extends IExecutionInfo> getTableModelList() {
    return fExecutionsInfos;
  }

  @Override
  protected int getDefaultSortColumnIndex() {
    return COL_TIME;
  }
}
