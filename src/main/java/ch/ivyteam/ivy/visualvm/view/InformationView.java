package ch.ivyteam.ivy.visualvm.view;

import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JComponent;

public class InformationView extends AbstractView {

  private final InformationPanelTopComponent fInformationPanel;
  private boolean uiComplete;

  public InformationView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fInformationPanel = createInfoPanel();
  }

  private void createInfoView() {
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("System Database",
            null, 10, fInformationPanel.getSysDbPanel(), null), DataViewComponent.TOP_LEFT);
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("Ports", null, 10,
            fInformationPanel.getConnectorsPanel(), null), DataViewComponent.TOP_RIGHT);
  }

  private InformationPanelTopComponent createInfoPanel() {
    InformationPanelTopComponent infoPanel = new InformationPanelTopComponent();
    infoPanel.readInformation(getDataBeanProvider().getMBeanServerConnection());
    return infoPanel;
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    if (!uiComplete) {
      createInfoView();
      uiComplete = true;
    }
    return viewComponent;
  }

  @Override
  protected String getMasterViewTitle() {
    return "General";
  }

  @Override
  protected JComponent getMasterViewComponent() {
    return fInformationPanel.getGeneralPanel();
  }

}
