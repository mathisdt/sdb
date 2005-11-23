package com.hoardersoft.fontselector;

import com.hoardersoft.util.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * HoarderSoft selector list used for the font selector dialog. Consists of
 * a label, a textfield and a list. Intended to mimic the Microsoft font
 * selector dialogs.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSFontList extends JPanel {
    // GUI members
    private JLabel m_label               = new JLabel("<label>:");
    private JTextField m_textField       = new JTextField();
    private JPanel m_listPanel           = new JPanel(new BorderLayout());
    private JList m_list                 = new JList();
    private JScrollPane m_listScrollPane = new JScrollPane(m_list);

    // Non-GUI members
    private ArrayList m_listenerList = new ArrayList();
    private String m_labelText       = null;
    private boolean m_blockEvents    = false;

    /**
     * Class constructor.
     */
    public HSFontList() {
        init("<label>", new Object[0]);
    }

    /**
     * Class constructor.
     *
     * @param text the string text for the label
     */
    public HSFontList(String text) {
        init(text, new Object[0]);
    }

    /**
     * Class constructor that takes a label and list data.
     *
     * @param text the string text for the label
     * @param data the list data (object array)
     */
    public HSFontList(String text, Object[] data) {
        init(text, data);
    }

    /**
     * Initialises the list.
     *
     * @param text the string text for the label
     * @param data the list data (object array)
     */
    private void init(String text, Object[] data) {
        try {
            jbInit();
        }
        catch (Exception ex) {
            // Let's hope this never happens !!
            throw new Error("Error initialising GUI - HSFontList");
        }

        // We don't want any events fired for setting up code
        m_blockEvents = true;

        // Set up other components
        setLabelText(text);
        m_list.setListData(data);

        // Reenable events
        m_blockEvents = false;
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
     * Gets the list data as an object array.
     *
     * @return the list data (object array)
     */
    public Object[] getData() {
        Object[] data = new Object[m_list.getModel().getSize()];

        for (int i = 0; i < data.length; i++) {
            data[i] = m_list.getModel().getElementAt(i);
        }

        return data;
    }

    /**
     * Sets the list data using an object array.
     *
     * @param data the list data (object array)
     */
    public void setData(Object[] data) {
        m_list.setListData(data);
    }

    /**
     * Gets the value currently stored in the textfield.
     *
     * @return the value string from the textfield
     */
    public String getValue() {
        return m_textField.getText();
    }

    /**
     * Sets the value in the textfield. This will search the
     * current list of values and will select one if there is a
     * match (which also corrects any capitalisation). If there is
     * no match then the first matching entry is scrolled to.
     *
     * @param valueString the text value for the textfield
     * @return <code>true</code> if the value matched a value in the list;
     *         <code>false</code> otherwise.
     */
    public boolean setValue(String valueString) {
        boolean found     = false;
        Object item       = null;
        Integer itemInt   = null;
        String itemString = null;
        Integer valueInt  = null;
        int index         = -1;
        int n             = m_list.getModel().getSize();

        if ((valueString == null) || (valueString.length() == 0)) {
            // Just do nothing
            return false;
        }

        try {
            valueInt = new Integer(Integer.parseInt(valueString));
        }
        catch (NumberFormatException ex) {
            // Just ignore any exceptions for now - we'll check for null later....
        }

        for (int i = 0; (i < n) && !found; i++) {
            item = m_list.getModel().getElementAt(i);

            if (item instanceof Integer) {
                // Compare as integers
                if (valueInt == null) {
                    // Nothing we can do (our typed value isn't an integer) - just do nothing
                    return false;
                }

                itemInt = (Integer) item;

                if (valueInt.compareTo(itemInt) == 0) {
                    found = true;
                    index = i;
                }
                else if (valueInt.compareTo(itemInt) > 0) {
                    index = i;
                }
            }
            else {
                // Compare as strings
                itemString = item.toString();

                if (valueString.compareToIgnoreCase(itemString) == 0) {
                    found = true;
                    index = i;
                }
                else if (valueString.compareToIgnoreCase(itemString) > 0) {
                    index = i;
                }
            }
        }

        if (found) {
            // Select found item and scroll to it
            m_list.setSelectionInterval(index, index);
            m_list.ensureIndexIsVisible(index);
            m_textField.setText(m_list.getModel().getElementAt(index).toString());
        }
        else {
            // Deselect all items and scroll to first partial matching
            m_list.clearSelection();
            m_list.ensureIndexIsVisible(index + 1);
            m_textField.setText(valueString);
        }

        return found;
    }

    /**
     * Add a change listener to the font list.
     *
     * @param cl the change listener to add
     */
    public void addChangeListener(ChangeListener cl) {
        m_listenerList.add(cl);
    }

    /**
     * Remove a change listener from the font list.
     *
     * @param cl the change listener to remove
     */
    public void removeChangeListener(ChangeListener cl) {
        m_listenerList.remove(cl);
    }

    /**
     * Updates the list of listeners (telling them the font list selection has changed).
     */
    private void updateListeners() {
        // Tell all our listeners that our state has changed
        ChangeEvent e = new ChangeEvent(this);

        for (int i = 0; i < m_listenerList.size(); i++) {
            ((ChangeListener) m_listenerList.get(i)).stateChanged(e);
        }
    }

    /**
     * Adds a key listener.
     *
     * @param l the key listener
     */
    public void addKeyListener(KeyListener l) {
        m_textField.addKeyListener(l);
        m_list.addKeyListener(l);
    }

    /**
     * Removes a key listener.
     *
     * @param l the key listener
     */
    public void removeKeyListener(KeyListener l) {
        m_textField.removeKeyListener(l);
        m_list.removeKeyListener(l);
    }

    // ---------------------------------------------------------------------------------
    // All code beneath here is GUI callback code
    // ---------------------------------------------------------------------------------
    private void m_textField_keyReleased() {
        if (!m_blockEvents) {
            setValue(m_textField.getText());
            updateListeners();
        }
    }

    private void m_list_valueChanged(ListSelectionEvent e) {
        if (!m_blockEvents) {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = m_list.getSelectedIndex();

                if ((selectedRow >= 0) && (selectedRow < m_list.getModel().getSize())) {
                    String listSelection = m_list.getModel().getElementAt(selectedRow).toString();

                    m_textField.setText(listSelection);
                    updateListeners();
                }
            }
        }
    }

    private void jbInit() {
        setLayout(new HSVerticalFlowLayout(HSVerticalFlowLayout.TOP, 0, 0, true, true));
        add(m_label, null);
        add(m_textField, null);
        add(m_listPanel, null);
        m_label.setPreferredSize(new Dimension(100, 20));
        m_textField.setPreferredSize(new Dimension(100, 20));
        m_textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                m_textField_keyReleased();
            }
        });
        m_listPanel.add(m_listScrollPane, BorderLayout.CENTER);
        m_listScrollPane.setPreferredSize(new Dimension(100, 80));
        m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                m_list_valueChanged(e);
            }
        });
    }
}
