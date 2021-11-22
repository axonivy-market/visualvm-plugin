package ch.ivyteam.ivy.visualvm.chart.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class RotateLabel extends JPanel {
  private String fText;
  private final AffineTransform fTransform;
  private double fTheta;
  private Image fIcon;
  private final int fIconTextGap;

  public RotateLabel(String text, Image icon) {
    super();
    fTransform = new AffineTransform();
    fTheta = Math.toRadians(90);
    fTransform.setToRotation(fTheta);
    fText = text;
    fIcon = icon;
    fIconTextGap = 20;
    setBackground(Color.WHITE);
  }

  public String getText() {
    return fText;
  }

  public void setText(String text) {
    this.fText = text;
  }

  @Override
  public String getToolTipText(MouseEvent event) {
    int centerX = getCenterX();
    int centerY = getCenterY();
    Dimension textSize = calculateTextSize();
    Dimension labelSize = calculateSize(textSize);
    Rectangle r = new Rectangle(centerX - labelSize.height / 2, centerY - labelSize.width / 2, 16, 16);
    if (r.contains(event.getPoint())) {
      return super.getToolTipText(event);
    }
    return null;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2 = (Graphics2D) g;
    int centerX = getCenterX();
    int centerY = getCenterY();
    Dimension textSize = calculateTextSize();
    Dimension labelSize = calculateSize(textSize);
    g2.rotate(fTheta, centerX, centerY);
    g2.setColor(getForeground());
    g2.drawImage(getIcon(), centerX - labelSize.width / 2, centerY - labelSize.height / 2, null);
    g2.drawString(fText, centerX - textSize.width / 2, centerY + textSize.height / 2);
  }

  private Dimension calculateSize(Dimension stringBounds) {
    int iconHeight = (getIcon() == null) ? 0 : getIcon().getHeight(null);
    int iconWidth = (getIcon() == null) ? 0 : (getIcon().getWidth(null) + fIconTextGap);
    int height = Math.max(stringBounds.height, iconHeight);
    int width = iconWidth + stringBounds.width;
    return new Dimension(width, height);
  }

  private Dimension calculateTextSize() {
    FontRenderContext context = new FontRenderContext(fTransform, true, false);
    Rectangle2D stringBounds = getFont().getStringBounds(fText, context);
    return new Dimension((int) stringBounds.getWidth(), (int) stringBounds.getHeight());
  }

  public double getTheta() {
    return fTheta;
  }

  protected void setTheta(double theta) {
    if (fTheta != theta) {
      fTheta = theta;
      fTransform.setToRotation(theta);
      repaint();
    }
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension textSize = calculateTextSize();
    return invert(calculateSize(textSize));
  }

  private Dimension invert(Dimension size) {
    return new Dimension(size.height, size.width);
  }

  private int getCenterX() {
    Insets insets = getInsets();
    int width = getWidth() - ((insets != null) ? (insets.left + insets.right) : 0);
    int padX = ((insets != null) ? insets.left : 0);
    return padX + width / 2;
  }

  private int getCenterY() {
    Insets insets = getInsets();
    int height = getHeight() - ((insets != null) ? (insets.top + insets.bottom) : 0);
    int padY = ((insets != null) ? insets.top : 0);
    return padY + height / 2;
  }

  public Image getIcon() {
    return fIcon;
  }

  public void setIcon(Image icon) {
    fIcon = icon;
  }

}
