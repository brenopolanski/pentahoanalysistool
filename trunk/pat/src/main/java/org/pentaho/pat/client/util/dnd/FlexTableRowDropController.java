package org.pentaho.pat.client.util.dnd;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;

public interface FlexTableRowDropController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onDrop(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	/**
	 * Fires on drop.
	 * 
	 * @param context
	 *            the context
	 */
	public abstract void onDrop(final DragContext context);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onEnter(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	/**
	 * Fires when the widget enters the drop zone.
	 * 
	 * @param context
	 *            the context
	 */
	public abstract void onEnter(final DragContext context);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onLeave(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	/**
	 * Fires on leaving the drop zone.
	 * 
	 * @param context
	 *            the context
	 */
	public abstract void onLeave(final DragContext context);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onMove(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	/**
	 * Fires when the widget is moved.
	 * 
	 * @param context
	 *            the context
	 */
	public abstract void onMove(final DragContext context);

	public abstract void onPreviewDrop(final DragContext context)
			throws VetoDragException;

}