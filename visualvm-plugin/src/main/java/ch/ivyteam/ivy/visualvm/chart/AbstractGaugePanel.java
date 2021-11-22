package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import eu.hansolo.steelseries.gauges.AbstractGauge;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.Section;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGaugePanel implements IUpdatableUIObject {
  private final GaugeDataSource fDataSource;
  private final AbstractGauge fGauge;
  private static final Color[] SECTION_COLORS = new Color[]{new Color(139, 195, 74), new Color(251, 140, 0),
    new Color(229, 28, 35)};
  private static final Color DEFAULT_COLOR = Color.GRAY;
  private final List<Double> fThresholds;
  private final List<Section> fSections = new ArrayList<>();

  public AbstractGaugePanel(GaugeDataSource dataSource, AbstractGauge gauge) {
    fDataSource = dataSource;
    fThresholds = dataSource.getThresholds();
    fGauge = gauge;
    initConfigs();
    initGauge();
  }

  private void initConfigs() {
    if (fThresholds.get(1) == fThresholds.get(2)) {
      fSections.add(new Section(fThresholds.get(0), fThresholds.get(1), SECTION_COLORS[0]));
      fSections.add(new Section(fThresholds.get(2), fThresholds.get(3), SECTION_COLORS[2]));
    } else {
      for (int i = 0; i < SECTION_COLORS.length; i++) {
        Color sectionColor = SECTION_COLORS[i] == null ? DEFAULT_COLOR : SECTION_COLORS[i];
        fSections.add(new Section(fThresholds.get(i), fThresholds.get(i + 1), sectionColor));
      }
    }
  }

  private void initGauge() {
    fGauge.setFrameVisible(false);
    fGauge.setBackgroundVisible(false);
    fGauge.setBackgroundColor(BackgroundColor.TRANSPARENT);

    fGauge.setTitle("");
    fGauge.setUnitString("");
    fGauge.setLedVisible(false);

    if (!fSections.isEmpty()) {
      fGauge.setMinValue(((Section) fSections.get(0)).getStart());
      fGauge.setMaxValue(((Section) fSections.get(fSections.size() - 1)).getStop());

      for (Section section : fSections) {
        fGauge.addSection(section);
      }
    }

    fGauge.setSectionsVisible(true);
    fGauge.setNiceScale(true);
    fGauge.setAreas(fSections.toArray(new Section[fSections.size()]));
    fGauge.setSectionTickmarksOnly(false);
    fGauge.setMinorTickmarkVisible(false);

  }

  @Override
  public void updateValues(QueryResult result) {
    double values = fDataSource.getValue(result);
    fGauge.setValue(values);
  }

  @Override
  public void updateQuery(Query query) {
    fDataSource.updateQuery(query);
  }

  public AbstractGauge getUI() {
    return fGauge;
  }

  public List<Double> getThresholds() {
    return fThresholds;
  }

}
