/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.test.datasource.externaldb;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonPanel;
import ch.ivyteam.ivy.visualvm.view.ExternalDbWsCommonView;

public class ExternalDbWsCommonPanelTest {
  private ExternalDbWsCommonPanel externaPanel;
  private Map<String, Map<String, Set<String>>> appEnvConfMap;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    externaPanel = new ExternalDbWsCommonPanel(mock(ExternalDbWsCommonView.class));
    appEnvConfMap = new HashMap<>();
  }

  @Test
  public void testSetTreeDataMethodWhenUpdating() {
    Set<String> configs1 = new HashSet<>();
    configs1.add("DBConfig1");
    configs1.add("DBConfig2");
    configs1.add("DBConfig3");

    Set<String> configs2 = new HashSet<>();
    configs2.add("DBConfig1");
    configs2.add("DBConfig3");

    Map<String, Set<String>> env1 = new HashMap<>();
    env1.put("Default", configs1);
    Map<String, Set<String>> env2 = new HashMap<>();
    env2.put("Default", configs2);

    appEnvConfMap.put("app1", env1);
    appEnvConfMap.put("app2", env2);

    externaPanel.setTreeData(appEnvConfMap);

    Assert.assertNotNull(externaPanel.getRootNode());

    assertEquals("+ Server+ app2+ app1+ Default+ Default  - DBConfig1  "
            + "- DBConfig3  - DBConfig1  - DBConfig2  - DBConfig3", mapToString(externaPanel.getRootNode()));

    configs2.add("DBConfig2");
    externaPanel.setTreeData(appEnvConfMap);

    assertEquals("+ Server+ app2+ app1+ Default+ Default  - DBConfig1  "
            + "- DBConfig2  - DBConfig3  - DBConfig1  - DBConfig2  - DBConfig3",
            mapToString(externaPanel.getRootNode()));
  }

  @Test
  public void testSetTreeMethodWithNullMap() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("The parameter must be not null");
    externaPanel.setTreeData(null);
  }

  @Test
  public void testSetTreeMethodWithEmptyMap() {
    externaPanel.setTreeData(new HashMap<String, Map<String, Set<String>>>());
    assertEquals("- Server", mapToString(externaPanel.getRootNode()).trim());
  }

  private String mapToString(DefaultMutableTreeNode rootNode) {
    StringBuilder expectedResult = new StringBuilder();
    @SuppressWarnings("unchecked")
    Enumeration<DefaultMutableTreeNode> en1 = rootNode.breadthFirstEnumeration();
    while (en1.hasMoreElements()) {
      DefaultMutableTreeNode node = en1.nextElement();
      TreeNode[] path = node.getPath();
      String strNode = (node.isLeaf() ? "  - " : "+ ") + path[path.length - 1];
      expectedResult.append(strNode);
    }
    return expectedResult.toString();
  }
}
