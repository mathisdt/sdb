package com.hoardersoft.toolbar;

import com.hoardersoft.util.HSBeanUtil;

import javax.swing.border.*;
import javax.swing.*;

import java.awt.event.*;

/**
 * Class used for listeners on buttons.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSButtonListener extends MouseAdapter {
    private static Border m_empty;
    private static Border m_raised;
    private static Border m_lowered;

    /**
     * Class constructor.
     */
    public HSButtonListener() {
        if (HSBeanUtil.getLookAndFeel().isPlastic()) {
            m_empty   = new EmptyBorder(5, 5, 5, 5);
            m_raised  = BorderFactory.createEtchedBorder();
            m_lowered = BorderFactory.createEtchedBorder();
        }
        else {
            m_empty   = new EmptyBorder(5, 5, 5, 5);
            m_raised  = new HSLightBevelBorder(HSLightBevelBorder.RAISED);
            m_lowered = new HSLightBevelBorder(HSLightBevelBorder.LOWERED);
        }
    }

    /**
     * Called when the mouse is clicked.
     *
     * @param e the mouse event
     */
    public void mouseClicked(MouseEvent e) {
        setNormalBorder(e);
    }

    /**
     * Called when the mouse is pressed.
     *
     * @param e the mouse event
     */
    public void mousePressed(MouseEvent e) {
        HSButton button = (HSButton) e.getSource();

        if (button.isEnabled()) {
            button.setBorder(m_lowered);
        }
    }

    /**
     * Called when the mouse is released.
     *
     * @param e the mouse event
     */
    public void mouseReleased(MouseEvent e) {
        setNormalBorder(e);
    }

    /**
     * Called when the mouse enters.
     *
     * @param e the mouse event
     */
    public void mouseEntered(MouseEvent e) {
        setNormalBorder(e);
    }

    /**
     * Called when the mouse exits.
     *
     * @param e the mouse event
     */
    public void mouseExited(MouseEvent e) {
        HSButton button = (HSButton) e.getSource();

        setNormalBorder(button, false);
    }

    /**
     * Sets the "normal" border on the button for a mouse event.
     *
     * @param e the mouse event
     */
    public static void setNormalBorder(MouseEvent e) {
        HSButton button = (HSButton) e.getSource();

        setNormalBorder(button, button.contains(e.getX(), e.getY()));
    }

    /**
     * Sets the "normal" border on the button.
     *
     * @param button the button (HSButton)
     * @param over whether the mouse is currently over the button
     */
    public static void setNormalBorder(HSButton button, boolean over) {
        if (button.isEnabled() && over) {
            button.setBorder(m_raised);
        }
        else {
            button.setBorder(m_empty);
        }
    }
}
