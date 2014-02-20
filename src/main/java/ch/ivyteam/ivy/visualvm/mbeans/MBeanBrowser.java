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
class MBeanBrowser implements IMBeanBrowser {
  private final MBeanServerConnection fMBeanServerConnection;

  public MBeanBrowser(MBeanServerConnection mBeanServerConnection) {
    fMBeanServerConnection = mBeanServerConnection;
  }

  @Override
  public String browse(String mBean) {
    MBeanBrowserTopComponent mBeanBrowser = new MBeanBrowserTopComponent();
    mBeanBrowser.setMBeanTreeModel(new MBeanTreeModel(fMBeanServerConnection));
    mBeanBrowser.setSelected(mBean, null);

    NotifyDescriptor descriptor = new NotifyDescriptor(mBeanBrowser, "New chart",
            NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null,
            NotifyDescriptor.YES_OPTION);

    if (DialogDisplayer.getDefault().notify(descriptor) == NotifyDescriptor.OK_OPTION) {
      return mBeanBrowser.getSelectedMBean();
    }
    return null;
  }

}
