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
  private ObjectName fObjectName;
  private String fAttribute;

  MBeanNode() {
    this(null);
  }

  MBeanNode(String name) {
    this(name, null);
  }

  MBeanNode(String name, ObjectName objectName) {
    super(name == null ? null : name.trim());
    this.fObjectName = objectName;
  }

  MBeanNode(ObjectName objectName, String attribute) {
    super(attribute);
    fObjectName = objectName;
    fAttribute = attribute;
  }

  boolean isMBean() {
    return fObjectName != null && fAttribute == null;
  }

  boolean isAttribute() {
    return fAttribute != null;
  }

  boolean hasName(String name) {
    return getUserObject().equals(name.trim());
  }

  void setObjectName(ObjectName objectName) {
    fObjectName = objectName;
  }

  String getMBeanName() {
    return fObjectName == null ? null : fObjectName.toString();
  }

  ObjectName getObjectName() {
    return fObjectName;
  }

  String getAttribute() {
    return fAttribute;
  }

}
