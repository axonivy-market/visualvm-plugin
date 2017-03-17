package ch.ivyteam.ivy.visualvm.view.restwebservice;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;

public abstract class AbstractRESTWebServiceExecutionPanel extends JPanel {

  private RESTWebServicesView fRESTWebServicesView;
  private boolean fIsRefreshing;
  private boolean fIsLoaded;

  public AbstractRESTWebServiceExecutionPanel(RESTWebServicesView view) {
    fRESTWebServicesView = view;
  }

  public void refresh(List<RESTWebServiceInfo> webServiceInfoList) {
    synchronized (this) {
      if (fIsRefreshing) {
        return;// prevent other threads from calling this method when there is a running thread.
      }
      fIsRefreshing = true;
    }
    refreshQueriesTable(webServiceInfoList);
    setLoaded(true);
    fIsRefreshing = false;
  }

  protected abstract JTable getExecutionsTable();

  protected abstract void refreshQueriesTable(List<RESTWebServiceInfo> webServiceInfoList);

  public boolean isLoaded() {
    return fIsLoaded;
  }

  protected void setLoaded(boolean isLoaded) {
    fIsLoaded = isLoaded;
  }

  public RESTWebServicesView getRESTWebServicesView() {
    return fRESTWebServicesView;
  }
}
