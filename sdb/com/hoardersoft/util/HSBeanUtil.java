package com.hoardersoft.util;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * General bean utilities. Also maintains hashmap of related buttons.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSBeanUtil {
    private static HashMap m_relatedButtons;

    /**
     * Sets the look and feel for the system.
     */
    public static void setLookAndFeel() {
        setLookAndFeel(E_HSLookAndFeel.PLASTIC_XP);
    }

    /**
     * Sets the look and feel for the system.
     */
    public static void setLookAndFeel(E_HSLookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel.getLookAndFeel());
        }
        catch (Exception e) {
            HSLogUtil.reportError("Error setting look and feel", e);
        }

    }

    /**
     * Returns the look and feel.
     * @return the look and feel (defaults to metal if not known)
     */
    public static E_HSLookAndFeel getLookAndFeel() {
        E_HSLookAndFeel lookAndFeel = E_HSLookAndFeel.fromLookAndFeel(UIManager.getLookAndFeel().getClass().getName());
        
        return lookAndFeel != null ? lookAndFeel : E_HSLookAndFeel.METAL;
    }

    /**
     * Adds a close listener to a frame that exits the system.
     *
     * @param frame the frame to add the listener to
     */
    public static void addCloseExitListener(Frame frame) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Adds a close listener to a dialog that exits the system.
     *
     * @param dialog the dialog to add the listener to
     */
    public static void addCloseExitListener(Dialog dialog) {
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Adds a related button for an action. Used for toggle buttons so that
     * selection status of the buttons can all be kept in sync.
     *
     * @param action the action the button is linked to
     * @param button the button (AbstractButton)
     * @param selected the default selection status for the button
     */
    public static void addRelatedButton(Action action, final AbstractButton button, boolean selected) {
        final HashSet relatedButtons = getRelatedButtons(action);

        if (relatedButtons.isEmpty()) {
            // No other buttons to get the selection status from - use our default
            button.setSelected(selected);
        }
        else {
            // Set the selected status the same as any of the current buttons (should
            // make all buttons selected the same as the first button added for the action)
            button.setSelected(((AbstractButton) (relatedButtons.iterator().next())).isSelected());
        }

        // Add the button to the hash map
        relatedButtons.add(button);

        // Add our listener to keep selection status in sync
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AbstractButton button = (AbstractButton) e.getSource();
                boolean selected      = button.isSelected();

                for (Iterator iter = relatedButtons.iterator(); iter.hasNext(); ) {
                    AbstractButton relatedButton = (AbstractButton) iter.next();

                    if (relatedButton != button) {
                        relatedButton.setSelected(selected);
                    }
                }
            }
        });
    }

    /**
     * Adds a popup menu to a component.
     *
     * @param comp the component to add the popup menu to
     * @param popupMenu the popup menu to add to the component
     */
    public static void addPopupMenu(Component comp, JPopupMenu popupMenu) {
        comp.addMouseListener(new PopupListener(popupMenu));
    }

    /**
     * Removes a popup menu from a component.
     *
     * @param comp the component to remove the popup menu from
     * @param popupMenu the popup menu to remove from the component
     */
    public static void removePopupMenu(Component comp, JPopupMenu popupMenu) {
        MouseListener[] listeners = (MouseListener[]) comp.getListeners(MouseListener.class);

        for (int i = 0; i < listeners.length; i++) {
            if ((listeners[i] instanceof PopupListener) && ((PopupListener) listeners[i]).getPopupMenu() == popupMenu) {
                comp.removeMouseListener(listeners[i]);
            }
        }
    }

    /**
     * Fires an action event to a component's action listeners.
     *
     * @param comp the component to fire the listeners for
     */
    public static void fireActionListeners(Component comp) {
        EventListener listeners[] = comp.getListeners(ActionListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ActionListener listener = (ActionListener) listeners[i];

            listener.actionPerformed(new ActionEvent(comp, 0, null));
        }
    }

    /**
     * Returns the set of related buttons for a given action.
     *
     * @param action the action to return the related buttons for
     * @return the set (HashSet) of related buttons (AbstractButton) for the action
     */
    private static HashSet getRelatedButtons(Action action) {
        if (m_relatedButtons == null) {
            m_relatedButtons = new HashMap();
        }

        HashSet relatedButtons = (HashSet) m_relatedButtons.get(action);

        if (relatedButtons == null) {
            relatedButtons = new HashSet();

            m_relatedButtons.put(action, relatedButtons);
        }

        return relatedButtons;
    }

    /**
     * Sets the selected state of a set of related buttons.
     *
     * @param action the action to which the buttons are all related
     * @param selected whether to set the buttons as selected or not
     */
    public static void setRelatedButtons(Action action, boolean selected) {
        HashSet relatedButtons = getRelatedButtons(action);

        for (Iterator iter = relatedButtons.iterator(); iter.hasNext(); ) {
            AbstractButton relatedButton = (AbstractButton) iter.next();

            relatedButton.setSelected(selected);
        }
    }

    /**
     * Private static class to handle popup menus.
     */
    private static class PopupListener extends MouseAdapter {
        JPopupMenu m_popupMenu;

        /**
         * Class constructor.
         *
         * @param popupMenu the popup menu (JPopupMenu)
         */
        public PopupListener(JPopupMenu popupMenu) {
            m_popupMenu = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        /**
         * Return the popup menu.
         *
         * @return the popup menu
         */
        public JPopupMenu getPopupMenu() {
            return m_popupMenu;
        }

        /**
         * Shows the popup menu if appropriate.
         *
         * @param e the mouse event (MouseEvent) that caused this request
         */
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                m_popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Creates a scroll pane with a standard border.
     *
     * @param comp the component within the scroll pane
     * @return the scroll pane (JScrollPane)
     */
    public static JScrollPane createScrollPane(Component comp) {
        return createScrollPane(comp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED, true);
    }

    /**
     * Creates a scroll pane with a standard border.
     *
     * @param comp the component within the scroll pane
     * @param vsbPolicy an integer that specifies the vertical scrollbar policy
     * @param hsbPolicy an integer that specifies the horizontal scrollbar policy
     * @return the scroll pane (JScrollPane)
     */
    public static JScrollPane createScrollPane(Component comp, int vsbPolicy, int hsbPolicy) {
        return createScrollPane(comp, vsbPolicy, hsbPolicy, true);
    }

    /**
     * Creates a scroll pane.
     *
     * @param comp the component within the scroll pane
     * @param border whether to have a border on the scroll pane
     * @return the scroll pane (JScrollPane)
     */
    public static JScrollPane createScrollPane(Component comp, boolean border) {
        return createScrollPane(comp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED, border);
    }

    /**
     * Creates a scroll pane.
     *
     * @param comp the component within the scroll pane
     * @param vsbPolicy an integer that specifies the vertical scrollbar policy
     * @param hsbPolicy an integer that specifies the horizontal scrollbar policy
     * @param border whether to have a border on the scroll pane
     * @return the scroll pane (JScrollPane)
     */
    public static JScrollPane createScrollPane(Component comp, int vsbPolicy, int hsbPolicy, boolean border) {
        JScrollPane scrollPane = new JScrollPane(comp, vsbPolicy, hsbPolicy);

        if (!border) {
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }

        return scrollPane;
    }

    /**
     * Creates a split pane with standard resizing (bottom/right component gets it all) but no border.
     *
     * @param orientation the orientation (JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT)
     * @param comp1 the top/left component
     * @param comp2 the bottom/right component
     * @return the scroll pane (JScrollPane)
     */
    public static JSplitPane createSplitPane(int orientation, Component comp1, Component comp2) {
        return createSplitPane(orientation, comp1, comp2, 0.0, false);
    }

    /**
     * Creates a split pane with standard resizing (bottom/right component gets it all).
     *
     * @param orientation the orientation (JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT)
     * @param comp1 the top/left component
     * @param comp2 the bottom/right component
     * @param border whether to have a border on the scroll pane
     * @return the scroll pane (JScrollPane)
     */
    public static JSplitPane createSplitPane(int orientation, Component comp1, Component comp2, boolean border) {
        return createSplitPane(orientation, comp1, comp2, 0.0, border);
    }

    /**
     * Creates a split pane with no border.
     *
     * @param orientation the orientation (JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT)
     * @param comp1 the top/left component
     * @param comp2 the bottom/right component
     * @param resizeWeight the resize weight (0 means bottom/right component gets extra space, 1 means top/left component gets extra space)
     * @return the scroll pane (JScrollPane)
     */
    public static JSplitPane createSplitPane(int orientation, Component comp1, Component comp2, double resizeWeight) {
        return createSplitPane(orientation, comp1, comp2, resizeWeight, false);
    }

    /**
     * Creates a split pane.
     *
     * @param orientation the orientation (JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT)
     * @param comp1 the top/left component
     * @param comp2 the bottom/right component
     * @param resizeWeight the resize weight (0 means bottom/right component gets extra space, 1 means top/left component gets extra space)
     * @param border whether to have a border on the scroll pane
     * @return the scroll pane (JScrollPane)
     */
    public static JSplitPane createSplitPane(int orientation, Component comp1, Component comp2, double resizeWeight, boolean border) {
        JSplitPane splitPane;

        if (!border) {
            splitPane = new HSBorderlessSplitPane(orientation, comp1, comp2);

            splitPane.setBorder(BorderFactory.createEmptyBorder());
        }
        else {
            splitPane = new JSplitPane(orientation, comp1, comp2);
        }

        splitPane.setResizeWeight(resizeWeight);
        splitPane.setOneTouchExpandable(false);

        return splitPane;
    }

    /**
     * Gets a JLabel initialised with the given label.
     * Also appends on a colon onto the string label (if necessary).
     *
     * @param label the string label for the JLabel
     * @return the initialised JLabel
     */
    public static JLabel getJLabel(String label) {
        return getJLabel(label, -1);
    }

    /**
     * Gets a JLabel initialised with the given label and width.
     * Also appends on a colon onto the string label (if necessary).
     *
     * @param label the string label for the JLabel
     * @param width the preferred width of the JLabel (negative for default)
     * @return the initialised JLabel
     */
    public static JLabel getJLabel(String label, int width) {
        JLabel jLabel = new JLabel((label.endsWith(":") ? label : label + ":"));

        if (width >= 0) {
            jLabel.setPreferredSize(new Dimension(width, jLabel.getPreferredSize().height));
        }

        return jLabel;
    }
}
