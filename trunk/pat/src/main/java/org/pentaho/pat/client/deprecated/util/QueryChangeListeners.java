/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.deprecated.util;

import org.pentaho.pat.client.deprecated.events.SourcesQueryEvents;
import org.pentaho.pat.client.deprecated.listeners.QueryListener;
import org.pentaho.pat.client.deprecated.listeners.QueryListenerCollection;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class QueryChangeListeners implements SourcesQueryEvents{
	/** ConnectionListenerCollection Object. */
	
	private final QueryListenerCollection queryListeners = new QueryListenerCollection();
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pentaho.pat.client.deprecated.events.SourcesConnectionEvents#addConnectionListener
	 * (org.pentaho.pat.client.deprecated.listeners.ConnectionListener)
	 */
	/**
	 * Initialize the connectionListeners instance.
	 */
	public void addQueryListener(QueryListener listener) {
		queryListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.pat.client.events.SourcesConnectionEvents#
	 * removeConnectionListener
	 * (org.pentaho.pat.client.deprecated.listeners.ConnectionListener)
	 */
	/**
	 * Remove a connectionListener
	 */
	public void removeQueryListener(QueryListener listener) {
		this.queryListeners.remove(listener);
	}

	public QueryListenerCollection getQueryListeners() {
		return this.queryListeners;
	}
	
	public void clearAllQueryListeners() {
		this.queryListeners.clear();
	}

}