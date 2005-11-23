package com.hoardersoft.util;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Class used to display the about box for the bean generator.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSAboutDialog extends JDialog {
    private JPanel m_panel                  = new JPanel(new BorderLayout());
    private JPanel m_textPanel              = new JPanel(new BorderLayout());
    private JTextArea m_licenseTextArea     = new JTextArea();
    private JScrollPane m_licenseScrollPane = new JScrollPane(m_licenseTextArea);
    private JPanel m_buttonPanel            = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JButton m_okButton              = new JButton("OK");

    /**
     * Class constructor.
     *
     * @param product the product name
     */
    public HSAboutDialog(String product) {
        super();

        init(product);
    }

    /**
     * Class constructor that takes a parent dialog.
     *
     * @param product the product name
     * @param dialog the parent dialog (Dialog)
     */
    public HSAboutDialog(Dialog dialog, String product) {
        super(dialog);

        init(product);
    }

    /**
     * Class constructor that takes a parent frame.
     *
     * @param frame the parent frame (Frame)
     * @param product the product name
     */
    public HSAboutDialog(Frame frame, String product) {
        super(frame);

        init(product);
    }

    /**
     * Initialise the GUI.
     *
     * @param product the product name
     */
    private void init(String product) {
        try {
            jbInit();
            pack();
        }
        catch (Exception ex) {
            throw new Error("Error initialising GUI - HSAboutDialog");
        }

        // Set our size
        setSize(400, 260);
        m_licenseTextArea.setText(product + " " + HSVersionUtil.VERSION + ", Copyright (C) 2002 Richard Kent\n" + "\n" + product + " comes with ABSOLUTELY NO WARRANTY.\n" + "This is free software, and you are welcome to redistribute it\n" + "under certain conditions.\n" + "\n" + "See the file COPYING for the full license and further details.\n" + "\n" + "This work also uses the non-commercial version of the Plastic\n" + "look-and-feel by Karsten Lentzsch (see http://www.jgoodies.com).\n" + "Please refer to the license in the plastic-license directory.");
    }

    /**
     * Show the dialog.
     */
    public void showDialog() {
        if (getParent() != null) {
            setLocationRelativeTo(getParent());
        }

        setVisible(true);
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void m_okButton_actionPerformed() {
        dispose();
    }

    private void jbInit() {
        getContentPane().add(m_panel);
        m_panel.add(m_textPanel, BorderLayout.CENTER);
        m_panel.add(m_buttonPanel, BorderLayout.SOUTH);
        m_textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_textPanel.add(m_licenseScrollPane, BorderLayout.CENTER);
        m_licenseTextArea.setFont(new Font("SansSerif", 0, 12));
        m_licenseTextArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(178, 178, 178)));
        m_licenseTextArea.setEditable(false);
        m_buttonPanel.add(m_okButton, null);
        m_okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_okButton_actionPerformed();
            }
        });
        setModal(true);
        setResizable(false);
        setTitle("About");
    }
}
