package ch.ivyteam.ivy.visualvm.view.restwebservice;

import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import java.util.List;

public class RESTWebServiceSlowExecutionPanel extends RESTWebServiceExecutionHistoryPanel {

  public RESTWebServiceSlowExecutionPanel(RESTWebServicesView view) {
    super(view);
  }

  @Override
  protected List<RESTWebServiceInfo> getBuffer() {
    return getRESTWebServicesView().getSlowExecutionInfoBuffer();
  }

  @Override
  protected int getDefaultSortColumnIndex() {
    return COL_EXEC_TIME;
  }
  
}
