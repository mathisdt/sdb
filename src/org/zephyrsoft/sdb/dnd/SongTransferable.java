package org.zephyrsoft.sdb.dnd;

import java.awt.datatransfer.*;
import java.io.*;

import org.zephyrsoft.sdb.structure.*;

public class SongTransferable implements Transferable, Serializable {


	private final static DataFlavor[] FLAVORS = {
				new SongDataFlavor()
			};
	        
	private Song song = null;
	
	public SongTransferable(Song song) {
		this.song = song;
		
	}
	
	public Song getSong() {
		return song;
	}
	
	public synchronized Object getTransferData(DataFlavor flavor)
	throws UnsupportedFlavorException, IOException {
		if (flavor instanceof SongDataFlavor) {
			return this.song;
		} else {
			throw new UnsupportedFlavorException (flavor);
		}
	}
	
	public synchronized DataFlavor[]
	getTransferDataFlavors() {
		return FLAVORS;
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor instanceof SongDataFlavor);
	}
}