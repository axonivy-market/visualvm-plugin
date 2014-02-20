/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

import ch.ivyteam.ivy.visualvm.MUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author rwei
 */
class MBeanTreeModel extends DefaultTreeModel {

  private MBeanServerConnection mBeanServerConnection;

  public MBeanTreeModel(MBeanServerConnection mBeanServerConnection) {
    super(new MBeanNode());
    try {
      this.mBeanServerConnection = mBeanServerConnection;
      Set<ObjectName> names = mBeanServerConnection.queryNames(
              ObjectName.WILDCARD, null);
      for (ObjectName name : names) {
        List<String> nameTokens = getNameTokens(name);
        createNodes(nameTokens, name);
      }
    } catch (Exception ex) {
    }
  }

  private List<String> getNameTokens(ObjectName name) {
    List<String> tokens = new ArrayList<String>();
    tokens.add(name.getDomain());
    for (String property : name.getKeyPropertyListString().split(",")) {
      int pos = property.lastIndexOf("=");
      tokens.add(property.substring(pos + 1, property.length()));
    }
    return tokens;
  }

  private void createNodes(List<String> nameTokens, ObjectName name)
          throws Exception {
    MBeanNode rootNode = (MBeanNode) getRoot();
    createNodes(rootNode, nameTokens, name);
  }

  private void createNodes(MBeanNode parentNode, List<String> nameTokens,
          ObjectName name) throws Exception {
    String nodeName = nameTokens.remove(0);
    MBeanNode node = findNode(parentNode, nodeName);
    if (node == null) {
      if (nameTokens.isEmpty()) {
        node = new MBeanNode(nodeName, name);
        createAttributeNodes(node);
        parentNode.add(node);
        return;
      } else {
        node = new MBeanNode(nodeName);
        parentNode.add(node);
      }
    }
    if (nameTokens.isEmpty()) {
      node.setObjectName(name);
      createAttributeNodes(node);
    } else {
      createNodes(node, nameTokens, name);
    }
  }

  private void createAttributeNodes(MBeanNode parentNode) throws Exception {
    MBeanInfo beanInfo = mBeanServerConnection.getMBeanInfo(MUtil
            .createObjectName(parentNode.getMBeanName()));
    for (MBeanAttributeInfo attribute : beanInfo.getAttributes()) {
      MBeanNode attributeNode = new MBeanNode(parentNode.getObjectName(),
              attribute.getName());
      parentNode.add(attributeNode);
    }
  }

  private MBeanNode findNode(MBeanNode parentNode, String nodeName) {
    for (int pos = 0; pos < parentNode.getChildCount(); pos++) {
      MBeanNode child = (MBeanNode) parentNode.getChildAt(pos);
      if (child.hasName(nodeName)) {
        return child;
      }
    }
    return null;
  }

  private MBeanNode findNode(List<String> nameTokens) {
    return findNode((MBeanNode) getRoot(), nameTokens);
  }

  private MBeanNode findNode(MBeanNode parentNode, List<String> nameTokens) {
    if (nameTokens.isEmpty()) {
      return parentNode;
    }
    String name = nameTokens.remove(0);
    MBeanNode node = findNode(parentNode, name);
    if (node == null) {
      return parentNode;
    }
    return findNode(node, nameTokens);
  }

  TreePath getTreePathFor(String mBean, String attribute) {
    try {
      ObjectName name;
      name = new ObjectName(mBean);
      List<String> nameTokens = getNameTokens(name);
      if (attribute == null || attribute.trim().isEmpty()) {
        nameTokens.add(attribute);
      }
      MBeanNode node = findNode(nameTokens);
      if (node == null) {
        return null;
      }
      return new TreePath(node.getPath());
    } catch (MalformedObjectNameException ex) {
      return null;
    }

  }

}
