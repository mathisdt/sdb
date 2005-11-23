package com.hoardersoft.enumgenerator;

import com.hoardersoft.menu.*;
import com.hoardersoft.statusbar.*;
import com.hoardersoft.toolbar.*;
import com.hoardersoft.util.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.*;

/**
 * Class holding the main enum generator dialog.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSEnumGeneratorDialog extends JFrame {
    // GUI members
    private JPanel m_toolbarPanel            = new JPanel(new BorderLayout());
    private JPanel m_classNamePanel          = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
    private JLabel m_classNameLabel          = new JLabel("Class:");
    private JTextField m_classNameTextField  = new JTextField();
    private JPanel m_topPanel                = new JPanel(new BorderLayout());
    private JList m_enumValueList            = new JList();
    private JScrollPane m_enumValueListPanel = HSBeanUtil.createScrollPane(m_enumValueList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JTextArea m_sourceTextArea       = new JTextArea();
    private JScrollPane m_sourcePanel        = HSBeanUtil.createScrollPane(m_sourceTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private JSplitPane m_splitPane           = HSBeanUtil.createSplitPane(JSplitPane.VERTICAL_SPLIT, m_topPanel, m_sourcePanel);
    private HSStatusBar m_statusBar          = new HSStatusBar();

    // Non-GUI members
    private HSEnumClass m_class     = new HSEnumClass();
    private HSEnumOptions m_options = new HSEnumOptions();

    /**
     * Class constructor.
     */
    public HSEnumGeneratorDialog() {
        super();

        try {
            jbInit();
            pack();
        }
        catch (Exception ex) {
            throw new Error("Error initialising GUI - HSEnumGeneratorDialog");
        }

        // Set the initial size
        setSize(420, 600);

        // Set up the toolbar
        HSToolbar toolbar = new HSToolbar(m_statusBar);

        toolbar.add(m_newAction);
        toolbar.addDivider();
        toolbar.add(m_deleteAction);
        toolbar.add(m_moveUpAction);
        toolbar.add(m_moveDownAction);
        toolbar.addDivider();
        toolbar.add(m_copyToClipboardAction);
        m_toolbarPanel.add(toolbar, BorderLayout.CENTER);

        // Set up the menu bar
        JMenuBar menubar = new JMenuBar();
        HSMenu fileMenu  = new HSMenu("File");
        HSMenu editMenu  = new HSMenu("Edit");
        HSMenu helpMenu  = new HSMenu("Help");

        fileMenu.add(m_newAction);
        fileMenu.addSeparator();
        fileMenu.add(m_exitAction);
        editMenu.add(m_deleteAction);
        editMenu.add(m_moveUpAction);
        editMenu.add(m_moveDownAction);
        editMenu.addSeparator();
        editMenu.add(m_copyToClipboardAction);
        editMenu.addSeparator();
        editMenu.add(m_optionsAction);
        helpMenu.add(m_aboutAction);
        menubar.add(fileMenu);
        menubar.add(editMenu);
        menubar.add(helpMenu);
        setJMenuBar(menubar);

        // Add a close window listener
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                HSEnumGeneratorDialog.this.windowClosing();
            }
        });

        // Update our internal prefences
        m_options.updateFromPropertiesFile();

        // Update the GUI
        updateGUI();
    }

    /**
     * Show the dialog.
     */
    public void showDialog() {
        setVisible(true);
    }

    /**
     * Updates the GUI.
     */
    private void updateGUI() {
        // Update the class name
        m_classNameTextField.setText(m_class.getClassName());

        // Update the list of enumerated values
        m_enumValueList.setListData(m_class.getEnumValuesAsArray());

        // Update source code
        updateSourceCode();

        // Update the menu items
        updateMenuItems();
    }

    /**
     * Updates the source code.
     */
    private void updateSourceCode() {
        m_sourceTextArea.setText(HSEnumCodeGenerator.getEnumCode(m_class, m_options));

        // Always scroll to the start
        m_sourceTextArea.select(0, 0);
    }

    /**
     * Updates the state of the menu items.
     */
    private void updateMenuItems() {
        int nSelected = m_enumValueList.getSelectedIndices().length;

        if (nSelected > 1) {
            m_deleteAction.setEnabled(true);
            m_moveUpAction.setEnabled(false);
            m_moveDownAction.setEnabled(false);
        }
        else {
            int selected = getSelectedEnumValueIndex();
            int nEnums   = m_class.getEnumValuesCount();

            m_deleteAction.setEnabled((selected >= 0) && (selected < nEnums));
            m_moveUpAction.setEnabled((selected > 0) && (selected < nEnums));
            m_moveDownAction.setEnabled((selected >= 0) && (selected < (nEnums - 1)));
        }
    }

    /**
     * Returns the currently selected enum value.
     *
     * @return the currently selected enum value (null if none currently selected)
     */
    private String getSelectedEnumValue() {
        int selected = getSelectedEnumValueIndex();

        if (selected >= 0) {
            return m_class.getEnumValues(selected);
        }

        return null;
    }

    /**
     * Returns the currently selected enum value index.
     *
     * @return the currently selected enum value index
     */
    private int getSelectedEnumValueIndex() {
        // Crap Swing component can return current index out of bounds !!
        int selected = m_enumValueList.getSelectedIndex();

        if ((selected >= 0) && (selected < m_class.getEnumValuesCount())) {
            return selected;
        }

        return -1;
    }

    /**
     * Called when the class name is edited.
     */
    private void updateClassName() {
        // Ensure the class name is a valid identifier
        String className = m_classNameTextField.getText().trim();

        m_class.setClassName(HSStringUtil.ensureValidIdentifier(className, m_class.getClassName()));
        updateGUI();
    }

    /**
     * Called when the program is closing.
     */
    private void windowClosing() {
        // Update our preferences and write them out
        m_options.updatePropertiesFile();

        // We can now exit safely
        System.exit(0);
    }

    /**
     * Called to show the options dialog.
     */
    private void showOptionsDialog() {
        HSEnumOptionsDialog optionsDialog = new HSEnumOptionsDialog(this);

        if (optionsDialog.showDialog(m_options)) {
            updateGUI();
        }
    }

    /**
     * Called when the new button is pressed.
     */
    private void newEnumValues() {
        HSEnumAddValueDialog addDialog = new HSEnumAddValueDialog(this);

        if (addDialog.showDialog()) {
            String[] enumValues = addDialog.getEnumValues();

            for (int i = 0; i < enumValues.length; i++) {
                // Ensure we have a valid value (and don't already have this value)
                if ((enumValues[i] != null) && (enumValues[i].length() > 0) && !m_class.containsEnumValues(enumValues[i])) {
                    m_class.addEnumValues(enumValues[i]);
                }
            }

            updateGUI();
        }
    }

    /**
     * Called when the delete button is pressed.
     */
    private void deleteEnumValues() {
        int firstSelected = getSelectedEnumValueIndex();
        int[] selected    = m_enumValueList.getSelectedIndices();
        int nEnums        = m_class.getEnumValuesCount();
        ArrayList remove  = new ArrayList();

        for (int i = 0; i < selected.length; i++) {
            if ((selected[i] >= 0) && (selected[i] < nEnums)) {
                remove.add(m_class.getEnumValues(selected[i]));
            }
        }

        for (Iterator iter = remove.iterator(); iter.hasNext(); ) {
            m_class.removeEnumValues((String) iter.next());
        }

        updateGUI();

        // Re-select for user convienience
        if (firstSelected >= m_class.getEnumValuesCount()) {
            firstSelected = m_class.getEnumValuesCount() - 1;
        }

        m_enumValueList.setSelectedIndex(firstSelected);
    }

    /**
     * Called when the move up button is pressed.
     */
    private void moveEnumValueUp() {
        int selected = getSelectedEnumValueIndex();

        if (selected > 0) {
            String enumValue = getSelectedEnumValue();

            m_class.removeEnumValues(selected--);
            m_class.addEnumValues(selected, enumValue);
            updateGUI();

            // Re-select for user convienience
            m_enumValueList.setSelectedIndex(selected);
        }
    }

    /**
     * Called when the move down button is pressed.
     */
    private void moveEnumValueDown() {
        int selected = getSelectedEnumValueIndex();
        int nEnums   = m_class.getEnumValuesCount();

        if ((selected >= 0) && (selected < (nEnums - 1))) {
            String enumValue = getSelectedEnumValue();

            m_class.removeEnumValues(selected++);
            m_class.addEnumValues(selected, enumValue);
            updateGUI();

            // Re-select for user convienience
            m_enumValueList.setSelectedIndex(selected);
        }
    }

    /**
     * Called to show the about dialog.
     */
    private void showAboutDialog() {
        HSAboutDialog aboutDialog = new HSAboutDialog(this, "HSEnumGenerator");

        aboutDialog.showDialog();
    }

    /**
     * Called to copy the current text to the clipboard.
     */
    private void copyToClipboard() {
        // Now transfer this data to the system clipboard
        Clipboard clip       = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection data = new StringSelection(m_sourceTextArea.getText());

        clip.setContents(data, data);
    }

    // ---------------------------------------------------------------------------------
    // Actions
    // ---------------------------------------------------------------------------------
    private Action m_newAction = new HSAbstractAction("New", HSIconUtil.loadImage("New.png"), "Creates new enum values") {
        public void actionPerformed(ActionEvent e) {
            newEnumValues();
        }
    };
    private Action m_deleteAction = new HSAbstractAction("Delete", HSIconUtil.loadImage("Delete.png"), "Deletes an enum value") {
        public void actionPerformed(ActionEvent e) {
            deleteEnumValues();
        }
    };
    private Action m_moveUpAction = new HSAbstractAction("Move Up", HSIconUtil.loadImage("MoveUp.png"), "Moves an enum value up") {
        public void actionPerformed(ActionEvent e) {
            moveEnumValueUp();
        }
    };
    private Action m_moveDownAction = new HSAbstractAction("Move Down", HSIconUtil.loadImage("MoveDown.png"), "Moves an enum value down") {
        public void actionPerformed(ActionEvent e) {
            moveEnumValueDown();
        }
    };
    private Action m_exitAction = new HSAbstractAction("Exit") {
        public void actionPerformed(ActionEvent e) {
            HSEnumGeneratorDialog.this.windowClosing();
        }
    };
    private Action m_optionsAction = new HSAbstractAction("Options...") {
        public void actionPerformed(ActionEvent e) {
            showOptionsDialog();
        }
    };
    private Action m_aboutAction = new HSAbstractAction("About...") {
        public void actionPerformed(ActionEvent e) {
            showAboutDialog();
        }
    };
    private Action m_copyToClipboardAction = new HSAbstractAction("Copy to Clipboard", HSIconUtil.loadImage("CopyToClipboard.png"), "Copies the current source to the system clipboard") {
        public void actionPerformed(ActionEvent e) {
            copyToClipboard();
        }
    };

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void m_classNameTextField_focusLost() {
        updateClassName();
    }

    private void m_classNameTextField_actionPerformed() {
        updateClassName();
    }

    private void m_enumValueList_valueChanged() {
        updateMenuItems();
    }

    private void jbInit() {
        getContentPane().add(m_toolbarPanel, BorderLayout.NORTH);
        getContentPane().add(m_splitPane, BorderLayout.CENTER);
        getContentPane().add(m_statusBar, BorderLayout.SOUTH);
        m_toolbarPanel.add(m_classNamePanel, BorderLayout.EAST);
        m_classNamePanel.add(m_classNameLabel, null);
        m_classNamePanel.add(Box.createHorizontalStrut(5), null);
        m_classNamePanel.add(m_classNameTextField, null);
        m_classNameTextField.setPreferredSize(new Dimension(120, 21));
        m_classNameTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                m_classNameTextField_focusLost();
            }
        });
        m_classNameTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_classNameTextField_actionPerformed();
            }
        });
        m_splitPane.setDividerLocation(100);
        m_topPanel.add(m_enumValueListPanel, BorderLayout.CENTER);
        m_enumValueList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                m_enumValueList_valueChanged();
            }
        });
        m_sourceTextArea.setFont(new Font("DialogInput", 0, 11));
        m_sourceTextArea.setEditable(false);
        m_sourceTextArea.setSelectionColor(new Color(10, 36, 106));
        m_statusBar.setPreferredSize(new Dimension(1000, 22));
        setResizable(true);
        setTitle("Enum Generator");
    }
}
