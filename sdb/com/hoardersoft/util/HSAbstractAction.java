package com.hoardersoft.util;

import javax.swing.*;

/**
 * Class used for actions.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public abstract class HSAbstractAction extends AbstractAction {
    /**
     * Constructor.
     */
    public HSAbstractAction() {
        super();
    }

    /**
     * Constructor that takes a name.
     *
     * @param name the name
     */
    public HSAbstractAction(String name) {
        super(name);
    }

    /**
     * Constructor that takes a name and an icon.
     *
     * @param name the name
     * @param icon the icon
     */
    public HSAbstractAction(String name, Icon icon) {
        super(name, icon);
    }

    /**
     * Class constructor that takes a name, icon and tooltip.
     *
     * @param name the name of the action
     * @param icon the icon for the action
     * @param tooltip the tooltip for the action
     */
    public HSAbstractAction(String name, ImageIcon icon, String tooltip) {
        super(name, icon);

        putValue(Action.SHORT_DESCRIPTION, tooltip);
    }

    /**
     * Class constructor that takes a name, icon, tooltip and help text.
     *
     * @param name the name of the action
     * @param icon the icon for the action
     * @param tooltip the tooltip for the action
     * @param helpText the help text for the action
     */
    public HSAbstractAction(String name, ImageIcon icon, String tooltip, String helpText) {
        super(name, icon);

        putValue(Action.SHORT_DESCRIPTION, tooltip);
        putValue(Action.LONG_DESCRIPTION, helpText);
    }
}
