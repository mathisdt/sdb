package org.zephyrsoft.sdb;

import java.io.*;
import java.util.*;
import org.zephyrsoft.sdb.structure.*;
import org.zephyrsoft.util.*;

public class PrintSongList {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Structure structure = new Structure();
		
		try {
			File file = new File(args[0]);
			structure.loadFromFile(file);
		} catch (Exception ex) {
			// nothing to do
		}
		
		List<Object> songs = structure.getSongs();
		Collections.sort(songs, new SongSorter(true));
		for (Object songAsObject : songs) {
			System.out.println(formatSongData((Song)songAsObject));
		}
		
	}

	private static String formatSongData(Song song) {
		StringBuilder sb = new StringBuilder();
		sb.append(song.getTitel());
		sb.append(" [");
		sb.append(song.getID());
		sb.append("]");
		return sb.toString();
	}
	
}
