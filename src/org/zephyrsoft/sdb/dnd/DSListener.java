package org.zephyrsoft.sdb.dnd;

import java.awt.dnd.*;

/**
 * DSListener
 * a listener that will track the state of the DnD operation
 * 
 * @see java.awt.dnd.DragSourceListener
 * @see java.awt.dnd.DragSource
 * @see java.awt.datatransfer.StringSelection      
 */
public class DSListener implements DragSourceListener {

	Object parent = null;
	
	public DSListener(Object parent) {
		this.parent = parent;
	}
	
	/**
	 * @param e the event
	 */
	public void dragDropEnd(DragSourceDropEvent e) {
		if ( e.getDropSuccess() == false ) {
			return ;
		}
		
		/*
		 * the dropAction should be what the drop target specified
		 * in acceptDrop
		 */
	}
	
	/**
	 * @param e the event
	 */
	public void dragEnter(DragSourceDragEvent e) {
		DragSourceContext context = e.getDragSourceContext();
		//intersection of the users selected action, and the source and target actions
		int myaction = e.getDropAction();
		if ( (myaction & DnDConstants.ACTION_COPY) != 0) {
			context.setCursor(DragSource.DefaultCopyDrop);
		} else {
			context.setCursor(DragSource.DefaultCopyNoDrop);
		}
	}
	/**
	 * @param e the event
	 */
	public void dragOver(DragSourceDragEvent e) {
		DragSourceContext context = e.getDragSourceContext();
		int sa = context.getSourceActions();
		int ua = e.getUserAction();
		int da = e.getDropAction();
		int ta = e.getTargetActions();
	}
	/**
	 * @param e the event
	 */
	public void dragExit(DragSourceEvent e) {
		DragSourceContext context = e.getDragSourceContext();
	}
	
	/**
	 * for example, press shift during drag to change to
	 * a link action
	 * @param e the event     
	 */
	public void dropActionChanged (DragSourceDragEvent e) {
		//context.setCursor(DragSource.DefaultCopyNoDrop);
	}
}