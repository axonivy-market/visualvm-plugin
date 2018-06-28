package ch.ivyteam.ivy.visualvm.view.restwebservice;

import ch.ivyteam.ivy.visualvm.model.IExecutionInfo;
import ch.ivyteam.ivy.visualvm.view.common.AbstractTablePanel;

public abstract class AbstractRESTWebServiceExecutionPanel extends AbstractTablePanel {
  
  private RESTWebServicesView fRESTWebServicesView;
  
  public AbstractRESTWebServiceExecutionPanel(RESTWebServicesView view) {
    fRESTWebServicesView = view;
  }
  
  public RESTWebServicesView getRESTWebServicesView() {
    return fRESTWebServicesView;
  }
  
  @Override
  protected void executeDoubleClick(IExecutionInfo info) {
    fRESTWebServicesView.showChart(info.getApplication(), info.getEnvironment(), info.getConfigName());
  }
  
}
