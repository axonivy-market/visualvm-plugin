package ch.ivyteam.ivy.visualvm.view.soapwebservice;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.AbtractWebServicesView;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;

public class SOAPWebServicesView extends AbtractWebServicesView {

  public SOAPWebServicesView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  @Override
  protected String getWebServiceNamePattern() {
    return IvyJmxConstant.IvyServer.SOAPWebService.NAME_PATTERN;
  }

  @Override
  protected void createPanels(DataViewComponent viewComponent) {
    viewComponent.add(getUIChartsPanel());
  }

  @Override
  protected void updateConfigTreeNodes() {
    getUIChartsPanel().setTreeData(DataUtils.getSOAPWebServicesConfigs(getDataBeanProvider().getMBeanServerConnection()));
  }

}
