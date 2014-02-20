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

public class RequestViewNew extends AbstractView {

  private static final String REQUESTS = "Requests";
  private static final String ERRORS = "Errors";
  private static final String SESSIONS = "Sessions";
  private boolean uiComplete;

  public RequestViewNew(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createRequestView() {
    ChartsPanel requestChart = new ChartsPanel();
    ChartsPanel errorChart = new ChartsPanel();
    ChartsPanel processingTimeChart = new ChartsPanel();
    ChartsPanel sessionChart = new ChartsPanel();

    MChartDataSource requestDataSource = new MChartDataSource(REQUESTS, null, REQUESTS);
    MChartDataSource errorDataSource = new MChartDataSource(ERRORS, null, ERRORS);
    MChartDataSource processingTimeDataSource = new MChartDataSource("Processing Time", null, "Time [ms]");
    for (ObjectName processorName : getTomcatRequestProcessors()) {
      String protocol = getProtocol(processorName);
      requestDataSource.addDeltaSerie(protocol, processorName, "requestCount");
      errorDataSource.addDeltaSerie(protocol, processorName, "errorCount");
      processingTimeDataSource.addDeltaSerie(protocol, processorName, "processingTime");
    }
    requestChart.addChart(requestDataSource);
    errorChart.addChart(errorDataSource);
    processingTimeChart.addChart(processingTimeDataSource);
    MChartDataSource sessionDataSource = new MChartDataSource(SESSIONS, null, SESSIONS);
    ObjectName tomcatManager = getTomcatManagerName();
    if (tomcatManager != null) {
      sessionDataSource.addSerie("Http", SerieStyle.LINE, tomcatManager, "sessionCounter");
    }
    sessionDataSource.addSerie("Ivy", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            "sessions");
    sessionDataSource.addSerie("Licensed", SerieStyle.LINE, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            "licensedSessions");
    sessionChart.addChart(sessionDataSource);
    getUpdatableUIObjects().add(requestChart);
    getUpdatableUIObjects().add(errorChart);
    getUpdatableUIObjects().add(processingTimeChart);
    getUpdatableUIObjects().add(sessionChart);

    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Requests", null, 10,
            requestChart.getUiComponent(), null), DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Errors", null, 10, errorChart.
            getUiComponent(), null), DataViewComponent.TOP_RIGHT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Processing Time", null, 10,
            processingTimeChart.getUiComponent(), null), DataViewComponent.BOTTOM_LEFT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView(SESSIONS, null, 10,
            sessionChart.getUiComponent(), null), DataViewComponent.BOTTOM_RIGHT);
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
      return getDataBeanProvider().getMBeanServerConnection().queryNames(new ObjectName(
              "*:type=GlobalRequestProcessor,name=*"), null);
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
    Set<ObjectName> tomcatManagers = MUtil.queryNames(getDataBeanProvider().getMBeanServerConnection(),
            "*:type=Manager,context=*,host=localhost");
    if (tomcatManagers.size() >= 1) {
      return tomcatManagers.iterator().next();
    }
    return null;
  }

}
