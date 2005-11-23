package org.zephyrsoft.util;

/** Tools, die in der String-Klasse fehlen. */
public class StringTools {
	
	public static String replace(String in, String toreplace, String replacewith) {
		String ret = new String(in.toString());
		while (ret.indexOf(toreplace)>=0) {
			ret = ret.substring(0, ret.indexOf(toreplace)) +
				replacewith +
				ret.substring(ret.indexOf(toreplace) + toreplace.length());
		}
		return ret;
	}
	
	public static String repeat(String torepeat, int count) {
		String ret = ""; //$NON-NLS-1$
		for ( int i = 0; i < count; i++ ) {
			ret += torepeat;
		}
		return ret;
	}
	
	public static String rightTrim(String in) {
		return "X".concat(in).trim().substring(1); //$NON-NLS-1$
	}
	
	public static boolean containsIgnoreCase(String lang, String kurz) {
		if ( kurz.length() > lang.length() ) {
			return false;
		}
		kurz = kurz.toLowerCase();
		lang = lang.toLowerCase();
		return ( lang.indexOf(kurz) > -1 );
	}
}