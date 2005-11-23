package com.hoardersoft.menu;

import com.hoardersoft.statusbar.*;
import com.hoardersoft.util.*;

import javax.swing.*;

import java.awt.*;
import java.util.*;

/**
 * Hoardersoft menu component. Main advantage over a JMenu is the ability
 * to add toggle actions which are kept in sync with related toggle actions
 * (with respect to selection status) and operate within a specific button
 * group. Also configures menu items as desired (including lining up items)
 * and supports a status bar listener. Why doesn't Swing handle any of this
 * for you ?
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSMenu extends JMenu {
    private ImageIcon m_emptyIcon = HSIconUtil.loadImage("Empty.png");
    private HSStatusBarAdapter m_statusBarAdapter;
    private HashMap m_groups;

    /**
     * Class constructor that takes a name.
     *
     * @param name the name of the menu
     */
    public HSMenu(String name) {
        super(name);

        init(null);
    }

    /**
     * Class constructor that takes a name and mnemonic.
     *
     * @param name the name of the menu
     * @param mnemonic the mnemonic for the menu
     */
    public HSMenu(String name, char mnemonic) {
        super(name);

        setMnemonic(mnemonic);
        init(null);
    }

    /**
     * Class constructor that takes a name and a status listener.
     *
     * @param name the name of the menu
     * @param statusBarListener the status bar listener (HSStatusBarListener)
     */
    public HSMenu(String name, HSStatusBarListener statusBarListener) {
        super(name);

        init(statusBarListener);
    }

    /**
     * Class constructor that takes a name, mnemonic and a status listener.
     *
     * @param name the name of the menu
     * @param mnemonic the mnemonic for the menu
     * @param statusBarListener the status bar listener (HSStatusBarListener)
     */
    public HSMenu(String name, char mnemonic, HSStatusBarListener statusBarListener) {
        super(name);

        setMnemonic(mnemonic);
        init(statusBarListener);
    }

    /**
     * Initialises the menu.
     *
     * @param statusBarListener the status bar listener (HSStatusBarListener) (null for none)
     */
    private void init(HSStatusBarListener statusBarListener) {
        // Create a new status bar adapter
        m_statusBarAdapter = new HSStatusBarAdapter(statusBarListener);
    }

    /**
     * Adds an action to the menu.
     *
     * @param action the action to add to the menu
     * @return the added menu item (JMenuItem)
     */
    public JMenuItem add(Action action) {
        JMenuItem menuItem = super.add(action);

        initButton(menuItem);
        alignIcons();

        return menuItem;
    }

    /**
     * Adds a toggle action to the menu.
     *
     * @param action the toggle action to add to the menu
     * @return the added menu item (JCheckBoxMenuItem)
     */
    public JMenuItem addToggle(Action action) {
        return addToggle(action, null, false);
    }

    /**
     * Adds a toggle action to the menu.
     *
     * @param action the toggle action to add to the menu
     * @param selected whether the toggle is selected
     * @return the added menu item (JCheckBoxMenuItem)
     */
    public JMenuItem addToggle(Action action, boolean selected) {
        return addToggle(action, null, selected);
    }

    /**
     * Adds a toggle action to the menu.
     *
     * @param action the toggle action to add to the menu
     * @param group the group name for the toggle (null for none)
     * @return the added menu item (JCheckBoxMenuItem)
     */
    public JMenuItem addToggle(Action action, String group) {
        return addToggle(action, group, false);
    }

    /**
     * Adds a toggle action to the menu.
     *
     * @param action the toggle action to add to the menu
     * @param group the group name for the toggle (null for none)
     * @param selected whether the toggle is selected
     * @return the added menu item (JCheckBoxMenuItem)
     */
    public JMenuItem addToggle(Action action, String group, boolean selected) {
        JMenuItem menuItem;

        if (group != null) {
            // Part of a group - use a radio button
            menuItem = super.add(new JRadioButtonMenuItem(action));
        }
        else {
            // Not part of a group - use a check box button
            menuItem = super.add(new JCheckBoxMenuItem(action));
        }

        initButton(menuItem);
        alignIcons();

        // Add to the group
        if (group != null) {
            getButtonGroup(group).add(menuItem);
        }

        // Add our related button
        HSBeanUtil.addRelatedButton(action, menuItem, selected);

        return menuItem;
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
     * @param statusListener the status listener property
     */
    public void setStatusListener(HSStatusBarListener statusListener) {
        m_statusBarAdapter.setStatusListener(statusListener);
    }

    /**
     * Return whether there are any icons at the moment.
     *
     * @return whether there are any icons at the moment
     */
    private boolean anyIcons() {
        int nMenuItems = getMenuComponentCount();

        for (int i = 0; i < nMenuItems; i++) {
            Component item = getMenuComponent(i);

            if ((item instanceof JMenuItem) && (((JMenuItem) item).getIcon() != null)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Fill in icons (if there is no icon, put in an empty icon).
     */
    private void fillInIcons() {
        int nMenuItems = getMenuComponentCount();

        for (int i = 0; i < nMenuItems; i++) {
            Component item = getMenuComponent(i);

            if ((item instanceof JMenuItem) && (((JMenuItem) item).getIcon() == null)) {
                ((JMenuItem) item).setIcon(m_emptyIcon);
            }
        }
    }

    /**
     * Align the icons.
     */
    private void alignIcons() {
        // We fill in a blank icon to align the menu items
        if (anyIcons()) {
            fillInIcons();
        }
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
     * Initialises a button on the menu.
     *
     * @param button the button (AbstractButton) to initialise
     */
    private void initButton(AbstractButton button) {
        // Don't want tooltip text on menu items - why does Swing insist on being stupid in its default settings?
        button.setToolTipText(null);

        // Add our status listener
        button.addMouseListener(m_statusBarAdapter);
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

    public JMenuItem add(String s) {
        return (JMenuItem) initComponent(super.add(s));
    }

    public JMenuItem add(JMenuItem menuItem) {
        return (JMenuItem) initComponent(super.add(menuItem));
    }
}
