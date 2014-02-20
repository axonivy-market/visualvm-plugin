package ch.ivyteam.ivy.visualvm.view;

import javax.swing.JComponent;

public class LicenseView extends AbstractView {

  private final LicenseInformationPanel fLicenseInformationPanel;

  public LicenseView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
    fLicenseInformationPanel = new LicenseInformationPanel();
    fLicenseInformationPanel.readInformation(getDataBeanProvider().getMBeanServerConnection());
  }

  @Override
  protected String getMasterViewTitle() {
    return "License Information";
  }

  @Override
  protected JComponent getMasterViewComponent() {
    return fLicenseInformationPanel;
  }

}
