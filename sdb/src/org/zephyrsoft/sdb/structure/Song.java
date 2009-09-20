package org.zephyrsoft.sdb.structure;

import java.awt.*;
import java.awt.datatransfer.*;
import java.util.*;

import org.zephyrsoft.sdb.*;
import org.zephyrsoft.util.*;

//import com.hoardersoft.fontselector.*;

/**
 * Die Struktur, in der ein Lied zur Laufzeit gespeichert ist.
 */
public class Song implements java.io.Serializable, java.util.Comparator, Cloneable {

	private static final long serialVersionUID = 5931839542614942316L;
	
	private int id = 0;
	private String titel = ""; //$NON-NLS-1$
	private String text = ""; // inkl. Akkorde //$NON-NLS-1$
	private String tonart = ""; //$NON-NLS-1$
	private String sprache = ""; //$NON-NLS-1$
	private String copyright = ""; //$NON-NLS-1$
	private String bemerkungen = ""; //$NON-NLS-1$
	
	public Song(int id) {
		this.id = id;
		titel = Messages.getString("Song.6"); //$NON-NLS-1$
		sprache = Messages.getString("Song.7"); //$NON-NLS-1$
	}
	
	public String getFirstLine() {
		String title = getTitel();
		String ret = title;
		String text = getOnlyText() + "\n"; //$NON-NLS-1$
		int i = text.indexOf("\n"); //$NON-NLS-1$
		if (i != -1) {
			ret = text.substring(0, i);
			//System.out.println(title + " => " + ret);
		}
		while (ret.endsWith(" ") || ret.endsWith(",") ||  //$NON-NLS-1$ //$NON-NLS-2$
				ret.endsWith(";") || ret.endsWith(":") || //$NON-NLS-1$ //$NON-NLS-2$
				ret.endsWith(".") || ret.endsWith("!")) { //$NON-NLS-1$ //$NON-NLS-2$
			ret = ret.substring(0, ret.length()-1);
		}
		while (ret.startsWith("1. ")) { //$NON-NLS-1$
			ret = ret.substring(3, ret.length());
		}
		return ret;
	}
	
