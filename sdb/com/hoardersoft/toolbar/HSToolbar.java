package com.hoardersoft.toolbar;

import com.hoardersoft.statusbar.*;
import com.hoardersoft.util.*;

import javax.swing.*;

import java.awt.*;
import java.util.*;

/**
 * Hoardersoft toolbar component. Main advantage over a JToolBar is the ability
 * to add toggle actions which are kept in sync with related toggle actions
 * (with respect to selection status) and operate within a specific button
 * group. Also configures toolbar buttons as we want them and supports a status
 * bar listener. Why doesn't Swing handle any of this for you ? Also much more
 * Windows look-and-feel than the standard 1.3 JToolBar.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSToolbar extends JPanel {
    private final static int DEFAULT_HEIGHT = 23;
    private HSStatusBarAdapter m_statusBarAdapter;
    private HashMap m_groups;

    /**
     * Class constructor.
     */
    public HSToolbar() {
        this(null);
    }

    /**
     * Class constructor that takes a status listener.
     *
     * @param statusBarListener the status listener (HSStatusBarListener)
     */
    public HSToolbar(HSStatusBarListener statusBarListener) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // Create a new status bar adapter
        m_statusBarAdapter = new HSStatusBarAdapter(statusBarListener);

        // Set our preferred size
        setPreferredSize(new Dimension(-1, DEFAULT_HEIGHT));

        // Add a separator at the start - looks nicer !!
        addSeparator();
    }

    /**
     * Adds an action to the toolbar.
     *
     * @param action the action to add to the toolbar
     * @return the added button (JButton)
     */
    public JButton add(Action action) {
        HSButton button = new HSButton(action);

        super.add(button);
        initButton(button);

        return button;
    }

    /**
     * Adds a toggle action to the toolbar.
     *
     * @param action the toggle action to add to the toolbar
     * @return the added button (JToggleButton)
     */
    public JToggleButton addToggle(Action action) {
        return addToggle(action, null, false);
    }

    /**
     * Adds a toggle action to the toolbar.
     *
     * @param action the toggle action to add to the toolbar
     * @param selected whether the toggle is selected
     * @return the added button (JToggleButton)
     */
    public JToggleButton addToggle(Action action, boolean selected) {
        return addToggle(action, null, selected);
    }

    /**
     * Adds a toggle action to the toolbar.
     *
     * @param action the toggle action to add to the toolbar
     * @param group the group name for the toggle (null for none)
     * @return the added button (JToggleButton)
     */
    public JToggleButton addToggle(Action action, String group) {
        return addToggle(action, group, false);
    }

    /**
     * Adds a toggle action to the toolbar.
     *
     * @param action the toggle action to add to the toolbar
     * @param group the group name for the toggle (null for none)
     * @param selected whether the toggle is selected
     * @return the added button (JToggleButton)
     */
    public JToggleButton addToggle(Action action, String group, boolean selected) {
        HSToggleButton button = new HSToggleButton(action);

        super.add(button);
        initButton(button);

        // Add to the group
        if (group != null) {
            getButtonGroup(group).add(button);
        }

        // Add our related button
        HSBeanUtil.addRelatedButton(action, button, selected);

        return button;
    }

    /**
     * Adds a separator to the toolbar.
     */
    public void addSeparator() {
        super.add(Box.createHorizontalStrut(5));
    }

    /**
     * Adds a divider to the toolbar.
     */
    public void addDivider() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {
            public void paintComponent(Graphics g) {
                g.setColor(Color.gray);
                g.drawLine(1, 0, 1, 23);
                g.setColor(Color.white);
                g.drawLine(2, 0, 2, 23);
            }
        };

        panel.setPreferredSize(new Dimension(5, 24));
        panel.setMinimumSize(new Dimension(5, 24));
        add(panel);
    }

    /**
     * Adds a gap to the toolbar.
     *
     * @param size the size of the gap
     */
    public void addGap(int size) {
        add(Box.createRigidArea(new Dimension(size, size)));
    }

    /**
     * Adds a glue to the toolbar.
     */
    public void addGlue() {
        add(Box.createGlue());
    }

    /**
     * Gets the status listener property.
     *
     * @return the status listener property (null for none)
     */
    public HSStatusBarListener getStatusListener() {
        return m_statusBarAdapter.getStatusListener();
    }

    /**
     * Sets the status listener property.
     *
     * @param statusListener the status listener property (null for none)
     */
    public void setStatusListener(HSStatusBarListener statusListener) {
        m_statusBarAdapter.setStatusListener(statusListener);
    }

    /**
     * Returns the button group for a given group name.
     *
     * @param group the group name
     * @return the button group for the name
     */
    private ButtonGroup getButtonGroup(String group) {
        if (m_groups == null) {
            m_groups = new HashMap();
        }

        ButtonGroup buttonGroup = (ButtonGroup) m_groups.get(group);

        if (buttonGroup == null) {
            buttonGroup = new ButtonGroup();

            m_groups.put(group, buttonGroup);
        }

        return buttonGroup;
    }

    /**
     * Initialises a component (if necessary).
     *
     * @param comp the component to initialise
     * @return the initialised component
     */
    private Component initComponent(Component comp) {
        if (comp instanceof AbstractButton) {
            initButton((AbstractButton) comp);
        }

        return comp;
    }

    /**
     * Initialises a button on the toolbar.
     *
     * @param button the button (AbstractButton) to initialise
     */
    private void initButton(AbstractButton button) {
        button.setMargin(new Insets(0, 0, 0, 0));

        if (button.getIcon() != null) {
            // We have an icon - don't want text
            button.setText(null);
        }

        // Set up default alignments and focus drawing
        button.setFocusPainted(false);

        // Add our status listener
        button.addMouseListener(m_statusBarAdapter);
    }

    // These methods override the appropriate Container add methods
    public Component add(Component comp) {
        return initComponent(super.add(comp));
    }

    public void add(Component comp, Object constraints) {
        super.add(comp, constraints);
        initComponent(comp);
    }

    public void add(Component comp, Object constraints, int index) {
        super.add(comp, constraints, index);
        initComponent(comp);
    }

    public Component add(Component comp, int index) {
        return initComponent(super.add(comp, index));
    }

    public Component add(String name, Component comp) {
        return initComponent(super.add(name, comp));
    }
}
