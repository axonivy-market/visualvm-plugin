package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.MUtil;
import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.MChartDataSource;
import ch.ivyteam.ivy.visualvm.chart.SerieStyle;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.openide.util.Exceptions;

public class RequestView extends AbstractView {

  private static final String REQUESTS = "Requests";
  private static final String ERRORS = "Errors";
  private static final String SESSIONS = "Sessions";
  private boolean uiComplete;

  public RequestView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    super.getViewComponent().configureDetailsArea(
            new DataViewComponent.DetailsAreaConfiguration(REQUESTS, false),
            DataViewComponent.TOP_RIGHT);
    ChartsPanel requestPanel = new ChartsPanel();

    MChartDataSource requestDataSource = new MChartDataSource(REQUESTS,
                                                              null, REQUESTS);
    MChartDataSource errorDataSource = new MChartDataSource(ERRORS, null,
                                                            ERRORS);
    MChartDataSource processingTimeDataSource = new MChartDataSource(
            "Processing Time", null, "Time [ms]");
    for (ObjectName processorName : getTomcatRequestProcessors()) {
      String protocol = getProtocol(processorName);
      requestDataSource.addDeltaSerie(protocol, processorName,
                                      "requestCount");
      errorDataSource
              .addDeltaSerie(protocol, processorName, "errorCount");
      processingTimeDataSource.addDeltaSerie(protocol, processorName,
                                             "processingTime");
    }
    requestPanel.addChart(requestDataSource);
    requestPanel.addChart(errorDataSource);
    requestPanel.addChart(processingTimeDataSource);
    MChartDataSource sessionDataSource = new MChartDataSource(SESSIONS,
                                                              null, SESSIONS);
    ObjectName tomcatManager = getTomcatManagerName();
    if (tomcatManager != null) {
      sessionDataSource.addSerie("Http", SerieStyle.LINE, tomcatManager,
                                 "sessionCounter");
    }
    sessionDataSource.addSerie("Ivy", SerieStyle.LINE,
                               IvyJmxConstant.IvyServer.SecurityManager.NAME, "sessions");
    sessionDataSource.addSerie("Licensed", SerieStyle.LINE,
                               IvyJmxConstant.IvyServer.SecurityManager.NAME, "licensedSessions");
    requestPanel.addChart(sessionDataSource);

    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(REQUESTS, null,
                                                                              10, requestPanel.
            getUiComponent(),
                                                                              null),
                                            DataViewComponent.TOP_RIGHT);
    getUpdatableUIObjects().add(requestPanel);
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      createRequestView();
      uiComplete = true;
    }
    return viewComponent;
  }

  private Set<ObjectName> getTomcatRequestProcessors() {
    try {
      return getDataBeanProvider().getMBeanServerConnection().queryNames(
              new ObjectName("*:type=GlobalRequestProcessor,name=*"),
              null);
    } catch (IOException | MalformedObjectNameException ex) {
      Exceptions.printStackTrace(ex);
      return Collections.emptySet();
    }
  }

  private String getProtocol(ObjectName processorName) {
    String protocol = processorName.getKeyProperty("name");
    protocol = protocol.substring(1, protocol.length() - 1);
    protocol = protocol.replace("-bio", "");
    return protocol;
  }

  private ObjectName getTomcatManagerName() {
    Set<ObjectName> tomcatManagers = MUtil.queryNames(
            getDataBeanProvider().getMBeanServerConnection(),
            "*:type=Manager,context=*,host=localhost");
    if (tomcatManagers.size() >= 1) {
      return tomcatManagers.iterator().next();
    }
    return null;
  }

}
