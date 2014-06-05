package ch.ivyteam.ivy.visualvm.view.externaldb;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.JLabel;
import javax.swing.JTable;

class DateTableCellRenderer extends EvenOddCellRenderer {
  private final SimpleDateFormat fDateFormat;

  public DateTableCellRenderer() {
    fDateFormat = new SimpleDateFormat(simpleDateFormatForLocale(Locale.getDefault()) + " HH:mm:ss");
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
    String dateValue = value.toString();
    if (value instanceof Date) {
      dateValue = fDateFormat.format(value);
    }
    return super.getTableCellRendererComponent(table, dateValue, isSelected, hasFocus, row, column);
  }

  public static String simpleDateFormatForLocale(Locale locale) {
    TimeZone commonTimeZone = TimeZone.getTimeZone("UTC");
    Calendar c = Calendar.getInstance(commonTimeZone);
    c.set(3333, Calendar.NOVEMBER, 22);

    DateFormat localeDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    localeDateFormat.setTimeZone(commonTimeZone);
    String dateText = localeDateFormat.format(c.getTime());

    return dateText.replace("11", "MM").replace("22", "dd").replace("3333", "yyyy").replace("33", "yyyy");
  }

}
