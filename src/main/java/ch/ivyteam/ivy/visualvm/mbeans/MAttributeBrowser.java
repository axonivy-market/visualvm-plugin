/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.mbeans;

import javax.management.MBeanServerConnection;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author rwei
 */
class MAttributeBrowser implements IMAttributeBrowser {
  private final MBeanServerConnection mBeanServerConnection;

  public MAttributeBrowser(MBeanServerConnection mBeanServerConnection) {
    this.mBeanServerConnection = mBeanServerConnection;
  }

  @Override
  public String[] browse(String mBeanName, String[] attributes) {
    MAttributeBrowserTopComponent mAttributeBrowser = new MAttributeBrowserTopComponent();
    mAttributeBrowser.setAttributeListModel(new MAttributeListModel(mBeanServerConnection, mBeanName));
    mAttributeBrowser.setSelectedAttributes(attributes);

    NotifyDescriptor descriptor = new NotifyDescriptor(mAttributeBrowser, "New chart",
            NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null,
            NotifyDescriptor.YES_OPTION);

    if (DialogDisplayer.getDefault().notify(descriptor) == NotifyDescriptor.OK_OPTION) {
      return mAttributeBrowser.getSelectedAttributes();
    }
    return null;

  }

}
