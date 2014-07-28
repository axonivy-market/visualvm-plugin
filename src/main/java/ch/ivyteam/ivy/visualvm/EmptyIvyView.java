package ch.ivyteam.ivy.visualvm;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.openide.util.ImageUtilities;

public class EmptyIvyView extends DataSourceView {

  public EmptyIvyView(Application application) {
    super(application, ContentProvider.get("XpertIvy"),
            new ImageIcon(ImageUtilities.loadImage(IvyView.IVY_IMAGE_PATH, true)).getImage(),
            60, false);
  }

  @Override
  protected DataViewComponent createComponent() {
    JLabel label = new JLabel(
            "<html><div style='text-align:center;padding-bottom:50px'><font color='#EF6C00' size='3'>"
            + ContentProvider.getFormatted("UnsupportedIvyVersionServerNotification")
            + "</font></div></html>");
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