	public void importTextFromClipboard(Frame parent) {
		// Liest den Text der Zwischenablage und berücksichtigt dabei die proportionale Schrift.
		try {
			// Zwischenablage lesen
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			String clipcontent = (String)clip.getContents(null).getTransferData(DataFlavor.stringFlavor);
			
			// ersetze Tabs durch 8 Leerzeichen:
			clipcontent = org.zephyrsoft.util.StringTools.replace(clipcontent, "\t", "        "); //$NON-NLS-1$ //$NON-NLS-2$
			
			/*			System.out.println("= START ============");
						System.out.println(clipcontent);
						System.out.println("= ENDE =============");
			*/ 
			// Font wählen lassen
			Font basefont = new Font("Arial", Font.PLAIN, 18); //$NON-NLS-1$
//			HSFontSelector fontsel = new HSFontSelector(parent, "Bitte Eingabeschrift wählen!", basefont);
//			Font font = fontsel.showDialog();
//			if ( font == null ) {
//				font = basefont;
//			}
			Font font = basefont;
			
			
			// jetzt zeilenweise umrechnen in Proportionalschrift!
			String my = ""; // später Songtext! //$NON-NLS-1$
			String before = ""; //$NON-NLS-1$
			String line = ""; //$NON-NLS-1$
			int pos = 0;
			if ( !text.endsWith("\n") ) { //$NON-NLS-1$
				text += "\n"; //$NON-NLS-1$
			}
			while ( clipcontent.indexOf("\n", pos) != -1 ) { //$NON-NLS-1$
				before = line;
				line = clipcontent.substring(pos, clipcontent.indexOf("\n", pos)); //$NON-NLS-1$
				pos = clipcontent.indexOf("\n", pos) + 1; // nach Zeilensprung weitersuchen //$NON-NLS-1$
				//System.out.println("isTextLine = " + isTextLine(line) + " - " + line);
				if ( isTextLine(line) ) {
					if ( !line.trim().equals("") ) { //$NON-NLS-1$
						String[] back = correctSpaces2(line, before, font);
						// Akkorde hinzufügen
						if ( !isTextLine(back[1]) ) {
							my += back[1] + "\n"; //$NON-NLS-1$
						}
						// Text dazu
						my += back[0] + "\n"; //$NON-NLS-1$
					} else {
						my += "\n"; //$NON-NLS-1$
					}
				} else {
					// nix tun
				}
			}
			setText(my);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String[] correctSpaces2(String textzeile, String akkordzeile, Font font) {
	
		// Akkorde extrahieren
		String akk_temp = " " + StringTools.replace(akkordzeile, "  ", " ").trim() + " "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		int pos = 0;
		Vector akk_temp2 = new Vector();
		while ( akk_temp.indexOf(" ", pos) != -1 ) { //$NON-NLS-1$
			akk_temp2.addElement(akk_temp.substring(pos, akk_temp.indexOf(" ", pos))); //$NON-NLS-1$
			pos = akk_temp.indexOf(" ", pos) + 1; // nach Leerzeichen weitersuchen //$NON-NLS-1$
		}
		Object[] akkorde = akk_temp2.toArray();
		
		pos = 0; // in Akkordzeile
		int pos2 = 0; // in Textzeile
		
		// Ergebniszeile:
		String erg_akkordzeile = ""; //$NON-NLS-1$
		
		// Schleife:
		boolean continuing = false;
		for ( int i = 0; i < akkorde.length; i++ ) {
			continuing = false;
			// Pixel bis zum Akkord feststellen:
			int zeichenvorakkord = akkordzeile.indexOf((String)akkorde[i], pos);
			pos = zeichenvorakkord + ((String)akkorde[i]).length();
			int pixelvorakkord = Utils.getPixelLengthInFont(akkordzeile.substring(0, zeichenvorakkord), font, false);
			// Textzeile durchgehen, bis die Textlänge pixelvorakkord gerade noch nicht übertrifft:
			int textpixel = 0;
			while ( textpixel < pixelvorakkord ) {
				pos2++;
				if ( pos2 > textzeile.length() ) {
					if ( erg_akkordzeile.length() < textzeile.length() ) {
						erg_akkordzeile += StringTools.repeat(" ", textzeile.length() - erg_akkordzeile.length()); //$NON-NLS-1$
					}
					erg_akkordzeile += " " + akkorde[i]; //$NON-NLS-1$
					continuing = true;
					break;
				}
				
				textpixel = Utils.getPixelLengthInFont(textzeile.substring(0, pos2), font, false);
			}
			if ( continuing ) {
				continue;
			}
			if ( pos2 > 0 ) {
				pos2--;
			}
			// bisherige Akkordzeile mit Leerzeichen auffüllen und Akkord anfügen:
			erg_akkordzeile += StringTools.repeat(" ", pos2 - erg_akkordzeile.length()) + akkorde[i]; //$NON-NLS-1$
			
		}
		
		// Rückgabewert zusammenstellen:
		String[] back = new String[2];
		back[0] = textzeile;
		back[1] = erg_akkordzeile;
		
		return back;
	}
	
	/** immer nur ein Leerzeichen zwischen den Worten,
	 *  am Zeilenanfang aber mehrere erlauben zum Einrücken
	 */
	private String correctspacesinthemiddle(String string) {
	    int pos1 = string.indexOf("  "); //$NON-NLS-1$
		while (pos1 >= 0) {
		    // Leerzeichen vor pos1 zählen
		    int blanks = 0;
		    try {
			    while (string.substring(pos1-blanks-1,pos1-blanks).equals(" ")) { //$NON-NLS-1$
			        blanks++;
			    }
		    } catch (Exception ex) {
		        //System.out.println("Song - pos1: " + pos1 + " blanks: " + blanks);
		        // wohl kleiner als 0, tja...
		    }
		    // was liegt davor?
		    String davorliegt = "\n"; //$NON-NLS-1$
		    try {
		        davorliegt = string.substring(pos1-blanks-1,pos1-blanks);
		    } catch(Exception ex) {
		        //System.out.println("Song - Anfang!");
		        // der Anfang des Liedes
		    }
		    if (!davorliegt.equals("\n")) { //$NON-NLS-1$
			    string = string.substring(0, pos1) +
					" " + //$NON-NLS-1$
					string.substring(pos1 + 2);
			    pos1 = string.indexOf("  ", pos1); //$NON-NLS-1$
		    } else {
		        pos1 = string.indexOf("  ", pos1 + 1); //$NON-NLS-1$
		    }
		}
		return string;
	}
	
	public String getOnlyText() {
		String dtext = text; 
		dtext = StringTools.replace(dtext, GUI.SEPARATOR, ""); //$NON-NLS-1$
		String my = ""; //$NON-NLS-1$
		int pos = 0;
		if ( !dtext.endsWith("\n") ) { //$NON-NLS-1$
			dtext += "\n"; //$NON-NLS-1$
		}
		while ( dtext.indexOf("\n", pos) != -1 ) { //$NON-NLS-1$
			String line = dtext.substring(pos, dtext.indexOf("\n", pos)); //$NON-NLS-1$
			pos = dtext.indexOf("\n", pos) + 1; // nach Zeilensprung weitersuchen //$NON-NLS-1$
			if ( isTextLine(line) || isTranslateLine(line) ) {
				my += line + "\n"; //$NON-NLS-1$
			} else {
				// nix tun
			}
		}
		
		return correctspacesinthemiddle(my);
	}
	
	// Variante für mehrere Folien:
	public String getOnlyText_Foil(int foil) {
		String dtext = getFoil(foil); 
		String my = ""; //$NON-NLS-1$
		int pos = 0;
		if ( !dtext.endsWith("\n") ) { //$NON-NLS-1$
			dtext += "\n"; //$NON-NLS-1$
		}
		while ( dtext.indexOf("\n", pos) != -1 ) { //$NON-NLS-1$
			String line = dtext.substring(pos, dtext.indexOf("\n", pos)); //$NON-NLS-1$
			pos = dtext.indexOf("\n", pos) + 1; // nach Zeilensprung weitersuchen //$NON-NLS-1$
			if ( isTextLine(line) || isTranslateLine(line) ) {
				my += line + "\n"; //$NON-NLS-1$
			} else {
				// nix tun
			}
		}
		
		return correctspacesinthemiddle(my);
	}
	
	public String getTextAndAccordsInFont(Font font, boolean printing) {
//		DEBUG:
//		System.out.println("***1*** "+text);
		
		String dtext = text; 
		dtext = StringTools.replace(dtext, GUI.SEPARATOR, ""); //$NON-NLS-1$
		String my = "\n"; //$NON-NLS-1$
		String before = ""; //$NON-NLS-1$
		String line = ""; //$NON-NLS-1$
		int pos = 0;
		if ( !dtext.endsWith("\n") ) { //$NON-NLS-1$
			dtext += "\n"; //$NON-NLS-1$
		}
		while ( dtext.indexOf("\n", pos) != -1 ) { //$NON-NLS-1$
			before = line;
			line = dtext.substring(pos, dtext.indexOf("\n", pos)); //$NON-NLS-1$
			pos = dtext.indexOf("\n", pos) + 1; // nach Zeilensprung weitersuchen //$NON-NLS-1$
			while (line.lastIndexOf("\n") >=0 && line.lastIndexOf("\n") == line.length()-1) { //$NON-NLS-1$ //$NON-NLS-2$
				line = line.substring(line.length()-1);
			}
			if ( isTextLine(line) ) {
				if ( !line.trim().equals("") ) { //$NON-NLS-1$
					String[] back = correctSpaces(line, before, font, printing);
					// Akkorde hinzufügen
					if ( !isTextLine(back[1]) && !isTranslateLine(back[1]) && !isEmpty(back[1]) ) {
						my += back[1] + "\n"; //$NON-NLS-1$
					}
					// Text dazu
					my += back[0] + "\n"; //$NON-NLS-1$
				} else {
					my += "\n"; //$NON-NLS-1$
				}
			} else if (isTranslateLine(line)) {
				my += line + "\n"; //$NON-NLS-1$
			}
		}
		
//		DEBUG:
//		System.out.println("***2*** "+my);
		
		return my;
	}
	
	public static boolean isEmpty(String string) {
		string = StringTools.replace(string, " ", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (string.equals("")) { //$NON-NLS-1$
			return true;
		} else {
			return false;
		}
	}
	
	// Variante für mehrere Folien:
	public String getTextAndAccordsInFont_Foil(Font font, boolean printing, int foil) {
		String dtext = getFoil(foil); 
		String my = ""; //$NON-NLS-1$
		String before = ""; //$NON-NLS-1$
		String line = ""; //$NON-NLS-1$
		int pos = 0;
		if ( !dtext.endsWith("\n") ) { //$NON-NLS-1$
			dtext += "\n"; //$NON-NLS-1$
		}
		while ( dtext.indexOf("\n", pos) != -1 ) { //$NON-NLS-1$
			before = new String(line);
			line = dtext.substring(pos, dtext.indexOf("\n", pos)); //$NON-NLS-1$
			pos = dtext.indexOf("\n", pos) + 1; // nach Zeilensprung weitersuchen //$NON-NLS-1$
			//System.out.println("isTextLine = " + isTextLine(line) + " - " + line);
			if ( isTextLine(line) ) {
				if ( !line.trim().equals("") ) { //$NON-NLS-1$
					String[] back = correctSpaces(line, before, font, printing);
					// Akkorde hinzufügen
					if ( !isTextLine(back[1]) ) {
						my += back[1] + "\n"; //$NON-NLS-1$
					}
					// Text dazu
					my += back[0] + "\n"; //$NON-NLS-1$
				} else {
					my += "\n"; //$NON-NLS-1$
				}
			} else if (isTranslateLine(line)) {
				my += line + "\n"; //$NON-NLS-1$
			}
		}
		
		return my;
	}
	
	public int getFoilCount() {
		int foils = 1;
		int pos = 0;
		while (text.indexOf(GUI.SEPARATOR, pos) != -1) {
			foils++;
			pos = text.indexOf(GUI.SEPARATOR, pos) + GUI.SEPARATOR.length();
		}
		return foils;
	}

	public String getFoil(int foil) { // foil = 0, 1, 2, ...
		if (foil < getFoilCount() && foil >= 0) {
			String dtext = new String(text);
			dtext += "\n" + GUI.SEPARATOR + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			int foils = 0;
			int prepos = 0;
			int pos = dtext.indexOf(GUI.SEPARATOR);
			while (foil > foils) {
				foils++;
				prepos = pos + GUI.SEPARATOR.length();
				pos = dtext.indexOf(GUI.SEPARATOR, prepos);
			}
			String ret = dtext.substring(prepos, pos);
			while (ret.startsWith("\n") || ret.startsWith(" ")) { //$NON-NLS-1$ //$NON-NLS-2$
				ret = ret.substring(1);
			}
			return ret;
		} else {
			return ""; //$NON-NLS-1$
		}
	}
	
	public String getFoilText(int foil) { // foil = 0, 1, 2, ...
		if (foil < getFoilCount() && foil >= 0) {
			String dtext = new String(text);
			dtext += "\n" + GUI.SEPARATOR + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			int foils = 0;
			int prepos = 0;
			int pos = dtext.indexOf(GUI.SEPARATOR);
			while (foil > foils) {
				foils++;
				prepos = pos + GUI.SEPARATOR.length();
				pos = dtext.indexOf(GUI.SEPARATOR, prepos);
			}
			dtext = dtext.substring(prepos, pos);
			String ret = ""; //$NON-NLS-1$
			int nextone = 0;
			try {
				while (nextone >= 0 && nextone < dtext.length()) {
				    int nextnext = dtext.indexOf("\n", nextone); //$NON-NLS-1$
				    String line = dtext.substring(nextone, nextnext);
				    if (isTextLine(line)) {
				        ret += line + " "; //$NON-NLS-1$
				    }
				    nextone = nextnext+1;
				}
			} catch(Exception ex) {}
			while (ret.startsWith("\n") || ret.startsWith(" ")) { //$NON-NLS-1$ //$NON-NLS-2$
				ret = ret.substring(1);
			}
			return ret;
		} else {
			return ""; //$NON-NLS-1$
		}
	}
	
	private String[] correctSpaces(String textzeile, String akkordzeile, Font font, boolean printing) {
	
		// Akkorde extrahieren
		String akk_temp = " " + StringTools.replace(akkordzeile, "  ", " ").trim() + " "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		int pos = 0;
		Vector akk_temp2 = new Vector();
		while ( akk_temp.indexOf(" ", pos) != -1 ) { //$NON-NLS-1$
			akk_temp2.addElement(akk_temp.substring(pos, akk_temp.indexOf(" ", pos))); //$NON-NLS-1$
			pos = akk_temp.indexOf(" ", pos) + 1; // nach Leerzeichen weitersuchen //$NON-NLS-1$
		}
		Object[] akkorde = akk_temp2.toArray();
		
		// Text aufsplitten in Stücke, die bis zum jeweiligen Akkord führen (vom Anfang der Zeile)
		String[] textbis = new String[akkorde.length];
		pos = 0;
		for ( int i = 0; i < akkorde.length; i++ ) {
			int biszu = akkordzeile.indexOf((String)akkorde[i], pos);
			try {
				textbis[i] = textzeile.substring(0, biszu);
			} catch (StringIndexOutOfBoundsException ex) {
				// Akkord ist hinter Textzeilenende
				textbis[i] = new String(textzeile);
				textbis[i] += StringTools.repeat(" ", biszu - textbis[i].length() + 5); //$NON-NLS-1$
			}
			pos = biszu + ((String)akkorde[i]).length();
		}
		
		// neue Akkordzeile basteln
		String ret = ""; //$NON-NLS-1$
		for ( int i = 0; i < akkorde.length; i++ ) {
			int pixels = Utils.getPixelLengthInFont(textbis[i], font, printing);
			String exp = ""; //$NON-NLS-1$
			while ( Utils.getPixelLengthInFont(ret + exp, font, printing) < pixels ) {
				exp += " "; //$NON-NLS-1$
			}
			ret += exp + akkorde[i];
		}
		
		// Rückgabewert zusammenstellen:
		String[] back = new String[2];
		back[0] = ( textbis[textbis.length - 1].length() > textzeile.length() ? textbis[textbis.length - 1] : textzeile);
		//back[0] = ( textbis[textbis.length - 1].length() > textzeile.length() ? textbis[textbis.length - 1] + StringTools.repeat(" ", PrintView.ANZAHL_KORREKTUR_LEERZEICHEN) : textzeile + StringTools.repeat(" ", PrintView.ANZAHL_KORREKTUR_LEERZEICHEN) ); //$NON-NLS-1$ //$NON-NLS-2$
		back[1] = ret; // + StringTools.repeat(" ", 10); //$NON-NLS-1$
		
		return back;
	}
	
	public static boolean isTextLine(String line) {
		return percentOfSpaces(line) < 50 && !line.trim().startsWith("["); //$NON-NLS-1$
	}
	
	public static boolean isTranslateLine(String line) {
	    return percentOfSpaces(line) < 50 && line.trim().startsWith("["); //$NON-NLS-1$
	}
	
	private static double percentOfSpaces(String in) {
		int leerzeichen = 0;
		for ( int i = 0; i < in.length(); i++ ) {
			if ( in.substring(i, i + 1).equals(" ") ) { //$NON-NLS-1$
				leerzeichen = leerzeichen + 1;
			}
		}
		if ( in.length() != 0 ) {
			double ret = (double)leerzeichen / (double)in.length();
			ret = ret * 100;
			return ret;
		} else {
			return 0.0;
		}
	}
	
	public String toString() {
		return titel;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getTonart() {
		return tonart;
	}
	
	public void setTonart(String text) {
		this.tonart = text;
	}
	
	public String getTitel() {
		return titel;
	}
	
	public void setTitel(String text) {
		this.titel = text;
	}
	
	public String getSprache() {
		return sprache;
	}
	
	public void setSprache(String text) {
		this.sprache = text;
	}
	
	public String getBemerkungen() {
		return bemerkungen;
	}
	
	public void setBemerkungen(String text) {
		this.bemerkungen = text;
	}
	
	public String getCopyright() {
		return StringTools.replace(StringTools.replace(copyright, "(c)", "\u00A9"), "(C)", "\u00A9"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
	
	public void setCopyright(String text) {
		this.copyright = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	// Interface Methods:
	
	public Object clone() throws CloneNotSupportedException {
		Song ret = new Song(id);
		ret.setTitel(new String(titel));
		ret.setText(new String(text));
		ret.setTonart(new String(tonart));
		ret.setSprache(new String(sprache));
		ret.setCopyright(new String(copyright));
		ret.setBemerkungen(new String(bemerkungen));
		return ret;
	}
	
	public int compare(Object o1, Object o2) throws ClassCastException {
		// hier werden zwei Objekte miteinander verglichen (primär der Titel)
		int ret = 0;
		try {
			Song comp1 = (Song)o1;
			Song comp2 = (Song)o2;
			String titel1 = comp1.getTitel().toLowerCase();
			String titel2 = comp2.getTitel().toLowerCase();
			titel1 = StringTools.replace(titel1, "ä", "ae"); //$NON-NLS-1$ //$NON-NLS-2$
			titel1 = StringTools.replace(titel1, "ö", "oe"); //$NON-NLS-1$ //$NON-NLS-2$
			titel1 = StringTools.replace(titel1, "ü", "ue"); //$NON-NLS-1$ //$NON-NLS-2$
			titel1 = StringTools.replace(titel1, "ß", "ss"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ä", "ae"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ö", "oe"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ü", "ue"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ß", "ss"); //$NON-NLS-1$ //$NON-NLS-2$
			
			/*
			if ( comp1.getID() == comp2.getID() ) {
				ret = 0;
		} else if ( comp1.getID() < comp2.getID() ) {
				ret = -1;
		} else if ( comp1.getID() > comp2.getID() ) {
				ret = 1;
		}
			*/
			ret = titel1.compareTo(titel2);
		} catch (ClassCastException ex) {
			throw new ClassCastException("The given class was not a Song."); //$NON-NLS-1$
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		}
		boolean ret = false;
		Song comp = null;
		try {
			comp = (Song)obj;
			if ( comp.getID() == getID() ) {
				ret = true;
			}
		} catch (Exception ex) {
			if ( comp != null ) {
				ex.printStackTrace();
			}
		}
		return ret;
	}
}