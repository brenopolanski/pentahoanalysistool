/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */

package org.pentaho.pat.client.i18n;

/**
 * The Interface PatMessages.
 */
public interface PatMessages extends com.google.gwt.i18n.client.Messages {

	/**
	 * Failed to get a list of cubes.
	 *
	 * @param localizedMessage the localized message
	 *
	 * @return the string
	 */
	String failedCubeList(String localizedMessage);


	/**
	 * Failed to get a list of dimensions.
	 *
	 * @param localizedMessage the localized message
	 *
	 * @return the string
	 */
	String failedDimensionList(String localizedMessage);


	/**
	 * Failed to set the dimension.
	 *
	 * @param localizedMessage the localized message
	 *
	 * @return the string
	 */
	String failedDimensionSet(String localizedMessage);


	/**
	 * Failed to create a query.
	 *
	 * @param localizedMessage the localized message
	 *
	 * @return the string
	 */
	String failedQueryCreate(String localizedMessage);

	/**
	 * Failed to set sessionID.
	 *
	 * @param localizedMessage the localized message
	 *
	 * @return the string
	 */
	String failedSessionID(String localizedMessage);


	/**
	 * Translated "Connection could not be established: {0}".
	 *
	 * @param arg0 the arg0
	 *
	 * @return translated "Connection could not be established: {0}"
	 * no_connection_param
	 */
	String noConnectionParam(String arg0);


	/**
	 * No Query Set
	 *
	 * @param localizedMessage the localized message
	 *
	 * @return the string
	 */
	String noQuerySet(String localizedMessage);


	/**
	 * Translated "Selection could not be cleared: {0}".
	 *
	 * @param arg0 the arg0
	 *
	 * @return translated "Selection could not be cleared: {0}"
	 * no_selection_cleared
	 */
	String noSelectionCleared(String arg0);


	/**
	 * Translated "Selection mode could not be set: {0}".
	 *
	 * @param arg0 the arg0
	 *
	 * @return translated "Selection mode could not be set: {0}"
	 * no_selection_set
	 */
	String noSelectionSet(String arg0);


	/**
	 * Translated "Unable to get data from server: {0}".
	 *
	 * @param arg0 the arg0
	 *
	 * @return translated "Unable to get data from server: {0}"
	 * no_server_data
	 */
	String noServerData(String arg0);


	/**
	 * Translated "Welcome.  The current time is {0}.".
	 *
	 * @param arg0 the arg0
	 *
	 * @return translated "Welcome.  The current time is {0}."
	 * welcome
	 */
	String welcome(String arg0);


	/**
	 * Translated "Failed to Fetch Members: {0}.".
	 *
	 * @param arg- the arg0
	 *
	 * @return translated "Failed to Fetch Members: {0}."
	 */
	String failedMemberFetch(String arg0);


}
