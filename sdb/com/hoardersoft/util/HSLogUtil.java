package com.hoardersoft.util;

import java.io.*;
import java.security.*;
import java.text.*;
import java.util.*;

/**
 * Centralised logging facilities. Should be used by all other code.
 *
 * <br/><br/>
 *
 * Uses a properties file (.hoardersoft.prefs by default, living in the user's home directory) to
 * determine the log settings. The following is the default example file showing the default settings:
 *
 * <pre>
 *   log.disableAllDebug     = true
 *   log.showClassNames      = true
 *   log.shortClassNames     = true
 *   log.classNamePadding    = 20
 *   log.showStackTrace      = true
 *   log.startMethodPrefix   = ---
 *   log.startMethodPostfix  = start ---
 *   log.endMethodPrefix     = ---
 *   log.endMethodPostfix    = end ---
 *   log.debugPrefix         = debug.
 *   log.loggerName          = com.hoardersoft
 *   log.loggerConsole       = false
 *   log.loggerFilePattern   = %h/.hoardersoft%u.log
 * </pre>
 *
 * Hopefully the above are mostly self-explanatory, but here are some
 * quick descriptions:
 *
 * <pre>
 *   log.disableAllDebug     = if set to true no debug is output
 *   log.showClassNames      = if true class names are shown in the messages
 *   log.shortClassNames     = if false you get fully qualified class names
 *   log.classNamePadding    = pads out class names to this length (set zero for none)
 *   log.showStackTract      = if true you will get stack traces shown on exceptions
 *   log.startMethodPrefix   = text added before start method debug message
 *   log.startMethodPostfix  = text added after start method debug message
 *   log.endMethodPrefix     = text added before end method debug message
 *   log.endMethodPostfix    = text added after end method debug message
 *   log.debugPrefix         = the prefix of properties that control debug output
 *   log.loggerName          = the name of the logger (see Java 1.4 logging API)
 *   log.loggerConsole       = whether to output to the console
 *   log.loggerFilePattern   = the file pattern for a file to save the log to
 * </pre>
 *
 * The debugging for individual classes and packages is enabled by
 * creating properties whose names are the debug prefix ("debug." by
 * default) followed by the fully qualified class name (or package name
 * using a ".*" wildcard). An example would be to append the following
 * to the properties file:
 *
 * <pre>
 *   debug.com.hoardersoft.util.
 *   debug.com.hoardersoft.statusbar.HSStatusBar
 * </pre>
 *
 * The debug for the above classes and packages will then be output. Note
 * that the properties file can be updated at runtime (it is constantly
 * checked for updates) so debug can be switched on/off just by editing
 * the properties file without having to restart the system. In the default
 * (installed) system all debug is switched off and all log/error messages
 * are saved to a file (with no console output).
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSLogUtil {
    private static String m_propertiesFilename    = ".hoardersoft.prefs";
    private static File m_propertiesFile          = null;
    private static boolean m_propertiesFileExists = false;
    private static Properties m_properties        = null;
    private static long m_propertiesLastModified  = -1L;
    private static Logger m_logger                = null;
    private static Stack m_methodStartTimes       = new Stack();
    private static Stack m_methodStartMessages    = new Stack();
    private static FromHelper m_fromHelper        = new FromHelper();

    // Note - the default values below are repeated in loadProperties method
    private static boolean m_disableAllDebug   = true;
    private static boolean m_showClassNames    = true;
    private static boolean m_shortClassNames   = true;
    private static int m_classNamePadding      = 20;
    private static boolean m_showStackTrace    = true;
    private static String m_startMethodPrefix  = "---";
    private static String m_startMethodPostfix = "start ---";
    private static String m_endMethodPrefix    = "---";
    private static String m_endMethodPostfix   = "end ---";
    private static String m_debugPrefix        = "debug.";
    private static String m_loggerName         = "com.hoardersoft";
    private static boolean m_loggerConsole     = false;
    private static String m_loggerFilePattern  = "%h/.hoardersoft%u.log";

    /**
     * Sets the properties filename.
     *
     * @param filename the properties filename
     */
    public static void setPropertiesFilename(String filename) {
        if ((filename != null) && (filename.length() > 0) && !m_propertiesFilename.equals(filename)) {
            m_propertiesFilename = filename;

            // We have changed properties file - force a reload
            m_properties = null;
        }
    }

    /**
     * Report an error message.
     *
     * @param message the error message
     */
    public static void reportError(String message) {
        loadProperties();
        internalDebug(m_fromHelper.getFrom(), message, Level.WARNING, null);
    }

    /**
     * Report a error message (with optional throwable).
     *
     * @param message the error message
     * @param throwable the throwable error (null for none)
     */
    public static void reportError(String message, Throwable throwable) {
        loadProperties();
        internalDebug(m_fromHelper.getFrom(), message, Level.WARNING, throwable);
    }

    /**
     * Logs a start method debug message. Used for monitoring and timing method calls.
     *
     * @param message the start method debug message
     */
    public static void startMethodDebug(String message) {
        loadProperties();

        String logClass = m_fromHelper.getFrom();

        if (!m_disableAllDebug && localDebugging(logClass)) {
            // Push the start times and messages
            m_methodStartTimes.push(new Long(System.currentTimeMillis()));
            m_methodStartMessages.push(message);

            // Output the actual debug
            internalDebug(logClass, m_startMethodPrefix + " " + message + " " + m_startMethodPostfix, Level.INFO, null);
        }
    }

    /**
     * Logs an end method debug message. Used for monitoring and timing method calls.
     */
    public static void endMethodDebug() {
        loadProperties();

        String logClass = m_fromHelper.getFrom();

        if (!m_disableAllDebug && localDebugging(logClass)) {
            // Pop the start times and messages
            long startTime      = -1;
            String startMessage = null;

            try {
                startTime    = ((Long) m_methodStartTimes.pop()).longValue();
                startMessage = ((String) m_methodStartMessages.pop());
            }
            catch (EmptyStackException ese) {
                // Hopefully this will never occur !! However it can occur if debug is switched on and
                // off at runtime (in between start and end method calls). Not a real problem as all
                // debugging is disabled by default (ie in the "real" world).
                internalDebug(logClass, "Logging error - empty debug stack while timing method (end without matching start)", Level.WARNING, ese);
            }

            if (startTime >= 0) {
                long time = (System.currentTimeMillis() - startTime);

                // Output the actual debug
                internalDebug(logClass, m_endMethodPrefix + " " + startMessage + " " + m_endMethodPostfix + " [" + time + " ms]", Level.INFO, null);
            }
        }
    }

    /**
     * Logs a log message.
     *
     * @param message the debug message
     */
    public static void log(String message) {
        loadProperties();
        internalDebug(m_fromHelper.getFrom(), message, Level.INFO, null);
    }

    /**
     * Logs a debug message. Uses reflection to see if class is on the "debug" list.
     *
     * @param message the debug message
     */
    public static void debug(String message) {
        loadProperties();

        String logClass = m_fromHelper.getFrom();

        if (!m_disableAllDebug && localDebugging(logClass)) {
            internalDebug(logClass, message, Level.INFO, null);
        }
    }

    /**
     * Writes out the properties to the properties file.
     */
    public static void saveProperties() {
        loadProperties();

        try {
            FileOutputStream outputStream = new FileOutputStream(m_propertiesFile);

            m_properties.store(outputStream, "HoarderSoft Preferences File");
        }
        catch (IOException ioe) {
            HSLogUtil.reportError("Error saving properties file: " + m_propertiesFilename, ioe);
        }
    }

    /**
     * Logs a debug message. Internal version without the debug checks.
     *
     * @param logClass the name of the class in which the message occurred (can be null)
     * @param message the debug message
     * @param level the debug level
     * @param throwable the throwable (null for none)
     */
    private static void internalDebug(String logClass, String message, Level level, Throwable throwable) {
        if (m_showClassNames && (logClass != null)) {
            getLogger().logp(level, logClass, null, getFormattedClassMessage(logClass, message), throwable);
        }
        else {
            getLogger().logp(level, null, null, message, throwable);
        }
    }

    /**
     * Loads the properties (if required).
     */
    private static void loadProperties() {
        if (m_properties == null) {
            String home     = null;
            String logClass = HSLogUtil.class.getName();

            try {
                home = System.getProperty("user.home");
            }
            catch (AccessControlException ace) {
                // We're almost ceratinly running in an applet - just log the fact (don't bother printing out exception)
                internalDebug(logClass, "Security prevents checking of user's home directory - using current directory instead", Level.WARNING, null);
            }

            m_properties             = new Properties();
            m_propertiesLastModified = System.currentTimeMillis();
            m_propertiesFile         = (home == null) ? (new File(m_propertiesFilename)) : (new File(home, m_propertiesFilename));
            m_propertiesFileExists   = m_propertiesFile.exists();

            boolean loaded = false;

            try {
                // Load the properties file (in the user's home directory and named as specified)
                m_properties.load(new FileInputStream(m_propertiesFile));

                loaded = true;
            }
            catch (FileNotFoundException fnfe) {
                // Problem loading the properties - log this fact (don't bother printing out exception) - properties stay empty (default)
                internalDebug(logClass, "No properties file: " + m_propertiesFile + " (using default settings)", Level.INFO, null);
            }
            catch (IOException ioe) {
                // Problem loading the properties - log this fact - properties stay empty (default)
                internalDebug(logClass, "Error while loading properties file: " + m_propertiesFile + " (using default settings)", Level.WARNING, ioe);
            }
            catch (AccessControlException ace) {
                // Problem loading the properties - log this fact (don't bother printing out exception) - properties stay empty (default)
                internalDebug(logClass, "Security prevents loading of properties file: " + m_propertiesFile + " (using default settings)", Level.WARNING, null);
            }

            // Now work out out settings - if not found then use defaults
            m_disableAllDebug    = getBooleanPropertyInt("log.disableAllDebug", true);
            m_showClassNames     = getBooleanPropertyInt("log.showClassNames", true);
            m_shortClassNames    = getBooleanPropertyInt("log.shortClassNames", true);
            m_classNamePadding   = getIntegerPropertyInt("log.classNamePadding", 20);
            m_showStackTrace     = getBooleanPropertyInt("log.showStackTrace", true);
            m_startMethodPrefix  = getStringPropertyInt("log.startMethodPrefix", "---");
            m_startMethodPostfix = getStringPropertyInt("log.startMethodPostfix", "start ---");
            m_endMethodPrefix    = getStringPropertyInt("log.endMethodPrefix", "---");
            m_endMethodPostfix   = getStringPropertyInt("log.endMethodPostfix", "end ---");
            m_debugPrefix        = getStringPropertyInt("log.debugPrefix", "debug.");

            // Watch for logger changes
            String loggerName        = getStringPropertyInt("log.loggerName", "com.hoardersoft");
            boolean loggerConsole    = getBooleanPropertyInt("log.loggerConsole", false);
            String loggerFilePattern = getStringPropertyInt("log.loggerFilePattern", "%h/.hoardersoft%u.log");

            if (!loggerName.equals(m_loggerName) || (loggerConsole != m_loggerConsole) || !loggerFilePattern.equals(m_loggerFilePattern)) {
                // Store the values and null out our reference (to force creation of a new logger)
                m_logger            = null;
                m_loggerName        = loggerName;
                m_loggerConsole     = loggerConsole;
                m_loggerFilePattern = loggerFilePattern;
            }

            if (loaded) {
                // This message is delayed to the end of the method so that in the normal
                // state of loading a file the log of this load is logged to the correct place
                // (as the logger details have been set up correctly above)
                internalDebug(logClass, "Properties file loaded: " + m_propertiesFile, Level.INFO, null);
            }
        }
        else {
            // Check modification dates and existence - reload if there has been an update
            if ((m_propertiesFile.exists() != m_propertiesFileExists) || (m_propertiesFile.lastModified() > m_propertiesLastModified)) {
                // Properties file has changed (updated, removed, created) - force a reload
                m_properties = null;

                loadProperties();
            }
        }
    }

    /**
     * Determines whether local debugging is switched on or not. Uses the
     * properties file to determine classes/packages to debug.
     *
     * Use the format (where the default prefix is "debug."):
     *
     *   debug.com.hoardersoft.util.HTMLUtil
     *   debug.com.hoardersoft.util.SelectionUtil
     *   debug.com.hoardersoft.test.
     *
     * @param logClass the name of the class in which the message occurred (can be null)
     * @return whether local debugging is switched on
     */
    private static boolean localDebugging(String logClass) {
        if (logClass != null) {
            // Check the the actual class
            if (getBooleanPropertyInt(m_debugPrefix + logClass, false)) {
                return true;
            }

            // Check for the package with wildcard
            int lastDot = logClass.lastIndexOf(".");

            if (lastDot >= 0) {
                String packageName = logClass.substring(0, lastDot);

                if (getBooleanPropertyInt(m_debugPrefix + packageName + ".*", false)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the "boolean" value of a property. A property is true
     * if it is "true", "yes" or just exists (empty string).
     *
     * @param property the property to check
     * @param defaultValue the default value to return if not found
     * @return the boolean value of the property
     */
    public static boolean getBooleanProperty(String property, boolean defaultValue) {
        loadProperties();

        return getBooleanPropertyInt(property, defaultValue);
    }

    /**
     * Sets the "boolean" value of a property. A property is true
     * if it is "true", "yes" or just exists (empty string).
     *
     * @param property the property to check
     * @param value the boolean value of the property
     */
    public static void setBooleanProperty(String property, boolean value) {
        loadProperties();
        m_properties.put(property, value ? "true" : "false");
    }

    /**
     * Returns the "integer" value of a property.
     *
     * @param property the property to check
     * @param defaultValue the default value to return if not found
     * @return the integer value of the property
     */
    public static int getIntegerProperty(String property, int defaultValue) {
        loadProperties();

        return getIntegerPropertyInt(property, defaultValue);
    }

    /**
     * Sets the "integer" value of a property.
     *
     * @param property the property to check
     * @param value the integer value of the property
     */
    public static void setIntegerProperty(String property, int value) {
        loadProperties();
        m_properties.put(property, Integer.toString(value));
    }

    /**
     * Returns the "string" value of a property.
     *
     * @param property the property to check
     * @param defaultValue the default value to return if not found
     * @return the string value of the property
     */
    public static String getStringProperty(String property, String defaultValue) {
        loadProperties();

        return getStringPropertyInt(property, defaultValue);
    }

    /**
     * Sets the "string" value of a property.
     *
     * @param property the property to check
     * @param value the string value of the property
     */
    public static void setStringProperty(String property, String value) {
        loadProperties();
        m_properties.put(property, value);
    }

    /**
     * Returns the "boolean" value of a property (internal version).
     * A property is true if it is "true", "yes" or just exists (empty string).
     *
     * @param property the property to check
     * @param defaultValue the default value to return if not found
     * @return the boolean value of the property
     */
    private static boolean getBooleanPropertyInt(String property, boolean defaultValue) {
        // Note we can assume m_properties is set (this method only called internally after a loadProperties method call)
        String value = (String) m_properties.get(property);

        if (value != null) {
            // Found a value - is it "true" (true, yes or blank, ignoring case)
            return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || (value.length() == 0));
        }
        else {
            // No value found - return the default value
            return defaultValue;
        }
    }

    /**
     * Returns the "integer" value of a property (internal version).
     *
     * @param property the property to check
     * @param defaultValue the default value to return if not found
     * @return the integer value of the property
     */
    private static int getIntegerPropertyInt(String property, int defaultValue) {
        // Note we can assume m_properties is set (this method only called internally after a loadProperties method call)
        String value = (String) m_properties.get(property);
        int intValue = defaultValue;

        if (value != null) {
            // Found a value - parse as an integer
            try {
                intValue = Integer.parseInt(value);
            }
            catch (NumberFormatException nfe) {
                // Ignore - integer stays at default value
            }
        }

        return intValue;
    }

    /**
     * Returns the "string" value of a property (internal method).
     *
     * @param property the property to check
     * @param defaultValue the default value to return if not found
     * @return the string value of the property
     */
    private static String getStringPropertyInt(String property, String defaultValue) {
        // Note we can assume m_properties is set (this method only called internally after a loadProperties method call)
        String value = (String) m_properties.get(property);

        if (value != null) {
            // Found a value - return it
            return value;
        }
        else {
            // No value found - return the default value
            return defaultValue;
        }
    }

    /**
     * Returns the name for a class.
     *
     * @param logClass the name of the class in which the error occurred
     * @param message the message to display
     * @return the name for the class
     */
    private static String getFormattedClassMessage(String logClass, String message) {
        StringBuffer buf = new StringBuffer(100);

        buf.append("[");

        if (m_shortClassNames) {
            int lastDot = logClass.lastIndexOf(".");

            if (lastDot >= 0) {
                logClass = logClass.substring(lastDot + 1);
            }
        }

        buf.append(logClass);
        buf.append("]");

        // Pad out string if necessary
        for (int i = buf.length(); i < m_classNamePadding; i++) {
            buf.append(" ");
        }

        buf.append(" ");
        buf.append(message);

        return buf.toString();
    }

    /**
     * Gets the logger instance.
     *
     * @return the logger instance
     */
    private static Logger getLogger() {
        if (m_logger == null) {
            // Create a logger that logs everything
            m_logger = Logger.getLogger(m_loggerName);

            m_logger.setLevel(Level.ALL);

            // Remove all the handlers
            Handler[] handlers = m_logger.getHandlers();

            for (int i = 0; i < handlers.length; i++) {
                m_logger.removeHandler(handlers[i]);
            }

            // And on the root as well
            Logger rootLogger = Logger.getLogger("");

            handlers = rootLogger.getHandlers();

            for (int i = 0; i < handlers.length; i++) {
                rootLogger.removeHandler(handlers[i]);
            }

            // Add back the console handler if necessary
            if (m_loggerConsole) {
                rootLogger.addHandler(new ConsoleHandler());
            }

            // Store the log in a file - if there is a pattern
            String logClass = HSLogUtil.class.getName();

            if (m_loggerFilePattern.length() > 0) {
                try {
                    Handler fileHandler = new FileHandler(m_loggerFilePattern);

                    m_logger.addHandler(fileHandler);
                    internalDebug(logClass, "Saving log information to file pattern: " + m_loggerFilePattern, Level.INFO, null);
                }
                catch (IOException ioe) {
                    // Can't store the log in a file - log this fact :(
                    internalDebug(logClass, "Error while trying to set up the log file to write to: " + m_loggerFilePattern, Level.WARNING, ioe);
                }
            }
            else {
                internalDebug(logClass, "Not saving log inforamtion", Level.INFO, null);
            }
        }

        return m_logger;
    }

    /**
     * Private helper class to get the calling context.
     */
    private static class FromHelper extends SecurityManager {
        public FromHelper() {}

        public void checkPermission(Permission perm) {}

        public void checkPermission(Permission perm, Object context) {}

        /**
         * Gets the name (fully qualified) of the calling class.
         *
         * @return the name (fully qualified) of the calling class
         */
        public String getFrom() {
            // The "2" is magic here - 0 is this method, 1 is the HSLogUtil method, 2 is the calling method
            String className = getClassContext()[2].getName();

            // We want calling class name without any extra subclass signs ($)
            int dollar = className.indexOf('$');

            return (dollar >= 0) ? className.substring(0, dollar) : className;
        }
    }

    // Code from here down replicates the Java 1.4 logging facilities
    // in a very basic manner (log file writing not supported yet).
    private static class Level {
        public final static Level ALL     = new Level(0, "ALL");
        public final static Level INFO    = new Level(1, "INFO");
        public final static Level WARNING = new Level(2, "WARNING");
        private int m_value;
        private String m_description;

        private Level(int value, String description) {
            m_value       = value;
            m_description = description;
        }

        public boolean equals(Object o) {
            if (o instanceof Level) {
                return m_value == ((Level) o).m_value;
            }

            return false;
        }

        public int hashCode() {
            int result = 17;

            result = 37 * result + m_value;

            return result;
        }

        public String toString() {
            return m_description;
        }
    }

    private static class Logger {
        private static Hashtable m_instances     = new Hashtable();
        private static SimpleDateFormat m_format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        private Level m_level                    = Level.ALL;
        private ArrayList m_handlers             = new ArrayList();
        private boolean m_console                = true;

        public static Logger getLogger(String name) {
            Logger logger = (Logger) m_instances.get(name);

            if (logger == null) {
                logger = new Logger();

                m_instances.put(name, logger);
                logger.addHandler(new ConsoleHandler());
            }

            return logger;
        }

        public Level getLevel() {
            return m_level;
        }

        public void setLevel(Level level) {
            m_level = level;
        }

        public Handler[] getHandlers() {
            return (Handler[]) m_handlers.toArray(new Handler[0]);
        }

        public void addHandler(Handler handler) {
            m_handlers.add(handler);

            if (handler instanceof ConsoleHandler) {
                m_console = true;
            }
        }

        public void removeHandler(Handler handler) {
            m_handlers.remove(handler);

            // Update the console flag
            m_console = false;

            for (Iterator iter = m_handlers.iterator(); iter.hasNext(); ) {
                if (iter.next() instanceof ConsoleHandler) {
                    m_console = true;
                }
            }
        }

        public void logp(Level level, String className, String methodName, String message, Throwable throwable) {
            if (m_console) {
                StringBuffer buf = new StringBuffer();

                buf.append(m_format.format(new Date()));
                buf.append(" ");
                buf.append(level.toString().substring(0, 4));
                buf.append(": ");
                buf.append((message != null) ? message : "");

                if (throwable != null) {
                    buf.append(" (");
                    buf.append(throwable.getClass().getName());
                    buf.append(": ");
                    buf.append(throwable.getMessage());
                    buf.append(")");
                }

                buf.append("\n");

                if ((throwable != null) && m_showStackTrace) {
                    StringWriter string = new StringWriter();
                    PrintWriter writer  = new PrintWriter(string);

                    throwable.printStackTrace(writer);
                    buf.append(string.toString());
                }

                System.err.print(buf.toString());
            }
        }
    }

    private static class Handler {}

    private static class ConsoleHandler extends Handler {}

    private static class FileHandler extends Handler {
        private String m_filePattern;

        public FileHandler(String filePattern) throws IOException {
            m_filePattern = filePattern;
        }

        public String getFilePattern() {
            return m_filePattern;
        }
    }
}
