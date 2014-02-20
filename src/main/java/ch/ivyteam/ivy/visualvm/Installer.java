/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

  @Override
  public void restored() {
    IvyViewProvider.initialize();
    IvyApplicationTypeFactory.initialize();
  }

  @Override
  public void uninstalled() {
    IvyApplicationTypeFactory.unregister();
    IvyViewProvider.unregister();
  }

}
