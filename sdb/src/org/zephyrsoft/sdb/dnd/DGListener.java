package org.zephyrsoft.sdb.dnd;

import java.awt.datatransfer.*;
import java.awt.dnd.*;

import org.zephyrsoft.sdb.*;

/**
 * DGListener
 * a listener that will start the drag.
 * has access to top level's dsListener and dragSource
 * @see java.awt.dnd.DragGestureListener
 * @see java.awt.dnd.DragSource
 * @see java.awt.datatransfer.StringSelection      
 */
public class DGListener implements DragGestureListener {

	Object parent = null;
	
	public DGListener(Object parent) {
		this.parent = parent;
	}
	
	/**
	 * Start the drag if the operation is ok.
	 * uses java.awt.datatransfer.StringSelection to transfer
	 * the label's data
	 * @param e the event object
	 */
	public void dragGestureRecognized(DragGestureEvent e) {
	
		// if the action is ok we go ahead
		// otherwise we punt
		if ((e.getDragAction() & DnDConstants.ACTION_COPY) == 0)
			return ;
			
		// get the label's text and put it inside a Transferable
		// Transferable transferable = new StringSelection( DragLabel.this.getText() );
		Transferable transferable = null;
		if (parent instanceof GUI) {
			transferable = new SongTransferable( ((GUI)parent).getSelectedSong() );
		} else if (parent instanceof BeamerGUI) {
			transferable = new SongTransferable( ((BeamerGUI)parent).getSelectedSong() );
		}
		
		// now kick off the drag
		try {
			// initial cursor, transferrable, dsource listener
			if (parent instanceof GUI) {
				e.startDrag(DragSource.DefaultCopyNoDrop, transferable, ((GUI)parent).getDSListener());
				((GUI)parent).beamerGuiToFront();
			} else if (parent instanceof BeamerGUI) {
				e.startDrag(DragSource.DefaultCopyNoDrop, transferable, ((BeamerGUI)parent).getDSListener());
			}
			
			// or if dragSource is a variable
			// dragSource.startDrag(e, DragSource.DefaultCopyDrop, transferable, dsListener);
			
			
			// or if you'd like to use a drag image if supported
			
			/*
			  if(DragSource.isDragImageSupported() )
			  // cursor, image, point, transferrable, dsource listener	
			  e.startDrag(DragSource.DefaultCopyDrop, image, point, transferable, dsListener);
			*/
			
		} catch ( InvalidDnDOperationException idoe ) {
			System.err.println( idoe );
		}
	}
}

