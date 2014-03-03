package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.swing.JComponent;

public class AbstractView {

  private final IDataBeanProvider fDataBeanProvider;
  private DataViewComponent fViewComponent;
  private final List<IUpdatableUIObject> fUpdatableUIObjects;

  public AbstractView(IDataBeanProvider dataBeanProvider) {
    fDataBeanProvider = dataBeanProvider;
    fUpdatableUIObjects = new ArrayList<>();
  }

  public IDataBeanProvider getDataBeanProvider() {
    return fDataBeanProvider;
  }

  public DataViewComponent getViewComponent() {
    if (fViewComponent == null) {
      // Add the master view and configuration view to the component:
      DataViewComponent.MasterView masterView = new DataViewComponent.MasterView(getMasterViewTitle(), "",
              getMasterViewComponent());
      // Configuration of master view:
      DataViewComponent.MasterViewConfiguration masterConfiguration
              = new DataViewComponent.MasterViewConfiguration(false);
      fViewComponent = new DataViewComponent(masterView, masterConfiguration);
    }
    return fViewComponent;
  }

  protected String getMasterViewTitle() {
    return "";
  }

  protected JComponent getMasterViewComponent() {
    return null;
  }

  public void update() {
    MBeanServerConnection serverConnection = getDataBeanProvider().getMBeanServerConnection();
    Query query = new Query();
    for (IUpdatableUIObject updatableUIObj : fUpdatableUIObjects) {
      updatableUIObj.updateQuery(query);
    }
    QueryResult result = query.execute(serverConnection);
    for (IUpdatableUIObject updatableUIObj : fUpdatableUIObjects) {
      updatableUIObj.updateValues(result);
    }
  }

  public void registerScheduledUpdate(IUpdatableUIObject updatableObject) {
    fUpdatableUIObjects.add(updatableObject);
  }

}
