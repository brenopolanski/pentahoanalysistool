package org.pentaho.pat.client.i18n;


public interface PatMessages extends com.google.gwt.i18n.client.Messages {

	/**
	 * Translated "Welcome.  The current time is {0}.".
	 * 
	 * @return translated "Welcome.  The current time is {0}." 
	 * welcome
	 */
	String welcome(String arg0);
	

	/**
	 * Translated "Connection could not be established: {0}".
	 * 
	 * @return translated "Connection could not be established: {0}"
	 *         no_connection_param
	 */
	String noconnectionparam(String arg0);
	

	/**
	 * Translated "Selection mode could not be set: {0}".
	 * 
	 * @return translated "Selection mode could not be set: {0}"
	 *         no_selection_set
	 */
	String noselectionset(String arg0);
	

	/**
	 * Translated "Unable to get data from server: {0}".
	 * 
	 * @return translated "Unable to get data from server: {0}" 
	 * no_server_data
	 */
	String noserverdata(String arg0);
	
	/**
	 * Translated "Selection could not be cleared: {0}".
	 * 
	 * @return translated "Selection could not be cleared: {0}"
	 *         no_selection_cleared
	 */
	String noselectioncleared(String arg0);


	/**
	 *TODO JAVADOC
	 *
	 * @param localizedMessage
	 * @return
	 */
	String failedDimensionList(String localizedMessage);


	/**
	 *TODO JAVADOC
	 *
	 * @param localizedMessage
	 * @return
	 */
	String failedQueryCreate(String localizedMessage);


	/**
	 *TODO JAVADOC
	 *
	 * @param localizedMessage
	 * @return
	 */
	String noqueryset(String localizedMessage);


	/**
	 *TODO JAVADOC
	 *
	 * @param localizedMessage
	 * @return
	 */
	String failedSessionID(String localizedMessage);


	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	String failedDimensionSet(String localizedMessage);


	/**
	 *TODO JAVADOC
	 *
	 * @param localizedMessage
	 * @return
	 */
	String failedCubeList(String localizedMessage);
	

}
