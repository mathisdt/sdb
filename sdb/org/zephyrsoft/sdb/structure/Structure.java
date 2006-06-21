package org.zephyrsoft.sdb.structure;

import java.io.*;
import java.util.*;

import javax.swing.*;

import org.zephyrsoft.sdb.*;
import org.zephyrsoft.util.*;

/**
 * Die Struktur, in der die Lieder zur Laufzeit gespeichert sind.
 *
 * @author   Mathis Dirksen-Thedens
 */
public class Structure implements Cloneable {

	private Vector songs = new Vector();
	private int nextid = 1;
	private String fileLoadedFrom = ""; //$NON-NLS-1$
	private GUI parentGUI = null;
	
	
	/** Konstruktor für die Structure */
	public Structure() {
		// nichts zu tun
	}
	
	/**
	 * Konstruktor für die Structure
	 *
	 * @param parent  Übergeordnete GUI
	 */
	public Structure(GUI parent)  {
		this.parentGUI = parent;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Structure ret = new Structure(null);
		for (int i = 0; i < songs.size(); i++) {
			ret.addSong((Song)songs.elementAt(i));
		}
		return ret;
	}
	
	public void sortByTitle() {
		Collections.sort(songs, new SongSorter(true));
	}
	
	public void sortByBegin() {
		Collections.sort(songs, new SongSorter(false));
	}
	
	public void setParent(GUI parent) {
		this.parentGUI = parent;
	}
	
	/**
	 * Formatiert die Daten zum Füllen eines JTable.
	 *
	 * @return   die Daten
	 */
	public String[][] getTableData() {
		boolean bst = false;
		try {
			bst = ((Boolean)parentGUI.getOptions().get("bst")).booleanValue(); //$NON-NLS-1$
		} catch(NullPointerException ex) {
			System.out.println(Messages.getString("Structure.2")); //$NON-NLS-1$
		}
		Vector lieder = new Vector();
		for (int i = 0; i < songs.size(); i++) {
			Song song = (Song)songs.elementAt(i);
			String[] ret1 = new String[4];
			ret1[0] = getTitleOf(song, bst);  //song.getTitel();
			ret1[1] = song.getSprache();
			ret1[2] = song.getTonart();
			ret1[3] = String.valueOf(song.getID());
			lieder.addElement(ret1);
			if (bst) {
				String[] ret2 = new String[4];
				ret2[0] = getTitleOf(song, !bst);
				ret2[1] = song.getSprache();
				ret2[2] = song.getTonart();
				ret2[3] = String.valueOf(song.getID());
				if (!ret1[0].equalsIgnoreCase(ret2[0]) && !ret1[0].toLowerCase().startsWith(ret2[0].toLowerCase())) {
					lieder.addElement(ret2);
				}
			}
		}
		String[][] ret = new String[lieder.size()][4];
		for (int i = 0;i < lieder.size();i++) {
			String[] elem = (String[])lieder.elementAt(i);
			ret[i][0] = elem[0];
			ret[i][1] = elem[1];
			ret[i][2] = elem[2];
			ret[i][3] = elem[3];
		}
		return ret;
	}
	
	/**
	 * Formatiert die Daten zum Füllen eines JTable. Dabei wird nur
	 * zurückgegeben, was den Kriterien entspricht!
	 *
	 * @param titelFilter  Parameter
	 * @param textFilter   Parameter
	 * @return             die gefilterten Daten
	 */
	public String[][] getTableData(String titelFilter, String textFilter) {
		Vector lieder = new Vector();
		boolean bst = false;
		try {
			bst = ((Boolean)parentGUI.getOptions().get("bst")).booleanValue(); //$NON-NLS-1$
		} catch(NullPointerException ex) {
			System.out.println(Messages.getString("Structure.4")); //$NON-NLS-1$
		}
		for (int i = 0; i < songs.size(); i++) {
			Song song = (Song)songs.elementAt(i);
			if (StringTools.containsIgnoreCase(getTitleOf(song, bst), titelFilter) && StringTools.containsIgnoreCase(song.getText(), textFilter)) {
				String[] ret1 = new String[4];
				ret1[0] = getTitleOf(song, bst);
				ret1[1] = song.getSprache();
				ret1[2] = song.getTonart();
				ret1[3] = String.valueOf(song.getID());
				lieder.addElement(ret1);
				if (bst) {
					String[] ret2 = new String[4];
					ret2[0] = getTitleOf(song, !bst);
					ret2[1] = song.getSprache();
					ret2[2] = song.getTonart();
					ret2[3] = String.valueOf(song.getID());
					if (!ret1[0].equalsIgnoreCase(ret2[0]) && !ret1[0].toLowerCase().startsWith(ret2[0].toLowerCase())) {
						lieder.addElement(ret2);
					}
				}
			}
		}
		String[][] ret = new String[lieder.size()][4];
		for (int i = 0;i < lieder.size();i++) {
			String[] elem = (String[])lieder.elementAt(i);
			ret[i][0] = elem[0];
			ret[i][1] = elem[1];
			ret[i][2] = elem[2];
			ret[i][3] = elem[3];
		}
		return ret;
	}
	
