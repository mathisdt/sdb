package com.hoardersoft.fontselector;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * HoarderSoft font selector dialog.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSFontSelector extends JDialog {
    // GUI members
    private JPanel m_panel                          = new JPanel(new BorderLayout());
    private HSFontSelectorPanel m_fontSelectorPanel = new HSFontSelectorPanel();
    private JPanel m_buttonPanel                    = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JButton m_okButton                      = new JButton("OK");
    private JButton m_cancelButton                  = new JButton("Abbrechen");

    // Non-GUI members
    private boolean m_okPressed   = false;
    private boolean m_blockEvents = false;

    /**
     * Class constructor.
     */
    public HSFontSelector() {
        this(null, "Schriftart wählen");
    }

    /**
     * Class constructor that takes a font.
     *
     * @param font the initial font for the dialog
     */
    public HSFontSelector(Font font) {
        this(null, "Schriftart wählen", font);
    }

    /**
     * Class constructor that takes a frame and title.
     *
     * @param parent the parent frame
     * @param title the title for the dialog
     */
    public HSFontSelector(Frame parent, String title) {
        super(parent, title);

        init(m_fontSelectorPanel.getCurrentFont());
    }

    /**
     * Class constructor that takes a frame, title and font.
     *
     * @param parent the parent frame
     * @param title the title for the dialog
     * @param font the initial font for the dialog
     */
    public HSFontSelector(Frame parent, String title, Font font) {
        super(parent, title);

        init(font);
    }

    /**
     * Add an action listener to the font selector.
     *
     * @param al the action listener to add
     */
    public void addActionListener(ActionListener al) {
        m_fontSelectorPanel.addActionListener(al);
    }

    /**
     * Remove an action listener from the font selector.
     *
     * @param al the action listener to remove
     */
    public void removeActionListener(ActionListener al) {
        m_fontSelectorPanel.removeActionListener(al);
    }

    /**
     * Initialises the selector.
     *
     * @param font the initial font for the selector
     */
    private void init(Font font) {
        // We don't want any events fired for setting up code
        m_blockEvents = true;

        try {
            jbInit();
            pack();
        }
        catch (Exception ex) {
            // Let's hope this never happens !!
            throw new Error("Error initialising GUI - HSFontSelector");
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cancelPressed();
            }
        });

        // Set the default button
        setDefaultButton();

        // Set the keyboard shortcuts
        KeyListener keyListener = new ListKeyListener();

        m_fontSelectorPanel.addKeyListener(keyListener);
        m_okButton.addKeyListener(keyListener);
        m_cancelButton.addKeyListener(keyListener);

        // Finally set the font
        m_fontSelectorPanel.setCurrentFont(font);

        // Reenable events
        m_blockEvents = false;
    }

    /**
     * Sets the default button.
     */
    private void setDefaultButton() {
        disableDefaultButtons(this);
        m_okButton.setDefaultCapable(true);
    }

    /**
     * Recursively sets all JButtons to not being capable of
     * being defaults. Why on earth isn't this standard for
     * JButtons ?
     *
     * @param comp the component in which to disable all default buttons
     */
    private void disableDefaultButtons(Component comp) {
        if (comp instanceof Container) {
            // Call for all children
            Container cont = (Container) comp;
            int n          = cont.getComponentCount();

            for (int i = 0; i < n; i++) {
                disableDefaultButtons(cont.getComponent(i));
            }
        }

        if (comp instanceof JButton) {
            JButton button = (JButton) comp;

            button.setDefaultCapable(false);
        }
    }

    /**
     * Shows the modal dialog.
     *
     * @return the selected font (null if cancel was pressed)
     */
    public Font showDialog() {
        setVisible(true);

        if (m_okPressed) {
            return getCurrentFont();
        }
        else {
            return null;
        }
    }

    /**
     * Gets the selected font.
     *
     * @return the selected font (null if cancel was pressed)
     */
    public Font getCurrentFont() {
        return m_fontSelectorPanel.getCurrentFont();
    }

    /**
     * Sets the currently selected font in the dialog.
     *
     * @param font the font to set the dialog to (sets
     *             the font name, style and size selectors)
     */
    public void setCurrentFont(Font font) {
        m_fontSelectorPanel.setCurrentFont(font);
    }

    /**
     * Called when ok is pressed. This can be overridden if required.
     */
    public void ok() {
        // Default does nothing
    }

    /**
     * Called when cancel is pressed. This can be overridden if required.
     */
    public void cancel() {
        // Default does nothing
    }

    /**
     * Performs an ok.
     */
    private void okPressed() {
        // Check to see we have valid fonts information in the textfields
        if (m_fontSelectorPanel.checkFont()) {
            // We have a valid font
            m_okPressed = true;

            setVisible(false);
            dispose();
            ok();
        }
    }

    /**
     * Performs a cancel.
     */
    private void cancelPressed() {
        m_okPressed = false;

        setVisible(false);
        dispose();
        cancel();
    }

    /**
     * Private class to capture key events.
     */
    private class ListKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                okPressed();
            }
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cancelPressed();
            }
        }
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private final void m_okButton_actionPerformed() {
        if (!m_blockEvents) {
            okPressed();
        }
    }

    private final void m_cancelButton_actionPerformed() {
        if (!m_blockEvents) {
            cancelPressed();
        }
    }

    private void jbInit() {
        getContentPane().add(m_panel);
        m_panel.add(m_fontSelectorPanel, BorderLayout.CENTER);
        m_panel.add(m_buttonPanel, BorderLayout.SOUTH);
        m_buttonPanel.add(m_okButton, null);
        m_buttonPanel.add(m_cancelButton, null);
        m_okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_okButton_actionPerformed();
            }
        });
        m_cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_cancelButton_actionPerformed();
            }
        });
        setModal(true);
        setResizable(true);
    }
}
