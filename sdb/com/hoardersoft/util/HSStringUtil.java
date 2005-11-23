package com.hoardersoft.util;

import com.hoardersoft.beangenerator.*;

import java.util.*;
import java.awt.*;

/**
 * Class with utility methods for dealing with strings.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSStringUtil {
    /**
     * Returns whether a string is all upper case.
     *
     * @param string the string to check
     * @return whether the string is all upper case
     */
    static public boolean isAllUpperCase(String string) {
        int n = string.length();

        for (int i = 0; i < n; i++) {
            if (!Character.isUpperCase(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a string with case sorted - all capitals is left
     * at all capitals - otherwise it is lowered in case.
     *
     * @param string the string to sort the case of
     * @return the sorted string
     */
    static public String sortCase(String string) {
        if (isAllUpperCase(string)) {
            return string;
        }

        return string.toLowerCase();
    }

    /**
     * Ensures a string only consists of "valid" identifier characters.
     * Basically strip out anything which isn't a letter, digit, underscore or array brackets (with nothing inbetween).
     *
     * @param string the string to ensure is valid as a type
     * @param defaultType the default type to use (if string is completely unusable)
     * @return the string which is valid as a type
     */
    static public String ensureValidType(String string, String defaultType) {
        String type = ensureValidType(string);

        if ((type == null) || (type.length() == 0)) {
            return defaultType;
        }

        return type;
    }

    /**
     * Ensures a string only consists of "valid" identifier characters.
     * Basically strip out anything which isn't a letter, digit, underscore or array brackets (with nothing inbetween).
     *
     * @param string the string to ensure is valid as a type
     * @return the string which is valid as a type
     */
    static public String ensureValidType(String string) {
        if (string == null) {
            return null;
        }

        // First trim off extra whitespace
        string = string.trim();

        // Now copy across all "valid" characters into a new string - note can't start with a digit or brackets
        int stringLen          = string.length();
        StringBuffer newString = new StringBuffer(stringLen);
        int bracketsOpened     = 0;
        boolean firstCharacter = true;

        for (int i = 0; i < stringLen; i++) {
            char c = string.charAt(i);

            if (Character.isLetterOrDigit(c) || (c == '_')) {
                // Ignore all characters between brackets
                if (bracketsOpened == 0) {
                    if (firstCharacter) {
                        if (!Character.isDigit(c)) {
                            newString.append(c);

                            firstCharacter = false;
                        }
                    }
                    else {
                        newString.append(c);
                    }
                }
            }
            else if (c == '[') {
                bracketsOpened++;
            }
            else if (c == ']') {
                bracketsOpened--;

                // Output a set of brackets when we get back to level again
                if (bracketsOpened == 0) {
                    if (!firstCharacter) {
                        newString.append("[]");
                    }
                }
            }
        }

        return newString.toString();
    }

    /**
     * Ensures a string is a valid variable name.
     *
     * @param string the string to ensure is valid as a variable name
     * @param defaultVariableName the default variable name to use (if string is completely unusable)
     * @return the string which is valid as a variable name
     */
    static public String ensureValidVariableName(String string, String defaultVariableName) {
        String variableName = ensureValidVariableName(string);

        if ((variableName == null) || (variableName.length() == 0)) {
            return defaultVariableName;
        }

        return variableName;
    }

    /**
     * Ensures a string is a valid variable name.
     *
     * @param string the string to ensure is valid as a variable name
     * @return the string which is valid as a variable name
     */
    static public String ensureValidVariableName(String string) {
        // First ensure it is a valid identifier and starts with a lower case letter
        String variableName = ensureFirstLetterCase(ensureValidIdentifier(string), false);

        // Finally be careful not to use native types
        if (E_HSTypes.isNative(variableName)) {
            variableName = variableName + "Value";
        }

        return variableName;
    }

    /**
     * Ensures a string only consists of "valid" identifier characters.
     * Basically strip out anything which isn't a letter, digit or underscore.
     *
     * @param string the string to ensure is valid as an identifier
     * @param defaultIdentifier the default identifier to use (if string is completely unusable)
     * @return the string which is valid as an identifier
     */
    static public String ensureValidIdentifier(String string, String defaultIdentifier) {
        String identifier = ensureValidIdentifier(string);

        if ((identifier == null) || (identifier.length() == 0)) {
            return defaultIdentifier;
        }

        return identifier;
    }

    /**
     * Ensures a string only consists of "valid" identifier characters.
     * Basically strip out anything which isn't a letter, digit or underscore.
     *
     * @param string the string to ensure is valid as an identifier
     * @return the string which is valid as an identifier
     */
    static public String ensureValidIdentifier(String string) {
        if (string == null) {
            return null;
        }

        // First trim off extra whitespace
        string = string.trim();

        // Now copy across all "valid" characters into a new string - note can't start with a digit
        int stringLen          = string.length();
        StringBuffer newString = new StringBuffer(stringLen);
        boolean firstCharacter = true;

        for (int i = 0; i < stringLen; i++) {
            char c = string.charAt(i);

            if (Character.isLetterOrDigit(c) || (c == '_')) {
                if (firstCharacter) {
                    if (!Character.isDigit(c)) {
                        newString.append(c);

                        firstCharacter = false;
                    }
                }
                else {
                    newString.append(c);
                }
            }
        }

        return newString.toString();
    }

    /**
     * Ensures a string starts with a capital letter or lower case letter.
     *
     * @param string the string to ensure starts with the right case
     * @param upperCase whether to ensure an upper case letter or lower case letter
     * @return the string with the first letter checked and amended as appropriate
     */
    static public String ensureFirstLetterCase(String string, boolean upperCase) {
        if (string == null) {
            return null;
        }

        // First trim off extra whitespace
        string = string.trim();

        // Now ensure we have a character of the correct case at the start
        int stringLen          = string.length();
        StringBuffer newString = new StringBuffer(string);

        if (stringLen > 0) {
            char c = newString.charAt(0);

            if (Character.isLowerCase(c) && upperCase) {
                // Need to make the first letter upper case
                newString.replace(0, 1, String.valueOf(Character.toUpperCase(c)));
            }
            else if (Character.isUpperCase(c) && !upperCase) {
                // Need to make the first letter lower case
                newString.replace(0, 1, String.valueOf(Character.toLowerCase(c)));
            }
        }

        return newString.toString();
    }

    /**
     * Ensures a string is singular (strips off any trailing 's').
     *
     * @param string the string to check is singular
     * @return the singular version of the string (any trailing 's' stripped off)
     */
    static public String ensureSingular(String string) {
        if (string == null) {
            return null;
        }

        // First trim off extra whitespace
        string = string.trim();

        if (string.endsWith("ies") && (string.length() > 3)) {
            // We normally have "y" as the singular for "ies"
            string = string.substring(0, (string.length() - 3)) + "y";
        }
        else if (string.endsWith("s") && (string.length() > 1)) {
            // Strip off the "s"
            string = string.substring(0, (string.length() - 1));
        }

        return string;
    }

    /**
     * Gets the default description for a given property name.
     * For example, "okButton" becomes "the ok button property".
     *
     * @param name the property name
     * @return the default description
     */
    static public String getDefaultDescription(String name) {
        StringBuffer description = new StringBuffer();
        boolean first            = true;

        // Start with "the "
        description.append("the ");

        // Append on spaced out version of the name
        ArrayList words = getIdentifierWords(name);

        for (Iterator iter = words.iterator(); iter.hasNext(); ) {
            // Append space if not the first word
            if (first) {
                first = false;
            }
            else {
                description.append(" ");
            }

            description.append((String) iter.next());
        }

        String descriptionString = description.toString();

        // Make sure description doesn't end with a full stop
        if (descriptionString.endsWith(".")) {
            descriptionString = descriptionString.substring(0, (descriptionString.length() - 1)).trim();
        }

        // End with " property" (if it doesn't already end with this !!)
        if (!descriptionString.endsWith(" property")) {
            descriptionString = descriptionString + " property";
        }

        return descriptionString;
    }

    /**
     * Gets the default constant for a given property name.
     * For example, "okButton" becomes "OK_BUTTON".
     *
     * @param name the property name
     * @return the default description
     */
    static public String getDefaultConstant(String name) {
        StringBuffer constantString = new StringBuffer();
        boolean first               = true;

        // Append on underscore spaced out version of the name
        ArrayList words = getIdentifierWords(name);

        for (Iterator iter = words.iterator(); iter.hasNext(); ) {
            // Append underscore if not the first word
            if (first) {
                first = false;
            }
            else {
                constantString.append("_");
            }

            constantString.append(((String) iter.next()).toUpperCase());
        }

        return constantString.toString();
    }

    /**
     * Gets the default package for a given property name.
     * For example, "okButton" becomes "ok.button".
     *
     * @param name the property name
     * @return the default description
     */
    static public String getDefaultPackage(String name) {
        StringBuffer packageString = new StringBuffer();
        boolean first              = true;

        // Append on dot spaced out version of the name
        ArrayList words = getIdentifierWords(name);

        for (Iterator iter = words.iterator(); iter.hasNext(); ) {
            // Append dot if not the first word
            if (first) {
                first = false;
            }
            else {
                packageString.append(".");
            }

            packageString.append(((String) iter.next()).toLowerCase());
        }

        return packageString.toString();
    }

    /**
     * Splits a Java identifier into separate words.
     * For example, "okButton" becomes the list { "ok", "button" }
     *
     * @param name the property name
     * @return the list of words
     */
    static public ArrayList getIdentifierWords(String name) {
        ArrayList words   = new ArrayList();
        StringBuffer word = new StringBuffer();
        char lastChar     = ' ';
        int n             = name.length();

        for (int i = 0; i < n; i++) {
            char c = name.charAt(i);

            // Check for start of new word
            if (((i > 0) && (Character.isUpperCase(c) && !Character.isUpperCase(lastChar))) || (Character.isDigit(c) && !Character.isDigit(lastChar))) {
                // Start of new word - append on our word and reset the current word
                words.add(sortCase(word.toString()));
                word.setLength(0);
            }

            // Append our character onto the current word and update the last character
            word.append(c);

            lastChar = c;
        }

        // Add the last word
        words.add(sortCase(word.toString()));

        return words;
    }

    /**
     * Gets a string array from a comma delimited string.
     *
     * @param string the string to parse
     * @return the string array (empty array if no strings available)
     */
    public static String[] getStringArray(String string) {
        return getStringArray(string, ",");
    }

    /**
     * Gets a string array from a delimited string.
     *
     * @param string the string to parse
     * @param delimiters the string containing the delimiters
     * @return the string array (empty array if no strings available)
     */
    public static String[] getStringArray(String string, String delimiters) {
        if (string == null) {
            return new String[0];
        }

        StringTokenizer st = new StringTokenizer(string, delimiters);
        int size           = st.countTokens();
        String[] result    = new String[size];

        for (int i = 0; i < size; i++) {
            result[i] = st.nextToken().trim();
        }

        return result;
    }

    /**
     * Removes duplicates from a string array.
     *
     * @param strings the string array from which to remove duplicates
     * @return the string array with all duplicate strings removed (empty array if no strings)
     */
    public static String[] removeDuplicates(String[] strings) {
        if (strings == null) {
            return new String[0];
        }

        ArrayList list = new ArrayList();

        for (int i = 0; i < strings.length; i++) {
            if (!list.contains(strings[i])) {
                list.add(strings[i]);
            }
        }

        return (String[]) list.toArray(new String[0]);
    }

    /**
     * Pads out a string to a given minimum length.
     * Adds trailing spaces to the end if necessary
     * to achieve the specified minimum length.
     *
     * @param string the string to pad
     * @param length the length to pad to
     * @return the padded string (null if null passed in)
     */
    public static String padString(String string, int length) {
        if (string == null) {
            return null;
        }

        if (string.length() >= length) {
            return string;
        }

        StringBuffer buf = new StringBuffer(string);

        for (int i = string.length(); i < length; i++) {
            buf.append(" ");
        }

        return buf.toString();
    }

    /**
     * Pads out a string to a given minimum length.
     * Adds leading or trailing character if necessary
     * to achieve the specified minimum length.
     *
     * @param string the string to pad
     * @param length the length to pad to
     * @param leading whether to add leading padding (otherwise it is trailing)
     * @param padCharacter the pad character
     * @return the padded string (null if null passed in)
     */
    public static String padString(String string, int length, boolean leading, char padCharacter) {
        if (string == null) {
            return null;
        }

        if (string.length() >= length) {
            return string;
        }

        StringBuffer padBuf = new StringBuffer();

        for (int i = string.length(); i < length; i++) {
            padBuf.append(padCharacter);
        }

        if (leading) {
            StringBuffer buf = new StringBuffer(padBuf.toString());

            buf.append(string);

            return buf.toString();
        }
        else {
            StringBuffer buf = new StringBuffer(string);

            buf.append(padBuf.toString());

            return buf.toString();
        }
    }

    /**
     * Gets the colour string for a given colour. For example white will return "#ffffff".
     *
     * @param c the colour
     * @return the colour string (null if null passed in)
     */
    public static String getColourString(Color c) {
        if (c != null) {
            String colourString = Integer.toHexString(c.getRGB() & 0x00ffffff).toUpperCase();

            // Make sure we have six digits so it looks good :)
            return "#" + padString(colourString, 6, true, '0');
        }

        return null;
    }

    /**
     * Gets the colour for a given colour string. For example "#ffffff" will return white.
     *
     * @param s the colour string
     * @return the colour (null if null/empty string passed in or duff colour string)
     */
    public static Color getColour(String s) {
        if ((s != null) && (s.length() > 0)) {
            try {
                return Color.decode(s);
            }
            catch (NumberFormatException nfe) {
                HSLogUtil.debug("Warning: Error during colour string parse: " + s);
            }
        }

        return null;
    }
}
