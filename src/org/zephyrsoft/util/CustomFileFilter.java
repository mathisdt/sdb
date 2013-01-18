package org.zephyrsoft.util;

import java.io.*;

import javax.swing.filechooser.FileFilter;

/** FileFilter für Dateien mit beliebigen Endungen. */
public class CustomFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
	
	private String[] extension = new String[] {};
	private String description = ""; //$NON-NLS-1$
	
	public CustomFileFilter(String description, String[] extension) {
		this.extension = extension;
		this.description = description;
	}

	// Accept all directories and all files with the specified extension.
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		for (int i = 0; i < extension.length; i++) {
			if ( f.getName().toLowerCase().endsWith(extension[i]) ) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return description;
	}
}