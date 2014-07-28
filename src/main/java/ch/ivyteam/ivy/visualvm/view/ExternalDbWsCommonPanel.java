/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.lang.StringUtils;
import org.openide.util.ImageUtilities;

@SuppressWarnings({"serial", "PMD.SingularField"})
public class ExternalDbWsCommonPanel extends javax.swing.JPanel {

  private static final String APP_ICON_PATH = "resources/icons/app_icon.png";
  private static final String ENV_ICON_PATH = "resources/icons/env_icon.png";
  private static final String CONF_ICON_PATH = "resources/icons/db_icon.png";
  private static final String DB_RECORDING_ICON_PATH = "resources/icons/db_conf_recording_icon.png";
  protected static final String WS_ICON_PATH = "resources/icons/web_services_icon.png";
  protected static final String WS_RECORDING_ICON_PATH = "resources/icons/ws_recording_icon.png";

  /**
   * icon for application node
   */
  private final Icon fAppIcon;
  /**
   * icon for environment node
   */
  private final Icon fEnvIcon;
  /**
   * icon for configuration node
   */
  private Icon fConfWsIcon;
  /**
   * icon for selected configuration node
   */
  private Icon fRecordingIcon;
  /**
   * data model for the tree
   */
  private final DefaultTreeModel fEnvTreeModel;
  /**
   * the root node
   */
  private final AppEnvConfigNode fRootNode;
  /**
   * the main view to interact when selection changed
   */
  private final ExternalDbWsCommonView fExternalDbWsView;
  /**
   * store the selected path when the tree collapses or expands
   */
  private TreePath[] fSelectedPath;
  /**
   * resize to 20% the width of whole panel at init time
   */
  private boolean fResized = false;
  /**
   * the tree data
   */
  private Map<String, Map<String, Set<String>>> fAppEnvConfigWsMap;
  /**
   * To store the tree leaves, get it out when needed to avoid navigating the tree
   */
  private final Map<String, AppEnvConfigNode> fLeaves;

  /**
   * Creates new form ExternalDbPanel
   */
  public ExternalDbWsCommonPanel(ExternalDbWsCommonView externalDbView) {
    fExternalDbWsView = externalDbView;
    fAppIcon = (Icon) ImageUtilities.loadImage(APP_ICON_PATH, true);
    fEnvIcon = (Icon) ImageUtilities.loadImage(ENV_ICON_PATH, true);
    fConfWsIcon = (Icon) ImageUtilities.loadImage(CONF_ICON_PATH, true);
    fRecordingIcon = (Icon) ImageUtilities.loadImage(DB_RECORDING_ICON_PATH, true);
    fLeaves = new HashMap<>();
    // need to init data models before initialization of the tree
    fRootNode = new AppEnvConfigNode("Server", fAppIcon);
    fEnvTreeModel = new DefaultTreeModel(fRootNode);
    initComponents();
    initTree();
    addResizeSplitpanesListener();
  }

  // CHECKSTYLE:OFF
  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
   * code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings({"PMD", "rawtypes", "unchecked"})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    mainSplitpane = new javax.swing.JSplitPane();
    jPanel5 = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    envJTree = new javax.swing.JTree();

    setLayout(new java.awt.GridBagLayout());

    mainSplitpane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    mainSplitpane.setDividerLocation(120);

