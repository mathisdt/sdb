package org.zephyrsoft.util;

import java.io.*;

import javax.swing.filechooser.FileFilter;

/** FileFilter für Dateien mit beliebigen Endungen. */
public class CustomFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
	
	private String extension = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$
	
	public CustomFileFilter(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	// Accept all directories and all files with the specified extension.
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		if ( f.getName().toLowerCase().endsWith(extension) ) {
			return true;
		} else {
			return false;
		}
	}

	public String getDescription() {
		return description;
	}
}