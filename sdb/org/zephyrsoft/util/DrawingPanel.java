package org.zephyrsoft.util;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

/** A window with a custom paintComponent method. Used to
 *  illustrate that you can make a general-purpose method
 *  that can print any component, regardless of whether
 *  that component performs custom drawing.
 *  See the PrintUtilities class for the printComponent method
 *  that lets you print an arbitrary component with a single
 *  function call.
 *  7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class DrawingPanel extends JPanel {
  private int fontSize = 90;
  private String message = "Java 2D"; //$NON-NLS-1$
  private int messageWidth;
  
  public DrawingPanel() {
    setBackground(Color.white);
    Font font = new Font("Serif", Font.PLAIN, fontSize); //$NON-NLS-1$
    setFont(font);
    FontMetrics metrics = getFontMetrics(font);
    messageWidth = metrics.stringWidth(message);
    int width = messageWidth*5/3;
    int height = fontSize*3;
    setPreferredSize(new Dimension(width, height));
  }

  /** Draws a black string with a tall angled "shadow"
   *  of the string behind it.
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    int x = messageWidth/10;
    int y = fontSize*5/2;
    g2d.translate(x, y);
    g2d.setPaint(Color.lightGray);
    AffineTransform origTransform = g2d.getTransform();
    g2d.shear(-0.95, 0);
    g2d.scale(1, 3);
    g2d.drawString(message, 0, 0);
    g2d.setTransform(origTransform);
    g2d.setPaint(Color.black);
    g2d.drawString(message, 0, 0);
  }
}
