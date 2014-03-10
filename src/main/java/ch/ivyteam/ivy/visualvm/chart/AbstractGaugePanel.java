package ch.ivyteam.ivy.visualvm.chart;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.view.IUpdatableUIObject;
import eu.hansolo.steelseries.gauges.AbstractGauge;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.Section;
import eu.hansolo.steelseries.tools.TickmarkType;
import java.awt.Color;
import java.util.List;

public abstract class AbstractGaugePanel implements IUpdatableUIObject {
  private final GaugeDataSource fDataSource;
  private final AbstractGauge fGauge;
  private static final Color[] SECTION_COLORS = new Color[]{new Color(58, 228, 103), new Color(225, 228, 90),
    new Color(255, 114, 102)};
  private static final Color DEFAULT_COLOR = Color.GRAY;
  private final List<Double> fThresHolds;

  public AbstractGaugePanel(GaugeDataSource dataSource, AbstractGauge gauge) {
    fDataSource = dataSource;
    fThresHolds = dataSource.getThresHolds();
    fGauge = gauge;
    initGauge();
  }

  private void initGauge() {
    fGauge.setTitle("");
    fGauge.setUnitString("");
    fGauge.setFrameVisible(false);
    fGauge.setBackgroundVisible(false);
    fGauge.setBackgroundColor(BackgroundColor.TRANSPARENT);
    fGauge.setLedVisible(false);
    setSections();
  }

  private void setSections() {
    if (!fThresHolds.isEmpty()) {
      fGauge.setMinValue(fThresHolds.get(0));
      fGauge.setMaxValue(fThresHolds.get(fThresHolds.size() - 1));
      for (int i = 0; i < fThresHolds.size() - 1; i++) {
        Color sectionColor = SECTION_COLORS[i] != null ? SECTION_COLORS[i] : DEFAULT_COLOR;
        fGauge.addSection(new Section(fThresHolds.get(i), fThresHolds.get(i + 1), sectionColor));
      }
    }
    fGauge.setSectionsVisible(true);
    fGauge.setNiceScale(false);

    if (!getThresHolds().isEmpty()) {
      double delta = getThresHolds().get(getThresHolds().size() - 1) - getThresHolds().get(0);
      int digit = (int) Math.floor(Math.log10(delta));

      double tickSpacing = chooseGaugeTickSpacing(Double.valueOf(delta), digit).doubleValue();
      fGauge.setMinorTickSpacing(tickSpacing / 10.0D);
      fGauge.setMinorTickmarkType(TickmarkType.LINE);
      fGauge.setMajorTickSpacing(tickSpacing);
      fGauge.setMajorTickmarkType(TickmarkType.TRIANGLE);
    }
  }

  private Double chooseGaugeTickSpacing(Double range, int digit) {
    double d = 1.0D;
    if (digit == -2) {
      d = 0.01D;
    } else if (digit == -1) {
      d = 0.1D;
    } else if (digit == 0) {
      d = 1.0D;
    } else if (digit > 0) {
      d = calculateGaugeTickSpacing(range, digit).doubleValue();
    }
    return Double.valueOf(d);
  }

  private Double calculateGaugeTickSpacing(Double range, int digit) {
    double round = Math.round(range.doubleValue());
    int tenPower = (int) Math.pow(10.0D, digit);
    double fraction = round / tenPower;
    double tickSpacing;
    if (digit < 3) {
      tickSpacing = Math.round(fraction) * tenPower / 10L;
    } else {
      tickSpacing = Math.ceil(fraction) * tenPower / 10.0D;
    }
    return Double.valueOf(tickSpacing);
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

  public List<Double> getThresHolds() {
    return fThresHolds;
  }

}
