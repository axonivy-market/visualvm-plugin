/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

/**
 *
 * @author rwei
 */
public interface IMAttributeBrowser {
  String[] browse(String mBeanName, String[] attributes);

}