	/**
	 * Holt alle Songs
	 *
	 * @return   die Songs als Vector
	 */
	public Vector getSongs() {
		return songs;
	}
	
	public static String getTitleOf(Song song, boolean bst) {
		if (bst) {
			return song.getFirstLine();
		} else {
			return song.getTitel();
		}
	}
	
	
	/**
	 * Holt alle Songs
	 *
	 * @return   die Songs als Vector
	 */
	public Vector getSongs(boolean bst) {
		Vector alle = new Vector();
		try {
			for (int i = 0; i < songs.size(); i++) {
				alle.addElement(((Song)songs.elementAt(i)).clone());
			}
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		for (int i = 0; i < alle.size(); i++) {
			Song dieser = (Song)alle.elementAt(i);
			dieser.setTitel(getTitleOf(dieser, bst));
		}
		return alle;
	}
	
	/**
	 * Erzeugt neuen Song, fügt ihn in die Structure ein und gibt ihn zurück.
	 *
	 * @return   der neue Song
	 */
	public Song insertNewSong() {
		Song ret = new Song(nextid++);
		songs.addElement(ret);
		return ret;
	}
	
	/**
	 * Fügt einen Song hinzu
	 *
	 * @param song  der hinzuzufügende Song
	 */
	public void addSong(Song song) {
		Song old = getSongByID(song.getID());
		try {
			if (old!=null) {
				song = (Song)song.clone();
				song.setID(nextid++);
			}
			if (song.getID() >= nextid) {
				nextid = song.getID()+1;
			}
			songs.addElement(song);
			//System.out.println("Song " + song.getTitel() + " - ID " + song.getID());
		} catch(CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Löscht einen Song
	 *
	 * @param song  der zu löschende Song 
	 */
	public void deleteSong(Song song) {
		songs.removeElement(song);
	}
	
	/**
	 * Fügt eine Kopie des Arguments in die Structure ein.
	 *
	 * @param songtocopy  zu kopierender Song
	 * @return            die Kopie
	 */
	public Song copySong(Song songtocopy) {
		try {
			Song newsong = (Song)songtocopy.clone();
			newsong.setID(nextid++);
			songs.addElement(newsong);
			return newsong;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Holt einen Song an einem bestimmten Index
	 *
	 * @param index  der Index
	 * @return       der Song
	 */
	public Song songAt(int index) {
		return (Song)songs.elementAt(index);
	}
	
	/**
	 * Sucht einen Song nach dessen ID-Nummer heraus.
	 *
	 * @param id  die ID
	 * @return    der gefundene Song
	 */
	public Song getSongByID(int id) {
		Song ret = null;
		for (int i = 0; i < songs.size(); i++) {
			if (((Song)songs.elementAt(i)).getID() == id) {
				ret = (Song)songs.elementAt(i);
			}
		}
		return ret;
	}
	
	/**
	 * Lädt die Structure aus einer Datei
	 *
	 * @param file  Datei
	 */
	public void loadFromFile(File file) {
		// aus Datei file laden (Datei enthält serialisierten Vektor!)
		if (file.exists() && file.isFile()) {
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
				songs = (Vector)in.readObject();
				Integer temp = (Integer)in.readObject();
				nextid = temp.intValue();
				in.close();
				fileLoadedFrom = file.getAbsolutePath();
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(parentGUI, Messages.getString("Structure.14") + file.getAbsolutePath(), Messages.getString("Structure.15"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	/**
	 * Speichert die Structure in eine Datei
	 *
	 * @param file  Datei
	 */
	public void saveToFile(File file) {
		// in Datei file speichern (serialisierten Vektor!)
		try {
			if (file.exists()) {
				file.delete();
			}
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(songs);
			out.writeObject(new Integer(nextid));
			out.flush();
			out.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(parentGUI, Messages.getString("Structure.16") + file.getAbsolutePath(), Messages.getString("Structure.17"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * Wie viele Songs hat diese Structure?
	 *
	 * @return   Anzahl
	 */
	public int getSongCount() {
		return songs.size();
	}
	
	/**
	 * die interne Position eines Songs herausfinden
	 *
	 * @param song  der zu findende Song
	 * @return      Position im Vector (intern)
	 */
	public int findSong(Song song) {
		for (int i = 0; i < songs.size(); i++) {
			if (((Song)songs.elementAt(i)).getID() == song.getID()) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Verschiebt einen Song um eine Stelle nach oben.
	 *
	 * @param index  Index
	 * @return       neuer Index
	 */
	public int moveUp(int index) {
		if (index > 0) {
			Song song = (Song)songs.elementAt(index);
			songs.removeElementAt(index);
			songs.add(index - 1, song);
			return index - 1;
		}
		return index;
	}
	
	/**
	 * Verschiebt einen Song um eine Stelle nach unten.
	 *
	 * @param index  Index
	 * @return       neuer Index
	 */
	public int moveDown(int index) {
		if (index < songs.size() - 1) {
			Song song = (Song)songs.elementAt(index);
			songs.removeElementAt(index);
			songs.add(index + 1, song);
			return index + 1;
		}
		return index;
	}
	
	/**
	 * löscht den Song bei dem Index
	 *
	 * @param index  der Index
	 */
	public void delete(int index) {
		deleteSong(songAt(index));
	}
}
