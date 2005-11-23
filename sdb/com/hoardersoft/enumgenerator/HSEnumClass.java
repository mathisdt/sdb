package com.hoardersoft.enumgenerator;

/**
 * Description for an enumerated class.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
import java.util.*;

public class HSEnumClass {
    private String m_className     = "E_CustomEnum";
    private ArrayList m_enumValues = new ArrayList();

    /**
     * Gets the class name property.
     *
     * @return the class name property
     */
    public String getClassName() {
        return m_className;
    }

    /**
     * Sets the class name property.
     *
     * @param className the class name property
     */
    public void setClassName(String className) {
        m_className = className;
    }

    /**
     * Gets an element of the enum values property at a given index.
     *
     * @param index the index of the element to get
     * @return the element of the enum values property at the given index
     */
    public String getEnumValues(int index) {
        return (String) m_enumValues.get(index);
    }

    /**
     * Gets an iterator for the enum values property.
     *
     * @return an iterator for the enum values property
     */
    public Iterator getEnumValuesIterator() {
        return m_enumValues.iterator();
    }

    /**
     * Gets the number of elements in the enum values property.
     *
     * @return the number of elements in the enum values property
     */
    public int getEnumValuesCount() {
        return m_enumValues.size();
    }

    /**
     * Returns whether the enum values property contains a given element.
     *
     * @param enumValue the element to look for
     * @return whether the enum values property contains the given element
     */
    public boolean containsEnumValues(String enumValue) {
        return m_enumValues.contains(enumValue);
    }

    /**
     * Adds an element to the enum values property.
     *
     * @param enumValue the element to add to the enum values property
     */
    public void addEnumValues(String enumValue) {
        m_enumValues.add(enumValue);
    }

    /**
     * Adds an element to the enum values property at a given index.
     *
     * @param index the index of the element to add
     * @param enumValue the element to add to the enum values property at the given index
     */
    public void addEnumValues(int index, String enumValue) {
        m_enumValues.add(index, enumValue);
    }

    /**
     * Removes an element from the enum values property.
     *
     * @param enumValue the element to remove from the enum values property
     */
    public void removeEnumValues(String enumValue) {
        m_enumValues.remove(enumValue);
    }

    /**
     * Removes an element from the enum values property at a given index.
     *
     * @param index the index of the element to remove
     */
    public void removeEnumValues(int index) {
        m_enumValues.remove(index);
    }

    /**
     * Returns the enum values property as an array.
     *
     * @return the enum values property as an array
     */
    public String[] getEnumValuesAsArray() {
        return (String[]) m_enumValues.toArray(new String[0]);
    }
}
