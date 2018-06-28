package ch.ivyteam.ivy.visualvm.view.externaldb;

import ch.ivyteam.ivy.visualvm.model.IExecutionInfo;
import ch.ivyteam.ivy.visualvm.view.common.MultiLineMergeTableCellRenderer;
import ch.ivyteam.ivy.visualvm.view.common.AbstractTablePanel;

@SuppressWarnings("serial")
public abstract class AbstractExternalDbQueriesPanel extends AbstractTablePanel {

  private final ExternalDbView fExternalDbView;
  private final MultiLineMergeTableCellRenderer fMultiLineRenderer = new MultiLineMergeTableCellRenderer();

  public AbstractExternalDbQueriesPanel(ExternalDbView externalDbView) {
    fExternalDbView = externalDbView;
  }

  protected ExternalDbView getExternalDbView() {
    return fExternalDbView;
  }

  @Override
  protected void executeDoubleClick(IExecutionInfo info) {
    getExternalDbView().showChart(info.getApplication(), info.getEnvironment(), info.getConfigName());
  }

  public MultiLineMergeTableCellRenderer getMultiLineCellRenderer() {
    return fMultiLineRenderer;
  }

}
