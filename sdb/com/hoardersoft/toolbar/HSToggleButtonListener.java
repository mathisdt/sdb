package com.hoardersoft.toolbar;

import com.hoardersoft.util.HSBeanUtil;

import javax.swing.border.*;
import javax.swing.*;

import java.awt.event.*;

/**
 * Class used for listeners on toggle buttons.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSToggleButtonListener extends MouseAdapter {
    private static Border m_empty;
    private static Border m_raised;
    private static Border m_lowered;

    /**
     * Class constructor.
     */
    public HSToggleButtonListener() {
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
        HSToggleButton button = (HSToggleButton) e.getSource();

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
        HSToggleButton button = (HSToggleButton) e.getSource();

        setNormalBorder(button, false);
    }

    /**
     * Sets the "normal" border on the toggle button for a mouse event.
     *
     * @param e the mouse event
     */
    public static void setNormalBorder(MouseEvent e) {
        HSToggleButton button = (HSToggleButton) e.getSource();

        setNormalBorder(button, button.contains(e.getX(), e.getY()));
    }

    /**
     * Sets the "normal" border on the toggle button.
     *
     * @param toggleButton the toggle button (HSToggleButton)
     * @param over whether the mouse is currently over the toggle button
     */
    public static void setNormalBorder(HSToggleButton toggleButton, boolean over) {
        if (toggleButton.isEnabled() && over) {
            if (toggleButton.isSelected()) {
                toggleButton.setBorder(m_lowered);
            }
            else {
                toggleButton.setBorder(m_raised);
            }
        }
        else {
            if (toggleButton.isSelected()) {
                toggleButton.setBorder(m_lowered);
            }
            else {
                toggleButton.setBorder(m_empty);
            }
        }
    }
}
