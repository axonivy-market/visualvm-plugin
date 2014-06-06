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
    super(application, "Xpert.ivy", new ImageIcon(ImageUtilities.loadImage(IvyView.IVY_IMAGE_PATH, true)).
            getImage(), 60, false);
  }

  @Override
  protected DataViewComponent createComponent() {
    JLabel label = new JLabel(
            "<html><div style='text-align:center;padding-bottom:50px'><font color='#F38630' size='3'>"
            + "You are using a version of Xpert.ivy before 5.1.<br/>"
            + "We are sorry but the Xpert.ivy VisualVM plugin only supports Xpert.ivy 5.1 and younger, "
            + "please update if you want to use the plugin."
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