    jPanel5.setBackground(new java.awt.Color(255, 255, 255));

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 310, Short.MAX_VALUE)
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 298, Short.MAX_VALUE)
    );

    mainSplitpane.setRightComponent(jPanel5);

    jPanel1.setBackground(new java.awt.Color(255, 255, 255));
    jPanel1.setLayout(new java.awt.GridBagLayout());

    jScrollPane1.setBorder(null);

    envJTree.setModel(fEnvTreeModel);
    envJTree.setRootVisible(false);
    jScrollPane1.setViewportView(envJTree);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    jPanel1.add(jScrollPane1, gridBagConstraints);

    mainSplitpane.setLeftComponent(jPanel1);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(mainSplitpane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTree envJTree;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JSplitPane mainSplitpane;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON
  // the view will call this method to change the charts
  void setChartPanelToVisible(ChartsPanel externalDbChartPanel) {
    int oldDividerLoc = mainSplitpane.getDividerLocation();
    mainSplitpane.setRightComponent(externalDbChartPanel.getUIComponent());
    mainSplitpane.setDividerLocation(oldDividerLoc);
  }

  // init data for the tree
  public void setTreeData(Map<String, Map<String, Set<String>>> appEnvConfMap) {
    fAppEnvConfigWsMap = appEnvConfMap;
    // reset the current tree
    for (int i = 0; i < fRootNode.getChildCount(); i++) {
      fEnvTreeModel.removeNodeFromParent((MutableTreeNode) fRootNode.getChildAt(i));
    }
    initTreeData();
    expandAllTreeNodes();
  }

  public void refreshOpenedNodes() {
    Set<String> createdChartKeySet = fExternalDbWsView.getCreatedChartKeySet();
    for (String key : createdChartKeySet) {
      AppEnvConfigNode node = fLeaves.get(key);
      if (node != null) {
        node.setIsOpened(true);
      }
    }
  }

  private void initTreeData() {
    fLeaves.clear();
    // update new data
    int index = 0;
    for (String appName : fAppEnvConfigWsMap.keySet()) {
      AppEnvConfigNode appNode = new AppEnvConfigNode(appName, fAppIcon);
      fEnvTreeModel.insertNodeInto(appNode, fRootNode, index++);
      int envIndex = 0;
      for (String env : fAppEnvConfigWsMap.get(appName).keySet()) {
        AppEnvConfigNode envNode = new AppEnvConfigNode(env, fEnvIcon);
        fEnvTreeModel.insertNodeInto(envNode, appNode, envIndex++);
        int configIndex = 0;
        for (String config : fAppEnvConfigWsMap.get(appName).get(env)) {
          AppEnvConfigNode confNode = new AppEnvConfigNode(config, fConfWsIcon);
          fEnvTreeModel.insertNodeInto(confNode, envNode, configIndex++);
          fLeaves.put(appName + "_" + env + "_" + config, confNode);
        }
      }
    }
    fEnvTreeModel.reload();
  }

  private void initTree() {
    envJTree.setCellRenderer(new EnvTreeCellRenderer());
    envJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    envJTree.setShowsRootHandles(true);
    addMouseClickListener();
    addKeyListener();
    keepSelectionWhenCollapse();
  }

  private void addKeyListener() {
    envJTree.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent e) {
        TreePath selectedPath = envJTree.getSelectionPath();
        if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedPath != null) {
          AppEnvConfigNode node = (AppEnvConfigNode) selectedPath.getLastPathComponent();
          createChartsPanel(node);
        }
      }

    });
  }

  private void addMouseClickListener() {
    envJTree.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        TreePath clickedPath = envJTree.getPathForLocation(e.getX(), e.getY());
        if (clickedPath != null) {
          AppEnvConfigNode node = (AppEnvConfigNode) clickedPath.getLastPathComponent();
          if (node != null && (node.isNodeOpened() || isDoubleClick(e))) {
            createChartsPanel(node);
          }
        }
      }

      private boolean isDoubleClick(MouseEvent e) {
        return SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1;
      }

    });
  }

  private void keepSelectionWhenCollapse() {
    envJTree.addTreeWillExpandListener(new TreeWillExpandListener() {
      @Override
      public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        envJTree.setSelectionPaths(fSelectedPath);
      }

      @Override
      public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        fSelectedPath = envJTree.getSelectionPaths();
      }

    });
  }

  private void expandAllTreeNodes() {
    for (int i = 0; i < envJTree.getRowCount(); ++i) {
      envJTree.expandRow(i);
    }
  }

  private void addResizeSplitpanesListener() {
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        if (!fResized) {
          mainSplitpane.setDividerLocation((int) (getSize().getWidth() / 5));
          fResized = true;
        }
      }

    });
  }

  private void createChartsPanel(AppEnvConfigNode node) {
    if (node != null && node.isLeaf()) {
      String appName = node.getParent().getParent().toString();
      String envName = node.getParent().toString();
      fExternalDbWsView.fireCreateChartsAction(appName, envName, node.getRealName());
      node.setIsOpened(true);
    }
  }

  void setWsIcon(Icon wsIcon) {
    fConfWsIcon = wsIcon;
  }

  void setRecordingIcon(Icon recordIcon) {
    fRecordingIcon = recordIcon;
  }

  public void setSelectedNode(String appName, String envName, String confWsName) {
    AppEnvConfigNode confWsNode = fLeaves.get(appName + "_" + envName + "_" + confWsName);
    if (confWsNode != null) {
      envJTree.setSelectionPath(new TreePath(confWsNode.getPath()));
    }
  }

  public boolean containsNode(String appName, String envName, String confWsName) {
    return fLeaves.get(appName + "_" + envName + "_" + confWsName) != null;
  }

  private class AppEnvConfigNode extends DefaultMutableTreeNode {

    private final Icon fNodeIcon;
    private boolean fNodeOpened;
    private final String fRealNodeText;

    public AppEnvConfigNode(Object userObject, Icon icon) {
      fRealNodeText = (String) userObject;
      setUserObject(cutNodeTextToView());
      fNodeIcon = icon;
      setAllowsChildren(true);
    }

    public Icon getNodeIcon() {
      return fNodeIcon;
    }

    private void setIsOpened(boolean opened) {
      fNodeOpened = opened;
    }

    private boolean isNodeOpened() {
      return fNodeOpened;
    }

    public String getRealName() {
      return fRealNodeText;
    }

    private String cutNodeTextToView() {
      String cut = StringUtils.substringBetween(fRealNodeText, "\"", " (");
      if (StringUtils.isEmpty(cut)) {
        return fRealNodeText;
      } else {
        return cut;
      }
    }

  }

  private class EnvTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
      JLabel resultLabel
              = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

      if (value instanceof AppEnvConfigNode) {
        AppEnvConfigNode node = (AppEnvConfigNode) value;
        resultLabel.setIcon(node.getNodeIcon());
        resultLabel.setText((String) node.getUserObject());
        if (node.isNodeOpened()) {
          resultLabel.setIcon(fRecordingIcon);
        }
      }
      return resultLabel;
    }

  }
}
