/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

import javax.management.ObjectName;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author rwei
 */
class MBeanNode extends DefaultMutableTreeNode {
  private ObjectName objectName;
  private String attribute;

  MBeanNode() {
    this(null);
  }

  MBeanNode(String name) {
    this(name, null);
  }

  MBeanNode(String name, ObjectName objectName) {
    super(name == null ? null : name.trim());
    this.objectName = objectName;
  }

  MBeanNode(ObjectName objectName, String attribute) {
    super(attribute);
    this.objectName = objectName;
    this.attribute = attribute;
  }

  boolean isMBean() {
    return objectName != null && attribute == null;
  }

  boolean isAttribute() {
    return attribute != null;
  }

  boolean hasName(String name) {
    return getUserObject().equals(name.trim());
  }

  void setObjectName(ObjectName objectName) {
    this.objectName = objectName;
  }

  String getMBeanName() {
    return objectName == null ? null : objectName.toString();
  }

  ObjectName getObjectName() {
    return objectName;
  }

  String getAttribute() {
    return attribute;
  }

}
