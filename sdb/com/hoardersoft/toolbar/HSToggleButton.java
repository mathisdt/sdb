package com.hoardersoft.toolbar;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Class used for toggle buttons.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSToggleButton extends JToggleButton {
    private static Dimension m_buttonSize   = new Dimension(22, 22);
    private static MouseListener m_listener = new HSToggleButtonListener();

    /**
     * Class constructor.
     */
    public HSToggleButton() {
        super();

        buttonSetup();
    }

    /**
     * Constructor that takes an action.
     *
     * @param action the action for the toggle button
     */
    public HSToggleButton(Action action) {
        super(action);

        buttonSetup();
    }

    /**
     * Constructor that takes an icon.
     *
     * @param icon the icon for the toggle button
     */
    public HSToggleButton(Icon icon) {
        super(icon);

        buttonSetup();
    }

    /**
     * Override the setSelected method to update the border.
     *
     * @param selected whether the button is selected
     */
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        HSToggleButtonListener.setNormalBorder(this, false);
    }

    /**
     * Sets up the toggle button.
     */
    private void buttonSetup() {
        setPreferredSize(m_buttonSize);
        setFocusPainted(false);
        setToolTipText(getText());

        if (getIcon() != null) {
            setText("");
        }

        HSToggleButtonListener.setNormalBorder(this, false);
        addMouseListener(m_listener);
    }
}
