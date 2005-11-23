package org.zephyrsoft.sdb.dnd;

import java.awt.datatransfer.*;
import java.awt.dnd.*;

import org.zephyrsoft.sdb.*;
import org.zephyrsoft.sdb.structure.*;

/**
 * DTListener
 * a listener that tracks the state of the operation
 * @see java.awt.dnd.DropTargetListener
 * @see java.awt.dnd.DropTarget
 */
public class DTListener implements DropTargetListener {

	private BeamerGUI parent = null;
	
	public DTListener(BeamerGUI parent) {
		this.parent = parent;
	}
	
	/**
	 * Called by isDragOk
	 * Checks to see if the flavor drag flavor is acceptable
	 * @param e the DropTargetDragEvent object
	 * @return whether the flavor is acceptable
	 */
	private boolean isDragFlavorSupported(DropTargetDragEvent e) {
		return e.isDataFlavorSupported(new SongDataFlavor());
	}
	/**
	 * Called by drop
	 * Checks the flavors and operations
	 * @param e the DropTargetDropEvent object
	 * @return the chosen DataFlavor or null if none match
	 */
	private DataFlavor chooseDropFlavor(DropTargetDropEvent e) {
		if (e.isDataFlavorSupported(new SongDataFlavor())) {
			return new SongDataFlavor();
		}
		return null;
	}
	
	/**
	 * Called by dragEnter and dragOver
	 * Checks the flavors and operations
	 * @param e the event object
	 * @return whether the flavor and operation is ok
	 */
	private boolean isDragOk(DropTargetDragEvent e) {
		if (isDragFlavorSupported(e) == false) {
			return false;
		}
		
		// the actions specified when the source
		// created the DragGestureRecognizer
		//      int sa = e.getSourceActions();
		
		// the docs on DropTargetDragEvent rejectDrag says that
		// the dropAction should be examined
		int da = e.getDropAction();
		
		// we're saying that these actions are necessary
		if ((da & DnDConstants.ACTION_COPY) == 0)
			return false;
		return true;
	}
	
	/**
	 * start "drag under" feedback on component
	 * invoke acceptDrag or rejectDrag based on isDragOk
	 */
	public void dragEnter(DropTargetDragEvent e) {
		if (isDragOk(e) == false) {
			e.rejectDrag();
			return ;
		}
		e.acceptDrag(e.getDropAction());
	}
	
	/**
	 * continue "drag under" feedback on component
	 * invoke acceptDrag or rejectDrag based on isDragOk
	 */
	public void dragOver(DropTargetDragEvent e) {
		if (isDragOk(e) == false) {
			e.rejectDrag();
			return ;
		}
		e.acceptDrag(e.getDropAction());
	}
	
	public void dropActionChanged(DropTargetDragEvent e) {
		if (isDragOk(e) == false) {
			e.rejectDrag();
			return ;
		}
		e.acceptDrag(e.getDropAction());
	}
	
	public void dragExit(DropTargetEvent e) {}
	
	/**
	 * perform action from getSourceActions on
	 * the transferrable
	 * invoke acceptDrop or rejectDrop
	 * invoke dropComplete
	 * if its a local (same JVM) transfer, use StringTransferable.localStringFlavor
	 * find a match for the flavor
	 * check the operation
	 * get the transferable according to the chosen flavor
	 * do the transfer
	 */
	public void drop(DropTargetDropEvent e) {
	
		DataFlavor chosen = chooseDropFlavor(e);
		if (chosen == null) {
			e.rejectDrop();
			return ;
		}
		
		// the actual operation
		int da = e.getDropAction();
		// the actions that the source has specified with DragGestureRecognizer
		int sa = e.getSourceActions();
		
		if ( ( sa & DnDConstants.ACTION_COPY ) == 0 ) {
			e.rejectDrop();
			return ;
		}
		
		Object data = null;
		try {
			/*
			 * the source listener receives this action in dragDropEnd.
			 * if the action is DnDConstants.ACTION_COPY_OR_MOVE then
			 * the source receives MOVE!
			 */
			e.acceptDrop(DnDConstants.ACTION_COPY);
			// e.acceptDrop(DnDConstants.ACTION_MOVE);
			//e.acceptDrop(DnDConstants.ACTION_COPY);
			//e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			
			data = e.getTransferable().getTransferData(chosen);
			if (data == null)
				throw new NullPointerException();
		} catch ( Throwable t ) {
			t.printStackTrace();
			e.dropComplete(false);
			return ;
		}
		if (data instanceof Song ) {
			// Song in die Liste (ans Ende) aufnehmen, wenn die ID nicht schon vorhanden ist
			parent.addSong((Song)data);
		} else {
			e.dropComplete(false);
			return ;
		}
		e.dropComplete(true);
	}
	
}