package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.Map;
import java.util.Set;

public class SOAPWebServicesView extends AbtractWebServicesView {

  public SOAPWebServicesView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  @Override
  protected Map<String, Map<String, Set<String>>> getWebServicesConfigs() {
    return DataUtils.getSOAPWebServicesConfigs(getDataBeanProvider().getMBeanServerConnection());
  }

  @Override
  protected String getWebServiceNamePattern() {
    return IvyJmxConstant.IvyServer.SOAPWebService.NAME_PATTERN;
  }

  @Override
  protected void addPanelsToView(DataViewComponent viewComponent) {
    viewComponent.add(getUIChartsPanel());
  }

}
