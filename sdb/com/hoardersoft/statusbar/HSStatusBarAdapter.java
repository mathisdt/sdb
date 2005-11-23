package com.hoardersoft.statusbar;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Status bar adapter.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSStatusBarAdapter extends MouseAdapter {
    private HSStatusBarListener m_statusListener;

    /**
     * Class constructor.
     */
    public HSStatusBarAdapter() {
        super();
    }

    /**
     * Class constructor that takes a status listener.
     *
     * @param statusBarListener the status bar listener (AXStatusBarListener) (null for none)
     */
    public HSStatusBarAdapter(HSStatusBarListener statusBarListener) {
        super();

        m_statusListener = statusBarListener;
    }

    /**
     * Overrides the mouse entered method.
     *
     * @param e the mouse event (MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        Component c = e.getComponent();
        Action a    = null;

        if (c instanceof AbstractButton) {
            a = ((AbstractButton) c).getAction();
        }

        if ((a != null) && (m_statusListener != null)) {
            // Try long description, short description and then name for status text
            String status = null;
            Object value  = a.getValue(Action.LONG_DESCRIPTION);

            if (value == null) {
                value = a.getValue(Action.SHORT_DESCRIPTION);
            }

            if (value == null) {
                value = a.getValue(Action.NAME);
            }

            if (value != null) {
                status = value.toString();
            }

            m_statusListener.status((status != null) ? status : "");
        }
    }

    /**
     * Overrides the mouse exited method.
     *
     * @param e the mouse event (MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        clearStatus();
    }

    /**
     * Overrides the mouse exited method.
     *
     * @param e the mouse event (MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        clearStatus();
    }

    /**
     * Overrides the mouse released method.
     *
     * @param e the mouse event (MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        clearStatus();
    }

    /**
     * Gets the status listener property.
     *
     * @return the status listener property (null for none)
     */
    public HSStatusBarListener getStatusListener() {
        return m_statusListener;
    }

    /**
     * Sets the status listener property.
     *
     * @param statusListener the status listener property (null for none)
     */
    public void setStatusListener(HSStatusBarListener statusListener) {
        m_statusListener = statusListener;
    }

    /**
     * Clears the status text.
     */
    private void clearStatus() {
        if (m_statusListener != null) {
            m_statusListener.status("");
        }
    }
}
