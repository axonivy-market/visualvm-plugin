package ch.ivyteam.ivy.visualvm.view.restwebservice;

import java.util.Map;
import java.util.Set;

import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent.DetailsView;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTErrorExecutionBuffer;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTSlowExecutiontBuffer;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.AbtractWebServicesView;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class RESTWebServicesView extends AbtractWebServicesView {

  private static final String CHARTS = ContentProvider.get("Charts");
  private static final String ERRORS = ContentProvider.get("Errors");
  private static final String SLOW_CALLS = ContentProvider.get("SlowCalls");
  private static final String CALLS_HISTORY = ContentProvider.get("CallsHistory");

  private DetailsView chartsDetailsTab;
  private RESTWebServiceExecutionHistoryPanel fExecutionHistoryPanel;
  private RESTWebServiceSlowExecutionPanel fSlowExecutionPanel;
  private RESTWebServiceErrorExecutionPanel fErrorExecutionPanel;
  private RESTSlowExecutiontBuffer fSlowExecutionBuffer;
  private RESTErrorExecutionBuffer fErrorExecutionBuffer;

  public RESTWebServicesView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fSlowExecutionBuffer = new RESTSlowExecutiontBuffer(dataBeanProvider.getMBeanServerConnection());
    fErrorExecutionBuffer = new RESTErrorExecutionBuffer(dataBeanProvider.getMBeanServerConnection());
    registerScheduledUpdate(fSlowExecutionBuffer);
    registerScheduledUpdate(fErrorExecutionBuffer);
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

    fExecutionHistoryPanel = new RESTWebServiceExecutionHistoryPanel(this);
    fSlowExecutionPanel = new RESTWebServiceSlowExecutionPanel(this);
    fErrorExecutionPanel = new RESTWebServiceErrorExecutionPanel(this);
    DetailsView chartsDetailsTab = new DataViewComponent.DetailsView("Charts", null, 10, getUIChartsPanel(), null);
    DetailsView callsHistoryTab = new DataViewComponent.DetailsView("Calls History", null, 10, fCallsHistoryPanel, null);
    DetailsView slowestCallsTab = new DataViewComponent.DetailsView("Slowest Calls", null, 10, fSlowExecutionPanel, null);
    DetailsView errorCallsTab = new DataViewComponent.DetailsView("Errors", null, 10, fErrorExecutionPanel, null);

    viewComponent.addDetailsView(chartsDetailsTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(callsHistoryTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(slowestCallsTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(errorCallsTab, DataViewComponent.TOP_LEFT);
  }

  @Override
  public void updateDisplay(QueryResult queryResult) {
    super.updateDisplay(queryResult);
    if (!fSlowExecutionPanel.isLoaded() && !fSlowExecutionBuffer.getBuffer().isEmpty()) {
      refreshSlowExecutionTable();
    }
    
    if (!fErrorExecutionPanel.isLoaded() && !fErrorExecutionBuffer.getBuffer().isEmpty()) {
      refreshErrorExecutionTable();
    }
  }

  public void refreshSlowExecutionTable() {
    fSlowExecutionPanel.refresh(fSlowExecutionBuffer.getBuffer());
  }
  
  public void refreshErrorExecutionTable() {
    fErrorExecutionPanel.refresh(fErrorExecutionBuffer.getBuffer());
  }

  public List<RESTWebServiceInfo> getSlowExecutionInfoBuffer() {
    return fSlowExecutionBuffer.getBuffer();
  }

  @Override
  protected DetailsView getChartsDetailsView() {
    return chartsDetailsTab;
  }

}
