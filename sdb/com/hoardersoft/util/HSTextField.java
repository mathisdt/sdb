package com.hoardersoft.util;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Text field that, on focus, selects all of its text
 * and on focus loss fires its action listeners.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSTextField extends JTextField {
    /**
     * Constructs a new text field. A default model is created,
     * the initial string is null, and the number of columns is set to 0.
     */
    public HSTextField() {
        super();
        
        init();
    }

    /**
     * Constructs a new empty text field with the specified
     * number of columns. A default model is created and the initial
     * string is set to null.
     *
     * @param columns the number of columns to use to calculate 
     *   the preferred width; if columns is set to zero, the
     *   preferred width will be whatever naturally results from
     *   the component implementation
     */
    public HSTextField(int columns) {
        super(columns);
        
        init();
    }

    /**
     * Constructs a new text field initialized with the
     * specified text. A default model is created and the number of
     * columns is 0.
     *
     * @param text the text to be displayed, or null
     */
    public HSTextField(String text) {
        super(text);
        
        init();
    }

    /**
     * Constructs a new text field initialized with the
     * specified text and columns. A default model is created.
     *
     * @param text the text to be displayed, or null
     * @param columns  the number of columns to use to calculate 
     *   the preferred width; if columns is set to zero, the
     *   preferred width will be whatever naturally results from
     *   the component implementation
     */
    public HSTextField(String text, int columns) {
        super(text, columns);
        
        init();
    }
    
    /**
     * Initialise the text field.
     */ 
    private void init() {
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                HSTextField.this.selectAll();
            }

            public void focusLost(FocusEvent e) {
                HSTextField.this.fireActionPerformed();
            }
        });
    }
}
