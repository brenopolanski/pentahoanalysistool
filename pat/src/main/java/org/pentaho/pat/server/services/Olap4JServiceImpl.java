/*
 */

package org.pentaho.pat.server.services;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.server.spring.GWTSpringController;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapWrapper;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.pentaho.pat.server.services.Messages;
import org.pentaho.pat.server.services.ObjectNotInCacheException;
import org.pentaho.pat.client.services.Olap4JService;
import org.pentaho.pat.client.util.OlapData;
import org.pentaho.pat.client.util.StringTree;

import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.query.Selection;



/**
 * @author Tom Barber
 *
 */
@SuppressWarnings("serial")
public class Olap4JServiceImpl extends GWTSpringController implements Olap4JService {
//public class Olap4JServiceImpl extends SpringGWTServlet implements Olap4JService {
	  protected static final Double ZERO_THRESHOLD = 1.2346E-8;

	  protected static HashMap<String, OlapConnection> connectionCache = new HashMap<String, OlapConnection>();

	  protected static HashMap<OlapConnection, Cube> cubeCache = new HashMap<OlapConnection, Cube>();

	  protected static HashMap<Cube, Query> queryCache = new HashMap<Cube, Query>();
	  
	public Olap4JServiceImpl() {
	    super();
	  }
	
	
	  public Boolean connect(String connectStr, String guid) {
		    OlapConnection connection;
		    try {
		      Class.forName("mondrian.olap4j.MondrianOlap4jDriver"); //$NON-NLS-1$
		      connection = (OlapConnection) DriverManager.getConnection(connectStr);
		      OlapWrapper wrapper = connection;
		      OlapConnection olapConnection = wrapper.unwrap(OlapConnection.class);
		      if (olapConnection != null) {
		        connectionCache.put(guid, olapConnection);
		        return true;
		      } else {
		        return false;
		      }
		    } catch (ClassNotFoundException e) {
		      e.printStackTrace();
		      return false;
		    } catch (SQLException e) {
		      e.printStackTrace();
		      return false;
		    }
		  }
	  
	  public Boolean disconnect(String guid) {
		    OlapConnection connection = connectionCache.get(guid);
		    if (connection != null) {
		      try {
		        connection.close();
		      } catch (SQLException e) {
		        e.printStackTrace();
		      }
		    }
		    queryCache.remove(cubeCache.remove(connectionCache.remove(guid)));
		    return true;
		  }
	  
	  public Boolean setCube(String cubeName, String guid) {
		    OlapConnection connection = connectionCache.get(guid);
		    if (connection == null) {
		      return new Boolean(false);
		    }
		    
		    try {
		        NamedList<Cube> cubes = connection.getSchema().getCubes();
		        Cube cube = null;
		        Iterator<Cube> iter = cubes.iterator();
		        while (iter.hasNext() && cube == null) {
		          Cube testCube = iter.next();
		          if (cubeName.equals(testCube.getName())) {
		            cube = testCube;
		          }
		        }
		        if (cube != null) {
		          cubeCache.put(connection, cube);
		          return new Boolean(true);
		        }
		        return new Boolean(false);
		      } catch (OlapException e) {
		        e.printStackTrace();
		        return new Boolean(false);
		      }
		    }
	  
	  public String[] getDimensions(String axis, String guid) {

		    Cube cube;
		    try {
		      cube = getCube4Guid(guid);
		    } catch (ObjectNotInCacheException e1) {
		      // TODO Auto-generated catch block
		      e1.printStackTrace();
		      return new String[0];
		    }

		    Query query = queryCache.get(cube);
		    if (query == null) {
		      try {
		        query = new Query(guid, cube);
		        queryCache.put(cube, query);
		      } catch (SQLException e) {
		        e.printStackTrace();
		        return new String[0];
		      }
		    }

		    Axis targetAxis = null;
		    if (!axis.equalsIgnoreCase("none")) { //$NON-NLS-1$
		      targetAxis = Axis.valueOf(axis);
		    }

		    List<QueryDimension> dimList = query.getAxes().get(targetAxis).getDimensions();
		    String[] dimNames = new String[dimList.size()];
		    for (int i = 0; i < dimList.size(); i++) {
		      dimNames[i] = dimList.get(i).getName();
		    }
		    return dimNames;
		  }
	  
	  public String[][] getCubes(String guid) {
		    OlapConnection connection = connectionCache.get(guid);
		    if (connection == null) {
		      return new String[0][];
		    }
		    try {
		      NamedList<Cube> cubes = connection.getSchema().getCubes();
		      String[][] cubeNames = new String[cubes.size()][2];
		      for (int i = 0; i < cubes.size(); i++) {
		        Cube cube = cubes.get(i);
		        cubeNames[i][0] = ""+i;
		        cubeNames[i][1] = cube.getName();
		      }
		      return cubeNames;
		    } catch (OlapException e) {
		      e.printStackTrace();
		    }
		    return null;
		  }
	  
	  
	  // Private utility methods
	  private Cube getCube4Guid(String guid) throws ObjectNotInCacheException {
	    OlapConnection connection = connectionCache.get(guid);
	    if (connection == null) {
	      throw new ObjectNotInCacheException(Messages.getString("Olap4JServiceImpl.OBJECT_NOT_IN_CACHE") + OlapConnection.class.toString() + Messages.getString("Olap4JServiceImpl.NO_KEY_FOUND") + guid); //$NON-NLS-1$ //$NON-NLS-2$
	    }

	    Cube cube = cubeCache.get(connection);
	    if (cube == null) {
	      throw new ObjectNotInCacheException(Messages.getString("Olap4JServiceImpl.OBJECT_NOT_IN_CACHE") + Cube.class.toString() + Messages.getString("Olap4JServiceImpl.NO_KEY_FOUND") + connection.toString()); //$NON-NLS-1$ //$NON-NLS-2$
	    }

	    return cube;
	  }
	 
