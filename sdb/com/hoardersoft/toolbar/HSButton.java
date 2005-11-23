package com.hoardersoft.toolbar;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Class used for buttons.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSButton extends JButton {
    private static Dimension m_buttonSize   = new Dimension(22, 22);
    private static MouseListener m_listener = new HSButtonListener();

    /**
     * Class constructor.
     */
    public HSButton() {
        super();

        buttonSetup();
    }

    /**
     * Constructor that takes an action.
     *
     * @param action the action for the button
     */
    public HSButton(Action action) {
        super(action);

        buttonSetup();
    }

    /**
     * Constructor that takes an icon.
     *
     * @param icon the icon for the button
     */
    public HSButton(Icon icon) {
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
        HSButtonListener.setNormalBorder(this, false);
    }

    /**
     * Sets up the button.
     */
    private void buttonSetup() {
        setPreferredSize(m_buttonSize);
        setFocusPainted(false);
        setToolTipText(getText());

        if (getIcon() != null) {
            setText("");
        }

        HSButtonListener.setNormalBorder(this, false);
        addMouseListener(m_listener);
    }
}
