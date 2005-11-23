package com.hoardersoft.colourselector;

import com.hoardersoft.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * HoarderSoft colour selector bean with label.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSColourSelectorWithLabel extends JPanel {
    // GUI members
    private JLabel m_label                    = new JLabel("<label>");
    private JPanel m_colourSelectorPanel      = new JPanel(new HSVerticalFlowLayout(HSVerticalFlowLayout.MIDDLE, 0, 0, false, false));
    private HSColourSelector m_colourSelector = new HSColourSelector();

    // Non-GUI members
    private String m_labelText     = null;
    private boolean m_labelExpands = true;
    private int m_labelWidth       = 0;

    /**
     * Class constructor.
     */
    public HSColourSelectorWithLabel() {
        init();
    }

    /**
     * Class constructor that takes an initial colour.
     *
     * @param colour the initial colour
     */
    public HSColourSelectorWithLabel(Color colour) {
        init();
        m_colourSelector.setColour(colour);
    }

    /**
     * Class constructor that takes an initial label text.
     *
     * @param text the string text for the label
     */
    public HSColourSelectorWithLabel(String text) {
        init();
        setLabelText(text);
    }

    /**
     * Class constructor that takes an initial colour and an initial label text.
     *
     * @param colour the initial colour
     * @param text the string text for the label
     */
    public HSColourSelectorWithLabel(Color colour, String text) {
        init();
        m_colourSelector.setColour(colour);
        setLabelText(text);
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
            throw new Error("Error initialising GUI - HSColourSelectorWithLabel");
        }
    }

    /**
     * Gets the label (JLabel).
     *
     * @return the label (JLabel)
     */
    public JLabel getJLabel() {
        return m_label;
    }

    /**
     * Gets the colour selector (HSColourSelector).
     *
     * @return the colour selector (HSColourSelector)
     */
    public HSColourSelector getColourSelector() {
        return m_colourSelector;
    }

    /**
     * Gets the label text.
     *
     * @return the string text for the label
     */
    public String getLabelText() {
        return m_labelText;
    }

    /**
     * Sets the label text.
     *
     * @param text the string text for the label
     */
    public void setLabelText(String text) {
        m_labelText = text;

        // Add ":" if necessary
        if ((text != null) && !text.equals("") && !text.endsWith(":")) {
            text = text + ":";
        }

        m_label.setText(text);
    }

    /**
     * Gets the label expands property.
     *
     * @return the label expands property
     */
    public boolean getLabelExpands() {
        return m_labelExpands;
    }

    /**
     * Sets the label expands property.
     *
     * @param labelExpands the label expands property
     */
    public void setLabelExpands(boolean labelExpands) {
        if (labelExpands != m_labelExpands) {
            m_labelExpands = labelExpands;

            remove(m_label);
            remove(m_colourSelectorPanel);
            add(m_label, m_labelExpands ? BorderLayout.CENTER : BorderLayout.WEST);
            add(m_colourSelectorPanel, BorderLayout.CENTER);
        }
    }

    /**
     * Gets the label width property.
     *
     * @return the label width property
     */
    public int getLabelWidth() {
        return m_labelWidth;
    }

    /**
     * Sets the label width property.
     *
     * @param labelWidth the label width property
     */
    public void setLabelWidth(int labelWidth) {
        m_labelWidth = labelWidth;

        m_label.setPreferredSize(new Dimension(m_labelWidth, m_label.getHeight()));
    }

    /**
     * Gets the selected colour.
     *
     * @return the selected colour (null for "no colour")
     */
    public Color getColour() {
        return m_colourSelector.getColour();
    }

    /**
     * Sets the selected colour and updates any listeners.
     *
     * @param colour the colour to set the selected colour to (null for "no colour")
     */
    public void updateColour(Color colour) {
        m_colourSelector.updateColour(colour);
    }

    /**
     * Sets the selected colour.
     *
     * @param colour the colour to set the selected colour to (null for "no colour")
     */
    public void setColour(Color colour) {
        m_colourSelector.setColour(colour);
    }

    /**
     * Gets whether the "no colour" functionality is enabled.
     *
     * @return whether the "no colour" functionality is enabled
     */
    public boolean getNoColourEnabled() {
        return m_colourSelector.getNoColourEnabled();
    }

    /**
     * Sets whether the "no colour" functionality is enabled.
     *
     * @param noColourEnabled whether the "no colour" functionality is enabled
     */
    public void setNoColourEnabled(boolean noColourEnabled) {
        m_colourSelector.setNoColourEnabled(noColourEnabled);
    }

    /**
     * Gets the "no colour" label.
     *
     * @return the "no colour" label
     */
    public String getNoColourLabel() {
        return m_colourSelector.getNoColourLabel();
    }

    /**
     * Sets the "no colour" label.
     *
     * @param noColourLabel the "no colour" label
     */
    public void setNoColourLabel(String noColourLabel) {
        m_colourSelector.setNoColourLabel(noColourLabel);
    }

    /**
     * Gets the "more colours" label.
     *
     * @return the "more colours" label
     */
    public String getMoreColoursLabel() {
        return m_colourSelector.getMoreColoursLabel();
    }

    /**
     * Sets the "more colours" label.
     *
     * @param moreColoursLabel the "more colours" label
     */
    public void setMoreColoursLabel(String moreColoursLabel) {
        m_colourSelector.setMoreColoursLabel(moreColoursLabel);
    }

    /**
     * Add an action listener to the font selector.
     *
     * @param al the action listener to add
     */
    public void addActionListener(ActionListener al) {
        m_colourSelector.addActionListener(al);
    }

    /**
     * Remove an action listener from the font selector.
     *
     * @param al the action listener to remove
     */
    public void removeActionListener(ActionListener al) {
        m_colourSelector.removeActionListener(al);
    }

    /**
     * Gets whether the colour selector is enabled.
     *
     * @return whether the colour selector is enabled
     */
    public boolean isEnabled() {
        return m_colourSelector.isEnabled();
    }

    /**
     * Sets whether the colour selector is enabled.
     *
     * @param enabled whether the colour selector is enabled
     */
    public void setEnabled(boolean enabled) {
        m_label.setEnabled(enabled);
        m_colourSelector.setEnabled(enabled);
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void jbInit() {
        setLayout(new BorderLayout());
        add(m_label, BorderLayout.CENTER);
        add(m_colourSelectorPanel, BorderLayout.EAST);
        m_colourSelectorPanel.add(m_colourSelector, null);
    }
}
