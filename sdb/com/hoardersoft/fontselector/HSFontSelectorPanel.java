package com.hoardersoft.fontselector;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.*;

/**
 * HoarderSoft font selector bean.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSFontSelectorPanel extends JPanel {
    public final static int MIN_FONT_SIZE  = 4;
    public final static int MAX_FONT_SIZE  = 100;
    private final static String[] m_styles = { "Normal", "Kursiv", "Fett", "Fett Kursiv" };
    private final static int[] m_sizes     = {
        8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72
    };

    // GUI members
    private JPanel m_topPanel               = new JPanel(new BorderLayout(5, 0));
    private JPanel m_previewPanel           = new JPanel(new BorderLayout());
    private JPanel m_selectorPanelLeft      = new JPanel(new BorderLayout(5, 0));
    private HSFontList m_fontSelector       = new HSFontList("Schriftart");
    private HSFontList m_styleSelector      = new HSFontList("Stil");
    private JPanel m_selectorPanelRight     = new JPanel(new BorderLayout(5, 0));
    private HSFontList m_sizeSelector       = new HSFontList("Größe");
    private JLabel m_previewPanelLabel      = new JLabel("Vorschau:");
    private HSPreviewJLabel m_previewJLabel = new HSPreviewJLabel();

    // Non-GUI members
    private ArrayList m_listenerList = new ArrayList();

    /**
     * Class constructor.
     */
    public HSFontSelectorPanel() {
        init(m_previewJLabel.getFont());
    }

    /**
     * Class constructor that takes a font.
     *
     * @param font the initial font for the selector
     */
    public HSFontSelectorPanel(Font font) {
        init(font);
    }

    /**
     * Initialises the selector.
     *
     * @param font the initial font for the selector
     */
    private void init(Font font) {
        try {
            jbInit();
        }
        catch (Exception ex) {
            // Let's hope this never happens !!
            throw new Error("Error initialising GUI - HSFontSelectorPanel");
        }

        // Set the font
        m_previewJLabel.setFont(font);

        // Set up font list - filter out any fonts with a dot and any
        // other fonts which do not appear to work well in Java land
        String fonts[] = (GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        int count      = 0;

        for (int i = 0; i < fonts.length; i++) {
            if (validFont(fonts[i])) {
                count++;
            }
        }

        String filteredFonts[] = new String[count];
        int j                  = 0;

        for (int i = 0; i < fonts.length; i++) {
            if (validFont(fonts[i])) {
                filteredFonts[j++] = fonts[i];
            }
        }

        m_fontSelector.setData(filteredFonts);

        // Create some styles and set up style list
        m_styleSelector.setData(m_styles);

        // Create some font sizes and set up size list
        Integer sizes[] = new Integer[m_sizes.length];

        for (int i = 0; i < m_sizes.length; i++) {
            sizes[i] = new Integer(m_sizes[i]);
        }

        m_sizeSelector.setData(sizes);

        // Finally set the font
        setCurrentFont(font);
    }

    /**
     * Returns whether a given font name is "valid".
     *
     * @param font the string fontname
     * @return <code>true</code> if the font is valid;
     *         <code>false</code> otherwise.
     */
    private boolean validFont(String font) {
        // Don't know why but some fonts just don't work :(
        // Others are listed with "package" names :(
        // Even weirder one has an unprintable name :(
        return ((font.indexOf('.') < 0) && !font.equals("CTE Hebrew") && !font.equals("Marlett") && !font.equals("MS Outlook") && !font.equals("Symbol") && !font.equals("Batang") && (font.indexOf("Webdings") < 0) && (font.indexOf("Wingdings") < 0) && (font.charAt(0) <= 255));
    }

    /**
     * Adds a key listener to the three lists.
     *
     * @param l the key listener to add
     */
    public void addKeyListener(KeyListener l) {
        m_fontSelector.addKeyListener(l);
        m_styleSelector.addKeyListener(l);
        m_sizeSelector.addKeyListener(l);
    }

    /**
     * Removes a key listener from the three lists.
     *
     * @param l the key listener to remove
     */
    public void removeKeyListener(KeyListener l) {
        m_fontSelector.removeKeyListener(l);
        m_styleSelector.removeKeyListener(l);
        m_sizeSelector.removeKeyListener(l);
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
     * Gets the currently selected font.
     *
     * @return the selected font
     */
    public Font getCurrentFont() {
        return m_previewJLabel.getFont();
    }

    /**
     * Sets the currently selected font in the selector.
     *
     * @param font the font to set the selector to (sets
     *             the font name, style and size selectors)
     */
    public void setCurrentFont(Font font) {
        // Initialise with initial font
        if (font != null) {
            m_fontSelector.setValue(font.getName());

            int style = font.getStyle();

            // Map style constants to our list
            if (style == 1) {
                style = 2;
            }
            else if (style == 2) {
                style = 1;
            }

            if ((style >= 0) && (style <= 3)) {
                m_styleSelector.setValue(m_styles[style]);
            }

            m_sizeSelector.setValue("" + font.getSize());
        }
    }

    /**
     * Checks the currently selected font, raising dialogs to
     * inform the user of problems if necessary.
     *
     * @return <code>true</code> if a valid font is selected;
     *         <code>false</code> otherwise.
     */
    public boolean checkFont() {
        String fontName = m_fontSelector.getValue();

        if (fontName.length() == 0) {
            JOptionPane.showMessageDialog(this, "There is no font currently selected.\n" + "Please choose a valid font from the list.", "Error", JOptionPane.WARNING_MESSAGE);

            return false;
        }
        else if (!m_fontSelector.setValue(fontName)) {
            JOptionPane.showMessageDialog(this, "There is no font with the name \"" + m_fontSelector.getValue() + "\".\n" + "Please choose a valid font from the list.", "Error", JOptionPane.WARNING_MESSAGE);

            return false;
        }

        String style = m_styleSelector.getValue();

        if (style.length() == 0) {
            JOptionPane.showMessageDialog(this, "There is no font style currently selected.\n" + "Please choose a valid font style from the list.", "Error", JOptionPane.WARNING_MESSAGE);

            return false;
        }
        else if (!m_styleSelector.setValue(m_styleSelector.getValue())) {
            JOptionPane.showMessageDialog(this, "There is no font style with the name \"" + m_styleSelector.getValue() + "\".\n" + "Please choose a valid font style from the list.", "Error", JOptionPane.WARNING_MESSAGE);

            return false;
        }

        String size = m_sizeSelector.getValue();

        if (size.length() == 0) {
            JOptionPane.showMessageDialog(this, "There is no font size currently selected.\n" + "Please enter a valid font size.", "Error", JOptionPane.WARNING_MESSAGE);

            return false;
        }
        else {
            try {
                int sizeInt = Integer.parseInt(size);

                if ((sizeInt < MIN_FONT_SIZE) || (sizeInt > MAX_FONT_SIZE)) {
                    JOptionPane.showMessageDialog(this, "The font size must be between " + MIN_FONT_SIZE + " and " + MAX_FONT_SIZE + ".\n" + "Please enter a valid font size in this range.", "Error", JOptionPane.WARNING_MESSAGE);

                    return false;
                }
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "The font size \"" + m_sizeSelector.getValue() + "\" is not a valid integer.\n" + "Please enter a valid integer for the font size.", "Error", JOptionPane.WARNING_MESSAGE);

                return false;
            }
        }

        return true;
    }

    /**
     * Preview label class which gets updated with the currently selected font.
     */
    private class HSPreviewJLabel extends JLabel implements ChangeListener {
        private FontRenderContext m_fontRenderContext = new FontRenderContext(null, true, false);

        /**
         * Class construtor.
         */
        HSPreviewJLabel() {
            super("AaBbYyZz123");

            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(LineBorder.createBlackLineBorder());
            setBackground(Color.white);
            setOpaque(true);
        }

        /**
         * Paint the component. For some reason a JLabel doesn't always update the
         * drawn font so we have to do some shennanigans to get it to work correctly.
         * Why is Swing so inconsistent in real use ?
         *
         * @param g the graphics context to draw into
         */
        public void paintComponent(Graphics g) {
            // This line makes the string always update - don't ask why :(
            // Must be something to do with the font render context but I really have
            // no idea (and stumbled onto this particular code quite by accident !!).
            // Without this line, changing style while on "Microsoft Sans Serif"
            // (for example) doesn't update the preview.
            getFont().getStringBounds("", m_fontRenderContext);

            // Now just call our parent paint method
            super.paintComponent(g);
        }

        /**
         * Updates the preview.
         *
         * @param e the change event (ChangeEvent) which caused the update
         *          (the source should be the font list which caused the update)
         */
        public void stateChanged(ChangeEvent e) {
            String name  = m_fontSelector.getValue();
            String style = m_styleSelector.getValue();
            String size  = m_sizeSelector.getValue();

            try {
                int sizeInt = Integer.parseInt(size);

                if ((sizeInt >= MIN_FONT_SIZE) && (sizeInt <= MAX_FONT_SIZE)) {
                    Font newFont = new Font(name, getStyle(style), sizeInt);

                    setFont(newFont);
                }
            }
            catch (NumberFormatException ex) {
                // Just ignore any exceptions coming back
            }

            updateListeners();
        }

        /**
         * Gets the style for a given string.
         *
         * @param s the string version of the style
         * @return the integer style (if in doubt returns plain)
         */
        private int getStyle(String s) {
            if (s.equals("Fett")) {
                return Font.BOLD;
            }
            else if (s.equals("Kursiv")) {
                return Font.ITALIC;
            }
            else if (s.equals("Fett Kursiv")) {
                return Font.BOLD | Font.ITALIC;
            }

            return Font.PLAIN;
        }
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void jbInit() {
        setLayout(new BorderLayout());
        add(m_topPanel, BorderLayout.NORTH);
        add(m_previewPanel, BorderLayout.CENTER);
        m_topPanel.add(m_selectorPanelLeft, BorderLayout.CENTER);
        m_topPanel.add(m_selectorPanelRight, BorderLayout.EAST);
        m_selectorPanelLeft.add(Box.createHorizontalStrut(0), BorderLayout.WEST);
        m_selectorPanelLeft.add(m_fontSelector, BorderLayout.CENTER);
        m_selectorPanelLeft.add(m_styleSelector, BorderLayout.EAST);
        m_fontSelector.setPreferredSize(new Dimension(180, 150));
        m_fontSelector.addChangeListener(m_previewJLabel);
        m_styleSelector.setPreferredSize(new Dimension(100, 150));
        m_styleSelector.addChangeListener(m_previewJLabel);
        m_selectorPanelRight.add(m_sizeSelector, BorderLayout.CENTER);
        m_selectorPanelRight.add(Box.createHorizontalStrut(0), BorderLayout.EAST);
        m_sizeSelector.setPreferredSize(new Dimension(70, 150));
        m_sizeSelector.addChangeListener(m_previewJLabel);
        m_previewPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        m_previewPanel.add(m_previewPanelLabel, BorderLayout.NORTH);
        m_previewPanel.add(m_previewJLabel, BorderLayout.CENTER);
        m_previewJLabel.setPreferredSize(new Dimension(-1, 100));
    }
}
