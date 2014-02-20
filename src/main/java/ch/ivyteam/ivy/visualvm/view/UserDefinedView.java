package ch.ivyteam.ivy.visualvm.view;

import ch.ivyteam.ivy.visualvm.chart.ChartsPanel;
import ch.ivyteam.ivy.visualvm.chart.NewMChartTopComponent;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

public class UserDefinedView extends AbstractView {

  private boolean uiComplete;
  private ChartsPanel chartsPanel;

  public UserDefinedView(IDataBeanProvider dataBeanProvider) {
    super(dataBeanProvider);
  }

  private void createUserDefinedView() {
    super.getViewComponent().configureDetailsArea(new DataViewComponent.DetailsAreaConfiguration(
            "User Defined", false),
            DataViewComponent.BOTTOM_RIGHT);

    chartsPanel = new ChartsPanel();
    getUpdatableUIObjects().add(chartsPanel);

    final JButton addChartButton = new JButton("Add ...");
    addChartButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        addChart();
      }

    });

    // Add detail views to the component:
    super.getViewComponent().addDetailsView(new DataViewComponent.DetailsView("User Defined", null, 10,
            chartsPanel.
            getUiComponent(), new JComponent[]{addChartButton}), DataViewComponent.BOTTOM_RIGHT);
  }

  @Override
  public DataViewComponent getViewComponent() {
    DataViewComponent viewComponent = super.getViewComponent();
    JPanel panel = (JPanel) viewComponent.getComponent(0);
    if (panel != null) {
      panel.remove(0);
    }
    if (!uiComplete) {
      createUserDefinedView();
      uiComplete = true;
    }
    return viewComponent;
  }

  private void addChart() {
    NewMChartTopComponent newMChart = new NewMChartTopComponent();
    newMChart.setMBeanServerConnection(getDataBeanProvider().getMBeanServerConnection());
    NotifyDescriptor descriptor = new NotifyDescriptor(newMChart, "New chart",
            NotifyDescriptor.OK_CANCEL_OPTION,
            NotifyDescriptor.PLAIN_MESSAGE, null,
            NotifyDescriptor.YES_OPTION);

    if (DialogDisplayer.getDefault().notify(descriptor) == NotifyDescriptor.OK_OPTION) {
      chartsPanel.addChart(newMChart.getChartDataSource());
    }
  }

}