	  public StringTree getMembers(String dimName, String guid) {

		    Query query = null;
		    try {
		      query = getQuery4Guid(guid);
		    } catch (ObjectNotInCacheException e1) {
		      // TODO Auto-generated catch block
		      e1.printStackTrace();
		    }

		    List<String> uniqueNameList = new ArrayList<String>();
		    NamedList<Level> levels = query.getDimension(dimName).getDimension().getHierarchies().get(dimName).getLevels();
		    for (Level level : levels) {
		      try {
		        List<Member> levelMembers = level.getMembers();
		        for (Member member : levelMembers) {
		          uniqueNameList.add(member.getUniqueName());
		        }
		      } catch (OlapException e) {
		        e.printStackTrace();
		      }
		    }
		    StringTree result = new StringTree(dimName, null);
		    for (int i = 0; i < uniqueNameList.size(); i++) {
		      String[] memberNames = uniqueNameList.get(i).split("\\."); //$NON-NLS-1$
		      for (int j = 0; j < memberNames.length; j++) { // Trim off the brackets
		        memberNames[j] = memberNames[j].substring(1, memberNames[j].length() - 1);
		      }
		      result = OlapUtil.parseMembers(memberNames, result);
		    }

		    return result;
		  }
	  private Query getQuery4Guid(String guid) throws ObjectNotInCacheException {
		    Cube cube;
		    try {
		      cube = getCube4Guid(guid);
		    } catch (ObjectNotInCacheException e) {
		      throw e;
		    }

		    Query query = queryCache.get(cube);
		    if (query == null) {
		      throw new ObjectNotInCacheException(Messages.getString("Olap4JServiceImpl.OBJECT_NOT_IN_CACHE") + Query.class.toString() + Messages.getString("Olap4JServiceImpl.NO_KEY_FOUND") + cube.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		    }

		    return query;
		  }
	  public Boolean moveDimension(String axisName, String DimName, String guid) {
		    Cube cube;
		    try {
		      cube = getCube4Guid(guid);
		    } catch (ObjectNotInCacheException e1) {
		      // TODO Auto-generated catch block
		      e1.printStackTrace();
		      return new Boolean(false);
		    }

		    Query query = queryCache.get(cube);
		    if (query == null) {
		      try {
		        query = new Query(guid, cube);
		        queryCache.put(cube, query);
		      } catch (SQLException e) {
		        e.printStackTrace();
		        return new Boolean(false);
		      }
		    }
		    Axis targetAxis = null;
		    if (!axisName.equalsIgnoreCase("none")) { //$NON-NLS-1$
		      targetAxis = Axis.valueOf(axisName);
		    }

		    query.getAxes().get(targetAxis).getDimensions().add(query.getDimension(DimName));

		    return new Boolean(true);
		  }
	  
	  public Boolean createSelection(String dimName, String[] memberNames, Integer selectionType, String guid) {
		    Cube cube = null;
		    try {
		      cube = getCube4Guid(guid);
		    } catch (ObjectNotInCacheException e1) {
		      e1.printStackTrace();
		      return new Boolean(false);
		    }

		    Query query = queryCache.get(cube);
		    if (query == null) {
		      return new Boolean(false);
		    }
		    try {
		      Member member = cube.lookupMember(memberNames);
		      QueryDimension qDim = OlapUtil.getQueryDimension(query, dimName);
		      Selection.Operator selectionMode = Selection.Operator.values()[selectionType.intValue()];
		      Selection selection = qDim.createSelection(member, selectionMode);
		      qDim.getSelections().add(selection);
		    } catch (OlapException e) {
		      e.printStackTrace();
		      return new Boolean(false);
		    }
		    return new Boolean(true);
		  }

		  /* (non-Javadoc)
		   * @see org.pentaho.halogen.client.services.Olap4JService#clearSelection(java.lang.String, java.lang.String[])
		   */
		  public Boolean clearSelection(String dimName, String[] memberNames, String guid) {
		    Query query = null;
		    try {
		      query = getQuery4Guid(guid);
		    } catch (ObjectNotInCacheException e) {
		      e.printStackTrace();
		      return new Boolean(false);
		    }

		    QueryDimension qDim = OlapUtil.getQueryDimension(query, dimName);
		    String path = OlapUtil.normalizeMemberNames(memberNames);
		    Selection selection = OlapUtil.findSelection(path, qDim);
		    if (selection == null) {
		      return new Boolean(false);
		    }
		    qDim.getSelections().remove(selection);
		    return new Boolean(true);
		  }

		  public OlapData executeQuery(String guid) {
			    CellSet results = null;
			    try {
			      results = getQuery4Guid(guid).execute();
			    } catch (OlapException e) {
			      e.printStackTrace();
			    } catch (ObjectNotInCacheException e) {
			      e.printStackTrace();
			    }
			    return OlapUtil.cellSet2OlapData(results);
			  }
}
