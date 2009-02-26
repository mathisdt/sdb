package org.zephyrsoft.sdb.dnd;

import java.awt.datatransfer.*;

import org.zephyrsoft.sdb.structure.*;

public class SongDataFlavor extends DataFlavor {

	public SongDataFlavor() {
		super((new SongTransferable(new Song( -3))).getClass(), "SongDataFlavor"); //$NON-NLS-1$
	}
	
	public String getHumanPresentableName() {
		return "SDB - Song"; //$NON-NLS-1$
	}
	
}