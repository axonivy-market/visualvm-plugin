package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.Query;
import ch.ivyteam.ivy.visualvm.chart.QueryResult;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

public class AbstractView {

  public static final String BR = "<br>";
  private final DataBeanProvider fDataBeanProvider;
  private DataViewComponent fViewComponent;
  private final List<IUpdatableUIObject> fUpdatableUIObjects;

  public AbstractView(DataBeanProvider dataBeanProvider) {
    fDataBeanProvider = dataBeanProvider;
    fUpdatableUIObjects = new ArrayList<>();
    ToolTipManager.sharedInstance().setInitialDelay(200);
    ToolTipManager.sharedInstance().setDismissDelay(99999);
  }

  public DataBeanProvider getDataBeanProvider() {
    return fDataBeanProvider;
  }

  public DataViewComponent getViewComponent() {
    if (fViewComponent == null) {
      // Add the master view and configuration view to the component:
      DataViewComponent.MasterView masterView = new DataViewComponent.MasterView(getMasterViewTitle(),
              "",
              getMasterViewComponent());
      // Configuration of master view:
      DataViewComponent.MasterViewConfiguration masterConfig = new DataViewComponent.MasterViewConfiguration(
              false);
      fViewComponent = new DataViewComponent(masterView, masterConfig);
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
    // use array to avoid concurrent modification exception
    IUpdatableUIObject[] array = fUpdatableUIObjects.toArray(new IUpdatableUIObject[]{});
    for (IUpdatableUIObject updatableUIObj : array) {
      updatableUIObj.updateQuery(query);
    }
    QueryResult result = query.execute(serverConnection);
    for (IUpdatableUIObject updatableUIObj : array) {
      updatableUIObj.updateValues(result);
    }
  }

  public void registerScheduledUpdate(IUpdatableUIObject updatableObject) {
    fUpdatableUIObjects.add(updatableObject);
  }

  public void unregisterScheduledUpdate(IUpdatableUIObject updatableObject) {
    fUpdatableUIObjects.remove(updatableObject);
  }

  /**
   * Disable close ability for an area with specified position on viewer
   *
   * @param viewer   the viewer
   * @param position the position on view
   * @see DataViewComponent#configureDetailsArea(
   * com.sun.tools.visualvm.core.ui.components.DataViewComponent.DetailsAreaConfiguration, int)
   */
  public void disableCloseAbilityArea(DataViewComponent viewer, int position) {
    viewer.configureDetailsArea(
            new DataViewComponent.DetailsAreaConfiguration(null, false), position);
  }

}
