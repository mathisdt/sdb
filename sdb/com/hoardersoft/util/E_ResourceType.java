package com.hoardersoft.util;

import java.io.*;
import java.util.*;

/**
 * Enumerated class for resource types.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public final class E_ResourceType implements Serializable {
    private static ArrayList m_enumList = new ArrayList();

    /** STRING */
    public static final E_ResourceType STRING = new E_ResourceType("STRING");

    /** ICON */
    public static final E_ResourceType ICON = new E_ResourceType("ICON");

    /** CHAR */
    public static final E_ResourceType CHAR = new E_ResourceType("CHAR");

    /** INT */
    public static final E_ResourceType INT = new E_ResourceType("INT");

    /** BOOLEAN */
    public static final E_ResourceType BOOLEAN = new E_ResourceType("BOOLEAN");

    // Static count
    private static final int m_count = m_enumList.size();

    // Private members
    private int m_value;
    private String m_description;

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
     * Returns the enumerated value for a given integer.
     *
     * @param i the integer to return the enumerated value for
     * @return the enumerated value matching the integer (or null if no match found)
     */
    public static E_ResourceType fromInt(int i) {
        if ((i >= 0) && (i < m_enumList.size())) {
            return (E_ResourceType) m_enumList.get(i);
        }

        return null;
    }

    /**
     * Returns the enumerated value for a given string.
     *
     * @param s the string to return the enumerated value for
     * @return the enumerated value matching the string (or null if no match found)
     */
    public static E_ResourceType fromString(String s) {
        for (Iterator iter = m_enumList.iterator(); iter.hasNext(); ) {
            E_ResourceType enum1 = (E_ResourceType) iter.next();

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
        return ((rhs instanceof E_ResourceType) && (m_value == ((E_ResourceType) rhs).m_value));
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
     */
    private E_ResourceType(String description) {
        m_value       = m_enumList.size();
        m_description = description;

        m_enumList.add(this);
    }
}
