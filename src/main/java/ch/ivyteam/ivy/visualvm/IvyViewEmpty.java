package ch.ivyteam.ivy.visualvm;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class IvyViewEmpty extends DataSourceView {

  public IvyViewEmpty(Application application) {
    super(application, IvyViewHelper.getViewName(application), IvyViewHelper.getViewIcon(application), 60,
            false);
  }

  @Override
  protected DataViewComponent createComponent() {
    JLabel label = new JLabel(
            "<html><div style='text-align:center;padding-bottom:50px;line-height:120%'>"
            + "<font color='#EF6C00' size='5'>"
            + ContentProvider.getFormatted("UnsupportedIvyVersionServerNotification")
            + "</font>"
            + "</div></html>");
    label.setVerticalAlignment(SwingConstants.CENTER);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setHorizontalTextPosition(SwingConstants.CENTER);

    DataViewComponent.MasterView masterView = new DataViewComponent.MasterView("", "", null);
    DataViewComponent.MasterViewConfiguration masterConfiguration
            = new DataViewComponent.MasterViewConfiguration(false);
    DataViewComponent dataViewComponent = new DataViewComponent(masterView, masterConfiguration);

    dataViewComponent.add(label);

    return dataViewComponent;
  }

}
