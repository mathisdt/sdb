package org.zephyrsoft.sdb;

import javax.swing.*;

/**
 * Startklasse der Liederdatenbank. Generiert nur eine Instanz von GUI.
 * @author Mathis Dirksen-Thedens
 */

public class Start {
	public static void main(String[] args) {
		new Start(args);
	}
	
	public Start(String[] args) {
		// Aussehen
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		if ( lookAndFeel != null ) {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (Exception e) {
				System.out.println("Look&Feel konnte nicht gesetzt werden."); //$NON-NLS-1$
			}
		}
		
		// Frage
		/*		Object[] options = { "Beamer-Präsentation", "Datenbank" };
				int answer = JOptionPane.showOptionDialog(null, "Welche Ansicht soll gezeigt werden?", "Auswahl der Ansicht",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		*/
		/*
		boolean gui_built = false;
		try {
			if (args[0] != null && args[0].equalsIgnoreCase("--presentation")) {
				GUI gui = new GUI(null);
				gui_built = true;
				try {
					if (args[1] != null ) {
						gui.showPresentation(args[1]);
					} else {
						gui.showPresentation();
					}
				} catch (ArrayIndexOutOfBoundsException ex2) {
					gui.showPresentation();
				}
			} else {
				new GUI(args);
				gui_built = true;
			}
	} catch (Exception ex) {
			if (!gui_built) {
				new GUI(args);
			}
	}
		*/
		
		String queryfor = "sdb"; //$NON-NLS-1$
		String sdb = ""; //$NON-NLS-1$
		boolean presentation = false;
		String spr = ""; //$NON-NLS-1$
		for (int i = 0; i < args.length; i++) {
			if (queryfor.equals("sdb")) { //$NON-NLS-1$
				// suche nach SDB-Datei
				sdb = args[i];
				queryfor = ""; //$NON-NLS-1$
			} else if (queryfor.equals("spr")) { //$NON-NLS-1$
				// suche nach SPR-Datei
				spr = args[i];
			}
			if (args[i].equalsIgnoreCase("--presentation")) { //$NON-NLS-1$
				queryfor = "spr"; //$NON-NLS-1$
				presentation = true;
			}
		}
		GUI gui = new GUI(sdb);
		if (presentation && spr.equals("")) { //$NON-NLS-1$
			gui.showPresentation();
		} else if (presentation) {
			gui.showPresentation(spr);
		}
	}
	
}