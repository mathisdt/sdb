package org.zephyrsoft.sdb;

import java.io.*;
import java.util.*;

/** Klasse um die Strings aus der lang_??.properties zu laden
 *  ==> mittels des "Singleton" Design Patterns implementiert <== */
public class Messages {
	private static final Messages _instance = new Messages();
	private String BUNDLE_NAME = null;
	private static final String FALLBACK_BUNDLE_NAME = "lang_de";//$NON-NLS-1$
	private ResourceBundle RESOURCE_BUNDLE = null;
	private ResourceBundle VERSION_RESOURCE_BUNDLE = null;

	private Messages() {
		// BUNDLE_NAME einlesen aus Datei language
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File("language")));//$NON-NLS-1$
			String language = in.readLine();
			if (language!=null) {
				BUNDLE_NAME = "lang_" + language.toLowerCase().trim(); //$NON-NLS-1$
				RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
			} else {
				throw new Exception();
			}
		} catch(Exception ex) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(FALLBACK_BUNDLE_NAME);
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(new File("language")));//$NON-NLS-1$
				out.write(FALLBACK_BUNDLE_NAME.substring(5));
				out.flush();
				out.close();
			} catch(Exception ex2) {
				System.err.println("couldn't write new language setting to file \"language\"");//$NON-NLS-1$
			}
		} finally {
			if (in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore exception at this point
				}
			}
		}
		// VERSION_RESOURCE_BUNDLE einlesen
		VERSION_RESOURCE_BUNDLE = ResourceBundle.getBundle("version"); //$NON-NLS-1$
	}
	
	private static Messages getInstance() {
		return _instance;
	}

	public static String getString(String key) {
		try {
			return getInstance().RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String getVersionString(String key) {
		try {
			return getInstance().VERSION_RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static void setLanguage(String lang) {
	    try {
			BufferedWriter out = new BufferedWriter(new FileWriter(new File("language")));//$NON-NLS-1$
			out.write(lang);
			out.flush();
			out.close();
		} catch(Exception ex2) {
			System.err.println("couldn't write new language setting to file \"language\"");//$NON-NLS-1$
		}
	}
}