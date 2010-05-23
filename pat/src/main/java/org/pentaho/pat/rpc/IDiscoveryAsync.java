/*
 * Copyright (C) 2009 Luc Boudreau
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
package org.pentaho.pat.rpc;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.LevelProperties;
import org.pentaho.pat.rpc.dto.MemberLabelItem;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines discovery operations methods.
 * 
 * @author Luc Boudreau
 */
public interface IDiscoveryAsync {

    void getDrivers(AsyncCallback<String[]> callback);

    void getDimensions(String sessionId, String queryId, IAxis axis, AsyncCallback<String[]> callback);

    void getDimensionList(String sessionId, String queryId, IAxis axis, AsyncCallback<ArrayList<MemberLabelItem>> callback);
    
    void getCubes(String sessionId, String connectionId, AsyncCallback<CubeItem[]> callback);

    void getMembers(String sessionId, String queryId, String dimensionName, AsyncCallback<StringTree> callback);
    
    void getAllLevelProperties(String sessionId, String queryId, String dimensionName, AsyncCallback<List<LevelProperties>> callback);
    
    void getNamedLevelProperties(String sessionId, String queryId, String dimensionName, String levelName, AsyncCallback<List<LevelProperties>> callback);

	void getHierarchies(String sessionID, String queryId, String dimensionName,	AsyncCallback<List<MemberLabelItem>> asyncCallback);

	void getLevels(String sessionID, String currQuery, String dimensionName, String hierarchyName, AsyncCallback<ArrayList<MemberLabelItem>> asyncCallback);
	
	void getLevelMembers(String sessionID, String currQuery, String dimensionName, String hierarchyName, String levelName, AsyncCallback<List<MemberLabelItem>> asyncCallback);

	void getMeasures(String sessionID, String currQuery, AsyncCallback<List<MemberLabelItem>> asyncCallback);

}
