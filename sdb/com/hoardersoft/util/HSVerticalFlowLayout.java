package com.hoardersoft.util;

import java.awt.*;
import java.io.*;

/**
 * Implements a vertical flow layout. Very similar to the one provided
 * with JBuilder and Comedia Beans package (looks like they were both
 * taken from the same source :)).
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSVerticalFlowLayout extends FlowLayout implements Serializable {
    /** Vertically align to the top of the container. */
    public static final int TOP = 0;

    /** Vertically align to the middle of the container. */
    public static final int MIDDLE = 1;

    /** Vertically align to the bottom of the container. */
    public static final int BOTTOM = 2;

    // Private members
    private int m_hgap;
    private int m_vgap;
    private boolean m_hfill;
    private boolean m_vfill;

    /**
     * Constructs this layout with default properties.
     */
    public HSVerticalFlowLayout() {
        this(TOP, 5, 5, true, false);
    }

    /**
     * Constructs this layout with specified fill properties.
     *
     * @param hfill whether to fill horizontally
     * @param vfill whether to fill vertically
     */
    public HSVerticalFlowLayout(boolean hfill, boolean vfill) {
        this(TOP, 5, 5, hfill, vfill);
    }

    /**
     * Constructs this layout with specified alignment.
     *
     * @param align the alignment value
     */
    public HSVerticalFlowLayout(int align) {
        this(align, 5, 5, true, false);
    }

    /**
     * Constructs this layout with specified alignment and fill properties.
     *
     * @param align the alignment value
     * @param hfill whether to fill horizontally
     * @param vfill whether to fill vertically
     */
    public HSVerticalFlowLayout(int align, boolean hfill, boolean vfill) {
        this(align, 5, 5, hfill, vfill);
    }

    /**
     * Constructs this layout with specified alignment and gap properties.
     *
     * @param align the alignment value
     * @param hgap the horizontal gap
     * @param vgap the vertical gap
     */
    public HSVerticalFlowLayout(int align, int hgap, int vgap) {
        this(align, hgap, vgap, true, false);
    }

    /**
     * Constructs this layout with specified gap properties.
     *
     * @param hgap the horizontal gap
     * @param vgap the vertical gap
     */
    public HSVerticalFlowLayout(int hgap, int vgap) {
        this(TOP, hgap, vgap, true, false);
    }

    /**
     * Constructs this layout with specified alignment, gap and fill properties.
     *
     * @param align the alignment value
     * @param hgap the horizontal gap
     * @param vgap the vertical gap
     * @param hfill whether to fill horizontally
     * @param vfill whether to fill vertically
     */
    public HSVerticalFlowLayout(int align, int hgap, int vgap, boolean hfill, boolean vfill) {
        setAlignment(align);

        m_hgap  = hgap;
        m_vgap  = vgap;
        m_hfill = hfill;
        m_vfill = vfill;
    }

    /**
     * Gets the horizontal gap.
     *
     * @return the horizontal gap
     */
    public int getHgap() {
        return m_hgap;
    }

    /**
     * Sets the horizontal gap.
     *
     * @param hgap the horizontal gap
     */
    public void setHgap(int hgap) {
        super.setHgap(hgap);

        m_hgap = hgap;
    }

    /**
     * Gets the vertical gap.
     *
     * @return the vertical gap
     */
    public int getVgap() {
        return m_vgap;
    }

    /**
     * Sets the vertical gap.
     *
     * @param vgap the vertical gap
     */
    public void setVgap(int vgap) {
        super.setVgap(vgap);

        m_vgap = vgap;
    }

    /**
     * Gets whether to fill vertically.
     *
     * @return whether to fill vertically
     */
    public boolean getVerticalFill() {
        return m_vfill;
    }

    /**
     * Sets whether to fill vertically.
     *
     * @param vfill whether to fill vertically
     */
    public void setVerticalFill(boolean vfill) {
        m_vfill = vfill;
    }

    /**
     * Gets whether to fill horizontally.
     *
     * @return whether to fill horizontally
     */
    public boolean getHorizontalFill() {
        return m_hfill;
    }

    /**
     * Sets whether to fill horizontally.
     *
     * @param hfill whether to fill horizontally
     */
    public void setHorizontalFill(boolean hfill) {
        m_hfill = hfill;
    }

    /**
     * Returns the preferred dimensions given the components in the target container.
     *
     * @param target the target container to lay out
     * @return the preferred dimensions after laying out the target container
     */
    public Dimension preferredLayoutSize(Container target) {
        Dimension size = new Dimension(0, 0);

        for (int i = 0; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);

            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();

                size.width = Math.max(size.width, d.width);

                if (i > 0) {
                    size.height += m_vgap;
                }

                size.height += d.height;
            }
        }

        Insets insets = target.getInsets();

        size.width  += insets.left + insets.right + m_hgap * 2;
        size.height += insets.top + insets.bottom + m_vgap * 2;

        return size;
    }

    /**
     * Returns the minimum dimensions given the components in the target container.
     *
     * @param target the target container to lay out
     * @return the minimum dimensions after laying out the target container
     */
    public Dimension minimumLayoutSize(Container target) {
        Dimension size = new Dimension(0, 0);

        for (int i = 0; i < target.getComponentCount(); i++) {
            Component current = target.getComponent(i);

            if (current.isVisible()) {
                Dimension d = current.getMinimumSize();

                size.width = Math.max(size.width, d.width);

                if (i > 0) {
                    size.height += m_vgap;
                }

                size.height += d.height;
            }
        }

        Insets insets = target.getInsets();

        size.width  += insets.left + insets.right + m_hgap * 2;
        size.height += insets.top + insets.bottom + m_vgap * 2;

        return size;
    }

    /**
     * Arranges the components first to last within the target container using the bounding box defined.
     *
     * @param target the target container to lay out
     * @param x the x coordinate of the bounding box
     * @param y the y coordinate of the bounding box
     * @param width the width of the bounding box
     * @param height the height of the bounding box
     * @param first the index of the first component to lay out
     * @param last the index of the last component to lay out
     */
    private void arrange(Container target, int x, int y, int width, int height, int first, int last) {
        int align = getAlignment();

        if (align == MIDDLE) {
            y += height / 2;
        }

        if (align == BOTTOM) {
            y += height;
        }

        for (int i = first; i < last; i++) {
            Component current = target.getComponent(i);
            Dimension d       = current.getSize();

            if (current.isVisible()) {
                int px = x + (width - d.width) / 2;

                current.setLocation(px, y);

                y += m_vgap + d.height;
            }
        }
    }

    /**
     * Lays out the components in the target container.
     *
     * @param target the container to lay out.
     */
    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        int maxheight = target.getSize().height - (insets.top + insets.bottom + m_vgap * 2);
        int maxwidth  = target.getSize().width - (insets.left + insets.right + m_hgap * 2);
        int n         = target.getComponentCount();
        int x         = insets.left + m_hgap;
        int y         = 0;
        int colw      = 0;
        int start     = 0;

        for (int i = 0; i < n; i++) {
            Component current = target.getComponent(i);

            if (current.isVisible()) {
                Dimension d = current.getPreferredSize();

                // Fit the last component to the remaining height
                if ((m_vfill) && (i == (n - 1))) {
                    d.height = Math.max((maxheight - y), current.getPreferredSize().height);
                }

                // Fit the componenent size to the container width
                if (m_hfill) {
                    current.setSize(maxwidth, d.height);

                    d.width = maxwidth;
                }
                else {
                    current.setSize(d.width, d.height);
                }

                if (y + d.height > maxheight) {
                    arrange(target, x, insets.top + m_vgap, colw, maxheight - y, start, i);

                    y     = d.height;
                    x     += m_hgap + colw;
                    colw  = d.width;
                    start = i;
                }
                else {
                    if (y > 0) {
                        y += m_vgap;
                    }

                    y    += d.height;
                    colw = Math.max(colw, d.width);
                }
            }
        }

        arrange(target, x, insets.top + m_vgap, colw, maxheight - y, start, n);
    }
}
