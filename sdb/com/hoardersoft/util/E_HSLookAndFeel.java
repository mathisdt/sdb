package com.hoardersoft.util;

import javax.swing.*;

import java.io.*;
import java.util.*;

/**
 * Enumerated class for look and feels.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
import java.io.*;
import java.util.*;

public final class E_HSLookAndFeel implements Serializable {
    private static ArrayList m_enumList = new ArrayList();

    /** METAL */
    public static final E_HSLookAndFeel METAL = new E_HSLookAndFeel("METAL", "javax.swing.plaf.metal.MetalLookAndFeel", false);

    /** SYSTEM */
    public static final E_HSLookAndFeel SYSTEM = new E_HSLookAndFeel("SYSTEM", UIManager.getSystemLookAndFeelClassName(), false);

    /** PLASTIC */
    public static final E_HSLookAndFeel PLASTIC = new E_HSLookAndFeel("PLASTIC", "com.jgoodies.plaf.plastic.PlasticLookAndFeel", true);

    /** PLASTIC_3D */
    public static final E_HSLookAndFeel PLASTIC_3D = new E_HSLookAndFeel("PLASTIC_3D", "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel", true);

    /** PLASTIC_XP */
    public static final E_HSLookAndFeel PLASTIC_XP = new E_HSLookAndFeel("PLASTIC_XP", "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel", true);

    // Static count
    private static final int m_count = m_enumList.size();

    // Private members
    private int m_value;
    private String m_description;
    private String m_lookAndFeel;
    private boolean m_plastic;

    /**
     * Returns the number of enumerated values.
     *
     * @return the number of enumerated values
     */
    public static int getValueCount() {
        return m_count;
    }

    /**
     * Returns an iterator for the enumerated values.
     *
     * @return an iterator for the enumerated values
     */
    public static Iterator iterator() {
        return m_enumList.iterator();
    }

    /**
     * Returns the integer for this enumerated value.
     *
     * @return the integer for this enumerated value
     */
    public int toInt() {
        return m_value;
    }

    /**
     * Returns the string for this enumerated value.
     *
     * @return the string for this enumerated value
     */
    public String toString() {
        return m_description;
    }

    /**
     * Returns the look and feel string.
     *
     * @return the look and feel string
     */
    public String getLookAndFeel() {
        return m_lookAndFeel;
    }

    /**
     * Returns whether this is a plastic look and feel.
     *
     * @return whether this is a plastic look and feel
     */
    public boolean isPlastic() {
        return m_plastic;
    }

    /**
     * Returns the enumerated value for a given integer.
     *
     * @param i the integer to return the enumerated value for
     * @return the enumerated value matching the integer (or null if no match found)
     */
    public static E_HSLookAndFeel fromInt(int i) {
        if ((i >= 0) && (i < m_enumList.size())) {
            return (E_HSLookAndFeel) m_enumList.get(i);
        }

        return null;
    }

    /**
     * Returns the enumerated value for a given look and feel string.
     *
     * @param lookAndFeel the look and feel string to return the enumerated value for
     * @return the enumerated value matching the look and feel string (or null if no match found)
     */
    public static E_HSLookAndFeel fromLookAndFeel(String lookAndFeel) {
        for (Iterator iter = m_enumList.iterator(); iter.hasNext(); ) {
            E_HSLookAndFeel enum1 = (E_HSLookAndFeel) iter.next();

            if (enum1.m_lookAndFeel.equals(lookAndFeel)) {
                return enum1;
            }
        }

        return null;
    }

    /**
     * Returns the enumerated value for a given string.
     *
     * @param s the string to return the enumerated value for
     * @return the enumerated value matching the string (or null if no match found)
     */
    public static E_HSLookAndFeel fromString(String s) {
        for (Iterator iter = m_enumList.iterator(); iter.hasNext(); ) {
            E_HSLookAndFeel enum1 = (E_HSLookAndFeel) iter.next();

            if (enum1.m_description.equals(s)) {
                return enum1;
            }
        }

        return null;
    }

    /**
     * Compares this enumerated value with another object.
     *
     * @param rhs the object for comparison
     * @return whether the object for comparison is equal to this enumerated value
     */
    public boolean equals(Object rhs) {
        return ((rhs instanceof E_HSLookAndFeel) && (m_value == ((E_HSLookAndFeel) rhs).m_value));
    }

    /**
     * Returns a hash code for this enumerated value (required for hashed storage).
     *
     * @return a hash code for this enumerated value
     */
    public int hashCode() {
        int result = 17;

        result = 37 * result + m_value;

        return result;
    }

    /**
     * Private constructor that creates a new preset enumerated value.
     *
     * @param description the descriptive text for the enumerated value
     * @param lookAndFeel the look and feel (class name)
     * @param plastic whether this is a plastic look and feel
     */
    private E_HSLookAndFeel(String description, String lookAndFeel, boolean plastic) {
        m_value       = m_enumList.size();
        m_description = description;
        m_lookAndFeel = lookAndFeel;
        m_plastic     = plastic;

        m_enumList.add(this);
    }
}
