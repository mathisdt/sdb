package com.hoardersoft.colourselector;

import com.hoardersoft.util.HSBeanUtil;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

/**
 * HoarderSoft colour selector popup window panel.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSColourPanel extends JPanel {
    // GUI members
    private JPanel m_topPanel                    = new JPanel(new BorderLayout(0, 5));
    private JPanel m_moreColoursButtonPanel      = new JPanel(new BorderLayout());
    private JToggleButton m_noColourToggleButton = new JToggleButton("Automatic");
    private JPanel m_gridPanel                   = new JPanel(new GridLayout(5, 8, 6, 6));
    private JButton m_moreColoursButton          = new JButton("More Colours...");
    private JButton m_buttons[]                  = null;
    private Border m_deselectedBorder            = BorderFactory.createLineBorder(Color.GRAY);
    private Border m_selectedBorder              = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), m_deselectedBorder);
    private JColorChooser m_colourChooser        = null;

    // Non-GUI members
    private HSColourSelector m_selector = null;
    private Color m_colour              = Color.white;
    private int m_colourIndex           = -1;
    private boolean m_noColourEnabled   = false;
    private String m_noColourLabel      = "Automatic";
    private String m_moreColoursLabel   = "More Colours";

    // Note colours and names pinched from Word - you can't get more WLAF surely !!
    private final static int m_nButtons         = 40;
    private final static int m_buttonCols       = 8;
    private final static Color m_colours[]      = {
        new Color(0, 0, 0), new Color(153, 51, 0), new Color(51, 51, 0), new Color(0, 51, 0), new Color(0, 51, 102), new Color(0, 0, 128), new Color(51, 51, 153), new Color(51, 51, 51), new Color(128, 0, 0), new Color(255, 102, 0), new Color(128, 128, 0), new Color(0, 128, 0), new Color(0, 128, 128), new Color(0, 0, 255), new Color(102, 102, 153), new Color(128, 128, 128), new Color(255, 0, 0), new Color(255, 153, 0), new Color(153, 204, 0), new Color(51, 153, 102), new Color(51, 204, 204), new Color(51, 102, 255), new Color(128, 0, 128), new Color(153, 153, 153), new Color(255, 0, 255), new Color(255, 204, 0), new Color(255, 255, 0), new Color(0, 255, 0), new Color(0, 255, 255), new Color(0, 204, 255), new Color(153, 51, 102), new Color(192, 192, 192), new Color(255, 153, 204), new Color(255, 204, 153), new Color(255, 255, 153), new Color(204, 255, 204), new Color(204, 255, 255), new Color(153, 204, 255), new Color(204, 153, 255), new Color(255, 255, 255)
    };
    private final static String m_colourNames[] = {
        "Black", "Brown", "Olive Green", "Dark Green", "Dark Teal", "Dark Blue", "Indigo", "Gray-80%", "Dark Red", "Orange", "Dark Yellow", "Green", "Teal", "Blue", "Blue-Gray", "Gray-50%", "Red", "Light Orange", "Lime", "Sea Green", "Aqua", "Light Blue", "Violet", "Gray-40%", "Pink", "Gold", "Yellow", "Bright Green", "Turquoise", "Sky Blue", "Plum", "Gray-25%", "Rose", "Tan", "Light Yellow", "Light Green", "Light Turquoise", "Pale Blue", "Lavender", "White"
    };

    /**
     * Class constructor.
     */
    public HSColourPanel() {
        init();
    }

    /**
     * Class constructor that takes an initial colour and parent colour selector.
     *
     * @param colour the initial colour (null for "no color", if enabled)
     * @param selector the parent colour selector
     */
    public HSColourPanel(Color colour, HSColourSelector selector) {
        m_colour   = colour;
        m_selector = selector;

        init();
    }

    /**
     * Initialises the panel.
     */
    private void init() {
        try {
            jbInit();
        }
        catch (Exception ex) {
            // Let's hope this never happens !!
            throw new Error("Error initialising GUI - HSColourPanel");
        }

        // Set up the grid of buttons
        m_buttons = new JButton[m_nButtons];

        ColourButtonListener colourButtonlistener = new ColourButtonListener();
        ButtonFocusListener buttonFocusListener   = new ButtonFocusListener();
        ButtonKeyListener buttonKeyListener       = new ButtonKeyListener();

        for (int i = 0; i < m_nButtons; i++) {
            m_buttons[i] = new JButton();

            m_buttons[i].setPreferredSize(new Dimension(12, 12));
            m_buttons[i].setBorder(m_deselectedBorder);
            m_buttons[i].setBackground(m_colours[i]);
            m_buttons[i].setToolTipText(m_colourNames[i]);
            m_buttons[i].addMouseListener(colourButtonlistener);
            m_buttons[i].addFocusListener(buttonFocusListener);
            m_buttons[i].addKeyListener(buttonKeyListener);
            m_buttons[i].setText("");
            m_buttons[i].setFocusPainted(false);
            m_gridPanel.add(m_buttons[i]);
        }

        // Initialise the colour
        setColour(m_colour);

        // By default we don't enable the "no colour" functionality
        setNoColourEnabled(false);

        // Set up the default selector
        m_colourChooser = new JColorChooser();
    }

    /**
     * Gets the current colour.
     *
     * @return the current colour (null for "no colour")
     */
    public Color getColour() {
        return m_colour;
    }

    /**
     * Sets the current colour.
     *
     * @param colour the colour to set the current colour to (null for "no colour")
     */
    public void setColour(Color colour) {
        // By default we don't update the "no colour" toggle
        setColour(colour, false);
    }

    /**
     * Sets the current colour.
     *
     * @param colour the colour to set the current colour to (null for "no colour")
     * @param updateNoColourToggle whether to update the state of the "no colour" toggle
     */
    public void setColour(Color colour, boolean updateNoColourToggle) {
        // Store the colour
        m_colour = colour;

        // Set the first button which matches the colour
        m_colourIndex = -1;

        boolean found = false;

        for (int i = 0; i < m_nButtons; i++) {
            if (!found && m_buttons[i].getBackground().equals(m_colour)) {
                found         = true;
                m_colourIndex = i;

                m_buttons[i].setBorder(m_selectedBorder);
            }
            else {
                m_buttons[i].setBorder(m_deselectedBorder);
            }
        }

        if (updateNoColourToggle) {
            // Set our "no colour" toggle button
            m_noColourToggleButton.setSelected(m_colour == null);
        }
    }

    /**
     * Gets whether the "no colour" functionality is enabled.
     *
     * @return whether the "no colour" functionality is enabled
     */
    public boolean getNoColourEnabled() {
        return m_noColourEnabled;
    }

    /**
     * Sets whether the "no colour" functionality is enabled.
     *
     * @param noColourEnabled whether the "no colour" functionality is enabled
     */
    public void setNoColourEnabled(boolean noColourEnabled) {
        m_noColourEnabled = noColourEnabled;

        m_noColourToggleButton.setVisible(m_noColourEnabled);
    }

    /**
     * Gets the "no colour" label.
     *
     * @return the "no colour" label
     */
    public String getNoColourLabel() {
        return m_noColourLabel;
    }

    /**
     * Sets the "no colour" label.
     *
     * @param noColourLabel the "no colour" label
     */
    public void setNoColourLabel(String noColourLabel) {
        m_noColourLabel = noColourLabel;

        m_noColourToggleButton.setText(noColourLabel);
    }

    /**
     * Gets the "more colours" label.
     *
     * @return the "more colours" label
     */
    public String getMoreColoursLabel() {
        return m_moreColoursLabel;
    }

    /**
     * Sets the "more colours" label.
     *
     * @param moreColoursLabel the "more colours" label
     */
    public void setMoreColoursLabel(String moreColoursLabel) {
        m_moreColoursLabel = moreColoursLabel;

        // Add "..." if necessary
        if ((moreColoursLabel != null) && !moreColoursLabel.equals("") && !moreColoursLabel.endsWith("...")) {
            moreColoursLabel = moreColoursLabel + "...";
        }

        m_moreColoursButton.setText(moreColoursLabel);
    }

    /**
     * Moves the colour "up" in the grid.
     */
    public void colourUp() {
        if (m_colourIndex < 0) {
            setColour(m_buttons[0].getBackground());
        }
        else {
            int newIndex = (m_colourIndex - m_buttonCols) % m_nButtons;

            if (newIndex < 0) {
                newIndex += m_nButtons;
            }

            setColour(m_buttons[newIndex].getBackground());
        }
    }

    /**
     * Moves the colour "down" in the grid.
     */
    public void colourDown() {
        if (m_colourIndex < 0) {
            setColour(m_buttons[0].getBackground());
        }
        else {
            int newIndex = (m_colourIndex + m_buttonCols) % m_nButtons;

            if (newIndex < 0) {
                newIndex += m_nButtons;
            }

            setColour(m_buttons[newIndex].getBackground());
        }
    }

    /**
     * Moves the colour "left" in the grid.
     */
    public void colourLeft() {
        if (m_colourIndex < 0) {
            setColour(m_buttons[0].getBackground());
        }
        else {
            int newIndex = (m_colourIndex - 1) % m_nButtons;

            if (newIndex < 0) {
                newIndex += m_nButtons;
            }

            setColour(m_buttons[newIndex].getBackground());
        }
    }

    /**
     * Moves the colour "right" in the grid.
     */
    public void colourRight() {
        if (m_colourIndex < 0) {
            setColour(m_buttons[0].getBackground());
        }
        else {
            int newIndex = (m_colourIndex + 1) % m_nButtons;

            if (newIndex < 0) {
                newIndex += m_nButtons;
            }

            setColour(m_buttons[newIndex].getBackground());
        }
    }

    /**
     * Hide the popup window.
     */
    private void hideWindow() {
        if (m_selector != null) {
            m_selector.hideWindow();
        }
    }

    /**
     * Reenable the popup window button.
     */
    private void enablePopupButton() {
        if (m_selector != null) {
            m_selector.enablePopupButton();
        }
    }

    /**
     * Sets the colour in the parent selector.
     */
    private void setSelectorColour() {
        if (m_selector != null) {
            m_selector.updateColour(m_colour);
        }
    }

    /**
     * The focus listener to catch focus lost events.
     */
    private class ButtonFocusListener extends FocusAdapter {
        public void focusLost(FocusEvent e) {
            // No idea why but temporary focus lost events are the
            // ones we want to catch - another Swing special :(
            if (e.isTemporary()) {
                hideWindow();
                enablePopupButton();
            }
        }
    }

    /**
     * The key listener to catch key press events.
     */
    private class ButtonKeyListener extends KeyAdapter {
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
                setSelectorColour();
            }
            else if (e.getKeyCode() == KeyEvent.VK_UP) {
                colourUp();
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                colourDown();
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                colourLeft();
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                colourRight();
            }
        }
    }

    /**
     * The mouse listener to catch mouse events on the colour swatches.
     */
    private class ColourButtonListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            JButton source = (JButton) e.getSource();

            setColour(source.getBackground());

            // Colour selected - hide the popup window and reenable the popup window button
            hideWindow();
            enablePopupButton();
            setSelectorColour();
        }
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void m_noColourToggleButton_actionPerformed() {
        // Hide the window
        hideWindow();

        // Re-enable our popup button
        enablePopupButton();

        // Clicking "no colour" always means "no colour" selected
        m_noColourToggleButton.setSelected(true);

        m_colour = null;

        setSelectorColour();
    }

    private void m_moreColoursButton_actionPerformed() {
        // Hide the window
        hideWindow();

        // Bring up a standard Java colour chooser
        Color colour = m_colourChooser.showDialog(m_selector, "Colors", m_colour);

        // Just in case they hit the colour selector again (useless Swing locking)
        hideWindow();

        // Re-enable our popup button
        enablePopupButton();

        if (colour != null) {
            // A colour has been selected
            m_colour = colour;

            setSelectorColour();
        }
    }

    private void jbInit() {
        setLayout(new BorderLayout());

        if (HSBeanUtil.getLookAndFeel().isPlastic()) {
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        else {
            setBorder(BorderFactory.createRaisedBevelBorder());
        }

        add(m_topPanel, BorderLayout.CENTER);
        add(m_moreColoursButtonPanel, BorderLayout.SOUTH);
        m_topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        m_topPanel.add(m_noColourToggleButton, BorderLayout.NORTH);
        m_topPanel.add(m_gridPanel, BorderLayout.CENTER);
        m_noColourToggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_noColourToggleButton_actionPerformed();
            }
        });
        m_moreColoursButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_moreColoursButtonPanel.add(m_moreColoursButton, BorderLayout.CENTER);
        m_moreColoursButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_moreColoursButton_actionPerformed();
            }
        });
    }
}
