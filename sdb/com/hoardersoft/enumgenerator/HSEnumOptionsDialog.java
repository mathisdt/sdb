package com.hoardersoft.enumgenerator;

import com.hoardersoft.util.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Dialog used to edit enum generator options.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSEnumOptionsDialog extends JDialog {
    // GUI members
    private JPanel m_panel                       = new JPanel(new BorderLayout());
    private JPanel m_prefsPanel                  = new JPanel(new HSVerticalFlowLayout());
    private JPanel m_buttonPanel                 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JPanel m_indentSizePanel             = new JPanel(new BorderLayout());
    private JLabel m_indentSizeLabel             = new JLabel("Indent Size:");
    private JSlider m_indentSizeSlider           = new JSlider();
    private JLabel m_indentSizeValueLabel        = new JLabel();
    private JPanel m_variablePrefixPanel         = new JPanel(new BorderLayout());
    private JLabel m_variablePrefixLabel         = new JLabel("Variable Prefix:");
    private JTextField m_variablePrefixTextField = new JTextField();
    private JPanel m_createAsBeanPanel           = new JPanel(new BorderLayout());
    private JLabel m_createAsBeanLabel           = new JLabel("Create as Bean:");
    private JCheckBox m_createAsBeanCheckBox     = new JCheckBox();
    private JPanel m_printableStringPanel        = new JPanel(new BorderLayout());
    private JLabel m_printableStringLabel        = new JLabel("Printable String:");
    private JCheckBox m_printableStringCheckBox  = new JCheckBox();
    private JPanel m_printableVectorPanel        = new JPanel(new BorderLayout());
    private JLabel m_printableVectorLabel        = new JLabel("Printable Vector:");
    private JCheckBox m_printableVectorCheckBox  = new JCheckBox();
    private JButton m_okButton                   = new JButton("OK");
    private JButton m_cancelButton               = new JButton("Cancel");

    // Non-GUI members
    private boolean m_okPressed = false;

    /**
     * Class constructor.
     */
    public HSEnumOptionsDialog() {
        super();

        init();
    }

    /**
     * Class constructor that takes a parent dialog.
     *
     * @param dialog the parent dialog (Dialog)
     */
    public HSEnumOptionsDialog(Dialog dialog) {
        super(dialog);

        init();
    }

    /**
     * Class constructor that takes a parent frame.
     *
     * @param frame the parent frame (Frame)
     */
    public HSEnumOptionsDialog(Frame frame) {
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
            throw new Error("Error initialising GUI - HSEnumOptionsDialog");
        }
    }

    /**
     * Show the dialog.
     *
     * @param options the options
     * @return whether the OK button was pressed or not
     */
    public boolean showDialog(HSEnumOptions options) {
        // Update the GUI - limit our indent size to 0 to 20
        m_indentSizeSlider.setValue(HSMathUtil.checkRange(options.getIndentSize(), 0, 20));
        m_variablePrefixTextField.setText(options.getVariablePrefix());
        m_createAsBeanCheckBox.setSelected(options.isCreateAsBean());
        m_printableStringCheckBox.setSelected(options.isPrintableString());
        m_printableVectorCheckBox.setSelected(options.isPrintableVector());
        m_printableVectorCheckBox.setEnabled(m_printableStringCheckBox.isSelected());

        if (getParent() != null) {
            setLocationRelativeTo(getParent());
        }

        setVisible(true);

        if (m_okPressed) {
            options.setIndentSize(m_indentSizeSlider.getValue());
            options.setVariablePrefix(m_variablePrefixTextField.getText());
            options.setCreateAsBean(m_createAsBeanCheckBox.isSelected());
            options.setPrintableString(m_printableStringCheckBox.isSelected());
            options.setPrintableVector(m_printableVectorCheckBox.isSelected());
        }

        return m_okPressed;
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void m_indentSizeSlider_stateChanged() {
        m_indentSizeValueLabel.setText(String.valueOf(m_indentSizeSlider.getValue()));
    }

    private void m_printableStringCheckBox_actionPerformed() {
        m_printableVectorCheckBox.setEnabled(m_printableStringCheckBox.isSelected());
    }

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
        m_panel.add(m_prefsPanel, BorderLayout.CENTER);
        m_panel.add(m_buttonPanel, BorderLayout.SOUTH);
        m_prefsPanel.add(m_indentSizePanel, null);
        m_prefsPanel.add(m_variablePrefixPanel, null);
        m_prefsPanel.add(m_createAsBeanPanel, null);
        m_prefsPanel.add(m_printableStringPanel, null);
        m_prefsPanel.add(m_printableVectorPanel, null);
        m_indentSizePanel.add(m_indentSizeLabel, BorderLayout.WEST);
        m_indentSizePanel.add(m_indentSizeSlider, BorderLayout.CENTER);
        m_indentSizePanel.add(m_indentSizeValueLabel, BorderLayout.EAST);
        m_indentSizeLabel.setPreferredSize(new Dimension(65, 21));
        m_indentSizeSlider.setMaximum(20);
        m_indentSizeSlider.setPreferredSize(new Dimension(100, 21));
        m_indentSizeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                m_indentSizeSlider_stateChanged();
            }
        });
        m_indentSizeValueLabel.setPreferredSize(new Dimension(15, 21));
        m_variablePrefixPanel.add(m_variablePrefixLabel, BorderLayout.WEST);
        m_variablePrefixPanel.add(m_variablePrefixTextField, BorderLayout.CENTER);
        m_variablePrefixLabel.setPreferredSize(new Dimension(130, 21));
        m_createAsBeanPanel.add(m_createAsBeanLabel, BorderLayout.WEST);
        m_createAsBeanPanel.add(m_createAsBeanCheckBox, BorderLayout.CENTER);
        m_createAsBeanLabel.setPreferredSize(new Dimension(130, 21));
        m_printableStringPanel.add(m_printableStringLabel, BorderLayout.WEST);
        m_printableStringPanel.add(m_printableStringCheckBox, BorderLayout.CENTER);
        m_printableStringLabel.setPreferredSize(new Dimension(130, 21));
        m_printableStringCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_printableStringCheckBox_actionPerformed();
            }
        });
        m_printableVectorPanel.add(m_printableVectorLabel, BorderLayout.WEST);
        m_printableVectorPanel.add(m_printableVectorCheckBox, BorderLayout.CENTER);
        m_printableVectorLabel.setPreferredSize(new Dimension(130, 21));
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
        setResizable(false);
        setTitle("Options");
    }
}
