package org.zephyrsoft.sdb;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

/**
 * Tools.
 * @author Mathis Dirksen-Thedens
 */

public class Utils {
	
	private static String[][] toene = { {"C", "His"}, {"Cis", "Des", "C#", "Db"}, {"D"}, {"Dis", "Es", "D#", "Eb"}, {"E", "Fes", "Fb"}, {"F", "Eis", "E#"}, {"Fis", "Ges", "F#", "Gb"}, {"G"}, {"Gis", "As", "G#", "Ab"}, {"A"}, {"B", "Ais", "A#", "Hb"}, {"H", "Ces", "Cb"} }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$ //$NON-NLS-32$ //$NON-NLS-33$ //$NON-NLS-34$
	
	/** Gibt die Länge eines Strings in einem beliebigen Font in Pixeln zurück. */	
	public static int getPixelLengthInFont(String in, Font font, boolean printing) {
		if ( in.length() == 0 ) {
			return 0;
		} else {
			TextLayout layout = new TextLayout(in, font, new FontRenderContext(new AffineTransform(), printing, printing));
			return ((int)layout.getAdvance())+2;
		}
	}
	
	/** Transponiert einen Akkord. <pre>halbtoene</pre>: Gibt die Anzahl der zu verschiebenden Schritte an (pos./neg.)! */
	public static String transpose(String akkord, int halbtoene) {
		String ret = ""; //$NON-NLS-1$
		
		// falls "A/D" oder so:
		String secondAccord = ""; //$NON-NLS-1$
		if ( akkord.indexOf("/") != -1 ) { //$NON-NLS-1$
			secondAccord = transpose(akkord.substring(akkord.indexOf("/")+1), halbtoene); //$NON-NLS-1$
			akkord = akkord.substring(0, akkord.indexOf("/")); //$NON-NLS-1$
		}
		
		// Akkord von zusätzlichen Anweisungen trennen:
		String additional = ""; //$NON-NLS-1$
		try {
			if ( akkord.substring(1).startsWith("1") || //$NON-NLS-1$
				akkord.substring(1).startsWith("2") || //$NON-NLS-1$
				akkord.substring(1).startsWith("3") || //$NON-NLS-1$
				akkord.substring(1).startsWith("4") || //$NON-NLS-1$
				akkord.substring(1).startsWith("5") || //$NON-NLS-1$
				akkord.substring(1).startsWith("6") || //$NON-NLS-1$
				akkord.substring(1).startsWith("7") || //$NON-NLS-1$
				akkord.substring(1).startsWith("8") || //$NON-NLS-1$
				akkord.substring(1).startsWith("9") || //$NON-NLS-1$
				akkord.substring(1).toLowerCase().startsWith("m") ) { //$NON-NLS-1$
					additional = akkord.substring(1);
					akkord = akkord.substring(0, 1);
			} else if ( akkord.substring(2).startsWith("1") || //$NON-NLS-1$
				akkord.substring(2).startsWith("2") || //$NON-NLS-1$
				akkord.substring(2).startsWith("3") || //$NON-NLS-1$
				akkord.substring(2).startsWith("4") || //$NON-NLS-1$
				akkord.substring(2).startsWith("5") || //$NON-NLS-1$
				akkord.substring(2).startsWith("6") || //$NON-NLS-1$
				akkord.substring(2).startsWith("7") || //$NON-NLS-1$
				akkord.substring(2).startsWith("8") || //$NON-NLS-1$
				akkord.substring(2).startsWith("9") || //$NON-NLS-1$
				akkord.substring(2).toLowerCase().startsWith("m") ) { //$NON-NLS-1$
					additional = akkord.substring(2);
					akkord = akkord.substring(0, 2);
			} else if ( akkord.substring(3).startsWith("1") || //$NON-NLS-1$
				akkord.substring(3).startsWith("2") || //$NON-NLS-1$
				akkord.substring(3).startsWith("3") || //$NON-NLS-1$
				akkord.substring(3).startsWith("4") || //$NON-NLS-1$
				akkord.substring(3).startsWith("5") || //$NON-NLS-1$
				akkord.substring(3).startsWith("6") || //$NON-NLS-1$
				akkord.substring(3).startsWith("7") || //$NON-NLS-1$
				akkord.substring(3).startsWith("8") || //$NON-NLS-1$
				akkord.substring(3).startsWith("9") || //$NON-NLS-1$
				akkord.substring(3).toLowerCase().startsWith("m") ) { //$NON-NLS-1$
					additional = akkord.substring(3);
					akkord = akkord.substring(0, 3);
			} else if ( akkord.substring(4).startsWith("1") || //$NON-NLS-1$
				akkord.substring(4).startsWith("2") || //$NON-NLS-1$
				akkord.substring(4).startsWith("3") || //$NON-NLS-1$
				akkord.substring(4).startsWith("4") || //$NON-NLS-1$
				akkord.substring(4).startsWith("5") || //$NON-NLS-1$
				akkord.substring(4).startsWith("6") || //$NON-NLS-1$
				akkord.substring(4).startsWith("7") || //$NON-NLS-1$
				akkord.substring(4).startsWith("8") || //$NON-NLS-1$
				akkord.substring(4).startsWith("9") || //$NON-NLS-1$
				akkord.substring(4).toLowerCase().startsWith("m") ) { //$NON-NLS-1$
					additional = akkord.substring(2);
					akkord = akkord.substring(0, 2);
			}
		} catch(Exception ex) {
			// naja, egal...
		}

					
		int pos = posInToene(akkord);
		int location = -1;
		if ( pos != -1 ) {
			location = (pos + halbtoene) % 12;
			if ( location < 0 ) {
				location += 12;
			}
			
			ret = toene[location][0] + additional;
			if ( additional.toLowerCase().startsWith("m") && !additional.toLowerCase().startsWith("maj") ) { //$NON-NLS-1$ //$NON-NLS-2$
				ret = ret.toLowerCase();
			}
			
			if ( !secondAccord.equals("") ) { //$NON-NLS-1$
				ret = ret + "/" + secondAccord; //$NON-NLS-1$
			}
		}
		
		return ret;
	}
	
	private static int posInToene(String akkord) {
		int ret = -1;
		OuterFor:
		for ( int i = 0; i < toene.length; i++ ) {
			for ( int a = 0; a < toene[i].length; a++ ) {
				if ( toene[i][a].equalsIgnoreCase(akkord) ) {
					ret = i;
					break OuterFor;
				}
			}
		}
		
		return ret;
	}
	
	public static void calculateScreenSizes() {
		// nur zum MERKEN...
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		// Get size of each screen
		for (int i=0; i < gs.length; i++) {
			DisplayMode dm = gs[i].getDisplayMode();
			int screenWidth = dm.getWidth();
			int screenHeight = dm.getHeight();
		}
	}
}
