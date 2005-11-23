package com.hoardersoft.colourselector;

import com.hoardersoft.util.*;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * HoarderSoft colour selector bean. Basically just a pull down swatch
 * menu with 40 swatches (taken from Word) with a "More Colours" button
 * to bring up the full Java colour selector.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSColourSelector extends JPanel {
    // GUI members
    private JPanel m_panel               = new JPanel(new FlowLayout());
    private JButton m_popupButton        = new JButton();
    private Border m_grayLineBorder      = BorderFactory.createLineBorder(Color.GRAY);
    private Border m_lightGrayLineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

    // Non-GUI members
    private HSColourPanel m_colourPanel = null;
    private JWindow m_window            = null;
    private boolean m_popupEnabled      = true;
    private ArrayList m_listenerList    = new ArrayList();
    private Color m_colour              = Color.white;
    private boolean m_hexColourString   = true;

    /**
     * Class constructor.
     */
    public HSColourSelector() {
        init();
    }

    /**
     * Class constructor that takes an initial colour.
     *
     * @param colour the initial colour
     */
    public HSColourSelector(Color colour) {
        m_colour = colour;

        init();
    }

    /**
     * Initialises the selector dialog.
     */
    private void init() {
        try {
            jbInit();
        }
        catch (Exception ex) {
            // Let's hope this never happens !!
            throw new Error("Error initialising GUI - HSColourSelector");
        }

        m_colourPanel = new HSColourPanel(m_colour, this);

        setColour(m_colour);
        updateIcons();
    }

    /**
     * Update the pulldown icon.
     */
    private void updateIcons() {
        m_popupButton.setIcon(HSIconUtil.loadImage("MenuPulldown.png"));
    }

    /**
     * Gets the selected colour.
     *
     * @return the selected colour (null for "no colour")
     */
    public Color getColour() {
        return m_colour;
    }

    /**
     * Sets the selected colour and updates any listeners.
     *
     * @param colour the colour to set the selected colour to (null for "no colour")
     */
    public void updateColour(Color colour) {
        Color oldColour = m_colour;

        setColour(colour);

        boolean sameColour = ((m_colour == null) ? (oldColour == null) : m_colour.equals(oldColour));

        if (!sameColour) {
            // We have changed - update the listeners and repaint
            updateListeners();
            repaint();
        }
    }

    /**
     * Sets the selected colour.
     *
     * @param colour the colour to set the selected colour to (null for "no colour")
     */
    public void setColour(Color colour) {
        m_colour = colour;

        m_colourPanel.setColour(m_colour);

        if (m_colour != null) {
            m_panel.setOpaque(true);
            m_panel.setBackground(m_colour);

            if (m_hexColourString) {
                m_panel.setToolTipText(HSStringUtil.getColourString(colour));
            }
            else {
                m_panel.setToolTipText("RGB: " + colour.getRed() + "," + colour.getGreen() + "," + colour.getBlue());
            }
        }
        else {
            m_panel.setOpaque(false);
            m_panel.setToolTipText(m_colourPanel.getNoColourLabel());
        }

        m_panel.invalidate();
        m_panel.repaint();
    }

    /**
     * Gets whether the "no colour" functionality is enabled.
     *
     * @return whether the "no colour" functionality is enabled
     */
    public boolean getNoColourEnabled() {
        return m_colourPanel.getNoColourEnabled();
    }

    /**
     * Sets whether the "no colour" functionality is enabled.
     *
     * @param noColourEnabled whether the "no colour" functionality is enabled
     */
    public void setNoColourEnabled(boolean noColourEnabled) {
        m_colourPanel.setNoColourEnabled(noColourEnabled);
    }

    /**
     * Gets the "no colour" label.
     *
     * @return the "no colour" label
     */
    public String getNoColourLabel() {
        return m_colourPanel.getNoColourLabel();
    }

    /**
     * Sets the "no colour" label.
     *
     * @param noColourLabel the "no colour" label
     */
    public void setNoColourLabel(String noColourLabel) {
        m_colourPanel.setNoColourLabel(noColourLabel);
    }

    /**
     * Gets the "more colours" label.
     *
     * @return the "more colours" label
     */
    public String getMoreColoursLabel() {
        return m_colourPanel.getMoreColoursLabel();
    }

    /**
     * Sets the "more colours" label.
     *
     * @param moreColoursLabel the "more colours" label
     */
    public void setMoreColoursLabel(String moreColoursLabel) {
        m_colourPanel.setMoreColoursLabel(moreColoursLabel);
    }

    /**
     * Gets whether the colour string (popup) is in hex.
     *
     * @return whether the colour string (popup) is in hex
     */
    public boolean getHexColourString() {
        return m_hexColourString;
    }

    /**
     * Sets whether the colour string (popup) is in hex.
     *
     * @param hexColourString whether the colour string (popup) is in hex
     */
    public void setHexColourString(boolean hexColourString) {
        m_hexColourString = hexColourString;
    }

    /**
     * Hide the popup window.
     */
    protected void hideWindow() {
        m_window.hide();
    }

    /**
     * Make the popup window grab the focus.
     */
    protected void setWindowFocus() {
        m_window.requestFocus();
    }

    /**
     * Reenable the popup window button.
     */
    protected void enablePopupButton() {
        m_popupEnabled = true;
    }

    /**
     * The focus listener to catch focus lost events.
     */
    private class WindowFocusListener extends FocusAdapter {
        public void focusLost(FocusEvent e) {
            // No idea why but temporary focus lost events are the
            // ones we want to catch - another Swing special :(
            if (e.isTemporary()) {
                // Hide the popup window and reenable the popup window button
                hideWindow();
                enablePopupButton();
            }
        }
    }

    /**
     * The key listener to catch key press events.
     */
    private class WindowKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                // Escape pressed - hide the popup window and reenable the popup window button
                hideWindow();
                enablePopupButton();
            }
            else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                // Colour selected - hide the popup window and reenable the popup window button
                hideWindow();
                enablePopupButton();
                updateColour(m_colourPanel.getColour());
            }
            else if (e.getKeyCode() == KeyEvent.VK_UP) {
                m_colourPanel.colourUp();
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                m_colourPanel.colourDown();
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                m_colourPanel.colourLeft();
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                m_colourPanel.colourRight();
            }
        }
    }

    /**
     * Add an action listener to the font selector.
     *
     * @param al the action listener to add
     */
    public void addActionListener(ActionListener al) {
        m_listenerList.add(al);
    }

    /**
     * Remove an action listener from the font selector.
     *
     * @param al the action listener to remove
     */
    public void removeActionListener(ActionListener al) {
        m_listenerList.remove(al);
    }

    /**
     * Updates the list of listeners (telling them the colour has changed).
     */
    private void updateListeners() {
        // Tell all our listeners that our state has changed
        ActionEvent e = new ActionEvent(this, 0, null);

        for (int i = 0; i < m_listenerList.size(); i++) {
            ((ActionListener) m_listenerList.get(i)).actionPerformed(e);
        }
    }

    /**
     * Returns whether the colour selector is enabled.
     *
     * @return whether the colour selector is enabled
     */
    public boolean isEnabled() {
        return m_popupButton.isEnabled();
    }

    /**
     * Enables/disables the colour selector.
     *
     * @param enabled whether the colour selector is enabled or disabled
     */
    public void setEnabled(boolean enabled) {
        m_popupButton.setEnabled(enabled);

        if (HSBeanUtil.getLookAndFeel().isPlastic()) {
            m_panel.setBorder(enabled ? m_grayLineBorder : m_lightGrayLineBorder);
        }
    }

    /**
     * Show the popup menu.
     */
    private void showPopup() {
        // Set the colour
        m_colourPanel.setColour(m_colour, true);

        // Find our root (and root location)
        Point loc      = getLocation();
        int rootX      = loc.x;
        int rootY      = loc.y;
        Component root = this;

        // Add all our locations up to the root
        while (root.getParent() != getRootPane().getParent()) {
            root  = root.getParent();
            loc   = root.getLocation();
            rootX += loc.x;
            rootY += loc.y;
        }

        // And finally add on the root's parent position
        root  = root.getParent();
        loc   = root.getLocation();
        rootX += loc.x;
        rootY += loc.y;

        if (m_window == null) {
            // Create a new popup window
            m_window = new JWindow((Window) getRootPane().getParent());

            // Add our colour selector, size the window correctly and add focus listener
            m_window.getContentPane().add(m_colourPanel);
            m_window.setSize(m_window.getContentPane().getPreferredSize());
            m_window.addFocusListener(new WindowFocusListener());
            m_window.addKeyListener(new WindowKeyListener());
        }

        // Position the window, show it and grab the focus
        m_window.setLocation(rootX, rootY + getSize().height);
        m_window.show();
        setWindowFocus();

        // Finally disable our popup window button for now
        m_popupEnabled = false;
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void m_popupButton_mousePressed(MouseEvent e) {
        // Do nothing if we are in a disabled state (or not left mouse button)
        if (!m_popupButton.isEnabled() || !m_popupEnabled || ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0)) {
            return;
        }

        showPopup();
    }

    private void m_popupButton_actionPerformed() {
        // Do nothing if we are in a disabled state
        if (!m_popupButton.isEnabled() || !m_popupEnabled) {
            return;
        }

        showPopup();
    }

    private void jbInit() {
        boolean isPlastic = HSBeanUtil.getLookAndFeel().isPlastic();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(isPlastic ? 35 : 37, 19));
        add(m_panel, BorderLayout.CENTER);
        add(m_popupButton, BorderLayout.EAST);
        m_panel.setBackground(Color.white);

        if (!isPlastic) {
            setBorder(BorderFactory.createRaisedBevelBorder());
            m_panel.setBorder(BorderFactory.createEtchedBorder());
            m_popupButton.setBorder(null);
        }

        m_popupButton.setPreferredSize(new Dimension(17, 15));
        m_popupButton.setFocusPainted(false);
        m_popupButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                m_popupButton_mousePressed(e);
            }
        });
        m_popupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_popupButton_actionPerformed();
            }
        });
        setEnabled(true);
    }
}
