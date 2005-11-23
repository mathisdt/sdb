package com.hoardersoft.enumgenerator;

import java.util.*;

/**
 * Class responsible for all enum code generation.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSEnumCodeGenerator {
    /**
     * Returns the enum code.
     *
     * @param enumClass the enum class to return the code for
     * @param options the options
     * @return the enum code
     */
    public static String getEnumCode(HSEnumClass enumClass, HSEnumOptions options) {
        if (enumClass.getEnumValuesCount() == 0) {
            return "";
        }

        String className        = enumClass.getClassName();
        String variablePrefix   = options.getVariablePrefix();
        int indentSize          = options.getIndentSize();
        boolean createAsBean    = options.isCreateAsBean();
        boolean printableString = options.isPrintableString();
        boolean printableVector = options.isPrintableVector();

        // We must get our "code" variable prefix (for use in generated, non-static, non-declaration code)
        String codeVariablePrefix = variablePrefix;

        if (codeVariablePrefix.length() == 0) {
            codeVariablePrefix = "this.";
        }

        StringBuffer buffer  = new StringBuffer();
        String indentString1 = getIndentString(indentSize);
        String indentString2 = indentString1 + indentString1;
        String indentString3 = indentString2 + indentString1;
        String indentString4 = indentString3 + indentString1;
        String indentString5 = indentString4 + indentString1;

        buffer.append("import java.io.*;\n");
        buffer.append("import java.util.*;\n");
        buffer.append("\n");
        buffer.append("public final class " + className + " implements Serializable\n");
        buffer.append("{\n");
        buffer.append(indentString1 + "private static ArrayList " + variablePrefix + "enumList = new ArrayList();\n");
        buffer.append(indentString1 + "\n");

        for (Iterator iter = enumClass.getEnumValuesIterator(); iter.hasNext(); ) {
            String enumValue = (String) iter.next();

            buffer.append(indentString1 + "/** " + enumValue + ". */\n");
            buffer.append(indentString1 + "public static final " + className + " " + enumValue + " = new " + className + "(\"" + enumValue + "\");\n");
        }

        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "// Static count\n");
        buffer.append(indentString1 + "private static final int " + variablePrefix + "count = " + variablePrefix + "enumList.size();\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "// Private members\n");
        buffer.append(indentString1 + "private int " + variablePrefix + "value;\n");
        buffer.append(indentString1 + "private String " + variablePrefix + "description;\n");

        if (printableString) {
            buffer.append(indentString1 + "private String " + variablePrefix + "printableString;\n");
        }

        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Returns the number of enumerated values.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @return the number of enumerated values\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public static int getValueCount()\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "return " + variablePrefix + "count;\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Returns an iterator for the enumerated values.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @return an iterator for the enumerated values\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public static Iterator iterator()\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "return " + variablePrefix + "enumList.iterator();\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Returns the integer for this enumerated value.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @return the integer for this enumerated value\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public int toInt()\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "return " + codeVariablePrefix + "value;\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Returns the string for this enumerated value.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @return the string for this enumerated value\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public String toString()\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "return " + codeVariablePrefix + "description;\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");

        if (printableString) {
            buffer.append(indentString1 + "/**\n");
            buffer.append(indentString1 + " * Returns the printable (display) string for this enumerated value.\n");
            buffer.append(indentString1 + " * \n");
            buffer.append(indentString1 + " * @return the printable (display) string for this enumerated value\n");
            buffer.append(indentString1 + " */\n");
            buffer.append(indentString1 + "public String toPrintableString()\n");
            buffer.append(indentString1 + "{\n");
            buffer.append(indentString2 + "return " + codeVariablePrefix + "printableString;\n");
            buffer.append(indentString1 + "}\n");
            buffer.append(indentString1 + "\n");

            if (printableVector) {
                buffer.append(indentString1 + "/**\n");
                buffer.append(indentString1 + " * Returns the enumerated items as a vector of printable strings.\n");
                buffer.append(indentString1 + " * Useful for initialising a combo box.\n");
                buffer.append(indentString1 + " * \n");
                buffer.append(indentString1 + " * @return the vector of printable strings\n");
                buffer.append(indentString1 + " */\n");
                buffer.append(indentString1 + "public static Vector getPrintableVector()\n");
                buffer.append(indentString1 + "{\n");
                buffer.append(indentString2 + "Vector vector = new Vector(" + variablePrefix + "enumList.size());\n");
                buffer.append(indentString2 + "for (Iterator iter = " + variablePrefix + "enumList.iterator(); iter.hasNext(); )\n");
                buffer.append(indentString2 + "{\n");
                buffer.append(indentString3 + "vector.add(((" + className + ") iter.next()).toPrintableString());\n");
                buffer.append(indentString2 + "}\n");
                buffer.append(indentString2 + "return vector;\n");
                buffer.append(indentString1 + "}\n");
                buffer.append(indentString1 + "\n");
            }
        }

        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Returns the enumerated value for a given integer.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @param i the integer to return the enumerated value for\n");
        buffer.append(indentString1 + " * @return the enumerated value matching the integer (or null if no match found)\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public static " + className + " fromInt(int i)\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "if (i >= 0 && i < " + variablePrefix + "enumList.size())\n");
        buffer.append(indentString2 + "{\n");
        buffer.append(indentString3 + "return (" + className + ") " + variablePrefix + "enumList.get(i);\n");
        buffer.append(indentString2 + "}\n");
        buffer.append(indentString2 + "\n");
        buffer.append(indentString2 + "return null;\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Returns the enumerated value for a given string.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @param s the string to return the enumerated value for\n");
        buffer.append(indentString1 + " * @return the enumerated value matching the string (or null if no match found)\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public static " + className + " fromString(String s)\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "for (Iterator iter = " + variablePrefix + "enumList.iterator(); iter.hasNext(); )\n");
        buffer.append(indentString2 + "{\n");
        buffer.append(indentString3 + className + " enum = (" + className + ") iter.next();\n");
        buffer.append(indentString3 + "\n");
        buffer.append(indentString3 + "if (enum." + variablePrefix + "description.equals(s))\n");
        buffer.append(indentString3 + "{\n");
        buffer.append(indentString4 + "return enum;\n");
        buffer.append(indentString3 + "}\n");
        buffer.append(indentString2 + "}\n");
        buffer.append(indentString2 + "\n");
        buffer.append(indentString2 + "return null;\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Compares this enumerated value with another object.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @param rhs the object for comparison\n");
        buffer.append(indentString1 + " * @return whether the object for comparison is equal to this enumerated value\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public boolean equals(Object rhs)\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "return ((rhs instanceof " + className + ") && (" + codeVariablePrefix + "value == ((" + className + ") rhs)." + variablePrefix + "value));\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Returns a hash code for this enumerated value (required for hashed storage).\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @return a hash code for this enumerated value\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "public int hashCode()\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + "int result = 17;\n");
        buffer.append(indentString2 + "result = 37 * result + " + codeVariablePrefix + "value;\n");
        buffer.append(indentString2 + "return result;\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");
        buffer.append(indentString1 + "/**\n");
        buffer.append(indentString1 + " * Private constructor that creates a new preset enumerated value.\n");
        buffer.append(indentString1 + " * \n");
        buffer.append(indentString1 + " * @param description the descriptive text for the enumerated value\n");
        buffer.append(indentString1 + " */\n");
        buffer.append(indentString1 + "private " + className + "(String description)\n");
        buffer.append(indentString1 + "{\n");
        buffer.append(indentString2 + codeVariablePrefix + "value           = " + variablePrefix + "enumList.size();\n");
        buffer.append(indentString2 + codeVariablePrefix + "description     = description;\n");

        if (printableString) {
            buffer.append(indentString2 + codeVariablePrefix + "printableString = getPrintableString();\n");
        }

        buffer.append(indentString2 + variablePrefix + "enumList.add(this);\n");
        buffer.append(indentString1 + "}\n");
        buffer.append(indentString1 + "\n");

        if (printableString) {
            buffer.append(indentString1 + "/**\n");
            buffer.append(indentString1 + " * Returns a printable (display) string for this enumerated value.\n");
            buffer.append(indentString1 + " * \n");
            buffer.append(indentString1 + " * @return a printable (display) string for this enumerated value\n");
            buffer.append(indentString1 + " */\n");
            buffer.append(indentString1 + "private String getPrintableString()\n");
            buffer.append(indentString1 + "{\n");
            buffer.append(indentString2 + "StringBuffer buf = new StringBuffer();\n");
            buffer.append(indentString2 + "boolean newWord  = true;\n");
            buffer.append(indentString2 + "\n");
            buffer.append(indentString2 + "for (int i = 0; i < " + codeVariablePrefix + "description.length(); i++)\n");
            buffer.append(indentString2 + "{\n");
            buffer.append(indentString3 + "char c = " + codeVariablePrefix + "description.charAt(i);\n");
            buffer.append(indentString3 + "\n");
            buffer.append(indentString3 + "if (c == '_')\n");
            buffer.append(indentString3 + "{\n");
            buffer.append(indentString4 + "buf.append(' ');\n");
            buffer.append(indentString4 + "newWord = true;\n");
            buffer.append(indentString3 + "}\n");
            buffer.append(indentString3 + "else\n");
            buffer.append(indentString3 + "{\n");
            buffer.append(indentString4 + "if (newWord)\n");
            buffer.append(indentString4 + "{\n");
            buffer.append(indentString5 + "buf.append(Character.toUpperCase(c));\n");
            buffer.append(indentString5 + "newWord = false;\n");
            buffer.append(indentString4 + "}\n");
            buffer.append(indentString4 + "else\n");
            buffer.append(indentString4 + "{\n");
            buffer.append(indentString5 + "buf.append(Character.toLowerCase(c));\n");
            buffer.append(indentString4 + "}\n");
            buffer.append(indentString3 + "}\n");
            buffer.append(indentString2 + "}\n");
            buffer.append(indentString2 + "\n");
            buffer.append(indentString2 + "return buf.toString();\n");
            buffer.append(indentString1 + "}\n");
            buffer.append(indentString1 + "\n");
        }

        if (createAsBean) {
            buffer.append(indentString1 + "\n");
            buffer.append(indentString1 + "/**\n");
            buffer.append(indentString1 + " * Public no argument constructor. Required for bean compatibility.\n");
            buffer.append(indentString1 + " * Creates an enumerated value equal to the first enumerated value in the list.\n");
            buffer.append(indentString1 + " */\n");
            buffer.append(indentString1 + "public " + className + "()\n");
            buffer.append(indentString1 + "{\n");
            buffer.append(indentString2 + className + " first = (" + className + ") " + variablePrefix + "enumList.get(0);\n");
            buffer.append(indentString2 + codeVariablePrefix + "value            = first." + variablePrefix + "value;\n");
            buffer.append(indentString2 + codeVariablePrefix + "description      = first." + variablePrefix + "description;\n");

            if (printableString) {
                buffer.append(indentString2 + codeVariablePrefix + "printableString  = first." + variablePrefix + "printableString;\n");
            }

            buffer.append(indentString1 + "}\n");
            buffer.append(indentString1 + "\n");
            buffer.append(indentString1 + "/**\n");
            buffer.append(indentString1 + " * Returns the string for this enumerated value. Required for bean compatibility.\n");
            buffer.append(indentString1 + " * \n");
            buffer.append(indentString1 + " * @return the string for this enumerated value\n");
            buffer.append(indentString1 + " */\n");
            buffer.append(indentString1 + "public String getString()\n");
            buffer.append(indentString1 + "{\n");
            buffer.append(indentString2 + "return toString();\n");
            buffer.append(indentString1 + "}\n");
            buffer.append(indentString1 + "\n");
            buffer.append(indentString1 + "/**\n");
            buffer.append(indentString1 + " * Sets the string for this enumerated value. Required for bean compatibility.\n");
            buffer.append(indentString1 + " * Attempts to set this enumerated value so that it equals the preset enumerated\n");
            buffer.append(indentString1 + " * value matching the string. Does nothing if no matching value is found.\n");
            buffer.append(indentString1 + " * \n");
            buffer.append(indentString1 + " * @param s the string for this enumerated value\n");
            buffer.append(indentString1 + " * @throws Error if an attempt is made to change one of the preset enumerated values\n");
            buffer.append(indentString1 + " */\n");
            buffer.append(indentString1 + "public void setString(String s)\n");
            buffer.append(indentString1 + "{\n");
            buffer.append(indentString2 + "// First check we aren't trying to change one of the presets\n");
            buffer.append(indentString2 + "for (Iterator iter = " + variablePrefix + "enumList.iterator(); iter.hasNext(); )\n");
            buffer.append(indentString2 + "{\n");
            buffer.append(indentString3 + className + " enum = (" + className + ") iter.next();\n");
            buffer.append(indentString3 + "\n");
            buffer.append(indentString3 + "if (this == enum)\n");
            buffer.append(indentString3 + "{\n");
            buffer.append(indentString4 + "// Can't change any of our presets\n");
            buffer.append(indentString4 + "throw new Error(\"Cannot change a preset enumerated value\");\n");
            buffer.append(indentString3 + "}\n");
            buffer.append(indentString2 + "}\n");
            buffer.append(indentString2 + "\n");
            buffer.append(indentString2 + className + " match = fromString(s);\n");
            buffer.append(indentString2 + "\n");
            buffer.append(indentString2 + "if (match != null)\n");
            buffer.append(indentString2 + "{\n");
            buffer.append(indentString3 + codeVariablePrefix + "value           = match." + variablePrefix + "value;\n");
            buffer.append(indentString3 + codeVariablePrefix + "description     = match." + variablePrefix + "description;\n");

            if (printableString) {
                buffer.append(indentString3 + codeVariablePrefix + "printableString = match." + variablePrefix + "printableString;\n");
            }

            buffer.append(indentString2 + "}\n");
            buffer.append(indentString1 + "}\n");
        }

        buffer.append("}\n");

        return buffer.toString();
    }

    /**
     * Gets the indent string for a given indent size.
     *
     * @param indentSize the indent size
     * @return the indent string for the given size
     */
    private static String getIndentString(int indentSize) {
        StringBuffer buffer = new StringBuffer(indentSize);

        for (int i = 0; i < indentSize; i++) {
            buffer.append(" ");
        }

        return buffer.toString();
    }
}
