/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */

package org.pentaho.pat.client.i18n;

/**
 * The Message Interface
 * @created Mar 10, 2009 
 * @since 0.2.0
 * @author Tom Barber
 * 
 */

public interface GuiMessages extends com.google.gwt.i18n.client.Messages {

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
