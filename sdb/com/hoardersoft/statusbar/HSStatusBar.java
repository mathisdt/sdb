package com.hoardersoft.statusbar;

import javax.swing.*;

import java.awt.*;

/**
 * Class used for status bars.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSStatusBar extends JPanel implements HSStatusBarListener {
    private JLabel m_label;

    /**
     * Class constructor.
     */
    public HSStatusBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));

        m_label = new JLabel("");

        add(m_label);
    }

    /**
     * Informs the listener that the status has changed.
     *
     * @param status the status text
     */
    public void status(String status) {
        m_label.setText(status);
    }
}
