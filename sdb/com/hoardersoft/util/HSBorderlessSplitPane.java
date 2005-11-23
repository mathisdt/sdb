package com.hoardersoft.util;

import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.*;
import javax.swing.border.*;

import java.awt.*;

/**
 * A split pane with no borders.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public final class HSBorderlessSplitPane extends JSplitPane {
    private static final Border EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);

    /**
     * Class constructor.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT
     * @param newLeftComponent the component that will appear on the left of a horizontally-split pane, or at the top of a vertically-split pane
     * @param newRightComponent the component that will appear on the right of a horizontally-split pane, or at the bottom of a vertically-split pane
     */
    public HSBorderlessSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newLeftComponent, newRightComponent);

        setBorder(EMPTY_BORDER);
        setOneTouchExpandable(false);
    }

    public void updateUI() {
        super.updateUI();
        removeDividerBorder();
    }

    /**
     * Removes the divider border.
     */
    private void removeDividerBorder() {
        BasicSplitPaneUI ui = (BasicSplitPaneUI) getUI();

        ui.getDivider().setBorder(new BorderUIResource(EMPTY_BORDER));
    }
}
