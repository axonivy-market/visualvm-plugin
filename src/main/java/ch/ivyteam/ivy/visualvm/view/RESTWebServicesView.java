/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.panel.webservices.rest.CallsHistoryPanel;
import ch.ivyteam.ivy.visualvm.panel.webservices.rest.SlowestCallsPanel;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import static com.sun.tools.visualvm.core.ui.components.DataViewComponent.DetailsView;
import java.util.Map;
import java.util.Set;

public class RESTWebServicesView extends AbtractWebServicesView {

  private CallsHistoryPanel fCallsHistoryPanel;
  private SlowestCallsPanel fSlowestCallsPanel;

  public RESTWebServicesView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  @Override
  protected Map<String, Map<String, Set<String>>> getWebServicesConfigs() {
    return DataUtils.getRESTWebServicesConfigs(getDataBeanProvider().getMBeanServerConnection());
  }

  @Override
  protected String getWebServiceNamePattern() {
    return IvyJmxConstant.IvyServer.RESTWebService.NAME_PATTERN;
  }

  @Override
  protected void addPanelsToView(DataViewComponent viewComponent) {
    viewComponent.configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null, false), DataViewComponent.TOP_LEFT);

    fCallsHistoryPanel = new CallsHistoryPanel();
    fSlowestCallsPanel = new SlowestCallsPanel();
    DetailsView chartsDetailsTab = new DataViewComponent.DetailsView("Charts", null, 10, getUIChartsPanel(), null);
    DetailsView callsHistoryTab = new DataViewComponent.DetailsView("Calls History", null, 10, fCallsHistoryPanel, null);
    DetailsView slowestCallsTab = new DataViewComponent.DetailsView("Slowest Calls", null, 10, fSlowestCallsPanel, null);

    viewComponent.addDetailsView(chartsDetailsTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(callsHistoryTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(slowestCallsTab, DataViewComponent.TOP_LEFT);
  }

}
