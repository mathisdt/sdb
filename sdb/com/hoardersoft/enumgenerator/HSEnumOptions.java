package com.hoardersoft.enumgenerator;

/**
 * Options for the enum generator.
 *
 * <ul>
 *   <li>indentSize      - the indent size to use</li>
 *   <li>variablePrefix  - the variable prefix to use</li>
 *   <li>createAsBean    - whether to create the enumerated type as a valid Java bean
 *                         (no argument constructor and get/set methods)</li>
 *   <li>printableString - whether to add "printable string" support</li>
 *   <li>printableVector - whether to add "printable vector" support</li>
 * </ul>
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
import com.hoardersoft.util.*;

public class HSEnumOptions {
    private static final String PREFS_INDENT_SIZE      = "IndentSize";
    private static final String PREFS_VARIABLE_PREFIX  = "VariablePrefix";
    private static final String PREFS_CREATE_AS_BEAN   = "CreateAsBean";
    private static final String PREFS_PRINTABLE_STRING = "PrintableString";
    private static final String PREFS_PRINTABLE_VECTOR = "PrintableVector";
    private int m_indentSize                           = 4;
    private String m_variablePrefix                    = "m_";
    private boolean m_createAsBean                     = false;
    private boolean m_printableString                  = false;
    private boolean m_printableVector                  = false;

    /**
     * Gets the indent size property.
     *
     * @return the indent size property
     */
    public int getIndentSize() {
        return m_indentSize;
    }

    /**
     * Sets the indent size property.
     *
     * @param indentSize the indent size property
     */
    public void setIndentSize(int indentSize) {
        m_indentSize = indentSize;
    }

    /**
     * Gets the variable prefix property.
     *
     * @return the variable prefix property
     */
    public String getVariablePrefix() {
        return m_variablePrefix;
    }

    /**
     * Sets the variable prefix property.
     *
     * @param variablePrefix the variable prefix property
     */
    public void setVariablePrefix(String variablePrefix) {
        m_variablePrefix = variablePrefix;
    }

    /**
     * Gets the create as bean property.
     *
     * @return the create as bean property
     */
    public boolean isCreateAsBean() {
        return m_createAsBean;
    }

    /**
     * Sets the create as bean property.
     *
     * @param createAsBean the create as bean property
     */
    public void setCreateAsBean(boolean createAsBean) {
        m_createAsBean = createAsBean;
    }

    /**
     * Gets the printable string property.
     *
     * @return the printable string property
     */
    public boolean isPrintableString() {
        return m_printableString;
    }

    /**
     * Sets the printable string property.
     *
     * @param printableString the printable string property
     */
    public void setPrintableString(boolean printableString) {
        m_printableString = printableString;
    }

    /**
     * Gets the printable vector property.
     *
     * @return the printable vector property
     */
    public boolean isPrintableVector() {
        return m_printableVector;
    }

    /**
     * Sets the printable vector property.
     *
     * @param printableVector the printable vector property
     */
    public void setPrintableVector(boolean printableVector) {
        m_printableVector = printableVector;
    }

    /**
     * Updates the options from the properties file.
     */
    public void updateFromPropertiesFile() {
        m_indentSize      = HSLogUtil.getIntegerProperty(PREFS_INDENT_SIZE, m_indentSize);
        m_variablePrefix  = HSLogUtil.getStringProperty(PREFS_VARIABLE_PREFIX, m_variablePrefix);
        m_createAsBean    = HSLogUtil.getBooleanProperty(PREFS_CREATE_AS_BEAN, m_createAsBean);
        m_printableString = HSLogUtil.getBooleanProperty(PREFS_PRINTABLE_STRING, m_printableString);
        m_printableVector = HSLogUtil.getBooleanProperty(PREFS_PRINTABLE_VECTOR, m_printableVector);
    }

    /**
     * Updates the properties file from the options.
     */
    public void updatePropertiesFile() {
        HSLogUtil.setIntegerProperty(PREFS_INDENT_SIZE, m_indentSize);
        HSLogUtil.setStringProperty(PREFS_VARIABLE_PREFIX, m_variablePrefix);
        HSLogUtil.setBooleanProperty(PREFS_CREATE_AS_BEAN, m_createAsBean);
        HSLogUtil.setBooleanProperty(PREFS_PRINTABLE_STRING, m_printableString);
        HSLogUtil.setBooleanProperty(PREFS_PRINTABLE_VECTOR, m_printableVector);
        HSLogUtil.saveProperties();
    }
}
