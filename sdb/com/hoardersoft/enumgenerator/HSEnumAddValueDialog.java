package com.hoardersoft.enumgenerator;

import com.hoardersoft.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Class used to add enum values.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSEnumAddValueDialog extends JDialog {
    // GUI members
    private JPanel m_panel                   = new JPanel(new BorderLayout());
    private JPanel m_enumValuesPanel         = new JPanel(new HSVerticalFlowLayout());
    private JPanel m_buttonPanel             = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JButton m_cancelButton           = new JButton("Cancel");
    private JButton m_okButton               = new JButton("OK");
    private JPanel m_enumValueTextFieldPanel = new JPanel(new BorderLayout());
    private JLabel m_enumValuesLabel         = new JLabel("Enum Values:");
    private JTextField m_enumValuesTextField = new JTextField();

    // Non-GUI members
    private boolean m_okPressed   = false;
    private String[] m_enumValues = null;

    /**
     * Class constructor.
     */
    public HSEnumAddValueDialog() {
        super();

        init();
    }

    /**
     * Class constructor that takes a parent dialog.
     *
     * @param dialog the parent dialog (Dialog)
     */
    public HSEnumAddValueDialog(Dialog dialog) {
        super(dialog);

        init();
    }

    /**
     * Class constructor that takes a parent frame.
     *
     * @param frame the parent frame (Frame)
     */
    public HSEnumAddValueDialog(Frame frame) {
        super(frame);

        init();
    }

    /**
     * Initialise the GUI.
     */
    private void init() {
        try {
            jbInit();
            pack();
        }
        catch (Exception ex) {
            throw new Error("Error initialising GUI - HSBeanPreferences");
        }
    }

    /**
     * Show the dialog.
     *
     * @return whether the OK button was pressed or not
     */
    public boolean showDialog() {
        if (getParent() != null) {
            setLocationRelativeTo(getParent());
        }

        setVisible(true);

        if (m_okPressed) {
            // Parse comma/separated string into string array
            m_enumValues = HSStringUtil.getStringArray(m_enumValuesTextField.getText(), ", ");

            // Enumerated values are always upper case and must be valid identifiers
            for (int i = 0; i < m_enumValues.length; i++) {
                m_enumValues[i] = m_enumValues[i].toUpperCase();
                m_enumValues[i] = HSStringUtil.ensureValidIdentifier(m_enumValues[i]);
            }

            // Remove duplicates
            m_enumValues = HSStringUtil.removeDuplicates(m_enumValues);
        }

        return m_okPressed;
    }

    /**
     * Gets the entered enum values.
     *
     * @return the string array of enetered enum values
     */
    public String[] getEnumValues() {
        return m_enumValues;
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void m_okButton_actionPerformed() {
        m_okPressed = true;

        dispose();
    }

    private void m_cancelButton_actionPerformed() {
        m_okPressed = false;

        dispose();
    }

    private void jbInit() {
        getContentPane().add(m_panel);
        m_panel.add(m_enumValuesPanel, BorderLayout.CENTER);
        m_panel.add(m_buttonPanel, BorderLayout.SOUTH);
        m_enumValuesPanel.add(m_enumValueTextFieldPanel, null);
        m_enumValueTextFieldPanel.add(m_enumValuesLabel, BorderLayout.WEST);
        m_enumValueTextFieldPanel.add(m_enumValuesTextField, BorderLayout.CENTER);
        m_enumValuesLabel.setPreferredSize(new Dimension(80, 21));
        m_enumValuesTextField.setPreferredSize(new Dimension(200, 21));
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
        setTitle("Add Enum Values");
    }
}
