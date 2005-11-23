package com.hoardersoft.toolbar;

import javax.swing.border.*;

import java.awt.*;

/**
 * Class used for a light bevel border on buttons.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSLightBevelBorder extends BevelBorder {
    /**
     * Constructor that takes a bevel type.
     *
     * @param bevelType the bevel type
     */
    public HSLightBevelBorder(int bevelType) {
        super(bevelType);
    }

    /**
     * Override the painting of a raised bevel border.
     *
     * @param c the component
     * @param g the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     */
    protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h          = height;
        int w          = width;

        g.translate(x, y);
        g.setColor(getHighlightInnerColor(c));

        // Draw left
        g.drawLine(1, 1, 1, h - 2);

        // Draw top
        g.drawLine(1, 1, w - 1, 1);
        g.setColor(getShadowInnerColor(c));

        // Draw bottom
        g.drawLine(1, h - 2, w - 1, h - 2);

        // Draw right
        g.drawLine(w - 1, 1, w - 1, h - 1);
        g.translate(-x, -y);
        g.setColor(oldColor);
    }

    /**
     * Override the painting of a lowered bevel border.
     *
     * @param c the component
     * @param g the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     */
    protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h          = height;
        int w          = width;

        g.translate(x, y);
        g.setColor(getShadowInnerColor(c));

        // Draw Left
        g.drawLine(0, 0, 0, h - 1);

        // Draw Top
        g.drawLine(1, 0, w - 1, 0);
        g.setColor(getHighlightInnerColor(c));

        // Draw bottom
        g.drawLine(2, h - 2, w - 2, h - 2);

        // Draw right
        g.drawLine(w - 1, 1, w - 1, h - 1);
        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
