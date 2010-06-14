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
 * 
 * @created Mar 10, 2009
 * @since 0.2.0
 * @author Tom Barber
 * 
 */

public interface IGuiMessages extends com.google.gwt.i18n.client.Messages {

    /**
     * Failed to get a list of cubes.
     * 
     * @param localizedMessage
     *            the localized message
     * 
     * @return the string
     */
    String failedCubeList(String localizedMessage);

    /**
     * Failed to get a list of dimensions.
     * 
     * @param localizedMessage
     *            the localized message
     * 
     * @return the string
     */
    String failedDimensionList(String localizedMessage);

    /**
     * Failed to set the dimension.
     * 
     * @param localizedMessage
     *            the localized message
     * 
     * @return the string
     */
    String failedDimensionSet(String localizedMessage);

    /**
     * Translated "Failed to Fetch Members: {0}.".
     * 
     * @param arg
     *            - the arg0
     * 
     * @return translated "Failed to Fetch Members: {0}."
     */
    String failedMemberFetch(String arg0);

    /**
     * Failed to set sessionID.
     * 
     * @param localizedMessage
     *            the localized message
     * 
     * @return the string
     */
    String failedSessionID(String localizedMessage);

    /**
     * Translated "Selection could not be cleared: {0}".
     * 
     * @param arg0
     *            the arg0
     * 
     * @return translated "Selection could not be cleared: {0}" no_selection_cleared
     */
    String noSelectionCleared(String arg0);

    /**
     * Translated "Selection mode could not be set: {0}".
     * 
     * @param arg0
     *            the arg0
     * 
     * @return translated "Selection mode could not be set: {0}" no_selection_set
     */
    String noSelectionSet(String arg0);

    /**
     * Translated "Unable to get data from server: {0}".
     * 
     * @param arg0
     *            the arg0
     * 
     * @return translated "Unable to get data from server: {0}" no_server_data
     */
    String noServerData(String arg0);

    /**
     * 
     *TODO JAVADOC
     * 
     * @param arg0
     * @return
     */
    String failedConnection(String arg0);

    String failedConnect(String arg0);
    /**
     * 
     *TODO JAVADOC
     * 
     * @param arg0
     * @return
     */
    String failedActiveConnection(String arg0);

    /**
     * 
     *TODO JAVADOC
     * 
     * @param arg0
     * @return
     */
    String failedDisconnection(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedDeleteConnection(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedLoadConnection(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedQuery(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedGetSelection(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedDeleteQuery(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedCreateQuery(String arg0);

    /**
     * 
     *TODO JAVADOC
     * 
     * @param arg0
     * @return
     */
    String failedPivot(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedGetSort();

    /**
     * 
     * @return
     */
    String unexpectedError();

    /**
     * 
     * @param item
     *            - Name of the item to delete
     * @return
     */
    String confirmDelete(String item);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedOpenQuery(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @param localizedMessage
     * @return
     */
    String failedGetQueryList(String localizedMessage);

    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String failedSaveQuery(String arg0);

    /**
     *TODO JAVADOC
     * 
     * @param localizedMessage
     * @return
     */
    String failedDrill(String localizedMessage);

    /**
     *TODO JAVADOC
     * 
     * @param localizedMessage
     * @return
     */
    String failedSetSortOrder(String localizedMessage);
    
    /**
     *TODO JAVADOC
     * 
     * @return
     */
    String schemaFileInvalid();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String failedNonEmpty();

    /**
     *TODO JAVADOC
     *
     * @return
     */
    String validationEmpty(String fields);

    String failedDrillThrough(String message);
    
    String confirmQueryOverwrite();
}
