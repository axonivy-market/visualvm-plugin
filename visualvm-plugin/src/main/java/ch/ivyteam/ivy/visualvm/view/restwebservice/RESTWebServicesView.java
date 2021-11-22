package ch.ivyteam.ivy.visualvm.view.restwebservice;

import ch.ivyteam.ivy.visualvm.ContentProvider;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent.DetailsView;

import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.model.RESTWebServiceInfo;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTErrorExecutionBuffer;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTExecutionHistoryBuffer;
import ch.ivyteam.ivy.visualvm.service.restwebservice.RESTSlowExecutiontBuffer;
import ch.ivyteam.ivy.visualvm.util.DataUtils;
import ch.ivyteam.ivy.visualvm.view.AbtractWebServicesView;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.util.List;
import javax.swing.JPanel;

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
  private RESTExecutionHistoryBuffer fExecutionHistoryBuffer;

  public RESTWebServicesView(DataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fExecutionHistoryBuffer = new RESTExecutionHistoryBuffer(dataBeanProvider.getMBeanServerConnection());
    fSlowExecutionBuffer = new RESTSlowExecutiontBuffer(dataBeanProvider.getMBeanServerConnection());
    fErrorExecutionBuffer = new RESTErrorExecutionBuffer(dataBeanProvider.getMBeanServerConnection());
    registerScheduledUpdate(fExecutionHistoryBuffer);
    registerScheduledUpdate(fSlowExecutionBuffer);
    registerScheduledUpdate(fErrorExecutionBuffer);
  }

  @Override
  protected String getWebServiceNamePattern() {
    return IvyJmxConstant.IvyServer.RESTWebService.NAME_PATTERN;
  }

  @Override
  protected void createPanels(DataViewComponent viewComponent) {
    JPanel panel = (JPanel) viewComponent.getComponent(0);
    panel.remove(0);
    viewComponent.configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(null, false), DataViewComponent.TOP_LEFT);

    fExecutionHistoryPanel = new RESTWebServiceExecutionHistoryPanel(this);
    fSlowExecutionPanel = new RESTWebServiceSlowExecutionPanel(this);
    fErrorExecutionPanel = new RESTWebServiceErrorExecutionPanel(this);
    chartsDetailsTab = new DataViewComponent.DetailsView(CHARTS, null, 10, getUIChartsPanel(), null);
    DetailsView callsHistoryTab = new DataViewComponent.DetailsView(CALLS_HISTORY, null, 10, fExecutionHistoryPanel, null);
    DetailsView slowestCallsTab = new DataViewComponent.DetailsView(SLOW_CALLS, null, 10, fSlowExecutionPanel, null);
    DetailsView errorCallsTab = new DataViewComponent.DetailsView(ERRORS, null, 10, fErrorExecutionPanel, null);

    viewComponent.addDetailsView(chartsDetailsTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(callsHistoryTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(slowestCallsTab, DataViewComponent.TOP_LEFT);
    viewComponent.addDetailsView(errorCallsTab, DataViewComponent.TOP_LEFT);
  }

  @Override
  protected void updateConfigTreeNodes() {
    getUIChartsPanel().setTreeData(DataUtils.getRESTWebServicesConfigs(getDataBeanProvider().getMBeanServerConnection()));
  }

  @Override
  public void updateDisplay(QueryResult queryResult) {
    super.updateDisplay(queryResult);
    if (!fExecutionHistoryPanel.isLoaded() && !getExecutionHistoryInfoBuffer().isEmpty()) {
      fExecutionHistoryPanel.refresh();
    }

    if (!fSlowExecutionPanel.isLoaded() && !getSlowExecutionInfoBuffer().isEmpty()) {
      fSlowExecutionPanel.refresh();
    }

    if (!fErrorExecutionPanel.isLoaded() && !getErrorExecutionInfoBuffer().isEmpty()) {
      fErrorExecutionPanel.refresh();
    }
  }

  public List<RESTWebServiceInfo> getExecutionHistoryInfoBuffer() {
    return fExecutionHistoryBuffer.getBuffer();
  }

  public List<RESTWebServiceInfo> getErrorExecutionInfoBuffer() {
    return fErrorExecutionBuffer.getBuffer();
  }

  public List<RESTWebServiceInfo> getSlowExecutionInfoBuffer() {
    return fSlowExecutionBuffer.getBuffer();
  }

  @Override
  protected DetailsView getChartsDetailsView() {
    return chartsDetailsTab;
  }

}
